package com.exclusave.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.exclusave.models.SubCategoriesData;
import com.exclusave.R;

import java.util.ArrayList;

public class SubCategoriesFilterAdapter extends RecyclerView.Adapter<SubCategoriesFilterAdapter.MyViewHolder> {

    private Context context;
    SubCategoriesFilterAdapter.CustomItemClickListener listener;
    ArrayList<SubCategoriesData> subCategoriesData;
    public static ArrayList<String> subcategories = new ArrayList<>();
    public static ArrayList<String> subcategoryIds = new ArrayList<>();

    public SubCategoriesFilterAdapter(Context context, ArrayList<SubCategoriesData> subCategoriesData, SubCategoriesFilterAdapter.CustomItemClickListener listener) {
        this.subCategoriesData = subCategoriesData;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public SubCategoriesFilterAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view;
        LayoutInflater inflater = LayoutInflater.from(context);

        view = inflater.inflate(R.layout.subfilter_recycler_item, parent, false);
        final SubCategoriesFilterAdapter.MyViewHolder viewHolder = new SubCategoriesFilterAdapter.MyViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SubCategoriesFilterAdapter.MyViewHolder holder, int position) {
        holder.checkBox.setText(subCategoriesData.get(position).getTitle());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    subcategories.add(subCategoriesData.get(position).getTitle());
                    subcategoryIds.add(subCategoriesData.get(position).getCategoryID());
                } else {
                    for (int i = 0; i < subcategories.size(); i++) {
                        if (subcategories.get(i).equals(subCategoriesData.get(position).getTitle())) {
                            subcategories.remove(i);
                            subcategoryIds.remove(i);
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return subCategoriesData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;

        MyViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.subfilterCheckBox);
        }
    }

    public interface CustomItemClickListener {
        void onItemClick(View v, int position);
    }
}