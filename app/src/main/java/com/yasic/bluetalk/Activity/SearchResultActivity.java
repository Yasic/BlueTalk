package com.yasic.bluetalk.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.yasic.bluetalk.Adapters.SearchResultAdapter;
import com.yasic.bluetalk.Object.BlueTalkUser;
import com.yasic.bluetalk.R;
import com.yasic.bluetalk.Utils.AsyncHttpUtils;
import com.yasic.bluetalk.Utils.DeveloperUtils;
import com.yasic.bluetalk.Utils.UserUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ESIR on 2016/2/28.
 */
public class SearchResultActivity extends AppCompatActivity {

    /**
     * 显示搜索结果的rv
     */
    private RecyclerView rvSearchResult;

    /**
     * 用户列表
     */
    private List<BlueTalkUser> blueTalkUserList;

    private ArrayList<String> stringList;

    /**
     * asynchttp实体
     */
    private AsyncHttpClient client;

    /**
     * cookiestore实体
     */
    private PersistentCookieStore cookieStore;

    private SearchResultAdapter searchResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresult);
        setTitle("Search Friends");
        init();
        searchResultAdapter.setOnItemClickListener(new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(SearchResultActivity.this, SingleMessageInterface.class);
                Bundle bundle = new Bundle();
                bundle.putString("chaterAccount",blueTalkUserList.get(position).getEmailAddress());
                bundle.putString("localAccount", UserUtils.getLocalUsrAccount());
                bundle.putString("chaterNickName",blueTalkUserList.get(position).getNickName());
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }

            @Override
            public void onItemLongCick(View v, int position) {

            }
        });
    }

    private void init(){
        blueTalkUserList = new ArrayList<BlueTalkUser>();
        searchResultAdapter = new SearchResultAdapter(this,blueTalkUserList);
        rvSearchResult = (RecyclerView)findViewById(R.id.rv_searchresult);
        rvSearchResult.setAdapter(searchResultAdapter);
        rvSearchResult.setLayoutManager(new LinearLayoutManager(this));
        rvSearchResult.setItemAnimator(new DefaultItemAnimator());
        client = new AsyncHttpClient();
        cookieStore = new PersistentCookieStore(getApplicationContext());
        client.setCookieStore(AsyncHttpUtils.getUtilsInstance().getCookieStoreInstance());
    }

    private void addResultItem(ArrayList<String> arrayList){
        blueTalkUserList.clear();
        for (int i = 0; i < arrayList.size();i+=2){
            blueTalkUserList.add(new BlueTalkUser(arrayList.get(i),arrayList.get(i+1)));
        }
        searchResultAdapter.refresh(blueTalkUserList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Nickname or Acount...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i("submit", query);
                stringList = new ArrayList<String>();
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
                                        Toast.makeText(SearchResultActivity.this, "User Is not Exist", Toast.LENGTH_LONG).show();
                                        break;
                                    }
                                    else {
                                        addResultItem(stringList);
                                        break;
                                    }
                                case "3":
                                    Toast.makeText(SearchResultActivity.this, MESSAGE, Toast.LENGTH_LONG).show();
                                    break;
                                case "2":
                                    Toast.makeText(SearchResultActivity.this, MESSAGE, Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Toast.makeText(SearchResultActivity.this, "Unkown error,please check your network.", Toast.LENGTH_LONG).show();
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
