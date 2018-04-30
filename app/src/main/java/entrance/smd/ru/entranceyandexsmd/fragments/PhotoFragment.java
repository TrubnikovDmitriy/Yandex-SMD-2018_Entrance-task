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

	@Inject
	YandexFotkiAPI yandexAPI;

	@BindView(R.id.photo_image)
	ImageView image;
	@BindView(R.id.photo_title)
	TextView title;
	@BindView(R.id.photo_author)
	TextView author;


	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.fragment_photo, container, false);
		ButterKnife.bind(this, view);
		App.getComponent().inject(this);

		final Bundle bundle = (savedInstanceState == null) ? getArguments() : savedInstanceState;
		if (bundle == null) {
			throw new IllegalArgumentException("Bundle is null");
		}

		yandexPhoto = (YandexPhoto) bundle.getSerializable(PHOTO);
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

		return view;
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		outState.putSerializable(PHOTO, yandexPhoto);
		super.onSaveInstanceState(outState);
	}
}
