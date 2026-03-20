package com.example.pawcontrolv1.ShelterVet;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.pawcontrolv1.BuildConfig;
import com.example.pawcontrolv1.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PetServicesActivity extends AppCompatActivity {

    private MapView mapView;
    private MapboxMap mapboxMap;

    // ── Filter chip IDs ───────────────────────────────────────────────────────
    private TextView chipAll, chipVet, chipShelter, chipEducation;
    private TextView activeChip;

    // ── Dot indicators ────────────────────────────────────────────────────────
    private View dot1, dot2, dot3, dot4, dot5;
    private final List<View> dots = new ArrayList<>();
    private int currentDotIndex = 0;

    // ── RecyclerView + Adapter ────────────────────────────────────────────────
    private RecyclerView rvPetServices;
    private PetServiceAdapter petServiceAdapter;
    private LinearLayoutManager llm;
    private SnapHelper snapHelper;

    // ── Sample data ───────────────────────────────────────────────────────────
    private final List<PetService> allServices = Arrays.asList(
            new PetService("Pet House Amanda",
                    "52-1, Jalan TTDI Grove, Kajang 1/2,43000 Kajang, Selangor",
                    "091-3213 3213", "shelter", 3.0738, 101.7987),
            new PetService("Klinik Haiwan Kajang",
                    "No. 10, Jalan Reko, 43000 Kajang, Selangor",
                    "03-8736 1212", "vet", 3.0682, 101.7914),
            new PetService("Pawsome Vet Clinic",
                    "Lot 5, Jalan Semenyih, 43500 Semenyih",
                    "03-8724 5500", "vet", 3.0601, 101.8432),
            new PetService("Furry Friends Education Centre",
                    "21, Jalan Balakong, 43300 Seri Kembangan",
                    "012-888 9900", "education", 3.0450, 101.7680),
            new PetService("Happy Paws Shelter",
                    "Lot 88, Jalan Cheras, 43200 Cheras",
                    "016-234 5678", "shelter", 3.0890, 101.7523)
    );

    private List<PetService> currentDisplayedServices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String key      = BuildConfig.MAPTILER_API_KEY;
        String styleUrl = "https://api.maptiler.com/maps/streets-v2/style.json?key=" + key;

        Mapbox.getInstance(this);
        setContentView(R.layout.activity_shelter_vet);

        bindViews();
        setupFilterChips();
        setupRecyclerView();
        setupDotIndicator();

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(map -> {
            mapboxMap = map;
            map.setStyle(styleUrl, style -> {
                map.setCameraPosition(new CameraPosition.Builder()
                        .target(new LatLng(3.0738, 101.7987))
                        .zoom(12.5)
                        .build());
                addMarkersForServices(allServices);
            });
        });

        refreshList(allServices);
    }

    // ── View binding ──────────────────────────────────────────────────────────
    private void bindViews() {
        chipAll       = findViewById(R.id.chipAll);
        chipVet       = findViewById(R.id.chipVet);
        chipShelter   = findViewById(R.id.chipShelter);
        chipEducation = findViewById(R.id.chipEducation);
        rvPetServices = findViewById(R.id.rvPetServices);

        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);
        dot4 = findViewById(R.id.dot4);
        dot5 = findViewById(R.id.dot5);
    }

    // ── Filter chip setup ─────────────────────────────────────────────────────
    private void setupFilterChips() {
        activeChip = chipAll;
        setChipSelected(chipAll);

        chipAll.setOnClickListener(v -> {
            setActiveChip(chipAll);
            addMarkersForServices(allServices);
            refreshList(allServices);
        });
        chipVet.setOnClickListener(v -> {
            setActiveChip(chipVet);
            List<PetService> filtered = filterByType("vet");
            addMarkersForServices(filtered);
            refreshList(filtered);
        });
        chipShelter.setOnClickListener(v -> {
            setActiveChip(chipShelter);
            List<PetService> filtered = filterByType("shelter");
            addMarkersForServices(filtered);
            refreshList(filtered);
        });
        chipEducation.setOnClickListener(v -> {
            Intent intent = new Intent(this, EducationActivity.class);
            startActivity(intent);
        });
    }

    private void setActiveChip(TextView selected) {
        setChipUnselected(chipAll);
        setChipUnselected(chipVet);
        setChipUnselected(chipShelter);
        setChipSelected(selected);
        activeChip = selected;
    }

    private void setChipSelected(TextView chip) {
        chip.setBackgroundResource(R.drawable.tab_orange_bg);
    }

    private void setChipUnselected(TextView chip) {
        chip.setBackgroundResource(R.drawable.tab_khaki_bg);
    }

    // ── Filter helper ─────────────────────────────────────────────────────────
    private List<PetService> filterByType(String type) {
        List<PetService> result = new ArrayList<>();
        for (PetService s : allServices) {
            if (s.getType().equalsIgnoreCase(type)) result.add(s);
        }
        return result;
    }

    // ── RecyclerView setup ────────────────────────────────────────────────────
    private void setupRecyclerView() {
        petServiceAdapter = new PetServiceAdapter(new ArrayList<>(), service -> {
            if (mapboxMap != null) {
                mapboxMap.animateCamera(
                        com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newLatLngZoom(
                                new LatLng(service.getLat(), service.getLng()), 15.0
                        ), 800
                );
            }
        });

        llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvPetServices.setLayoutManager(llm);
        rvPetServices.setAdapter(petServiceAdapter);
        rvPetServices.setItemAnimator(null);

        // clipToPadding=false lets neighbouring cards peek from the sides
        // while the centred card is fully visible.
        rvPetServices.setClipToPadding(false);

        // ── Dynamic centering padding ─────────────────────────────────────────
        // After the RecyclerView is laid out we know its exact pixel width.
        // horizontal padding = (recyclerWidth - cardWidth) / 2
        // Card width in item_pet_service.xml = 280dp
        rvPetServices.post(() -> {
            int recyclerWidth  = rvPetServices.getWidth();
            float density      = getResources().getDisplayMetrics().density;
            int cardWidthPx    = (int) (280 * density);
            int sidePadding    = Math.max(0, (recyclerWidth - cardWidthPx) / 2);
            rvPetServices.setPadding(sidePadding, 0, sidePadding, 0);
        });

        // ── Snap one card at a time to the centre ─────────────────────────────
        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvPetServices);

        // ── Update dot + pan map when snapping stops ──────────────────────────
        rvPetServices.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    View snapView = snapHelper.findSnapView(llm);
                    if (snapView != null) {
                        int pos = llm.getPosition(snapView);
                        if (pos >= 0) {
                            setActiveDot(pos);
                            // Pan map to the card that just snapped into centre
                            if (mapboxMap != null
                                    && pos < currentDisplayedServices.size()) {
                                PetService s = currentDisplayedServices.get(pos);
                                mapboxMap.animateCamera(
                                        com.mapbox.mapboxsdk.camera.CameraUpdateFactory
                                                .newLatLngZoom(
                                                        new LatLng(s.getLat(), s.getLng()),
                                                        14.0
                                                ), 500
                                );
                            }
                        }
                    }
                }
            }
        });
    }

    // ── Refresh list data ─────────────────────────────────────────────────────
    private void refreshList(List<PetService> services) {
        currentDisplayedServices = services;
        petServiceAdapter.updateData(services);

        // Always snap back to first card after a filter change
        rvPetServices.scrollToPosition(0);

        // Show only as many dots as there are items (max 5)
        int visibleDots = Math.min(services.size(), dots.size());
        for (int i = 0; i < dots.size(); i++) {
            dots.get(i).setVisibility(i < visibleDots ? View.VISIBLE : View.GONE);
        }
        setActiveDot(0);
    }

    // ── Dot indicator ─────────────────────────────────────────────────────────
    private void setupDotIndicator() {
        dots.add(dot1);
        dots.add(dot2);
        dots.add(dot3);
        dots.add(dot4);
        dots.add(dot5);
        setActiveDot(0);
    }

    private void setActiveDot(int index) {
        currentDotIndex = index;
        for (int i = 0; i < dots.size(); i++) {
            if (dots.get(i).getVisibility() == View.VISIBLE) {
                dots.get(i).setBackgroundResource(
                        i == index ? R.drawable.dot_active : R.drawable.dot_inactive);
            }
        }
    }

    // ── Map markers ───────────────────────────────────────────────────────────
    private void addMarkersForServices(List<PetService> services) {
        if (mapboxMap == null) return;
        mapboxMap.clear();
        for (PetService s : services) {
            mapboxMap.addMarker(new MarkerOptions()
                    .position(new LatLng(s.getLat(), s.getLng()))
                    .title(s.getName())
                    .snippet(s.getAddress()));
        }
        if (!services.isEmpty()) {
            mapboxMap.animateCamera(
                    com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newLatLngZoom(
                            new LatLng(services.get(0).getLat(), services.get(0).getLng()),
                            12.5
                    ), 600
            );
        }
    }

    // ── MapView lifecycle ─────────────────────────────────────────────────────
    @Override protected void onStart()   { super.onStart();    mapView.onStart();   }
    @Override protected void onResume()  { super.onResume();   mapView.onResume();  }
    @Override protected void onPause()   { super.onPause();    mapView.onPause();   }
    @Override protected void onStop()    { super.onStop();     mapView.onStop();    }
    @Override public void onLowMemory() { super.onLowMemory(); mapView.onLowMemory(); }
    @Override protected void onDestroy() { super.onDestroy();  mapView.onDestroy(); }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}