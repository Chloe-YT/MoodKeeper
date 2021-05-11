package com.e.moodkeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.e.moodkeeper.R;
import com.e.moodkeeper.viewmodel.UserViewModel;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.SimpleDateFormat;
import java.util.Date;

import dao.DiaryDAO;
import dao.DiaryDAOImpl;
import pojo.Diary;
import pojo.User;

public class AddDiaryActivity extends AppCompatActivity implements View.OnClickListener {

    private User user;

    private UserViewModel userViewModel;

    private TextView cancelTv;
    private TextView saveTv;

    private EditText titleEt;
    private EditText contentEt;

    private MaterialSpinner chooseCategory;

    private int diary_id = 1;
    private int user_id = 1;
    private int mood_id = 1;
    private int weather_id = 1;
    private int category_id = 1;
    private String days;
    private String diary_title;
    private String diary_content;
    private Date diary_date = new Date();
    private Date anchor = new Date(0);
    //状态
    private int addState = 0;  //本地新增
    private int deleteState = -1;  //标记删除
    private int updateState = 1;  //本地更新

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);

        //获取心情、日期、天气id
        Intent intent = getIntent();

        Bundle bundle = intent.getBundleExtra("paraBundle");
        mood_id = bundle.getInt("mood_id");
        weather_id = bundle.getInt("weather_id");
        days = bundle.getString("diary_date");
        Log.d("AddDiaryActivity", String.valueOf(mood_id));
        Log.d("AddDiaryActivity", String.valueOf(weather_id));
        Log.d("AddDiaryActivity", days);

        // AndroidViewModel 初始化方式
        userViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(UserViewModel.class);
        // 观察者动态更新 UI
        userViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                // 登录成功
                if (user != null) {
                    user_id = user.getId();
                }
            }
        });

        cancelTv = (TextView) findViewById(R.id.cancel);
        cancelTv.setOnClickListener(this);

        saveTv = (TextView) findViewById(R.id.save);
        saveTv.setOnClickListener(this);

        titleEt = (EditText) findViewById(R.id.diary_title);

        contentEt = (EditText) findViewById(R.id.diary_content);

        chooseCategory = (MaterialSpinner)findViewById(R.id.choose_category);
        chooseCategory.setItems("生活", "学习", "办公", "娱乐", "其他");
        chooseCategory.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
//                Snackbar.make(view, "Clicked" + item, Snackbar.LENGTH_LONG).show();
                category_id = position + 1;
                Log.d("AddDiaryActivity", String.valueOf(category_id));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                Intent intent1 = new Intent(AddDiaryActivity.this, MainActivity.class);
                startActivityForResult(intent1, 1);
                break;
            case R.id.save:
                save();
                Intent intent2 = new Intent(AddDiaryActivity.this, MainActivity.class);
                startActivityForResult(intent2, 1);
                break;
        }
    }

    //保存
    public void save(){
        diary_title = titleEt.getText().toString();
        diary_content = contentEt.getText().toString();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            diary_date = sdf.parse(days);
        } catch (Exception e) {
        }

        Diary diary = new Diary(diary_id, user_id, mood_id, weather_id, category_id, diary_title, diary_content, diary_date, addState, anchor);
        DiaryDAO diaryDAO = new DiaryDAOImpl();
        diaryDAO.insertDiary(diary);

        Toast.makeText(this, "添加成功！", Toast.LENGTH_SHORT).show();
    }
}
