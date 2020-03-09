package com.exclusave.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.exclusave.ApiStructure.GpsTracker;
import com.exclusave.R;

public class Store_Location_Activity extends AppCompatActivity implements OnMapReadyCallback {

    private FusedLocationProviderClient client;
    GoogleApiClient mGoogleApiClient;

    MapView mapView;
    GpsTracker gpsTracker;
    private GoogleMap googleMap;
    String user_id;
    Marker marker;
    private static final int REQUEST_LOCATION_CODE = 99;
    String lang;
    String lat;
    String storename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store__location_);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        client = LocationServices.getFusedLocationProviderClient(Store_Location_Activity.this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle bundle = getIntent().getExtras();
        if (!bundle.isEmpty()) {
            lat = bundle.getString("LATITUDE");
            lang = bundle.getString("LONGITUDE");
            storename = bundle.getString("STORENAME");
        }else {
            Toast.makeText(Store_Location_Activity.this, "No store available!", Toast.LENGTH_SHORT).show();
        }

//        storeLocationsMarking(Double.parseDouble(lat), Double.parseDouble(lang), storename);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;


        if (ContextCompat.checkSelfPermission(Store_Location_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // enableButtons();
            googleMap.setMyLocationEnabled(true);
        }

        storeLocationsMarking(Double.parseDouble(lat), Double.parseDouble(lang), storename);

    }

    public boolean checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(Store_Location_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Store_Location_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(Store_Location_Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(Store_Location_Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            }
            return false;

        } else
            return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                    if (ContextCompat.checkSelfPermission(Store_Location_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        //   enableButtons();
                    }
                } else {
                    Toast.makeText(Store_Location_Activity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
        }

    }

    public void storeLocationsMarking(final Double latitude, final Double longitude, String storename) {
        //  if (latitude != null && longitude != null) {

        if (marker != null) {
            marker.remove();
        }
        LatLng latLng = new LatLng(latitude, longitude);
        try {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(storename);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            marker = googleMap.addMarker(markerOptions);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,14.2f));
           // googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.2f));

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("markeroption", e.getMessage());
            Toast.makeText(Store_Location_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
