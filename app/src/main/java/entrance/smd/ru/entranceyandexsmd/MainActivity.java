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

// TODO:
// 1) Добавить дату в CardHolder (?)
// 2) Изменить всю аттрибутику с названием на "ФотоДня"
// 3) Parcelable
// 5) Заменить RelativeLayout на LinerLayout, чтобы изображение не налазило на текст?
// 6) Увеличить кол-во колонок в Portland
// 7) Сортировать полученный список по podDate
// 8) Обертки для Listener'ов, чтобы избежать утечек памяти
// 9) Тесты блэт
// 10) Значочки-хуечки, иконки-поебонки
// 11) Удалить лишние поля из моделей
// 12) Не пересоздавать второй фрагмент, а прятать его (там слишком много findViewById)
// 13) Изменить placeholder для picasso и добавить икону на случай фейла
// 14) Добавить кнопки действий на ActionBar
