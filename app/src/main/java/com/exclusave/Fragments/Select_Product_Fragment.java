package com.exclusave.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.exclusave.Adapters.SelectPro_SectionsPageAdapter;
import com.exclusave.R;

public class Select_Product_Fragment extends Fragment implements  View.OnClickListener {

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor mEditor;
    public static final String MyPREFERENCES = "MyPrefs";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private static final String  TAG = "MainListActivity";
    SelectPro_SectionsPageAdapter adapter;
    BottomNavigationView bottomNavigationView;
    ImageView header_comment,header_cart,header_more;

    private SelectPro_SectionsPageAdapter sectionsPageAdapter;
    private ViewPager viewPager;
    TabLayout tabLayout;

    private View root;

    public static Select_Product_Fragment newInstance(String param1, String param2) {
        Select_Product_Fragment fragment = new Select_Product_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root == null){
            root = inflater.inflate(R.layout.fragment_select_product, container, false);
        }
        return root;
    }

    private void setupViewPager(ViewPager viewPager){
        adapter = new SelectPro_SectionsPageAdapter(getChildFragmentManager());

        if(sharedpreferences.getString("isLoggedIn", "").equals("1")){
            adapter.addFragment(new WhatsNewFragment(),getString(R.string.whatsNew));
            adapter.addFragment(new SelectPro_Student_Fragment(),getString(R.string.offers));
            tabLayout.setTabTextColors(getActivity().getResources().getColor(R.color.white), getResources().getColor(R.color.white));
        }else if(sharedpreferences.getString("isLoggedIn", "").equals("0")){
            adapter.addFragment(new SelectPro_Student_Fragment(),getString(R.string.Student));
            adapter.addFragment(new SelectPro_Corporate_Fragment(),getString(R.string.corporate));
        }


        viewPager.setAdapter(adapter);
        int limit = (adapter.getCount() > 1 ? adapter.getCount() - 1 : 1);
//        viewPager.setOffscreenPageLimit(limit);         /* limit is a fixed integer*/
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mEditor = sharedpreferences.edit();

        initilizeviews(view);
        tabLayout = view.findViewById(R.id.tabs);
        sectionsPageAdapter = new SelectPro_SectionsPageAdapter(getChildFragmentManager());

        viewPager = view.findViewById(R.id.contain);
        setupViewPager(viewPager);

        if(sharedpreferences.getString("isLoggedIn", "").equals("1")) {
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor(sharedpreferences.getString("FontColor", "")));
        }else if(sharedpreferences.getString("isLoggedIn", "").equals("0")){
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if(tab.getPosition() == 0){
                        tabLayout.setTabTextColors(Color.parseColor("#8F6C38"), getActivity().getResources().getColor(R.color.orange));
                        tabLayout.setSelectedTabIndicatorColor(getActivity().getResources().getColor(R.color.orange));
                    }else if(tab.getPosition() == 1){
                        tabLayout.setTabTextColors(getActivity().getResources().getColor(R.color.orange), Color.parseColor("#8F6C38"));
                        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#8F6C38"));
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }

        tabLayout.setupWithViewPager(viewPager);
        bottomNavigationView = getActivity().findViewById(R.id.navigation);

        if(sharedpreferences.getString("isLoggedIn", "").equals("1")){
            viewPager.setCurrentItem(1);
        }
//        setRetainInstance(true);
    }

    private void initilizeviews(View view) {

    }


    @Override
    public void onClick(View view) {

    }
}
