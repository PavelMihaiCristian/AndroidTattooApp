package com.example.tattooshopapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tattooshopapp.model.ForecastItem;

import java.util.ArrayList;

public class ForecastItemAdapter extends RecyclerView.Adapter<ForecastItemAdapter.ViewHolder> {

    private ArrayList<ForecastItem> forecastItems;

    public ForecastItemAdapter(ArrayList<ForecastItem> forecastItems) {
        this.forecastItems = forecastItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.forecast_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.textView.setText(forecastItems.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return forecastItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.forecastItemView);
        }
    }
}
