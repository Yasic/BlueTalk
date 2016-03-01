package com.yasic.bluetalk.Utils;

/**
 * Created by ESIR on 2016/3/1.
 */
public class UserUtils {
    private static String localUsrAccount = "2207793765@qq.com";

    private static String localUsrNickName = "yasic";

    public static String getLocalUsrNickName() {
        return localUsrNickName;
    }

    public static String getLocalUsrAccount(){
        return localUsrAccount;
    }

    public static void setLocalUsrAccount(String str){
        localUsrAccount = str;
    }

    public static void setLocalUsrNickName(String localUsrNickName) {
        UserUtils.localUsrNickName = localUsrNickName;
    }
}
