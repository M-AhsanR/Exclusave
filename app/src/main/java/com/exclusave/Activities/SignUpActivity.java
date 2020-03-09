package com.exclusave.Activities;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.bikomobile.circleindicatorpager.CircleIndicatorPager;
import com.exclusave.Adapters.SignUp_ViewPager_Adapter;
import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.models.CardsData;
import com.exclusave.models.CitiesData;
import com.exclusave.BuildConfig;
import com.exclusave.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    ViewPager reg_viewPager;
    CircleIndicatorPager indicatorPager;
    ArrayList<String> arrayList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor mEditor;
    ArrayList<CardsData> cardsData = new ArrayList<>();
    public static ArrayList<CitiesData> citiesData = new ArrayList<>();
    public static ArrayList<String> citiesNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initilizeViews();

        sharedPreferences = BaseClass.sharedPreferances(SignUpActivity.this);
        mEditor = BaseClass.sharedPreferancesEditor(SignUpActivity.this);

//        getCards();
//        getCities();

        arrayList.add("");
        arrayList.add("");


    }

    private void initilizeViews() {
        reg_viewPager = findViewById(R.id.registeration_pager);
        indicatorPager = findViewById(R.id.indicator);
    }

    private void getCards(){
        String language = BaseClass.checkLanguageFunction(SignUpActivity.this);
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        Log.e("header", headers.toString());

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.GETCARDS + "?AndroidAppVersion=" + BuildConfig.VERSION_CODE+"&Language="+language, SignUpActivity.this, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {

                if (ERROR.isEmpty()){
                    Log.e("GetCardsResp", result + " ");
                    try {
                        cardsData.clear();
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200){

                            JSONArray cardsArray = jsonObject.getJSONArray("cards");

                            for(int i=0; i<cardsArray.length(); i++){
                                JSONObject cardsObject = cardsArray.getJSONObject(i);
                                String CardCode = cardsObject.getString("CardCode");
                                String CardID = cardsObject.getString("CardID");
                                String CardType = cardsObject.getString("CardType");
                                String Title = cardsObject.getString("Title");

//                                cardsData.add(new CardsData(CardCode, CardID, CardType, Title));
                            }

                            SignUp_ViewPager_Adapter adapter = new SignUp_ViewPager_Adapter(SignUpActivity.this, cardsData);
                            reg_viewPager.setAdapter(adapter);
                            reg_viewPager.setPageMargin(40);

                            indicatorPager.setViewPager(reg_viewPager);

                        }else {
                            Toast.makeText(SignUpActivity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(SignUpActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getCities() {

        String language = BaseClass.checkLanguageFunction(SignUpActivity.this);
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.CITIES + "?AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&OS=" + Build.VERSION.RELEASE+"&Language="+language, SignUpActivity.this, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                if (ERROR.isEmpty()) {
                    Log.e("GetCitiesResponse", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            JSONArray citiesArray = jsonObject.getJSONArray("cities");
                            for (int i = 0; i < citiesArray.length(); i++) {
                                JSONObject jsonObject1 = citiesArray.getJSONObject(i);

                                String CityID = jsonObject1.getString("CityID");
                                String CityLat = jsonObject1.getString("CityLat");
                                String CityLong = jsonObject1.getString("CityLong");
                                String CityPlaceID = jsonObject1.getString("CityPlaceID");
                                String Title = jsonObject1.getString("Title");
                                citiesNames.add(Title);
                                citiesData.add(new CitiesData(CityID, CityLat, CityLong, CityPlaceID, Title));
                            }

//                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(SettingsActivity.this,
//                                    android.R.layout.simple_list_item_1, citiesNames);
//                            adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
//                            editCity.setAdapter(adapter);


                        } else {
                            Toast.makeText(SignUpActivity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
