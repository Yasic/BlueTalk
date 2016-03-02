package com.yasic.bluetalk.Applications;

import android.app.Application;
import android.util.Log;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.yasic.bluetalk.Utils.MessageSQLiteUtils;

/**
 * Created by ESIR on 2016/3/1.
 */
public class MyApplication extends Application {
    private static MyApplication instance;
    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this, "t1DrKxx1O72MkgwRFako8OAS-gzGzoHsz", "nTqIi1dQzFh48z4p7DL20WNm");
        AVIMMessageManager.registerDefaultMessageHandler(new CustomMessageHandler());
    }

    public static class CustomMessageHandler extends AVIMMessageHandler {
        //接收到消息后的处理逻辑
        @Override
        public void onMessage(AVIMMessage message,AVIMConversation conversation,AVIMClient client){
            if(message instanceof AVIMTextMessage){
                String tempStr = ((AVIMTextMessage) message).getText().toString();
                String strMessageTime = tempStr.substring(tempStr.length() - 18, tempStr.length());
                String strMessage = tempStr.substring(0,tempStr.length() - 19);
                MessageSQLiteUtils.insertDB(MyApplication.getInstance(), "", "",
                        "", "", "");
            }
        }

        public void onMessageReceipt(AVIMMessage message,AVIMConversation conversation,AVIMClient client){

        }
    }
}
