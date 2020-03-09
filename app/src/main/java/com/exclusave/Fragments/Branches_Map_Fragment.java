package com.exclusave.Fragments;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.exclusave.Adapters.Branches_list_Adapter;
import com.exclusave.models.BranchesList_Data;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.GpsTracker;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.BuildConfig;
import com.exclusave.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Branches_Map_Fragment extends Fragment implements OnMapReadyCallback {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private FusedLocationProviderClient client;

    MapView mapView;
    GpsTracker gpsTracker;
    Double latitude = 0.0, longitude = 0.0;
    private GoogleMap googleMap;
    String user_id;
    Marker marker;
    private static final int REQUEST_LOCATION_CODE = 99;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_branches, container, false);
        sharedPreferences = BaseClass.sharedPreferances(getActivity());
        editor = BaseClass.sharedPreferancesEditor(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getLocations();

        if(sharedPreferences.getString("MapType", "").equals("Stores")){
            branchesMarkerApiCall();
        }else if(sharedPreferences.getString("MapType", "").equals("Branches")) {
            branchesListApiCall();
        }
        return view;
    }

    public boolean checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.e("result", "is called");
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            }
            return false;

        } else
            return true;
    }

    private void getLocations() {
        if (ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    currentLocationsMarking(latitude, longitude);
                   // Toast.makeText(getActivity(), "lat " + latitude + " long " + longitude, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // enableButtons();
            googleMap.setMyLocationEnabled(true);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e("result", "is called");
        switch (requestCode) {
            case REQUEST_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocations();
                    // permission granted
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        getLocations();
                    }
                } else {
                    Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
        }

    }

    public void currentLocationsMarking(final Double latitude, final Double longitude) {
        //  if (latitude != null && longitude != null) {

        if (marker != null) {
            marker.remove();
        }
        LatLng latLng = new LatLng(latitude, longitude);
        try {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Me");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            marker = googleMap.addMarker(markerOptions);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,14.2f));
            //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.2f));

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void apiLocationsMarking(final Double latitude, final Double longitude, String title) {
        //  if (latitude != null && longitude != null) {

        LatLng latLng = new LatLng(latitude, longitude);
        try {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(title);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            marker = googleMap.addMarker(markerOptions);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void branchesMarkerApiCall() {
        String language = BaseClass.checkLanguageFunction(getActivity());
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        String URL = "";
        if (sharedPreferences.getString("isLoggedIn", "").equals("0")) {
            URL = Constants.URL.BASE_URL + "stores?AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&CardID=1" + "&OrderBy=DESC"+"&Language="+language;
        } else if (sharedPreferences.getString("isLoggedIn", "").equals("1")) {
            URL = Constants.URL.STORE + sharedPreferences.getString("UserID", "") + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&OrderBy=DESC"+"&Language="+language;
            Log.e("GetBranchesResponse", " "+URL);
        }

        ApiModelClass.GetApiResponse(Request.Method.GET, URL, getActivity(), body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                if (ERROR.isEmpty()) {
                    Log.e("GetBranchesResponse", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            JSONArray storearray = jsonObject.getJSONArray("stores");

                            for (int i=0; i<storearray.length(); i++){

                                JSONObject object = storearray.getJSONObject(i);

                                String Address = object.getString("Address");
                                String CategoryID = object.getString("CategoryID");
                                String CategoryTitle = object.getString("CategoryTitle");
                                String CityID = object.getString("CityID");
                                String CityTitle = object.getString("CityTitle");
                                String CoverImage = object.getString("CoverImage");
                                String CreatedAt = object.getString("CreatedAt");
                                String CreatedBy = object.getString("CreatedBy");
                                String DistrictID = object.getString("DistrictID");
                                String DistrictTitle = object.getString("DistrictTitle");
                                String Email = object.getString("Email");
                                String FacebookLink = object.getString("FacebookLink");
                                String FirstName = object.getString("FirstName");
                                String Hide = object.getString("Hide");
                                String Image = object.getString("Image");
                                String InstagramLink = object.getString("InstagramLink");
                                String IsActive = object.getString("IsActive");
                                String LastName = object.getString("LastName");
                                String Latitude = object.getString("Latitude");
                                String Longitude = object.getString("Longitude");
                                String MiddleName = object.getString("MiddleName");
                                String Mobile = object.getString("Mobile");
                                String ParentID = object.getString("ParentID");
                                String SortOrder = object.getString("SortOrder");
                                String StoreID = object.getString("StoreID");
                                String StoreTextID = object.getString("StoreTextID");
                                String SystemLanguageID = object.getString("SystemLanguageID");
                                String Title = object.getString("Title");
                                String TwitterLink = object.getString("TwitterLink");
                                String UpdatedAt = object.getString("UpdatedAt");
                                String UpdatedBy = object.getString("UpdatedBy");
                                String WebsiteLink = object.getString("WebsiteLink");

                                apiLocationsMarking(Double.parseDouble(Latitude),Double.parseDouble(Longitude),Title);
                            }

                        } else {
                            BaseClass.dialog.dismiss();
                            Toast.makeText(getActivity(), String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(getActivity(), ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void branchesListApiCall() {
        String language = BaseClass.checkLanguageFunction(getActivity());
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        String URL = "";
        if (sharedPreferences.getString("isLoggedIn", "").equals("0")) {
            URL = Constants.URL.BASE_URL + "stores?AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&CardID=1" + "&OrderBy=DESC" + "&Language=" + language;
        } else if (sharedPreferences.getString("isLoggedIn", "").equals("1")) {
            URL = Constants.URL.BRANCHES + sharedPreferences.getString("UserID", "") + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&OrderBy=DESC" + "&ParentID=" + sharedPreferences.getString("StoreID", "");
            Log.e("URL", URL);
        }

        ApiModelClass.GetApiResponse(Request.Method.GET, URL, getActivity(), body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                if (ERROR.isEmpty()) {
                    Log.e("BranchesResponse", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            JSONArray storearray = jsonObject.getJSONArray("stores");

                            for (int i = 0; i < storearray.length(); i++) {

                                JSONObject object = storearray.getJSONObject(i);

                                String Address = object.getString("Address");
                                String CategoryBackgroundImage = object.getString("CategoryBackgroundImage");
                                String CategoryID = object.getString("CategoryID");
                                String CategoryTitle = object.getString("CategoryTitle");
                                String CityID = object.getString("CityID");
                                String CityTitle = object.getString("CityTitle");
                                String ContactPersonName = object.getString("ContactPersonName");
                                String CoverImage = object.getString("CoverImage");
                                String CreatedAt = object.getString("CreatedAt");
                                String CreatedBy = object.getString("CreatedBy");
                                String Description = object.getString("Description");
                                String Distance = object.getString("Distance");
                                String DistrictID = object.getString("DistrictID");
                                String DistrictTitle = object.getString("DistrictTitle");
                                String Email = object.getString("Email");
                                String FacebookLink = object.getString("FacebookLink");
                                String FirstName = object.getString("FirstName");
                                String GoldImage = object.getString("GoldImage");
                                String Hide = object.getString("Hide");
                                String Image = object.getString("Image");
                                String InstagramLink = object.getString("InstagramLink");
                                String IsActive = object.getString("IsActive");
                                String IsSponsored = object.getString("IsSponsored");
                                String LastName = object.getString("LastName");
                                String Latitude = object.getString("Latitude");
                                String Longitude = object.getString("Longitude");
                                String MiddleName = object.getString("MiddleName");
                                String Mobile = object.getString("Mobile");
                                String OrangeImage = object.getString("OrangeImage");
                                String ParentID = object.getString("ParentID");
                                String SortOrder = object.getString("SortOrder");
                                String SponsoredSortOrder = object.getString("SponsoredSortOrder");
                                String SponsorshipExpiresAt = object.getString("SponsorshipExpiresAt");
                                String StoreCode = object.getString("StoreCode");
                                String StoreID = object.getString("StoreID");
                                String StoreTextID = object.getString("StoreTextID");
                                String StoreType = object.getString("StoreType");
                                String SubCategoryID = object.getString("SubCategoryID");
                                String SystemLanguageID = object.getString("SystemLanguageID");
                                String Title = object.getString("Title");
                                String TwitterLink = object.getString("TwitterLink");
                                String UpdatedAt = object.getString("UpdatedAt");
                                String UpdatedBy = object.getString("UpdatedBy");
                                String UserID = object.getString("UserID");
                                String WebsiteLink = object.getString("WebsiteLink");

                                apiLocationsMarking(Double.parseDouble(Latitude),Double.parseDouble(Longitude),Title);

//                                branchesNames.add(Title);
//                                branchesList_data.add(new BranchesList_Data(Address, CategoryID, CategoryTitle, CityID, CityTitle, CoverImage, CreatedAt, CreatedBy, DistrictID,
//                                        DistrictTitle, Email, FacebookLink, FirstName, Hide, Image, InstagramLink, IsActive, LastName, Latitude, Longitude,
//                                        MiddleName, Mobile, ParentID, SortOrder, StoreID, StoreTextID, SystemLanguageID, Title, TwitterLink, UpdatedAt, UpdatedBy, WebsiteLink));
                            }

                        } else {
                            BaseClass.dialog.dismiss();
                            Toast.makeText(getActivity(), String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(getActivity(), ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}