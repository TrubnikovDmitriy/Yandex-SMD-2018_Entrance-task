package entrance.smd.ru.entranceyandexsmd;

import android.app.Application;

import com.squareup.picasso.Picasso;

import entrance.smd.ru.entranceyandexsmd.injections.AppComponent;
import entrance.smd.ru.entranceyandexsmd.injections.DaggerAppComponent;
import entrance.smd.ru.entranceyandexsmd.injections.NetworkModule;


public class App extends Application {

	private static AppComponent component;

	public static AppComponent getComponent() {
		return component;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		component = DaggerAppComponent.builder()
				.networkModule(new NetworkModule())
				.build();

		Picasso.with(getApplicationContext());
	}
}
