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
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dao.DiaryDAO;
import dao.DiaryDAOImpl;
import pojo.Diary;

public class ActivityUpdateDiary extends AppCompatActivity implements View.OnClickListener {

    Diary diary;

    private TextView cancelUpdateTv;
    private TextView saveUpdateTv;
    private TextView updateMoodTv;
    private TextView updateWeatherTv;
    private TextView updateDateTv;
    private TextView delectTv;

    private EditText updateTitleEt;
    private EditText updateContentEt;

    private MaterialSpinner updateCategory;

    private int user_id = 1;
    private int diary_id;
    private int mood_id;
    private int weather_id;
    private int category_id;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_diary);

        cancelUpdateTv = (TextView) findViewById(R.id.cancel1);
        cancelUpdateTv.setOnClickListener(this);

        saveUpdateTv = (TextView) findViewById(R.id.save1);
        saveUpdateTv.setOnClickListener(this);

        updateMoodTv = (TextView) findViewById(R.id.mood1);
        updateMoodTv.setOnClickListener(this);

        updateWeatherTv = (TextView) findViewById(R.id.weather1);
        updateWeatherTv.setOnClickListener(this);

        updateDateTv = (TextView) findViewById(R.id.date1);
        updateDateTv.setOnClickListener(this);

        delectTv = (TextView) findViewById(R.id.delect_btn);
        delectTv.setOnClickListener(this);

        updateTitleEt = (EditText) findViewById(R.id.diary_title1);

        updateContentEt = (EditText) findViewById(R.id.diary_content1);

        updateCategory = (MaterialSpinner) findViewById(R.id.choose_category1);

        setData();

        updateCategory.setItems("生活", "学习", "办公", "娱乐", "其他");
        updateCategory.setSelectedIndex(category_id - 1);
        updateCategory.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
//                Snackbar.make(view, "Clicked" + item, Snackbar.LENGTH_LONG).show();
                category_id = position + 1;
                Log.d("AddDiaryActivityNew", String.valueOf(category_id));
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel1:
                setResult(RESULT_OK, new Intent());
                finish();
                break;
            case R.id.save1:
                save();
                break;
            case R.id.mood1:
                //选择心情图标
                Intent intent1 = new Intent(ActivityUpdateDiary.this, ActivityShowMood.class);
                startActivityForResult(intent1, 3);
                break;
            case R.id.weather1:
                //选择天气图标
                Intent intent2 = new Intent(ActivityUpdateDiary.this, ActivityShowWeather.class);
                startActivityForResult(intent2, 4);
                break;
            case R.id.date1:
                showDateSelector();
                break;
            case R.id.delect_btn:
                delectDiary();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == 3) {
            Bundle bundle = data.getExtras();
            mood_id = bundle.getInt("mood_id");
            Log.d("ActivityUpdateDiary", String.valueOf(mood_id));
        } else if (requestCode == 4 && resultCode == 4) {
            Bundle bundle1 = data.getExtras();
            weather_id = bundle1.getInt("weather_id");
            Log.d("ActivityUpdateDiary",String.valueOf(weather_id));
        }
    }

    //初始化数据
    public void setData() {
        Intent intent = getIntent();
        final Bundle bundle = intent.getBundleExtra("bundle");
        diary_id = bundle.getInt("diary_id");
        Log.d("ActivityUpdateDiary", String.valueOf(diary_id));

        DiaryDAO diaryDAO = new DiaryDAOImpl();
        diary = diaryDAO.getDiaryById(diary_id);

        updateTitleEt.setText(diary.getDiary_name());
        updateContentEt.setText(diary.getDiary_content());

        mood_id = diary.getMood_id();
        weather_id = diary.getWeather_id();
        category_id = diary.getCategory_id();

        //设置日记日期
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        days = formatter.format(diary.getDiary_date());

        //获取当前年，月，日
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDays = calendar.get(Calendar.DAY_OF_MONTH);

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
        diary_title = updateTitleEt.getText().toString();
        diary_content = updateContentEt.getText().toString();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            diary_date = sdf.parse(days);
        } catch (Exception e) {
        }

        Diary diary = new Diary(diary_id, user_id, mood_id, weather_id, category_id, diary_title, diary_content, diary_date, addState, anchor);
        DiaryDAO diaryDAO = new DiaryDAOImpl();
        diaryDAO.updateDiary(diary);

        Toast.makeText(this, "修改成功！", Toast.LENGTH_SHORT).show();
        finish();
    }

    //删除
    public void delectDiary() {
        DiaryDAO diaryDAO = new DiaryDAOImpl();
        diaryDAO.deleteDiary(diary_id);
        finish();
    }

}
