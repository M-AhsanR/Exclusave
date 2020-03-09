package com.exclusave.Fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.bikomobile.circleindicatorpager.CircleIndicatorPager;
import com.exclusave.Activities.ReviewsActivity;
import com.exclusave.Adapters.DetailImage_Adapter;
import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.models.OfferDetailImagesData;
import com.exclusave.models.SelectPro_Student_Model;
import com.exclusave.models.StoreDetails_Data;
import com.exclusave.BuildConfig;
import com.exclusave.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Details_Details_Fragment extends Fragment implements View.OnClickListener {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    CircleIndicatorPager indicatorPager;
    ArrayList<SelectPro_Student_Model> list = new ArrayList<>();
    ViewPager viewPager;
    TextView textdescription, reviews;
    LinearLayout facebook, twitter, instagram, website;

    String fb, insta, twiter, web;
    String StoreID;

    ArrayList<OfferDetailImagesData> offerDetailImagesData;
    ArrayList<StoreDetails_Data> storeOffersData = new ArrayList<>();
    ArrayList<String> imageViewerArray = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details_details, container, false);
        initilizeView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = BaseClass.sharedPreferances(getActivity());
        editor = BaseClass.sharedPreferancesEditor(getActivity());

        getOfferDetails();
    }

    private void initilizeView(View view) {
        viewPager = view.findViewById(R.id.pager);
        indicatorPager = view.findViewById(R.id.indicator);
        textdescription = view.findViewById(R.id.description);
        facebook = view.findViewById(R.id.fb);
        twitter = view.findViewById(R.id.twitter);
        instagram = view.findViewById(R.id.insta);
        website = view.findViewById(R.id.web);
        reviews = view.findViewById(R.id.reviews);

        facebook.setOnClickListener(this);
        twitter.setOnClickListener(this);
        instagram.setOnClickListener(this);
        website.setOnClickListener(this);
        reviews.setOnClickListener(this);

    }

    private void getOfferDetails() {
        String language = BaseClass.checkLanguageFunction(getActivity());
        BaseClass.showCustomLoader(getActivity());

        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.STORE + sharedPreferences.getString("UserID", "") + "&ParentID=0" + "&StoreID=" + sharedPreferences.getString("StoreID", "") + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&OrderBy=DESC" + "&Language=" + language, getActivity(), body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                if (ERROR.isEmpty()) {
                    Log.e("StoreDetailsResp", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            BaseClass.dialog.dismiss();

                            JSONObject object = jsonObject.getJSONObject("stores");

                            String Address = object.getString("Address");
                            String CategoryBackgroundImage = object.getString("CategoryBackgroundImage");
                            String CategoryID = object.getString("CategoryID");
                            String CategoryImage = object.getString("CategoryImage");
                            String CategoryTitle = object.getString("CategoryTitle");
                            String CityID = object.getString("CityID");
                            String CityTitle = object.getString("CityTitle");
                            String CoverImage = object.getString("CoverImage");
                            String CreatedAt = object.getString("CreatedAt");
                            String CreatedBy = object.getString("CreatedBy");
                            String Description = object.getString("Description");
                            String DistrictID = object.getString("DistrictID");
                            String DistrictTitle = object.getString("DistrictTitle");
                            String Email = object.getString("Email");
                            String FacebookLink = object.getString("FacebookLink");
                            String FirstName = object.getString("FirstName");
                            String Hide = object.getString("Hide");
                            String Image = object.getString("Image");
                            String InstagramLink = object.getString("InstagramLink");
                            String IsActive = object.getString("IsActive");
                            String IsAddedToFavourite = object.getString("IsAddedToFavourite");
                            String LastName = object.getString("LastName");
                            String Latitude = object.getString("Latitude");
                            String Longitude = object.getString("Longitude");
                            String MiddleName = object.getString("MiddleName");
                            String Mobile = object.getString("Mobile");
                            String ParentID = object.getString("ParentID");
                            String SortOrder = object.getString("SortOrder");
                            StoreID = object.getString("StoreID");
                            String StoreTextID = object.getString("StoreTextID");
                            String SystemLanguageID = object.getString("SystemLanguageID");
                            String Title = object.getString("Title");
                            String TwitterLink = object.getString("TwitterLink");
                            String UpdatedAt = object.getString("UpdatedAt");
                            String UpdatedBy = object.getString("UpdatedBy");
                            String WebsiteLink = object.getString("WebsiteLink");
                            String TotalReview = object.getString("TotalReview");

                            fb = FacebookLink;
                            insta = InstagramLink;
                            twiter = TwitterLink;
                            web = WebsiteLink;
                            reviews.setText(getActivity().getResources().getString(R.string.reviews) + " (" + TotalReview + ") ");

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                textdescription.setText(Html.fromHtml(Description, Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                textdescription.setText(Html.fromHtml(Description));
                            }

                            storeOffersData.add(new StoreDetails_Data(Address, CategoryBackgroundImage, CategoryID, CategoryImage, CategoryTitle, CityID, CityTitle, CoverImage, CreatedAt, CreatedBy, DistrictID,
                                    DistrictTitle, Email, FacebookLink, FirstName, Hide, Image, InstagramLink, IsActive, IsAddedToFavourite, LastName, Latitude, Longitude,
                                    MiddleName, Mobile, ParentID, SortOrder, StoreID, StoreTextID, SystemLanguageID, Title, TwitterLink, UpdatedAt, UpdatedBy, WebsiteLink, Description));

                            DetailImage_Adapter adapter = new DetailImage_Adapter(getActivity(), storeOffersData);
                            viewPager.setAdapter(adapter);
                            viewPager.setPageMargin(40);
                            if (storeOffersData.size() < 2) {
                                indicatorPager.setVisibility(View.GONE);
                            } else {
                                indicatorPager.setViewPager(viewPager);
                            }

                        } else {
                            BaseClass.dialog.dismiss();
                            Toast.makeText(getActivity(), String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(getActivity(), ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fb:
                openFacebook();
                break;
            case R.id.twitter:
                openTwitter();
                break;
            case R.id.insta:
                openInstagram();
                break;
            case R.id.web:
                openWebsite();
                break;
            case R.id.reviews:
                Intent i = new Intent(getActivity(), ReviewsActivity.class);
                i.putExtra("StoreID", StoreID);
                startActivity(i);
                break;
        }
    }

    private void openFacebook() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + fb));
            startActivity(intent);
        } catch (Exception e) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + fb));
            startActivity(intent);
        }
    }

    private void openWebsite() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://www." + web));
        startActivity(i);
    }

    private void openInstagram() {
        if ((isPackageInstalled("com.instagram.android", getActivity().getPackageManager()))) {
            String url = "https://instagram.com/" + insta;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseInstallInstagram), Toast.LENGTH_SHORT).show();
        }
    }


    private void openTwitter() {
        if ((isPackageInstalled("com.twitter.android", getActivity().getPackageManager()))) {
            String url = "http://twitter.com/" + twiter;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseInstallTwitter), Toast.LENGTH_SHORT).show();
        }
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

}
