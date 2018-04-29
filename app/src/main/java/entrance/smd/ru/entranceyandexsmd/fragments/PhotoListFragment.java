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
import android.widget.TextView;
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
	private YandexCollection collection;

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

		if (savedInstanceState == null) {
			yandexAPI.getCollection(new OnYandexCollectionLoad());

		} else {
			collection = (YandexCollection) savedInstanceState.getSerializable(COLLECTION);
			progressBar.setVisibility(ProgressBar.INVISIBLE);
		}

		// TODO: fix size of adapter's holder
		adapter = new PhotoAdapter(collection);
		recyclerView.setAdapter(adapter);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new GridLayoutManager(
				getContext(), getResources().getInteger(R.integer.span_grid_count)));

		return view;
	}


	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		outState.putSerializable(COLLECTION, collection);
		super.onSaveInstanceState(outState);
	}

	private final class OnYandexCollectionLoad implements
			YandexFotkiAPI.OnRequestCompleteListener<YandexCollection> {

		private final Handler handler = new Handler(Looper.getMainLooper());

		@Override
		public void onSuccess(final Response<YandexCollection> response,
		                      @Nullable final YandexCollection body) {

			if (response.isSuccessful() && body != null) {
				collection = body;
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handler.post(new Runnable() {
					@Override
					public void run() {
						progressBar.setVisibility(ProgressBar.INVISIBLE);
						adapter.updateDataset(collection);
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
}
