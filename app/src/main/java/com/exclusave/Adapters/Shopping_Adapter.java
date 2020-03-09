package com.exclusave.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.models.ShoppingActivity_Data;
import com.exclusave.BuildConfig;
import com.exclusave.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

public class Shopping_Adapter extends RecyclerView.Adapter<Shopping_Adapter.MyViewHolder> {

    private Context context;
    Shopping_Adapter.CustomItemClickListener listener;
    ArrayList<ShoppingActivity_Data> commentsArray;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Drawable drawable;


    public Shopping_Adapter(Context context, ArrayList<ShoppingActivity_Data> commentsArray, Shopping_Adapter.CustomItemClickListener listener) {
        this.commentsArray = commentsArray;
        this.context = context;
        this.listener = listener;
        sharedPreferences = BaseClass.sharedPreferances((Activity) context);
        editor = BaseClass.sharedPreferancesEditor((Activity) context);
    }

    @Override
    public Shopping_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view;
        LayoutInflater inflater = LayoutInflater.from(context);

        view = inflater.inflate(R.layout.shopping_withoffer_adapter, parent, false);
        final Shopping_Adapter.MyViewHolder viewHolder = new Shopping_Adapter.MyViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Shopping_Adapter.MyViewHolder holder, int position) {

        drawable = context.getResources().getDrawable(R.drawable.ic_heart_new_black);
        drawable.setTint(Color.parseColor(sharedPreferences.getString("FontColor", "")));
        holder.favBtn.setLikeDrawable(drawable);
        holder.favBtn.setUnlikeDrawable(context.getResources().getDrawable(R.drawable.ic_heart_new));
//        holder.nametitle.setSelected(true);
//        String percent = commentsArray.get(position).getDiscount();
//        String off = commentsArray.get(position).getDiscountType();
//        SpannableString span1 = new SpannableString(percent);
//        span1.setSpan(new AbsoluteSizeSpan(30), 0, percent.length(), SPAN_INCLUSIVE_INCLUSIVE);
//        SpannableString span2 = new SpannableString(off);
//        span2.setSpan(new AbsoluteSizeSpan(23), 0, off.length(), SPAN_INCLUSIVE_INCLUSIVE);
//        CharSequence finalText = TextUtils.concat(span1, "", span2);
        holder.offerprice.setText(commentsArray.get(position).getDiscount() + commentsArray.get(position).getDiscountType());

        holder.distance.setText(commentsArray.get(position).getDistance() + " km");
        holder.title.setText(commentsArray.get(position).getTitle());
        holder.item.setText(commentsArray.get(position).getCategoryTitle());
//        holder.nametitle.setText(commentsArray.get(position).getFirstName()+" "+commentsArray.get(position).getMiddleName()+" "+commentsArray.get(position).getLastName());
        Picasso.get().load(Constants.URL.IMG_URL + commentsArray.get(position).getImage()).placeholder(R.drawable.blur_icon).into(holder.imageView);

        if(commentsArray.get(position).getIsAddedToFavourite() == 1){
            holder.favBtn.setLiked(true);
        }else if(commentsArray.get(position).getIsAddedToFavourite() == 0){
            holder.favBtn.setLiked(false);
        }

        holder.favBtn.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
//                addToFav(position);

                String language = BaseClass.checkLanguageFunction((Activity) context);
                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();

                Map<String,String> body = new HashMap<String, String>();
                body.put("UserID", sharedPreferences.getString("UserID",""));
                body.put("StoreID", commentsArray.get(position).getStoreID());
                body.put("Type", "add");
                body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
//        body.put("Language",language);

                HashMap<String,String> header = new HashMap<String, String>();
                header.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));


                ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.addStoreToFavourite, context, body, header, new ServerCallback() {
                    @Override
                    public void onSuccess(String result, String ERROR) {

                        if (ERROR.isEmpty()){
                            Log.e("AddToFavResp", result + " ");
                            try {
                                JSONObject jsonObject = new JSONObject(String.valueOf(result));
                                String message = jsonObject.getString("message");
                                int status = jsonObject.getInt("status");
                                if (status == 200){
                                    holder.favBtn.setLiked(true);
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//                            notifyDataSetChanged();
                                }else {
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(context, ERROR, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void unLiked(LikeButton likeButton) {
//                removeFromFav(position);

                String language = BaseClass.checkLanguageFunction((Activity) context);
                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();

                Map<String,String> body = new HashMap<String, String>();
                body.put("UserID", sharedPreferences.getString("UserID",""));
                body.put("StoreID", commentsArray.get(position).getStoreID());
                body.put("Type", "remove");
                body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
//        body.put("Language",language);

                HashMap<String,String> header = new HashMap<String, String>();
                header.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));


                ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.addStoreToFavourite, context, body, header, new ServerCallback() {
                    @Override
                    public void onSuccess(String result, String ERROR) {

                        if (ERROR.isEmpty()){
                            Log.e("RemoveFromFavResp", result + " ");
                            try {
                                JSONObject jsonObject = new JSONObject(String.valueOf(result));
                                String message = jsonObject.getString("message");
                                int status = jsonObject.getInt("status");
                                if (status == 200){
                                    holder.favBtn.setLiked(false);
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(context, ERROR, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        holder.ratingBar.setRating(Float.parseFloat(commentsArray.get(position).getStoreAverageRating()));
    }

    @Override
    public int getItemCount() {
        return commentsArray.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView distance,title,nametitle,item,offerprice;
        ImageView imageView;
        RatingBar ratingBar;
        LikeButton favBtn;

//        RelativeLayout relativeLayout;
//        RoundedImageView category_img;

        MyViewHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            offerprice = itemView.findViewById(R.id.offerprice);
  //          nametitle = itemView.findViewById(R.id.nametitle);
            title = itemView.findViewById(R.id.title);
            distance = itemView.findViewById(R.id.distance);
            imageView = itemView.findViewById(R.id.image);
            favBtn = itemView.findViewById(R.id.favBtn);
            ratingBar = itemView.findViewById(R.id.ratingBar);
//            category_name = itemView.findViewById(R.id.tv_cattitle_cla);
//            catsub_item = itemView.findViewById(R.id.tv_catsubitem_cla);
//            category_img = itemView.findViewById(R.id.iv_imgcat_cla);
        }
    }

    public interface CustomItemClickListener {
        void onItemClick(View v, int position);
    }

//    private void addToFav(Integer poistion) {
//
//        String language = BaseClass.checkLanguageFunction((Activity) context);
//        Long tsLong = System.currentTimeMillis()/1000;
//        String ts = tsLong.toString();
//
//        Map<String,String> body = new HashMap<String, String>();
//        body.put("UserID", sharedPreferences.getString("UserID",""));
//        body.put("StoreID", commentsArray.get(poistion).getStoreID());
//        body.put("Type", "add");
//        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
////        body.put("Language",language);
//
//        HashMap<String,String> header = new HashMap<String, String>();
//        header.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));
//
//
//        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.addStoreToFavourite, context, body, header, new ServerCallback() {
//            @Override
//            public void onSuccess(String result, String ERROR) {
//
//                if (ERROR.isEmpty()){
//                    Log.e("AddToFavResp", result + " ");
//                    try {
//                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
//                        String message = jsonObject.getString("message");
//                        int status = jsonObject.getInt("status");
//                        if (status == 200){
//                            favBtn.setLiked(true);
//                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
////                            notifyDataSetChanged();
//                        }else {
//                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }else {
//                    Toast.makeText(context, ERROR, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    private void removeFromFav(Integer poistion) {
//        String language = BaseClass.checkLanguageFunction((Activity) context);
//        Long tsLong = System.currentTimeMillis()/1000;
//        String ts = tsLong.toString();
//
//        Map<String,String> body = new HashMap<String, String>();
//        body.put("UserID", sharedPreferences.getString("UserID",""));
//        body.put("StoreID", commentsArray.get(poistion).getStoreID());
//        body.put("Type", "remove");
//        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
////        body.put("Language",language);
//
//        HashMap<String,String> header = new HashMap<String, String>();
//        header.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));
//
//
//        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.addStoreToFavourite, context, body, header, new ServerCallback() {
//            @Override
//            public void onSuccess(String result, String ERROR) {
//
//                if (ERROR.isEmpty()){
//                    Log.e("RemoveFromFavResp", result + " ");
//                    try {
//                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
//                        String message = jsonObject.getString("message");
//                        int status = jsonObject.getInt("status");
//                        if (status == 200){
//                            favBtn.setLiked(false);
//                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//                        }else {
//                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }else {
//                    Toast.makeText(context, ERROR, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
}
