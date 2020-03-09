package com.exclusave.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.android.volley.Request;
import com.bikomobile.circleindicatorpager.CircleIndicatorPager;
import com.exclusave.Activities.MainActivity;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.exclusave.Activities.Book_Now_Activity;
import com.exclusave.Activities.Branches_Activity;
import com.exclusave.Activities.ChatActivity;
import com.exclusave.Activities.WebViewActivity;
import com.exclusave.Adapters.OfferDetailsImagesAdapter;
import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.models.BranchesList_Data;
import com.exclusave.models.CitiesData;
import com.exclusave.models.InterestsData;
import com.exclusave.models.OfferDetailImagesData;
import com.exclusave.models.SelectPro_Student_Model;
import com.exclusave.models.StoreOffersData;
import com.exclusave.models.StoresDataForDropDown;
import com.exclusave.models.StoresPagerData;
import com.exclusave.BuildConfig;
import com.exclusave.R;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;
import static com.exclusave.R.drawable.ic_booknow_icon;
import static com.exclusave.R.drawable.ic_pointer_icon;
import static com.exclusave.R.drawable.ic_redeem_icon;
import static com.exclusave.R.drawable.ic_tick_icon;

public class Details_Offer_Fragment extends Fragment implements View.OnClickListener {

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor mEditor;
    LinearLayout book_now_linearlayout, branches_linear;
    ImageView changeIcon;
    CircleIndicatorPager indicatorPager;
    ArrayList<SelectPro_Student_Model> list = new ArrayList<>();
    ArrayList<StoresPagerData> storesPagerData = new ArrayList<>();
    ViewPager viewPager;
    TextView textViewpercent, details;
    ArrayList<OfferDetailImagesData> offerDetailImagesData;
    ArrayList<StoreOffersData> storeOffersData = new ArrayList<>();
    TextView actionText;
    public static TextView uploadID;
    Dialog raisacomplain;
    String StoreId;
    String BranchStoreId;
    ArrayList<BranchesList_Data> branches_list = new ArrayList<>();
    ArrayList<String> branchesNames = new ArrayList<>();
    ArrayList<String> storeName = new ArrayList<>();
    ArrayList<StoresDataForDropDown> storesPagerDatafordropDown = new ArrayList<>();
    AutoCompleteTextView select_city, complaintitle;

    public static int reqCode = 0;
    EditText dob, company_name, position;
    ArrayList<CitiesData> citiesData = new ArrayList<>();
    ArrayList<String> citiesNames = new ArrayList<>();
    AutoCompleteTextView gender, interest;
    String Gender;
    Map<String, String> body = new HashMap<String, String>();
    ArrayList<InterestsData> interestsData = new ArrayList<>();
    ArrayList<String> interestsNames = new ArrayList<>();
    static String InterestID;
    static Dialog updatedialog;
    public static CircleImageView completeprofilePic;
    public static ArrayList<String> returnValue = new ArrayList<>();
    public static Uri myUri;
    public static Bitmap bitmap;
    public static ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
    public static Options options;
    public static ImageView filter, cam_image, ProfilePic, filterheader;
    public static TextView uploadid;
    LinearLayout storetype_linear;
    Map<String, String> saveTransbody = new HashMap<String, String>();

    EditText beforeBill;
    EditText afterBill;
    TextView saved, discount, discountTpe;
    TextView upto;
    Dialog confirmBillDialog;
    Dialog rateYourExpeirenceDialog;
    Dialog complaintDialog;
    RatingBar ratingBar;
    LikeButton favBtn;
    Dialog merchantCodeDialog;
    LinearLayout promotionsLinear;

    String StoreCoverImage = "", StoreTitle = "", Rating = "1", StoreID = "", TransactionID = "";
    ColorStateList iconsColorStates;
    Drawable drawable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offer_details, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedpreferences = BaseClass.sharedPreferances(getActivity());
        mEditor = BaseClass.sharedPreferancesEditor(getActivity());

        drawable = getResources().getDrawable(R.drawable.ic_heart_new_black);
        drawable.setTint(Color.parseColor(sharedpreferences.getString("FontColor", "")));


        initilizedViews(view);
        getOfferDetails();
        getStoresApiCall();

