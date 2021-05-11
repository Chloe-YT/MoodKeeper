package com.e.moodkeeper.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.moodkeeper.R;

import java.util.List;

import pojo.Weather;

public class ChooseWeatherAdapter extends RecyclerView.Adapter<ChooseWeatherAdapter.ViewHolder> {

    private List<Weather> mWeatherList;

    private int mPosition;

    public interface OnItemClickListener {
        void onClick (int position);
    }

    public OnItemClickListener listener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView weather_name;
        ImageView weather_image;

        public ViewHolder(View view) {
            super(view);
            weather_name = (TextView) view.findViewById(R.id.weather_name);
            weather_image = (ImageView) view.findViewById(R.id.weather_image);
        }
    }

    public ChooseWeatherAdapter(List<Weather> mWList) {
        mWeatherList = mWList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Weather weather = mWeatherList.get(position);
        holder.weather_name.setText(weather.getWeather_name());
        holder.weather_image.setImageResource(weather.getImage_id());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                    notifyDataSetChanged();
                }
            }
        });

        if (position == getmPosition()) {
            holder.weather_image.setBackgroundColor(Color.GRAY);
        }
    }

    public int getItemCount() {
        return mWeatherList.size();
    }

    public int getmPosition() {
        return mPosition;
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public void clear() {
        mWeatherList.clear();
    }

}
