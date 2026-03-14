package com.example.pawcontrolv1.Onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.pawcontrolv1.MainActivity;
import com.example.pawcontrolv1.R;
// import com.example.pawcontrolv1.MainActivity; // ← uncomment when ready

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private Button btnSkip, btnNext;
    private LinearLayout dotsLayout;
    private OnboardingAdapter adapter;

    private final List<ImageView> dots = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager  = findViewById(R.id.viewPager);
        btnSkip    = findViewById(R.id.btnSkip);
        btnNext    = findViewById(R.id.btnNext);
        dotsLayout = findViewById(R.id.dotsLayout);

        List<OnboardingItem> items = buildItems();
        adapter = new OnboardingAdapter(this, items);
        viewPager.setAdapter(adapter);

        setupDots(items.size());
        updateDots(0);
        updateButtons(0, items.size());

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateDots(position);
                updateButtons(position, items.size());
            }
        });

        btnNext.setOnClickListener(v -> {
            int current = viewPager.getCurrentItem();
            if (current < adapter.getItemCount() - 1) {
                viewPager.setCurrentItem(current + 1, true);
            } else {
                finishOnboarding();
            }
        });

        btnSkip.setOnClickListener(v -> finishOnboarding());
    }

    // ── Build your onboarding pages here ────────────────────────────────────
    private List<OnboardingItem> buildItems() {
        List<OnboardingItem> list = new ArrayList<>();

        // titleNormal | titleOrange | titleNormal2 (use \n for line break) | subtitle | image
        list.add(new OnboardingItem(
                "View ", "Live Map", "\nLocations",
                "See stray animals, shelters, and vets pinned in real time around you so you always know what's nearby and can act fast when it matters.",
                R.drawable.ic_onboarding_1));   // ← replace with your drawable

        list.add(new OnboardingItem(
                "Find ", "Near Shelter", "\nNearby",
                "Locate the closest animal shelters in your area, check their availability, and get directions so every stray you find has a safe place to go.",
                R.drawable.ic_onboarding_1));

        list.add(new OnboardingItem(
                "Report ", "Stray Pet", "\nEasily",
                "Spotted a stray? Snap a photo, describe their condition, and submit a report in seconds helping the community respond faster and save more lives.",
                R.drawable.ic_onboarding_1));

        return list;
    }

    // ── Dot indicators ───────────────────────────────────────────────────────
    private void setupDots(int count) {
        dots.clear();
        dotsLayout.removeAllViews();

        for (int i = 0; i < count; i++) {
            ImageView dot = new ImageView(this);
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(20, 12);
            params.setMargins(6, 0, 6, 0);
            dot.setLayoutParams(params);
            dot.setImageResource(R.drawable.dot_inactive);
            dots.add(dot);
            dotsLayout.addView(dot);
        }
    }

    private void updateDots(int activeIndex) {
        for (int i = 0; i < dots.size(); i++) {
            dots.get(i).setImageResource(
                    i == activeIndex ? R.drawable.dot_active   // orange pill
                            : R.drawable.dot_inactive
            );
        }
    }

    // ── Button label swap on last page ───────────────────────────────────────
    private void updateButtons(int position, int total) {
        btnNext.setText(position == total - 1 ? "Finish" : "Next");
    }

    // ── Navigate away after onboarding ──────────────────────────────────────
    private void finishOnboarding() {
        // TODO: mark onboarding complete in SharedPreferences
         Intent intent = new Intent(this, MainActivity.class);
         startActivity(intent);
        finish();
    }
}