package com.yasic.bluetalk.Utils;

/**
 * Created by ESIR on 2016/2/24.
 */
public class MessageUtils {
    /**
     * 空构造函数
     */
    private MessageUtils(){}

    public static MessageUtils getInstance(){
        return MessageUtilsHolder.sInstance;
    }

    private static class MessageUtilsHolder{
        private static final MessageUtils sInstance = new MessageUtils();
    }

    /**
     * 传入消息，进行发送
     */
    public void postMessage(String Message){

    }
}
