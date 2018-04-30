package entrance.smd.ru.entranceyandexsmd.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import entrance.smd.ru.entranceyandexsmd.App;
import entrance.smd.ru.entranceyandexsmd.R;
import entrance.smd.ru.entranceyandexsmd.models.YandexCollection;
import entrance.smd.ru.entranceyandexsmd.network.YandexFotkiAPI;
import entrance.smd.ru.entranceyandexsmd.recycler.PhotoAdapter;
import retrofit2.Response;


public class PhotoListFragment extends Fragment {

	public static final String COLLECTION = "collection_data_bundle";

	private PhotoAdapter adapter;
	private GridLayoutManager layoutManager;

	@Inject
	YandexFotkiAPI yandexAPI;

	@BindView(R.id.progress_bar)
	ProgressBar progressBar;

	@BindView(R.id.recycler)
	RecyclerView recyclerView;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.photo_list_fragment, container, false);
		ButterKnife.bind(this, view);
		App.getComponent().inject(this);

		progressBar.setVisibility(ProgressBar.VISIBLE);

		YandexCollection collection = null;
		if (savedInstanceState == null) {
			yandexAPI.getCollection(new OnYandexCollectionLoad());
		} else {
			collection = (YandexCollection) savedInstanceState.getSerializable(COLLECTION);
			progressBar.setVisibility(ProgressBar.INVISIBLE);
		}

		createRecyclerView(collection);
		return view;
	}


	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		outState.putSerializable(COLLECTION, adapter.getDataset());
		super.onSaveInstanceState(outState);
	}

	private void createRecyclerView(@Nullable final YandexCollection collection) {

		adapter = new PhotoAdapter(collection == null ? null : collection.getPhotos());
		recyclerView.setAdapter(adapter);
		recyclerView.setHasFixedSize(true);

		// This is necessary for the correct display of ProgressBar in the footer
		final Integer spanCount = getResources().getInteger(R.integer.span_grid_count);
		layoutManager = new GridLayoutManager(getContext(), spanCount);
		layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
			@Override
			public int getSpanSize(int position) {
				if (adapter.getItemViewType(position) == PhotoAdapter.LOADING_TYPE) {
					return spanCount;
				}
				// Default value
				return 1;
			}
		});
		recyclerView.setLayoutManager(layoutManager);

		// Loading additional data if RecyclerView is scrolled through
		recyclerView.addOnScrollListener(new OnEndlessScrollListener());
	}


	private final class OnYandexCollectionLoad implements YandexFotkiAPI.OnRequestCompleteListener<YandexCollection> {

		private final Handler handler = new Handler(Looper.getMainLooper());

		@Override
		public void onSuccess(final Response<YandexCollection> response,
		                      @Nullable final YandexCollection body) {

			if (response.isSuccessful() && body != null) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						progressBar.setVisibility(ProgressBar.INVISIBLE);
						adapter.addExtraData(body.getPhotos());
					}
				});

			} else {
				handler.post(new Runnable() {
					@Override
					public void run() {
						progressBar.setVisibility(ProgressBar.INVISIBLE);
						Toast.makeText(getContext(),
								R.string.network_failure, Toast.LENGTH_LONG).show();
					}
				});
			}
		}

		@Override
		public void onFailure(final Exception exception) {
			Log.w("Network exception", exception);
			handler.post(new Runnable() {
				@Override
				public void run() {
					progressBar.setVisibility(ProgressBar.INVISIBLE);
					Toast.makeText(getContext(),
							R.string.network_err, Toast.LENGTH_LONG).show();
				}
			});
		}
	}

	private final class OnEndlessScrollListener extends RecyclerView.OnScrollListener {

		private static final int START_LOADING_THRESHOLD = 2;
		private boolean isLoading = false;

		@Override
		public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

			if (isLoading) return;

			final Integer visibleItemCount = layoutManager.getChildCount();
			final Integer totalItemCount = layoutManager.getItemCount();
			final Integer firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

			if (totalItemCount - (firstVisibleItem + visibleItemCount) <= START_LOADING_THRESHOLD) {
				isLoading = true;
				yandexAPI.getCollection(new YandexFotkiAPI.OnRequestCompleteListener<YandexCollection>() {

							private final Handler handler = new Handler(Looper.getMainLooper());

							@Override
							public void onSuccess(final Response<YandexCollection> response,
							                      @Nullable final YandexCollection body) {
								if (response.isSuccessful() && body != null) {
									adapter.addExtraData(body.getPhotos());
								} else {
									handler.post(new Runnable() {
										@Override
										public void run() {
											progressBar.setVisibility(ProgressBar.INVISIBLE);
											Toast.makeText(getContext(),
													R.string.network_failure, Toast.LENGTH_LONG).show();
										}
									});
								}
								isLoading = false;
							}

							@Override
							public void onFailure(final Exception exception) {
								Log.w("Network exception", exception);
								handler.post(new Runnable() {
									@Override
									public void run() {
										progressBar.setVisibility(ProgressBar.INVISIBLE);
										Toast.makeText(getContext(),
												R.string.network_err, Toast.LENGTH_LONG).show();
									}
								});
								isLoading = false;
							}
						});
			}
		}
	}

}
