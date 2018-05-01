package entrance.smd.ru.entranceyandexsmd.models;


import android.content.Context;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

import entrance.smd.ru.entranceyandexsmd.test.R;


public class TestModels {


	private String yandexCollectionJSON;
	private YandexCollection originalModel;
	private Integer photoCount;

	@Before
	public void setUp() {

		final Context context = InstrumentationRegistry.getContext();
		yandexCollectionJSON = context.getResources().getString(R.string.testModelsJSON);

		final String[] authors = context.getResources().getStringArray(R.array.testModelsAuthors);
		final String[] titles = context.getResources().getStringArray(R.array.testModelsTitles);
		final String[] podDates = context.getResources().getStringArray(R.array.testModelsPodDates);

		photoCount = context.getResources().getInteger(R.integer.testModelsImagesCount);

		final ArrayList<YandexPhoto> photos = new ArrayList<>(photoCount);
		for (int i = 0; i < photoCount; ++i) {
			final YandexPhoto yandexPhoto =
					new YandexPhoto(authors[i], titles[i], podDates[i], null);
			photos.add(yandexPhoto);
		}

		originalModel = new YandexCollection(photos);
	}

	@Test
	public void testDeserializationFromJSON() {

		final YandexCollection deserializeModel = new Gson()
				.fromJson(yandexCollectionJSON, YandexCollection.class);

		testEqualsYandexPhotos(deserializeModel.getPhotos(), originalModel.getPhotos());

		for (int i = 0; i < photoCount; ++i) {
			// Check inside arrays of Images
			assertNotNull(deserializeModel.getPhotos().get(i).getLargeImageUrl());
			assertNotNull(deserializeModel.getPhotos().get(i).getSmallImageUrl());
		}
	}

	@Test
	public void testParcelable() {

		final Parcel parcel = Parcel.obtain();
		final YandexPhoto originalPhoto = originalModel.getPhotos().get(0);

		// Write to parcel
		originalPhoto.writeToParcel(parcel, 0);

		// Reset parcel for reading
		parcel.setDataPosition(0);
		final YandexPhoto photoFromParcel = YandexPhoto.CREATOR.createFromParcel(parcel);

		assertEquals(originalPhoto.getTitle(), photoFromParcel.getTitle());
		assertEquals(originalPhoto.getPodDate(), photoFromParcel.getPodDate());
		assertEquals(originalPhoto.getAuthor(), photoFromParcel.getAuthor());

	}


	private void testEqualsYandexPhotos(@NonNull final ArrayList<YandexPhoto> testing,
	                                    @NonNull final ArrayList<YandexPhoto> original) {

		assertEquals((long) testing.size(), (long) photoCount);
		assertEquals(testing.size(), original.size());

		for (int i = 0; i < photoCount; ++i) {

			// Check order of photos
			assertEquals(
					testing.get(i).getAuthor(),
					original.get(i).getAuthor()
			);
			assertEquals(
					testing.get(i).getTitle(),
					original.get(i).getTitle()
			);
			assertEquals(
					testing.get(i).getTitle(),
					original.get(i).getTitle()
			);
		}
	}
}
