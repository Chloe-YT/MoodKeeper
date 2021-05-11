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

import pojo.Mood;

public class ChooseMoodAdapter extends RecyclerView.Adapter<ChooseMoodAdapter.ViewHolder> {

    private List<Mood> mMoodList;

    private int mPosition;

    public interface OnItemClickListener {
        void onClick (int position);
    }

    public OnItemClickListener listener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mood_name;
        ImageView mood_image;

        public ViewHolder(View view) {
            super(view);
            mood_name = (TextView) view.findViewById(R.id.mood_name);
            mood_image = (ImageView) view.findViewById(R.id.mood_image);
        }
    }

    public ChooseMoodAdapter(List<Mood> mMList) {
        mMoodList = mMList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mood_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Mood mood = mMoodList.get(position);
        holder.mood_name.setText(mood.getMood_name());
        holder.mood_image.setImageResource(mood.getImage_id());

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
            holder.mood_image.setBackgroundColor(Color.GRAY);
        }
    }

    public int getItemCount() {
        return mMoodList.size();
    }

    public int getmPosition() {
        return mPosition;
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public void clear() {
        mMoodList.clear();
    }

}
