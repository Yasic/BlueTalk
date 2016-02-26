package com.yasic.bluetalk.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yasic.bluetalk.R;
import com.yasic.bluetalk.Utils.AsyncHttpUtils;
import com.yasic.bluetalk.Utils.DeveloperUtils;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        setLoginFunction();
        setRegistJumpFunction();
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
    }

    /**
     * 实现登录功能
     */
    private void setLoginFunction(){
        btrLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btrLogin.setClickable(false);
                RequestParams params = new RequestParams();
                params.put("id","yasic");
                params.put("password", "123456");
                client.post("http://45.78.59.95/login", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.i("login", DeveloperUtils.byteToString(responseBody));
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.i("error",error.getMessage());
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
                startActivity(new Intent(LoginActivity.this,RegistActivity.class));
                finish();
            }
        });
    }
}
