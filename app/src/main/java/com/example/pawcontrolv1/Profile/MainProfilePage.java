package com.example.pawcontrolv1.Profile;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawcontrolv1.R;

import java.util.ArrayList;
import java.util.List;

public class MainProfilePage extends AppCompatActivity {

    private ImageButton btnTabPilihan, btnTabDaily;
    private ImageView ivSettings, dp;
    private View tabIndicator;
    private RecyclerView filterRecyclerView;
    private CatAdapter catAdapter;
    private List<Cat> allCats     = new ArrayList<>();
    private List<Cat> favCats     = new ArrayList<>();

    private boolean isCatTabActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profile);

        initViews();
        setupIndicatorWidth();
        setupClickListeners();
        setupRecyclerView();
        loadDummyData();
    }

    private void initViews() {
        btnTabPilihan = findViewById(R.id.btnPost);
        btnTabDaily   = findViewById(R.id.btnFav);
        ivSettings    = findViewById(R.id.ivSettings);
        dp            = findViewById(R.id.dp);
        tabIndicator  = findViewById(R.id.tabIndicator);
        filterRecyclerView = findViewById(R.id.filterRecyclerView);
    }

    // Wait for layout to be drawn, then set indicator width to match one tab
    private void setupIndicatorWidth() {
        btnTabPilihan.post(() -> {
            ViewGroup.LayoutParams params = tabIndicator.getLayoutParams();
            params.width = btnTabPilihan.getWidth();
            tabIndicator.setLayoutParams(params);
            // Start on first tab, no animation needed
            tabIndicator.setTranslationX(0);
        });
    }

    private void setupClickListeners() {

        btnTabPilihan.setOnClickListener(v -> {
            if (!isCatTabActive) {
                isCatTabActive = true;
                slideIndicator(0);
            }
        });

        btnTabDaily.setOnClickListener(v -> {
            if (isCatTabActive) {
                isCatTabActive = false;
                slideIndicator(1);
            }
        });

        ivSettings.setOnClickListener(v -> {
            // TODO: open settings screen
        });

        dp.setOnClickListener(v -> {
            // TODO: open full-screen image or edit profile photo
        });
    }

    private void setupRecyclerView() {
        GridLayoutManager gridLayout = new GridLayoutManager(this, 2);
        filterRecyclerView.setLayoutManager(gridLayout);

        catAdapter = new CatAdapter(this, new ArrayList<>());
        filterRecyclerView.setAdapter(catAdapter);

        catAdapter.setOnCatClickListener(new CatAdapter.OnCatClickListener() {
            @Override
            public void onCatClick(Cat cat, int position) {
                // TODO: open cat detail screen
            }

            @Override
            public void onFavouriteClick(Cat cat, int position) {
                // Sync favCats list when heart is tapped
                if (cat.isFavourite()) {
                    if (!favCats.contains(cat)) favCats.add(cat);
                } else {
                    favCats.remove(cat);
                }
            }
        });
    }

    // ── loadDummyData() ──────────────────────────────────────────────────────────
    private void loadDummyData() {
        allCats.add(new Cat("Luna",   "Siamese",      "2 yrs", R.drawable.dp_cat_1));
        allCats.add(new Cat("Mochi",  "British Short","1 yr",  R.drawable.dp_cat_1));
        allCats.add(new Cat("Oreo",   "Tuxedo",       "3 yrs", R.drawable.dp_cat_1));
        allCats.add(new Cat("Ginger", "Tabby",        "4 yrs", R.drawable.dp_cat_1));

        catAdapter.updateList(allCats);
    }

    // ── Update slideIndicator() to swap list on tab switch ──────────────────────
    private void slideIndicator(int tabIndex) {
        float targetX = tabIndex * btnTabPilihan.getWidth();

        tabIndicator.animate()
                .translationX(targetX)
                .setDuration(250)
                .setInterpolator(new FastOutSlowInInterpolator())
                .start();

        btnTabPilihan.setAlpha(tabIndex == 0 ? 1f : 0.5f);
        btnTabDaily.setAlpha(tabIndex == 1 ? 1f : 0.5f);

        // Swap displayed list based on active tab
        catAdapter.updateList(tabIndex == 0 ? allCats : favCats);
    }
}