package entrance.smd.ru.entranceyandexsmd.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class YandexImage {

	@SerializedName("href")
	private String url;
	@SerializedName("width")
	private Integer width;
	@SerializedName("height")
	private Integer height;

	public String getUrl() {
		return url;
	}

	public Integer getWidth() {
		return width;
	}

	public Integer getHeight() {
		return height;
	}

}
