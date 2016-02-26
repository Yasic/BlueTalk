package com.yasic.bluetalk.Activity;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.DialogPreference;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.yasic.bluetalk.R;
import com.yasic.bluetalk.Utils.AsyncHttpUtils;
import com.yasic.bluetalk.Utils.BLEUtils;

import org.apache.http.conn.scheme.HostNameResolver;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    private Button btLogin;
    private Button btLogout;
    private AsyncHttpClient client;
    private PersistentCookieStore cookieStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToobar();
        setFAB();
        btLogin = (Button)findViewById(R.id.bt_login);
        btLogout = (Button)findViewById(R.id.bt_logout);
        client = new AsyncHttpClient();
        cookieStore = new PersistentCookieStore(getApplicationContext());
        client.setCookieStore(AsyncHttpUtils.getUtilsInstance().getCookieStoreInstance());
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.get("http://45.78.59.95/login", null, new AsyncHttpResponseHandler() {
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
        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    }

    /**
     * 设置toolbar
     */
    private void setToobar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * 设置FAB
     */
    private void setFAB(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SingleMessageInterface.class);
                startActivity(intent);
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
