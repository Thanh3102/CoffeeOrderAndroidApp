package com.example.coffeeorderjavaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {
    private GoogleMap map;
    private final String[] permissionArray = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private final int requestCode = 1;
    private Marker currentMark;

    private TextView markerTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        this.markerTv = findViewById(R.id.markerLocationTv);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapActivity.this, permissionArray, requestCode);
        } else {
            LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                    500, location -> {
                        Toast.makeText(this, "Current location " + location, Toast.LENGTH_SHORT).show();
                        Log.i("Location", "Current location " + location);
                    });
        }

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.map, mapFragment)
                .commit();
        mapFragment.getMapAsync(this);

        Button testBtn = findViewById(R.id.test);
        Button backBtn = findViewById(R.id.mapBack);
        testBtn.setOnClickListener(v -> {
            if (map == null) {
                Toast.makeText(this, "Map is not ready", Toast.LENGTH_SHORT).show();
            } else {
                LatLngBounds australiaBounds = new LatLngBounds(
                        new LatLng(-44, 113), // SW bounds
                        new LatLng(-10, 154)  // NE bounds
                );
                map.moveCamera(CameraUpdateFactory.newLatLngBounds(australiaBounds, 0));
            }
        });

        backBtn.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.map = googleMap;
        googleMap.setOnMapClickListener(latLng -> {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                Address currentAddress = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).get(0);
                if (this.currentMark != null) {
                    this.currentMark.remove();
                }
                this.currentMark = map.addMarker(new MarkerOptions()
                        .position(new LatLng(latLng.latitude, latLng.longitude))
                        .title("Vị trí giao hàng"));
                markerTv.setText(currentAddress.getAddressLine(0).toString());
                Log.i("Click location", currentAddress.toString());
                Log.i("Address line", currentAddress.getAddressLine(0).toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
//        googleMap.addMarker(new MarkerOptions()
//                .position(new LatLng(0, 0))
//                .title("Marker"));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == this.requestCode) {
            LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                return;
            }
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                    500, location -> {
                        Toast.makeText(this, "Current location: " + location, Toast.LENGTH_SHORT).show();
                        Log.i("Location", "Current location " + location);
                    });
        }
    }
}