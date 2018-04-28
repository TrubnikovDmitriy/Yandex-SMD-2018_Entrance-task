package entrance.smd.ru.entranceyandexsmd.deserialization;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import entrance.smd.ru.entranceyandexsmd.R;
import entrance.smd.ru.entranceyandexsmd.models.YandexImage;

import static org.junit.Assert.*;



@RunWith(AndroidJUnit4.class)
public class YandexPhotoTest {

	// TODO: how to get resources from "./Entrance/app/src/androidTest/res/values/strings.xml"?

	@Test
	public void happyPath() {
		final Context context = InstrumentationRegistry.getTargetContext();
		final String photoJSON = context.getResources().getString(R.string.test_json);
		final Gson gson = new Gson();
		final YandexImage image = gson.fromJson(photoJSON, YandexImage.class);

		assertEquals(context.getResources().getString(R.string.test_width), String.valueOf(image.getWidth()));
		assertEquals(context.getResources().getString(R.string.test_height), String.valueOf(image.getHeight()));
		assertEquals(context.getResources().getString(R.string.test_href), image.getUrl());
	}
}
