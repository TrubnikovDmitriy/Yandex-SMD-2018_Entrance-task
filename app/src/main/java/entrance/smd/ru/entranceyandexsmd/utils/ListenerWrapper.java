package entrance.smd.ru.entranceyandexsmd.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


// Simple wrapper to avoid memory-leaks.
// It is necessary for cases when activity is going to die
// but long request still keeps its context through listener.
public class ListenerWrapper<T> {

	private T listener;

	public ListenerWrapper(@NonNull T listener) {
		this.listener = listener;
	}

	@Nullable
	public T getListener() {
		return listener;
	}

	public void unregister() {
		listener = null;
	}
}
