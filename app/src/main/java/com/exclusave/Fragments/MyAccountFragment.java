package com.exclusave.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.exclusave.Activities.HistoryActivity;
import com.exclusave.Activities.MyProfileActivity;
import com.exclusave.Activities.RewardsActivity;
import com.exclusave.BaseClass;
import com.exclusave.R;


public class MyAccountFragment extends Fragment implements View.OnClickListener {

    TextView profile, history, reward, Name, points, savings;
    LinearLayout bottomLinear;
    RoundCornerProgressBar roundCornerProgressBar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor mEditor;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public static MyAccountFragment newInstance(String param1, String param2) {
        MyAccountFragment fragment = new MyAccountFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = BaseClass.sharedPreferances(getActivity());
        mEditor = BaseClass.sharedPreferancesEditor(getActivity());

        initializeViews();
        initializeClickListeners();
        setDimentions();

        if(sharedPreferences.getString("IsFamilyMember", "").equals("1")){
            Name.setText(sharedPreferences.getString("FamilyMemberFirstName", "") + " " + sharedPreferences.getString("FamilyMemberMiddleName", "") + " " + sharedPreferences.getString("FamilyMemberLastName", ""));
        }else {
            Name.setText(sharedPreferences.getString("FullName", ""));
        }

        savings.setText(sharedPreferences.getString("TotalSaving", ""));
    }

    private void initializeClickListeners() {
        profile.setOnClickListener(this);
        reward.setOnClickListener(this);
    }

    private void setDimentions() {
        profile.setMinimumHeight(profile.getMaxWidth());
        history.setMinimumHeight(history.getMaxWidth());
        reward.setMinimumHeight(reward.getMaxWidth());
        bottomLinear.setMinimumHeight(profile.getMaxWidth());

        Log.e("width", profile.getMinimumWidth() + "  " + profile.getMeasuredWidth() + "  " + profile.getMaxWidth() + " " + profile.getMinWidth());
    }

    private void initializeViews() {
        profile = getView().findViewById(R.id.profile);
        history = getView().findViewById(R.id.history);
        reward = getView().findViewById(R.id.reward);
        bottomLinear = getView().findViewById(R.id.bottomLinear);
        Name = getView().findViewById(R.id.Name);
        points = getView().findViewById(R.id.points);
        savings = getView().findViewById(R.id.savings);
        roundCornerProgressBar = getView().findViewById(R.id.progressBar);

        if(sharedPreferences.getString("isLoggedIn", "").equals("1")){
            points.setBackgroundDrawable( new BaseClass.DrawableGradientForAccount(new int[] {Color.parseColor(sharedPreferences.getString("PackageColorUpper","")), Color.parseColor(sharedPreferences.getString("PackageColorLower","")), Color.parseColor(sharedPreferences.getString("PackageColorLower",""))}, 0));
            savings.setBackgroundDrawable( new BaseClass.DrawableGradientForAccount(new int[] {Color.parseColor(sharedPreferences.getString("PackageColorUpper","")), Color.parseColor(sharedPreferences.getString("PackageColorLower","")), Color.parseColor(sharedPreferences.getString("PackageColorLower",""))}, 0));
            roundCornerProgressBar.setProgressColor(Color.parseColor(sharedPreferences.getString("FontColor", "")));
        }

        history.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        if(v == profile){
            startActivity(new Intent(getActivity(), MyProfileActivity.class));
        }else if (v == history){
            startActivity(new Intent(getActivity(), HistoryActivity.class));
        }else if(v == reward){
//            startActivity(new Intent(getActivity(), RewardsActivity.class));
            Toast.makeText(getActivity(), getResources().getString(R.string.comming_soon), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            if(sharedPreferences.getString("IsFamilyMember", "").equals("1")){
                Name.setText(sharedPreferences.getString("FamilyMemberFirstName", "") + " " + sharedPreferences.getString("FamilyMemberMiddleName", "") + " " + sharedPreferences.getString("FamilyMemberLastName", ""));
            }else {
                Name.setText(sharedPreferences.getString("FullName", ""));
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser == true){
            savings.setText(sharedPreferences.getString("TotalSaving", ""));
            if(sharedPreferences.getString("IsFamilyMember", "").equals("1")){
                Name.setText(sharedPreferences.getString("FamilyMemberFirstName", "") + " " + sharedPreferences.getString("FamilyMemberMiddleName", "") + " " + sharedPreferences.getString("FamilyMemberLastName", ""));
            }else {
                Name.setText(sharedPreferences.getString("FullName", ""));
            }
        }else if (isVisibleToUser == false && getActivity() != null){

        }
    }
}
