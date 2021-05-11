package com.e.moodkeeper.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.e.moodkeeper.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dao.DiaryDAO;
import dao.DiaryDAOImpl;
import pojo.Diary;

public class AddDiaryActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView cancelTv;
    private TextView saveTv;
    private TextView weatherTv;
    private TextView moodTv;
    private TextView dateTv;

    private EditText diaryTitleEt;
    private EditText diaryContentEt;

    private int diary_id = 1;
    private int user_id = 1;
    private int mood_id = 1;
    private int weather_id = 1;
    private int category_id = 1;
    private String diary_title;
    private String diary_content;
    private Date diary_date = new Date();
    private Date anchor = new Date(0);
    //状态
    private int addState = 0;  //本地新增
    private int deleteState = -1;  //标记删除
    private int updateState = 1;  //本地更新

    //时间选择器
    protected String days;
    protected int mYear;
    protected int mMonth;
    protected int mDays;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_diary);

        cancelTv = (TextView) findViewById(R.id.cancel);
        cancelTv.setOnClickListener(this);

        diaryTitleEt = (EditText) findViewById(R.id.diary_title);

        saveTv = (TextView) findViewById(R.id.save);
        saveTv.setOnClickListener(this);

        diaryContentEt = (EditText) findViewById(R.id.diary_content);

        weatherTv = (TextView) findViewById(R.id.weather1);
        weatherTv.setOnClickListener(this);

        moodTv = (TextView) findViewById(R.id.mood1);
        moodTv.setOnClickListener(this);

        //获取当前年，月，日
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDays = calendar.get(Calendar.DAY_OF_MONTH);

        dateTv = (TextView) findViewById(R.id.date1);
        days = new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDays).toString();
        dateTv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                //返回上一界面
                setResult(RESULT_OK, new Intent());
                finish();
                break;
            case R.id.save:
                //保存
                save();
               break;
            case R.id.weather1:
                //选择天气图标
                Intent intent1 = new Intent(AddDiaryActivity.this, ActivityShowWeather.class);
                startActivityForResult(intent1, 1);
                break;
            case R.id.mood1:
                //选择心情图标
                Intent intent2 = new Intent(AddDiaryActivity.this, ActivityShowMood.class);
                startActivityForResult(intent2, 2);
                break;
            case R.id.date1:
                //选择日期
                showDateSelector();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (requestCode == RESULT_OK) {
//                    String weather_id1 = data.getStringExtra("weather_id");
//                    Log.d("AddDiaryActivity", weather_id1);
//                    weather_id = Integer.parseInt(String.valueOf(weather_id1));
                    weather_id = data.getIntExtra("weather_id",0);
//                    Bundle bundle = data.getExtras();
//                    if (bundle != null) {
//                        weather_id = bundle.getInt("weather_id", 0);
//                    }
                }
                break;
            case 2:
                if (requestCode == RESULT_OK) {
                    String mood_id1 = data.getStringExtra("mood_id");
                    mood_id = Integer.parseInt(String.valueOf(mood_id1));
                    Log.d("AddDiaryActivity", mood_id1);
                }
                break;
            default:
                Log.d("AddDiaryActivity","失败");
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        weather_id = getIntent().getIntExtra("weather_id", 0);
        mood_id = getIntent().getIntExtra("mood_id", 0);
        Log.d("AddDiaryActivity", String.valueOf(weather_id));
        Log.d("AddDiaryActivity", String.valueOf(mood_id));
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
                    } else {
                        days = new StringBuffer().append(mYear).append("-").append("0")
                                .append(mMonth + 1).append("-").append(mDays).toString();
                    }
                } else {
                    if (mDays < 10) {
                        days = new StringBuffer().append(mYear).append("-")
                                .append(mMonth + 1).append("-").append("0").append(mDays).toString();
                    } else {
                        days = new StringBuffer().append(mYear).append("-")
                                .append(mMonth + 1).append("-").append(mDays).toString();
                    }
                }
            }
        },mYear,mMonth,mDays).show();
    }

    //保存
    public void save(){
        diary_title = diaryTitleEt.getText().toString();
        diary_content = diaryContentEt.getText().toString();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = String.valueOf(dateTv.getText());
            diary_date = sdf.parse(date);
        } catch (Exception e) {
        }

        Diary diary = new Diary(diary_id, user_id, mood_id, weather_id, category_id, diary_title, diary_content, diary_date, addState, anchor);
        DiaryDAO diaryDAO = new DiaryDAOImpl();
        diaryDAO.insertDiary(diary);

        Toast.makeText(this, "添加成功！", Toast.LENGTH_SHORT).show();
    }

}
