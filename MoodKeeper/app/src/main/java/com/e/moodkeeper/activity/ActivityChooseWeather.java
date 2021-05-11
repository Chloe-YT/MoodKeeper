package com.e.moodkeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.moodkeeper.R;
import com.e.moodkeeper.adapter.ChooseWeatherAdapter;

import java.util.ArrayList;
import java.util.List;

import pojo.Weather;

public class ActivityChooseWeather extends AppCompatActivity implements View.OnClickListener {

    private ChooseWeatherAdapter chooseWeatherAdapter;

    private List<Weather> weatherList = new ArrayList<>();

    private TextView frontStepTv;
    private TextView nextStepTv;

    private RecyclerView chooseWeatherRv;

    private int mood_id;
    private int weather_id;
    private String days;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_weather);

        Intent intent = getIntent();
        final Bundle bundle = intent.getBundleExtra("bundle");
        mood_id = bundle.getInt("mood_id");
        days = bundle.getString("diary_date");
        Log.d("ActivityChooseWeather", String.valueOf(mood_id));
        Log.d("ActivityChooseWeather", days);

        frontStepTv = findViewById(R.id.to_choose_mood);
        frontStepTv.setOnClickListener(this);

        nextStepTv = findViewById(R.id.to_write_content);
        nextStepTv.setOnClickListener(this);

        initWeather();

        chooseWeatherRv = (RecyclerView) findViewById(R.id.choose_weather_rv);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        chooseWeatherRv.setLayoutManager(layoutManager);
        chooseWeatherAdapter = new ChooseWeatherAdapter(weatherList);
        chooseWeatherRv.setAdapter(chooseWeatherAdapter);

        chooseWeatherAdapter.setOnItemClickListener(new ChooseWeatherAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                chooseWeatherAdapter.setmPosition(position);
                chooseWeatherAdapter.notifyDataSetChanged();
                Weather weather = weatherList.get(position);
                weather_id = weather.getWeather_id();
                Intent intent = new Intent(ActivityChooseWeather.this, AddDiaryActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("mood_id", mood_id);
                bundle1.putInt("weather_id", weather_id);
                bundle1.putString("diary_date", days);
                intent.putExtra("paraBundle", bundle1);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.to_choose_mood:
                finish();
                break;
            case R.id.to_write_content:
                Intent intent1 = new Intent(ActivityChooseWeather.this, AddDiaryActivity.class);
                startActivityForResult(intent1, 1);
                break;
        }
    }

    private void initWeather() {
        Weather sunny = new Weather(1, "晴朗", R.drawable.sunny_pic);
        weatherList.add(sunny);
        Weather hail = new Weather(2, "雾", R.drawable.fog_pic);
        weatherList.add(hail);
        Weather smog = new Weather(3, "雾霾", R.drawable.smog_pic);
        weatherList.add(smog);
        Weather snow = new Weather(4, "下雪", R.drawable.snow_pic);
        weatherList.add(snow);
        Weather cloudy = new Weather(5, "阴", R.drawable.cloudy_pic);
        weatherList.add(cloudy);
        Weather rain = new Weather(6, "下雨", R.drawable.rain_pic);
        weatherList.add(rain);
        Weather duoyvn = new Weather(7, "多云", R.drawable.duoyvn_pic);
        weatherList.add(duoyvn);
        Weather leizhenyu = new Weather(8, "雷阵雨", R.drawable.leizhenyu_pic);
        weatherList.add(leizhenyu);
        Weather storm = new Weather(9, "沙尘暴", R.drawable.storm_pic);
        weatherList.add(storm);


    }

}
