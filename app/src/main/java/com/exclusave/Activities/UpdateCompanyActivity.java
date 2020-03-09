package com.exclusave.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.BuildConfig;
import com.exclusave.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateCompanyActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    TextView company;
    Button update;
    Map<String, String> body = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_company);

        sharedPreferences = BaseClass.sharedPreferances(UpdateCompanyActivity.this);
        editor = BaseClass.sharedPreferancesEditor(UpdateCompanyActivity.this);

        company = findViewById(R.id.company);
        update = findViewById(R.id.update);
        company.setText(sharedPreferences.getString("CompanyName", ""));

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(company.getText().toString().isEmpty() || company.getText().toString().startsWith(" ")){
                    Toast.makeText(UpdateCompanyActivity.this, getResources().getString(R.string.fillEmptyField), Toast.LENGTH_SHORT).show();
                }else {
                    updateProfile();

                }
            }
        });

    }

    private void updateProfile(){
        String language = BaseClass.checkLanguageFunction(UpdateCompanyActivity.this);
        BaseClass.showCustomLoader(UpdateCompanyActivity.this);

        body.put("CompanyName", company.getText().toString());
        body.put("UserID", sharedPreferences.getString("UserID", " "));
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
        body.put("Language",language);

        Log.e("body", body.toString());

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.UPDATE_PROFILE, UpdateCompanyActivity.this, body, headers, new ServerCallback() {
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
                            String FamilyMemberImage = user_info.getString("FamilyMemberImage");
                            String IsFamilyMember = user_info.getString("IsFamilyMember");
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
                            editor.putString("FamilyMemberImage", FamilyMemberImage);
                            editor.putString("IsFamilyMember", IsFamilyMember);
                            editor.putString("TotalSaving", TotalSaving);
                            editor.putString("FontColor", FontColor);
                            editor.commit();

                            Toast.makeText(UpdateCompanyActivity.this,message, Toast.LENGTH_SHORT).show();
                            BaseClass.dialog.dismiss();

                        } else {
                            BaseClass.dialog.dismiss();
                            Toast.makeText(UpdateCompanyActivity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(UpdateCompanyActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
