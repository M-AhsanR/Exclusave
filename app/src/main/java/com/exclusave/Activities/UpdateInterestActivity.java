package com.exclusave.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.models.InterestsData;
import com.exclusave.BuildConfig;
import com.exclusave.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateInterestActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ArrayList<InterestsData> interestsData = new ArrayList<>();
    ArrayList<String> interestsNames = new ArrayList<>();
    String InterestID = "";
    AutoCompleteTextView interest;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_interest);

        sharedPreferences = BaseClass.sharedPreferances(UpdateInterestActivity.this);
        editor = BaseClass.sharedPreferancesEditor(UpdateInterestActivity.this);

        interest = findViewById(R.id.interest);
        update = findViewById(R.id.update);
        update.setOnClickListener(this);
        update.setHintTextColor(this.getResources().getColor(R.color.black));
        InterestID = sharedPreferences.getString("Interest", "");
        interest.setHint(sharedPreferences.getString("InterestTitle", ""));

        getInterests();
        interestClickListener();

    }

    private void getInterests() {
        String language = BaseClass.checkLanguageFunction(UpdateInterestActivity.this);
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.CATEGORIES + sharedPreferences.getString("UserID", "") + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE +"&Language="+language, UpdateInterestActivity.this, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                if (ERROR.isEmpty()) {
                    Log.e("GetInterestsResp", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

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
                                interestsNames.add(Title);
                                interestsData.add(new InterestsData(BackgroundImage, CategoryID, Image, Title,SortOrder,GoldImage,"no"));
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateInterestActivity.this,
                                    android.R.layout.simple_list_item_1, interestsNames);
                            adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                            interest.setAdapter(adapter);

                        } else {
                            Toast.makeText(UpdateInterestActivity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(UpdateInterestActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void interestClickListener(){
        interest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseClass.hideKeyboard(interest, UpdateInterestActivity.this);
                interest.showDropDown();
            }
        });

        interest.setTextIsSelectable(true);
        interest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    InterestID = interestsData.get(position).getCategoryID();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateProfile(){
        String language = BaseClass.checkLanguageFunction(UpdateInterestActivity.this);
        Map<String, String> body = new HashMap<String, String>();

        BaseClass.showCustomLoader(UpdateInterestActivity.this);

        body.put("Interest",InterestID);
        body.put("UserID", sharedPreferences.getString("UserID", " "));
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
        body.put("Language",language);

        Log.e("body", body.toString());

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.UPDATE_PROFILE, UpdateInterestActivity.this, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {

                if (ERROR.isEmpty()) {
                    Log.e("UpdateNameResp", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        String message = jsonObject.getString("message");
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            JSONObject user_info = jsonObject.getJSONObject("user_info");

                            String Cityid = user_info.getString("CityID");
                            String CityTitle = user_info.getString("CityTitle");
                            String Email = user_info.getString("Email");
                            String FirstName = user_info.getString("FirstName");
                            String Image = user_info.getString("Image");
                            String LastName = user_info.getString("LastName");
                            String MiddleName = user_info.getString("MiddleName");
                            String Mobile = user_info.getString("Mobile");
                            String DateOfBirth = user_info.getString("DateOfBirth");
                            String Interest = user_info.getString("Interest");
                            String CompanyName = user_info.getString("CompanyName");
                            String Position = user_info.getString("Position");
                            String Gender = user_info.getString("Gender");
                            String ProfileCompletedPercentage = user_info.getString("ProfileCompletedPercentage");
                            String InterestTitle = user_info.getString("InterestTitle");
                            String CardID = user_info.getString("CardID");
                            String CouponID = user_info.getString("CouponID");
                            String CardBackgroundImage = user_info.getString("CardBackgroundImage");
                            String CardImage = user_info.getString("CardImage");
                            String FamilyMemberDob = user_info.getString("FamilyMemberDob");
                            String FamilyMemberFirstName = user_info.getString("FamilyMemberFirstName");
                            String FamilyMemberGender = user_info.getString("FamilyMemberGender");
                            String FamilyMemberLastName = user_info.getString("FamilyMemberLastName");
                            String FamilyMemberMiddleName = user_info.getString("FamilyMemberMiddleName");
                            String FamilyMemberMobile = user_info.getString("FamilyMemberMobile");
                            String IDImage = user_info.getString("IDImage");
                            String PackageColorLower = user_info.getString("PackageColorLower");
                            String PackageColorUpper = user_info.getString("PackageColorUpper");
                            String TotalSaving = user_info.getString("TotalSaving");
                            String FontColor = user_info.getString("FontColor");

                            editor.putString("FullName", FirstName + " " + MiddleName + " " + LastName);
                            editor.putString("FirstName", FirstName);
                            editor.putString("MiddleName", MiddleName);
                            editor.putString("LastName", LastName);
                            editor.putString("Email", Email);
                            editor.putString("Image", Image);
                            editor.putString("Mobile", Mobile);
                            editor.putString("CityTitle", CityTitle);
                            editor.putString("DateOfBirth", DateOfBirth);
                            editor.putString("Interest", Interest);
                            editor.putString("CompanyName", CompanyName);
                            editor.putString("Position", Position);
                            editor.putString("CityID", Cityid);
                            editor.putString("Gender", Gender);
                            editor.putString("ProfileCompletedPercentage", ProfileCompletedPercentage);
                            editor.putString("InterestTitle", InterestTitle);
                            editor.putString("CardID", CardID);
                            editor.putString("CouponID", CouponID);
                            editor.putString("CardImage", CardImage);
                            editor.putString("CardBackgroundImage", CardBackgroundImage);
                            editor.putString("FamilyMemberDob", FamilyMemberDob);
                            editor.putString("FamilyMemberFirstName", FamilyMemberFirstName);
                            editor.putString("FamilyMemberGender", FamilyMemberGender);
                            editor.putString("FamilyMemberLastName", FamilyMemberLastName);
                            editor.putString("FamilyMemberMiddleName", FamilyMemberMiddleName);
                            editor.putString("FamilyMemberMobile", FamilyMemberMobile);
                            editor.putString("IDImage", IDImage);
                            editor.putString("PackageColorLower", PackageColorLower);
                            editor.putString("PackageColorUpper", PackageColorUpper);
                            String FamilyMemberImage = user_info.getString("FamilyMemberImage");
                            String IsFamilyMember = user_info.getString("IsFamilyMember");
                            editor.putString("FamilyMemberImage", FamilyMemberImage);
                            editor.putString("IsFamilyMember", IsFamilyMember);
                            editor.putString("TotalSaving", TotalSaving);
                            editor.putString("FontColor", FontColor);
                            editor.commit();

                            Toast.makeText(UpdateInterestActivity.this,message, Toast.LENGTH_SHORT).show();
                            BaseClass.dialog.dismiss();

                        } else {
                            BaseClass.dialog.dismiss();
                            Toast.makeText(UpdateInterestActivity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(UpdateInterestActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update:
                if(InterestID.equals("")){
                    Toast.makeText(UpdateInterestActivity.this, getResources().getString(R.string.fillEmptyField), Toast.LENGTH_SHORT).show();
                    break;
                }else {
                    updateProfile();
                    break;
                }
        }
    }
}
