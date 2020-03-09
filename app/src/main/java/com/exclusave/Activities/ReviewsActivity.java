package com.exclusave.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.exclusave.Adapters.ReviewsAdapter;
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

public class ReviewsActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    RecyclerView reviewsRecycler;
    ArrayList<History_Model> reviewsData = new ArrayList<>();
    String StoreID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        sharedPreferences = BaseClass.sharedPreferances(ReviewsActivity.this);
        editor = BaseClass.sharedPreferancesEditor(ReviewsActivity.this);
        Bundle bundle = getIntent().getExtras();
        if(!bundle.isEmpty()){
            StoreID = bundle.getString("StoreID");
        }
        initilizeViews();

        getReviews();
    }

    private void initilizeViews() {
        reviewsRecycler = findViewById(R.id.reviewsRecycler);
    }

    private void getReviews() {
        String language = BaseClass.checkLanguageFunction(ReviewsActivity.this);
        BaseClass.showCustomLoader(ReviewsActivity.this);

        final Map<String, String> body = new HashMap<String, String>();
        body.put("StoreID", StoreID);
        body.put("UserID", sharedPreferences.getString("UserID", ""));
//        body.put("TransactionUserID",sharedPreferences.getString("UserID", ""));
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
        body.put("Language",language);

        final HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.GETTRANSACTION, ReviewsActivity.this, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {

                if (ERROR.isEmpty()) {
                    Log.e("ReviewsResp", Constants.URL.GETTRANSACTION);
                    Log.e("ReviewsBody", body.toString());
                    Log.e("ReviewsHeader", headers.toString());
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

                                if(!RatingID.equals("")){
                                    reviewsData.add(new History_Model(BillAfter,BillBefore,CardTypeTitle,Comment,CompressedCoverImage,CompressedImage,CoverImage,CreatedAt,
                                            Discount,Email,FamilyMemberFirstName,FamilyMemberLastName,FamilyMemberMiddleName,FamilyMemberMobile,FirstName,IsFamilyMember,
                                            Image,LastName,MiddleName,Mobile,Rating,RatingID,RatingType,Reason,Receipt,StoreID,Title,TransactionID,UserID, StoreImage, FamilyMemberImage));
                                }
                            }

                            BaseClass.dialog.dismiss();

                            reviewsRecycler.setLayoutManager(new LinearLayoutManager(ReviewsActivity.this));
                            ReviewsAdapter adapter = new ReviewsAdapter(ReviewsActivity.this, reviewsData, new ReviewsAdapter.CustomItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {

                                }
                            });
                            reviewsRecycler.setAdapter(adapter);

                        } else {
                            BaseClass.dialog.dismiss();
                            String message = jsonObject.getString("message");
                            Toast.makeText(ReviewsActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(ReviewsActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
