package com.e.moodkeeper.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import pojo.OtpCode;
import pojo.User;
import util.SharedPreferencesUtils2;
import util.ValidUtils;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    // Log打印的通用Tag
    private final String TAG = "RegisterActivity";

    protected Toast toast;

    private String account = "";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private ImageView backToLogin;

    private EditText registerPhoneNumber;
    private EditText registerOptCode;
    private EditText registerName;
    private EditText registerGender;
    private EditText registerAge;
    private EditText registerPassword;
    private EditText registerPassword2;

    private TextView registerTv;
    private TextView getOptTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        // 接收用户在登录界面输入的数据，简化用户操作（如果输入过了就不用再输入了）
        // 注意接收上一个页面 Intent 的信息，需要 getIntent() 即可，而非重新 new 一个 Intent
        Intent intent = getIntent();
        account = intent.getStringExtra("account");

        registerPhoneNumber = findViewById(R.id.register_phone_number);
        registerPhoneNumber.setText(account);

        registerOptCode = findViewById(R.id.otp_code);
        registerName = findViewById(R.id.register_name);
        registerGender = findViewById(R.id.register_gender);
        registerAge = findViewById(R.id.register_age);
        registerPassword = findViewById(R.id.register_password);
        registerPassword2 = findViewById(R.id.register_password2);

        backToLogin = findViewById(R.id.back_iv);
        backToLogin.setOnClickListener(this);

        getOptTv = findViewById(R.id.get_otp_tv);
        getOptTv.setOnClickListener(this);

        registerTv = findViewById(R.id.register_tv);
        registerTv.setOnClickListener(this);

        setOnFocusChangeErrMsg(registerPhoneNumber, "phone", "手机号格式不正确");
        setOnFocusChangeErrMsg(registerPassword, "password", "密码必须不少于6位");
        setOnFocusChangeErrMsg(registerGender, "gender", "性别只能填1或2");
    }

    @Override
    public void onClick(View v) {
        String telephone = registerPhoneNumber.getText().toString();
        String otpCode = registerOptCode.getText().toString();
        String username = registerName.getText().toString();
        String gender = registerGender.getText().toString();
        String age = registerAge.getText().toString();
        String password1 = registerPassword.getText().toString();
        String password2 = registerPassword2.getText().toString();

        switch (v.getId()) {
            case R.id.get_otp_tv:
                // 点击获取验证码按钮响应事件
                if (TextUtils.isEmpty(telephone)) {
                    Toast.makeText(RegisterActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (ValidUtils.isPhoneValid(telephone)) {
//                      asyncGetOtpCode(telephone);
                        asyncGetOtpCodeWithXHttp2(telephone);
                    } else {
                        Toast.makeText(RegisterActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.register_tv:
                //注册
//                asyncRegister(telephone, otpCode, username, gender, age, password1, password2);
                // 点击提交注册按钮响应事件
                // 尽管后端进行了判空，但Android端依然需要判空
                asyncRegisterWithXHttp2(telephone, otpCode, username, gender, age, password1, password2);
//                Intent intent1 = new Intent(RegisterActivity.this, ActivityLogin.class);
//                startActivityForResult(intent1, 1);
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

                                case "gender":
                                    if (!ValidUtils.isGenderValid(inputStr)) {
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

    private void asyncGetOtpCodeWithXHttp2(String telephone) {
        XHttp.post(NetConstant.getGetOtpCodeURL())
                .params("telephone", telephone)
                .syncRequest(false)
                .execute(new SimpleCallBack<OtpCode>() {
                    @Override
                    public void onSuccess(OtpCode data) throws Throwable {
                        Log.d(TAG, "请求URL成功： " + data);
                        if (data != null) {
                            String otpCode = data.getOtpCode();
                            // 自动填充验证码
                            setTextInThread(registerOptCode, otpCode);
                            // 在子线程中显示Toast
                            showToastInThread(RegisterActivity.this, "验证码：" + otpCode);
                            Log.d(TAG, "telephone: " + telephone + " otpCode: " + otpCode);
                        }
                        Log.d(TAG, "验证码已发送，注意查收！");
                    }

                    @Override
                    public void onError(ApiException e) {
                        Log.d(TAG, "请求URL异常： " + e.toString());
                        showToastInThread(RegisterActivity.this, e.getMessage());
                    }
                });
    }

    /* 在子线程中更新UI ，实现自动填充验证码 */
    private void setTextInThread(EditText editText, String otpCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                editText.setText(otpCode);
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

    private void asyncRegisterWithXHttp2(final String telephone,
                                         final String otpCode,
                                         final String username,
                                         final String gender,
                                         final String age,
                                         final String password1,
                                         final String password2) {
        // 非空校验
        if (TextUtils.isEmpty(telephone) || TextUtils.isEmpty(otpCode) || TextUtils.isEmpty(username)
                || TextUtils.isEmpty(gender) || TextUtils.isEmpty(age)
                || TextUtils.isEmpty(password1) || TextUtils.isEmpty(password2)) {
            Toast.makeText(RegisterActivity.this, "存在输入为空，注册失败", Toast.LENGTH_SHORT).show();
            return;
        }

        // 密码一致校验
        if (!TextUtils.equals(password1, password2)) {
            Toast.makeText(RegisterActivity.this, "两次密码不一致，注册失败", Toast.LENGTH_SHORT).show();
            return;
        }

        // 注册
        XHttp.post(NetConstant.getRegisterURL())
                .params("telephone", telephone)
                .params("otpCode", otpCode)
                .params("name", username)
                .params("gender", gender)
                .params("age", age)
                .params("password", password1)
                .syncRequest(false)
                .execute(new SimpleCallBack<User>() {
                    @Override
                    public void onSuccess(User user) throws Throwable {
//                        // 注册成功，用户名与密码
//                        sp = getSharedPreferences(ModelConstant.LOGIN_INFO, MODE_PRIVATE);
//                        editor = sp.edit();
//                        editor.putString("token", "token_value");
//                        editor.putString("telephone", telephone);
//                        String encryptedPassword = ValidUtils.encodeByMd5(password1);
//                        editor.putString("encryptedPassword", encryptedPassword);
//
//                        if (editor.commit()) {
//                            Intent intentToMain = new Intent(RegisterActivity.this, MainActivity.class);
//                            startActivity(intentToMain);
//                            // 注册成功后，注册界面就没必要占据资源了
//                            finish();
//                        } else {
//                            showToastInThread(RegisterActivity.this, "保存密码到本地失败！");
//                        }

                        // 注册成功，保存用户信息到本地
                        SharedPreferencesUtils2 sp = new SharedPreferencesUtils2(RegisterActivity.this, ModelConstant.LOGIN_INFO);
                        sp.putObject(ModelConstant.KEY_LOGIN_USER, user);
                        Intent intentToMain = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intentToMain);
                        finish();
                    }

                    @Override
                    public void onError(ApiException e) {
                        Log.d(TAG, "请求URL异常： " + e.toString());
                        showToastInThread(RegisterActivity.this, e.getMessage());
                    }
                });
    }


}
