package com.e.moodkeeper.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.e.moodkeeper.constant.ModelConstant;
import com.e.moodkeeper.constant.NetConstant;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.SimpleCallBack;
import com.xuexiang.xhttp2.exception.ApiException;

import util.SharedPreferencesUtils;

public class JudgeActivity extends AppCompatActivity {

    // Log打印的通用Tag
    private final String TAG = "JudgeActivity";

    private boolean isLogin = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        autoLogin();
        checkToJump();

    }

    private void autoLogin() {
        SharedPreferences sp = getSharedPreferences(ModelConstant.LOGIN_INFO, MODE_PRIVATE);
        String telephoneInSP = sp.getString("telephone", "");
        String passwordInSP = sp.getString("encryptedPassword", "");
        // 异步登录
        // asyncValidate(telephoneInSP, passwordInSP);
        asyncValidateWithXHttp2(telephoneInSP, passwordInSP);
    }

    // 首次打开程序判断
    private void checkToJump() {
        boolean isFirstLogin = SharedPreferencesUtils.getBoolean(JudgeActivity.this, ModelConstant.FIRST_LOGIN, true);
        // 首次打开，进入登录注册页
        if (!isFirstLogin) {
            if (isLogin) {
                startActivity(new Intent(JudgeActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(JudgeActivity.this, LoginActivity.class));
            }

        }
        // 非首次打开，登录回调判断
        else {
            Intent it_to_guide = new Intent(JudgeActivity.this, LoginActivity.class);
            startActivity(it_to_guide);
            SharedPreferencesUtils.putBoolean(JudgeActivity.this, ModelConstant.FIRST_LOGIN, false);
        }
        finish();
    }

    // 异步登录
    private void asyncValidateWithXHttp2(String account, String password) {
        XHttp.post(NetConstant.getLoginURL())
                .params("telephone", account)
                .params("password", password)
                .params("type", "autoLogin")
                .syncRequest(false)
                .execute(new SimpleCallBack<Object>() {
                    @Override
                    public void onSuccess(Object data) throws Throwable {
                        isLogin = true;
                        Log.d(TAG, "请求URL成功,自动登录成功");
                    }

                    @Override
                    public void onError(ApiException e) {
                        Log.d(TAG, "请求URL异常,自动登录失败" + e.toString());
//                        showToastInThread(CountDownActivity.this, e.getMessage());
                    }
                });
    }

}
