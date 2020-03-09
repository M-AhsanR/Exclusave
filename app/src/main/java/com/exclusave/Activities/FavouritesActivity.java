package com.exclusave.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.exclusave.Adapters.FavRecyclerAdapter;
import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.models.FavStoresData;
import com.exclusave.BuildConfig;
import com.exclusave.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FavouritesActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    RecyclerView favRecycler;
    ArrayList<FavStoresData> favStoresData = new ArrayList<>();
    TextView noFavsTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        sharedPreferences = BaseClass.sharedPreferances(FavouritesActivity.this);
        editor = BaseClass.sharedPreferancesEditor(FavouritesActivity.this);

        initializeViews();
        favStores();
    }

    private void initializeViews() {
        favRecycler = findViewById(R.id.favRecycler);
        noFavsTxt = findViewById(R.id.noFavsText);
    }

    @Override
    public void onClick(View v) {

    }

    private void favStores() {

        String language = BaseClass.checkLanguageFunction(FavouritesActivity.this);
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        Map<String, String> body = new HashMap<String, String>();
        body.put("UserID", sharedPreferences.getString("UserID", ""));
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
        body.put("Language", language);

        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));


        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.favouriteStores, FavouritesActivity.this, body, header, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {

                if (ERROR.isEmpty()) {
                    Log.e("FavStoresResp", result + " ");
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
                                String CategoryImage = object.getString("CategoryImage");
                                String CityID = object.getString("CityID");
                                String CityTitle = object.getString("CityTitle");
                                String CoverImage = object.getString("CoverImage");
                                String CreatedAt = object.getString("CreatedAt");
                                String CreatedBy = object.getString("CreatedBy");
                                String DistrictID = object.getString("DistrictID");
                                String GoldImage = object.getString("GoldImage");
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
//                                String IsAddedToFavourite = object.getString("IsAddedToFavourite");

                                favStoresData.add(new FavStoresData(Address, CategoryID, CategoryTitle,CategoryImage, CityID, CityTitle, CoverImage, CreatedAt, CreatedBy, DistrictID,GoldImage,
                                        Hide,Image, IsActive, Latitude, Longitude, SortOrder, StoreID, StoreTextID, SystemLanguageID, Title, UpdatedAt, UpdatedBy));

                            }

                            if(favStoresData.size()==0){
                                noFavsTxt.setVisibility(View.VISIBLE);
                            }else {
                                noFavsTxt.setVisibility(View.GONE);
                            }

                            favRecycler.setLayoutManager(new LinearLayoutManager(FavouritesActivity.this));
                            FavRecyclerAdapter adapter = new FavRecyclerAdapter(FavouritesActivity.this, favStoresData, new FavRecyclerAdapter.CustomItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {

                                    if (sharedPreferences.getString("isLoggedIn", "").equals("1")) {
                                        editor.putString("StoreID", favStoresData.get(position).getStoreID()).commit();
                                        Intent intent = new Intent(FavouritesActivity.this, Details_Activity.class);
                                        intent.putExtra("Title", favStoresData.get(position).getTitle());
                                        startActivity(intent);

                                    } else {
                                        Toast.makeText(FavouritesActivity.this, getApplicationContext().getResources().getString(R.string.PleaseLogin), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                            favRecycler.setAdapter(adapter);

                        } else {
                            String message = jsonObject.getString("message");
                            Toast.makeText(FavouritesActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(FavouritesActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
