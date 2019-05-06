package com.example.memorableplaces;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.memorableplaces.sync.MemorablePlacesIntentService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String EXTRA_MEMORABLE_ADDRESS = "com.example.memorableplaces.memorable_address";
    private static final String EXTRA_MEMORABLE_LOCATION = "com.example.memorableplaces.memorable_latlng";

    private static final LatLng EGYPT_LATLNG = new LatLng(30.0444, 31.2357);

    public static Intent newIntent(Context packageContext, String address, LatLng location) {
        Intent intent = new Intent(packageContext, MapActivity.class);
        intent.putExtra(EXTRA_MEMORABLE_ADDRESS, address);
        intent.putExtra(EXTRA_MEMORABLE_LOCATION, location);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        // Apply a custom map style
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));

        // If the user pressed on one of the saved locations
        if (getIntent().getStringExtra(EXTRA_MEMORABLE_ADDRESS) != null) {

            final String address = getIntent().getStringExtra(EXTRA_MEMORABLE_ADDRESS);
            final LatLng latLng = getIntent().getParcelableExtra(EXTRA_MEMORABLE_LOCATION);
            googleMap.addMarker(new MarkerOptions().position(latLng).title(address));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

        } else {
            //the user selected (Add a new memorable place) from MainActivity's ListView
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(EGYPT_LATLNG, 15));
        }

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(latLng));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                //Starting an intent service that will add the memorable place to SharedPreferences
                Intent memorablePlacesIntent = MemorablePlacesIntentService.newIntent(MapActivity.this, latLng);
                memorablePlacesIntent.setAction(MemorablePlacesIntentService.ACTION_ADD_MEMORABLE_PLACE);
                startService(memorablePlacesIntent);

            }
        });
    }
}
