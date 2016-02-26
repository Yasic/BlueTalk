package com.yasic.bluetalk.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yasic.bluetalk.R;
import com.yasic.bluetalk.Utils.AsyncHttpUtils;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ESIR on 2016/2/24.
 */
public class SingleMessageInterface extends AppCompatActivity{
    /**
     * 发送消息按钮
     */
    Button btPostMessage;

    /**
     * 内容显示tv
     */
    TextView tvGetResult;

    /**
     * 登出按钮
     */
    private Button btLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlemessageinterface);
        btPostMessage = (Button)findViewById(R.id.bt_postmessage);
        btLogout = (Button)findViewById(R.id.bt_logout);
        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpClient client = new AsyncHttpClient();
                client.setCookieStore(AsyncHttpUtils.getUtilsInstance().getCookieStoreInstance());
                client.get("http://45.78.59.95/logout", null, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String strRead = new String(responseBody);
                        strRead = String.copyValueOf(strRead.toCharArray(),0,responseBody.length);
                        Log.i("successful", strRead);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.i("error",error.getMessage());
                    }
                });
            }
        });
        btPostMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                /*asyncHttpClient.get("http://192.168.1.106:8000/testget",null, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.i("get","successful");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.i("get","faliure" + "+" + error.getMessage());
                    }
                });*/
                RequestParams params = new RequestParams();
                params.add("name","yuxuan");
                params.add("message","helloworld");
                asyncHttpClient.post("http://45.78.59.95/testget", params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String strRead = new String(responseBody);
                        strRead = String.copyValueOf(strRead.toCharArray(),0,responseBody.length);
                        Log.i("successful", strRead);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.i("error",error.getMessage());
                    }
                });
            }
        });
    }
}
