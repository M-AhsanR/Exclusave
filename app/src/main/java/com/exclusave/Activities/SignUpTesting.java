package com.exclusave.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bikomobile.circleindicatorpager.CircleIndicatorPager;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.exclusave.Adapters.SignUpTesting_SectionsPageAdapter;
import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.Fragments.FragmentSignUp_Page1;
import com.exclusave.Fragments.FragmentSignUp_Page2;
import com.exclusave.models.CardsData;
import com.exclusave.models.CitiesData;
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

import static com.fxn.pix.Pix.start;

public class SignUpTesting extends AppCompatActivity {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private static final String  TAG = "MainListActivity";
    SignUpTesting_SectionsPageAdapter adapter;
    BottomNavigationView bottomNavigationView;
    ImageView header_comment,header_cart,header_more;
    CircleIndicatorPager indicatorPager;
    int id;

    private SignUpTesting_SectionsPageAdapter sectionsPageAdapter;
    public static ViewPager viewPager;
    TabLayout tabLayout;
    ViewPager cardTypePager;
    public static LinearLayout submit;
    public static TextView btn_submit;
    public static ArrayList<CitiesData> citiesData = new ArrayList<>();
    public static ArrayList<String> citiesNames = new ArrayList<>();
    public static ArrayList<CardsData> cardsData = new ArrayList<>();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor mEditor;

    Bitmap bitmap;
    int codeRequest= 1234;
    ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
    ArrayList<String> returnValue = new ArrayList<>();
    Uri myUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_testing);

        sharedPreferences = BaseClass.sharedPreferances(SignUpTesting.this);
        mEditor = BaseClass.sharedPreferancesEditor(SignUpTesting.this);

//        getCards();
//        getCities();

        sectionsPageAdapter = new SignUpTesting_SectionsPageAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.contain);
        submit = findViewById(R.id.submit);
        btn_submit = findViewById(R.id.btn_submit);
        indicatorPager = findViewById(R.id.indicator);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setVisibility(View.GONE);
        NextButtonClickListner();
    }

    private void setupViewPager(ViewPager viewPager){
        adapter = new SignUpTesting_SectionsPageAdapter(getSupportFragmentManager());

        adapter.addFragment(new FragmentSignUp_Page1(),"");
        adapter.addFragment(new FragmentSignUp_Page2(),"");
        viewPager.setAdapter(adapter);
        indicatorPager.setViewPager(viewPager);
        viewPager.setOffscreenPageLimit(1);         /* limit is a fixed integer*/

    }

    private void NextButtonClickListner(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });
    }

    private void getCards(){

        String language = BaseClass.checkLanguageFunction(SignUpTesting.this);
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        Log.e("header", headers.toString());

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.GETCARDS + "?AndroidAppVersion=" + BuildConfig.VERSION_CODE+ "&Language="+language, SignUpTesting.this, body, headers, new ServerCallback() {
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

//                            SignUp_ViewPager_Adapter adapter = new SignUp_ViewPager_Adapter(SignUpTesting.this, cardsData);
//                            reg_viewPager.setAdapter(adapter);
//                            reg_viewPager.setPageMargin(40);
//
//                            indicatorPager.setViewPager(reg_viewPager);

                        }else {
                            Toast.makeText(SignUpTesting.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(SignUpTesting.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("resultCode", requestCode + "   " + requestCode);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            Log.e("getdata","---->"+ returnValue.size());
            Log.e("getdata","---->"+ resultCode);
            Log.e("getdata","---->"+ requestCode);
            Log.e("getdata","---->"+ Crop.REQUEST_CROP);
            Log.e("getdata","---->"+ Activity.RESULT_OK);
            for (int a = 0; a < returnValue.size(); a++) {
                File imgFile = new File(returnValue.get(a));
//                Uri myUri = Uri.parse(returnValue.get(a));
                myUri = Uri.fromFile(new File(returnValue.get(a)));
                Log.e("getdata","---->"+ myUri);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    Log.e("getdata","---->"+ myBitmap);
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
                    start(SignUpTesting.this, 100);
                } else {
                    Toast.makeText(SignUpTesting.this, "Approve permissions to open Camera", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(SignUpTesting.this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {

            //  completeprofilePic.setImageBitmap(null);
            // completeprofilePic.setImageURI(Crop.getOutput(result));
            FragmentSignUp_Page2 fragmentSignUpPage2 = new FragmentSignUp_Page2();

            Log.e("Image", " " +Crop.getOutput(result));
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Crop.getOutput(result));
                bitmapArrayList.add(bitmap);
                fragmentSignUpPage2.setTextViewText(this.getResources().getString(R.string.uploaded), bitmap);
                Log.e("chekingBit", bitmap.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(SignUpTesting.this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
