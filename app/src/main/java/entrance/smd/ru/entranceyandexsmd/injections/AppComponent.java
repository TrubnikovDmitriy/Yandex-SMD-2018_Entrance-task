package entrance.smd.ru.entranceyandexsmd.injections;


import javax.inject.Singleton;

import dagger.Component;
import entrance.smd.ru.entranceyandexsmd.MainActivity;


@Component(modules = { NetworkModule.class })
@Singleton
public interface AppComponent {

	void inject(MainActivity activity);
}
