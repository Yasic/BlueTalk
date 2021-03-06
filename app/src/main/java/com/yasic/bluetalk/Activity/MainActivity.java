package com.yasic.bluetalk.Activity;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.DialogPreference;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.SyncHttpClient;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushService;
import com.yasic.bluetalk.R;
import com.yasic.bluetalk.Utils.AsyncHttpUtils;
import com.yasic.bluetalk.Utils.BLEUtils;
import com.yasic.bluetalk.Utils.DeveloperUtils;

import org.apache.http.conn.scheme.HostNameResolver;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

public class MainActivity extends AppCompatActivity {
    private Handler handler = new Handler();

    /**
     * asynchttp实体
     */
    private AsyncHttpClient client;

    /**
     * cookiestore实体
     */
    private PersistentCookieStore cookieStore;

    private ArrayList<String> stringList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToobar();
        setFAB();
        client = new AsyncHttpClient();
        cookieStore = new PersistentCookieStore(getApplicationContext());
        client.setCookieStore(AsyncHttpUtils.getUtilsInstance().getCookieStoreInstance());
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
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Nickname or Email...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i("submit", query);
                stringList = new ArrayList<String>();
                final ArrayList<String> strings;
                RequestParams params = new RequestParams();
                params.put("nickname", query);
                client.post("http://45.78.59.95/searchusr", params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            String CODE = response.get("CODE").toString();
                            String MESSAGE = response.get("MESSAGE").toString();
                            switch (CODE) {
                                case "9":
                                    Log.i("NICKNAMEJSON",response.get("NICKNAMEJSON").toString());
                                    JSONObject NICKNAMEJSON = new JSONObject(response.get("NICKNAMEJSON").toString());
                                    for(int i = 0; i < NICKNAMEJSON.length(); i++){
                                        stringList.add(NICKNAMEJSON.get(i+"")+"");
                                    }
                                    if (stringList.size() == 0){
                                        Toast.makeText(MainActivity.this, "User Is not Exist", Toast.LENGTH_LONG).show();
                                        break;
                                    }
                                    else {
                                        Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                                        intent.putStringArrayListExtra("SEARCHRESULT", stringList);
                                        startActivity(intent);
                                        break;
                                    }
                                case "3":
                                    Toast.makeText(MainActivity.this, MESSAGE, Toast.LENGTH_LONG).show();
                                    break;
                                case "2":
                                    Toast.makeText(MainActivity.this, MESSAGE, Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Toast.makeText(MainActivity.this, "Unkown error,please check your network.", Toast.LENGTH_LONG).show();
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
                //startActivity(new Intent(MainActivity.this, SearchResultActivity.class));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("change",newText);
                return false;
            }
        });
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
