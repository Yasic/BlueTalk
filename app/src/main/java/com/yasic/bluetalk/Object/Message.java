package com.yasic.bluetalk.Object;

/**
 * Created by ESIR on 2016/2/24.
 */
public class Message {
    /**
     * 消息内容
     */
    private String messageData;

    /**
     * 消息发送者Id
     */
    private String posterId;

    /**
     * 消息接收者Id
     */
    private String geterId;

    /**
     * 获得消息内容
     * @return 返回消息内容
     */
    public String getMessageData() {
        return messageData;
    }

    /**
     * 获得发送者Id
     * @return 返回发送者Id
     */
    public String getPosterId() {
        return posterId;
    }

    /**
     * 获得接收者Id
     * @return 返回接收者Id
     */
    public String getGeterId() {
        return geterId;
    }

    /**
     * 设置发送消息内容
     * @param messageData 传入消息内容
     */
    public void setMessageData(String messageData) {
        this.messageData = messageData;
    }

    /**
     * 设置发送者Id
     * @param posterId 传入发送者Id
     */
    public void setPosterId(String posterId) {
        this.posterId = posterId;
    }

    /**
     * 设置接收者Id
     * @param geterId 传入接收者Id
     */
    public void setGeterId(String geterId) {
        this.geterId = geterId;
    }
}
