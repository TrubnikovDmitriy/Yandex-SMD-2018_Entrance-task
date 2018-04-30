package entrance.smd.ru.entranceyandexsmd.network;


import entrance.smd.ru.entranceyandexsmd.models.YandexCollection;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;


// API for Yandex.Fotki "Photo of the Day"
public interface YandexFotkiService {

	@Headers("Accept: application/json")
	@GET("podhistory/poddate/")
	Call<YandexCollection> getPhoto();

	@Headers("Accept: application/json")
	@GET("podhistory/poddate;{podDate}/")
	Call<YandexCollection> getPhoto(@Path("podDate") String podDate);

}
