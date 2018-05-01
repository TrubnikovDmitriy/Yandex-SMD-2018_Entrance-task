package entrance.smd.ru.entranceyandexsmd.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import entrance.smd.ru.entranceyandexsmd.App;
import entrance.smd.ru.entranceyandexsmd.R;
import entrance.smd.ru.entranceyandexsmd.models.YandexPhoto;
import entrance.smd.ru.entranceyandexsmd.network.YandexFotkiAPI;


public class PhotoFragment extends Fragment {

	public static final String PHOTO = "photo_bundle";
	public static final Long ANIMATION_DURATION = 500L;

	private YandexPhoto yandexPhoto;
	private AnimatorSet animatorHideTextContent;
	private AnimatorSet animatorShowTextContent;
	private final DateFormat dateParser =
			new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ROOT);
	private final DateFormat dateFormatter =
			new SimpleDateFormat("dd-MM-yyyy", Locale.ROOT);

	@Inject YandexFotkiAPI yandexAPI;

	@BindView(R.id.photo_image) ImageView image;
	@BindView(R.id.photo_title) TextView title;
	@BindView(R.id.photo_author) TextView author;
	@BindView(R.id.photo_label_author) TextView labelAuthor;
	@BindView(R.id.photo_date) TextView date;
	@BindView(R.id.photo_label_date) TextView labelDate;


	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.fragment_photo, container, false);
		ButterKnife.bind(this, view);
		App.getComponent().inject(this);


		initTextAnimators();

		// Switching to full-screen mode by click
		view.setOnClickListener(new SwitchFullscreenModeListener());
		view.performClick();


		final Bundle bundle;
		if (savedInstanceState != null) {
			bundle = savedInstanceState;
		} else {
			if (getArguments() == null) {
				throw new IllegalArgumentException("Bundle is null");
			}
			bundle = getArguments();
		}

		yandexPhoto = bundle.getParcelable(PHOTO);
		fillContent(yandexPhoto);

		return view;
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		outState.putParcelable(PHOTO, yandexPhoto);
		super.onSaveInstanceState(outState);
	}


	public void fillContent(@Nullable final YandexPhoto yandexPhoto) {

		if (yandexPhoto == null) {
			throw new IllegalArgumentException("Argument is null");
		}

		Picasso.with(getContext())
				.load(yandexPhoto.getLargeImageUrl())
				.placeholder(R.drawable.photo_loading)
				.error(R.drawable.photo_error)
				.into(image);

		image.setContentDescription(yandexPhoto.getTitle());
		title.setText(yandexPhoto.getTitle());
		author.setText(yandexPhoto.getAuthor());
		date.setText(parsePodDate(yandexPhoto.getPodDate()));
	}

	private void showSystemUI(@NonNull final View view) {
		view.setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
		);
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

	private void initTextAnimators() {
		animatorHideTextContent = new AnimatorSet();
		animatorHideTextContent.playTogether(
				ObjectAnimator.ofFloat(title, View.ALPHA, 1f, 0f),
				ObjectAnimator.ofFloat(labelAuthor, View.ALPHA, 1f, 0f),
				ObjectAnimator.ofFloat(author, View.ALPHA, 1f, 0f),
				ObjectAnimator.ofFloat(labelDate, View.ALPHA, 1f, 0f),
				ObjectAnimator.ofFloat(date, View.ALPHA, 1f, 0f)
		);
		animatorHideTextContent.setDuration(ANIMATION_DURATION);

		animatorShowTextContent = new AnimatorSet();
		animatorShowTextContent.playTogether(
				ObjectAnimator.ofFloat(title, View.ALPHA, 0f, 1f),
				ObjectAnimator.ofFloat(labelAuthor, View.ALPHA, 0f, 1f),
				ObjectAnimator.ofFloat(author, View.ALPHA, 0f, 1f),
				ObjectAnimator.ofFloat(labelDate, View.ALPHA, 0f, 1f),
				ObjectAnimator.ofFloat(date, View.ALPHA, 0f, 1f)
		);
		animatorShowTextContent.setDuration(ANIMATION_DURATION);
	}

	@Nullable
	private String parsePodDate(@NonNull final String format) {
		try {
			final Date date = dateParser.parse(format);
			return dateFormatter.format(date);
		} catch (ParseException e) {
			Log.w("Error parse podDate", e);
			return null;
		}
	}


	class SwitchFullscreenModeListener implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			final Boolean visibleUI =
					(view.getSystemUiVisibility() & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;

			if (visibleUI) {
				hideSystemUI(view);
				if (animatorShowTextContent != null) {
					animatorShowTextContent.start();
				}
			} else {
				showSystemUI(view);
				if (animatorHideTextContent != null) {
					animatorHideTextContent.start();
				}
			}
		}
	}
}
