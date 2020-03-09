package com.exclusave.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.exclusave.ApiStructure.Constants;
import com.exclusave.models.History_Model;
import com.exclusave.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashBorddummy_Adapter extends RecyclerView.Adapter<DashBorddummy_Adapter.MyViewHolder> {

    private Context context;
    CustomItemClickListener listener;
    ArrayList<History_Model> merchant_list_data;
    public static ArrayList<String> cities = new ArrayList<>();
    public static ArrayList<String> cityIds = new ArrayList<>();

    public DashBorddummy_Adapter(Context context, ArrayList<History_Model> listData, CustomItemClickListener listener) {
        this.merchant_list_data = listData;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view;
        LayoutInflater inflater = LayoutInflater.from(context);

        view = inflater.inflate(R.layout.dashbord_adapter, parent, false);
        final DashBorddummy_Adapter.MyViewHolder viewHolder = new DashBorddummy_Adapter.MyViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(new Date(Long.parseLong(merchant_list_data.get(position).getCreatedAt())));

        SimpleDateFormat timeformatter = new SimpleDateFormat("hh:mm");
        String timeString = timeformatter.format(new Date(Long.parseLong(merchant_list_data.get(position).getCreatedAt())));

        // expanded views filling
        Picasso.get().load(Constants.URL.IMG_URL + merchant_list_data.get(position).getImage()).into(holder.expanded_profile_pic);
        holder.expanded_customer_name.setText(merchant_list_data.get(position).getFirstName() + " " + merchant_list_data.get(position).getMiddleName() + " " + merchant_list_data.get(position).getLastName());
        holder.expanded_cardtype.setText(merchant_list_data.get(position).getCardTypeTitle());
        holder.expanded_time.setText(timeString + " | " + dateString);
        holder.expanded_totalbill.setText(merchant_list_data.get(position).getBillBefore());
        holder.expanded_savings.setText(merchant_list_data.get(position).getDiscount());
        if (merchant_list_data.get(position).getReason().isEmpty()&& merchant_list_data.get(position).getComment().isEmpty()){
            holder.expanded_comment.setText(context.getResources().getString(R.string.noCommentGiven));
        }else {
            holder.expanded_comment.setText(merchant_list_data.get(position).getReason()+" "+merchant_list_data.get(position).getComment());
        }
        if (merchant_list_data.get(position).getRating() == "null") {
            Log.e("rating", merchant_list_data.get(position).getRating());
        } else {
            holder.expanded_ratingbar.setRating(Float.parseFloat((merchant_list_data.get(position).getRating())));
        }

        // simple views filling
        Picasso.get().load(Constants.URL.IMG_URL + merchant_list_data.get(position).getImage()).into(holder.simple_profile_pic);
        holder.simple_customer_name.setText(merchant_list_data.get(position).getFirstName() + " " + merchant_list_data.get(position).getMiddleName() + " " + merchant_list_data.get(position).getLastName());
        holder.simple_cardtype.setText(merchant_list_data.get(position).getCardTypeTitle());
        holder.simple_time.setText(timeString + " | " + dateString);
        holder.simple_total_bill.setText(context.getResources().getString(R.string.totalBill) + " " + merchant_list_data.get(position).getBillAfter() + " SAR");

        holder.expandbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.closed.setVisibility(View.GONE);
                holder.expended.setVisibility(View.VISIBLE);
            }
        });

        holder.closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.expended.setVisibility(View.GONE);
                holder.closed.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public int getItemCount() {
        return merchant_list_data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView expanded_profile_pic, simple_profile_pic;
        TextView expanded_customer_name, expanded_cardtype, expanded_time, expanded_comment, expanded_totalbill, expanded_savings;
        TextView simple_customer_name, simple_cardtype, simple_time, simple_total_bill;
        RatingBar expanded_ratingbar;

        LinearLayout expended, closed, expanded_receipt;
        ImageView expandbtn, closebtn;

        MyViewHolder(View itemView) {
            super(itemView);
            //extended views;
            expanded_customer_name = itemView.findViewById(R.id.expanded_customername);
            expanded_cardtype = itemView.findViewById(R.id.expanded_cardtype);
            expanded_time = itemView.findViewById(R.id.expanded_time);
            expanded_comment = itemView.findViewById(R.id.expanded_comment);
            expanded_totalbill = itemView.findViewById(R.id.expanded_totalbill);
            expanded_savings = itemView.findViewById(R.id.expanded_totalsavings);
            expanded_ratingbar = itemView.findViewById(R.id.expanded_ratings);
            expanded_profile_pic = itemView.findViewById(R.id.expanded_profilepic);
            expanded_receipt = itemView.findViewById(R.id.expanded_receipt);

            //simple views
            simple_profile_pic = itemView.findViewById(R.id.simple_profilepic);
            simple_customer_name = itemView.findViewById(R.id.simple_customername);
            simple_cardtype = itemView.findViewById(R.id.simple_cardtype);
            simple_time = itemView.findViewById(R.id.simple_time);
            simple_total_bill = itemView.findViewById(R.id.simple_totalbill);

            //main views
            expended = itemView.findViewById(R.id.expended_linear);
            closed = itemView.findViewById(R.id.closelinear);
            expandbtn = itemView.findViewById(R.id.expandlistbtn);
            closebtn = itemView.findViewById(R.id.closelistbtn);
        }
    }

    public interface CustomItemClickListener {
        void onItemClick(View v, int position);
    }
}
