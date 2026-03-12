package com.example.pawcontrolv1.Profile;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pawcontrolv1.R;

public class MainProfilePage extends AppCompatActivity {

    private ImageButton btnTabPilihan, btnTabDaily;
    private ImageView ivSettings, dp;

    private boolean isCatTabActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profile);

        initViews();
        setTabActive(true);
        setupClickListeners();
    }

    private void initViews() {
        btnTabPilihan = findViewById(R.id.btnPost);
        btnTabDaily   = findViewById(R.id.btnFav);
        ivSettings    = findViewById(R.id.ivSettings);
        dp            = findViewById(R.id.dp);
    }

    private void setupClickListeners() {

        btnTabPilihan.setOnClickListener(v -> {
            if (!isCatTabActive) {
                isCatTabActive = true;
                setTabActive(true);
            }
        });

        btnTabDaily.setOnClickListener(v -> {
            if (isCatTabActive) {
                isCatTabActive = false;
                setTabActive(false);
            }
        });

        ivSettings.setOnClickListener(v -> {
            // TODO: open settings screen
        });

        dp.setOnClickListener(v -> {
            // TODO: open full-screen image or edit profile photo
        });
    }

    private void setTabActive(boolean catActive) {
        if (catActive) {
            btnTabPilihan.setBackgroundResource(R.drawable.tab_active_bg);
            btnTabDaily.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        } else {
            btnTabDaily.setBackgroundResource(R.drawable.tab_active_bg);
            btnTabPilihan.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        }
    }
}