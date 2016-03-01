package com.yasic.bluetalk.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.tencent.android.tpush.XGPushConfig;
import com.yasic.bluetalk.Adapters.ChatListAdapter;
import com.yasic.bluetalk.Adapters.MessageAdapter;
import com.yasic.bluetalk.Object.BlueTalkUser;
import com.yasic.bluetalk.Object.Message;
import com.yasic.bluetalk.R;
import com.yasic.bluetalk.Utils.AsyncHttpUtils;
import com.yasic.bluetalk.Utils.ChatListSQLiteUtils;
import com.yasic.bluetalk.Utils.TimeUtils;
import com.yasic.bluetalk.Utils.UserUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ContentHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ESIR on 2016/2/24.
 */
public class SingleMessageInterface extends AppCompatActivity{

    /**
     * 消息显示rv
     */
    private RecyclerView rvMessageList;

    /**
     * asynchttp实体
     */
    private AsyncHttpClient client;

    /**
     * cookiestore实体
     */
    private PersistentCookieStore cookieStore;

    /**
     * 消息列表
     */
    private List<Message> messageList = new ArrayList<Message>();

    /**
     * 对方账号
     */
    private String chaterAccount;

    /**
     * 本地账号
     */
    private String localAccount;

    /**
     * 对方昵称
     */
    private String chaterNickName;

    /**
     * 处理消息的adapter
     */
    private MessageAdapter messageAdapter;

    /**
     * 显示空消息的textview
     */
    private TextView tvEmptyMessage;

    /**
     * 发消息的button
     */
    private Button btPostMessage;

    /**
     * 输入消息的edittext
     */
    private EditText etMassageInput;

    /**
     * 要发送的消息
     */
    private String messange;

