package com.exclusave.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ShoppingActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor mEditor;

    String catID, type, iscorporate;
    double lat, lang;
    RecyclerView recyclerView;
    TextView noItemsTxt,headingtitle;
    // ArrayList<Shopping_withoffer_Model> list = new ArrayList<>();
    ArrayList<ShoppingActivity_Data> shoppingActivity_datalist = new ArrayList<>();
    private FusedLocationProviderClient client;
    protected static final String TAG = "myresponse";
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitie_shopping);

        onRequestpermission();
        sharedpreferences = BaseClass.sharedPreferances(ShoppingActivity.this);
        mEditor = BaseClass.sharedPreferancesEditor(ShoppingActivity.this);

        client = LocationServices.getFusedLocationProviderClient(this);
        initilizeViews();
        getLocations();
        Bundle bundle = getIntent().getExtras();
        if (!bundle.isEmpty()) {
            catID = bundle.getString("CATID");
            type = bundle.getString("TYPE");
            iscorporate = bundle.getString("CORPORATE");
        }

    }

    private void initilizeViews() {
        recyclerView = findViewById(R.id.shopping_recycler);
        headingtitle = findViewById(R.id.categoryTitle);
        noItemsTxt = findViewById(R.id.noItemsTxt);
    }

    @Override
    public void onClick(View view) {

    }

    private void onRequestpermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    private void getShoppinsStatusApiCall() {
        String language = BaseClass.checkLanguageFunction(ShoppingActivity.this);
        BaseClass.showCustomLoader(ShoppingActivity.this);

        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

        String URL = "";
        if (sharedpreferences.getString("isLoggedIn", "").equals("0")) {
            if (type.equals("single")) {
                URL = Constants.URL.BASE_URL + "stores?AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&OrderBy=DESC" + "&CategoryID="+catID + "&Latitude=" + lat + "&Longitude=" + lang + "&Language=" + language;
            }else {
                URL = Constants.URL.BASE_URL + "stores?AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&OrderBy=DESC" + "&Latitude=" + lat + "&Longitude=" + lang + "&Language=" + language;
            }
//            URL = Constants.URL.BASE_URL + "stores?AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&OrderBy=DESC" + "&CategoryID="+catID + "&Latitude="+lat + "&Longitude="+lang +"&Language="+language;
        } else if (sharedpreferences.getString("isLoggedIn", "").equals("1")) {
            URL = Constants.URL.STORE + sharedpreferences.getString("UserID", "") + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&OrderBy=DESC" + "&CategoryID="+catID + "&Latitude="+lat + "&Longitude="+lang+"&Language="+language;
        }

        Log.e("URL", URL + " ");

        ApiModelClass.GetApiResponse(Request.Method.GET, URL, ShoppingActivity.this, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                if (ERROR.isEmpty()) {
                    try {

                        Log.e("GetShoppingResponse1", result);
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            shoppingActivity_datalist.clear();
                            BaseClass.dialog.dismiss();

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


                                if (type.equals("single")) {
                                    headingtitle.setText(CategoryTitle);
                                }else {
                                    headingtitle.setText(getResources().getString(R.string.All));
                                }
                                shoppingActivity_datalist.add(new ShoppingActivity_Data(Address,CategoryID,CategoryImage,CategoryTitle,CityID,CityTitle,CoverImage,CreatedAt,CreatedBy,DistrictID,
                                        DistrictTitle,Email,FacebookLink,FirstName,Hide,Image,InstagramLink,IsActive,IsAddedToFavourite,LastName,Latitude,Longitude,
                                        MiddleName,Mobile,ParentID,SortOrder,StoreID,StoreTextID,SystemLanguageID,Title,TwitterLink,UpdatedAt,UpdatedBy,WebsiteLink,Distance,Discount,DiscountType, StoreAverageRating, GoldImage, iscorporate));
                            }

                            if (shoppingActivity_datalist.size()==0){
                                noItemsTxt.setVisibility(View.VISIBLE);
                            }

                            recyclerView.setLayoutManager(new LinearLayoutManager(ShoppingActivity.this));
                            Shopping_withoffer_Adapter adapter = new Shopping_withoffer_Adapter(ShoppingActivity.this, shoppingActivity_datalist, new Shopping_withoffer_Adapter.CustomItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {
                                    if(sharedpreferences.getString("isLoggedIn", "").equals("1")){
                                        mEditor.putString("StoreID", shoppingActivity_datalist.get(position).getStoreID()).commit();
                                      //  mEditor.putString("ReceiverID", shoppingActivity_datalist.get(position).getUserID()).commit();
                                        Intent intent = new Intent(ShoppingActivity.this, Details_Activity.class);
                                        intent.putExtra("Title",shoppingActivity_datalist.get(position).getTitle());
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(ShoppingActivity.this, getResources().getString(R.string.PleaseLogin), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                            recyclerView.setAdapter(adapter);

                        } else {
                            BaseClass.dialog.dismiss();
                            Toast.makeText(ShoppingActivity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(ShoppingActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void getLocations(){
        if (ActivityCompat.checkSelfPermission(ShoppingActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(ShoppingActivity.this, getResources().getString(R.string.locationrequest), Toast.LENGTH_LONG);
            finish();
        }
        client.getLastLocation().addOnSuccessListener(ShoppingActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    lat = location.getLatitude();
                    lang = location.getLongitude();
                    getShoppinsStatusApiCall();
//                    Toast.makeText(Shopping_withoffers_Activity.this, "lat " + lat + " long " + lang, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

 }
