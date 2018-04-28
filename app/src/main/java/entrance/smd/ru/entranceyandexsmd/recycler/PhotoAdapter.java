package entrance.smd.ru.entranceyandexsmd.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import dagger.Provides;
import entrance.smd.ru.entranceyandexsmd.App;
import entrance.smd.ru.entranceyandexsmd.R;
import entrance.smd.ru.entranceyandexsmd.models.YandexCollection;
import entrance.smd.ru.entranceyandexsmd.models.YandexPhoto;


public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

	@Nullable
	private YandexCollection dataset;

	class PhotoHolder extends RecyclerView.ViewHolder {

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
					.into(imageView);
		}
	}

	public PhotoAdapter(@Nullable YandexCollection dataset) {
		this.dataset = dataset;
	}

	@NonNull
	@Override
	public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		final CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
				.inflate(R.layout.photo_holder, parent, false);
		return new PhotoHolder(cardView);
	}

	@Override
	public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
		if (dataset == null) {
			return;
		}
		holder.updateData(dataset.getPhotos().get(position));
	}

	@Override
	public int getItemCount() {
		return dataset == null ? 0 : dataset.getPhotos().size();
	}

	public void updateDataset(@Nullable YandexCollection dataset) {
		this.dataset = dataset;
		notifyDataSetChanged();
	}

}
