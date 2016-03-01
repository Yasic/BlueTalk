package com.yasic.bluetalk.Object;

import com.yasic.bluetalk.Adapters.SearchResultAdapter;

/**
 * Created by ESIR on 2016/2/28.
 */
public class BlueTalkUser {
    /**
     * 昵称
     */
    private String nickName;

    /**
     * 邮箱地址
     */
    private String emailAddress;

    /**
     * 最近一条消息
     */
    private String lastMessage;

    public BlueTalkUser(String nickName, String emailAddress){
        this.nickName = nickName;
        this.emailAddress = emailAddress;
        this.lastMessage = "";
    }


    public BlueTalkUser(String nickName, String emailAddress, String lastMessage){
        this.nickName = nickName;
        this.emailAddress = emailAddress;
        this.lastMessage = lastMessage;
    }

    public String getNickName() {
        return nickName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
