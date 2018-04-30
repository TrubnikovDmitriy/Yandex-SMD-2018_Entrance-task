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
		// "/home/dmitriy/Yandex/SMD/Entrance/app/build/generated/source/r/androidTest/debug/entrance/smd/ru/entranceyandexsmd/test/R.java"
		final Gson gson = new Gson();
	}
}
