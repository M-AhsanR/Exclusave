package com.exclusave.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.android.material.tabs.TabLayout;
import com.exclusave.Adapters.Details_SectionpagerAdapter;
import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.Fragments.Details_Details_Fragment;
import com.exclusave.Fragments.Details_Offer_Fragment;
import com.exclusave.models.OfferDetailImagesData;
import com.exclusave.BuildConfig;
import com.exclusave.R;
import com.soundcloud.android.crop.Crop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Details_Activity extends AppCompatActivity {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    Details_SectionpagerAdapter adapter;

    private Details_SectionpagerAdapter sectionsPageAdapter;
    private ViewPager viewPager;
    TabLayout tabLayout;

    TextView headerTitle;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static ArrayList<OfferDetailImagesData> offerDetailImagesData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_);

        sharedPreferences = BaseClass.sharedPreferances(Details_Activity.this);
        editor = BaseClass.sharedPreferancesEditor(Details_Activity.this);
        initilizeviews();
        Bundle bundle = getIntent().getExtras();
        String cattitle = bundle.getString("Title");
        headerTitle.setText(cattitle);
        sectionsPageAdapter = new Details_SectionpagerAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.contain);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor(sharedPreferences.getString("FontColor", "")));
        tabLayout.setTabTextColors(this.getResources().getColor(R.color.white), getResources().getColor(R.color.white));

        tabLayout.setupWithViewPager(viewPager);

//        getOfferDetails();

    }

    private void initilizeviews() {
        headerTitle = findViewById(R.id.headertitle);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Details_SectionpagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new Details_Offer_Fragment(), this.getResources().getString(R.string.offer));
        adapter.addFragment(new Details_Details_Fragment(), this.getResources().getString(R.string.details));

        viewPager.setAdapter(adapter);
        int limit = (adapter.getCount() > 1 ? adapter.getCount() - 1 : 1);
//        viewPager.setOffscreenPageLimit(limit);         /* limit is a fixed integer*/
    }

    private void getOfferDetails() {

        String language = BaseClass.checkLanguageFunction(Details_Activity.this);
        BaseClass.showCustomLoader(Details_Activity.this);

        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.offerDetail + sharedPreferences.getString("UserID", "") + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&PromotionID=1"+" &Language="+language, Details_Activity.this, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                if (ERROR.isEmpty()) {
                    Log.e("OfferDetailsResp", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            BaseClass.dialog.dismiss();

                            JSONObject offer = jsonObject.getJSONObject("offer");

                            String Address = offer.getString("Address");
                            String CategoryID = offer.getString("CategoryID");
                            String CategoryTitle = offer.getString("CategoryTitle");
                            String CityID = offer.getString("CityID");
                            String CityTitle = offer.getString("CityTitle");
                            String CoverImage = offer.getString("CoverImage");
                            String Distance = offer.getString("Distance");
                            String DistrictTitle = offer.getString("DistrictTitle");
                            String DistrictID = offer.getString("DistrictID");
                            String Latitude = offer.getString("Latitude");
                            String Longitude = offer.getString("Longitude");
                            String Mobile = offer.getString("Mobile");
                            String StoreID = offer.getString("StoreID");
                            String StoreTextID = offer.getString("StoreTextID");
                            String SystemLanguageID = offer.getString("SystemLanguageID");
                            String Title = offer.getString("Title");
                            String Email = offer.getString("Email");
                            String FacebookLink = offer.getString("FacebookLink");
                            String InstagramLink = offer.getString("InstagramLink");
                            String TwitterLink = offer.getString("TwitterLink");
                            String WebsiteLink = offer.getString("WebsiteLink");

                            offerDetailImagesData = new ArrayList<>();
                            JSONArray jsonArray = offer.getJSONArray("PromotionBannerImages");
                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String ImageName = jsonObject1.getString("ImageName");

                                offerDetailImagesData.add(new OfferDetailImagesData(ImageName));
                            }

                        } else {
                            BaseClass.dialog.dismiss();
                            Toast.makeText(Details_Activity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(Details_Activity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }*/

        if (resultCode == RESULT_OK && requestCode == 100) {
            Details_Offer_Fragment.returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            Log.e("getdata","---->"+ Details_Offer_Fragment.returnValue.size());
            Log.e("getdata","---->"+ resultCode);
            Log.e("getdata","---->"+ requestCode);
            Log.e("getdata","---->"+ Crop.REQUEST_CROP);
            Log.e("getdata","---->"+ RESULT_OK);
            for (int a = 0; a < Details_Offer_Fragment.returnValue.size(); a++) {
                File imgFile = new File(Details_Offer_Fragment.returnValue.get(a));
//                Uri myUri = Uri.parse(returnValue.get(a));
                Details_Offer_Fragment.myUri = Uri.fromFile(new File(Details_Offer_Fragment.returnValue.get(a)));
                Log.e("getdata","---->"+ Details_Offer_Fragment.myUri);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    Log.e("getdata","---->"+ myBitmap);
//                    ProfilePic.setImageBitmap(myBitmap);
                }
                beginCrop(Details_Offer_Fragment.myUri);

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
                    Pix.start(Details_Activity.this, Details_Offer_Fragment.options);
                } else {
                    Toast.makeText(Details_Activity.this, "Approve permissions to open Camera", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(Details_Activity.this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            if (Details_Offer_Fragment.reqCode == 2222){
                Details_Offer_Fragment.ProfilePic.setImageBitmap(null);
                Details_Offer_Fragment.ProfilePic.setImageURI(Crop.getOutput(result));
            }
            if (Details_Offer_Fragment.reqCode == 1111) {
                Details_Offer_Fragment.completeprofilePic.setImageBitmap(null);
                Details_Offer_Fragment.completeprofilePic.setImageURI(Crop.getOutput(result));
            }
            if (Details_Offer_Fragment.reqCode == 4444) {
                Details_Offer_Fragment.uploadID.setText("UPLOADED");
            }
            if (Details_Offer_Fragment.reqCode == 3333){
                Details_Offer_Fragment.uploadid.setText("UPLOADED");
            }
            try {
                Details_Offer_Fragment.bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Crop.getOutput(result));
                Details_Offer_Fragment.bitmapArrayList.add(Details_Offer_Fragment.bitmap);
                Log.e("chekingBit", Details_Offer_Fragment.bitmap.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(Details_Activity.this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
