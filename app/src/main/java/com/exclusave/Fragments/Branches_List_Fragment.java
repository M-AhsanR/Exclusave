package com.exclusave.Fragments;

import android.content.SharedPreferences;
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

import com.android.volley.Request;
import com.exclusave.Activities.MainActivity;
import com.exclusave.Adapters.Branches_list_Adapter;
import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.models.BranchesList_Data;
import com.exclusave.models.Branches_list_Model;
import com.exclusave.BuildConfig;
import com.exclusave.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Branches_List_Fragment extends Fragment {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor mEditor;


    RecyclerView recyclerView;
    ArrayList<BranchesList_Data> branchesList_data = new ArrayList<>();
    ArrayList<Branches_list_Model> list = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_branches,container,false);
        return view;

  //      viewPager.setCurrentItem(0);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = BaseClass.sharedPreferances(getActivity());
        mEditor = BaseClass.sharedPreferancesEditor(getActivity());
        initilizeViews(view);

        if(sharedPreferences.getString("MapType", "").equals("Stores")){
            branchesListViewApiCall();
        }else if(sharedPreferences.getString("MapType", "").equals("Branches")){
            branchesListApiCall();
        }
    }

    private void initilizeViews(View view) {
        recyclerView = view.findViewById(R.id.branches_recycler);
    }


    private void branchesListViewApiCall() {
        String language = BaseClass.checkLanguageFunction(getActivity());
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        String URL = "";
        if (sharedPreferences.getString("isLoggedIn", "").equals("0")) {
            URL = Constants.URL.BASE_URL + "stores?AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&CardID=1" + "&OrderBy=DESC"+"&Language="+language;
        } else if (sharedPreferences.getString("isLoggedIn", "").equals("1")) {
            URL = Constants.URL.STORE + sharedPreferences.getString("UserID", "") + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&OrderBy=DESC"+"&Language="+language;
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

                            for (int i=0; i<storearray.length(); i++){

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
                                String DistrictTitle = object.getString("DistrictTitle");
                                String Email = object.getString("Email");
                                String FacebookLink = object.getString("FacebookLink");
                                String FirstName = object.getString("FirstName");
                                String Hide = object.getString("Hide");
                                String Image = object.getString("Image");
                                String InstagramLink = object.getString("InstagramLink");
                                String IsActive = object.getString("IsActive");
                                String LastName = object.getString("LastName");
                                String Latitude = object.getString("Latitude");
                                String Longitude = object.getString("Longitude");
                                String MiddleName = object.getString("MiddleName");
                                String Mobile = object.getString("Mobile");
                                String ParentID = object.getString("ParentID");
                                String SortOrder = object.getString("SortOrder");
                                String StoreID = object.getString("StoreID");
                                String StoreTextID = object.getString("StoreTextID");
                                String SystemLanguageID = object.getString("SystemLanguageID");
                                String Title = object.getString("Title");
                                String TwitterLink = object.getString("TwitterLink");
                                String UpdatedAt = object.getString("UpdatedAt");
                                String UpdatedBy = object.getString("UpdatedBy");
                                String WebsiteLink = object.getString("WebsiteLink");

                                branchesList_data.add(new BranchesList_Data(Address,CategoryID,CategoryTitle,CityID,CityTitle,CoverImage,CreatedAt,CreatedBy,DistrictID,
                                        DistrictTitle,Email,FacebookLink,FirstName,Hide,Image,InstagramLink,IsActive,LastName,Latitude,Longitude,
                                        MiddleName,Mobile,ParentID,SortOrder,StoreID,StoreTextID,SystemLanguageID,Title,TwitterLink,UpdatedAt,UpdatedBy,WebsiteLink));


                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                Branches_list_Adapter adapter = new Branches_list_Adapter(getActivity(), branchesList_data, new Branches_list_Adapter.CustomItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, int position) {

                                    }
                                });
                                recyclerView.setAdapter(adapter);

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

    private void branchesListApiCall() {
        String language = BaseClass.checkLanguageFunction(getActivity());
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        String URL = "";
        if (sharedPreferences.getString("isLoggedIn", "").equals("0")) {
            URL = Constants.URL.BASE_URL + "stores?AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&CardID=1" + "&OrderBy=DESC" + "&Language=" + language;
        } else if (sharedPreferences.getString("isLoggedIn", "").equals("1")) {
            URL = Constants.URL.BRANCHES + sharedPreferences.getString("UserID", "") + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&OrderBy=DESC" + "&ParentID=" + sharedPreferences.getString("StoreID", "");
            Log.e("URL", URL);
        }

        ApiModelClass.GetApiResponse(Request.Method.GET, URL, getActivity(), body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                if (ERROR.isEmpty()) {
                    Log.e("BranchesResponse", result);
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

//                                branchesNames.add(Title);
                                branchesList_data.add(new BranchesList_Data(Address, CategoryID, CategoryTitle, CityID, CityTitle, CoverImage, CreatedAt, CreatedBy, DistrictID,
                                        DistrictTitle, Email, FacebookLink, FirstName, Hide, Image, InstagramLink, IsActive, LastName, Latitude, Longitude,
                                        MiddleName, Mobile, ParentID, SortOrder, StoreID, StoreTextID, SystemLanguageID, Title, TwitterLink, UpdatedAt, UpdatedBy, WebsiteLink));

                            }

                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            Branches_list_Adapter adapter = new Branches_list_Adapter(getActivity(), branchesList_data, new Branches_list_Adapter.CustomItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {

                                }
                            });
                            recyclerView.setAdapter(adapter);

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
