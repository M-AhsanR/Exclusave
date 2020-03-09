package com.exclusave.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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

public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    LinearLayout callLinear, chatLinear, mailLinear, fbLinear, instaLinear, twitterLinear, webLinear;
    String Email,FacebookUrl,GoogleUrl,InstagramUrl,PhoneNumber,TwitterUrl,Whatsapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        sharedPreferences = BaseClass.sharedPreferances(ContactUsActivity.this);
        editor = BaseClass.sharedPreferancesEditor(ContactUsActivity.this);

        initializeViews();

//        getSiteSettings();
    }

    private void initializeViews() {
        callLinear = findViewById(R.id.callLinear);
        chatLinear = findViewById(R.id.chatLinear);
        mailLinear = findViewById(R.id.mailLinear);
        fbLinear = findViewById(R.id.fbLinear);
        instaLinear = findViewById(R.id.instaLinear);
        twitterLinear = findViewById(R.id.twitterLinear);
        webLinear = findViewById(R.id.webLinear);

        callLinear.setOnClickListener(this);
        chatLinear.setOnClickListener(this);
        mailLinear.setOnClickListener(this);
        fbLinear.setOnClickListener(this);
        instaLinear.setOnClickListener(this);
        twitterLinear.setOnClickListener(this);
        webLinear.setOnClickListener(this);
    }

    private void getSiteSettings() {
        String language = BaseClass.checkLanguageFunction(ContactUsActivity.this);
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", ""));

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.getSiteSettings + "AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&Language=EN", ContactUsActivity.this, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                if (ERROR.isEmpty()) {
                    try {
                        Log.e("SiteSettingsResp", result + " ");
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            JSONObject user_info = jsonObject.getJSONObject("site_setting");
//
                            String CloseTime = user_info.getString("CloseTime");
                            Email = user_info.getString("Email");
                            FacebookUrl = user_info.getString("FacebookUrl");
                            String Fax = user_info.getString("Fax");
                            GoogleUrl = user_info.getString("GoogleUrl");
                            InstagramUrl = user_info.getString("InstagramUrl");
                            String LinkedInUrl = user_info.getString("LinkedInUrl");
                            String OpenTime = user_info.getString("OpenTime");
                            PhoneNumber = user_info.getString("PhoneNumber");
                            String SiteImage = user_info.getString("SiteImage");
                            String SiteName = user_info.getString("SiteName");
                            String Skype = user_info.getString("Skype");
                            TwitterUrl = user_info.getString("TwitterUrl");
                            Whatsapp = user_info.getString("Whatsapp");

                        } else {
//                            DynamicToast.makeError(ContactUsActivity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
//                    Toast.makeText(SplashScreen.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openFacebook() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + FacebookUrl));
            startActivity(intent);
        } catch (Exception e) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + FacebookUrl));
            startActivity(intent);
        }
    }

    private void openWebsite() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://www."+ GoogleUrl));
        startActivity(i);
    }

    private void openInstagram() {
        if ((isPackageInstalled("com.instagram.android", getPackageManager()))) {
            String url = "https://instagram.com/" + InstagramUrl;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } else {
            Toast.makeText(ContactUsActivity.this, getResources().getString(R.string.pleaseInstallInstagram), Toast.LENGTH_SHORT).show();
        }
    }

    private void openTwitter() {
        if ((isPackageInstalled("com.twitter.android", getPackageManager()))) {
            String url = "http://twitter.com/" + TwitterUrl;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } else {
            Toast.makeText(ContactUsActivity.this, getResources().getString(R.string.pleaseInstallTwitter), Toast.LENGTH_SHORT).show();
        }
    }

    private void call(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+ "92000000"));
        startActivity(intent);
    }

    private void sendMail(){
        Intent intent=new Intent(Intent.ACTION_SEND);
        String[] recipients={Email};
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
//        intent.putExtra(Intent.EXTRA_SUBJECT,"Subject text here...");
//        intent.putExtra(Intent.EXTRA_TEXT,"Body of the content here...");
//        intent.putExtra(Intent.EXTRA_CC,"mailcc@gmail.com");
        intent.setType("text/html");
        intent.setPackage("com.google.android.gm");
        startActivity(Intent.createChooser(intent, "Send mail"));
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        boolean found = true;
        try {
            packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            found = false;
        }
        return found;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mailLinear:
                sendMail();
                break;
            case R.id.fbLinear:
                openFacebook();
                break;
            case R.id.instaLinear:
                openInstagram();
                break;
            case R.id.callLinear:
                call();
                break;
            case R.id.twitterLinear:
                openTwitter();
                break;
            case R.id.webLinear:
                openWebsite();
                break;
        }

    }
}
