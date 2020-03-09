package com.exclusave.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.exclusave.Adapters.Branches_Fragment_SectionpageAdapter;
import com.exclusave.BaseClass;
import com.exclusave.Fragments.Branches_List_Fragment;
import com.exclusave.Fragments.Branches_Map_Fragment;
import com.exclusave.R;

public class Branches_Activity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private static final String  TAG = "MainListActivity";
    Branches_Fragment_SectionpageAdapter adapter;
    BottomNavigationView bottomNavigationView;
    ImageView header_comment,header_cart,header_more;

    private Branches_Fragment_SectionpageAdapter sectionsPageAdapter;
    private ViewPager viewPager;
    TabLayout tabLayout;
    TextView storeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branches_);

        sharedPreferences = BaseClass.sharedPreferances(Branches_Activity.this);
        editor = BaseClass.sharedPreferancesEditor(Branches_Activity.this);

        editor.putString("MapType", "Branches").commit();
        initilizeviews();
        sectionsPageAdapter = new Branches_Fragment_SectionpageAdapter(getSupportFragmentManager());

        storeName = findViewById(R.id.storeName);
        storeName.setText(sharedPreferences.getString("StoreName", ""));

        viewPager = findViewById(R.id.contain);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        if(sharedPreferences.getString("isLoggedIn", "").equals("1")) {
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor(sharedPreferences.getString("FontColor", "")));
            tabLayout.setTabTextColors(getResources().getColor(R.color.white), getResources().getColor(R.color.white));
        }

        bottomNavigationView = findViewById(R.id.navigation);


//        setRetainInstance(true);

    }

    private void initilizeviews() {

    }

    private void setupViewPager(ViewPager viewPager){
        adapter = new Branches_Fragment_SectionpageAdapter(getSupportFragmentManager());


        adapter.addFragment(new Branches_Map_Fragment(),getResources().getString(R.string.mapView));
        adapter.addFragment(new Branches_List_Fragment(),getResources().getString(R.string.listView));

        viewPager.setAdapter(adapter);
        int limit = (adapter.getCount() > 1 ? adapter.getCount() - 1 : 1);
//        viewPager.setOffscreenPageLimit(limit);         /* limit is a fixed integer*/
    }
}
