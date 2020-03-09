package com.exclusave.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.models.History_Model;
import com.exclusave.BuildConfig;
import com.exclusave.R;
import com.squareup.picasso.Picasso;
import com.stfalcon.frescoimageviewer.ImageViewer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class History_Adapter extends RecyclerView.Adapter<History_Adapter.MyViewHolder> {

    private Context context;
    History_Adapter.CustomItemClickListener listener;
    ArrayList<History_Model> commentsArray;
    String Rating = "0.0";
    SharedPreferences sharedPreferences;

    public History_Adapter(Context context, ArrayList<History_Model> commentsArray, History_Adapter.CustomItemClickListener listener) {
        this.commentsArray = commentsArray;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public History_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view;
        LayoutInflater inflater = LayoutInflater.from(context);

        view = inflater.inflate(R.layout.history_adapter, parent, false);
        final History_Adapter.MyViewHolder viewHolder = new History_Adapter.MyViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final History_Adapter.MyViewHolder holder, int position) {
        sharedPreferences = BaseClass.sharedPreferances((Activity) context);

        holder.linear_two.setVisibility(View.GONE);
        holder.linear_three.setVisibility(View.GONE);

        if (commentsArray.get(position).getRatingID().equals("")) {
            holder.expand1.setBackground(context.getResources().getDrawable(R.drawable.red_circle_icon));
        } else {
            holder.expand1.setBackground(context.getResources().getDrawable(R.drawable.drop_down_black));
        }

        holder.expand1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!commentsArray.get(position).getRatingID().equals("")) {
                    if (Integer.parseInt(commentsArray.get(position).getRatingID()) > 0) {
                        holder.linear_one.setVisibility(View.GONE);
                        holder.linear_two.setVisibility(View.GONE);
                        holder.linear_three.setVisibility(View.VISIBLE);
                        holder.ratingBar3.setRating(Float.parseFloat(commentsArray.get(position).getRating()));
                    } else {
                        holder.linear_one.setVisibility(View.GONE);
                        holder.linear_two.setVisibility(View.VISIBLE);
                        holder.linear_three.setVisibility(View.GONE);
                    }
                } else {
                    holder.linear_one.setVisibility(View.GONE);
                    holder.linear_two.setVisibility(View.VISIBLE);
                    holder.linear_three.setVisibility(View.GONE);
                }
            }
        });


        holder.expand2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((holder.commentEdit.getText().toString().isEmpty() || holder.commentEdit.getText().toString().startsWith(" ")) && Rating.equals("0.0")) {
                    holder.linear_one.setVisibility(View.VISIBLE);
                    holder.linear_two.setVisibility(View.GONE);
                    holder.linear_three.setVisibility(View.GONE);
                } else {
                    String language = BaseClass.checkLanguageFunction((Activity) context);
                    Long tsLong = System.currentTimeMillis() / 1000;
                    String ts = tsLong.toString();

                    BaseClass.showCustomLoader((Activity) context);
                    Map<String, String> body = new HashMap<String, String>();

                    body.put("UserID", sharedPreferences.getString("UserID", " "));
                    body.put("Rating", Rating);
                    body.put("RatingType", "Rating");
                    body.put("TransactionID", commentsArray.get(position).getTransactionID());
                    body.put("Comment", holder.commentEdit.getText().toString());
                    body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
//                body.put("Language", language);

                    Log.e("body", body.toString());

                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

                    ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.saveRating, context, body, headers, new ServerCallback() {
                        @Override
                        public void onSuccess(String result, String ERROR) {

                            if (ERROR.isEmpty()) {
                                Log.e("RateResp", result);
                                try {

                                    JSONObject jsonObject = new JSONObject(String.valueOf(result));
                                    int status = jsonObject.getInt("status");
                                    if (status == 200) {

                                        Rating = "0.0";

                                        BaseClass.dialog.dismiss();
                                        JSONObject jsonObject1 = jsonObject.getJSONObject("rating_detail");
                                        String Comment = jsonObject1.getString("Comment");
                                        String Rating = jsonObject1.getString("Rating");
                                        String RatingID = jsonObject1.getString("RatingID");
                                        String RatingType = jsonObject1.getString("RatingType");
                                        String Reason = jsonObject1.getString("Reason");
                                        String TransactionID = jsonObject1.getString("TransactionID");

//                                        notifyDataSetChanged();
                                        holder.linear_one.setVisibility(View.VISIBLE);
                                        holder.linear_two.setVisibility(View.GONE);
                                        holder.linear_three.setVisibility(View.GONE);

                                        commentsArray.get(position).setComment(Comment);
                                        commentsArray.get(position).setRating(Rating);
                                        commentsArray.get(position).setRatingID(RatingID);
                                        commentsArray.get(position).setRatingType(RatingType);
                                        commentsArray.get(position).setReason(Reason);
                                        commentsArray.get(position).setTransactionID(TransactionID);
                                        notifyDataSetChanged();


                                    } else {
                                        BaseClass.dialog.dismiss();
                                        String message = jsonObject.getString("message");
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                        Rating = "0.0";
                                    }
                                } catch (JSONException e) {
                                    BaseClass.dialog.dismiss();
                                    Rating = "0.0";
                                    e.printStackTrace();
                                }
                            } else {
                                BaseClass.dialog.dismiss();
                                Toast.makeText(context, ERROR, Toast.LENGTH_SHORT).show();
                                Rating = "0.0";
                            }
                        }
                    });
                }
            }
        });

        holder.expand3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.linear_one.setVisibility(View.VISIBLE);
                holder.linear_two.setVisibility(View.GONE);
                holder.linear_three.setVisibility(View.GONE);
            }
        });

        holder.ratingBar2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Rating = String.valueOf(rating);
                Log.e("Ratings", Rating);
            }
        });

        Picasso.get().load(Constants.URL.IMG_URL + commentsArray.get(position).getStoreImage()).into(holder.img1);
        Picasso.get().load(Constants.URL.IMG_URL + commentsArray.get(position).getStoreImage()).into(holder.img2);
        Picasso.get().load(Constants.URL.IMG_URL + commentsArray.get(position).getStoreImage()).into(holder.img3);

        holder.title1.setText(commentsArray.get(position).getTitle());
        holder.title2.setText(commentsArray.get(position).getTitle());
        holder.title3.setText(commentsArray.get(position).getTitle());

        holder.timeAndDate1.setText(BaseClass.TimeStampToTime(commentsArray.get(position).getCreatedAt()) + " | " + BaseClass.TimeStampToDate(commentsArray.get(position).getCreatedAt()));
        holder.timeAndDate2.setText(BaseClass.TimeStampToTime(commentsArray.get(position).getCreatedAt()) + " | " + BaseClass.TimeStampToDate(commentsArray.get(position).getCreatedAt()));
        holder.timeAndDate3.setText(BaseClass.TimeStampToTime(commentsArray.get(position).getCreatedAt()) + " | " + BaseClass.TimeStampToDate(commentsArray.get(position).getCreatedAt()));

        holder.totalBill1.setText(context.getResources().getString(R.string.totalBill) + " : " + commentsArray.get(position).getBillAfter() + " SAR");
        holder.totalBill2.setText(commentsArray.get(position).getBillAfter() + " SAR");
        holder.totalBill3.setText(commentsArray.get(position).getBillAfter() + " SAR");

        holder.totalSavings2.setText(commentsArray.get(position).getDiscount() + " SAR");
        holder.totalSavings3.setText(commentsArray.get(position).getDiscount() + " SAR");

        if (commentsArray.get(position).getComment() != "null") {
            holder.commentText.setText(commentsArray.get(position).getComment());
        }

        ArrayList<String> imageViewerArray = new ArrayList<>();
        imageViewerArray.add(Constants.URL.IMG_URL + commentsArray.get(position).getReceipt());
        holder.viewReceipt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fresco.initialize(context);
                new ImageViewer.Builder(context, imageViewerArray)
                        .show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return commentsArray.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linear_one, linear_two, linear_three, main_linear, viewReceipt2;
        ImageView img1, img2, img3, expand1, expand2, expand3;
        TextView title1, title2, title3, timeAndDate1, timeAndDate2, timeAndDate3, totalBill1, totalBill2, totalBill3, totalSavings2, totalSavings3, commentText;
        RatingBar ratingBar2, ratingBar3;
        EditText commentEdit;

        MyViewHolder(View itemView) {
            super(itemView);
            main_linear = itemView.findViewById(R.id.main_linear);
            linear_one = itemView.findViewById(R.id.linear_one);
            linear_two = itemView.findViewById(R.id.linear_two);
            linear_three = itemView.findViewById(R.id.linear_three);
            viewReceipt2 = itemView.findViewById(R.id.viewReceipt2);

            img1 = itemView.findViewById(R.id.img1);
            img2 = itemView.findViewById(R.id.img2);
            img3 = itemView.findViewById(R.id.img3);
            expand1 = itemView.findViewById(R.id.expand1);
            expand2 = itemView.findViewById(R.id.expand2);
            expand3 = itemView.findViewById(R.id.expand3);

            title1 = itemView.findViewById(R.id.title1);
            title2 = itemView.findViewById(R.id.title2);
            title3 = itemView.findViewById(R.id.title3);
            timeAndDate1 = itemView.findViewById(R.id.timeAndDate1);
            timeAndDate2 = itemView.findViewById(R.id.timeAndDate2);
            timeAndDate3 = itemView.findViewById(R.id.timeAndDate3);
            totalBill1 = itemView.findViewById(R.id.totalBill1);
            totalBill2 = itemView.findViewById(R.id.totalBill2);
            totalBill3 = itemView.findViewById(R.id.totalBill3);
            totalSavings2 = itemView.findViewById(R.id.savings2);
            totalSavings3 = itemView.findViewById(R.id.savings3);
            commentText = itemView.findViewById(R.id.commentText);

            ratingBar2 = itemView.findViewById(R.id.ratingBar2);
            ratingBar3 = itemView.findViewById(R.id.ratingBar3);

            commentEdit = itemView.findViewById(R.id.commentEdit);
        }
    }

    public interface CustomItemClickListener {
        void onItemClick(View v, int position);
    }

    private void rate(String comment, Integer position) {

        String language = BaseClass.checkLanguageFunction((Activity) context);
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

//        BaseClass.showCustomLoader(MainActivity.this);
        Map<String, String> body = new HashMap<String, String>();

        body.put("UserID", sharedPreferences.getString("UserID", " "));
        body.put("Rating", Rating);
        body.put("RatingType", "Rating");
        body.put("TransactionID", commentsArray.get(position).getTransactionID());
        body.put("Comment", comment);
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
        body.put("Language", language);

        Log.e("body", "");

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.saveRating, context, body, headers, new ServerCallback() {
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

//                                Dialog regsuccess = new Dialog(context, android.R.style.ThemeOverlay_Material_Dialog_Alert);
//                                regsuccess.setCancelable(false);
//                                regsuccess.setContentView(R.layout.redeem_successfull_dialog);
//                                regsuccess.show();
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        regsuccess.dismiss();
//                                    }
//                                }, 1000);

                        } else {
                            BaseClass.dialog.dismiss();
                            String message = jsonObject.getString("message");
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(context, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

