package com.exclusave.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;

import com.exclusave.Activities.Details_Activity;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.BaseClass;
import com.exclusave.models.StoresPagerData;
import com.exclusave.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StudFragPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    public static final String MyPREFERENCES = "MyPrefs";
    ArrayList<StoresPagerData> storesPagerData;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    public StudFragPagerAdapter(Context context, ArrayList<StoresPagerData> storesPagerData) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        this.storesPagerData = storesPagerData;
        sharedPreferences = BaseClass.sharedPreferances((Activity) context);
        editor = BaseClass.sharedPreferancesEditor((Activity) context);

    }

    @Override
    public int getCount() {
        return storesPagerData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        sharedPreferences = BaseClass.sharedPreferances((Activity) context);
        editor = BaseClass.sharedPreferancesEditor((Activity) context);
        View view = this.layoutInflater.inflate(R.layout.stud_frag_pageritem, container, false);

        ImageView background, img;
        TextView title;

        background = view.findViewById(R.id.background);
        img = view.findViewById(R.id.img);
        title = view.findViewById(R.id.title);

        if(sharedPreferences.getString("isLoggedIn", "").equals("0") && storesPagerData.get(position).getCorporate().equals("yes")){
            Picasso.get().load(Constants.URL.IMG_URL + storesPagerData.get(position).getGoldImage()).placeholder(R.drawable.blur_icon).into(img);
        }else {
            Picasso.get().load(Constants.URL.IMG_URL + storesPagerData.get(position).getCategoryImage()).placeholder(R.drawable.blur_icon).into(img);
        }

        Picasso.get().load(Constants.URL.IMG_URL + storesPagerData.get(position).getCoverImage()).placeholder(R.drawable.black_blur_icon).into(background);
 //       Picasso.get().load(Constants.URL.IMG_URL + storesPagerData.get(position).getCategoryImage()).placeholder(R.drawable.black_blur_icon).into(img);
        title.setText(storesPagerData.get(position).getTitle());

        container.addView(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sharedPreferences.getString("isLoggedIn", "").equals("1")){
                    editor.putString("StoreID",storesPagerData.get(position).getStoreID()).commit();
                    editor.putString("ReceiverID",storesPagerData.get(position).getUserID()).commit();
                    Intent intent = new Intent(context, Details_Activity.class);
                    intent.putExtra("Title",storesPagerData.get(position).getTitle());
                    context.startActivity(intent);
                  //  context.startActivity(new Intent(context, Details_Activity.class));
                }else if(sharedPreferences.getString("isLoggedIn", "").equals("0")){
                    Toast.makeText(context, context.getResources().getString(R.string.PleaseLogin), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}