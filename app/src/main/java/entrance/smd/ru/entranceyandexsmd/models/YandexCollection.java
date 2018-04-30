package entrance.smd.ru.entranceyandexsmd.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


@SuppressWarnings("unused")
public class YandexCollection {

	@SerializedName("entries")
	private ArrayList<YandexPhoto> photos;

	public ArrayList<YandexPhoto> getPhotos() {
		return photos;
	}
}
