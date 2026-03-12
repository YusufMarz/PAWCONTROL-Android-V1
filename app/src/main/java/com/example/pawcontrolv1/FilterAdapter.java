package com.example.pawcontrolv1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.Arrays;
import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {

    public interface OnFilterClickListener {
        void onFilterClick(String filterName);
    }

    // ── Filter model ──────────────────────────────────────────────────────────
    public static class FilterItem {
        public final String label;
        public final int    iconRes; // drawable resource, 0 = no icon

        public FilterItem(String label, int iconRes) {
            this.label   = label;
            this.iconRes = iconRes;
        }
    }

    // ── Default filters for a stray cat/dog app ───────────────────────────────
    public static List<FilterItem> getDefaultFilters(Context ctx) {
        return Arrays.asList(
                new FilterItem("All",      R.drawable.ic_cat),       // grid / paw icon
                new FilterItem("Cat",      R.drawable.ic_cat),
                new FilterItem("Dog",      R.drawable.ic_cat),
                new FilterItem("Lost",     R.drawable.ic_cat),      // exclamation / pin icon
                new FilterItem("Rescued",  R.drawable.ic_cat),   // heart icon
                new FilterItem("Shelter",  R.drawable.ic_cat)    // house icon
        );
    }

    // ── Fields ────────────────────────────────────────────────────────────────
    private final List<FilterItem>      filters;
    private final OnFilterClickListener listener;
    private       int                   selectedPosition = 0;   // "All" selected by default

    public FilterAdapter(List<FilterItem> filters, OnFilterClickListener listener) {
        this.filters  = filters;
        this.listener = listener;
    }

    // ── ViewHolder ────────────────────────────────────────────────────────────
    public static class FilterViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView card;
        ImageView        icon;
        TextView         label;

        public FilterViewHolder(@NonNull View itemView) {
            super(itemView);
            card  = itemView.findViewById(R.id.filterCard);
            icon  = itemView.findViewById(R.id.filterIcon);
            label = itemView.findViewById(R.id.filterLabel);
        }
    }

    // ── Adapter overrides ─────────────────────────────────────────────────────
    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_filter, parent, false);
        return new FilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {
        FilterItem item      = filters.get(position);
        Context    ctx       = holder.itemView.getContext();
        boolean    isSelected = (position == selectedPosition);

        // ── Label ──
        holder.label.setText(item.label);

        // ── Icon ──
        if (item.iconRes != 0) {
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(item.iconRes);
        } else {
            holder.icon.setVisibility(View.GONE);
        }

        // ── Selected vs unselected style ──
        if (isSelected) {
            holder.card.setCardBackgroundColor(
                    ContextCompat.getColor(ctx, R.color.filter_bg_selected));   // e.g. #FFE4E1
            holder.card.setStrokeColor(
                    ContextCompat.getColor(ctx, R.color.filter_stroke_selected)); // e.g. #D84315
            holder.label.setTextColor(
                    ContextCompat.getColor(ctx, R.color.filter_text_selected));  // e.g. #D84315
        } else {
            holder.card.setCardBackgroundColor(
                    ContextCompat.getColor(ctx, R.color.filter_bg_default));     // #
            holder.card.setStrokeColor(
                    ContextCompat.getColor(ctx, R.color.filter_stroke_default)); // #DDDDDD
            holder.label.setTextColor(
                    ContextCompat.getColor(ctx, R.color.filter_text_default));   // #666666
        }

        // ── Click ──
        holder.itemView.setOnClickListener(v -> {
            int previous       = selectedPosition;
            selectedPosition   = holder.getAdapterPosition();
            notifyItemChanged(previous);
            notifyItemChanged(selectedPosition);
            listener.onFilterClick(item.label);
        });
    }

    @Override
    public int getItemCount() {
        return filters.size();
    }

    // ── Public helper ─────────────────────────────────────────────────────────
    /** Programmatically select a filter by label (e.g. after deep-link). */
    public void selectFilter(String label) {
        for (int i = 0; i < filters.size(); i++) {
            if (filters.get(i).label.equalsIgnoreCase(label)) {
                int previous     = selectedPosition;
                selectedPosition = i;
                notifyItemChanged(previous);
                notifyItemChanged(i);
                break;
            }
        }
    }
}