package entrance.smd.ru.entranceyandexsmd.injections;

import javax.inject.Singleton;

import dagger.Component;
import entrance.smd.ru.entranceyandexsmd.fragments.PhotoListFragment;


@Component(modules = { NetworkModule.class })
@Singleton
public interface AppComponent {

	void inject(PhotoListFragment fragment);
}
