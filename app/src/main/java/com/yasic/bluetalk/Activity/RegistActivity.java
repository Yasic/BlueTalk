package com.yasic.bluetalk.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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
import com.yasic.bluetalk.R;
import com.yasic.bluetalk.Utils.AsyncHttpUtils;
import com.yasic.bluetalk.Utils.DeveloperUtils;
import com.yasic.bluetalk.Utils.EditTextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by ESIR on 2016/2/26.
 */
public class RegistActivity extends AppCompatActivity {
    /**
     * 昵称输入框
     */
    private EditText etNickName;

    /**
     * email输入框
     */
    private EditText etEmail;

    /**
     * password输入框
     */
    private EditText etPassword;

    /**
     * 确认密码输入框
     */
    private EditText etPasswordAgain;

    /**
     * 注册按钮
     */
    private ButtonRectangle btrRegist;

    /**
     * asynchttp实体
     */
    private AsyncHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        init();
        setRegistFunction();
    }

    private void init(){
        etNickName = (EditText)findViewById(R.id.et_nickname);
        etEmail = (EditText)findViewById(R.id.et_email);
        etPassword = (EditText)findViewById(R.id.et_password);
        etPasswordAgain = (EditText)findViewById(R.id.et_passwordagain);
        btrRegist = (ButtonRectangle)findViewById(R.id.btr_regist);
        client = new AsyncHttpClient();
        client.setCookieStore(AsyncHttpUtils.getUtilsInstance().getCookieStoreInstance());
    }

    private void setRegistFunction() {
        btrRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btrRegist.setClickable(false);
                btrRegist.setAlpha(0.3f);
                if (!checkEdittext()){
                    btrRegist.setAlpha(1.0f);
                    return;
                }
                else {
                    RequestParams params = new RequestParams();
                    params.put("nickname", etNickName.getText().toString());
                    params.put("email", etEmail.getText().toString());
                    params.put("password", etPassword.getText().toString());
                    client.post(RegistActivity.this, "http://45.78.59.95/regist", params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                String CODE = response.get("CODE").toString();
                                String MESSAGE = response.get("MESSAGE").toString();
                                switch (CODE){
                                    case "7":
                                        Toast.makeText(RegistActivity.this, MESSAGE, Toast.LENGTH_LONG).show();
                                        finish();
                                        break;
                                    case "5":
                                        Toast.makeText(RegistActivity.this, MESSAGE, Toast.LENGTH_LONG).show();
                                        etEmail.setText("");
                                        btrRegist.setAlpha(1.0f);
                                        break;
                                    default:
                                        Toast.makeText(RegistActivity.this, "Unkown error,please check your network.", Toast.LENGTH_LONG).show();
                                        btrRegist.setAlpha(1.0f);
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
                        }
                    });
                }
            }});
    }

    private boolean checkEdittext() {
        if (!checkEmpty()){
            return false;
        }
        if (!EditTextUtils.isEmail(etEmail.getText().toString())){
            Toast.makeText(this, "Your EmailAddress is Wrong!", Toast.LENGTH_LONG).show();
            etEmail.setText("");
            return false;
        }
        if (!EditTextUtils.isPassword(etPassword.getText().toString())){
            Toast.makeText(this, "Password can only be composed of letters and numbers!", Toast.LENGTH_LONG).show();
            etPassword.setText("");
            return false;
        }
        if (!etPasswordAgain.getText().toString().equals(etPassword.getText().toString())){
            Toast.makeText(this, "Twice input of password is different!", Toast.LENGTH_LONG).show();
            etPassword.setText("");
            return false;
        }
        else return true;
    }

    private boolean checkEmpty() {
        if (EditTextUtils.isEmpty(etNickName.getText().toString())){
            Toast.makeText(this, "You may forget to input NickName..", Toast.LENGTH_LONG).show();
            return false;
        }
        if (EditTextUtils.isEmpty(etEmail.getText().toString())){
            Toast.makeText(this, "You may forget to input EmailAddress..", Toast.LENGTH_LONG).show();
            return false;
        }
        if (EditTextUtils.isEmpty(etPassword.getText().toString())){
            Toast.makeText(this, "You may forget to input Password..", Toast.LENGTH_LONG).show();
            return false;
        }
        if (EditTextUtils.isEmpty(etPasswordAgain.getText().toString())){
            Toast.makeText(this, "You may forget to confirm Password..", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }
}
