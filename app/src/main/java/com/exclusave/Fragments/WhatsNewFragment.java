package com.exclusave.Fragments;

import android.content.SharedPreferences;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.exclusave.Activities.Details_Activity;
import com.exclusave.Activities.Filters_Activity;
import com.exclusave.Adapters.WhatsNewRecyclerAdapter;
import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.models.WhatsNew_Data;
import com.exclusave.BuildConfig;
import com.exclusave.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WhatsNewFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    String cityID = "",categoryID = "",subcategoryID= "", URL = "";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor mEditor;

    ArrayList<WhatsNew_Data> whatsNew_dataList = new ArrayList<>();

    RecyclerView whatsNewRecycler;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<String> arrayList = new ArrayList<>();


    @Override
    public void onResume() {
        super.onResume();

        cityID = Filters_Activity.publiccityID;
        categoryID = Filters_Activity.publiccategoryID;
        subcategoryID = Filters_Activity.publicsubCategoryID;

        if (!cityID.isEmpty() || !categoryID.isEmpty() || !subcategoryID.isEmpty()){

        Log.e("called", "called called");
        BaseClass.showCustomLoader(getActivity());
        String language = BaseClass.checkLanguageFunction(getActivity());
        whatsNewRecycler.removeAllViews();
        String url = "";
        if (sharedPreferences.getString("isLoggedIn", "").equals("0")) {
            url = Constants.URL.BASE_URL + "stores?AndroidAppVersion=" + BuildConfig.VERSION_CODE+"&Language="+language;
        } else if (sharedPreferences.getString("isLoggedIn", "").equals("1")) {
            url = Constants.URL.STORE + sharedPreferences.getString("UserID", "") + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&OrderBy=DESC" + "&CardID=" +  sharedPreferences.getString("CardID", "")+"&CategoryID"+ categoryID + "&SubCategoryID" +subcategoryID+"&CityID"+cityID+"&Language="+language;
        }

        whatsNewApiCall(url);
        }

    }

    public static WhatsNewFragment newInstance(String param1, String param2) {
        WhatsNewFragment fragment = new WhatsNewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_whats_new, container, false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {

        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = BaseClass.sharedPreferances(getActivity());
        mEditor = BaseClass.sharedPreferancesEditor(getActivity());
        initailizeView();

        Filters_Activity.publiccityID = "";
        Filters_Activity.publiccategoryID = "";
        Filters_Activity.publicsubCategoryID = "";

        // whatsNew();

        String language = BaseClass.checkLanguageFunction(getActivity());
        if (sharedPreferences.getString("isLoggedIn", "").equals("0")) {
            URL = Constants.URL.BASE_URL + "stores?AndroidAppVersion=" + BuildConfig.VERSION_CODE +"&OrderBy=DESC"+"&Language="+language;
        } else if (sharedPreferences.getString("isLoggedIn", "").equals("1")) {
            URL = Constants.URL.STORE + sharedPreferences.getString("UserID", "") + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&OrderBy=DESC"+"&Language="+language;
        }
        whatsNewApiCall(URL);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        whatsNewApiCallRefresh(URL);
                    }
                }
        );
    }

    private void initailizeView() {
        whatsNewRecycler = getView().findViewById(R.id.whatsNewRecycler);
        whatsNewRecycler.setItemViewCacheSize(25);
        whatsNewRecycler.setDrawingCacheEnabled(true);
        whatsNewRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        swipeRefreshLayout = getView().findViewById(R.id.order_swipeRefresh);
    }

    @Override
    public void onClick(View v) {

    }

    private void whatsNewApiCall(String URL) {
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.GET, URL, getActivity(), body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                if (ERROR.isEmpty()) {
                    Log.e("GetWhatsResponse", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {
                            whatsNew_dataList.clear();
                            BaseClass.dialog.dismiss();
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
                                String DistrictID = object.getString("DistrictID");
                                String Hide = object.getString("Hide");
                                String Image = object.getString("Image");
                                String IsActive = object.getString("IsActive");
                                String Latitude = object.getString("Latitude");
                                String Longitude = object.getString("Longitude");
                                String SortOrder = object.getString("SortOrder");
                                String StoreID = object.getString("StoreID");
                                String StoreTextID = object.getString("StoreTextID");
                                String SystemLanguageID = object.getString("SystemLanguageID");
                                String Title = object.getString("Title");
                                String UpdatedAt = object.getString("UpdatedAt");
                                String UpdatedBy = object.getString("UpdatedBy");
                                Integer IsAddedToFavourite = object.getInt("IsAddedToFavourite");
                                String CategoryImage = object.getString("CategoryImage");
                                String UserID = object.getString("UserID");

                                whatsNew_dataList.add(new WhatsNew_Data(Address, CategoryID, CategoryTitle, CityID, CityTitle, CoverImage, CreatedAt, CreatedBy, DistrictID, Hide,
                                        Image, IsActive, Latitude, Longitude, SortOrder, StoreID, StoreTextID, SystemLanguageID, Title, UpdatedAt, UpdatedBy, IsAddedToFavourite, CategoryImage, UserID));

                                whatsNewRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                                WhatsNewRecyclerAdapter adapter = new WhatsNewRecyclerAdapter(getActivity(), whatsNew_dataList, new WhatsNewRecyclerAdapter.CustomItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, int position) {

                                        if (sharedPreferences.getString("isLoggedIn", "").equals("1")) {
                                            mEditor.putString("StoreID", whatsNew_dataList.get(position).getStoreID()).commit();
                                            mEditor.putString("ReceiverID", whatsNew_dataList.get(position).getUserID()).commit();
                                            Intent intent = new Intent(getActivity(), Details_Activity.class);
                                            intent.putExtra("Title", whatsNew_dataList.get(position).getTitle());
                                            startActivity(intent);
                                            // startActivity(new Intent(getActivity(), Details_Activity.class));
                                        } else {
                                            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.PleaseLogin), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                                whatsNewRecycler.setAdapter(adapter);

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

    private void whatsNewApiCallRefresh(String URL) {
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.GET, URL, getActivity(), body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                if (ERROR.isEmpty()) {
                    Log.e("GetWhatsResponse", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {
                            whatsNew_dataList.clear();
                            swipeRefreshLayout.setRefreshing(false);
                            BaseClass.dialog.dismiss();
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
                                String DistrictID = object.getString("DistrictID");
                                String Hide = object.getString("Hide");
                                String Image = object.getString("Image");
                                String IsActive = object.getString("IsActive");
                                String Latitude = object.getString("Latitude");
                                String Longitude = object.getString("Longitude");
                                String SortOrder = object.getString("SortOrder");
                                String StoreID = object.getString("StoreID");
                                String StoreTextID = object.getString("StoreTextID");
                                String SystemLanguageID = object.getString("SystemLanguageID");
                                String Title = object.getString("Title");
                                String UpdatedAt = object.getString("UpdatedAt");
                                String UpdatedBy = object.getString("UpdatedBy");
                                Integer IsAddedToFavourite = object.getInt("IsAddedToFavourite");
                                String CategoryImage = object.getString("CategoryImage");
                                String UserID = object.getString("UserID");


                                whatsNew_dataList.add(new WhatsNew_Data(Address, CategoryID, CategoryTitle, CityID, CityTitle, CoverImage, CreatedAt, CreatedBy, DistrictID, Hide,
                                        Image, IsActive, Latitude, Longitude, SortOrder, StoreID, StoreTextID, SystemLanguageID, Title, UpdatedAt, UpdatedBy, IsAddedToFavourite, CategoryImage, UserID));

                                whatsNewRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                                WhatsNewRecyclerAdapter adapter = new WhatsNewRecyclerAdapter(getActivity(), whatsNew_dataList, new WhatsNewRecyclerAdapter.CustomItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, int position) {

                                        if (sharedPreferences.getString("isLoggedIn", "").equals("1")) {
                                            mEditor.putString("StoreID", whatsNew_dataList.get(position).getStoreID()).commit();
                                            mEditor.putString("ReceiverID", whatsNew_dataList.get(position).getUserID()).commit();
                                            Intent intent = new Intent(getActivity(), Details_Activity.class);
                                            intent.putExtra("Title", whatsNew_dataList.get(position).getTitle());
                                            startActivity(intent);
                                            // startActivity(new Intent(getActivity(), Details_Activity.class));
                                        } else {
                                            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.PleaseLogin), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                                whatsNewRecycler.setAdapter(adapter);

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

}
