package entrance.smd.ru.entranceyandexsmd.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class YandexImage implements Serializable {

	@SerializedName("href")
	private String url;

	String getUrl() {
		return url;
	}
}