    /**
     * 发送时间
     */
    private String sendTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlemessageinterface);
        getInfo();
        initRecyclerView();
        initAsyncHttp();
        getMessageData();
        setSendFunction();
    }

    private void getInfo(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        try {
            chaterAccount = bundle.getString("chaterAccount");
            localAccount = bundle.getString("localAccount");
            chaterNickName = bundle.getString("chaterNickName");
            setTitle(chaterNickName);
        }catch (Exception e){
            Log.i("intenterror",e.getMessage());
        }
    }

    private void initRecyclerView() {
        rvMessageList = (RecyclerView)findViewById(R.id.rv_messagelist);
        rvMessageList.setLayoutManager(new LinearLayoutManager(this));
        rvMessageList.setItemAnimator(new DefaultItemAnimator());
        messageAdapter = new MessageAdapter(this, messageList, localAccount);
        rvMessageList.setAdapter(messageAdapter);

        tvEmptyMessage = (TextView)findViewById(R.id.tv_emptymessage);
        tvEmptyMessage.setVisibility(View.GONE);
    }

    private void setSendFunction(){
        btPostMessage = (Button)findViewById(R.id.bt_postmessage);
        etMassageInput = (EditText)findViewById(R.id.et_massageinput);
        btPostMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messange = etMassageInput.getText().toString();
                if (messange.equals("")) {
                    Toast.makeText(SingleMessageInterface.this, "You may forget to write message..", Toast.LENGTH_SHORT).show();
                    return;
                }
                btPostMessage.setClickable(false);
                btPostMessage.setAlpha(0.3f);
                RequestParams params = new RequestParams();
                params.put("posterAccount", UserUtils.getLocalUsrAccount());
                params.put("receiverAccount", chaterAccount);
                params.put("posterNickName", UserUtils.getLocalUsrNickName());
                params.put("receiverNickName", chaterNickName);
                params.put("message", messange);
                sendTime = TimeUtils.getCurrentDateAndTime();
                params.put("releasedate", sendTime);
                Log.i("releasetime", TimeUtils.getCurrentDateAndTime());
                client.post("http://45.78.59.95/sendmessage", params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            String CODE = response.get("CODE").toString();
                            String MESSAGE = response.get("MESSAGE").toString();
                            switch (CODE) {
                                case "8":
                                    Log.i("MESSAGE", response.get("MESSAGE").toString());
                                    messageList.add(new Message(
                                            UserUtils.getLocalUsrAccount(),
                                            UserUtils.getLocalUsrNickName(),
                                            chaterAccount,
                                            chaterNickName,
                                            messange,
                                            sendTime));
                                    tvEmptyMessage.setVisibility(View.GONE);
                                    Collections.sort(messageList);
                                    addMessageItem(messageList);
                                    updateChatListDB(messageList);
                                    btPostMessage.setClickable(true);
                                    btPostMessage.setAlpha(1f);
                                    etMassageInput.setText("");
                                    break;
                                case "3":
                                    Log.i("error", MESSAGE);
                                    Toast.makeText(SingleMessageInterface.this, MESSAGE, Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Log.i("error", MESSAGE);
                                    Toast.makeText(SingleMessageInterface.this, "Unkown error,please check your network.", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        btPostMessage.setClickable(true);
                        btPostMessage.setAlpha(1f);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                        Log.i("jsonsuccessfulArray", timeline.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("error", responseString);
                        btPostMessage.setClickable(true);
                        btPostMessage.setAlpha(1f);
                    }
                });

            }
        });
    }

    private void initAsyncHttp(){
        client = new AsyncHttpClient();
        cookieStore = new PersistentCookieStore(getApplicationContext());
        client.setCookieStore(AsyncHttpUtils.getUtilsInstance().getCookieStoreInstance());
    }

    private List<Message> getMessageData() {
        final List<Message> messages = new ArrayList<Message>();
        RequestParams params = new RequestParams();
        params.put("LocalAccount",localAccount);
        params.put("ChaterAccount", chaterAccount);
        client.post("http://45.78.59.95/searchmsg", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String CODE = response.get("CODE").toString();
                    String MESSAGE = response.get("MESSAGE").toString();
                    switch (CODE) {
                        case "10":
                            Log.i("MESSAGEJSON", response.get("MESSAGEJSON").toString());
                            JSONObject MESSAGEJSON = new JSONObject(response.get("MESSAGEJSON").toString());
                            for (int i = 0; i < MESSAGEJSON.length(); i += 6) {
                                String posterAccount = MESSAGEJSON.get(i + "") + "";
                                String posterNickName = MESSAGEJSON.get(i + 1 + "") + "";
                                String geterAccount = MESSAGEJSON.get(i + 2 + "") + "";
                                String geterNickName = MESSAGEJSON.get(i + 3 + "") + "";
                                String message = MESSAGEJSON.get(i + 4 + "") + "";
                                String sendTime = MESSAGEJSON.get(i + 5 + "") + "";
                                messageList.add(new Message(posterAccount, posterNickName, geterAccount, geterNickName, message, sendTime));
                            }
                            if (messageList.size() == 0) {
                                tvEmptyMessage.setVisibility(View.VISIBLE);
                                TextView tvEmptyMessage = (TextView) findViewById(R.id.tv_emptymessage);
                                tvEmptyMessage.setText("There is no message..");
                            } else {
                                tvEmptyMessage.setVisibility(View.GONE);
                                Collections.sort(messageList);
                                addMessageItem(messageList);
                            }
                            updateChatListDB(messageList);
                            break;
                        case "3":
                            Log.i("error", MESSAGE);
                            Toast.makeText(SingleMessageInterface.this, MESSAGE, Toast.LENGTH_LONG).show();
                            break;
                        case "2":
                            Log.i("error", MESSAGE);
                            Toast.makeText(SingleMessageInterface.this, MESSAGE, Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Log.i("error", MESSAGE);
                            Toast.makeText(SingleMessageInterface.this, "Unkown error,please check your network.", Toast.LENGTH_LONG).show();
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
                Log.i("error", responseString);
            }
        });
        return messages;
    }

    private void addMessageItem(List<Message> messages){
        messageAdapter.refresh(messages);
        rvMessageList.smoothScrollToPosition(messageAdapter.getItemCount());
    }

    private void updateChatListDB(List<Message> messageList){
        ChatListSQLiteUtils chatListSQLiteUtils = new ChatListSQLiteUtils(this, "ChatList", 1);
        Cursor cursor = chatListSQLiteUtils.getReadableDatabase().query("ChatItem",
                null, "ACCOUNT = ? and LOCALACCOUNT = ?",
                new String[]{chaterAccount,UserUtils.getLocalUsrAccount()},null,null,null);
        if (!cursor.moveToNext()){
            Log.i("cursor","1");
            ContentValues values = new ContentValues();
            values.put("NICKNAME",chaterNickName);
            values.put("ACCOUNT",chaterAccount);
            values.put("MESSAGE", messageList.get(messageAdapter.getItemCount()-1).getMessageData());
            values.put("MESSAGETIME", messageList.get(messageAdapter.getItemCount()-1).getSendTime());
            values.put("LOCALACCOUNT", UserUtils.getLocalUsrAccount());
            chatListSQLiteUtils.getReadableDatabase().insert("ChatItem",null,values);
        }
        else {
            Log.i("cursor","2");
            ContentValues values = new ContentValues();
            values.put("NICKNAME",chaterNickName);
            values.put("ACCOUNT",chaterAccount);
            values.put("MESSAGE", messageList.get(messageAdapter.getItemCount()-1).getMessageData());
            values.put("MESSAGETIME", messageList.get(messageAdapter.getItemCount() - 1).getSendTime());
            chatListSQLiteUtils.getReadableDatabase().update("ChatItem",
                    values, "ACCOUNT = ? and LOCALACCOUNT = ?",
                    new String[]{chaterAccount, UserUtils.getLocalUsrAccount()});
        }
        cursor.close();
    }
}
