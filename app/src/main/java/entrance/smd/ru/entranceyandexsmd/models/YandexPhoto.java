package entrance.smd.ru.entranceyandexsmd.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class YandexPhoto implements Serializable {
	// TODO: try make fields final
	// TODO: read about final in habrahabr guide

	@SerializedName("author")
	private String author;
	@SerializedName("title")
	private String title;
	@SerializedName("id")
	private String id;
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

	public String getId() {
		return id;
	}

	public String getSmallUrl() {
		return images.small.getUrl();
	}

	public String getMediumUrl() {
		return images.medium.getUrl();
	}

	public String getLargeUrl() {
		return images.large.getUrl();
	}

	public String getPodDate() {
		return podDate;
	}

	private static final class YandexImages implements Serializable {
		@SerializedName("L")
		private YandexImage small;
		@SerializedName("XXL")
		private YandexImage medium;
		@SerializedName("XXXL")
		private YandexImage large;
	}
}
