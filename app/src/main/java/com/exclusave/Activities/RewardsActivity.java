package com.exclusave.Activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adroitandroid.chipcloud.ChipCloud;
import com.exclusave.Adapters.RewardsRecyclerAdapter;
import com.exclusave.R;

import java.util.ArrayList;

public class RewardsActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rewardsRecycler;

    ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        initializeViews();
        rewards();
    }

    private void rewards() {

        for (int i = 0; i < 7; i++) {
            list.add("");
        }
        rewardsRecycler.setLayoutManager(new LinearLayoutManager(RewardsActivity.this));
        RewardsRecyclerAdapter adapter = new RewardsRecyclerAdapter(RewardsActivity.this, list, new RewardsRecyclerAdapter.CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                final Dialog updatedialog = new Dialog(RewardsActivity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
                updatedialog.setContentView(R.layout.rewards_dialog);
                Button code = updatedialog.findViewById(R.id.code);
                final ImageView cancel = updatedialog.findViewById(R.id.cancel);
                updatedialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updatedialog.dismiss();
                    }
                });
                code.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        merchantCodeDialog();
                    }
                });
                updatedialog.show();
            }
        });
        rewardsRecycler.setAdapter(adapter);

    }

    private void initializeViews() {
        rewardsRecycler = findViewById(R.id.rewardsRecycler);
    }

    @Override
    public void onClick(View v) {

    }

    private void merchantCodeDialog(){
        final Dialog merchantCodeDialog = new Dialog(RewardsActivity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        merchantCodeDialog.setContentView(R.layout.merchant_code_dialog);
        Button applyCode = merchantCodeDialog.findViewById(R.id.applyCode);
        Button compain = merchantCodeDialog.findViewById(R.id.complain);
        merchantCodeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        merchantCodeDialog.show();
        applyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                merchantCodeDialog.dismiss();
                confirmBillDialog();
            }
        });
        compain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DynamicToast.makeSuccess(LoginActivity.this, "Mmm, working on it!", 3).show();
                merchantCodeDialog.dismiss();
                complaintDialog();
            }
        });
    }

    private void confirmBillDialog(){
        final Dialog confirmBillDialog = new Dialog(RewardsActivity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        confirmBillDialog.setContentView(R.layout.confirm_bill_dialog);
        Button submitBill = confirmBillDialog.findViewById(R.id.submitBill);
//        Button compain = merchantCodeDialog.findViewById(R.id.complain);
        confirmBillDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confirmBillDialog.show();
        submitBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmBillDialog.dismiss();
                rateYourExpeirenceDialog();
            }
        });
//        compain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DynamicToast.makeSuccess(LoginActivity.this, "Mmm, working on it!", 3).show();
//            }
//        });
    }

    private void rateYourExpeirenceDialog(){
        final Dialog rateYourExpeirenceDialog = new Dialog(RewardsActivity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        rateYourExpeirenceDialog.setContentView(R.layout.rate_experience_dialog);
        ChipCloud chipCloud = rateYourExpeirenceDialog.findViewById(R.id.chipcloud);
        Button ratingDone = rateYourExpeirenceDialog.findViewById(R.id.ratingDone);
        Button skipRating = rateYourExpeirenceDialog.findViewById(R.id.skipRating);
        Button complain = rateYourExpeirenceDialog.findViewById(R.id.complain);
        rateYourExpeirenceDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        rateYourExpeirenceDialog.show();

        String list[]={"chip one","chip two", "chip three","chip four", "I am chip number five","and i am chip number seven ."};

        chipCloud.addChips(list);

        ratingDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateYourExpeirenceDialog.dismiss();
            }
        });
        skipRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DynamicToast.makeSuccess(LoginActivity.this, "Mmm, working on it!", 3).show();
                rateYourExpeirenceDialog.dismiss();
            }
        });
        complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateYourExpeirenceDialog.dismiss();
                complaintDialog();
//                DynamicToast.makeSuccess(LoginActivity.this, "Mmm, working on it!", 3).show();
            }
        });
    }

    private void complaintDialog(){
        ArrayList<String> tags = new ArrayList<>();
        for(int i=0; i<7; i++){
            tags.add("Tag" + i);
        }
        final Dialog complaintDialog = new Dialog(RewardsActivity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        complaintDialog.setContentView(R.layout.comlaint_dialog);
        ChipCloud cloud = complaintDialog.findViewById(R.id.chipcloud);
        Button complainDone = complaintDialog.findViewById(R.id.complainDone);
        complaintDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        complaintDialog.show();

        String list[]={"chip one","chip two", "chip three","chip four", "I am chip number five","and i am chip number seven ."};

        cloud.addChips(list);

        complainDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complaintDialog.dismiss();
            }
        });
    }
}
