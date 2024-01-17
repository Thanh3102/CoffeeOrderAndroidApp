package com.example.coffeeorderjavaapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {
    private GoogleMap map;
    private final String[] permissionArray = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private final int requestCode = 1;
    private Marker deliveryMark;
    private Marker currentMark;
    private TextView markerTv;
    private Location currentLocation;
    private LocationManager mLocationManager;
    private TextView distanceTv;
    private int shippingFee;
    private String deliveryLocation;

    private final LatLng ShopLatLng = new LatLng(21.0074, 105.8237);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        markerTv = findViewById(R.id.markerLocationTv);
        distanceTv = findViewById(R.id.distanceTv);
        if (currentLocation == null) {
            markerTv.setText("Vị trí nhận hàng: Chưa chọn vị trí");
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapActivity.this, permissionArray, requestCode);
        } else {
            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                    100, location -> {
                        Log.i("Location", "Current location " + location);
                        this.currentLocation = location;
                    });
        }

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.map, mapFragment)
                .commit();
        mapFragment.getMapAsync(this);

        Button myLocationBtn = findViewById(R.id.myLocationBtn);
        Button backBtn = findViewById(R.id.mapBack);
        Button saveBtn = findViewById(R.id.saveLocationBtn);

        saveBtn.setOnClickListener(v -> {
            if (this.deliveryMark != null) {
                Intent intent = new Intent(this, CheckoutActivity.class);
                intent.putExtra("deliveryLocation", this.deliveryLocation);
                intent.putExtra("shippingFee", this.shippingFee);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Chưa chọn vị trí nhận hàng", Toast.LENGTH_SHORT).show();
            }
        });

        myLocationBtn.setOnClickListener(v -> {
            if (map == null || currentLocation == null) {
                Toast.makeText(this, "Đang định vị vị trí của bạn", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Lat: " + currentLocation.getLatitude() + " Lng: " + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                if (this.currentMark != null) {
                    this.currentMark.remove();
                }
                this.currentMark = map.addMarker(new MarkerOptions()
                        .position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                        .title("Vị trí của bạn"));
                LatLng currentPosition = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
                map.animateCamera(CameraUpdateFactory.zoomTo(18));
            }
        });

        backBtn.setOnClickListener(v -> {
            finish();
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.map = googleMap;
        LatLng vietnam = new LatLng(15.9030623, 105.8066925);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(vietnam, 5));
        googleMap.setOnMapClickListener(latLng -> {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                Address currentAddress = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).get(0);
                if (this.deliveryMark != null) {
                    this.deliveryMark.remove();
                }
                this.deliveryMark = map.addMarker(new MarkerOptions()
                        .position(new LatLng(latLng.latitude, latLng.longitude))
                        .title("Vị trí nhận hàng"));
                markerTv.setText("Vị trí nhận hàng: " + currentAddress.getAddressLine(0).toString());
                this.deliveryLocation = currentAddress.getAddressLine(0).toString();

                LatLng deliveryLocation = new LatLng(deliveryMark.getPosition().latitude, deliveryMark.getPosition().longitude);
                distanceTv.setText("Khoảng cách giao hàng: " + getDistance(ShopLatLng, deliveryLocation));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == this.requestCode) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                        100, location -> {
                            Toast.makeText(this, "Current location " + location, Toast.LENGTH_SHORT).show();
                            this.currentLocation = location;
                        });
            }
        }
    }

    private float getDistance(LatLng a, LatLng b) {
        float[] results = new float[1];
        Location.distanceBetween(a.latitude, a.longitude, b.latitude, b.longitude, results);
        float distance = results[0];
        this.shippingFee = Math.round(distance) / 100 * 1000;
        return results[0];
    }
}