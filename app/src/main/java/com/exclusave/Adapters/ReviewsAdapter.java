package com.exclusave.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.exclusave.ApiStructure.Constants;
import com.exclusave.BaseClass;
import com.exclusave.models.History_Model;
import com.exclusave.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder> {

    private Context context;
    CustomItemClickListener listener;
    ArrayList<History_Model> reviewsData;


    public ReviewsAdapter(Context context, ArrayList<History_Model> reviewsData, CustomItemClickListener listener) {
        this.reviewsData = reviewsData;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view;
        LayoutInflater inflater = LayoutInflater.from(context);

        view = inflater.inflate(R.layout.reviews_item, parent, false);
        final ReviewsAdapter.MyViewHolder viewHolder = new ReviewsAdapter.MyViewHolder(view);

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

        if(!reviewsData.get(position).getCompressedImage().equals("null")){
            Picasso.get().load(Constants.URL.IMG_URL + reviewsData.get(position).getCompressedImage()).into(holder.img);
        }

        holder.timeAndDate.setText(BaseClass.TimeStampToTime(reviewsData.get(position).getCreatedAt()) + " | " + BaseClass.TimeStampToDate(reviewsData.get(position).getCreatedAt()));
        if(reviewsData.get(position).getIsFamilyMember().equals("0")){
            holder.name.setText(reviewsData.get(position).getFirstName());
        }else {
            holder.name.setText(reviewsData.get(position).getFamilyMemberFirstName());
            Picasso.get().load(Constants.URL.IMG_URL + reviewsData.get(position).getFamilyMemberImage()).error(R.drawable.user).into(holder.img);
        }

        holder.cardType.setText(reviewsData.get(position).getCardTypeTitle());

        if(reviewsData.get(position).getComment() != null){
            if(reviewsData.get(position).getComment().equals("null")){
                holder.comment.setText(context.getResources().getString(R.string.noCommentGiven));
            }else {
                holder.comment.setText(reviewsData.get(position).getComment());
            }
        }else {
            holder.comment.setText(context.getResources().getString(R.string.noCommentGiven));
        }

//        Log.e("rating123", reviewsData.get(position).getRating());

        if(reviewsData.get(position).getRating() != null){
            if(!reviewsData.get(position).getRating().equals("")){
                holder.ratings.setRating(Float.parseFloat(reviewsData.get(position).getRating()));
            }
        }



    }


    @Override
    public int getItemCount() {
        return reviewsData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView img;
        TextView name, cardType, timeAndDate, comment;
        RatingBar ratings;

        MyViewHolder(View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            cardType = itemView.findViewById(R.id.cardType);
            timeAndDate = itemView.findViewById(R.id.timeAndDate);
            comment = itemView.findViewById(R.id.comment);
            ratings = itemView.findViewById(R.id.ratings);
        }
    }

    public interface CustomItemClickListener {
        void onItemClick(View v, int position);
    }
}
