package com.exclusave.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.exclusave.models.CitiesData;
import com.exclusave.R;

import java.util.ArrayList;

public class CitiesFilterAdapter extends RecyclerView.Adapter<CitiesFilterAdapter.MyViewHolder> {

    private Context context;
    CustomItemClickListener listener;
    ArrayList<CitiesData> citiesData;
    public static ArrayList<String> cities = new ArrayList<>();
    public static ArrayList<String> cityIds = new ArrayList<>();

    public CitiesFilterAdapter(Context context, ArrayList<CitiesData> citiesData, CustomItemClickListener listener) {
        this.citiesData = citiesData;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view;
        LayoutInflater inflater = LayoutInflater.from(context);

        view = inflater.inflate(R.layout.filter_recycler_item, parent, false);
        final CitiesFilterAdapter.MyViewHolder viewHolder = new CitiesFilterAdapter.MyViewHolder(view);

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
        holder.checkBox.setText(citiesData.get(position).getTitle());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cities.add(citiesData.get(position).getTitle());
                    cityIds.add(citiesData.get(position).getCityID());
                }else {
                    for (int i = 0; i < cities.size(); i++){
                        if (cities.get(i).equals(citiesData.get(position).getTitle())){
                            cities.remove(i);
                            cityIds.remove(i);
                        }
                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return citiesData.size();
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
