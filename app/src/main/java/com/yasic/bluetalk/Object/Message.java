package com.yasic.bluetalk.Object;

import java.util.Comparator;

/**
 * Created by ESIR on 2016/2/24.
 */
public class Message implements Comparable{
    /**
     * 消息内容
     */
    private String messageData;

    /**
     * 消息发送者Id
     */
    private String posterId;

    /**
     * 发送者账号
     */
    private String posterAccount;

    /**
     * 接收者账号
     */
    private String geterAccount;


    /**
     * 消息接收者Id
     */
    private String geterId;

    /**
     * 发送时间
     */
    private String sendTime;

    public Message(String posterAccount,String posterId, String geterAccount, String geterId, String messageData, String sendTime) {
        this.messageData = messageData;
        this.sendTime = sendTime;
        this.posterAccount = posterAccount;
        this.geterId = geterId;
        this.posterId = posterId;
        this.geterAccount = geterAccount;
    }

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

    public String getPosterAccount() {
        return posterAccount;
    }

    public String getGeterAccount() {
        return geterAccount;
    }

    public String getSendTime() {
        return sendTime;
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

    public void setPosterAccount(String posterAccount) {
        this.posterAccount = posterAccount;
    }

    public void setGeterAccount(String geterAccount) {
        this.geterAccount = geterAccount;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public int compareTo(Object another) {
        return -this.getSendTime().compareTo(((Message)another).getSendTime());
    }
}
