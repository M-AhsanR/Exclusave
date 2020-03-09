package com.exclusave.Fragments;

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
import com.exclusave.Adapters.Branches_Fragment_SectionpageAdapter;
import com.exclusave.BaseClass;
import com.exclusave.R;

public class Branches_Fragment  extends Fragment implements View.OnClickListener {

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

    public static Branches_Fragment newInstance(String param1, String param2) {
        Branches_Fragment fragment = new Branches_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_branches,container,false);

        return view;
    }

    private void setupViewPager(ViewPager viewPager){
        adapter = new Branches_Fragment_SectionpageAdapter(getChildFragmentManager());

        adapter.addFragment(new Branches_Map_Fragment(),getActivity().getResources().getString(R.string.mapView));
        adapter.addFragment(new Branches_List_Fragment(),getActivity().getString(R.string.listView));

        viewPager.setAdapter(adapter);
        int limit = (adapter.getCount() > 1 ? adapter.getCount() - 1 : 1);
//        viewPager.setOffscreenPageLimit(limit);         /* limit is a fixed integer*/
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initilizeviews(view);
        sectionsPageAdapter = new Branches_Fragment_SectionpageAdapter(getChildFragmentManager());

        viewPager = view.findViewById(R.id.contain);
        setupViewPager(viewPager);

        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        if(sharedPreferences.getString("isLoggedIn", "").equals("1")) {
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor(sharedPreferences.getString("FontColor", "")));
            tabLayout.setTabTextColors(getActivity().getResources().getColor(R.color.white), getResources().getColor(R.color.white));
        }

        bottomNavigationView = getActivity().findViewById(R.id.navigation);
//        setRetainInstance(true);


    }

    private void initilizeviews(View view) {
        sharedPreferences = BaseClass.sharedPreferances(getActivity());
        editor = BaseClass.sharedPreferancesEditor(getActivity());
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            editor.putString("MapType", "Stores").commit();
        }
    }

}
