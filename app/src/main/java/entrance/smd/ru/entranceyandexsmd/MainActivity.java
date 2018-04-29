package entrance.smd.ru.entranceyandexsmd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import entrance.smd.ru.entranceyandexsmd.fragments.PhotoListFragment;


public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.fragment_container, new PhotoListFragment())
					.commit();
		}
	}
}
