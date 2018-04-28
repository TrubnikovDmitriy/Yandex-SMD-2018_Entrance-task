package entrance.smd.ru.entranceyandexsmd;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import entrance.smd.ru.entranceyandexsmd.models.YandexCollection;
import entrance.smd.ru.entranceyandexsmd.network.YandexFotkiAPI;
import entrance.smd.ru.entranceyandexsmd.recycler.PhotoAdapter;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

	public static final int SPAN_GRID_COUNT = 2;

	@Inject
	YandexFotkiAPI yandexAPI;

	@BindView(R.id.progress_bar)
	ProgressBar progressBar;
	@BindView(R.id.recycler)
	RecyclerView recyclerView;

	private PhotoAdapter adapter;
	private YandexCollection collection;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		App.getComponent().inject(this);
		ButterKnife.bind(this);

		progressBar.setVisibility(ProgressBar.VISIBLE);
		if (savedInstanceState == null) {
			// TODO: weak references
			yandexAPI.getCollection(new OnYandexCollectionLoad());
		}

		// TODO: fix size of adapter's holder
		adapter = new PhotoAdapter(collection);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new GridLayoutManager(this, SPAN_GRID_COUNT));
	}

	private final class OnYandexCollectionLoad implements
			YandexFotkiAPI.OnRequestCompleteListener<YandexCollection> {

		private final Handler handler = new Handler(getMainLooper());

		@Override
		public void onSuccess(final Response<YandexCollection> response,
		                      @Nullable final YandexCollection body) {

			if (response.isSuccessful() && body != null) {
				collection = body;
				handler.post(new Runnable() {
					@Override
					public void run() {
						progressBar.setVisibility(ProgressBar.INVISIBLE);
						adapter.updateDataset(collection);
						Toast.makeText(getApplicationContext(),
								"Success load", Toast.LENGTH_LONG).show();
					}
				});

			} else {
				handler.post(new Runnable() {
					@Override
					public void run() {
						progressBar.setVisibility(ProgressBar.INVISIBLE);
						Toast.makeText(getApplicationContext(),
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
					Toast.makeText(getApplicationContext(),
							R.string.network_err, Toast.LENGTH_LONG).show();
				}
			});
		}
	}
}
