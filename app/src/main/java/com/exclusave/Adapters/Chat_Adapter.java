package com.exclusave.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.BaseClass;
import com.exclusave.models.ChatMsgsModel;
import com.exclusave.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Chat_Adapter extends RecyclerView.Adapter<Chat_Adapter.MyViewHolder> {

    private Context context;
    CustomItemClickListener listener;
    ArrayList<ChatMsgsModel> chatMsgsModels;
    SharedPreferences sharedPreferences;

    public Chat_Adapter(Context context, ArrayList<ChatMsgsModel> chatMsgsModels, CustomItemClickListener listener) {
        this.chatMsgsModels = chatMsgsModels;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public Chat_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view;
        LayoutInflater inflater = LayoutInflater.from(context);

        view = inflater.inflate(R.layout.chat_adapter, parent, false);
        final Chat_Adapter.MyViewHolder viewHolder = new Chat_Adapter.MyViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final Chat_Adapter.MyViewHolder holder, int position) {

        sharedPreferences = BaseClass.sharedPreferances((Activity) context);

        holder.setIsRecyclable(false);

        if(!chatMsgsModels.get(position).getCompressedMessageImage().equals("")){
            Picasso.get().load(Constants.URL.IMG_URL + chatMsgsModels.get(position).getCompressedMessageImage()).into(holder.sentImg);
            Picasso.get().load(Constants.URL.IMG_URL + chatMsgsModels.get(position).getCompressedMessageImage()).into(holder.recievedImg);
        }else {
            holder.my_message.setText(chatMsgsModels.get(position).getMessage());
            holder.others_message.setText(chatMsgsModels.get(position).getMessage());
        }

        if (chatMsgsModels.get(position).getSenderUserID().equals(sharedPreferences.getString("UserID", ""))){
            if(chatMsgsModels.get(position).getMessageImage().equals("")){
                holder.sentImg.setVisibility(View.GONE);
                holder.recievedImg.setVisibility(View.GONE);
                holder.my_message.setVisibility(View.VISIBLE);
                holder.others_message.setVisibility(View.GONE);
            }else {
                holder.sentImg.setVisibility(View.VISIBLE);
                holder.recievedImg.setVisibility(View.GONE);
                holder.my_message.setVisibility(View.GONE);
                holder.others_message.setVisibility(View.GONE);
            }
        }else {
            if(chatMsgsModels.get(position).getMessageImage().equals("")){
                holder.sentImg.setVisibility(View.GONE);
                holder.recievedImg.setVisibility(View.GONE);
                holder.my_message.setVisibility(View.GONE);
                holder.others_message.setVisibility(View.VISIBLE);
            }else {
                holder.sentImg.setVisibility(View.GONE);
                holder.recievedImg.setVisibility(View.VISIBLE);
                holder.my_message.setVisibility(View.GONE);
                holder.others_message.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return chatMsgsModels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView my_message,others_message;
        RoundedImageView recievedImg, sentImg;

        MyViewHolder(View itemView) {
            super(itemView);
            my_message = itemView.findViewById(R.id.mymessage);
            others_message = itemView.findViewById(R.id.othersmessage);
            recievedImg = itemView.findViewById(R.id.recievedImg);
            sentImg = itemView.findViewById(R.id.sentImg);
        }
    }

    public interface CustomItemClickListener {
        void onItemClick(View v, int position);
    }
}
