package com.exclusave.Adapters;

import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.exclusave.Activities.Store_Location_Activity;
import com.exclusave.models.BranchesList_Data;
import com.exclusave.R;

import java.util.ArrayList;

public class Branches_list_Adapter  extends RecyclerView.Adapter<Branches_list_Adapter.MyViewHolder> {

    private Context context;
    Branches_list_Adapter.CustomItemClickListener listener;
    ArrayList<BranchesList_Data> commentsArray;


    public Branches_list_Adapter(Context context, ArrayList<BranchesList_Data> commentsArray, Branches_list_Adapter.CustomItemClickListener listener) {
        this.commentsArray = commentsArray;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public Branches_list_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view;
        LayoutInflater inflater = LayoutInflater.from(context);

        view = inflater.inflate(R.layout.branches_list_adapter, parent, false);
        final Branches_list_Adapter.MyViewHolder viewHolder = new Branches_list_Adapter.MyViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Branches_list_Adapter.MyViewHolder holder, int position) {
        holder.store_name.setText(commentsArray.get(position).getFirstName()+" "+commentsArray.get(position).getLastName());
        holder.store_name.setText(commentsArray.get(position).getTitle());
        holder.store_address.setText(commentsArray.get(position).getCityTitle()+", "+commentsArray.get(position).getAddress());
        holder.locationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String latitude = commentsArray.get(position).getLatitude();
                String longitude = commentsArray.get(position).getLongitude();
                String storename = commentsArray.get(position).getFirstName()+commentsArray.get(position).getMiddleName()+commentsArray.get(position).getLastName();
                Intent intent = new Intent(context, Store_Location_Activity.class);
                intent.putExtra("LATITUDE", latitude);
                intent.putExtra("LONGITUDE", longitude);
                intent.putExtra("STORENAME", storename);
                context.startActivity(intent);
            }
        });

        holder.callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(commentsArray.get(position).getMobile().equals("")){
                    Toast.makeText(context, context.getResources().getText(R.string.notAvailable), Toast.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+ commentsArray.get(position).getMobile()));
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return commentsArray.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

          TextView store_name,store_address;
          RelativeLayout locationbtn, callbtn;

        MyViewHolder(View itemView) {
            super(itemView);
            callbtn = itemView.findViewById(R.id.callstore);
            locationbtn = itemView.findViewById(R.id.location);
            store_name = itemView.findViewById(R.id.store_name);
            store_address = itemView.findViewById(R.id.store_address);
        }
    }

    public interface CustomItemClickListener {
        void onItemClick(View v, int position);
    }


}
