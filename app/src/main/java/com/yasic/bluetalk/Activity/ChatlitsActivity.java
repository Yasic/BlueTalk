package com.yasic.bluetalk.Activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.yasic.bluetalk.Adapters.ChatListAdapter;
import com.yasic.bluetalk.Adapters.MyLinearLayoutManager;
import com.yasic.bluetalk.Adapters.SearchResultAdapter;
import com.yasic.bluetalk.Applications.MyApplication;
import com.yasic.bluetalk.Applications.MyApplication.CustomMessageHandler;
import com.yasic.bluetalk.Object.BlueTalkUser;
import com.yasic.bluetalk.R;
import com.yasic.bluetalk.Utils.AsyncHttpUtils;
import com.yasic.bluetalk.Utils.ChatListSQLiteUtils;
import com.yasic.bluetalk.Utils.UserUtils;

import java.util.ArrayList;
import java.util.List;

public class ChatlitsActivity extends AppCompatActivity {

    private Handler handler = new Handler();

    /**
     * asynchttp实体
     */
    private AsyncHttpClient client;

    /**
     * cookiestore实体
     */
    private PersistentCookieStore cookieStore;

    /**
     * 显示聊天信息的list
     */
    private RecyclerView rvChatList;

    /**
     * 聊天列表adapter
     */
    private ChatListAdapter chatListAdapter;

    /**
     * 聊天信息列表
     */
    private List<BlueTalkUser> blueTalkUserList = new ArrayList<BlueTalkUser>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);
        init();
        initFAB();
        initRecyclerview();
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle("ChatList");
        collapsingToolbarLayout.setExpandedTitleColor(00000000);
        client = new AsyncHttpClient();
        cookieStore = new PersistentCookieStore(getApplicationContext());
        client.setCookieStore(AsyncHttpUtils.getUtilsInstance().getCookieStoreInstance());
        XGPushManager.registerPush(ChatlitsActivity.this, UserUtils.getLocalUsrAccount(),
                new XGIOperateCallback() {
                    @Override
                    public void onSuccess(Object data, int flag) {
                        Log.d("TPush", "注册成功，设备token为：" + data);
                    }

                    @Override
                    public void onFail(Object data, int errCode, String msg) {
                        Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
                    }
                });
    }

    /**
     * 初始化FAB
     */
    private void initFAB(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatlitsActivity.this, SearchResultActivity.class));
            }
        });
    }

    private void initRecyclerview(){
        rvChatList = (RecyclerView)findViewById(R.id.rv_chatlist);
        chatListAdapter = new ChatListAdapter(this,blueTalkUserList);
        rvChatList.setLayoutManager(new MyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        rvChatList.setAdapter(chatListAdapter);
        rvChatList.setItemAnimator(new DefaultItemAnimator());
        blueTalkUserList = getBlueTalkUserList();
        chatListAdapter.refresh(blueTalkUserList);
        chatListAdapter.setOnItemClickListener(new ChatListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(ChatlitsActivity.this, SingleMessageInterface.class);
                Bundle bundle = new Bundle();
                bundle.putString("chaterAccount",blueTalkUserList.get(position).getEmailAddress());
                bundle.putString("localAccount", UserUtils.getLocalUsrAccount());
                bundle.putString("chaterNickName",blueTalkUserList.get(position).getNickName());
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onItemLongCick(View v, int position) {

            }
        });
    }

    public List<BlueTalkUser> getBlueTalkUserList() {
        List<BlueTalkUser> blueTalkUserList = new ArrayList<>();
        ChatListSQLiteUtils chatListSQLiteUtils = new ChatListSQLiteUtils(this, "ChatList", 1);
        Cursor cursor = chatListSQLiteUtils.getReadableDatabase().query("ChatItem",
                null, "LOCALACCOUNT = ?", new String[]{UserUtils.getLocalUsrAccount()},
                null, null, "MESSAGETIME DESC");
        while (cursor.moveToNext()) {
            blueTalkUserList.add(new BlueTalkUser(
                    cursor.getString(cursor.getColumnIndex("NICKNAME")),
                    cursor.getString(cursor.getColumnIndex("ACCOUNT")),
                    cursor.getString(cursor.getColumnIndex("MESSAGE"))));
        }
        cursor.close();
        return blueTalkUserList;
    }
}