        favBtn.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                addToFav();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                removeFromFav();
            }
        });
    }

    private void initilizedViews(View view) {
        viewPager = view.findViewById(R.id.pager);
        indicatorPager = view.findViewById(R.id.indicator);
//        textViewpercent = view.findViewById(R.id.percentage);
        branches_linear = view.findViewById(R.id.branches_linear);
        details = view.findViewById(R.id.details);
        book_now_linearlayout = view.findViewById(R.id.book_now_linear);
        actionText = view.findViewById(R.id.actionText);
        changeIcon = view.findViewById(R.id.heart_image);
        storetype_linear = view.findViewById(R.id.storetype_linear);
        discount  = view.findViewById(R.id.discount);
        discountTpe = view.findViewById(R.id.discountType);
        ratingBar = view.findViewById(R.id.ratingBar);
        favBtn = view.findViewById(R.id.favBtn);
        upto = view.findViewById(R.id.upto);
        promotionsLinear = view.findViewById(R.id.promotionsLinear);

        book_now_linearlayout.setOnClickListener(this);
        branches_linear.setOnClickListener(this);
        promotionsLinear.setOnClickListener(this);

        favBtn.setUnlikeDrawable(getResources().getDrawable(R.drawable.black_heart));
        favBtn.setLikeDrawable(drawable);

//        favBtn.setBackgroundTintList(iconsColorStates);
//        favBtn.setForegroundTintList(iconsColorStates);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.book_now_linear:
                if (storeOffersData.get(0).getStoreType().equals("Physical")) {
                    if (!sharedpreferences.getString("ProfileCompletedPercentage", "").equals("100")) {
                        updateMissingDataPopup();
                    } else {
                        merchantCodeDialog();
                    }
                } else if (storeOffersData.get(0).getStoreType().equals("Social Media")) {
                    startActivity(new Intent(getActivity(), ChatActivity.class));
                } else if (storeOffersData.get(0).getStoreType().equals("Ecommerce")) {
                    if (!sharedpreferences.getString("ProfileCompletedPercentage", "").equals("100")) {
                        updateMissingDataPopup();
                    } else {
                        Intent intent = new Intent(getActivity(), WebViewActivity.class);
                        intent.putExtra("WEBURL", "http://exclusave.schopfen.com/index/merchant_promocodes/" + sharedpreferences.getString("UserID", "") + "/" + sharedpreferences.getString("CardID", ""));
                        startActivity(intent);
                    }
                } else if (storeOffersData.get(0).getStoreType().equals("Booking")) {
                    if (!sharedpreferences.getString("ProfileCompletedPercentage", "").equals("100")) {
                        updateMissingDataPopup();
                    } else {
                        mEditor.putString("StoreID", storeOffersData.get(0).getStoreID()).commit();
                        mEditor.putString("PromotionID", storeOffersData.get(0).getPromotionID()).commit();
                        startActivity(new Intent(getActivity(), Book_Now_Activity.class));
                    }
                }
                break;
            case R.id.branches_linear:
//                merchantCodeDialog();
                startActivity(new Intent(getContext(), Branches_Activity.class));
                break;
            case R.id.promotionsLinear:
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.comming_soon), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void getStoresApiCall() {
        String language = BaseClass.checkLanguageFunction(getActivity());
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

        String URL = "";
        if (sharedpreferences.getString("isLoggedIn", "").equals("0")) {
            URL = Constants.URL.BASE_URL + "store?AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&Langujage=" + language;
        } else if (sharedpreferences.getString("isLoggedIn", "").equals("1")) {
            URL = Constants.URL.STORE + sharedpreferences.getString("UserID", "") + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&Language=" + language;
        }

        ApiModelClass.GetApiResponse(Request.Method.GET, URL, getActivity(), body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                if (ERROR.isEmpty()) {
                    Log.e("GetStoresResponse", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            JSONArray storearray = jsonObject.getJSONArray("stores");

                            for (int i = 0; i < storearray.length(); i++) {

                                JSONObject object = storearray.getJSONObject(i);

                                String Address = object.getString("Address");
                                String CategoryID = object.getString("CategoryID");
                                String CategoryTitle = object.getString("CategoryTitle");
                                String CityID = object.getString("CityID");
                                String CityTitle = object.getString("CityTitle");
                                String CoverImage = object.getString("CoverImage");
                                String CreatedAt = object.getString("CreatedAt");
                                String CreatedBy = object.getString("CreatedBy");
                                String Distance = object.getString("Distance");
                                String DistrictID = object.getString("DistrictID");
                                String DistrictTitle = object.getString("DistrictTitle");
                                String Hide = object.getString("Hide");
                                String Email = object.getString("Email");
                                String FirstName = object.getString("FirstName");
                                String Image = object.getString("Image");
                                String IsActive = object.getString("IsActive");
                                String LastName = object.getString("LastName");
                                String Latitude = object.getString("Latitude");
                                String Longitude = object.getString("Longitude");
                                String MiddleName = object.getString("MiddleName");
                                String Mobile = object.getString("Mobile");
                                String ParentID = object.getString("ParentID");
                                String PromotionID = object.getString("PromotionID");
                                String SortOrder = object.getString("SortOrder");
                                String StoreID = object.getString("StoreID");
                                String StoreTextID = object.getString("StoreTextID");
                                String SystemLanguageID = object.getString("SystemLanguageID");
                                String Title = object.getString("Title");
                                String UpdatedAt = object.getString("UpdatedAt");
                                String UpdatedBy = object.getString("UpdatedBy");
                                String CategoryImage = object.getString("CategoryImage");
                                String UserID = object.getString("UserID");
                                Integer IsAddedToFavourite = object.getInt("IsAddedToFavourite");

                                Log.e("IsAddedToFav", IsAddedToFavourite + "");

                                storeName.add(Title);
                                storesPagerDatafordropDown.add(new StoresDataForDropDown(StoreID, Title));
//                                if(IsAddedToFavourite == 0){
//                                    favBtn.setLiked(false);
//                                    Log.e("IsAddedToFavLiked", "nO");
//                                }else if(IsAddedToFavourite == 1){
//                                    Log.e("IsAddedToFavLiked", "Yes");
//                                    favBtn.setLiked(true);
//                                }
                                mEditor.putString("StoreID", StoreID).commit();
                                mEditor.putString("StoreName", Title).commit();
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
                    Toast.makeText(getActivity(), String.valueOf(ERROR), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void selectstoresClickListener() {
        complaintitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("drawableClicked", "Checked");
                hideKeyboard(complaintitle);
                complaintitle.showDropDown();
            }
        });

        complaintitle.setTextIsSelectable(true);
        complaintitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    if (!storesPagerDatafordropDown.get(position).getStoreID().isEmpty()) {
                        StoreId = storesPagerDatafordropDown.get(position).getStoreID();
                        branchesNames.clear();
                        branchesListApiCall();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void selectbranchesClickListener() {
        select_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("drawableClicked", "Checked");
                hideKeyboard(select_city);
                select_city.showDropDown();
            }
        });

        select_city.setTextIsSelectable(true);
        select_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    if (!branches_list.get(position).getStoreID().isEmpty()) {
                        BranchStoreId = branches_list.get(position).getStoreID();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)getActivity().
                getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm.isAcceptingText()){
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        }
    }

    private void branchesListApiCall() {
        String language = BaseClass.checkLanguageFunction(getActivity());
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

        String URL = "";
        if (sharedpreferences.getString("isLoggedIn", "").equals("0")) {
            URL = Constants.URL.BASE_URL + "stores?AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&CardID=1" + "&OrderBy=DESC" + "&Language=" + language;
        } else if (sharedpreferences.getString("isLoggedIn", "").equals("1")) {
            URL = Constants.URL.BRANCHES + sharedpreferences.getString("UserID", "") + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&OrderBy=DESC" + "&ParentID=" + StoreId;
            Log.e("URL", URL);
        }

        ApiModelClass.GetApiResponse(Request.Method.GET, URL, getActivity(), body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                if (ERROR.isEmpty()) {
                    Log.e("GetBranchesResponse", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            JSONArray storearray = jsonObject.getJSONArray("stores");

                            for (int i = 0; i < storearray.length(); i++) {

                                JSONObject object = storearray.getJSONObject(i);

                                String Address = object.getString("Address");
                                String CategoryBackgroundImage = object.getString("CategoryBackgroundImage");
                                String CategoryID = object.getString("CategoryID");
                                String CategoryTitle = object.getString("CategoryTitle");
                                String CityID = object.getString("CityID");
                                String CityTitle = object.getString("CityTitle");
                                String ContactPersonName = object.getString("ContactPersonName");
                                String CoverImage = object.getString("CoverImage");
                                String CreatedAt = object.getString("CreatedAt");
                                String CreatedBy = object.getString("CreatedBy");
                                String Description = object.getString("Description");
                                String Distance = object.getString("Distance");
                                String DistrictID = object.getString("DistrictID");
                                String DistrictTitle = object.getString("DistrictTitle");
                                String Email = object.getString("Email");
                                String FacebookLink = object.getString("FacebookLink");
                                String FirstName = object.getString("FirstName");
                                String GoldImage = object.getString("GoldImage");
                                String Hide = object.getString("Hide");
                                String Image = object.getString("Image");
                                String InstagramLink = object.getString("InstagramLink");
                                String IsActive = object.getString("IsActive");
                                String IsSponsored = object.getString("IsSponsored");
                                String LastName = object.getString("LastName");
                                String Latitude = object.getString("Latitude");
                                String Longitude = object.getString("Longitude");
                                String MiddleName = object.getString("MiddleName");
                                String Mobile = object.getString("Mobile");
                                String OrangeImage = object.getString("OrangeImage");
                                String ParentID = object.getString("ParentID");
                                String SortOrder = object.getString("SortOrder");
                                String SponsoredSortOrder = object.getString("SponsoredSortOrder");
                                String SponsorshipExpiresAt = object.getString("SponsorshipExpiresAt");
                                String StoreCode = object.getString("StoreCode");
                                String StoreID = object.getString("StoreID");
                                String StoreTextID = object.getString("StoreTextID");
                                String StoreType = object.getString("StoreType");
                                String SubCategoryID = object.getString("SubCategoryID");
                                String SystemLanguageID = object.getString("SystemLanguageID");
                                String Title = object.getString("Title");
                                String TwitterLink = object.getString("TwitterLink");
                                String UpdatedAt = object.getString("UpdatedAt");
                                String UpdatedBy = object.getString("UpdatedBy");
                                String UserID = object.getString("UserID");
                                String WebsiteLink = object.getString("WebsiteLink");

                                branchesNames.add(Title);
                                branches_list.add(new BranchesList_Data(Address, CategoryID, CategoryTitle, CityID, CityTitle, CoverImage, CreatedAt, CreatedBy, DistrictID,
                                        DistrictTitle, Email, FacebookLink, FirstName, Hide, Image, InstagramLink, IsActive, LastName, Latitude, Longitude,
                                        MiddleName, Mobile, ParentID, SortOrder, StoreID, StoreTextID, SystemLanguageID, Title, TwitterLink, UpdatedAt, UpdatedBy, WebsiteLink));

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

    private void saveTransaction(String storeID, String billBefore, String billAfter) {

        String language = BaseClass.checkLanguageFunction(getActivity());
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

//        BaseClass.showCustomLoader(getActivity());

        imgToStringFunction(bitmap, 5678);
        saveTransbody.put("UserID", sharedpreferences.getString("UserID", ""));
        saveTransbody.put("StoreID", storeID);
        saveTransbody.put("BillBefore", billBefore);
        saveTransbody.put("BillAfter", billAfter);
        saveTransbody.put("CreatedAt", ts);
        saveTransbody.put("IsFamilyMember", sharedpreferences.getString("IsFamilyMember", ""));
        saveTransbody.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
        saveTransbody.put("Language", language);

        Log.e("saveTrasn", saveTransbody.toString());

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.saveTransaction, getActivity(), saveTransbody, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {

                if (ERROR.isEmpty()) {
                    Log.e("SaveTransactionResp", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            confirmBillDialog.dismiss();
                            rateYourExpeirenceDialog();
                            JSONObject jsonObject1 = jsonObject.getJSONObject("transaction_detail");
                            String BillAfter = jsonObject1.getString("BillAfter");
                            String BillBefore = jsonObject1.getString("BillBefore");
                            String CreatedAt = jsonObject1.getString("CreatedAt");
                            String Discount = jsonObject1.getString("Discount");
                            String Receipt = jsonObject1.getString("Receipt");
                            String StoreID = jsonObject1.getString("StoreID");
                            TransactionID = jsonObject1.getString("TransactionID");
                            String UserID = jsonObject1.getString("UserID");

                        } else {
                            BaseClass.dialog.dismiss();
                            String message = jsonObject.getString("message");
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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

    private void getOfferDetails() {

//        BaseClass.showCustomLoader(getActivity());
        String language = BaseClass.checkLanguageFunction(getActivity());
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.offersForStore + sharedpreferences.getString("UserID", "") + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&StoreID=" + sharedpreferences.getString("StoreID", "") + "&Language=" + language, getActivity(), body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                if (ERROR.isEmpty()) {
                    Log.e("OfferStoreID", sharedpreferences.getString("StoreID", ""));
                    Log.e("OfferDetailsResp", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            BaseClass.dialog.dismiss();

                            JSONArray offersArray = jsonObject.getJSONArray("offers");
                            for (int i = 0; i < offersArray.length(); i++) {
                                JSONObject offerObj = offersArray.getJSONObject(i);

                                String Description = offerObj.getString("Description");
                                String Discount = offerObj.getString("Discount");
                                String DiscountPrefix = offerObj.getString("DiscountPrefix");
                                String DiscountType = offerObj.getString("DiscountType");
                                String EndDate = offerObj.getString("EndDate");
                                String PromotionID = offerObj.getString("PromotionID");
                                String QRCodeType = offerObj.getString("QRCodeType");
                                String StartDate = offerObj.getString("StartDate");
                                String StoreID = offerObj.getString("StoreID");
                                String StoreTitle = offerObj.getString("StoreTitle");
                                String Title = offerObj.getString("Title");
                                String StoreType = offerObj.getString("StoreType");
                                String StoreAverageRating = offerObj.getString("StoreAverageRating");
                                Integer IsAddedToFavourite = offerObj.getInt("IsAddedToFavourite");

                                if(IsAddedToFavourite == 0){
                                    favBtn.setLiked(false);
                                    Log.e("IsAddedToFavLiked", "nO");
                                }else if(IsAddedToFavourite == 1){
                                    Log.e("IsAddedToFavLiked", "Yes");
                                    favBtn.setLiked(true);
                                    favBtn.setCircleStartColorInt(Color.parseColor(sharedpreferences.getString("FontColor", "")));
                                    favBtn.setExplodingDotColorsInt(Color.parseColor(sharedpreferences.getString("FontColor", "")), Color.parseColor(sharedpreferences.getString("FontColor", "")));
                                }

                                if (DiscountPrefix.equals("No prefix") || DiscountPrefix.equals("")){
                                    upto.setVisibility(View.GONE);
                                }else {
                                    upto.setText(DiscountPrefix);
                                }

                                if (StoreType.equals("Physical")) {
                                    actionText.setText(getActivity().getResources().getString(R.string.orderNow));
                                    changeIcon.setBackgroundDrawable(getActivity().getResources().getDrawable(ic_tick_icon));
                                } else if (StoreType.equals("Social Media")) {
                                    actionText.setText(getActivity().getResources().getString(R.string.orderNow));
                                    changeIcon.setBackgroundDrawable(getActivity().getResources().getDrawable(ic_pointer_icon));
                                } else if (StoreType.equals("Ecommerce")) {
                                    actionText.setText(getActivity().getResources().getString(R.string.redeemNow));
                                    changeIcon.setBackgroundDrawable(getActivity().getResources().getDrawable(ic_tick_icon));
                                } else if (StoreType.equals("Booking")) {
                                    actionText.setText(getActivity().getResources().getString(R.string.bookNow));
                                    changeIcon.setBackgroundDrawable(getActivity().getResources().getDrawable(ic_booknow_icon));
                                }
                                String percent = Discount + " ";
                                String off = DiscountType + " OFF";

                                discount.setText(Discount);
                                discountTpe.setText(DiscountType);
                                ratingBar.setRating(Float.parseFloat(StoreAverageRating));

                                SpannableString span1 = new SpannableString(percent);
                                span1.setSpan(new AbsoluteSizeSpan(70), 0, percent.length(), SPAN_INCLUSIVE_INCLUSIVE);

                                SpannableString span2 = new SpannableString(off);
                                span2.setSpan(new AbsoluteSizeSpan(30), 0, off.length(), SPAN_INCLUSIVE_INCLUSIVE);

                                CharSequence finalText = TextUtils.concat(span1, " ", span2);
//                                textViewpercent.setText(finalText);

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    details.setText(Html.fromHtml(Description, Html.FROM_HTML_MODE_COMPACT));
                                } else {
                                    details.setText(Html.fromHtml(Description));
                                }
                                // details.setText(Description);

                                offerDetailImagesData = new ArrayList<>();
                                JSONArray jsonArray = offerObj.getJSONArray("PromotionBannerImages");
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                    String ImageName = jsonObject1.getString("ImageName");

                                    offerDetailImagesData.add(new OfferDetailImagesData(ImageName));
                                }

                                storeOffersData.add(new StoreOffersData(Description, Discount, EndDate, PromotionID, QRCodeType, StartDate, StoreID, StoreTitle, Title, StoreType, offerDetailImagesData));
                            }



                            OfferDetailsImagesAdapter offerDetailsImagesAdapter = new OfferDetailsImagesAdapter(getActivity(), offerDetailImagesData);
                            viewPager.setAdapter(offerDetailsImagesAdapter);
                            viewPager.setPageMargin(40);
                            if (offerDetailImagesData.size() < 2) {
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

    private void updateMissingDataPopup() {
        updatedialog = new Dialog(getActivity(), android.R.style.ThemeOverlay_Material_Dialog_Alert);
        updatedialog.setContentView(R.layout.update_missingdata_dialog);
        Button skip = updatedialog.findViewById(R.id.skip_btn);
        Button submit = updatedialog.findViewById(R.id.submitBtn);
        dob = updatedialog.findViewById(R.id.date_of_birth);
        company_name = updatedialog.findViewById(R.id.companyName);
        position = updatedialog.findViewById(R.id.position);
        completeprofilePic = updatedialog.findViewById(R.id.completeprofilePic);
        gender = updatedialog.findViewById(R.id.gender);
        interest = updatedialog.findViewById(R.id.interestType);

        submit.setBackgroundDrawable(new BaseClass.DrawableGradient(new int[]{Color.parseColor(sharedpreferences.getString("PackageColorUpper", "")), Color.parseColor(sharedpreferences.getString("PackageColorLower", "")), Color.parseColor(sharedpreferences.getString("PackageColorLower", ""))}, 8));
        // getCities();

        String GENDER[] = {this.getResources().getString(R.string.male), this.getResources().getString(R.string.female)};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, GENDER);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        gender.setAdapter(adapter);

        cityClickListener();
        getInterests();
        interestClickListener();
        ImageView imageuploadbtn = updatedialog.findViewById(R.id.profilePicBtn);
        updatedialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        updatedialog.show();

        dob.setText(sharedpreferences.getString("DateOfBirth", ""));
        company_name.setText(sharedpreferences.getString("CompanyName", ""));
        position.setText(sharedpreferences.getString("Position", ""));
        if (!sharedpreferences.getString("Gender", "").equals("")) {
            gender.setHint(sharedpreferences.getString("Gender", ""));
            gender.setHintTextColor(getActivity().getResources().getColor(R.color.black));
        }
        if (!sharedpreferences.getString("InterestTitle", "").equals("")) {
            interest.setHint(sharedpreferences.getString("InterestTitle", ""));
            interest.setHintTextColor(getActivity().getResources().getColor(R.color.black));
        }

        InterestID = sharedpreferences.getString("Interest", "");

        if (!sharedpreferences.getString("Image", "").equals("")) {
            Picasso.get().load(Constants.URL.IMG_URL + sharedpreferences.getString("Image", "")).into(completeprofilePic);
        }


        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });
        imageuploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reqCode = 1111;
                options = Options.init()
                        .setRequestCode(100)
                        .setCount(1);
                Log.e("cameraClick", "yes");
                returnValue.clear();
                Pix.start(getActivity(),                    //Activity or Fragment Instance
                        options);
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.putString("Info", "Skipped").commit();
                updatedialog.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeProfilleApiCall();
                // DynamicToast.makeSuccess(MainActivity.this, "Mmm, working on it!", 3).show();
            }
        });
    }

    private void cityClickListener() {
        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("drawableClicked", "Checked");
                BaseClass.hideKeyboard(gender, getActivity());
                gender.showDropDown();
            }
        });

        gender.setTextIsSelectable(true);
        gender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {

//                    mEditor.putString("CityID", CityID).commit();
//                    mEditor.putString("City", citiesDataArrayList.get(position).getTitle()).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void completeProfilleApiCall() {

        String language = BaseClass.checkLanguageFunction(getActivity());
        BaseClass.showCustomLoader(getActivity());

        if (bitmapArrayList.size() > 0) {
            imgToStringFunction(bitmap, 1234);
        }
        body.put("DateOfBirth", dob.getText().toString());
        if (!InterestID.equals("")) {
            body.put("Interest", InterestID);
        }
        if (!company_name.getText().toString().isEmpty()) {
            body.put("CompanyName", company_name.getText().toString());
        }
        if (!position.getText().toString().isEmpty()) {
            body.put("Position", position.getText().toString());
        }
        if (gender.getHint().toString().equals(this.getResources().getString(R.string.male)) || gender.getHint().toString().equals(this.getResources().getString(R.string.female))) {
            body.put("Gender", gender.getHint().toString());
        }
        if (gender.getText().toString().equals(this.getResources().getString(R.string.male)) || gender.getText().toString().equals(this.getResources().getString(R.string.female))) {
            body.put("Gender", gender.getText().toString());
        }
        body.put("UserID", sharedpreferences.getString("UserID", " "));
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
        body.put("Language", language);

        Log.e("body", body.toString());

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.UPDATE_PROFILE, getActivity(), body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {

                if (ERROR.isEmpty()) {
                    Log.e("UpdateProfileResp", result);
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

                            mEditor.putString("FullName", FirstName + " " + MiddleName + " " + LastName);
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

//                            materialProgressBar.setProgress(Integer.parseInt(sharedpreferences.getString("ProfileCompletedPercentage", "")));
//                            Picasso.get().load(Constants.URL.IMG_URL + sharedpreferences.getString("Image", "")).into(ProfilePic);

                            updatedialog.dismiss();

                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            BaseClass.dialog.dismiss();

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

    private void imgToStringFunction(Bitmap bitmap, int req) {

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
                if (req == 1234) {
                    body.put("Image", Base64.encodeToString(data, Base64.DEFAULT));
                } else if (req == 5678) {
                    saveTransbody.put("Receipt", Base64.encodeToString(data, Base64.DEFAULT));
                }

            }
        }
    }

    private void getInterests() {

        String language = BaseClass.checkLanguageFunction(getActivity());
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.CATEGORIES + sharedpreferences.getString("UserID", "") + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&Language=" + language, getActivity(), body, headers, new ServerCallback() {
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
                                interestsData.add(new InterestsData(BackgroundImage, CategoryID, Image, Title, SortOrder, GoldImage, "no"));
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_list_item_1, interestsNames);
                            adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                            interest.setAdapter(adapter);

                        } else {
                            Toast.makeText(getActivity(), String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void interestClickListener() {
        interest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseClass.hideKeyboard(interest, getActivity());
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

    private void datePicker() {
        //  String currentDate = DateFormat.getDateInstance().format(new Date().getDay());

        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                (DatePickerDialog.OnDateSetListener) getActivity(),
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dpd.setYearRange(1900, now.getWeekYear() - 18);
        }
        dpd.show(getActivity().getSupportFragmentManager(), "Datepickerdialog");
    }

    private void calculate() {
        if (beforeBill.getText().toString().isEmpty()) {
            saved.setText("= " + afterBill.getText().toString() + "SAR");
        } else if (afterBill.getText().toString().isEmpty()) {
            saved.setText("= " + beforeBill.getText().toString() + "SAR");
        } else if (Float.parseFloat(beforeBill.getText().toString()) < Float.parseFloat(afterBill.getText().toString())) {
            saved.setText("= " + (Float.parseFloat(beforeBill.getText().toString()) - Float.parseFloat(afterBill.getText().toString())) + " SAR");
            saved.setTextColor(Color.RED);
        } else {
            saved.setText("= " + (Float.parseFloat(beforeBill.getText().toString()) - Float.parseFloat(afterBill.getText().toString())) + " SAR");
            saved.setTextColor(getActivity().getResources().getColor(R.color.green));
        }

    }

    private void applyStoreCode(String storecode) {
        BaseClass.showCustomLoader(getActivity());

        String language = BaseClass.checkLanguageFunction(getActivity());
        Map<String, String> body = new HashMap<String, String>();

        body.put("UserID", sharedpreferences.getString("UserID", " "));
        body.put("StoreCode", storecode);
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
        body.put("Language", language);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.APPLYCODE, getActivity(), body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {

                if (ERROR.isEmpty()) {
                    Log.e("StoreCodeResp", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            JSONObject jsonObject1 = jsonObject.getJSONObject("store");
                            String Address = jsonObject1.getString("Address");
                            String CategoryID = jsonObject1.getString("CategoryID");
                            String CategoryTitle = jsonObject1.getString("CategoryTitle");
                            String CityID = jsonObject1.getString("CityID");
                            String CityTitle = jsonObject1.getString("CityTitle");
                            StoreCoverImage = jsonObject1.getString("CoverImage");
                            String CreatedAt = jsonObject1.getString("CreatedAt");
                            String CreatedBy = jsonObject1.getString("CreatedBy");
                            String DistrictID = jsonObject1.getString("DistrictID");
                            String Hide = jsonObject1.getString("Hide");
                            String Image = jsonObject1.getString("Image");
                            String IsActive = jsonObject1.getString("IsActive");
                            String Latitude = jsonObject1.getString("Latitude");
                            String Longitude = jsonObject1.getString("Longitude");
                            String SortOrder = jsonObject1.getString("SortOrder");
                            StoreID = jsonObject1.getString("StoreID");
                            String StoreTextID = jsonObject1.getString("StoreTextID");
                            String SystemLanguageID = jsonObject1.getString("SystemLanguageID");
                            StoreTitle = jsonObject1.getString("Title");
                            String UpdatedAt = jsonObject1.getString("UpdatedAt");
                            String UpdatedBy = jsonObject1.getString("UpdatedBy");

                            confirmBillDialog();


                        } else {
                            BaseClass.dialog.dismiss();
                            String message = jsonObject.getString("message");
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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

    private void rate(String comment, String reason) {

        String language = BaseClass.checkLanguageFunction(getActivity());
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

//        BaseClass.showCustomLoader(MainActivity.this);
        Map<String, String> body = new HashMap<String, String>();

        body.put("UserID", sharedpreferences.getString("UserID", " "));
        body.put("Rating", Rating);
        body.put("RatingType", "Rating");
        body.put("TransactionID", TransactionID);
        body.put("Reason", reason);
        body.put("Comment", comment);
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
        body.put("Language", language);

        Log.e("body", "");

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.saveRating, getActivity(), body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {

                if (ERROR.isEmpty()) {
                    Log.e("RateResp", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            BaseClass.dialog.dismiss();
                            JSONObject jsonObject1 = jsonObject.getJSONObject("rating_detail");
                            String Comment = jsonObject1.getString("Comment");
                            String Rating = jsonObject1.getString("Rating");
                            String RatingID = jsonObject1.getString("RatingID");
                            String RatingType = jsonObject1.getString("RatingType");
                            String Reason = jsonObject1.getString("Reason");
                            String TransactionID = jsonObject1.getString("TransactionID");

//                            Dialog regsuccess = new Dialog(MainActivity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
//                            regsuccess.setCancelable(false);
//                            regsuccess.setContentView(R.layout.redeem_successfull_dialog);
//                            rateYourExpeirenceDialog.dismiss();
//                            regsuccess.show();
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    regsuccess.dismiss();
//                                    BaseClass.dialog.dismiss();
//                                    bottomNavigationView.setSelectedItemId(R.id.bottom_menu);
//                                    setFragment(select_pro_frag);
//                                    active = select_pro_frag;
//                                    mDrarLayout.closeDrawers();
//                                }
//                            }, 1000);

                        } else {
                            BaseClass.dialog.dismiss();
                            String message = jsonObject.getString("message");
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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

    private void complain(String comment, String reason) {

        String language = BaseClass.checkLanguageFunction(getActivity());
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        BaseClass.showCustomLoader(getActivity());
        Map<String, String> body = new HashMap<String, String>();

        body.put("UserID", sharedpreferences.getString("UserID", " "));
        body.put("RatingType", "Complain");
        body.put("TransactionID", TransactionID);
        body.put("Reason", reason);
        body.put("Comment", comment);
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
        body.put("Language", language);

        Log.e("body", "");

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.saveRating, getActivity(), body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {

                if (ERROR.isEmpty()) {
                    Log.e("ComplainResp", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            BaseClass.dialog.dismiss();
                            complaintDialog.dismiss();

//                            Dialog regsuccess = new Dialog(MainActivity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
//                            regsuccess.setCancelable(false);
//                            regsuccess.setContentView(R.layout.redeem_successfull_dialog);
//                            regsuccess.show();
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    regsuccess.dismiss();
//                                    bottomNavigationView.setSelectedItemId(R.id.bottom_menu);
//                                    setFragment(select_pro_frag);
//                                    active = select_pro_frag;
//                                    mDrarLayout.closeDrawers();
//                                }
//                            }, 1000);
//                            JSONObject jsonObject1 = jsonObject.getJSONObject("rating_detail");
//                            String Comment = jsonObject1.getString("Comment");
//                            String Rating = jsonObject1.getString("Rating");
//                            String RatingID = jsonObject1.getString("RatingID");
//                            String RatingType = jsonObject1.getString("RatingType");
//                            String Reason = jsonObject1.getString("Reason");
//                            String TransactionID = jsonObject1.getString("TransactionID");

                        } else {
                            BaseClass.dialog.dismiss();
                            String message = jsonObject.getString("message");
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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

    private void addToFav() {

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        Map<String,String> body = new HashMap<String, String>();
        body.put("UserID", sharedpreferences.getString("UserID",""));
        body.put("StoreID", sharedpreferences.getString("StoreID", ""));
        body.put("Type", "add");
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
//        body.put("Language",language);

        HashMap<String,String> header = new HashMap<String, String>();
        header.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.addStoreToFavourite, getActivity(), body, header, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {

                if (ERROR.isEmpty()){
                    Log.e("AddToFavResp", result + " ");
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        String message = jsonObject.getString("message");
                        int status = jsonObject.getInt("status");
                        if (status == 200){
                            favBtn.setLiked(true);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getActivity(), ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void removeFromFav() {

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        Map<String,String> body = new HashMap<String, String>();
        body.put("UserID", sharedpreferences.getString("UserID",""));
        body.put("StoreID", sharedpreferences.getString("StoreID", ""));
        body.put("Type", "remove");
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
//        body.put("Language",language);

        HashMap<String,String> header = new HashMap<String, String>();
        header.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.addStoreToFavourite, getActivity(), body, header, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {

                if (ERROR.isEmpty()){
                    Log.e("RemoveFromFavResp", result + " ");
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        String message = jsonObject.getString("message");
                        int status = jsonObject.getInt("status");
                        if (status == 200){
                            favBtn.setLiked(false);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getActivity(), ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void merchantCodeDialog() {
        merchantCodeDialog = new Dialog(getActivity(), android.R.style.ThemeOverlay_Material_Dialog_Alert);
        merchantCodeDialog.setContentView(R.layout.merchant_code_dialog);
        Button applyCode = merchantCodeDialog.findViewById(R.id.applyCode);
        Button compain = merchantCodeDialog.findViewById(R.id.complain);
        EditText storecode = merchantCodeDialog.findViewById(R.id.storeCode);
        RoundedImageView cardImg = merchantCodeDialog.findViewById(R.id.cardImg);
        TextView userName = merchantCodeDialog.findViewById(R.id.username);
        TextView companyName = merchantCodeDialog.findViewById(R.id.companyName);

        applyCode.setBackgroundDrawable(new BaseClass.DrawableGradient(new int[]{Color.parseColor(sharedpreferences.getString("PackageColorUpper", "")), Color.parseColor(sharedpreferences.getString("PackageColorLower", "")), Color.parseColor(sharedpreferences.getString("PackageColorUpper", ""))}, 8));

        userName.setText(sharedpreferences.getString("FullName", ""));
        companyName.setText(sharedpreferences.getString("CompanyName", ""));
        Picasso.get().load(Constants.URL.IMG_URL + sharedpreferences.getString("CardImage", "")).into(cardImg);

        merchantCodeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        merchantCodeDialog.show();
        applyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (storecode.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.pleaseEnterStoreCode), Toast.LENGTH_SHORT).show();
                } else {
                    applyStoreCode(storecode.getText().toString());
                }
//                merchantCodeDialog.dismiss();
//                BaseClass.dialog.dismiss();
//                confirmBillDialog();
            }
        });
        compain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DynamicToast.makeSuccess(LoginActivity.this, "Mmm, working on it!", 3).show();
                merchantCodeDialog.dismiss();
                raisacomplaintDialog();
            }
        });

        merchantCodeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
//                bottomNavigationView.setSelectedItemId(R.id.bottom_menu);
//                mDrarLayout.closeDrawers();
            }
        });
    }

    private void confirmBillDialog() {
        confirmBillDialog = new Dialog(getActivity(), android.R.style.ThemeOverlay_Material_Dialog_Alert);
        confirmBillDialog.setContentView(R.layout.confirm_bill_dialog);
        Button submitBill = confirmBillDialog.findViewById(R.id.submitBill);
        uploadid = confirmBillDialog.findViewById(R.id.uploadid);
        RoundedImageView storeImg = confirmBillDialog.findViewById(R.id.storeImg);
        TextView storeName = confirmBillDialog.findViewById(R.id.storeName);
        beforeBill = confirmBillDialog.findViewById(R.id.billBefore);
        afterBill = confirmBillDialog.findViewById(R.id.billAfter);
        saved = confirmBillDialog.findViewById(R.id.saved);

        beforeBill.setFilters(new InputFilter[]{new BaseClass.DecimalDigitsInputFilter(6, 2)});
        afterBill.setFilters(new InputFilter[]{new BaseClass.DecimalDigitsInputFilter(6, 2)});

        submitBill.setBackgroundDrawable(new BaseClass.DrawableGradient(new int[]{Color.parseColor(sharedpreferences.getString("PackageColorUpper", "")), Color.parseColor(sharedpreferences.getString("PackageColorLower", "")), Color.parseColor(sharedpreferences.getString("PackageColorUpper", ""))}, 8));
//        Button compain = merchantCodeDialog.findViewById(R.id.complain);
        Picasso.get().load(Constants.URL.IMG_URL + StoreCoverImage).into(storeImg);
        storeName.setText(StoreTitle);
        confirmBillDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confirmBillDialog.show();

        beforeBill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (beforeBill.getText().toString().startsWith(".")) {
                    beforeBill.setError(getResources().getString(R.string.startwithdoterrormessage));
                    beforeBill.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        });

        afterBill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (afterBill.getText().toString().startsWith(".")) {
                    afterBill.setError(getResources().getString(R.string.startwithdoterrormessage));
                    afterBill.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        });

        submitBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                if (beforeBill.getText().toString().isEmpty()) {
                    beforeBill.startAnimation(shake);
                } else if (afterBill.getText().toString().isEmpty()) {
                    afterBill.setAnimation(shake);
                } else {
                    saveTransaction(StoreID, beforeBill.getText().toString(), afterBill.getText().toString());
                    BaseClass.dialog.dismiss();
                }
//                confirmBillDialog.dismiss();
//                rateYourExpeirenceDialog();
            }
        });
        uploadid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reqCode = 3333;
                options = Options.init()
                        .setRequestCode(100)
                        .setCount(1);
                Log.e("cameraClick", "yes");
                returnValue.clear();
                Pix.start(getActivity(),                    //Activity or Fragment Instance
                        options);
            }
        });
    }

    private void rateYourExpeirenceDialog() {
        final String[] title = {" "};
        rateYourExpeirenceDialog = new Dialog(getActivity(), android.R.style.ThemeOverlay_Material_Dialog_Alert);
        rateYourExpeirenceDialog.setContentView(R.layout.rate_experience_dialog);
        ChipCloud chipCloud = rateYourExpeirenceDialog.findViewById(R.id.chipcloud);
        Button ratingDone = rateYourExpeirenceDialog.findViewById(R.id.ratingDone);
        Button skipRating = rateYourExpeirenceDialog.findViewById(R.id.skipRating);
        Button complain = rateYourExpeirenceDialog.findViewById(R.id.complain);
        RatingBar ratingBar = rateYourExpeirenceDialog.findViewById(R.id.ratingBar);
        EditText comment = rateYourExpeirenceDialog.findViewById(R.id.comment);

        ratingDone.setBackgroundDrawable(new BaseClass.DrawableGradient(new int[]{Color.parseColor(sharedpreferences.getString("PackageColorUpper", "")), Color.parseColor(sharedpreferences.getString("PackageColorLower", "")), Color.parseColor(sharedpreferences.getString("PackageColorUpper", ""))}, 8));

        rateYourExpeirenceDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        rateYourExpeirenceDialog.show();

        String list[] = {"EXCELLENT SERVICS", "RECOMMEND THIS PLACE", "ABOVE AND BEYOND", "OTHER"};

        chipCloud.addChips(list);
        chipCloud.setChipListener(new ChipListener() {
            @Override
            public void chipSelected(int index) {
                title[0] = list[index];
                Log.e("SelectedTitle", title[0]);
            }

            @Override
            public void chipDeselected(int index) {

            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Rating = String.valueOf(rating);
            }
        });

        ratingDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rate(comment.getText().toString(), title[0]);
                BaseClass.dialog.dismiss();
//                rateYourExpeirenceDialog.dismiss();
            }
        });
        skipRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DynamicToast.makeSuccess(LoginActivity.this, "Mmm, working on it!", 3).show();
                rateYourExpeirenceDialog.dismiss();
//                Dialog regsuccess = new Dialog(MainActivity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
//                regsuccess.setCancelable(false);
//                regsuccess.setContentView(R.layout.redeem_successfull_dialog);
//                regsuccess.show();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        regsuccess.dismiss();
//                        BaseClass.dialog.dismiss();
////                        rateYourExpeirenceDialog.dismiss();
//                        bottomNavigationView.setSelectedItemId(R.id.bottom_menu);
//                        setFragment(select_pro_frag);
//                        active = select_pro_frag;
//                        mDrarLayout.closeDrawers();
//                    }
//                }, 1000);
            }
        });
        complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateYourExpeirenceDialog.dismiss();
                complaintDialog();
//                DynamicToast.makeSuccess(LoginActivity.this, "Mmm, working on it!", 3).show();
            }
        });
    }

    private void raisacomplaintDialog() {
        final String[] title = {""};
        raisacomplain = new Dialog(getActivity(), android.R.style.ThemeOverlay_Material_Dialog_Alert);
        raisacomplain.setContentView(R.layout.raisacomlaint_dialog);
        complaintitle = raisacomplain.findViewById(R.id.complain_title);
        select_city = raisacomplain.findViewById(R.id.city_selection);
        ChipCloud complain_cloud = raisacomplain.findViewById(R.id.complain_chipcloud);
        Button complainsubmit = raisacomplain.findViewById(R.id.complainsubmit);
        EditText complain_comment = raisacomplain.findViewById(R.id.complain_comment);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, storeName);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        complaintitle.setAdapter(adapter);

        selectstoresClickListener();
        selectbranchesClickListener();

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, branchesNames);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        select_city.setAdapter(adapter1);

        complainsubmit.setBackgroundDrawable(new BaseClass.DrawableGradient(new int[]{Color.parseColor(sharedpreferences.getString("PackageColorUpper", "")), Color.parseColor(sharedpreferences.getString("PackageColorLower", "")), Color.parseColor(sharedpreferences.getString("PackageColorUpper", ""))}, 8));

        raisacomplain.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        raisacomplain.show();

        String list[] = {"VERY BAD SERVICE", "NOT RECOMMENDED", "HIGH PRICES", "WORST SERVICE"};
        complain_cloud.addChips(list);
        complain_cloud.setChipListener(new ChipListener() {
            @Override
            public void chipSelected(int index) {
                title[0] = list[index];
                Log.e("SelectedTitle", title[0]);
            }

            @Override
            public void chipDeselected(int index) {

            }
        });

        complainsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (complaintitle.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.selectstore), Toast.LENGTH_LONG).show();
                }
