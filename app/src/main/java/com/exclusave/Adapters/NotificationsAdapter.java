package com.exclusave.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exclusave.Activities.ChatActivity;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.BaseClass;
import com.exclusave.models.Notifications_Data;
import com.exclusave.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static int size;
    private Context mContext;
    private CRLCallbacks callbacks;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor mEditor;
    public static final String MyPREFERENCES = "MyPrefs";
    ArrayList<Notifications_Data> Notificaitons;

    public NotificationsAdapter(Context mContext, ArrayList<Notifications_Data> Notifications, CRLCallbacks callbacks) {
        this.mContext = mContext;
        this.Notificaitons = Notifications;
        this.callbacks = callbacks;
    }

    @Override
    public int getItemViewType(int position) {

        String type = Notificaitons.get(position).getBookingID();
//        Log.e("type", type);
        if (type != null) {
            return Integer.parseInt(type);
        }
        return Integer.parseInt(type);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == 0){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notificationview_1, parent, false);
                    return new MyViewHolder(view);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notificationview_2, parent, false);
                    return new MyViewHolder1(view);
        }
//        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int i) {

        String type = String.valueOf(Notificaitons.get(i).getBookingID());

        size = Notificaitons.size();
        sharedpreferences = mContext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mEditor = sharedpreferences.edit();

        if(type.equals("0")) {
            if(!Notificaitons.get(i).getChatID().equals("0")){
                ((MyViewHolder) holder).announcementTitle.setText(Notificaitons.get(i).getTitle());
            }
            if(Notificaitons.get(i).getDescription().equals("")){
                ((MyViewHolder) holder).announcementText.setText(mContext.getResources().getString(R.string.sentAnImage));
            }else {
                ((MyViewHolder) holder).announcementText.setText(" " + Notificaitons.get(i).getDescription());
            }
            ((MyViewHolder) holder).timeAndDate.setText(BaseClass.TimeStampToTime(Notificaitons.get(i).getCreatedAt()) + " | " + BaseClass.TimeStampToDate(Notificaitons.get(i).getCreatedAt()));
            ((MyViewHolder) holder).mainLinear1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!Notificaitons.get(i).getChatID().equals("0")){
                        Log.e("NotiID", Notificaitons.get(i).getChatID());
                        mEditor.putString("ReceiverID", Notificaitons.get(i).getNotificationFrom()).commit();
                        mContext.startActivity(new Intent(mContext, ChatActivity.class));
                    }
                }
            });
        }else {
            ((MyViewHolder1) holder).merchantName.setText(Notificaitons.get(i).getFirstName() + " " + Notificaitons.get(i).getMiddleName() + " " + Notificaitons.get(i).getLastName());
            Picasso.get().load(Constants.URL.IMG_URL + Notificaitons.get(i).getImage()).error(R.drawable.merchant_place).into(((MyViewHolder1) holder).img);
            ((MyViewHolder1) holder).branch.setText(Notificaitons.get(i).getCategoryTitle());
            ((MyViewHolder1) holder).dateOfVisit.setText(BaseClass.TimeStampToDate(Notificaitons.get(i).getBookingDate()));
            ((MyViewHolder1) holder).timePrefered.setText(BaseClass.TimeStampToTime(Notificaitons.get(i).getBookingTime()));
            ((MyViewHolder1) holder).noOfGuests.setText(Notificaitons.get(i).getNoOfGuests());
            ((MyViewHolder1) holder).timeAndDate.setText(BaseClass.TimeStampToTime(Notificaitons.get(i).getCreatedAt()) + " | " + BaseClass.TimeStampToDate(Notificaitons.get(i).getCreatedAt()));


            if (Notificaitons.get(i).getIsApproved().equals("0")){
                ((MyViewHolder1) holder).approved.setText(mContext.getResources().getString(R.string.pending));
            }else if (Notificaitons.get(i).getIsApproved().equals("1")){
                ((MyViewHolder1) holder).approved.setText(mContext.getResources().getString(R.string.accepted));
            }else if (Notificaitons.get(i).getIsApproved().equals("2")){
                ((MyViewHolder1) holder).approved.setText(mContext.getResources().getString(R.string.rejected));
            }
        }
    }

    @Override
    public int getItemCount() {
        return Notificaitons.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView announcementText, timeAndDate, announcementTitle;
        LinearLayout mainLinear1;

        public MyViewHolder(View itemView) {
            super(itemView);

            announcementText = itemView.findViewById(R.id.announcementText);
            timeAndDate = itemView.findViewById(R.id.timeAndDate);
            announcementTitle = itemView.findViewById(R.id.announcementTitle);
            mainLinear1 = itemView.findViewById(R.id.main_linear1);

        }
    }

    class MyViewHolder1 extends RecyclerView.ViewHolder {

        ImageView img;
        TextView merchantName, branch, dateOfVisit, timePrefered, noOfGuests, timeAndDate;
        Button approved;

        MyViewHolder1(View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img);
            merchantName = itemView.findViewById(R.id.merchantName);
            branch = itemView.findViewById(R.id.branch);
            dateOfVisit = itemView.findViewById(R.id.dateOfVisit);
            timePrefered = itemView.findViewById(R.id.timePrefered);
            noOfGuests = itemView.findViewById(R.id.noOfGuests);
            timeAndDate = itemView.findViewById(R.id.timeAndDate);
            approved = itemView.findViewById(R.id.approved_button);

        }
    }

    public interface CRLCallbacks {
        void onItemClick(int position);
    }
}
