package com.e.moodkeeper.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.moodkeeper.R;
import com.e.moodkeeper.adapter.ChooseWeatherAdapter;

import java.util.ArrayList;
import java.util.List;

import pojo.Weather;

public class ActivityShowWeather extends Activity {

    private ChooseWeatherAdapter chooseWeatherAdapter;

    private List<Weather> weatherList = new ArrayList<>();

    private TextView closeTv;

    private int weather_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weather);
        initWeather();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.weather_rv);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        chooseWeatherAdapter = new ChooseWeatherAdapter(weatherList);
        recyclerView.setAdapter(chooseWeatherAdapter);

        chooseWeatherAdapter.setOnItemClickListener(new ChooseWeatherAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                chooseWeatherAdapter.setmPosition(position);
                chooseWeatherAdapter.notifyDataSetChanged();
                Weather weather = weatherList.get(position);
                weather_id = weather.getWeather_id();
                Toast.makeText(ActivityShowWeather.this, "You click"+weather_id, Toast.LENGTH_SHORT).show();

                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                bundle.putInt("weather_id", weather_id);
                intent.putExtras(bundle);

                setResult(4, intent);
                finish();

            }
        });

        closeTv = (TextView)findViewById(R.id.close_btn);
        closeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