//                else if (select_city.getText().toString().isEmpty()) {
//                    Toast.makeText(MainActivity.this, getResources().getString(R.string.selectbranch), Toast.LENGTH_LONG).show();
//                }
                else if (complain_comment.getText().toString().equals("") && title[0].equals("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.riseAComplaint), Toast.LENGTH_LONG).show();
                } else {
                    submitComplainDirect(complain_comment.getText().toString(), title[0]);
                }
                BaseClass.dialog.dismiss();
            }
        });
    }

    private void submitComplainDirect(String comment, String reason) {
        String language = BaseClass.checkLanguageFunction(getActivity());
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        BaseClass.showCustomLoader(getActivity());
        Map<String, String> body = new HashMap<String, String>();

        body.put("UserID", sharedpreferences.getString("UserID", " "));
        body.put("RatingType", "Complain");
        body.put("StoreID", BranchStoreId);
        body.put("Reason", reason);
        body.put("Comment", comment);
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));

        Log.e("directComplainBody", body.toString());

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.saveRating, getActivity(), body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {

                if (ERROR.isEmpty()) {
                    Log.e("DirectComplainResp", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            BaseClass.dialog.dismiss();
                            raisacomplain.dismiss();

                            Toast.makeText(getActivity(), getResources().getString(R.string.complainsuccess), Toast.LENGTH_LONG).show();

                        } else {
                            BaseClass.dialog.dismiss();
                            String message = jsonObject.getString("message");
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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

    private void complaintDialog() {
        final String[] title = {" "};
        ArrayList<String> tags = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            tags.add("Tag" + i);
        }
        complaintDialog = new Dialog(getActivity(), android.R.style.ThemeOverlay_Material_Dialog_Alert);
        complaintDialog.setContentView(R.layout.comlaint_dialog);
        ChipCloud cloud = complaintDialog.findViewById(R.id.chipcloud);
        Button complainDone = complaintDialog.findViewById(R.id.complainDone);
        EditText comment = complaintDialog.findViewById(R.id.comment);

        complainDone.setBackgroundDrawable(new BaseClass.DrawableGradient(new int[]{Color.parseColor(sharedpreferences.getString("PackageColorUpper", "")), Color.parseColor(sharedpreferences.getString("PackageColorLower", "")), Color.parseColor(sharedpreferences.getString("PackageColorUpper", ""))}, 8));

        complaintDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        complaintDialog.show();

        String list[] = {"VERY BAD SERVICE", "NOT RECOMMENDED", "HIGH PRICES", "WORST SERVICE"};

        cloud.addChips(list);

        cloud.setChipListener(new ChipListener() {
            @Override
            public void chipSelected(int index) {
                title[0] = list[index];
                Log.e("SelectedTitle", title[0]);
            }

            @Override
            public void chipDeselected(int index) {

            }
        });

        complainDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complain(comment.getText().toString(), title[0]);
                BaseClass.dialog.dismiss();
            }
        });
    }
}
