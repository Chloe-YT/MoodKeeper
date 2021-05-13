package com.e.moodkeeper.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.e.moodkeeper.R;
import com.e.moodkeeper.constant.ModelConstant;
import com.e.moodkeeper.constant.NetConstant;
import com.e.moodkeeper.viewmodel.UserViewModel;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.SimpleCallBack;
import com.xuexiang.xhttp2.exception.ApiException;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import pojo.User;
import util.SharedPreferencesUtils2;

public class MessageUpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private UserViewModel userViewModel;

    protected Toast toast;
    // Log打印的通用Tag
    private final String TAG = "MessageUpdateActivity";

    private ImageView backIv;
    private ImageView backHeadSculpture;
    private ImageView frontHeadSculpture;

    private EditText editNameEt;
    private EditText editGenderEt;
    private EditText editAgeEt;

    private RelativeLayout editPasswordRl;

    private TextView userPhoneTv;
    private TextView saveEditTv;

    private int id;
    private String telephone;
    private String username;
    private String gender;
    private String age;
    private String password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_personalmessage);

        // AndroidViewModel 初始化方式
        userViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(UserViewModel.class);

        backIv = findViewById(R.id.back_iv1);
        backIv.setOnClickListener(this);

        backHeadSculpture = findViewById(R.id.back_head1);
        frontHeadSculpture = findViewById(R.id.front_head1);

        Glide.with(this).load(R.drawable.ic_head)
                .bitmapTransform(new BlurTransformation(this, 25), new CenterCrop(this))
                .into(backHeadSculpture);
        Glide.with(this).load(R.drawable.ic_head)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(frontHeadSculpture);

        editNameEt = findViewById(R.id.edit_name);
        editGenderEt = findViewById(R.id.edit_gender);
        editAgeEt = findViewById(R.id.edit_age);

        editPasswordRl = findViewById(R.id.edit_password_rl);
        editPasswordRl.setOnClickListener(this);

        userPhoneTv = findViewById(R.id.user_phone_tv1);

        saveEditTv = findViewById(R.id.save_edit);
        saveEditTv.setOnClickListener(this);

        // 观察者动态更新 UI
        userViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                // 登录成功
                if (user != null) {
                    id = user.getId();
                    telephone = user.getTelephone();
                    userPhoneTv.setText(user.getTelephone());
                    editNameEt.setText(user.getName());
                    editAgeEt.setText(String.valueOf(user.getAge()));
                    if (user.getGender() == 1) {
                        editGenderEt.setText("男");
                    } else if (user.getGender() == 2) {
                        editGenderEt.setText("女");
                    }
                }
                // 未登录
                else {
                    userPhoneTv.setText("");
                    editNameEt.setText("");
                    editGenderEt.setText("");
                    editAgeEt.setText("");
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        username = editNameEt.getText().toString();
        gender = editGenderEt.getText().toString();
        age = editAgeEt.getText().toString();

        if (TextUtils.equals(editGenderEt.getText().toString(), "男")) {
            gender = "1";
        } else if (TextUtils.equals(editGenderEt.getText().toString(), "女")) {
            gender = "2";
        }

        switch (v.getId()) {
            case R.id.back_iv1:
                Intent intent1 = new Intent(MessageUpdateActivity.this,MineActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.edit_password_rl:
                //显示修改密码对话框
                showEditDialog();
                break;
            case R.id.save_edit:
                //保存修改
                asyncUpdateMessageWithXHttp2(id, telephone, username, gender, age, password);
                break;
        }
    }

    public void showEditDialog() {
        LayoutInflater factory = LayoutInflater.from(MessageUpdateActivity.this);
        View textEntryView = factory.inflate(R.layout.edit_password_dialog, null);
        EditText editPassword1 = (EditText) textEntryView.findViewById(R.id.edit_password1);
        EditText editPassword2 = (EditText)textEntryView.findViewById(R.id.edit_password2);

        new AlertDialog.Builder(this)
                .setTitle("修改密码")
                .setCancelable(false)
                .setView(textEntryView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(editPassword1.getText().toString())) {
                            Toast.makeText(MessageUpdateActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            dialog = null;
                            return;
                        } else if (TextUtils.isEmpty(editPassword2.getText().toString())) {
                            Toast.makeText(MessageUpdateActivity.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            dialog = null;
                            return;
                        } else {
                            // 密码一致校验
                            if (!TextUtils.equals(editPassword1.getText().toString(), editPassword2.getText().toString())) {
                                Toast.makeText(MessageUpdateActivity.this, "两次密码不一致，修改失败", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                dialog = null;
                                return;
                            }
                            if(editPassword1.getText().toString().trim().length() <= 5) {
                                Toast.makeText(MessageUpdateActivity.this, "密码必须不少于6位", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                dialog = null;
                                return;
                            }
                            password = editPassword1.getText().toString();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //取消
                        dialog.dismiss();
                        dialog = null;
                    }
                })
                .show();
    }

    private void asyncUpdateMessageWithXHttp2(final int id,
                                         final String telephone,
                                         final String username,
                                         final String gender,
                                         final String age,
                                         final String password) {

        // 修改信息
        XHttp.post(NetConstant.getUpdateMessageURL())
                .params("id",id)
                .params("telephone", telephone)
                .params("name", username)
                .params("gender", gender)
                .params("age", age)
                .params("password", password)
                .syncRequest(false)
                .execute(new SimpleCallBack<User>() {
                    @Override
                    public void onSuccess(User user) throws Throwable {
                        // 修改成功，保存用户信息到本地
                        SharedPreferencesUtils2 sp = new SharedPreferencesUtils2(MessageUpdateActivity.this, ModelConstant.LOGIN_INFO);
                        sp.putObject(ModelConstant.KEY_LOGIN_USER, user);
                        Intent intentToMine = new Intent(MessageUpdateActivity.this, MineActivity.class);
                        startActivity(intentToMine);
                        finish();
                    }

                    @Override
                    public void onError(ApiException e) {
                        Log.d(TAG, "请求URL异常： " + e.toString());
                        showToastInThread(MessageUpdateActivity.this, e.getMessage());
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
