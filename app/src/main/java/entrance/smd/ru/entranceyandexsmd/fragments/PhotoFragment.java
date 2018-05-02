package entrance.smd.ru.entranceyandexsmd.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import entrance.smd.ru.entranceyandexsmd.utils.TimeService;


public class PhotoFragment extends Fragment {

	public static final String PHOTO = "photo_bundle";
	public static final Long ANIMATION_DURATION = 500L;

	private YandexPhoto yandexPhoto;
	private AnimatorSet animatorHideTextContent;
	private AnimatorSet animatorShowTextContent;

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
		setHasOptionsMenu(true);

		// Gesture control (for fling-up and single-click)
		view.setOnTouchListener(new View.OnTouchListener() {
			final GestureDetectorCompat gestureDetector =
					new GestureDetectorCompat(getContext(), new PhotoGestureListener(view));

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				gestureDetector.onTouchEvent(event);
				view.performClick();
				return true;
			}

		});
		hideSystemUI(view);

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

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.photo, menu);

		// Created share action for sending images
		final MenuItem shareItem = menu.findItem(R.id.menu_item_share);
		final ShareActionProvider shareActionProvider =
				(ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

		final Intent shareIntent = new Intent()
				.setAction(Intent.ACTION_SEND)
				.setType("text/plain")
				.putExtra(Intent.EXTRA_TEXT, yandexPhoto.getLargeImageUrl());

		shareActionProvider.setShareIntent(shareIntent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return (item.getItemId() == R.id.menu_item_share);
	}

	public void fillContent(@Nullable final YandexPhoto yandexPhoto) {

		if (yandexPhoto == null) {
			throw new IllegalArgumentException("Photo is null");
		}

		Picasso.with(null)
				.load(yandexPhoto.getLargeImageUrl())
				.placeholder(R.drawable.photo_loading)
				.error(R.drawable.photo_error)
				.into(image);

		image.setContentDescription(yandexPhoto.getTitle());
		title.setText(yandexPhoto.getTitle());
		author.setText(yandexPhoto.getAuthor());
		date.setText(TimeService.parsePodDate(yandexPhoto.getPodDate()));
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


	private class PhotoGestureListener extends GestureDetector.SimpleOnGestureListener {

		private final Float MIN_VELOCITY_Y_FOR_SWIPE = -5_000f;
		private final View view;

		PhotoGestureListener(View view) {
			this.view = view;
		}

		// Returns to the previous fragment by flinging to up
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

			if (velocityY < MIN_VELOCITY_Y_FOR_SWIPE && getFragmentManager() != null) {
				getFragmentManager().popBackStack();
				return true;
			}

			return false;
		}

		// Switches to the full-screen mode by click
		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
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
			return true;
		}
	}
}
