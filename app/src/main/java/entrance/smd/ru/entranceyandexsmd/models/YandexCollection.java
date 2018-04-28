package entrance.smd.ru.entranceyandexsmd.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class YandexCollection {

	@SerializedName("title")
	private String title;
	@SerializedName("entries")
	private ArrayList<YandexPhoto> arrayList;

	public String getTitle() {
		return title;
	}

	public ArrayList<YandexPhoto> getArrayList() {
		return arrayList;
	}
}
