package com.example.pawcontrolv1.ShelterVet;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawcontrolv1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class ShelterVetActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private MaterialButton btnAll, btnVet, btnShelter;
    private RecyclerView recyclerViewPetHouses;
    private BottomNavigationView bottomNavigation;

    private PetHouseAdapter petHouseAdapter;
    private List<PetHouse> allPetHouses = new ArrayList<>();
    private String currentFilter = "ALL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // FIX 1: Hardcode token — do NOT use R.string.mapbox_access_token
        // Replace the string below with your real token from mapbox.com
//        Mapbox.getInstance(this, "yx1YuJkGJE90mmcL91Vl");

        setContentView(R.layout.activity_shelter_vet);

        initViews();
        setupMapView(savedInstanceState);
        setupFilterButtons();
        setupRecyclerView();
        setupBottomNavigation();
        loadPetHouses();
    }

    private void initViews() {
        mapView               = findViewById(R.id.mapViewInner);
        btnAll                = findViewById(R.id.btnAll);
        btnVet                = findViewById(R.id.btnVet);
        btnShelter            = findViewById(R.id.btnShelter);
        recyclerViewPetHouses = findViewById(R.id.recyclerViewPetHouses);
    }

    // ─────────────────────────────────────────────
    // Map
    // ─────────────────────────────────────────────

    private void setupMapView(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        // FIX 2: Style.MAPBOX_STREETS is deprecated — use the URI string directly
        mapboxMap.setStyle("mapbox://styles/mapbox/streets-v11", style -> {
            mapboxMap.setCameraPosition(new CameraPosition.Builder()
                    .target(new LatLng(3.0738, 101.5183)) // Kajang, Selangor
                    .zoom(13.0)
                    .build());
            addMapMarkers(allPetHouses);
        });
    }

    private void addMapMarkers(List<PetHouse> petHouses) {
        if (mapboxMap == null) return;
        mapboxMap.clear();
        for (PetHouse house : petHouses) {
            mapboxMap.addMarker(new MarkerOptions()
                    .position(new LatLng(house.getLatitude(), house.getLongitude()))
                    .title(house.getName())
                    .snippet(house.getAddress()));
        }
    }

    // ─────────────────────────────────────────────
    // Filter Buttons
    // ─────────────────────────────────────────────

    private void setupFilterButtons() {
        btnAll.setOnClickListener(v -> applyFilter("ALL"));
        btnVet.setOnClickListener(v -> applyFilter("VET"));
        btnShelter.setOnClickListener(v -> applyFilter("SHELTER"));
    }

    private void applyFilter(String filter) {
        currentFilter = filter;
        updateFilterButtonStyles(filter);
        List<PetHouse> filtered = getFilteredList(filter);
        petHouseAdapter.updateList(filtered);
        addMapMarkers(filtered);
    }

    private void updateFilterButtonStyles(String activeFilter) {
        setButtonInactive(btnAll);
        setButtonInactive(btnVet);
        setButtonInactive(btnShelter);
        switch (activeFilter) {
            case "ALL":     setButtonActive(btnAll);     break;
            case "VET":     setButtonActive(btnVet);     break;
            case "SHELTER": setButtonActive(btnShelter); break;
        }
    }

    private void setButtonActive(MaterialButton button) {
        button.setBackgroundTintList(
                androidx.core.content.ContextCompat.getColorStateList(this, R.color.white));
        button.setTextColor(
                androidx.core.content.ContextCompat.getColor(this, R.color.white));
        button.setStrokeWidth(0);
    }

    private void setButtonInactive(MaterialButton button) {
        button.setBackgroundTintList(
                androidx.core.content.ContextCompat.getColorStateList(this, R.color.white));
        button.setTextColor(
                androidx.core.content.ContextCompat.getColor(this, R.color.white));
        button.setStrokeWidth(2);
    }

    // ─────────────────────────────────────────────
    // RecyclerView
    // ─────────────────────────────────────────────

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPetHouses.setLayoutManager(layoutManager);

        petHouseAdapter = new PetHouseAdapter(this, new ArrayList<>(), petHouse -> {
            if (mapboxMap != null) {
                mapboxMap.setCameraPosition(new CameraPosition.Builder()
                        .target(new LatLng(petHouse.getLatitude(), petHouse.getLongitude()))
                        .zoom(15.0)
                        .build());
            }
        });
        recyclerViewPetHouses.setAdapter(petHouseAdapter);
    }

    // ─────────────────────────────────────────────
    // Bottom Navigation
    // ─────────────────────────────────────────────

    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if      (id == R.id.nav_map)        return true;
            else if (id == R.id.nav_shelterVet) return true;
            else if (id == R.id.nav_shop)       return true;
            else if (id == R.id.nav_profile)    return true;
            else if (id == R.id.nav_report)     return true;
            return false;
        });
        bottomNavigation.setSelectedItemId(R.id.nav_map);
    }

    // ─────────────────────────────────────────────
    // Data
    // ─────────────────────────────────────────────

    private void loadPetHouses() {
        allPetHouses.add(new PetHouse("Pet House Amanda",   "52-1, Jalan TTDI Grove, Kajang 1/2, 43000 Kajang, Selangor", "091-3213 3213", "VET",     3.0738, 101.5183));
        allPetHouses.add(new PetHouse("Happy Paws Shelter", "10, Jalan Putra, 43000 Kajang, Selangor",                    "011-2345 6789", "SHELTER", 3.0800, 101.5250));
        allPetHouses.add(new PetHouse("PetCare Clinic",     "25, Jalan Reko, 43000 Kajang, Selangor",                     "012-9876 5432", "VET",     3.0680, 101.5100));
        allPetHouses.add(new PetHouse("Furever Home",       "3, Jalan Semenyih, 43500 Semenyih, Selangor",                "013-1111 2222", "SHELTER", 3.0600, 101.5400));

        petHouseAdapter.updateList(allPetHouses);
        if (mapboxMap != null) addMapMarkers(allPetHouses);
    }

    private List<PetHouse> getFilteredList(String filter) {
        if (filter.equals("ALL")) return new ArrayList<>(allPetHouses);
        List<PetHouse> result = new ArrayList<>();
        for (PetHouse house : allPetHouses) {
            if (house.getType().equals(filter)) result.add(house);
        }
        return result;
    }

    // ─────────────────────────────────────────────
    // Mapbox Lifecycle
    // ─────────────────────────────────────────────

    @Override protected void onStart()     { super.onStart();     mapView.onStart();     }
    @Override protected void onResume()    { super.onResume();    mapView.onResume();    }
    @Override protected void onPause()     { super.onPause();     mapView.onPause();     }
    @Override protected void onStop()      { super.onStop();      mapView.onStop();      }
    @Override public    void onLowMemory() { super.onLowMemory(); mapView.onLowMemory(); }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    // ─────────────────────────────────────────────
    // Model
    // ─────────────────────────────────────────────

    public static class PetHouse {
        private final String name, address, contact, type;
        private final double latitude, longitude;

        public PetHouse(String name, String address, String contact,
                        String type, double latitude, double longitude) {
            this.name = name; this.address = address; this.contact = contact;
            this.type = type; this.latitude = latitude; this.longitude = longitude;
        }

        public String getName()      { return name; }
        public String getAddress()   { return address; }
        public String getContact()   { return contact; }
        public String getType()      { return type; }
        public double getLatitude()  { return latitude; }
        public double getLongitude() { return longitude; }
    }

    public interface OnPetHouseClickListener {
        void onPetHouseClick(PetHouse petHouse);
    }
}