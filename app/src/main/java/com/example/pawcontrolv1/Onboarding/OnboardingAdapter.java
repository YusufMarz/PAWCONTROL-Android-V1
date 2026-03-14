package com.example.pawcontrolv1.Onboarding;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawcontrolv1.R;

import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder> {

    private final Context context;
    private final List<OnboardingItem> items;

    public OnboardingAdapter(Context context, List<OnboardingItem> items) {
        this.context = context;
        this.items   = items;
    }

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_onboarding_page, parent, false);
        return new OnboardingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() { return items.size(); }

    class OnboardingViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivImage;
        private final TextView tvTitle;
        private final TextView tvSubtitle;

        OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage    = itemView.findViewById(R.id.ivOnboardingImage);
            tvTitle    = itemView.findViewById(R.id.tvTitle);
            tvSubtitle = itemView.findViewById(R.id.tvSubtitle);
        }

        void bind(com.example.pawcontrolv1.Onboarding.OnboardingItem item) {
            ivImage.setImageResource(item.getImageResId());
            tvSubtitle.setText(item.getSubtitle());

            // Build mixed-color spannable title
            // Format: [normal][orange][normal2]
            // e.g.  "View " + "Live Map" + "\nLocations"
            String full = item.getTitleNormal()
                    + item.getTitleOrange()
                    + item.getTitleNormal2();

            SpannableString spannable = new SpannableString(full);

            int orangeStart = item.getTitleNormal().length();
            int orangeEnd   = orangeStart + item.getTitleOrange().length();

            spannable.setSpan(
                    new ForegroundColorSpan(
                            ContextCompat.getColor(context, R.color.filter_bg_selected)),
                    orangeStart,
                    orangeEnd,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            tvTitle.setText(spannable);
        }
    }
}