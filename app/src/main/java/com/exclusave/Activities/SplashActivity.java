package com.exclusave.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.MyChatService;
import com.exclusave.BuildConfig;
import com.exclusave.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {


    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String LogIn_Status;
    String deviceToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedpreferences = BaseClass.sharedPreferances(SplashActivity.this);
        editor = BaseClass.sharedPreferancesEditor(SplashActivity.this);

        SharedPreferences sharedPreferencesLang = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        String lang = sharedPreferencesLang.getString("language", "");
        Log.e("lang", sharedPreferencesLang.getString("language", "") + "nullll");

        Configuration configuration;
        configuration = this.getResources().getConfiguration();


        if (lang.equals("en")){
            String languageToLoad = "en";
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            configuration.locale = locale;
            configuration = this.getResources().getConfiguration();
            configuration.setLayoutDirection(new Locale("en"));
            this.getResources().updateConfiguration(configuration, this.getResources().getDisplayMetrics());
        }else if (lang.equals("ar")){
            String languageToLoad = "ar";
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            configuration.locale = locale;
            configuration = this.getResources().getConfiguration();
            configuration.setLayoutDirection(new Locale("ar"));
            this.getResources().updateConfiguration(configuration, this.getResources().getDisplayMetrics());
        }

        deviceToken = sharedpreferences.getString("DeviceToken", "");

        if (TextUtils.isEmpty(deviceToken)) {
//            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SplashActivity.this, new OnSuccessListener<InstanceIdResult>() {
//                @Override
//                public void onSuccess(InstanceIdResult instanceIdResult) {
//                    String newToken = instanceIdResult.getToken();
//                    Log.e("newToken", newToken);
//                        editor.putString("DeviceToken", newToken);
//                        editor.commit();
//                }
//            });

            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w("TaskError", "getInstanceId failed", task.getException());
                                return;
                            }

                            // Get new Instance ID token
                            String newToken = task.getResult().getToken();
                            Log.e("newToken", newToken);
                            editor.putString("DeviceToken", newToken);
                            editor.commit();

                        }
                    });
        }

        LogIn_Status = sharedpreferences.getString("LOGIN_STATUS", "");

        Log.e("getresult", ""+sharedpreferences.getString("ApiToken", " "));

        //Splash Function Call
        splashFunction();

        Intent i = new Intent(SplashActivity.this, MyChatService.class);
        startService(i);

    }

    // Splash Function
    private void splashFunction(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (LogIn_Status.equals("YES")){
                    getUserDetails();
                }else{
                    generateTokenByApi();
                }

            }
        },5000);

    }

    // Get User Details Function
    private void getUserDetails() {
        String language = BaseClass.checkLanguageFunction(SplashActivity.this);
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", ""));

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.getUserDetail + "?UserID=" + sharedpreferences.getString("UserID", "") + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE+"&Language="+language, SplashActivity.this, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                Log.e("UserDetailsResponse", result + " ");
                if (ERROR.isEmpty()) {
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            JSONObject user_info = jsonObject.getJSONObject("user_info");
//
                            String UserID = user_info.getString("UserID");
                            String AuthToken = user_info.getString("AuthToken");
                            String CardID = user_info.getString("CardID");
                            String CardNumber = user_info.getString("CardNumber");
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

                            editor.putString("ApiToken", AuthToken);
                            editor.putString("UserID", UserID);
                            editor.putString("isLoggedIn", "1").commit();
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

                            Log.e("familyMemberLog", sharedpreferences.getString("IsFamilyMember", ""));

                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                         //   mEditor.putString("USER_ID", CardUserId).commit();

                        } else {
                            String message = jsonObject.getString("message");
                            Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SplashActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Generate Token Function
    public void generateTokenByApi() {

        String language = BaseClass.checkLanguageFunction(SplashActivity.this);
        Map<String, String> body = new HashMap<>();
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
        body.put("Language",language);

        HashMap<String, String> header = new HashMap<>();
        header.put("Newtoken", String.valueOf(true));
//        header.put("UserID", sharedpreferences.getString("UserID", " "));

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.GENERATETOKEN, SplashActivity.this, body, header, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
//                Log.e("GetTokenError", String.valueOf(ERROR));

                if (ERROR.isEmpty()) {
                    Log.e("GetTokenResponse", String.valueOf(result));
                    try {
                        JSONObject mainObject = new JSONObject(String.valueOf(result));
                        int status = mainObject.getInt("status");
                        if (status == 200) {
//                            editor.clear().commit();
                            String token = mainObject.getString("token");
                            editor.putString("ApiToken", token).commit();
                            editor.putString("isLoggedIn", "0").commit();

                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            String message = mainObject.getString("message");
                            Toast.makeText(SplashActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SplashActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
