package com.e.moodkeeper.application;

import android.app.Application;

import com.e.moodkeeper.constant.NetConstant;
import com.xuexiang.xhttp2.XHttpSDK;

public class App extends Application {

    private boolean isDebug = true;

    @Override
    public void onCreate() {
        super.onCreate();

        XHttpSDK.init(this);   //初始化网络请求框架，必须首先执行
        XHttpSDK.setBaseUrl(NetConstant.baseService);  //设置网络请求的基础地址

    }
}
