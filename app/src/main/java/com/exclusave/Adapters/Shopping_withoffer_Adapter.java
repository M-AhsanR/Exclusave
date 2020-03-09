package com.exclusave.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.exclusave.ApiStructure.Constants;
import com.exclusave.BaseClass;
import com.exclusave.models.ShoppingActivity_Data;
import com.exclusave.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Shopping_withoffer_Adapter extends RecyclerView.Adapter<Shopping_withoffer_Adapter.MyViewHolder> {

    private Context context;
    Shopping_withoffer_Adapter.CustomItemClickListener listener;
    ArrayList<ShoppingActivity_Data> shoppingActivityData;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public Shopping_withoffer_Adapter(Context context, ArrayList<ShoppingActivity_Data> shoppingActivityData, Shopping_withoffer_Adapter.CustomItemClickListener listener) {
        this.shoppingActivityData = shoppingActivityData;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public Shopping_withoffer_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view;
        LayoutInflater inflater = LayoutInflater.from(context);

        view = inflater.inflate(R.layout.shopping_adapter, parent, false);
        final Shopping_withoffer_Adapter.MyViewHolder viewHolder = new Shopping_withoffer_Adapter.MyViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final Shopping_withoffer_Adapter.MyViewHolder holder, int position) {
        sharedPreferences = BaseClass.sharedPreferances((Activity) context);
        editor = BaseClass.sharedPreferancesEditor((Activity) context);
        if(sharedPreferences.getString("isLoggedIn", "").equals("0") && shoppingActivityData.get(position).getCorporate().equals("yes")){
            Picasso.get().load(Constants.URL.IMG_URL + shoppingActivityData.get(position).getGoldImage()).into(holder.categoryImg);
        }else {
            Picasso.get().load(Constants.URL.IMG_URL + shoppingActivityData.get(position).getCategoryImage()).into(holder.categoryImg);
        }

       holder.distance.setText(shoppingActivityData.get(position).getDistance() + " km");
       holder.title.setText(shoppingActivityData.get(position).getFirstName() + " " + shoppingActivityData.get(position).getMiddleName() + " " + shoppingActivityData.get(position).getLastName());
       Picasso.get().load(Constants.URL.IMG_URL + shoppingActivityData.get(position).getImage()).into(holder.merchantImg);
      // Picasso.get().load(Constants.URL.IMG_URL + shoppingActivityData.get(position).getCategoryImage()).into(holder.categoryImg);

       holder.ratingBar.setRating(Float.parseFloat(shoppingActivityData.get(position).getStoreAverageRating()));

    }

    @Override
    public int getItemCount() {
        return shoppingActivityData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView merchantImg, categoryImg;
        TextView distance, title;
        RatingBar ratingBar;

        MyViewHolder(View itemView) {
            super(itemView);
            merchantImg = itemView.findViewById(R.id.merchantImg);
            categoryImg = itemView.findViewById(R.id.categoryImg);
            distance = itemView.findViewById(R.id.distance);
            title = itemView.findViewById(R.id.title);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }

    public interface CustomItemClickListener {
        void onItemClick(View v, int position);
    }
}

