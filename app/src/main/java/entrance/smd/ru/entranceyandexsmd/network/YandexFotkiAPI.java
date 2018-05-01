package entrance.smd.ru.entranceyandexsmd.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import entrance.smd.ru.entranceyandexsmd.models.YandexCollection;
import entrance.smd.ru.entranceyandexsmd.utils.ListenerWrapper;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class YandexFotkiAPI {

	private static final String BASE_URL_YANDEX_FOTKI = "http://api-fotki.yandex.ru/api/";
	private final ExecutorService executor = Executors.newSingleThreadExecutor();

	private final YandexFotkiService service;

	public YandexFotkiAPI() {
		final Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(BASE_URL_YANDEX_FOTKI)
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		service = retrofit.create(YandexFotkiService.class);
	}


	public ListenerWrapper<OnRequestCompleteListener<YandexCollection>>
	getCollection(@NonNull OnRequestCompleteListener<YandexCollection> listener,
	              @Nullable final String podDate) {

		final ListenerWrapper<OnRequestCompleteListener<YandexCollection>> wrapper = new ListenerWrapper<>(listener);
		final Call<YandexCollection> call = (podDate == null) ?
				service.getPhoto() : service.getPhoto(podDate);

		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					final Response<YandexCollection> response = call.execute();
					final YandexCollection body = response.body();

					final OnRequestCompleteListener<YandexCollection> listener = wrapper.getListener();
					if (listener != null) {
						listener.onSuccess(response, body);
						wrapper.unregister();
					}

				} catch (IOException | RuntimeException e) {
					final OnRequestCompleteListener listener = wrapper.getListener();
					if (listener != null) {
						listener.onFailure(e);
						wrapper.unregister();
					}
				}
			}
		});

		return wrapper;
	}


	public interface OnRequestCompleteListener<T> {

		void onSuccess(final Response<T> response, @Nullable final T body);

		void onFailure(final Exception exception);
	}
}
