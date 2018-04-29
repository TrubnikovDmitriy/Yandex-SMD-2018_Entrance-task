package entrance.smd.ru.entranceyandexsmd.recycler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import entrance.smd.ru.entranceyandexsmd.R;
import entrance.smd.ru.entranceyandexsmd.models.YandexCollection;
import entrance.smd.ru.entranceyandexsmd.models.YandexPhoto;


public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	public static final int IMAGE_TYPE = 0;
	public static final int LOADING_TYPE = 1;

	@Nullable
	private YandexCollection dataset;

	private class PhotoHolder extends RecyclerView.ViewHolder {

		private final ImageView imageView;
		private final Picasso picasso;
		private YandexPhoto photo;

		private PhotoHolder(CardView item) {
			super(item);
			picasso = Picasso.with(item.getContext());
			picasso.setIndicatorsEnabled(true);
			imageView = item.findViewById(R.id.image);
		}

		private void updateData(@NonNull final YandexPhoto photo) {

			this.photo = photo;
			imageView.setContentDescription(photo.getTitle());

			picasso.load(photo.getSmallUrl())
					.placeholder(R.mipmap.placeholder)
					.fit()
					.centerCrop()
					.into(imageView);
		}
	}

	private class LoadingHolder extends RecyclerView.ViewHolder {

		private final ProgressBar progressBar;

		private LoadingHolder(View view) {
			super(view);
			this.progressBar = view.findViewById(R.id.loading);
		}
	}

	public PhotoAdapter(@Nullable YandexCollection dataset) {
		this.dataset = dataset;
	}


	@Override
	public int getItemViewType(int position) {
		if (dataset == null) {
			return IMAGE_TYPE;
		}
		if (position >= dataset.getPhotos().size()) {
			return LOADING_TYPE;
		}
		return IMAGE_TYPE;
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

		switch (viewType) {

			case IMAGE_TYPE:
				final CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
						.inflate(R.layout.photo_holder, parent, false);
				return new PhotoHolder(cardView);

			case LOADING_TYPE:
				final View bar = LayoutInflater.from(parent.getContext())
						.inflate(R.layout.loading_holder, parent, false);
				return new LoadingHolder(bar);

			default:
				throw new IllegalArgumentException("Invalid view type");
		}
	}



	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
		if (dataset == null) {
			return;
		}
		if (holder instanceof PhotoHolder) {
			((PhotoHolder) holder).updateData(dataset.getPhotos().get(position));
		}
	}

	@Override
	public int getItemCount() {
		// One more for footer progress-bar
		return dataset == null ? 0 : dataset.getPhotos().size() + 1;
	}

	public void updateDataset(@Nullable YandexCollection dataset) {
		this.dataset = dataset;
		notifyDataSetChanged();
	}


	public interface ExtraLoading {
		void OnExtraLoad(@NonNull YandexCollection oldCollection);
	}
}
