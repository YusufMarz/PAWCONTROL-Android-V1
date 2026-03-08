package com.example.pawcontrolv1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MapView mapView;

    // ── Sample people data — replace with your real data source ──────────────
    private final List<Person> peopleList = Arrays.asList(
            new Person("Oppaih",     R.drawable.ic_default_avatar),
            new Person("Yusuf Marz", R.drawable.ic_default_avatar),
            new Person("Pesuu",      R.drawable.ic_default_avatar),
            new Person("Lurkiz",      R.drawable.ic_default_avatar),
            new Person("Muz",      R.drawable.ic_default_avatar)
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String key      = BuildConfig.MAPTILER_API_KEY;
        String styleUrl = "https://api.maptiler.com/maps/streets-v2/style.json?key=" + key;

        Mapbox.getInstance(this);
        setContentView(R.layout.activity_main);

        // ── Filter RecyclerView ───────────────────────────────────────────────
        RecyclerView filterRecyclerView = findViewById(R.id.filterRecyclerView);
        FilterAdapter filterAdapter = new FilterAdapter(
                FilterAdapter.getDefaultFilters(this),
                filterName -> {
                    switch (filterName) {
                        case "All":     showAllMarkers();               break;
                        case "Cat":     showMarkersByType("cat");       break;
                        case "Dog":     showMarkersByType("dog");       break;
                        case "Lost":    showMarkersByStatus("lost");    break;
                        case "Rescued": showMarkersByStatus("rescued"); break;
                        case "Shelter": showMarkersByType("shelter");   break;
                    }
                }
        );
        filterRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        filterRecyclerView.setAdapter(filterAdapter);
        filterRecyclerView.setItemAnimator(null);

        // ── People badge — show count ─────────────────────────────────────────
        TextView peopleBadge = findViewById(R.id.peopleBadge);
        int count = peopleList.size();
        if (count > 0) {
            peopleBadge.setText(String.valueOf(count));
            peopleBadge.setVisibility(View.VISIBLE);
        }

        // ── People dropdown click → show popup ────────────────────────────────
        LinearLayout peopleDropdown = findViewById(R.id.peopleDropdown);
        peopleDropdown.setOnClickListener(v -> showPeoplePopup(v));

        // ── Map ───────────────────────────────────────────────────────────────
        mapView = findViewById(R.id.mapView);
        mapView.getMapAsync(map -> {
            map.setStyle(styleUrl);
            map.setCameraPosition(new CameraPosition.Builder()
                    .target(new LatLng(3.1390, 101.6869))
                    .zoom(12.0)
                    .build());
        });
    }

    // ── People popup ──────────────────────────────────────────────────────────
    private void showPeoplePopup(View anchor) {
        // Inflate popup layout
        View popupView = LayoutInflater.from(this)
                .inflate(R.layout.popup_people, null);

        // Build popup window
        PopupWindow popup = new PopupWindow(
                popupView,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                true // focusable — dismisses on outside touch
        );
        popup.setElevation(16f);

        // Wire up the RecyclerView inside the popup
        RecyclerView list = popupView.findViewById(R.id.popupPeopleList);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(new PeoplePopupAdapter(peopleList, person -> {
            // TODO: handle person tap — e.g. center map on their pet
            Toast.makeText(this, person.getName(), Toast.LENGTH_SHORT).show();
            popup.dismiss();
        }));

        // Add People row click
        popupView.findViewById(R.id.addPeopleRow).setOnClickListener(v -> {
            // TODO: open Add People screen
            Toast.makeText(this, "Add People", Toast.LENGTH_SHORT).show();
            popup.dismiss();
        });

        // Right-align popup to the right edge of the People button
        popupView.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );
        int xOffset = anchor.getWidth() - popupView.getMeasuredWidth();
        popup.showAsDropDown(anchor, -300, 8);
    }

    // ── Filter stubs ──────────────────────────────────────────────────────────
    private void showAllMarkers()                  { /* TODO */ }
    private void showMarkersByType(String type)    { /* TODO */ }
    private void showMarkersByStatus(String status){ /* TODO */ }

    // ── MapView lifecycle ─────────────────────────────────────────────────────
    @Override protected void onStart()    { super.onStart();    mapView.onStart();    }
    @Override protected void onResume()   { super.onResume();   mapView.onResume();   }
    @Override protected void onPause()    { super.onPause();    mapView.onPause();    }
    @Override protected void onStop()     { super.onStop();     mapView.onStop();     }
    @Override public    void onLowMemory(){ super.onLowMemory();mapView.onLowMemory();}
    @Override protected void onDestroy()  { super.onDestroy();  mapView.onDestroy();  }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}