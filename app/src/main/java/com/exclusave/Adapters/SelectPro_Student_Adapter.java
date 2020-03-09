package com.exclusave.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.models.InterestsData;
import com.exclusave.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SelectPro_Student_Adapter extends RecyclerView.Adapter<SelectPro_Student_Adapter.MyViewHolder> {

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor mEditor;
    public static final String MyPREFERENCES = "MyPrefs";
    private Context context;
    CustomItemClickListener listener;
    ArrayList<InterestsData> categoriesData;

    public SelectPro_Student_Adapter(Context context, ArrayList<InterestsData> categoriesData, CustomItemClickListener listener) {
        this.categoriesData = categoriesData;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view;
        LayoutInflater inflater = LayoutInflater.from(context);

        view = inflater.inflate(R.layout.selectpro_student_adapter, parent, false);
        final SelectPro_Student_Adapter.MyViewHolder viewHolder = new SelectPro_Student_Adapter.MyViewHolder(view);

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
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mEditor = sharedpreferences.edit();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels/3;
        holder.img.setMinimumWidth(width);
        holder.img.setMaxHeight(width);
        holder.img.setMaxWidth(width);
        holder.img.setMinimumHeight(width);
        holder.mainRelative.setMinimumHeight(width);
        holder.mainRelative.setMinimumWidth(width);

        holder.title.setText(categoriesData.get(position).getTitle());
        if(sharedpreferences.getString("isLoggedIn", "").equals("0") && categoriesData.get(position).getCorporate().equals("yes")){
            Picasso.get().load(Constants.URL.IMG_URL + categoriesData.get(position).getGoldImage()).placeholder(R.drawable.blur_icon).into(holder.img);
        }else {
            Picasso.get().load(Constants.URL.IMG_URL + categoriesData.get(position).getImage()).placeholder(R.drawable.blur_icon).into(holder.img);
        }


        Picasso.get().load(Constants.URL.IMG_URL + categoriesData.get(position).getBackgroundImage()).placeholder(R.color.orange).into(holder.background);
        //        MainCatData lisadapter = categoriesListModels.get(position);
//        holder.category_name.setText(lisadapter.getItemTitle());
//        holder.catsub_item.setText(lisadapter.getSubTitle());
    }

    @Override
    public int getItemCount() {
        return categoriesData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout mainRelative;
        RoundedImageView background;
        ImageView img;
        TextView title;

        MyViewHolder(View itemView) {
            super(itemView);
            mainRelative = itemView.findViewById(R.id.mainRelative);
            background = itemView.findViewById(R.id.background);
            img = itemView.findViewById(R.id.img);
            title = itemView.findViewById(R.id.title);
        }
    }

    public interface CustomItemClickListener {
        void onItemClick(View v, int position);
    }

}
