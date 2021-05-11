package com.e.moodkeeper.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.e.moodkeeper.constant.ModelConstant;

import pojo.User;
import util.SharedPreferencesUtils2;

public class UserViewModel extends AndroidViewModel {
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public UserViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<User> getUser() {
        // 在 ViewModel 中获取数据
        SharedPreferencesUtils2 sp = new SharedPreferencesUtils2(getApplication(), ModelConstant.LOGIN_INFO);
        User user = (User) sp.getObject(ModelConstant.KEY_LOGIN_USER, User.class);
        userLiveData.setValue(user);
        return userLiveData;
    }


    void doAction() {
        // depending on the action, do necessary business logic calls and update the
        // userLiveData.
    }

}
