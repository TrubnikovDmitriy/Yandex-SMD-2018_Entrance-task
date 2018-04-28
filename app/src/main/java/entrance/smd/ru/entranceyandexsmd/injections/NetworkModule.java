package entrance.smd.ru.entranceyandexsmd.injections;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import entrance.smd.ru.entranceyandexsmd.network.YandexFotkiAPI;


@Module
public class NetworkModule {

	@Provides
	@Singleton
	YandexFotkiAPI getYandexAPI() {
		return new YandexFotkiAPI();
	}
}
