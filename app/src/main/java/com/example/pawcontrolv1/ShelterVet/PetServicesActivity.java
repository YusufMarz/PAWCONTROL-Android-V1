package com.example.pawcontrolv1.ShelterVet;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawcontrolv1.BuildConfig;
import com.example.pawcontrolv1.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;

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

    // ── Sample data — replace with your real data source ─────────────────────
    private final List<PetService> allServices = Arrays.asList(
            new PetService("Pet House Amanda",
                    "52-1, Jalan TTDI Grove, Kajang 1/2,43000 Kajang, Selangor",
                    "091-3213 3213",
                    "shelter",
                    3.0738, 101.7987),

            new PetService("Klinik Haiwan Kajang",
                    "No. 10, Jalan Reko, 43000 Kajang, Selangor",
                    "03-8736 1212",
                    "vet",
                    3.0682, 101.7914),

            new PetService("Pawsome Vet Clinic",
                    "Lot 5, Jalan Semenyih, 43500 Semenyih",
                    "03-8724 5500",
                    "vet",
                    3.0601, 101.8432),

            new PetService("Furry Friends Education Centre",
                    "21, Jalan Balakong, 43300 Seri Kembangan",
                    "012-888 9900",
                    "education",
                    3.0450, 101.7680),

            new PetService("Happy Paws Shelter",
                    "Lot 88, Jalan Cheras, 43200 Cheras",
                    "016-234 5678",
                    "shelter",
                    3.0890, 101.7523)
    );

    // ── Map marker coordinates (all services) ─────────────────────────────────
    private List<PetService> currentDisplayedServices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialise Mapbox with your API key
        String key      = BuildConfig.MAPTILER_API_KEY;
        String styleUrl = "https://api.maptiler.com/maps/streets-v2/style.json?key=" + key;

        Mapbox.getInstance(this);
        setContentView(R.layout.activity_shelter_vet);

        // ── Bind views ────────────────────────────────────────────────────────
        bindViews();

        // ── Setup filter chips ────────────────────────────────────────────────
        setupFilterChips();

        // ── Setup RecyclerView ────────────────────────────────────────────────
        setupRecyclerView();

        // ── Setup dot indicators ──────────────────────────────────────────────
        setupDotIndicator();

        // ── Init map ──────────────────────────────────────────────────────────
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(map -> {
            mapboxMap = map;
            map.setStyle(styleUrl, style -> {
                // Default camera — centre on Kajang / Selangor area
                map.setCameraPosition(new CameraPosition.Builder()
                        .target(new LatLng(3.0738, 101.7987))
                        .zoom(12.5)
                        .build());

                // Show all markers on first load
                addMarkersForServices(allServices);
            });
        });

        // Default: show all services in list
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
        // Default selected = "All"
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
        });
    }

    private void setActiveChip(TextView selected) {
        // Reset all chips to unselected style
        setChipUnselected(chipAll);
        setChipUnselected(chipVet);
        setChipUnselected(chipShelter);

        // Highlight selected chip
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
            // Tap on a card → fly map camera to that service location
            if (mapboxMap != null) {
                mapboxMap.animateCamera(
                        com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newLatLngZoom(
                                new LatLng(service.getLat(), service.getLng()), 15.0
                        ), 800
                );
            }
        });

        rvPetServices.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvPetServices.setAdapter(petServiceAdapter);
        rvPetServices.setItemAnimator(null);

        // Sync dot indicator with horizontal scroll position
        rvPetServices.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager lm =
                            (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (lm != null) {
                        int pos = lm.findFirstVisibleItemPosition();
                        setActiveDot(pos);
                    }
                }
            }
        });
    }

    // ── Refresh list data ─────────────────────────────────────────────────────
    private void refreshList(List<PetService> services) {
        currentDisplayedServices = services;
        petServiceAdapter.updateData(services);

        // Reset dots to match new list size (max 5 dots shown)
        int visibleDots = Math.min(services.size(), dots.size());
        for (int i = 0; i < dots.size(); i++) {
            dots.get(i).setVisibility(i < visibleDots ? View.VISIBLE : View.GONE);
        }
        setActiveDot(0);
    }

    // ── Dot indicator setup ───────────────────────────────────────────────────
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
        mapboxMap.clear(); // remove existing markers

        for (PetService s : services) {
            mapboxMap.addMarker(new MarkerOptions()
                    .position(new LatLng(s.getLat(), s.getLng()))
                    .title(s.getName())
                    .snippet(s.getAddress()));
        }

        // Optionally pan camera to first result
        if (!services.isEmpty()) {
            mapboxMap.animateCamera(
                    com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newLatLngZoom(
                            new LatLng(services.get(0).getLat(), services.get(0).getLng()),
                            12.5
                    ), 600
            );
        }
    }

    // ── MapView lifecycle (required by Mapbox SDK) ────────────────────────────
    @Override protected void onStart()   { super.onStart();   mapView.onStart();   }
    @Override protected void onResume()  { super.onResume();  mapView.onResume();  }
    @Override protected void onPause()   { super.onPause();   mapView.onPause();   }
    @Override protected void onStop()    { super.onStop();    mapView.onStop();    }
    @Override public void onLowMemory() { super.onLowMemory(); mapView.onLowMemory(); }
    @Override protected void onDestroy() { super.onDestroy(); mapView.onDestroy(); }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}