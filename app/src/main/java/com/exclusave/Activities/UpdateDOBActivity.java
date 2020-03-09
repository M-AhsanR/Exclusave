package com.exclusave.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Build;
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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateDOBActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView dob, heading;
    Button update;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_dob);

        sharedPreferences = BaseClass.sharedPreferances(UpdateDOBActivity.this);
        editor = BaseClass.sharedPreferancesEditor(UpdateDOBActivity.this);

        initializeViews();
        bundle = getIntent().getExtras();
        bundle.getString("UpdateType");

        if (bundle.getString("UpdateType").equals("Personal")){
            heading.setText(this.getResources().getString(R.string.updateYourDOB));
            dob.setText(sharedPreferences.getString("DateOfBirth", ""));
        }else if(bundle.getString("UpdateType").equals("Family")){
            heading.setText(this.getResources().getString(R.string.updateFamilyDOB));
            dob.setText(sharedPreferences.getString("FamilyMemberDob", ""));
        }

    }

    private void initializeViews() {
        heading = findViewById(R.id.heading);
        dob = findViewById(R.id.dob);
        update = findViewById(R.id.update);

        dob.setOnClickListener(this);
        update.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dob:
                datePicker();
                break;
            case R.id.update:
                if(dob.getText().toString().isEmpty()){
                    Toast.makeText(UpdateDOBActivity.this, getResources().getString(R.string.fillEmptyField), Toast.LENGTH_SHORT).show();
                    break;
                }else {
                    updateProfile();
                    break;
                }

        }
    }

    private void datePicker() {
        //  String currentDate = DateFormat.getDateInstance().format(new Date().getDay());

        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                UpdateDOBActivity.this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
       // dpd.setMaxDate(now);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dpd.setYearRange(1900,now.getWeekYear()-18);
        }
        dpd.show(getSupportFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
        dob.setText(date);
    }

    private void updateProfile(){
        String language = BaseClass.checkLanguageFunction(UpdateDOBActivity.this);

        Map<String, String> body = new HashMap<String, String>();

        BaseClass.showCustomLoader(UpdateDOBActivity.this);

        if(bundle.getString("UpdateType").equals("Personal")){
            body.put("DateOfBirth", dob.getText().toString());
        }else if(bundle.getString("UpdateType").equals("Family")){
            body.put("FamilyMemberDob", dob.getText().toString());
        }

        body.put("UserID", sharedPreferences.getString("UserID", " "));
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
        body.put("Language",language);

        Log.e("body", body.toString());

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.UPDATE_PROFILE, UpdateDOBActivity.this, body, headers, new ServerCallback() {
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

                            Toast.makeText(UpdateDOBActivity.this,message, Toast.LENGTH_SHORT).show();
                            BaseClass.dialog.dismiss();

                        } else {
                            BaseClass.dialog.dismiss();
                            Toast.makeText(UpdateDOBActivity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(UpdateDOBActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
