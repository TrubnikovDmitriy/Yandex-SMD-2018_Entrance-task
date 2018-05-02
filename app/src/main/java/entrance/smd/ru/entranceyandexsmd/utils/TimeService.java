package entrance.smd.ru.entranceyandexsmd.utils;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeService {

	private final static DateFormat dateParser =
			new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ROOT);
	private final static DateFormat dateFormatter =
			new SimpleDateFormat("dd-MM-yyyy", Locale.ROOT);

	@Nullable
	public static String parsePodDate(@NonNull final String format) {
		try {
			final Date date = dateParser.parse(format);
			return dateFormatter.format(date);
		} catch (ParseException e) {
			Log.w("Error parse podDate", e);
			return null;
		}
	}
}
