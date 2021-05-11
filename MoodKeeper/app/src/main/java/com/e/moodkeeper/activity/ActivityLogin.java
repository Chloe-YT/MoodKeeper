package com.e.moodkeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.e.moodkeeper.R;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {

    private EditText loginPhoneNumber;
    private EditText loginPassword;

    private TextView tvRegister;
    private TextView tvForgetPassword;
    private TextView loginTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginPhoneNumber = findViewById(R.id.login_phone_number);
        loginPassword = findViewById(R.id.login_password);

        tvRegister = findViewById(R.id.tv_register);
        tvRegister.setOnClickListener(this);

        tvForgetPassword = findViewById(R.id.tv_forget_password);
        tvForgetPassword.setOnClickListener(this);

        loginTv = findViewById(R.id.login_tv);
        loginTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register:
                Intent intent1 = new Intent(ActivityLogin.this, ActivityRegister.class);
                startActivityForResult(intent1, 1);
                break;
            case R.id.tv_forget_password:
                //忘记密码
                break;
            case R.id.login_tv:
                //登录
                Intent intent2 = new Intent(ActivityLogin.this, MainActivity.class);
                startActivityForResult(intent2, 1);
                break;
        }
    }
}
