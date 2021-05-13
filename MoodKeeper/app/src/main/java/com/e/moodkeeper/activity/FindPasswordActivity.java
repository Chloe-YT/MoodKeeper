package com.e.moodkeeper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.e.moodkeeper.R;
import com.e.moodkeeper.constant.ModelConstant;
import com.e.moodkeeper.constant.NetConstant;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.SimpleCallBack;
import com.xuexiang.xhttp2.exception.ApiException;

import pojo.User;
import util.SharedPreferencesUtils2;
import util.ValidUtils;

public class FindPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    // Log打印的通用Tag
    private final String TAG = "FindPasswordActivity";

    protected Toast toast;

    private String account = "";
    private String telephone;
    private String password1;
    private String password2;

    private ImageView backTologin;

    private EditText findPhone;
    private EditText findPassword1;
    private EditText findPassword2;

    private TextView findPasswordTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        // 接收用户在登录界面输入的数据，简化用户操作（如果输入过了就不用再输入了）
        // 注意接收上一个页面 Intent 的信息，需要 getIntent() 即可，而非重新 new 一个 Intent
        Intent intent = getIntent();
        account = intent.getStringExtra("phone");

        backTologin = findViewById(R.id.back_to_login);
        backTologin.setOnClickListener(this);

        findPhone = findViewById(R.id.find_phone_number);
        findPhone.setText(account);

        findPassword1 = findViewById(R.id.find_password1);
        findPassword2 = findViewById(R.id.find_password2);

        findPasswordTv = findViewById(R.id.find_password_tv);
        findPasswordTv.setOnClickListener(this);

        setOnFocusChangeErrMsg(findPhone, "phone", "手机号格式不正确");
        setOnFocusChangeErrMsg(findPassword1, "password", "密码必须不少于6位");


    }

    @Override
    public void onClick(View v) {
        telephone = findPhone.getText().toString();
        password1 = findPassword1.getText().toString();
        password2 = findPassword2.getText().toString();

        switch (v.getId()) {
            case R.id.back_to_login:
                finish();
                break;
            case R.id.find_password_tv:
                asyncFindWithXHttp2(telephone, password1, password2);
                break;
        }
    }

    /*
    当输入账号FocusChange时，校验账号是否是中国大陆手机号
    当输入密码FocusChange时，校验密码是否不少于6位
     */
    private void setOnFocusChangeErrMsg(EditText editText, String inputType, String errMsg) {
        editText.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        String inputStr = editText.getText().toString();
                        // 失去焦点
                        if (!hasFocus) {
                            switch (inputType) {
                                case "phone":
                                    if (!ValidUtils.isPhoneValid(inputStr)) {
                                        editText.setError(errMsg);
                                    }
                                    break;

                                case "password":
                                    if (!ValidUtils.isPasswordValid(inputStr)) {
                                        editText.setError(errMsg);
                                    }
                                    break;

                                default:
                                    break;
                            }
                        }
                    }
                }
        );
    }

    private void asyncFindWithXHttp2(final String telephone,
                                     final String password1,
                                     final String password2) {
        // 非空校验
        if (TextUtils.isEmpty(telephone) || TextUtils.isEmpty(password1) || TextUtils.isEmpty(password2)) {
            Toast.makeText(FindPasswordActivity.this, "存在输入为空，找回密码失败", Toast.LENGTH_SHORT).show();
            return;
        }

        // 密码一致校验
        if (!TextUtils.equals(password1, password2)) {
            Toast.makeText(FindPasswordActivity.this, "两次密码不一致，找回密码失败", Toast.LENGTH_SHORT).show();
            return;
        }

        // 注册
        XHttp.post(NetConstant.getFindPasswordURL())
                .params("telephone", telephone)
                .params("password", password1)
                .syncRequest(false)
                .execute(new SimpleCallBack<User>() {
                    @Override
                    public void onSuccess(User user) throws Throwable {
                        // 找回密码成功，跳转登录页
//                        SharedPreferencesUtils2 sp = new SharedPreferencesUtils2(FindPasswordActivity.this, ModelConstant.LOGIN_INFO);
//                        sp.putObject(ModelConstant.KEY_LOGIN_USER, user);
                        Intent intentToLogin = new Intent(FindPasswordActivity.this, LoginActivity.class);
                        intentToLogin.putExtra("telephone", telephone);
                        startActivity(intentToLogin);
                        finish();
                    }

                    @Override
                    public void onError(ApiException e) {
                        Log.d(TAG, "请求URL异常： " + e.toString());
                        showToastInThread(FindPasswordActivity.this, e.getMessage());
                    }
                });
    }

    // 实现在子线程中显示Toast
    protected void showToastInThread(Context context, String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

}
