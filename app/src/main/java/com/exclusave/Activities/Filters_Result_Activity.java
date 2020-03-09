package com.exclusave.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.exclusave.Adapters.WhatsNewRecyclerAdapter;
import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.models.WhatsNew_Data;
import com.exclusave.BuildConfig;
import com.exclusave.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Filters_Result_Activity extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor mEditor;

    ArrayList<WhatsNew_Data> whatsNew_dataList = new ArrayList<>();

    RecyclerView whatsNewRecycler;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<String> arrayList = new ArrayList<>();
    String URL = "";
    String sortType = "A : Z";
    ImageView search,filter;
    TextView textViewmessage;
    String sortkey = "&OrderBy=",sortvalue = "ASC";

    String cityID = "",categoryID = "",subcategoryID= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters__result_);
        initailizeView();
        sharedPreferences = BaseClass.sharedPreferances(Filters_Result_Activity.this);
        mEditor = BaseClass.sharedPreferancesEditor(Filters_Result_Activity.this);

        Bundle bundle = getIntent().getExtras();
        if (!bundle.isEmpty()) {
           sortType = bundle.getString("SORTTYPE");
        }

        cityID = Filters_Activity.publiccityID;
        categoryID = Filters_Activity.publiccategoryID;
        subcategoryID = Filters_Activity.publicsubCategoryID;

        String language = BaseClass.checkLanguageFunction(Filters_Result_Activity.this);

        if (!cityID.isEmpty() || !categoryID.isEmpty() || !subcategoryID.isEmpty()){

            Log.e("called", "called called");
            BaseClass.showCustomLoader(Filters_Result_Activity.this);
            whatsNewRecycler.removeAllViews();
            String url = "";
            if (sharedPreferences.getString("isLoggedIn", "").equals("0")) {
                url = Constants.URL.BASE_URL + "stores?AndroidAppVersion=" + BuildConfig.VERSION_CODE+"&Language="+language;
            } else if (sharedPreferences.getString("isLoggedIn", "").equals("1")) {
                if (sortType.equals("A:Z")){
                    sortkey = "&OrderBy=";
                    sortvalue = "ASC";
                }else if (sortType.equals("Discount")){
                    sortkey = "&RatingFilter=";
                    sortvalue = "ASC";
                }else if (sortType.equals("Popular")){
                    sortkey = "&DiscountFilter=";
                    sortvalue = "ASC";
                }
                url = Constants.URL.STORE + sharedPreferences.getString("UserID", "") + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE + sortkey+sortvalue + "&CardID=" +  sharedPreferences.getString("CardID", "")+"&CategoryID="+ categoryID + "&SubCategoryID=" +subcategoryID+"&CityID="+cityID+"&Language="+language+"&OrderByField=stores_text.Title";
            }
            whatsNewApiCall(url);
        }

        if (sharedPreferences.getString("isLoggedIn", "").equals("0")) {
            URL = Constants.URL.BASE_URL + "stores?AndroidAppVersion=" + BuildConfig.VERSION_CODE +"&OrderBy=DESC"+"&Language="+language;
        } else if (sharedPreferences.getString("isLoggedIn", "").equals("1")) {
            URL = Constants.URL.STORE + sharedPreferences.getString("UserID", "") + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE +"&OrderBy=ASC"+"&Language="+language+"&OrderByField=stores_text.Title";
        }

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        filtersResultApiCallRefresh(URL);
                    }
                }
        );
    }

    private void initailizeView() {
        whatsNewRecycler =findViewById(R.id.whatsNewRecycler);
        whatsNewRecycler.setItemViewCacheSize(25);
        whatsNewRecycler.setDrawingCacheEnabled(true);
        whatsNewRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        swipeRefreshLayout = findViewById(R.id.order_swipeRefresh);
        search = findViewById(R.id.search);
        filter = findViewById(R.id.filter);
        textViewmessage = findViewById(R.id.hiddentext);

        search.setOnClickListener(this);
        filter.setOnClickListener(this);
    }

    private void filtersResultApiCallRefresh(String URL) {
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.GET, URL, Filters_Result_Activity.this, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                if (ERROR.isEmpty()) {
                    Log.e("GetWhatsResponse", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {
                            whatsNew_dataList.clear();
                            swipeRefreshLayout.setRefreshing(false);
                            BaseClass.dialog.dismiss();
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
                                String DistrictID = object.getString("DistrictID");
                                String Hide = object.getString("Hide");
                                String Image = object.getString("Image");
                                String IsActive = object.getString("IsActive");
                                String Latitude = object.getString("Latitude");
                                String Longitude = object.getString("Longitude");
                                String SortOrder = object.getString("SortOrder");
                                String StoreID = object.getString("StoreID");
                                String StoreTextID = object.getString("StoreTextID");
                                String SystemLanguageID = object.getString("SystemLanguageID");
                                String Title = object.getString("Title");
                                String UpdatedAt = object.getString("UpdatedAt");
                                String UpdatedBy = object.getString("UpdatedBy");
                                Integer IsAddedToFavourite = object.getInt("IsAddedToFavourite");
                                String CategoryImage = object.getString("CategoryImage");
                                String UserID = object.getString("UserID");


                                whatsNew_dataList.add(new WhatsNew_Data(Address, CategoryID, CategoryTitle, CityID, CityTitle, CoverImage, CreatedAt, CreatedBy, DistrictID, Hide,
                                        Image, IsActive, Latitude, Longitude, SortOrder, StoreID, StoreTextID, SystemLanguageID, Title, UpdatedAt, UpdatedBy, IsAddedToFavourite, CategoryImage, UserID));

                                whatsNewRecycler.setLayoutManager(new LinearLayoutManager(Filters_Result_Activity.this));
                                WhatsNewRecyclerAdapter adapter = new WhatsNewRecyclerAdapter(Filters_Result_Activity.this, whatsNew_dataList, new WhatsNewRecyclerAdapter.CustomItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, int position) {

                                        if (sharedPreferences.getString("isLoggedIn", "").equals("1")) {
                                            mEditor.putString("StoreID", whatsNew_dataList.get(position).getStoreID()).commit();
                                            mEditor.putString("ReceiverID", whatsNew_dataList.get(position).getUserID()).commit();
                                            Intent intent = new Intent(Filters_Result_Activity.this, Details_Activity.class);
                                            intent.putExtra("Title", whatsNew_dataList.get(position).getTitle());
                                            startActivity(intent);
                                            // startActivity(new Intent(getActivity(), Details_Activity.class));
                                        } else {
                                            Toast.makeText(Filters_Result_Activity.this, getApplicationContext().getResources().getString(R.string.PleaseLogin), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                                whatsNewRecycler.setAdapter(adapter);

                            }

                        } else {
                            BaseClass.dialog.dismiss();
                            Toast.makeText(Filters_Result_Activity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(Filters_Result_Activity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void whatsNewApiCall(String URL) {
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.GET, URL, Filters_Result_Activity.this, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                if (ERROR.isEmpty()) {
                    Log.e("GetWhatsResponse", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {
                            whatsNew_dataList.clear();
                            BaseClass.dialog.dismiss();
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
                                String DistrictID = object.getString("DistrictID");
                                String Hide = object.getString("Hide");
                                String Image = object.getString("Image");
                                String IsActive = object.getString("IsActive");
                                String Latitude = object.getString("Latitude");
                                String Longitude = object.getString("Longitude");
                                String SortOrder = object.getString("SortOrder");
                                String StoreID = object.getString("StoreID");
                                String StoreTextID = object.getString("StoreTextID");
                                String SystemLanguageID = object.getString("SystemLanguageID");
                                String Title = object.getString("Title");
                                String UpdatedAt = object.getString("UpdatedAt");
                                String UpdatedBy = object.getString("UpdatedBy");
                                Integer IsAddedToFavourite = object.getInt("IsAddedToFavourite");
                                String CategoryImage = object.getString("CategoryImage");
                                String UserID = object.getString("UserID");

                                whatsNew_dataList.add(new WhatsNew_Data(Address, CategoryID, CategoryTitle, CityID, CityTitle, CoverImage, CreatedAt, CreatedBy, DistrictID, Hide,
                                        Image, IsActive, Latitude, Longitude, SortOrder, StoreID, StoreTextID, SystemLanguageID, Title, UpdatedAt, UpdatedBy, IsAddedToFavourite, CategoryImage, UserID));

                            }
                            if (whatsNew_dataList.size() == 0){
                                whatsNewRecycler.setVisibility(View.GONE);
                                swipeRefreshLayout.setVisibility(View.GONE);
                                textViewmessage.setVisibility(View.VISIBLE);
                            }
                            whatsNewRecycler.setLayoutManager(new LinearLayoutManager(Filters_Result_Activity.this));
                            WhatsNewRecyclerAdapter adapter = new WhatsNewRecyclerAdapter(Filters_Result_Activity.this, whatsNew_dataList, new WhatsNewRecyclerAdapter.CustomItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {

                                    if (sharedPreferences.getString("isLoggedIn", "").equals("1")) {
                                        mEditor.putString("StoreID", whatsNew_dataList.get(position).getStoreID()).commit();
                                        mEditor.putString("ReceiverID", whatsNew_dataList.get(position).getUserID()).commit();
                                        Intent intent = new Intent(Filters_Result_Activity.this, Details_Activity.class);
                                        intent.putExtra("Title", whatsNew_dataList.get(position).getTitle());
                                        startActivity(intent);
                                        // startActivity(new Intent(getActivity(), Details_Activity.class));
                                    } else {
                                        Toast.makeText(Filters_Result_Activity.this, getApplicationContext().getResources().getString(R.string.PleaseLogin), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                            whatsNewRecycler.setAdapter(adapter);

                        } else {
                            BaseClass.dialog.dismiss();
                            Toast.makeText(Filters_Result_Activity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(Filters_Result_Activity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == search){
            Toast.makeText(Filters_Result_Activity.this, "Mm, Working on it!", Toast.LENGTH_SHORT).show();
        }else if (view == filter){
            startActivity(new Intent(Filters_Result_Activity.this, Filters_Activity.class));
            finish();
        }
    }
}
