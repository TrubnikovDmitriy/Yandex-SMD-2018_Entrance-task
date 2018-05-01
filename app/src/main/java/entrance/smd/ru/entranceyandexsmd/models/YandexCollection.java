package entrance.smd.ru.entranceyandexsmd.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class YandexCollection {

	@SerializedName("entries")
	private ArrayList<YandexPhoto> photos;

	YandexCollection(ArrayList<YandexPhoto> photos) {
		this.photos = photos;
	}

	public ArrayList<YandexPhoto> getPhotos() {
		return photos;
	}
}
