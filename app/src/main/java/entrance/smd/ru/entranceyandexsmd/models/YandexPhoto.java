package entrance.smd.ru.entranceyandexsmd.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


@SuppressWarnings("unused")
public class YandexPhoto implements Serializable {

	@SerializedName("author")
	private String author;
	@SerializedName("title")
	private String title;
	@SerializedName("podDate")
	private String podDate;
	@SerializedName("img")
	private YandexImages images;


	public String getAuthor() {
		return author;
	}

	public String getTitle() {
		return title;
	}

	public String getSmallImageUrl() {
		return images.small.getUrl();
	}

	public String getLargeImageUrl() {
		return images.large.getUrl();
	}

	public String getPodDate() {
		return podDate;
	}


	private static final class YandexImages implements Serializable {
		@SerializedName("L")
		private YandexImage small;
		@SerializedName("XXXL")
		private YandexImage large;
	}
}
