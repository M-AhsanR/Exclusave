package com.exclusave.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.BuildConfig;
import com.exclusave.R;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;
import com.stfalcon.frescoimageviewer.ImageViewer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity implements View.OnClickListener {

    CircleImageView personalinfopic, familypic;
    ImageView personalinfopicButton, familypicButton;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor mEditor;
    public static final String MyPREFERENCES = "MyPrefs";
    LinearLayout name_click,email_click,dob_click,phone_click,company_click,interest_click,familyname_click,familyphone_click,familydob_click,familygender_click,showid_click;
    TextView name, email, dob, phone, company, interest, familyName, familyPhone, familyDOB, familyGender, showID;

    ArrayList<String> returnValue = new ArrayList<>();
    Uri myUri;
    Bitmap bitmap;
    ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
    Options options;
    int reqCode = 0;
    ArrayList<String> imageViewerArray = new ArrayList<>();

    Map<String, String> familybody = new HashMap<>();
    Map<String, String> body = new HashMap<>();
    Map<String, String> idImage = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        sharedPreferences = BaseClass.sharedPreferances(MyProfileActivity.this);
        mEditor = BaseClass.sharedPreferancesEditor(MyProfileActivity.this);

        intializeViews();
        setValues();
    }

    private void setValues() {

        name.setText(sharedPreferences.getString("FullName" , ""));
        email.setText(sharedPreferences.getString("Email", ""));
        dob.setText(sharedPreferences.getString("DateOfBirth", ""));
        phone.setText(sharedPreferences.getString("Mobile", ""));
        company.setText(sharedPreferences.getString("CompanyName", ""));
        interest.setText((sharedPreferences.getString("InterestTitle", "")));
        if(!sharedPreferences.getString("FamilyMemberFirstName", "").equals("")){
            familyName.setText(sharedPreferences.getString("FamilyMemberFirstName", "") + " " + sharedPreferences.getString("FamilyMemberMiddleName", "") + " " + sharedPreferences.getString("FamilyMemberLastName", ""));
        }
        familyGender.setText(sharedPreferences.getString("FamilyMemberGender", ""));
        familyDOB.setText(sharedPreferences.getString("FamilyMemberDob", ""));
        familyPhone.setText(sharedPreferences.getString("FamilyMemberMobile", ""));
        Picasso.get().load(Constants.URL.IMG_URL + sharedPreferences.getString("Image", "")).into(personalinfopic);
        Picasso.get().load(Constants.URL.IMG_URL + sharedPreferences.getString("FamilyMemberImage", "")).into(familypic);
    }

    private void intializeViews() {
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        dob = findViewById(R.id.dob);
        phone = findViewById(R.id.phone);
        company = findViewById(R.id.company);
        interest = findViewById(R.id.interest);
        personalinfopic = findViewById(R.id.personalinfopic);
        familypic = findViewById(R.id.familypic);
        personalinfopicButton = findViewById(R.id.personalinfopicbtn);
        familypicButton = findViewById(R.id.familypicbtn);
        familyName = findViewById(R.id.familyName);
        familyDOB = findViewById(R.id.familyDOB);
        familyGender = findViewById(R.id.familyGender);
        familyPhone = findViewById(R.id.familyPhone);
        showID = findViewById(R.id.showID);

        name_click = findViewById(R.id.name_linear);
        email_click = findViewById(R.id.email_linear);
        dob_click = findViewById(R.id.dob_linear);
        phone_click = findViewById(R.id.phone_linear);
        company_click = findViewById(R.id.company_linear);
        interest_click = findViewById(R.id.interest_linear);
        familyname_click = findViewById(R.id.familyName_linear);
        familydob_click = findViewById(R.id.familyDOB_linear);
        familygender_click = findViewById(R.id.familyGender_linear);
        familyphone_click = findViewById(R.id.familyPhone_linear);
        showid_click = findViewById(R.id.showID_linear);

        personalinfopicButton.setOnClickListener(this);
        familypicButton.setOnClickListener(this);
        name_click.setOnClickListener(this);
        dob_click.setOnClickListener(this);
        phone_click.setOnClickListener(this);
        interest_click.setOnClickListener(this);
        company_click.setOnClickListener(this);
        familyname_click.setOnClickListener(this);
        familyphone_click.setOnClickListener(this);
        familydob_click.setOnClickListener(this);
        familygender_click.setOnClickListener(this);
        showid_click.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.name_linear
                    :
                Intent intent = new Intent(MyProfileActivity.this, UpdateNameActivity.class);
                intent.putExtra("UpdateType", "Personal");
                startActivity(intent);
                break;
            case R.id.dob_linear:
                Intent intent2 = new Intent(MyProfileActivity.this, UpdateDOBActivity.class);
                intent2.putExtra("UpdateType", "Personal");
                startActivity(intent2);
                break;
            case R.id.phone_linear:
                Intent intent3 = new Intent(MyProfileActivity.this, UpdatePhoneActivity.class);
                intent3.putExtra("UpdateType", "Personal");
                startActivity(intent3);
                break;
            case R.id.interest_linear:
                Intent intent4 = new Intent(MyProfileActivity.this, UpdateInterestActivity.class);
                intent4.putExtra("UpdateType", "Personal");
                startActivity(intent4);
                break;
            case R.id.company_linear:
                Intent intent5 = new Intent(MyProfileActivity.this, UpdateCompanyActivity.class);
                intent5.putExtra("UpdateType", "Personal");
                startActivity(intent5);
                break;
            case R.id.personalinfopicbtn:
                reqCode = 1111;
                options = Options.init()
                        .setRequestCode(100)
                        .setCount(1);
                Log.e("cameraClick", "yes");
                returnValue.clear();
                Pix.start(MyProfileActivity.this,                    //Activity or Fragment Instance
                        options);
                break;
            case R.id.familypicbtn:
                reqCode = 2222;
                options = Options.init()
                        .setRequestCode(100)
                        .setCount(1);
                Log.e("cameraClick", "yes");
                returnValue.clear();
                Pix.start(MyProfileActivity.this,                    //Activity or Fragment Instance
                        options);
                break;
            case R.id.familyName_linear:
                Intent intent6 = new Intent(MyProfileActivity.this, UpdateNameActivity.class);
                intent6.putExtra("UpdateType", "Family");
                startActivity(intent6);
                break;
            case R.id.familyDOB_linear:
                Intent intent7 = new Intent(MyProfileActivity.this, UpdateDOBActivity.class);
                intent7.putExtra("UpdateType", "Family");
                startActivity(intent7);
                break;
            case R.id.familyGender_linear:
                Intent intent8 = new Intent(MyProfileActivity.this, UpdateGenderActivity.class);
                intent8.putExtra("UpdateType", "Family");
                startActivity(intent8);
                break;
            case R.id.familyPhone_linear:
                Intent intent9 = new Intent(MyProfileActivity.this, UpdatePhoneActivity.class);
                intent9.putExtra("UpdateType", "Family");
                startActivity(intent9);
                break;
            case R.id.showID_linear:

                final Dialog dialog = new Dialog(MyProfileActivity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
                dialog.setContentView(R.layout.vieworchangeimage_alert_dialog);
                Button ViewProfile = dialog.findViewById(R.id.view_profile);
                Button UploadIDImage = dialog.findViewById(R.id.update_id_image);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                ViewProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageViewerArray.add(Constants.URL.IMG_URL + sharedPreferences.getString("IDImage", ""));
                        Fresco.initialize(MyProfileActivity.this);
                        new ImageViewer.Builder(MyProfileActivity.this, imageViewerArray)
//                        .setStartPosition(imageViewerArray.get(0))
                                .show();
                        dialog.dismiss();
                    }
                });

                UploadIDImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reqCode = 3333;
                        options = Options.init()
                                .setRequestCode(100)
                                .setCount(1);
                        Log.e("cameraClick", "yes");
                        returnValue.clear();
                        Pix.start(MyProfileActivity.this,                    //Activity or Fragment Instance
                                options);
                        dialog.dismiss();
                    }
                });
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setValues();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            Log.e("getdata", "---->" + returnValue.size());
            Log.e("getdata", "---->" + resultCode);
            Log.e("getdata", "---->" + requestCode);
            Log.e("getdata", "---->" + Crop.REQUEST_CROP);
            Log.e("getdata", "---->" + Activity.RESULT_OK);
            for (int a = 0; a < returnValue.size(); a++) {
                File imgFile = new File(returnValue.get(a));
//                Uri myUri = Uri.parse(returnValue.get(a));
                myUri = Uri.fromFile(new File(returnValue.get(a)));
                Log.e("getdata", "---->" + myUri);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    Log.e("getdata", "---->" + myBitmap);
//                    ProfilePic.setImageBitmap(myBitmap);
                }
                beginCrop(myUri);

            }
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(MyProfileActivity.this, options);
                } else {
                    Toast.makeText(MyProfileActivity.this, "Approve permissions to open Camera", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Crop.getOutput(result));
                bitmapArrayList.add(bitmap);
                Log.e("chekingBit", bitmap.toString());
                Log.e("chekingBit", bitmapArrayList.size() + "");
                if (reqCode == 2222) {
                    updateFamilyInfoPic(result);
                }
                if (reqCode == 1111) {
                    updatePersonalInfoPic(result);
                }
                if (reqCode == 3333){
                    updateIdImagePic(result);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updatePersonalInfoPic(Intent res) {

        String language = BaseClass.checkLanguageFunction(MyProfileActivity.this);
        BaseClass.showCustomLoader(MyProfileActivity.this);

        imgToStringFunction(bitmap);
        body.put("UserID", sharedPreferences.getString("UserID", " "));
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
        body.put("Language",language);

        Log.e("body", body.toString());

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.UPDATE_PROFILE, MyProfileActivity.this, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {

                if (ERROR.isEmpty()) {
                    Log.e("UpdateNameResp", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        String message = jsonObject.getString("message");
                        int status = jsonObject.getInt("status");
                        if (status == 200) {
                            personalinfopic.setImageBitmap(null);
                            personalinfopic.setImageURI(Crop.getOutput(res));

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
                            mEditor.putString("CityID", Cityid);
                            mEditor.putString("Gender", Gender);
                            mEditor.putString("ProfileCompletedPercentage", ProfileCompletedPercentage);
                            mEditor.putString("InterestTitle", InterestTitle);
                            mEditor.putString("CardID", CardID);
                            mEditor.putString("CouponID", CouponID);
                            mEditor.putString("CardImage", CardImage);
                            mEditor.putString("CardBackgroundImage", CardBackgroundImage);
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
                            mEditor.commit();

                            Toast.makeText(MyProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                            BaseClass.dialog.dismiss();

                        } else {
                            BaseClass.dialog.dismiss();
                            Toast.makeText(MyProfileActivity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(MyProfileActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateIdImagePic(Intent res) {

        String language = BaseClass.checkLanguageFunction(MyProfileActivity.this);

        BaseClass.showCustomLoader(MyProfileActivity.this);

        imgToStringFunction(bitmap);
        idImage.put("UserID", sharedPreferences.getString("UserID", " "));
        idImage.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
        idImage.put("Language",language);

        Log.e("body", idImage.toString());

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.UPDATE_PROFILE, MyProfileActivity.this, idImage, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {

                if (ERROR.isEmpty()) {
                    Log.e("UpdateNameResp", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        String message = jsonObject.getString("message");
                        int status = jsonObject.getInt("status");
                        if (status == 200) {
                            personalinfopic.setImageBitmap(null);
                            personalinfopic.setImageURI(Crop.getOutput(res));

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

                            imageViewerArray.clear();

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
                            mEditor.putString("CityID", Cityid);
                            mEditor.putString("Gender", Gender);
                            mEditor.putString("ProfileCompletedPercentage", ProfileCompletedPercentage);
                            mEditor.putString("InterestTitle", InterestTitle);
                            mEditor.putString("CardID", CardID);
                            mEditor.putString("CouponID", CouponID);
                            mEditor.putString("CardImage", CardImage);
                            mEditor.putString("CardBackgroundImage", CardBackgroundImage);
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
                            mEditor.commit();

                            Toast.makeText(MyProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                            BaseClass.dialog.dismiss();

                        } else {
                            BaseClass.dialog.dismiss();
                            Toast.makeText(MyProfileActivity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(MyProfileActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void imgToStringFunction(Bitmap bitmap) {
        Log.e("sizee", bitmapArrayList.size() + "");
        if (bitmapArrayList.size() > 0) {
            Log.e("sizee", bitmapArrayList.size() + "");
            for (int j = 0; j < bitmapArrayList.size(); j++) {
                bitmap = bitmapArrayList.get(j);
                Log.e("bitmap", bitmap + " ");
                ByteArrayOutputStream bos = new ByteArrayOutputStream(); // Compressed
                ByteArrayOutputStream original_image = new ByteArrayOutputStream(); // Original
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos); // Compressed
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, original_image); // Original
                byte[] data = bos.toByteArray(); // Compressed
                byte[] original_data = original_image.toByteArray(); // Original
                // images[0] = Base64.encodeToString(data, Base64.DEFAULT);
                // images[1] = Base64.encodeToString(original_data, Base64.DEFAULT);
                if (reqCode == 1111) {
                    body.put("Image", Base64.encodeToString(data, Base64.DEFAULT));
                } else if (reqCode == 2222) {
                    familybody.put("FamilyMemberImage", Base64.encodeToString(data, Base64.DEFAULT));
                }else if (reqCode == 3333) {
                    idImage.put("IDImage", Base64.encodeToString(data, Base64.DEFAULT));
                }

            }
        }
    }

    private void updateFamilyInfoPic(Intent res) {

        String language = BaseClass.checkLanguageFunction(MyProfileActivity.this);
        BaseClass.showCustomLoader(MyProfileActivity.this);

        imgToStringFunction(bitmap);
        familybody.put("UserID", sharedPreferences.getString("UserID", " "));
        familybody.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
        familybody.put("Language",language);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.UPDATE_PROFILE, MyProfileActivity.this, familybody, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {

                if (ERROR.isEmpty()) {
                    Log.e("UpdateNameResp", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        String message = jsonObject.getString("message");
                        int status = jsonObject.getInt("status");
                        if (status == 200) {
                            familypic.setImageBitmap(null);
                            familypic.setImageURI(Crop.getOutput(res));


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
                            mEditor.putString("CityID", Cityid);
                            mEditor.putString("Gender", Gender);
                            mEditor.putString("ProfileCompletedPercentage", ProfileCompletedPercentage);
                            mEditor.putString("InterestTitle", InterestTitle);
                            mEditor.putString("CardID", CardID);
                            mEditor.putString("CouponID", CouponID);
                            mEditor.putString("CardImage", CardImage);
                            mEditor.putString("CardBackgroundImage", CardBackgroundImage);
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
                            mEditor.commit();

                            Toast.makeText(MyProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                            BaseClass.dialog.dismiss();

                        } else {
                            BaseClass.dialog.dismiss();
                            Toast.makeText(MyProfileActivity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(MyProfileActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
