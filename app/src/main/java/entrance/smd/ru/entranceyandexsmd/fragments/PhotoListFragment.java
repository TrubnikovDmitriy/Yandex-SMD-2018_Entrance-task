package entrance.smd.ru.entranceyandexsmd.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import entrance.smd.ru.entranceyandexsmd.App;
import entrance.smd.ru.entranceyandexsmd.R;
import entrance.smd.ru.entranceyandexsmd.models.YandexCollection;
import entrance.smd.ru.entranceyandexsmd.models.YandexPhoto;
import entrance.smd.ru.entranceyandexsmd.network.YandexFotkiAPI;
import entrance.smd.ru.entranceyandexsmd.recycler.PhotoAdapter;
import entrance.smd.ru.entranceyandexsmd.utils.ListenerWrapper;
import retrofit2.Response;


public class PhotoListFragment extends Fragment {

	public static final String DATASET = "collection_data_bundle";

	private PhotoAdapter adapter;
	private GridLayoutManager layoutManager;
	private final OnEndlessScrollListener scrollListener = new OnEndlessScrollListener();
	private final LinkedList<ListenerWrapper> wrappers = new LinkedList<>();

	@Inject YandexFotkiAPI yandexAPI;

	@BindView(R.id.progress_bar) ProgressBar progressBar;
	@BindView(R.id.recycler) RecyclerView recyclerView;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.fragment_photo_list, container, false);
		ButterKnife.bind(this, view);
		App.getComponent().inject(this);

		showSystemUI(container);
		progressBar.setVisibility(ProgressBar.VISIBLE);
		setHasOptionsMenu(true);

		ArrayList<YandexPhoto> dataset = null;

		if (savedInstanceState == null) {
			scrollListener.isLoading = true;
			final ListenerWrapper wrapper = yandexAPI.getCollection(new OnYandexCollectionLoad(), null);
			wrappers.add(wrapper);
		} else {
			dataset = savedInstanceState.getParcelableArrayList(DATASET);
			progressBar.setVisibility(ProgressBar.INVISIBLE);
		}

		createRecyclerView(dataset);
		return view;
	}


	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		outState.putParcelableArrayList(DATASET, adapter.getDataset());
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.photo_list, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

			case R.id.menu_item_update:
				// Prevents launch other loadings
				scrollListener.isLoading = true;
				// Cancel all current requests to avoid duplication of images
				for (ListenerWrapper wrapper : wrappers) {
					wrapper.unregister();
				}
				wrappers.clear();
				// Clearing dataset in recycler
				adapter.clearData();
				// Start new loading as first time
				progressBar.setVisibility(ProgressBar.VISIBLE);
				wrappers.add(yandexAPI.getCollection(new OnYandexCollectionLoad(), null));
				return true;

			default:
				return false;
		}
	}

	@Override
	public void onDestroy() {
		for (ListenerWrapper wrapper : wrappers) {
			wrapper.unregister();
		}
		super.onDestroy();
	}


	private void createRecyclerView(@Nullable final ArrayList<YandexPhoto> collection) {

		adapter = new PhotoAdapter(collection);
		adapter.setOnImageClickListenerListener(new OnYandexImageClickListener());

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
		recyclerView.addOnScrollListener(scrollListener);
	}

	// For return to normal view after exit from PhotoFragment
	private void showSystemUI(@Nullable final View view) {
		if (view != null) {
			view.setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
		}
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
						adapter.addExtraData(body.getPhotos());
						progressBar.setVisibility(ProgressBar.INVISIBLE);
						scrollListener.isLoading = false;
					}
				});

			} else {
				handler.post(new Runnable() {
					@Override
					public void run() {
						progressBar.setVisibility(ProgressBar.INVISIBLE);
						scrollListener.isLoading = false;
						Toast.makeText(getContext(),
								R.string.network_failure, Toast.LENGTH_LONG).show();
					}
				});
			}
		}

		@Override
		public void onFailure(final Exception exception) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					progressBar.setVisibility(ProgressBar.INVISIBLE);
					scrollListener.isLoading = false;
					Toast.makeText(getContext(),
							R.string.network_err, Toast.LENGTH_LONG).show();
				}
			});
		}
	}

	private final class OnYandexImageClickListener implements PhotoAdapter.OnImageClickListener {
		@Override
		public void onClick(@NonNull YandexPhoto photo) {

			if (getFragmentManager() != null) {

				final Bundle bundle = new Bundle();
				bundle.putParcelable(PhotoFragment.PHOTO, photo);

				final PhotoFragment photoFragment = new PhotoFragment();
				photoFragment.setArguments(bundle);

				getFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.photo_enter, R.anim.photo_exit, R.anim.photo_pop_enter, R.anim.photo_pop_exit)
						.hide(PhotoListFragment.this)
						.add(R.id.fragment_container, photoFragment)
						.addToBackStack(null)
						.commit();
			}
		}
	}

	private final class OnEndlessScrollListener extends RecyclerView.OnScrollListener {

		// How many photos have to remain that starting adapter_loading extra data
		private static final int START_LOADING_THRESHOLD = 2;
		private final Handler handler = new Handler(Looper.getMainLooper());
		private boolean isLoading = false;

		@Override
		public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

			if (isLoading) return;

			final Integer visibleItemCount = layoutManager.getChildCount();
			final Integer totalItemCount = layoutManager.getItemCount();
			final Integer firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

			// Check whether we are at the footer of RecyclerView
			if (totalItemCount - (firstVisibleItem + visibleItemCount) <= START_LOADING_THRESHOLD) {

				isLoading = true;
				final ArrayList<YandexPhoto> dataset = adapter.getDataset();

				if (dataset.isEmpty()) {
					// If first loading is failed and adapter hasn't any data
					final ListenerWrapper wrapper = yandexAPI.getCollection(
							new YandexFotkiAPI.OnRequestCompleteListener<YandexCollection>() {
						@Override
						public void onSuccess(Response<YandexCollection> response,
						                      @Nullable final YandexCollection body) {
							if (response.isSuccessful() && body != null) {
								adapter.addExtraData(body.getPhotos());
							} else {
								postToast(R.string.network_failure);
							}
							isLoading = false;
						}

						@Override
						public void onFailure(Exception exception) {
							postToast(R.string.network_err);
							isLoading = false;
						}
					}, null);
					wrappers.add(wrapper);
					return;
				}

				// Take the date of the last element in oldDataSet for pagination API
				final String podDate = dataset.get(dataset.size() - 1).getPodDate();
				final ListenerWrapper wrapper = yandexAPI.getCollection(
						new YandexFotkiAPI.OnRequestCompleteListener<YandexCollection>() {

							@Override
							public void onSuccess(final Response<YandexCollection> response,
							                      @Nullable final YandexCollection body) {

								if (response.isSuccessful() && body != null) {
									final ArrayList<YandexPhoto> newDataSet = body.getPhotos();

									if (newDataSet.isEmpty()) {
										postToast(R.string.empty_dataset);
									} else {
										// Need to remove the first item of newDataSet to avoid
										// duplicating the last image of the oldDataSet
										newDataSet.remove(0);
										adapter.addExtraData(newDataSet);
									}

								} else {
									postToast(R.string.network_failure);
								}
								isLoading = false;
							}

							@Override
							public void onFailure(final Exception exception) {
								postToast(R.string.network_err);
								isLoading = false;
							}

						}, podDate);
				wrappers.add(wrapper);
			}
		}

		private void postToast(@NonNull final Integer resourceID) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getContext(), resourceID, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
}
