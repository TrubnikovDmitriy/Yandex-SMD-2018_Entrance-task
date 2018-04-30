package entrance.smd.ru.entranceyandexsmd.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;


public class YandexPhoto implements Parcelable {

	@SerializedName("author")
	private String author;
	@SerializedName("title")
	private String title;
	@SerializedName("podDate")
	private String podDate;
	@SerializedName("img")
	private YandexImages images;

	private YandexPhoto(String author, String title,
	                   String podDate, YandexImages images) {
		this.author = author;
		this.title = title;
		this.podDate = podDate;
		this.images = images;
	}


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


	private static final class YandexImages implements Parcelable {

		@SerializedName("L")
		private YandexImage small;
		@SerializedName("XXXL")
		private YandexImage large;

		private YandexImages(@NonNull YandexImage small,
		             @NonNull YandexImage large) {
			this.small = small;
			this.large = large;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeParcelable(small, flags);
			dest.writeParcelable(large, flags);
		}

		public static final Parcelable.Creator<YandexImages> CREATOR =
				new Parcelable.Creator<YandexImages>() {

					@Override
					public YandexImages createFromParcel(Parcel source) {
						final YandexImage small = source.readParcelable(YandexImage.class.getClassLoader());
						final YandexImage large = source.readParcelable(YandexImage.class.getClassLoader());
						return new YandexImages(small, large);
					}

					@Override
					public YandexImages[] newArray(int size) {
						return new YandexImages[size];
					}
				};
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(author);
		dest.writeString(title);
		dest.writeString(podDate);
		dest.writeParcelable(images, flags);
	}

	public static final Parcelable.Creator<YandexPhoto> CREATOR =
			new Parcelable.Creator<YandexPhoto>() {

				@Override
				public YandexPhoto createFromParcel(Parcel source) {
					final String author = source.readString();
					final String title = source.readString();
					final String podDate = source.readString();
					final YandexImages images =
							source.readParcelable(YandexImages.class.getClassLoader());

					return new YandexPhoto(author, title, podDate, images);
				}

				@Override
				public YandexPhoto[] newArray(int size) {
					return new YandexPhoto[size];
				}
			};
}
