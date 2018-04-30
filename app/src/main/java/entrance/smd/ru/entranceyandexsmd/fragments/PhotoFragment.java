package entrance.smd.ru.entranceyandexsmd.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import entrance.smd.ru.entranceyandexsmd.App;
import entrance.smd.ru.entranceyandexsmd.R;
import entrance.smd.ru.entranceyandexsmd.models.YandexPhoto;
import entrance.smd.ru.entranceyandexsmd.network.YandexFotkiAPI;

public class PhotoFragment extends Fragment {

	public static final String PHOTO = "photo_bundle";

	private YandexPhoto yandexPhoto;

	@Inject YandexFotkiAPI yandexAPI;

	@BindView(R.id.photo_image) ImageView image;
	@BindView(R.id.photo_title) TextView title;
	@BindView(R.id.photo_author) TextView author;


	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.fragment_photo, container, false);
		ButterKnife.bind(this, view);
		App.getComponent().inject(this);

		// Switching to full-screen mode by click
		view.setOnClickListener(new SwitchFullscreenModeListener());


		final Bundle bundle;
		if (savedInstanceState != null) {
			// Switch to fullscreen-mode after rotate
			view.performClick();
			bundle = savedInstanceState;

		} else {
			if (getArguments() == null) {
				throw new IllegalArgumentException("Bundle is null");
			}
			bundle = getArguments();
		}

		yandexPhoto = (YandexPhoto) bundle.getSerializable(PHOTO);
		fillContent(yandexPhoto);

		return view;
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		outState.putSerializable(PHOTO, yandexPhoto);
		super.onSaveInstanceState(outState);
	}


	public void fillContent(@Nullable final YandexPhoto yandexPhoto) {

		if (yandexPhoto == null) {
			throw new IllegalArgumentException("Argument is null");
		}

		Picasso.with(getContext())
				.load(yandexPhoto.getLargeUrl())
				.placeholder(R.mipmap.placeholder)
				.into(image);

		image.setContentDescription(yandexPhoto.getTitle());
		title.setText(yandexPhoto.getTitle());
		author.setText(yandexPhoto.getAuthor());
	}

	private void showSystemUI(@NonNull final View view) {
		view.setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
	}

	private void hideSystemUI(@NonNull final View view) {
		view.setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_FULLSCREEN
						| View.SYSTEM_UI_FLAG_LOW_PROFILE
						| View.SYSTEM_UI_FLAG_IMMERSIVE
		);
	}


	class SwitchFullscreenModeListener implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			final Boolean visibleUI =
					(view.getSystemUiVisibility() & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;

			if (visibleUI) {
				hideSystemUI(view);
			} else {
				showSystemUI(view);
			}
		}
	}
}
