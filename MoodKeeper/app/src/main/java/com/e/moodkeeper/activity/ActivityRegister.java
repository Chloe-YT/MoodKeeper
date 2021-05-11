package com.e.moodkeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.e.moodkeeper.R;

public class ActivityRegister extends AppCompatActivity implements View.OnClickListener {

    private ImageView backToLogin;

    private EditText registerPhoneNumber;
    private EditText registerName;
    private EditText registerPassword;

    private TextView registerTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        backToLogin = findViewById(R.id.back_iv);
        backToLogin.setOnClickListener(this);

        registerPhoneNumber = findViewById(R.id.register_phone_number);
        registerName = findViewById(R.id.register_name);
        registerPassword = findViewById(R.id.register_password);

        registerTv = findViewById(R.id.register_tv);
        registerTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_tv:
                //注册
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(ActivityRegister.this, ActivityLogin.class);
                startActivityForResult(intent1, 1);
        }
    }
}
