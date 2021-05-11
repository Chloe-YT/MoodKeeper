package com.e.moodkeeper.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.moodkeeper.R;
import com.e.moodkeeper.adapter.ChooseMoodAdapter;

import java.util.ArrayList;
import java.util.List;

import pojo.Mood;

public class ActivityShowMood extends Activity {

    private ChooseMoodAdapter chooseMoodAdapter;

    private List<Mood> moodList = new ArrayList<>();

    private TextView closeTv;

    private int mood_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_mood);
        initMood();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.mood_rv);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        chooseMoodAdapter = new ChooseMoodAdapter(moodList);
        recyclerView.setAdapter(chooseMoodAdapter);

        chooseMoodAdapter.setOnItemClickListener(new ChooseMoodAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                chooseMoodAdapter.setmPosition(position);
                chooseMoodAdapter.notifyDataSetChanged();
                Mood mood = moodList.get(position);
                mood_id = mood.getMood_id();

                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                bundle.putInt("mood_id", mood_id);
                intent.putExtras(bundle);

                setResult(3, intent);
                finish();
            }
        });

        closeTv = (TextView)findViewById(R.id.close_btn1);
        closeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initMood() {
        Mood happy = new Mood(1, "开心", R.drawable.happy_pic);
        moodList.add(happy);
        Mood proud = new Mood(2, "得意", R.drawable.proud_pic);
        moodList.add(proud);
        Mood full = new Mood(3, "充实", R.drawable.full_pic);
        moodList.add(full);
        Mood strive = new Mood(4, "努力", R.drawable.strive_pic);
        moodList.add(strive);
        Mood calm = new Mood(5, "平静", R.drawable.calm_pic);
        moodList.add(calm);
        Mood tired = new Mood(6, "疲惫", R.drawable.tired_pic);
        moodList.add(tired);
        Mood sad = new Mood(7, "伤心", R.drawable.sad_pic);
        moodList.add(sad);
        Mood angry = new Mood(8, "生气", R.drawable.angry_pic);
        moodList.add(angry);
        Mood others = new Mood(9, "其他", R.drawable.others_pic);
        moodList.add(others);
    }

}
