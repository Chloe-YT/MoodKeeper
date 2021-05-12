package com.e.moodkeeper.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.e.moodkeeper.R;
import com.e.moodkeeper.constant.ModelConstant;
import com.e.moodkeeper.viewmodel.UserViewModel;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import pojo.User;
import util.SharedPreferencesUtils2;
import util.SyncUtil;

import static util.SyncUtil.HOST_IP;

public class MineActivity extends AppCompatActivity implements View.OnClickListener {

    private UserViewModel userViewModel;
    private static SharedPreferences preferences;

    public static final MediaType MEDIA_TYPE_JSON
            = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .build();

    private ImageView backIv;
    private ImageView backHeadSculpture;
    private ImageView frontHeadSculpture;

    private TextView userName;
    private TextView userPhone;
    private TextView logoutTv;
    private TextView personalMessageTv;
    private TextView recoverDataTv;
    private TextView updateDataTv;

    private RelativeLayout personalMessage;
    private RelativeLayout recoverData;
    private RelativeLayout updateData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);

        // AndroidViewModel 初始化方式
        userViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(UserViewModel.class);

        backIv = findViewById(R.id.back_iv);
        backIv.setOnClickListener(this);

        backHeadSculpture = findViewById(R.id.back_head);
        frontHeadSculpture = findViewById(R.id.front_head);

        Glide.with(this).load(R.drawable.ic_head)
                .bitmapTransform(new BlurTransformation(this, 25), new CenterCrop(this))
                .into(backHeadSculpture);
        Glide.with(this).load(R.drawable.ic_head)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(frontHeadSculpture);

        userName = findViewById(R.id.user_name);
        userName.setOnClickListener(this);

        userPhone = findViewById(R.id.user_phone);
        userPhone.setOnClickListener(this);

        personalMessageTv = findViewById(R.id.personal_message_tv);
        TextPaint textPaint1 = personalMessageTv.getPaint();
        textPaint1.setFakeBoldText(true);

        recoverDataTv = findViewById(R.id.recover_data_tv);
        TextPaint textPaint2 = recoverDataTv.getPaint();
        textPaint2.setFakeBoldText(true);

        updateDataTv = findViewById(R.id.update_data_tv);
        TextPaint textPaint3 = updateDataTv.getPaint();
        textPaint3.setFakeBoldText(true);

        personalMessage = findViewById(R.id.personal_message);
        personalMessage.setOnClickListener(this);

        recoverData = findViewById(R.id.recover_data_rl);
        recoverData.setOnClickListener(this);

        updateData = findViewById(R.id.update_data_rl);
        updateData.setOnClickListener(this);

        logoutTv = findViewById(R.id.logout);
        logoutTv.setOnClickListener(this);

        // 观察者动态更新 UI
        userViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                // 登录成功
                if (user != null) {
//                    mBinding.btnExit.setVisibility(View.VISIBLE);
//                    mBinding.btnLogin.setVisibility(View.GONE);
                    userName.setText(user.getName());
                    userPhone.setText(user.getTelephone());
                }
                // 未登录
                else {
//                    mBinding.btnExit.setVisibility(View.GONE);
//                    mBinding.btnLogin.setVisibility(View.VISIBLE);
                    userName.setText("");
                    userPhone.setText("");
                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.logout:
                // 清空本地缓存信息
                SharedPreferencesUtils2 sp = new SharedPreferencesUtils2(this, ModelConstant.LOGIN_INFO);
                sp.putObject(ModelConstant.KEY_LOGIN_USER, null);
                // 动态更新UI
                userViewModel.getUser().setValue(null);
                //跳转登录页
                Intent intent1 = new Intent(MineActivity.this, LoginActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.personal_message:
                Intent intent2 = new Intent(MineActivity.this, MessageUpdateActivity.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.recover_data_rl:
                Toast.makeText(this, "You click 2", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(this).setTitle("系统提示")//设置对话框标题
                        .setMessage("将使用服务器数据覆盖本地数据，是否继续?")//设置显示的内容
                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                Toast.makeText(MineActivity.this,"开始恢复...",Toast.LENGTH_SHORT).show();
                                postRecoverRequest();
                            }
                        }).setNegativeButton("取消",new DialogInterface.OnClickListener() {//添加返回按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//响应事件

                    }
                }).show();//在按键响应事件中显示此对话框

                break;
            case R.id.update_data_rl:
                Toast.makeText(this,"同步中...请稍候",Toast.LENGTH_SHORT).show();
                postDownloadRequest();
                break;
        }
    }

    /**
     * 向服务器发送恢复数据请求
     */
    private void postRecoverRequest(){
        JSONObject dateObject=new JSONObject();
        dateObject.put("Diary",new Date(0));

        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON,
                dateObject.toJSONString());

        //这里的主机地址要填电脑的ip地址 ,token要填用户登录时获取的token，超过一定时间会失效，
        // 需要重新获取
        Request request = new Request.Builder()
                .url("http://"+HOST_IP+":8082/app/download")
                .post(requestBody)
                .addHeader("token", preferences.getString("token",""))
                .build();

        Log.d("发送恢复数据请求","开始尝试获取当前用户在服务器端的所有数据");

        //使用异步请求（用同步发送请求需要在子线程上发起，因为安卓较新的版本都不允许在主线程发送网络请求）
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(e instanceof SocketTimeoutException){//判断超时异常
                    Looper.prepare();
                    Toast.makeText(MineActivity.this,"下载时出错：网络连接超时",
                            Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    e.printStackTrace();
                }
                if(e instanceof ConnectException){//判断连接异常，我这里是报Failed to connect to 10.7.5.144
                    Looper.prepare();
                    Toast.makeText(MineActivity.this,"下载时出错：连接到服务器失败",
                            Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    e.printStackTrace();
                }
                else{
                    Looper.prepare();
                    Toast.makeText(MineActivity.this,"下载时出错：其他错误",
                            Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    e.printStackTrace();
                }

            }


            @Override
            public void onResponse(Call call, Response response)  {
                try (ResponseBody responseBody = response.body()) {
                    String responseString=responseBody.string();
                    JSONObject resultJson=JSONObject.parseObject(responseString);
                    int statusCode=resultJson.getInteger("code");
                    String message=resultJson.getString("msg");

                    //如果响应结果状态码为成功的
                    if(statusCode>=200 && statusCode<400) {
                        //取出返回的数据；更新本地记录状态
                        JSONObject dataJson = resultJson.getJSONObject("data");

                        Log.d("处理恢复数据请求","下载数据成功，开始重置数据库并进行恢复数据");
                        SyncUtil.deleteAllRecords();
                        SyncUtil.processDownloadResult(dataJson);
                        //syncTextView.setText("更新数据（上次同步时间："+new Date()+")");

                        Looper.prepare();
                        Toast.makeText(MineActivity.this,"恢复数据成功",
                                Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                    //响应结果为失败类型
                    else{
                        String errorMessage="响应状态码："+statusCode+",错误信息："+message+'\n';
                        Looper.prepare();
                        Toast.makeText(MineActivity.this,"下载服务器记录失败\n"+errorMessage,
                                Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } catch (IOException e) {
                    Looper.prepare();
                    Toast.makeText(MineActivity.this,"转换响应结果为字符串时出错",Toast.LENGTH_SHORT)
                            .show();
                    Looper.loop();
                    e.printStackTrace();
                }
                catch (Exception e){
                    Looper.prepare();
                    Toast.makeText(MineActivity.this,"其他错误，详细信息见控制台",Toast.LENGTH_SHORT)
                            .show();
                    Looper.loop();
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 向服务器发起请求，获取待下载数据并对本地数据库执行相应更新
     */
    private void postDownloadRequest(){
        JSONObject lastUpdateDates= SyncUtil.getTableLastUpdateTime();
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON,
                lastUpdateDates.toJSONString());

        //这里的主机地址要填电脑的ip地址 ,token要填用户登录时获取的token，超过一定时间会失效，
        // 需要重新获取
        Request request = new Request.Builder()
                .url("http://"+HOST_IP+":8082/app/download")
                .post(requestBody)
                .addHeader("token", preferences.getString("token",""))
                .build();

        Log.d("发送下载请求","开始尝试下载");

        //使用异步请求（用同步发送请求需要在子线程上发起，因为安卓较新的版本都不允许在主线程发送网络请求）
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(e instanceof SocketTimeoutException){//判断超时异常
                    Looper.prepare();
                    Toast.makeText(MineActivity.this,"下载时出错：网络连接超时",
                            Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    e.printStackTrace();
                }
                if(e instanceof ConnectException){//判断连接异常，我这里是报Failed to connect to 10.7.5.144
                    Looper.prepare();
                    Toast.makeText(MineActivity.this,"下载时出错：连接到服务器失败",
                            Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    e.printStackTrace();
                }
                else{
                    Looper.prepare();
                    Toast.makeText(MineActivity.this,"下载时出错：其他错误",
                            Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    e.printStackTrace();
                }

            }


            @Override
            public void onResponse(Call call, Response response)  {
                try (ResponseBody responseBody = response.body()) {
                    String responseString=responseBody.string();
                    JSONObject resultJson=JSONObject.parseObject(responseString);
                    int statusCode=resultJson.getInteger("code");
                    String message=resultJson.getString("msg");

                    //如果响应结果状态码为成功的
                    if(statusCode>=200 && statusCode<400) {
                        //取出返回的数据；更新本地记录状态
                        JSONObject dataJson = resultJson.getJSONObject("data");

                        SyncUtil.processDownloadResult(dataJson);

                        Log.d("处理下载请求","下载成功，开始执行上传");
                        postUploadRequest();

                        //syncTextView.setText("更新数据（上次同步时间："+new Date()+")");

                    }
                    //响应结果为失败类型
                    else{
                        String errorMessage="响应状态码："+statusCode+",错误信息："+message+'\n';
                        Looper.prepare();
                        Toast.makeText(MineActivity.this,"下载服务器待同步记录失败\n"+errorMessage,
                                Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } catch (IOException e) {
                    Looper.prepare();
                    Toast.makeText(MineActivity.this,"转换响应结果为字符串时出错",Toast.LENGTH_SHORT)
                            .show();
                    Looper.loop();
                    e.printStackTrace();
                }
                catch (Exception e){
                    Looper.prepare();
                    Toast.makeText(MineActivity.this,"其他错误，详细信息见控制台",Toast.LENGTH_SHORT)
                            .show();
                    Looper.loop();
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 执行上传任务
     */
    private void postUploadRequest(){
        JSONObject syncRecordsJsonObject= SyncUtil.getAllSyncRecords();
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON,
                syncRecordsJsonObject.toJSONString());

        //这里的主机地址要填电脑的ip地址 ,token要填用户登录时获取的token，超过一定时间会失效，
        // 需要重新获取
        Request request = new Request.Builder()
                .url("http://"+HOST_IP+":8080/app/synchronization")
                .post(requestBody)
                .addHeader("token", preferences.getString("token",""))
                .build();

        Log.d("发送上传请求","发送了上传请求");

        //使用异步请求（用同步发送请求需要在子线程上发起，因为安卓较新的版本都不允许在主线程发送网络请求）
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(e instanceof SocketTimeoutException){//判断超时异常
                    Looper.prepare();
                    Toast.makeText(MineActivity.this,"上传时出错：网络连接超时",
                            Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    e.printStackTrace();
                }
                if(e instanceof ConnectException){//判断连接异常，我这里是报Failed to connect to 10.7.5.144
                    Looper.prepare();
                    Toast.makeText(MineActivity.this,"上传时出错：连接到服务器失败",
                            Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    e.printStackTrace();
                }
                else{
                    Looper.prepare();
                    Toast.makeText(MineActivity.this,"上传时出错：其他错误",
                            Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    e.printStackTrace();
                }

            }


            @Override
            public void onResponse(Call call, Response response)  {
                try (ResponseBody responseBody = response.body()) {
                    String responseString=responseBody.string();
                    JSONObject resultJson=JSONObject.parseObject(responseString);
                    int statusCode=resultJson.getInteger("code");
                    String message=resultJson.getString("msg");
                    Log.d("处理上传相应","接收到了上传响应结果，状态码为"+statusCode);

                    //如果响应结果状态码为成功的
                    if(statusCode>=200 && statusCode<400) {
                        //取出返回的数据；更新本地记录状态
                        JSONObject dataJson = resultJson.getJSONObject("data");
                        Log.d("处理上传响应","响应结果为成功（200），开始更新本地记录同步状态");
                        SyncUtil.processUploadResult(dataJson);


                        Looper.prepare();
                        Toast.makeText(MineActivity.this,"同步成功！",Toast.LENGTH_LONG).show();
                        Looper.loop();

                        Log.d("处理上传响应","!!!更新本地记录同步状态完成，同步成功！");
                    }
                    //响应结果为失败类型
                    else{
                        String errorMessage="响应状态码："+statusCode+",错误信息："+message+'\n';
                        Looper.prepare();
                        Toast.makeText(MineActivity.this,"上传本地记录失败\n"+errorMessage,
                                Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } catch (IOException e) {
                    Looper.prepare();
                    Toast.makeText(MineActivity.this,"转换响应结果为字符串时出错",Toast.LENGTH_SHORT)
                            .show();
                    Looper.loop();
                    e.printStackTrace();
                }
            }
        });
    }

}
