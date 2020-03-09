package com.exclusave.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.models.OfferDetailImagesData;
import com.exclusave.R;
import com.squareup.picasso.Picasso;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;

public class OfferDetailsImagesAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    public static final String MyPREFERENCES = "MyPrefs";
    ArrayList<OfferDetailImagesData> storeOffersData;
    ArrayList<String> imageViewerArray = new ArrayList<>();


    public OfferDetailsImagesAdapter(Context context, ArrayList<OfferDetailImagesData> storeOffersData) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        this.storeOffersData = storeOffersData;
    }

    @Override
    public int getCount() {
        return storeOffersData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        View view = this.layoutInflater.inflate(R.layout.stud_frag_pageritem, container, false);

        LinearLayout bottomLayout;
        ImageView img;

        bottomLayout = view.findViewById(R.id.bottomLayout);
        img = view.findViewById(R.id.background);

        bottomLayout.setVisibility(View.GONE);
        if(storeOffersData.size()>0){
            Picasso.get().load(Constants.URL.IMG_URL + storeOffersData.get(position).getImg()).into(img);
           // Log.e("Img", Constants.URL.IMG_URL + storeOffersData.get(position).getOfferDetailImagesData().get(position).getImg());
            imageViewerArray.add(Constants.URL.IMG_URL + storeOffersData.get(position).getImg());
        }

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fresco.initialize(context);
                new ImageViewer.Builder(context, imageViewerArray)
                        .show();
            }
        });


        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}