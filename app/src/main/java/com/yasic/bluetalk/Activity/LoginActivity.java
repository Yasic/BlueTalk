package com.yasic.bluetalk.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushService;
import com.yasic.bluetalk.R;
import com.yasic.bluetalk.Utils.AsyncHttpUtils;
import com.yasic.bluetalk.Utils.DeveloperUtils;
import com.yasic.bluetalk.Utils.EditTextUtils;
import com.yasic.bluetalk.Utils.UserUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ESIR on 2016/2/24.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * 邮箱信息输入框
     */
    private EditText etEmailInfo;

    /**
     * 密码输入框
     */
    private EditText etPasswordInfo;

    /**
     * 登陆按钮
     */
    private ButtonRectangle btrLogin;

    /**
     * 忘记密码按钮
     */
    private ButtonFlat btfforgetpassword;

    /**
     * 注册账号按钮
     */
    private ButtonFlat btfRegist;

    /**
     * 网络请求实体
     */
    private AsyncHttpClient client;

    /**
     * 输入检测工具类
     */
    private EditTextUtils editTextUtils;

    /**
     * 本地账户
     */
    private String localUsrAccount;

    /**
     * 本地昵称
     */
    private String localUsrNickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        setLoginFunction();
        setRegistJumpFunction();
        setForgetPasswordFunction();
    }

    private void init(){
        setTitle("Login");
        AsyncHttpUtils.getUtilsInstance().initCookieStore(getApplicationContext());//important
        client = new AsyncHttpClient();
        client.setCookieStore(AsyncHttpUtils.getUtilsInstance().getCookieStoreInstance());
        etEmailInfo = (EditText)findViewById(R.id.et_emailinfo);
        etPasswordInfo = (EditText)findViewById(R.id.et_passwordinfo);
        btrLogin = (ButtonRectangle)findViewById(R.id.btr_login);
        btfforgetpassword = (ButtonFlat)findViewById(R.id.btf_gotgetpassword);
        btfRegist = (ButtonFlat)findViewById(R.id.btf_regiest);

        // 开启logcat输出，方便debug，发布时请关闭
        // XGPushConfig.enableDebug(this, true);
        // 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(), XGIOperateCallback)带callback版本
        // 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
        // 具体可参考详细的开发指南
        // 传递的参数为ApplicationContext
        Context context = getApplicationContext();
        XGPushManager.registerPush(context);
        // 2.36（不包括）之前的版本需要调用以下2行代码
        Intent service = new Intent(context, XGPushService.class);
        context.startService(service);
        Log.i("Token", XGPushConfig.getToken(context)+"");
        // 其它常用的API：
        // 绑定账号（别名）注册：registerPush(context,account)或registerPush(context,account, XGIOperateCallback)，其中account为APP账号，可以为任意字符串（qq、openid或任意第三方），业务方一定要注意终端与后台保持一致。
        // 取消绑定账号（别名）：registerPush(context,"*")，即account="*"为取消绑定，解绑后，该针对该账号的推送将失效
        // 反注册（不再接收消息）：unregisterPush(context)
        // 设置标签：setTag(context, tagName)
        // 删除标签：deleteTag(context, tagName)
    }

    /**
     * 实现登录功能
     */
    private void setLoginFunction(){
        btrLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btrLogin.setClickable(false);
                DeveloperUtils.changeSoftKeyboardStatus(false, getApplicationContext());
                btrLogin.setAlpha(0.3f);
                RequestParams params = new RequestParams();
                localUsrAccount = etEmailInfo.getText().toString();
                params.put("email",etEmailInfo.getText().toString());
                params.put("password", etPasswordInfo.getText().toString());
                params.put("Token",  XGPushConfig.getToken(getApplication()));
                client.post("http://45.78.59.95/login", params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            String CODE = response.get("CODE").toString();
                            String MESSAGE = response.get("MESSAGE").toString();
                            switch (CODE){
                                case "0":
                                    localUsrNickName = response.get("NICKNAME").toString();
                                    Toast.makeText(LoginActivity.this, MESSAGE, Toast.LENGTH_LONG).show();
                                    UserUtils.setLocalUsrAccount(localUsrAccount);
                                    UserUtils.setLocalUsrNickName(localUsrNickName);
                                    Intent intent = new Intent(LoginActivity.this, ChatlitsActivity.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case "1":
                                    Toast.makeText(LoginActivity.this, MESSAGE, Toast.LENGTH_LONG).show();
                                    etPasswordInfo.setText("");
                                    btrLogin.setAlpha(1.0f);
                                    break;
                                case "2":
                                    Toast.makeText(LoginActivity.this, MESSAGE, Toast.LENGTH_LONG).show();
                                    etPasswordInfo.setText("");
                                    btrLogin.setAlpha(1.0f);
                                    break;
                                default:
                                    Toast.makeText(LoginActivity.this, "Unkown error,please check your network.", Toast.LENGTH_LONG).show();
                                    btrLogin.setAlpha(1.0f);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                        Log.i("jsonsuccessfulArray", timeline.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("error",responseString);
                    }
                });
            }
        });
    }

    /**
     * 实现注册跳转
     */
    private void setRegistJumpFunction(){
        btfRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistActivity.class));
            }
        });
    }

    /**
     * 实现忘记密码联系管理员功能
     */
    private void setForgetPasswordFunction(){
        btfforgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Call Yasic Yu the super administrator now?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent("android.intent.action.CALL", Uri.parse("tel:" + "18215570287"));
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        });
    }
}
