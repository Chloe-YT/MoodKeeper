package com.e.moodkeeper.adapter;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.e.moodkeeper.R;

import java.text.SimpleDateFormat;
import java.util.List;

import pojo.Diary;

public class ShowDiaryAdapter extends RecyclerView.Adapter<ShowDiaryAdapter.ViewHolder> {

    private Context mContext;

    private List<Diary> mDiaryList;

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public OnItemClickListener listener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView diary_cv;
        private TextView diary_title;
        private TextView diary_date;
        private TextView diary_content;
        private TextView diary_category;
        private ImageView diary_mood_iv;
        private ImageView diary_weather_iv;

        public ViewHolder(View view) {
            super(view);
            diary_cv = (CardView) view.findViewById(R.id.diary_cardview);

            diary_title = (TextView) view.findViewById(R.id.diary_title_cv);
            TextPaint textPaint = diary_title.getPaint();
            textPaint.setFakeBoldText(true);

            diary_date = (TextView) view.findViewById(R.id.diary_date_cv);
            diary_content = (TextView) view.findViewById(R.id.diary_content_cv);
            diary_category = (TextView) view.findViewById(R.id.diary_category_tv);
//            diary_mood = (TextView) view.findViewById(R.id.diary_mood_cv);
            diary_mood_iv = (ImageView) view.findViewById(R.id.diary_mood_iv);
            diary_weather_iv = (ImageView) view.findViewById(R.id.diary_weather_iv);

        }
    }

    public ShowDiaryAdapter(List<Diary> diaryList) {
        mDiaryList = diaryList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.diary_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Diary diary = mDiaryList.get(position);
        holder.diary_title.setText(diary.getDiary_name());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        String data = formatter.format(diary.getDiary_date());
        holder.diary_date.setText(data);

        holder.diary_content.setText(diary.getDiary_content());
        holder.diary_category.setText(String.valueOf(diary.getCategory_id()));
//        holder.diary_mood.setText(String.valueOf(diary.getMood_id()));
//        Glide.with(mContext).load(diary.getCategory_id()).into(holder.diary_mood_Iv);
        // holder.diary_weather.setImageResource(diary.get(i).image_id);

        //设置日记分类
        switch (diary.getCategory_id()) {
            case 1:
                holder.diary_category.setText("生活");
                break;
            case 2:
                holder.diary_category.setText("学习");
                break;
            case 3:
                holder.diary_category.setText("办公");
                break;
            case 4:
                holder.diary_category.setText("娱乐");
                break;
            case 5:
                holder.diary_category.setText("其他");
                break;
        }

        //设置心情图标
        switch (diary.getMood_id()) {
            case 1:
                holder.diary_mood_iv.setImageResource(R.drawable.happy_pic);
                break;
            case 2:
                holder.diary_mood_iv.setImageResource(R.drawable.proud_pic);
                break;
            case 3:
                holder.diary_mood_iv.setImageResource(R.drawable.full_pic);
                break;
            case 4:
                holder.diary_mood_iv.setImageResource(R.drawable.strive_pic);
                break;
            case 5:
                holder.diary_mood_iv.setImageResource(R.drawable.calm_pic);
                break;
            case 6:
                holder.diary_mood_iv.setImageResource(R.drawable.tired_pic);
                break;
            case 7:
                holder.diary_mood_iv.setImageResource(R.drawable.sad_pic);
                break;
            case 8:
                holder.diary_mood_iv.setImageResource(R.drawable.angry_pic);
                break;
            case 9:
                holder.diary_mood_iv.setImageResource(R.drawable.others_pic);
                break;
        }

        //设置天气图标
        switch (diary.getWeather_id()) {
            case 1:
                holder.diary_weather_iv.setImageResource(R.drawable.sunny_pic);
                break;
            case 2:
                holder.diary_weather_iv.setImageResource(R.drawable.fog_pic);
                break;
            case 3:
                holder.diary_weather_iv.setImageResource(R.drawable.smog_pic);
                break;
            case 4:
                holder.diary_weather_iv.setImageResource(R.drawable.snow_pic);
                break;
            case 5:
                holder.diary_weather_iv.setImageResource(R.drawable.cloudy_pic);
                break;
            case 6:
                holder.diary_weather_iv.setImageResource(R.drawable.rain_pic);
                break;
            case 7:
                holder.diary_weather_iv.setImageResource(R.drawable.duoyvn_pic);
                break;
            case 8:
                holder.diary_weather_iv.setImageResource(R.drawable.leizhenyu_pic);
                break;
            case 9:
                holder.diary_weather_iv.setImageResource(R.drawable.storm_pic);
                break;
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDiaryList.size();
    }

    public void clear() {
        mDiaryList.clear();
    }

}
