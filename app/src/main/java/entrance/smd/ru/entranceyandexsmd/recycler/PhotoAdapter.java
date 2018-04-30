package entrance.smd.ru.entranceyandexsmd.recycler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import entrance.smd.ru.entranceyandexsmd.R;
import entrance.smd.ru.entranceyandexsmd.models.YandexPhoto;

public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	@SuppressWarnings("all")
	public static final int IMAGE_TYPE = 0;
	public static final int LOADING_TYPE = 1;
	private static final int MAX_DATA_SIZE = 500;

	@NonNull
	private ArrayList<YandexPhoto> dataset = new ArrayList<>();

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
		private LoadingHolder(View view) {
			super(view);
		}
	}


	public PhotoAdapter(@Nullable ArrayList<YandexPhoto> dataset) {
		if (dataset != null) {
			this.dataset = dataset;
		}
	}


	@Override
	public int getItemViewType(int position) {
		if (position >= dataset.size()) {
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
						.inflate(R.layout.image_holder, parent, false);
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
		if (holder instanceof PhotoHolder) {
			((PhotoHolder) holder).updateData(dataset.get(position));
		}
	}

	@Override
	public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
		if (holder instanceof PhotoHolder) {
			((PhotoHolder) holder).imageView.setImageDrawable(null);
		}
	}

	@Override
	public int getItemCount() {
		// One more for progress-bar in the footer
		return dataset.isEmpty() ? 0 : dataset.size() + 1;
	}

	@NonNull
	public ArrayList<YandexPhoto> getDataset() {
		return dataset;
	}

	public void addExtraData(@NonNull ArrayList<YandexPhoto> newDataset) {

		final Integer oldSize = dataset.size();
		dataset.addAll(newDataset);

		try {
			notifyItemRangeInserted(oldSize, newDataset.size());

			// Check size limit
			if (dataset.size() > MAX_DATA_SIZE) {
				final Integer lastDeletedIndex = dataset.size() - MAX_DATA_SIZE;
				dataset.subList(0, lastDeletedIndex).clear();

				notifyItemRangeRemoved(0, lastDeletedIndex);
			}

		} catch (IllegalStateException e) {
			// "Cannot call this method while RecyclerView is computing a layout or scrolling RecyclerView"
			// It's a very rare case produced by notifyItem* when a small amount of new data is loaded
			// and user often scrolls the screen. It does not disrupt the application.
			Log.w("Extra data", e);
		}
	}
}
