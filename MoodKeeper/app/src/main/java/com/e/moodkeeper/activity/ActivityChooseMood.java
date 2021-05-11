package com.e.moodkeeper.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.moodkeeper.R;
import com.e.moodkeeper.adapter.ChooseMoodAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pojo.Mood;

public class ActivityChooseMood extends AppCompatActivity implements View.OnClickListener {

    private ChooseMoodAdapter chooseMoodAdapter;

    private List<Mood> moodList = new ArrayList<>();

    private TextView backTv;
    private TextView chooseDateTv;
    private TextView nextStepTv;

    private RecyclerView chooseMoodRv;

    //时间选择器
    protected String days;
    protected int mYear;
    protected int mMonth;
    protected int mDays;

    private int mood_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mood);

        //获取当前年，月，日
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDays = calendar.get(Calendar.DAY_OF_MONTH);

        backTv = findViewById(R.id.back_main);
        backTv.setOnClickListener(this);

        chooseDateTv = findViewById(R.id.choose_date);
        chooseDateTv.setOnClickListener(this);
        days = new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDays).toString();
        chooseDateTv.setText(days);

        nextStepTv = findViewById(R.id.to_choose_weather);
        nextStepTv.setOnClickListener(this);

        initMood();

        chooseMoodRv = (RecyclerView) findViewById(R.id.choose_mood_rv);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        chooseMoodRv.setLayoutManager(layoutManager);
        chooseMoodAdapter = new ChooseMoodAdapter(moodList);
        chooseMoodRv.setAdapter(chooseMoodAdapter);

        chooseMoodAdapter.setOnItemClickListener(new ChooseMoodAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                chooseMoodAdapter.setmPosition(position);
                chooseMoodAdapter.notifyDataSetChanged();
                Mood mood = moodList.get(position);
                mood_id = mood.getMood_id();
                Intent intent = new Intent(ActivityChooseMood.this, ActivityChooseWeather.class);
                Bundle bundle = new Bundle();
                bundle.putInt("mood_id", mood_id);
                bundle.putString("diary_date",days);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_main:
                finish();
                break;
            case R.id.choose_date:
                showDateSelector();
                break;
            case R.id.to_choose_weather:
                Intent intent = new Intent(ActivityChooseMood.this, ActivityChooseWeather.class);
                intent.putExtra("mood_id", mood_id);
                startActivity(intent);
                break;
        }
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

    //显示日期选择器
    public void showDateSelector() {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                mYear = i;
                mMonth = i1;
                mDays = i2;
                if (mMonth + 1 < 10) {
                    if (mDays < 10) {
                        days = new StringBuffer().append(mYear).append("-").append("0")
                                .append(mMonth + 1).append("-").append("0").append(mDays).toString();
                        chooseDateTv.setText(days);
                    } else {
                        days = new StringBuffer().append(mYear).append("-").append("0")
                                .append(mMonth + 1).append("-").append(mDays).toString();
                        chooseDateTv.setText(days);
                    }
                } else {
                    if (mDays < 10) {
                        days = new StringBuffer().append(mYear).append("-")
                                .append(mMonth + 1).append("-").append("0").append(mDays).toString();
                        chooseDateTv.setText(days);
                    } else {
                        days = new StringBuffer().append(mYear).append("-")
                                .append(mMonth + 1).append("-").append(mDays).toString();
                        chooseDateTv.setText(days);
                    }
                }
            }
        },mYear,mMonth,mDays).show();
    }

}
