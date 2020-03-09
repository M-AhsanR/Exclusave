package com.exclusave.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Locale;
import java.util.Map;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor mEditor;
    public static final String MyPREFERENCES = "MyPrefs";

    SharedPreferences sharedpreferences_main;
    SharedPreferences.Editor editor;

    Button registerNowBtn, btn_login;
    ImageView testClick;
    TextView view_usage, forgetpass;
    EditText username, password;
    CheckBox checkBox;
    TextView change_langujage;
    String deviceToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedpreferences_main = BaseClass.sharedPreferances(LoginActivity.this);
        editor = BaseClass.sharedPreferancesEditor(LoginActivity.this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mEditor = sharedpreferences.edit();

        initializeViews();
        initializeClickListeners();

        deviceToken = sharedpreferences_main.getString("DeviceToken", "");
        Log.e("deviceToken", deviceToken);

        Configuration configuration;
        configuration = this.getResources().getConfiguration();
        if (configuration.getLayoutDirection() == 1) {
            change_langujage.setText("English");
        } else if (configuration.getLayoutDirection() == 0) {
            change_langujage.setText("عربى");
        }

        registerNowBtn.setAllCaps(false);
        SpannableString span1 = new SpannableString(this.getResources().getString(R.string.registerNow));
        span1.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.orange)), 0, span1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString span2 = new SpannableString(getString(R.string.newUser));
        span2.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.black)), 0, span2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        CharSequence finalText = TextUtils.concat(span2, " ", span1);

        registerNowBtn.setText(finalText);
    }

    private void initializeClickListeners() {
        registerNowBtn.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        testClick.setOnClickListener(this);
        view_usage.setOnClickListener(this);
        forgetpass.setOnClickListener(this);
        change_langujage.setOnClickListener(this);
    }

    private void initializeViews() {
        registerNowBtn = findViewById(R.id.registerNowBtn);
        btn_login = findViewById(R.id.btn_login);
        testClick = findViewById(R.id.testClick);
        view_usage = findViewById(R.id.txt_view_usage);
        username = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        forgetpass = findViewById(R.id.forgetpassword);
        checkBox = findViewById(R.id.login_saved_check);
        change_langujage = findViewById(R.id.change_language);
//        btn_login.setBackgroundDrawable( new DrawableGradient(new int[] { 0xff666666, 0xff111111, 0xffffffff }, 8));
    }

    @Override
    public void onClick(View v) {
        if (v == registerNowBtn) {
            startActivity(new Intent(LoginActivity.this, SignUpTesting.class));
//            finish();
        } else if (v == btn_login) {
            login();
        } else if (v == view_usage) {
            Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
//            intent1.putExtra("ID", "2");
            mEditor.putString("isLoggedIn", "0").commit();
            startActivity(intent1);
//            finish();
        } else if (v == testClick) {
        } else if (v == forgetpass) {
            startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
        } else if (v == change_langujage) {
            Configuration configuration;
            configuration = this.getResources().getConfiguration();
            if (configuration.getLayoutDirection() == 1) {
                change_langujage.setText("English");
            } else if (configuration.getLayoutDirection() == 0) {
                change_langujage.setText("عربى");
            }
            if (change_langujage.getText().toString().equals("عربى")) {
                change_langujage.setText("English");
                editor.putString("language", "ar");
                editor.commit();
                Log.e("langu", sharedpreferences_main.getString("language", "") + "eng");
                String languageToLoad = "ar";
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                configuration.locale = locale;
                configuration = this.getResources().getConfiguration();
                configuration.setLayoutDirection(new Locale("ar"));
                this.getResources().updateConfiguration(configuration, this.getResources().getDisplayMetrics());
            } else if (change_langujage.getText().toString().equals("English")) {
                change_langujage.setText("عربى");
                editor.putString("language", "en");
                editor.commit();
                Log.e("langu", sharedpreferences_main.getString("language", "") + "arab");
                String languageToLoad = "en";
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                configuration.locale = locale;
                configuration = this.getResources().getConfiguration();
                configuration.setLayoutDirection(new Locale("en"));
                this.getResources().updateConfiguration(configuration, this.getResources().getDisplayMetrics());
            }

            Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
            startActivity(intent);
            finishAffinity();

        }
    }

    private void login() {
        String language = BaseClass.checkLanguageFunction(LoginActivity.this);
        Animation shake = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);
        if (username.getText().toString().isEmpty() || username.getText().toString().startsWith(" ")) {
            username.startAnimation(shake);
            Toast.makeText(LoginActivity.this, this.getResources().getString(R.string.InvalidEmail), Toast.LENGTH_LONG).show();
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(username.getText().toString()).matches()) {
            username.startAnimation(shake);
            Toast.makeText(LoginActivity.this, this.getResources().getString(R.string.InvalidEmail), Toast.LENGTH_LONG).show();
        } else if (password.getText().toString().isEmpty() || password.getText().toString().startsWith(" ")) {
            password.startAnimation(shake);
            Toast.makeText(LoginActivity.this, this.getResources().getString(R.string.EnterPassword), Toast.LENGTH_LONG).show();
        } else {

            BaseClass.showCustomLoader(LoginActivity.this);

            Map<String, String> body = new HashMap<String, String>();
            body.put("Email", username.getText().toString());
            body.put("Password", password.getText().toString());
            body.put("DeviceType", "Android");
            body.put("DeviceToken", deviceToken);
            body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
            body.put("OS", Build.VERSION.RELEASE);
            body.put("Language",language);

            Log.e("deviceToken", body.toString());

            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

            ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.LOGIN, LoginActivity.this, body, headers, new ServerCallback() {
                @Override
                public void onSuccess(String result, String ERROR) {

                    if (ERROR.isEmpty()) {
                        Log.e("LoginResponse", result);
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(result));
                            int status = jsonObject.getInt("status");
                            String message = jsonObject.getString("message");
                            if (status == 200) {

                                JSONObject user = jsonObject.getJSONObject("user_info");
                                String UserID = user.getString("UserID");
                                String UpdatedBy = user.getString("UpdatedBy");
                                String UpdatedAt = user.getString("UpdatedAt");
                                String SortOrder = user.getString("SortOrder");
                                String RoleID = user.getString("RoleID");
                                String Password = user.getString("Password");
                                String OTP = user.getString("OTP");
                                String OS = user.getString("OS");
                                String OnlineStatus = user.getString("OnlineStatus");
                                String Notification = user.getString("Notification");
                                String Mobile = user.getString("Mobile");
                                String MiddleName = user.getString("MiddleName");
                                String LastName = user.getString("LastName");
                                String IsVerified = user.getString("IsVerified");
                                String IsActive = user.getString("IsActive");
                                String Image = user.getString("Image");
                                String IDImage = user.getString("IDImage");
                                String Hide = user.getString("Hide");
                                String FirstName = user.getString("FirstName");
                                String Email = user.getString("Email");
                                String DeviceType = user.getString("DeviceType");
                                String DeviceToken = user.getString("DeviceToken");
                                String CreatedAt = user.getString("CreatedAt");
                                String CurrencySymbol = user.getString("CurrencySymbol");
                                String Currency = user.getString("Currency");
                                String CreatedBy = user.getString("CreatedBy");
                                String CoverImage = user.getString("CoverImage");
                                String CompressedImage = user.getString("CompressedImage");
                                String CompressedCoverImage = user.getString("CompressedCoverImage");
                                String CityTitle = user.getString("CityTitle");
                                String CityID = user.getString("CityID");
                                String CardNumber = user.getString("CardNumber");
                                String CardID = user.getString("CardID");
                                String AuthToken = user.getString("AuthToken");
                                String DateOfBirth = user.getString("DateOfBirth");
                                String Interest = user.getString("Interest");
                                String CompanyName = user.getString("CompanyName");
                                String Position = user.getString("Position");
                                String Gender = user.getString("Gender");
                                String ProfileCompletedPercentage = user.getString("ProfileCompletedPercentage");
                                String InterestTitle = user.getString("InterestTitle");
                                String CouponID = user.getString("CouponID");
                                String CardBackgroundImage = user.getString("CardBackgroundImage");
                                String CardImage = user.getString("CardImage");
                                String FamilyMemberDob = user.getString("FamilyMemberDob");
                                String FamilyMemberFirstName = user.getString("FamilyMemberFirstName");
                                String FamilyMemberGender = user.getString("FamilyMemberGender");
                                String FamilyMemberLastName = user.getString("FamilyMemberLastName");
                                String FamilyMemberMiddleName = user.getString("FamilyMemberMiddleName");
                                String FamilyMemberMobile = user.getString("FamilyMemberMobile");
                                String PackageColorLower = user.getString("PackageColorLower");
                                String PackageColorUpper = user.getString("PackageColorUpper");
                                String FamilyMemberImage = user.getString("FamilyMemberImage");
                                String IsFamilyMember = user.getString("IsFamilyMember");
                                String TotalSaving = user.getString("TotalSaving");
                                String FontColor = user.getString("FontColor");

                                mEditor.putString("FullName", FirstName + " " + MiddleName + " " + LastName);
                                mEditor.putString("FirstName", FirstName);
                                mEditor.putString("MiddleName", MiddleName);
                                mEditor.putString("LastName", LastName);
                                mEditor.putString("Email", Email);
                                mEditor.putString("Image", Image);
                                mEditor.putString("Mobile", Mobile);
                                mEditor.putString("CityTitle", CityTitle);
                                mEditor.putString("DateOfBirth", DateOfBirth);
                                mEditor.putString("Interest", Interest);
                                mEditor.putString("CompanyName", CompanyName);
                                mEditor.putString("Position", Position);
                                mEditor.putString("CityID", CityID);
                                mEditor.putString("Gender", Gender);
                                mEditor.putString("ProfileCompletedPercentage", ProfileCompletedPercentage);
                                mEditor.putString("InterestTitle", InterestTitle);
                                mEditor.putString("CardID", CardID);
                                mEditor.putString("CouponID", CouponID);
                                mEditor.putString("CardImage", CardImage);
                                mEditor.putString("CardBackgroundImage", CardBackgroundImage);
                                mEditor.putString("FamilyMemberDob", FamilyMemberDob);
                                mEditor.putString("FamilyMemberFirstName", FamilyMemberFirstName);
                                mEditor.putString("FamilyMemberGender", FamilyMemberGender);
                                mEditor.putString("FamilyMemberLastName", FamilyMemberLastName);
                                mEditor.putString("FamilyMemberMiddleName", FamilyMemberMiddleName);
                                mEditor.putString("FamilyMemberMobile", FamilyMemberMobile);
                                mEditor.putString("IDImage", IDImage);
                                mEditor.putString("PackageColorLower", PackageColorLower);
                                mEditor.putString("PackageColorUpper", PackageColorUpper);
                                mEditor.putString("FamilyMemberImage", FamilyMemberImage);
                                mEditor.putString("IsFamilyMember", IsFamilyMember);
                                mEditor.putString("TotalSaving", TotalSaving);
                                mEditor.putString("FontColor", FontColor);

                                Log.e("UserId", UserID);
                                mEditor.putString("UserID", UserID).commit();
                                mEditor.putString("ApiToken", AuthToken).commit();
                                if (checkBox.isChecked()) {
                                    mEditor.putString("LOGIN_STATUS", "YES");
                                } else if (!checkBox.isChecked()) {
                                    mEditor.putString("LOGIN_STATUS", "NO");
                                }
                                mEditor.commit();

                                BaseClass.dialog.dismiss();

                                Toast.makeText(LoginActivity.this,getApplicationContext().getResources().getString(R.string.loginsuccessful), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                mEditor.putString("isLoggedIn", "1").commit();
                                startActivity(intent);
                                finish();

                            } else {
                                BaseClass.dialog.dismiss();
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            BaseClass.dialog.dismiss();
                            e.printStackTrace();
                        }
                    } else {
                        BaseClass.dialog.dismiss();
                        Toast.makeText(LoginActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

//    public class DrawableGradient extends GradientDrawable {
//        DrawableGradient(int[] colors, int cornerRadius) {
//            super(Orientation.LEFT_RIGHT, colors);
//
//            try {
//                this.setShape(GradientDrawable.RECTANGLE);
//                this.setGradientType(GradientDrawable.LINEAR_GRADIENT);
//                this.setCornerRadius(cornerRadius);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
////        public DrawableGradient SetTransparency(int transparencyPercent) {
////            this.setAlpha(255 - ((255 * transparencyPercent) / 100));
////
////            return this;
////        }
//    }

}