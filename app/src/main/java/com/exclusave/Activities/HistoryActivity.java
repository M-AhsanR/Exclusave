package com.exclusave.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.exclusave.Adapters.History_Adapter;
import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.models.History_Model;
import com.exclusave.BuildConfig;
import com.exclusave.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HistoryActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    RecyclerView history_recyc;
    ArrayList<History_Model> arrayListDashBordData = new ArrayList<>();

    CircleImageView profilepic;
    TextView merchantName,Branch, redemptios_count, view_all_button;
    Button redeem_customer;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        sharedPreferences = BaseClass.sharedPreferances(HistoryActivity.this);
        editor = BaseClass.sharedPreferancesEditor(HistoryActivity.this);
        initilizeViews();

        getTransactionsApiCall();

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getRefreshedTransactionsApiCall();
                    }
                }
        );
    }

    private void getTransactionsApiCall() {
        String language = BaseClass.checkLanguageFunction(HistoryActivity.this);
        BaseClass.showCustomLoader(HistoryActivity.this);

        final Map<String, String> body = new HashMap<String, String>();
//        body.put("StoreID", sharedPreferences.getString("StoreID", ""));
        body.put("UserID", sharedPreferences.getString("UserID", ""));
        body.put("TransactionUserID",sharedPreferences.getString("UserID", ""));
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
        body.put("Language",language);

        final HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.GETTRANSACTION, HistoryActivity.this, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {

                if (ERROR.isEmpty()) {
                    Log.e("DashbordResp", Constants.URL.GETTRANSACTION);
                    Log.e("DashbordResp", body.toString());
                    Log.e("DashbordResp", headers.toString());
                    Log.e("DashbordResp", result);
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {
                            JSONArray jsonArray = jsonObject.getJSONArray("transactions");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String BillAfter = jsonObject1.getString("BillAfter");
                                String BillBefore = jsonObject1.getString("BillBefore");
                                String CardTypeTitle = jsonObject1.getString("CardTypeTitle");
                                String Comment = jsonObject1.getString("Comment");
                                String CompressedCoverImage = jsonObject1.getString("CompressedCoverImage");
                                String CompressedImage = jsonObject1.getString("CompressedImage");
                                String CoverImage = jsonObject1.getString("CoverImage");
                                String CreatedAt = jsonObject1.getString("CreatedAt");
                                String Discount = jsonObject1.getString("Discount");
                                String Email = jsonObject1.getString("Email");
                                String FamilyMemberFirstName = jsonObject1.getString("FamilyMemberFirstName");
                                String FamilyMemberLastName = jsonObject1.getString("FamilyMemberLastName");
                                String FamilyMemberMiddleName = jsonObject1.getString("FamilyMemberMiddleName");
                                String FamilyMemberMobile = jsonObject1.getString("FamilyMemberMobile");
                                String FirstName = jsonObject1.getString("FirstName");
                                String IsFamilyMember = jsonObject1.getString("IsFamilyMember");
                                String Image = jsonObject1.getString("Image");
                                String LastName = jsonObject1.getString("LastName");
                                String MiddleName = jsonObject1.getString("MiddleName");
                                String Mobile = jsonObject1.getString("Mobile");
                                String Rating = jsonObject1.getString("Rating");
                                String RatingID = jsonObject1.getString("RatingID");
                                String RatingType = jsonObject1.getString("RatingType");
                                String Reason = jsonObject1.getString("Reason");
                                String Receipt = jsonObject1.getString("Receipt");
                                String StoreID = jsonObject1.getString("StoreID");
                                String Title = jsonObject1.getString("Title");
                                String TransactionID = jsonObject1.getString("TransactionID");
                                String UserID = jsonObject1.getString("UserID");
                                String StoreImage = jsonObject1.getString("StoreImage");
                                String FamilyMemberImage = jsonObject1.getString("FamilyMemberImage");


                                arrayListDashBordData.add(new History_Model(BillAfter,BillBefore,CardTypeTitle,Comment,CompressedCoverImage,CompressedImage,CoverImage,CreatedAt,
                                        Discount,Email,FamilyMemberFirstName,FamilyMemberLastName,FamilyMemberMiddleName,FamilyMemberMobile,FirstName,IsFamilyMember,
                                        Image,LastName,MiddleName,Mobile,Rating,RatingID,RatingType,Reason,Receipt,StoreID,Title,TransactionID,UserID, StoreImage,FamilyMemberImage));
                            }

                            BaseClass.dialog.dismiss();

//                            history_recyc.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
//                            DashBorddummy_Adapter adapter = new DashBorddummy_Adapter(HistoryActivity.this, arrayListDashBordData, new DashBorddummy_Adapter.CustomItemClickListener() {
//                                @Override
//                                public void onItemClick(View v, int position) {
//
//                                }
//                            });
//                            history_recyc.setAdapter(adapter);

                            history_recyc.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
                            History_Adapter adapter = new History_Adapter(HistoryActivity.this, arrayListDashBordData, new History_Adapter.CustomItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {

                                }
                            });
                            history_recyc.setAdapter(adapter);


                        } else {
                            BaseClass.dialog.dismiss();
                            String message = jsonObject.getString("message");
                            Toast.makeText(HistoryActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(HistoryActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getRefreshedTransactionsApiCall() {
        String language = BaseClass.checkLanguageFunction(HistoryActivity.this);
        BaseClass.showCustomLoader(HistoryActivity.this);

        final Map<String, String> body = new HashMap<String, String>();
//        body.put("StoreID", sharedPreferences.getString("StoreID", ""));
        body.put("UserID", sharedPreferences.getString("UserID", ""));
        body.put("TransactionUserID",sharedPreferences.getString("UserID", ""));
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
        body.put("Language",language);

        final HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.GETTRANSACTION, HistoryActivity.this, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {

                if (ERROR.isEmpty()) {
                    Log.e("DashbordResp", Constants.URL.GETTRANSACTION);
                    Log.e("DashbordResp", body.toString());
                    Log.e("DashbordResp", headers.toString());
                    Log.e("DashbordResp", result);
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {
                            arrayListDashBordData.clear();
                            swipeRefreshLayout.setRefreshing(false);
                            JSONArray jsonArray = jsonObject.getJSONArray("transactions");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String BillAfter = jsonObject1.getString("BillAfter");
                                String BillBefore = jsonObject1.getString("BillBefore");
                                String CardTypeTitle = jsonObject1.getString("CardTypeTitle");
                                String Comment = jsonObject1.getString("Comment");
                                String CompressedCoverImage = jsonObject1.getString("CompressedCoverImage");
                                String CompressedImage = jsonObject1.getString("CompressedImage");
                                String CoverImage = jsonObject1.getString("CoverImage");
                                String CreatedAt = jsonObject1.getString("CreatedAt");
                                String Discount = jsonObject1.getString("Discount");
                                String Email = jsonObject1.getString("Email");
                                String FamilyMemberFirstName = jsonObject1.getString("FamilyMemberFirstName");
                                String FamilyMemberLastName = jsonObject1.getString("FamilyMemberLastName");
                                String FamilyMemberMiddleName = jsonObject1.getString("FamilyMemberMiddleName");
                                String FamilyMemberMobile = jsonObject1.getString("FamilyMemberMobile");
                                String FirstName = jsonObject1.getString("FirstName");
                                String IsFamilyMember = jsonObject1.getString("IsFamilyMember");
                                String Image = jsonObject1.getString("Image");
                                String LastName = jsonObject1.getString("LastName");
                                String MiddleName = jsonObject1.getString("MiddleName");
                                String Mobile = jsonObject1.getString("Mobile");
                                String Rating = jsonObject1.getString("Rating");
                                String RatingID = jsonObject1.getString("RatingID");
                                String RatingType = jsonObject1.getString("RatingType");
                                String Reason = jsonObject1.getString("Reason");
                                String Receipt = jsonObject1.getString("Receipt");
                                String StoreID = jsonObject1.getString("StoreID");
                                String Title = jsonObject1.getString("Title");
                                String TransactionID = jsonObject1.getString("TransactionID");
                                String UserID = jsonObject1.getString("UserID");
                                String StoreImage = jsonObject1.getString("StoreImage");
                                String FamilyMemberImage = jsonObject1.getString("FamilyMemberImage");


                                arrayListDashBordData.add(new History_Model(BillAfter,BillBefore,CardTypeTitle,Comment,CompressedCoverImage,CompressedImage,CoverImage,CreatedAt,
                                        Discount,Email,FamilyMemberFirstName,FamilyMemberLastName,FamilyMemberMiddleName,FamilyMemberMobile,FirstName,IsFamilyMember,
                                        Image,LastName,MiddleName,Mobile,Rating,RatingID,RatingType,Reason,Receipt,StoreID,Title,TransactionID,UserID, StoreImage,FamilyMemberImage));
                            }

                            BaseClass.dialog.dismiss();

//                            history_recyc.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
//                            DashBorddummy_Adapter adapter = new DashBorddummy_Adapter(HistoryActivity.this, arrayListDashBordData, new DashBorddummy_Adapter.CustomItemClickListener() {
//                                @Override
//                                public void onItemClick(View v, int position) {
//
//                                }
//                            });
//                            history_recyc.setAdapter(adapter);

                            history_recyc.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
                            History_Adapter adapter = new History_Adapter(HistoryActivity.this, arrayListDashBordData, new History_Adapter.CustomItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {

                                }
                            });
                            history_recyc.setAdapter(adapter);


                        } else {
                            BaseClass.dialog.dismiss();
                            String message = jsonObject.getString("message");
                            Toast.makeText(HistoryActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(HistoryActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initilizeViews() {
        history_recyc = findViewById(R.id.history_recycler);
        swipeRefreshLayout = findViewById(R.id.order_swipeRefresh);
    }
}
