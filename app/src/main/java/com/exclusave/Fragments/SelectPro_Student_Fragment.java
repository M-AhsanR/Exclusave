package com.exclusave.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.exclusave.Activities.ShoppingActivity;
import com.exclusave.Activities.Shopping_withoffers_Activity;
import com.exclusave.Adapters.SelectPro_Student_Adapter;
import com.exclusave.Adapters.StudFragPagerAdapter;
import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.models.InterestsData;
import com.exclusave.models.SelectPro_Student_Model;
import com.exclusave.models.StoresPagerData;
import com.exclusave.BuildConfig;
import com.exclusave.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectPro_Student_Fragment extends Fragment implements View.OnClickListener {

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor mEditor;
    public static final String MyPREFERENCES = "MyPrefs";

    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ViewPager pager;
    ArrayList<SelectPro_Student_Model> list = new ArrayList<>();
    ArrayList<InterestsData> categoriesData = new ArrayList<>();
    Button allBtn;
    ArrayList<StoresPagerData> storesPagerData = new ArrayList<>();

    protected static final String TAG = "myresponse";
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selectpro_student, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mEditor = sharedpreferences.edit();

        initializeViews();
        getCategories();
        getStoresApiCall();
        displayLocationSettingsRequest(getActivity());

        if (sharedpreferences.getString("isLoggedIn", "").equals("1")) {
            allBtn.setBackgroundResource(R.drawable.black_btn);
        }

        Log.e("shared", sharedpreferences.getString("isLoggedIn", ""));

        SelectPro_Student_Model model;

        for (int i = 0; i < 9; i++) {
            model = new SelectPro_Student_Model("", "");
            list.add(model);
        }


//        StudFragPagerAdapter studFragPagerAdapter = new StudFragPagerAdapter(getActivity(), list);
//        pager.setAdapter(studFragPagerAdapter);
//        pager.setPageMargin(40);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser == true && getActivity() != null) {
            nestedScrollView.setNestedScrollingEnabled(true);
            recyclerView.setNestedScrollingEnabled(false);
        }

        if(sharedpreferences != null){
            if(sharedpreferences.getString("isLoggedIn", "").equals("0")){
                if(isVisibleToUser == true){
                    Log.e("here?", "yess");
                    BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.navigation);
                    ColorStateList iconsColorStates;
                    iconsColorStates = new ColorStateList(
                            new int[][]{
                                    new int[]{-android.R.attr.state_checked},
                                    new int[]{android.R.attr.state_checked}
                            },
                            new int[]{
                                    Color.parseColor("#FFFFFF"),
                                    Color.parseColor("#E9341D")
                            });

                    bottomNavigationView.setItemIconTintList(iconsColorStates);
                }
            }
        }

    }

    private void initializeViews() {
        recyclerView = getView().findViewById(R.id.rv_products);
        pager = getView().findViewById(R.id.pager);
        allBtn = getView().findViewById(R.id.allBtn);
        nestedScrollView = getView().findViewById(R.id.nested);


        allBtn.setOnClickListener(this);
        nestedScrollView.setNestedScrollingEnabled(true);
        recyclerView.setNestedScrollingEnabled(false);

        if(sharedpreferences.getString("isLoggedIn", "").equals("0")){
            allBtn.setBackgroundDrawable( new BaseClass.DrawableGradient(new int[] { Color.parseColor("#DF5926"),  Color.parseColor("#EA7724"), Color.parseColor("#EA7724")}, 10));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.allBtn:
                if(sharedpreferences.getString("isLoggedIn", "").equals("1")){
                    Intent intent = new Intent(getActivity(), Shopping_withoffers_Activity.class);
                    intent.putExtra("TYPE", "all");
                    startActivity(intent);
                    break;
                }else {
                    Intent intent = new Intent(getActivity(), ShoppingActivity.class);
                    intent.putExtra("TYPE", "all");
                    intent.putExtra("CORPORATE","no");
                    startActivity(intent);
                    break;
                }
        }
    }

    private void getCategories() {

        BaseClass.showCustomLoader(getActivity());
        String language = BaseClass.checkLanguageFunction(getActivity());
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));
        Log.e("VerifyToken", sharedpreferences.getString("ApiToken", " "));

        String URL = "";
        if (sharedpreferences.getString("isLoggedIn", "").equals("0")) {
            URL = Constants.URL.BASE_URL + "categories?AndroidAppVersion=" + BuildConfig.VERSION_CODE+"&Language="+language;
        } else if (sharedpreferences.getString("isLoggedIn", "").equals("1")) {
            URL = Constants.URL.CATEGORIES + sharedpreferences.getString("UserID", "") + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE+"&Language="+language;
        }

        ApiModelClass.GetApiResponse(Request.Method.GET, URL, getActivity(), body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                if (ERROR.isEmpty()) {
                    Log.e("GetInterestsResp", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {
                            BaseClass.dialog.dismiss();
                            JSONArray citiesArray = jsonObject.getJSONArray("categories");
                            for (int i = 0; i < citiesArray.length(); i++) {
                                JSONObject categoriesObj = citiesArray.getJSONObject(i);
//
                                String BackgroundImage = categoriesObj.getString("BackgroundImage");
                                String CategoryID = categoriesObj.getString("CategoryID");
                                String Image = categoriesObj.getString("Image");
                                String Title = categoriesObj.getString("Title");
                                String SortOrder = categoriesObj.getString("SortOrder");
                                String GoldImage = categoriesObj.getString("GoldImage");
                                categoriesData.add(new InterestsData(BackgroundImage, CategoryID, Image, Title,SortOrder,GoldImage,"no"));
                            }

                            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                            SelectPro_Student_Adapter adapter = new SelectPro_Student_Adapter(getActivity(), categoriesData, new SelectPro_Student_Adapter.CustomItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {

                                    if (sharedpreferences.getString("isLoggedIn", "").equals("1")) {
                                        Intent intent = new Intent(getActivity(), Shopping_withoffers_Activity.class);
                                        intent.putExtra("CATID", categoriesData.get(position).getCategoryID());
                                        intent.putExtra("TYPE", "single");
                                        startActivity(intent);
                                    } else if (sharedpreferences.getString("isLoggedIn", "").equals("0")) {
                                        Intent intent = new Intent(getActivity(), ShoppingActivity.class);
                                        intent.putExtra("CATID", categoriesData.get(position).getCategoryID());
                                        intent.putExtra("TYPE", "single");
                                        intent.putExtra("CORPORATE","no");
                                        startActivity(intent);
                                    }

                                }
                            });
                            recyclerView.setAdapter(adapter);

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

    private void getStoresApiCall() {
        String language = BaseClass.checkLanguageFunction(getActivity());
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

        String URL = "";
        if (sharedpreferences.getString("isLoggedIn", "").equals("0")) {
            URL = Constants.URL.BASE_URL + "sponsoredStores?AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&CardID=1" + "&Language="+language;
        } else if (sharedpreferences.getString("isLoggedIn", "").equals("1")) {
            URL = Constants.URL.sponsoredStores + sharedpreferences.getString("UserID", "") + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&CardID=" + sharedpreferences.getString("CardID", "") + "&OrderBy=ASC"+ "&Language="+language;
        }

        ApiModelClass.GetApiResponse(Request.Method.GET, URL, getActivity(), body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                if (ERROR.isEmpty()) {
                    Log.e("GetStudentStoresResp", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            JSONArray storearray = jsonObject.getJSONArray("stores");

                            for (int i = 0; i < storearray.length(); i++) {

                                JSONObject object = storearray.getJSONObject(i);

                                String Address = object.getString("Address");
                                String CategoryID = object.getString("CategoryID");
                                String CategoryTitle = object.getString("CategoryTitle");
                                String CityID = object.getString("CityID");
                                String CityTitle = object.getString("CityTitle");
                                String CoverImage = object.getString("CoverImage");
                                String CreatedAt = object.getString("CreatedAt");
                                String CreatedBy = object.getString("CreatedBy");
                                String Distance = object.getString("Distance");
                                String DistrictID = object.getString("DistrictID");
                                String DistrictTitle = object.getString("DistrictTitle");
                                String Hide = object.getString("Hide");
                                String Email = object.getString("Email");
                                String FirstName = object.getString("FirstName");
                                String GoldImage = object.getString("GoldImage");
                                String Image = object.getString("Image");
                                String IsActive = object.getString("IsActive");
                                String LastName = object.getString("LastName");
                                String Latitude = object.getString("Latitude");
                                String Longitude = object.getString("Longitude");
                                String MiddleName = object.getString("MiddleName");
                                String Mobile = object.getString("Mobile");
                                String OrangeImage = object.getString("OrangeImage");
                                String ParentID = object.getString("ParentID");
//                                String PromotionID = object.getString("PromotionID");
                                String SortOrder = object.getString("SortOrder");
                                String StoreID = object.getString("StoreID");
                                String StoreTextID = object.getString("StoreTextID");
                                String SystemLanguageID = object.getString("SystemLanguageID");
                                String Title = object.getString("Title");
                                String UpdatedAt = object.getString("UpdatedAt");
                                String UpdatedBy = object.getString("UpdatedBy");
                                String CategoryImage = object.getString("CategoryImage");
                                String UserID = object.getString("UserID");
                                String corporate = "no";

                                storesPagerData.add(new StoresPagerData(Address, CategoryID, CategoryTitle, CityID, CityTitle, CoverImage, CreatedAt, CreatedBy, DistrictID, Hide,
                                        GoldImage, Image, IsActive, Latitude, Longitude, SortOrder, StoreID, StoreTextID, SystemLanguageID, Title, UpdatedAt, UpdatedBy,Distance,
                                        DistrictTitle,Email,FirstName,LastName,MiddleName,Mobile,OrangeImage,ParentID,"", CategoryImage,UserID,corporate));
                            }

                            Log.e("storesSize", storesPagerData.size() + " ");
                            StudFragPagerAdapter studFragPagerAdapter = new StudFragPagerAdapter(getActivity(), storesPagerData);
                            pager.setAdapter(studFragPagerAdapter);
                            pager.setPageMargin(40);
//                            indicatorPager.setViewPager(viewPager);


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

    private boolean displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
        return true;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }
}
