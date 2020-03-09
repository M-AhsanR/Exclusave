package com.exclusave.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.exclusave.models.InterestsData;
import com.exclusave.R;

import java.util.ArrayList;

public class CategoriesFilterAdapter extends RecyclerView.Adapter<CategoriesFilterAdapter.MyViewHolder> {

    private Context context;
    CustomItemClickListener listener;
    ArrayList<InterestsData> interestsData;
    public static ArrayList<String> categories = new ArrayList<>();
    public static ArrayList<String> categoryIds = new ArrayList<>();

    public CategoriesFilterAdapter(Context context, ArrayList<InterestsData> interestsData, CustomItemClickListener listener) {
        this.interestsData = interestsData;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view;
        LayoutInflater inflater = LayoutInflater.from(context);

        view = inflater.inflate(R.layout.filter_recycler_item, parent, false);
        final CategoriesFilterAdapter.MyViewHolder viewHolder = new CategoriesFilterAdapter.MyViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.checkBox.setText(interestsData.get(position).getTitle());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    categories.add(interestsData.get(position).getTitle());
                    categoryIds.add(interestsData.get(position).getCategoryID());
                }else {
                    for (int i = 0; i < categories.size(); i++){
                        if (categories.get(i).equals(interestsData.get(position).getTitle())){
                            categories.remove(i);
                            categoryIds.remove(i);
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return interestsData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;

        MyViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.filterCheckBox);
        }
    }

    public interface CustomItemClickListener {
        void onItemClick(View v, int position);
    }
}
