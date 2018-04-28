package entrance.smd.ru.entranceyandexsmd.network;


import entrance.smd.ru.entranceyandexsmd.models.YandexCollection;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;


public interface YandexFotkiService {

	@Headers("Accept: application/json")
	@GET("top/")
	Call<YandexCollection> getPhoto();
}
