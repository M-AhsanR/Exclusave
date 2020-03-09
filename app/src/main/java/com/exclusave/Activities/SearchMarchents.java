package com.exclusave.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.exclusave.Adapters.Shopping_withoffer_Adapter;
import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.models.ShoppingActivity_Data;
import com.exclusave.BuildConfig;
import com.exclusave.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class SearchMarchents extends AppCompatActivity {

    ImageView search_btn;
    EditText searchText;
    TextView noItemsTxt;
    RecyclerView recyclerView;
    RelativeLayout progressbar;

    double lat, lang;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor mEditor;
    ArrayList<ShoppingActivity_Data> shoppingActivity_datalist = new ArrayList<>();
    private FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_marchents);
        onRequestpermission();
        sharedpreferences = BaseClass.sharedPreferances(SearchMarchents.this);
        mEditor = BaseClass.sharedPreferancesEditor(SearchMarchents.this);
        Initialization();

        client = LocationServices.getFusedLocationProviderClient(this);

        noItemsTxt.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        progressbar.setVisibility(View.GONE);

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()){
                    if (editable.toString().length() >= 2){
                        getLocations(editable.toString());
                    }
                }
            }
        });

    }

    private void Initialization(){
        search_btn = findViewById(R.id.search_btn);
        searchText = findViewById(R.id.search_text_field);
        recyclerView = findViewById(R.id.search_recycler);
        noItemsTxt = findViewById(R.id.noItemsTxt);
        progressbar = findViewById(R.id.progressbar);
    }

    private void getLocations(String title){
        if (ActivityCompat.checkSelfPermission(SearchMarchents.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(SearchMarchents.this, getResources().getString(R.string.locationrequest), Toast.LENGTH_LONG);
            finish();
        }
        client.getLastLocation().addOnSuccessListener(SearchMarchents.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    lat = location.getLatitude();
                    lang = location.getLongitude();
                    getShoppinsStatusApiCall(title);
//                    Toast.makeText(Shopping_withoffers_Activity.this, "lat " + lat + " long " + lang, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onRequestpermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    private void getShoppinsStatusApiCall(String title) {
        progressbar.setVisibility(View.VISIBLE);
        noItemsTxt.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        String language = BaseClass.checkLanguageFunction(SearchMarchents.this);
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

        String URL = "";
        if (sharedpreferences.getString("isLoggedIn", "").equals("0")) {
            URL = Constants.URL.BASE_URL + "stores?AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&OrderBy=DESC" + "&StoreTitle="+title + "&Latitude="+lat + "&Longitude="+lang +"&Language="+language;
        } else if (sharedpreferences.getString("isLoggedIn", "").equals("1")) {
            URL = Constants.URL.STORE + sharedpreferences.getString("UserID", "") + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&OrderBy=DESC" + "&StoreTitle="+title + "&Latitude="+lat + "&Longitude="+lang+"&Language="+language;
        }

        Log.e("URL", URL + " ");

        ApiModelClass.GetApiResponse(Request.Method.GET, URL, SearchMarchents.this, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                if (ERROR.isEmpty()) {
                    Log.e("GetShoppingResponseone", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            shoppingActivity_datalist.clear();

                            JSONArray storearray = jsonObject.getJSONArray("stores");

                            for (int i=0; i<storearray.length(); i++){

                                JSONObject object = storearray.getJSONObject(i);

                                String Address = object.getString("Address");
                                String CategoryID = object.getString("CategoryID");
                                String CategoryImage = object.getString("CategoryImage");
                                String CategoryTitle = object.getString("CategoryTitle");
                                String CityID = object.getString("CityID");
                                String CityTitle = object.getString("CityTitle");
                                String CoverImage = object.getString("CoverImage");
                                String CreatedAt = object.getString("CreatedAt");
                                String CreatedBy = object.getString("CreatedBy");
                                String Discount = object.getString("Discount");
                                String DiscountType = object.getString("DiscountType");
                                String DistrictID = object.getString("DistrictID");
                                String Distance = object.getString("Distance");
                                String DistrictTitle = object.getString("DistrictTitle");
                                String Email = object.getString("Email");
                                String FacebookLink = object.getString("FacebookLink");
                                String FirstName = object.getString("FirstName");
                                String Hide = object.getString("Hide");
                                String Image = object.getString("Image");
                                String InstagramLink = object.getString("InstagramLink");
                                String IsActive = object.getString("IsActive");
                                Integer IsAddedToFavourite = object.getInt("IsAddedToFavourite");
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
                                String StoreAverageRating = object.getString("StoreAverageRating");
                                String GoldImage = object.getString("GoldImage");

//                                Float distanceinKM = Float.parseFloat(Distance)/1000;

                                shoppingActivity_datalist.add(new ShoppingActivity_Data(Address,CategoryID,CategoryImage,CategoryTitle,CityID,CityTitle,CoverImage,CreatedAt,CreatedBy,DistrictID,
                                        DistrictTitle,Email,FacebookLink,FirstName,Hide,Image,InstagramLink,IsActive,IsAddedToFavourite,LastName,Latitude,Longitude,
                                        MiddleName,Mobile,ParentID,SortOrder,StoreID,StoreTextID,SystemLanguageID,Title,TwitterLink,UpdatedAt,UpdatedBy,WebsiteLink,Distance,Discount,DiscountType, StoreAverageRating,GoldImage, "no"));
                            }

                            if (shoppingActivity_datalist.size() == 0){
                                noItemsTxt.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                progressbar.setVisibility(View.GONE);
                            }else {
                                noItemsTxt.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                progressbar.setVisibility(View.GONE);
                            }

                            recyclerView.setLayoutManager(new LinearLayoutManager(SearchMarchents.this));
                            Shopping_withoffer_Adapter adapter = new Shopping_withoffer_Adapter(SearchMarchents.this, shoppingActivity_datalist, new Shopping_withoffer_Adapter.CustomItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {
                                    if(sharedpreferences.getString("isLoggedIn", "").equals("1")){
                                        mEditor.putString("StoreID", shoppingActivity_datalist.get(position).getStoreID()).commit();
                                        //  mEditor.putString("ReceiverID", shoppingActivity_datalist.get(position).getUserID()).commit();
                                        Intent intent = new Intent(SearchMarchents.this, Details_Activity.class);
                                        intent.putExtra("Title",shoppingActivity_datalist.get(position).getTitle());
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(SearchMarchents.this, getResources().getString(R.string.PleaseLogin), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                            recyclerView.setAdapter(adapter);

                        } else {
                            String message = jsonObject.getString("message");
                            Toast.makeText(SearchMarchents.this, message, Toast.LENGTH_SHORT).show();
                            progressbar.setVisibility(View.GONE);
                            noItemsTxt.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressbar.setVisibility(View.GONE);
                        noItemsTxt.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(SearchMarchents.this, ERROR, Toast.LENGTH_SHORT).show();
                    progressbar.setVisibility(View.GONE);
                    noItemsTxt.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
    }


}
