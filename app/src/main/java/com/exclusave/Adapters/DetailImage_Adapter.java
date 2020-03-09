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
import com.exclusave.models.StoreDetails_Data;
import com.exclusave.R;
import com.squareup.picasso.Picasso;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;

public class DetailImage_Adapter extends PagerAdapter implements View.OnClickListener {
    private Context context;
    private LayoutInflater layoutInflater;
    public static final String MyPREFERENCES = "MyPrefs";
    ArrayList<StoreDetails_Data> storeOffersData;
    LinearLayout bottomLayout;
    ImageView img;
    ArrayList<String> imageViewerArray = new ArrayList<>();

    public DetailImage_Adapter(Context context, ArrayList<StoreDetails_Data> storeOffersData) {
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

        initilizeViews(view);

        bottomLayout.setVisibility(View.GONE);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewerArray.add(Constants.URL.IMG_URL + storeOffersData.get(position).getCoverImage());
                Fresco.initialize(context);
                new ImageViewer.Builder(context, imageViewerArray)
//                        .setStartPosition(imageViewerArray.get(0))
                        .show();
            }
        });

        Picasso.get().load(Constants.URL.IMG_URL + storeOffersData.get(position).getCoverImage()).into(img);

        container.addView(view);
        return view;
    }

    private void initilizeViews(View view) {
        bottomLayout = view.findViewById(R.id.bottomLayout);
        img = view.findViewById(R.id.background);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void onClick(View view) {

    }
}
