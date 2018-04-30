package entrance.smd.ru.entranceyandexsmd.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class YandexImage implements Parcelable {

	@SerializedName("href")
	private String url;

	private YandexImage(String url) {
		this.url = url;
	}

	String getUrl() {
		return url;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(url);
	}

	public static final Parcelable.Creator<YandexImage> CREATOR =
			new Parcelable.Creator<YandexImage>() {

		@Override
		public YandexImage createFromParcel(Parcel source) {
			return new YandexImage(source.readString());
		}

		@Override
		public YandexImage[] newArray(int size) {
			return new YandexImage[size];
		}
	};
}
