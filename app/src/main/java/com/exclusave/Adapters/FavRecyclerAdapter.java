package com.exclusave.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.models.FavStoresData;
import com.exclusave.BuildConfig;
import com.exclusave.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FavRecyclerAdapter extends RecyclerView.Adapter<FavRecyclerAdapter.MyViewHolder> {

    private Context context;
    CustomItemClickListener listener;
    ArrayList<FavStoresData> favStoresData;
    private int lastPosition = -1;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Drawable drawable;


    public FavRecyclerAdapter(Context context, ArrayList<FavStoresData> favStoresData, CustomItemClickListener listener) {
        this.favStoresData = favStoresData;
        this.context = context;
        this.listener = listener;
        sharedPreferences = BaseClass.sharedPreferances((Activity) context);
        editor = BaseClass.sharedPreferancesEditor((Activity) context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view;
        LayoutInflater inflater = LayoutInflater.from(context);

        view = inflater.inflate(R.layout.whats_new_item, parent, false);
        final FavRecyclerAdapter.MyViewHolder viewHolder = new FavRecyclerAdapter.MyViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        setAnimation(holder.itemView, position);

        drawable = context.getResources().getDrawable(R.drawable.ic_heart_new_black);
        drawable.setTint(Color.parseColor(sharedPreferences.getString("FontColor", "")));
        holder.favBtn.setLikeDrawable(drawable);
        holder.favBtn.setUnlikeDrawable(context.getResources().getDrawable(R.drawable.ic_heart_new));

        holder.whatsnew_title.setText(favStoresData.get(position).getTitle());
        Picasso.get().load(Constants.URL.IMG_URL + favStoresData.get(position).getCoverImage()).into(holder.roundedImageView);
        Picasso.get().load(Constants.URL.IMG_URL + favStoresData.get(position).getCategoryImage()).into(holder.whatsnew_icon);

//        favBtn.set

            holder.favBtn.setLiked(true);
            holder.favBtn.setEnabled(false);

        holder.favBtn.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
//                addToFav(position);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
//                removeFromFav(position);
            }
        });

    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return favStoresData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView roundedImageView;
        TextView whatsnew_title;
        ImageView whatsnew_icon;
        LikeButton favBtn;

        MyViewHolder(View itemView) {
            super(itemView);
            roundedImageView = itemView.findViewById(R.id.whatsnewbackgroundcover);
            whatsnew_title = itemView.findViewById(R.id.whatsnewtitle);
            whatsnew_icon = itemView.findViewById(R.id.whatsnewicon);
            favBtn = itemView.findViewById(R.id.favBtn);

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
//        body.put("StoreID", favStoresData.get(poistion).getStoreID());
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
//
//        String language = BaseClass.checkLanguageFunction((Activity) context);
//        Long tsLong = System.currentTimeMillis()/1000;
//        String ts = tsLong.toString();
//
//        Map<String,String> body = new HashMap<String, String>();
//        body.put("UserID", sharedPreferences.getString("UserID",""));
//        body.put("StoreID", favStoresData.get(poistion).getStoreID());
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
