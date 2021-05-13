package com.e.moodkeeper.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    protected Toast toast;
    // 声明SharedPreferences对象
    SharedPreferences sp;
    // 声明SharedPreferences编辑器对象
    SharedPreferences.Editor editor;

    // Log打印的通用Tag
    private final String TAG = "LoginActivity";
    private String findPhone = "";

    private String password;

    private boolean isLogin = false;

    private EditText loginPhoneNumber;
    private EditText loginPassword;

    private TextView tvRegister;
    private TextView tvForgetPassword;
    private TextView loginTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        findPhone = intent.getStringExtra("telephone");

        loginPhoneNumber = findViewById(R.id.login_phone_number);
        loginPhoneNumber.setOnClickListener(this);
        loginPhoneNumber.setText(findPhone);

        loginPassword = findViewById(R.id.login_password);
        loginPassword.setOnClickListener(this);

        tvRegister = findViewById(R.id.tv_register);
        tvRegister.setOnClickListener(this);

        tvForgetPassword = findViewById(R.id.tv_forget_password);
        tvForgetPassword.setOnClickListener(this);

        loginTv = findViewById(R.id.login_tv);
        loginTv.setOnClickListener(this);

        /*
            当输入框焦点失去时,检验输入数据，提示错误信息
            第一个参数：输入框对象
            第二个参数：输入数据类型
            第三个参数：输入不合法时提示信息
         */
        setOnFocusChangeErrMsg(loginPhoneNumber, "phone", "手机号格式不正确");
        setOnFocusChangeErrMsg(loginPassword, "password", "密码必须不少于6位");

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        // 获取用户输入的账号和密码以进行验证
        String account = loginPhoneNumber.getText().toString();
        String password = loginPassword.getText().toString();

        switch (v.getId()) {
            case R.id.tv_register:
                //跳转注册页面
                Intent intent1 = new Intent(LoginActivity.this, RegisterActivity.class);
                intent1.putExtra("account", account);
                startActivity(intent1);
                break;
            case R.id.tv_forget_password:
                //忘记密码
                Intent intent2 = new Intent(LoginActivity.this, FindPasswordActivity.class);
                intent2.putExtra("phone", account);
                startActivity(intent2);
                break;
            case R.id.login_tv:
                //登录
                // 让密码输入框失去焦点,触发setOnFocusChangeErrMsg方法
                loginPassword.clearFocus();
                // 发送URL请求之前,先进行校验
                if (!(ValidUtils.isPhoneValid(account) && ValidUtils.isPasswordValid(password))) {
                    Toast.makeText(this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                    break;
                }
                asyncLoginWithXHttp2(account, password);
//                Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
//                startActivityForResult(intent2, 1);
                break;
        }
    }

    /*
    当账号输入框失去焦点时，校验账号是否是中国大陆手机号
    当密码输入框失去焦点时，校验密码是否不少于6位
    如有错误，提示错误信息
     */
    private void setOnFocusChangeErrMsg(EditText editText, String inputType, String errMsg) {
        editText.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        String inputStr = editText.getText().toString();
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

    private void asyncLoginWithXHttp2(String telephone, String password) {
        XHttp.post(NetConstant.getLoginURL())
                .params("telephone", telephone)
                .params("password", password)
                .params("type", "login")
                .syncRequest(false)
                .execute(new SimpleCallBack<User>() {
                    @Override
                    public void onSuccess(User user) throws Throwable {
//                        Log.d(TAG, "请求URL成功,登录成功");
//                        String encryptedPassword = ValidUtils.encodeByMd5(password);
//                        sp = getSharedPreferences("login_info", MODE_PRIVATE);
//                        editor = sp.edit();
//                        editor.putString("telephone", telephone);
//                        editor.putString("encryptedPassword", encryptedPassword);
//
//                        if (editor.commit()) {
//                            Intent it_login_to_main = new Intent(LoginActivity.this, MainActivity.class);
//                            startActivity(it_login_to_main);
//                            // 登录成功后，登录界面就没必要占据资源了
//                            finish();
//                        } else {
//                            showToastInThread(LoginActivity.this, "账号密码保存失败，请重新登录");
//                        }

                        Log.d(TAG, "请求URL成功,登录成功");
                        // 更新本地用户信息，用于下次打开的自动登录
                        SharedPreferencesUtils2 sp = new SharedPreferencesUtils2(LoginActivity.this, ModelConstant.LOGIN_INFO);
                        sp.putObject(ModelConstant.KEY_LOGIN_USER, user);

                        Intent it_login_to_main = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(it_login_to_main);

                        // 登录成功后，登录界面就没必要占据资源了
                        finish();
                    }

                    @Override
                    public void onError(ApiException e) {
                        Log.d(TAG, "请求URL失败： " + e.getMessage());
                        showToastInThread(LoginActivity.this, e.getMessage());
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
