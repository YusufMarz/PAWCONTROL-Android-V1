package com.example.pawcontrolv1.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawcontrolv1.R;

import java.util.List;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.CatViewHolder> {

    public interface OnCatClickListener {
        void onCatClick(Cat cat, int position);
        void onFavouriteClick(Cat cat, int position);
    }

    private final Context context;
    private final List<Cat> catList;
    private OnCatClickListener listener;

    public CatAdapter(Context context, List<Cat> catList) {
        this.context = context;
        this.catList = catList;
    }

    public void setOnCatClickListener(OnCatClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_cat_card, parent, false);
        return new CatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatViewHolder holder, int position) {
        Cat cat = catList.get(position);
        holder.bind(cat, position);
    }

    @Override
    public int getItemCount() {
        return catList.size();
    }

    // ── Update list (e.g. when switching tabs) ──────────────────────────────
    public void updateList(List<Cat> newList) {
        catList.clear();
        catList.addAll(newList);
        notifyDataSetChanged();
    }

    // ── ViewHolder ──────────────────────────────────────────────────────────
    class CatViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivCatPhoto;
        private final ImageView ivFavourite;
        private final TextView tvCatName;
        private final TextView tvCatBreed;
        private final TextView tvCatAge;

        CatViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCatPhoto  = itemView.findViewById(R.id.ivCatPhoto);
            ivFavourite = itemView.findViewById(R.id.ivFavourite);
            tvCatName   = itemView.findViewById(R.id.tvCatName);
            tvCatBreed  = itemView.findViewById(R.id.tvCatBreed);
            tvCatAge    = itemView.findViewById(R.id.tvCatAge);
        }

        void bind(Cat cat, int position) {
            tvCatName.setText(cat.getName());
            tvCatBreed.setText(cat.getBreed());
            tvCatAge.setText(cat.getAge());

            // Photo — local drawable
            if (cat.getPhotoResId() != 0) {
                ivCatPhoto.setImageResource(cat.getPhotoResId());
            }
            // Photo — remote URL (uncomment if using Glide)
            // Glide.with(context).load(cat.getPhotoUrl()).into(ivCatPhoto);

            // Favourite tint toggle
            ivFavourite.setColorFilter(cat.isFavourite()
                    ? context.getColor(R.color.filter_bg_selected)   // #F67F47
                    : context.getColor(R.color.filter_bg_default));  // #CCCCCC

            // Whole card click
            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onCatClick(cat, position);
            });

            // Heart click — toggle favourite
            ivFavourite.setOnClickListener(v -> {
                cat.setFavourite(!cat.isFavourite());
                ivFavourite.setColorFilter(cat.isFavourite()
                        ? context.getColor(R.color.filter_bg_selected)
                        : context.getColor(R.color.filter_bg_default));
                if (listener != null) listener.onFavouriteClick(cat, position);
            });
        }
    }
}