package entrance.smd.ru.entranceyandexsmd;

import android.app.Application;

import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import entrance.smd.ru.entranceyandexsmd.injections.AppComponent;
import entrance.smd.ru.entranceyandexsmd.injections.DaggerAppComponent;
import entrance.smd.ru.entranceyandexsmd.injections.NetworkModule;


public class App extends Application {

	// Average size of small image is 540 kB (cache for 2-3 scrolls of screen)
	private static final Integer MAX_MEM_CACHE_SIZE_BYTES = 20_000_000;

	private static AppComponent component;

	public static AppComponent getComponent() {
		return component;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		// Creating Dagger's singleton
		component = DaggerAppComponent.builder()
				.networkModule(new NetworkModule())
				.build();

		// Tuning picasso mem-cache (disk cache is immutable - from 5 MB to 50 MB)
		final Picasso picasso = new Picasso.Builder(this)
				.memoryCache(new LruCache(MAX_MEM_CACHE_SIZE_BYTES))
				.build();
		Picasso.setSingletonInstance(picasso);
	}
}
