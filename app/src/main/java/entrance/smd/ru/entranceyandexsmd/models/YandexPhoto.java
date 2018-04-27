package entrance.smd.ru.entranceyandexsmd.models;


import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class YandexPhoto {
	// TODO: try make fields final
	// TODO: read about final in habrahabr guide

	@SerializedName("author")
	private String author;
	@SerializedName("title")
	private String title;
	@SerializedName("id")
	private String id;
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


	private static final class YandexImages {
		@SerializedName("S")
		private YandexImage small;
		@SerializedName("M")
		private YandexImage medium;
		@SerializedName("L")
		private YandexImage large;
	}
}
