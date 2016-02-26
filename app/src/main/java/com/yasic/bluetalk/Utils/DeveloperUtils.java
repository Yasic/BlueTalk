package com.yasic.bluetalk.Utils;

/**
 * Created by ESIR on 2016/2/26.
 */
public class DeveloperUtils {
    public static String byteToString(byte[] responseBody){
        String strRead = new String(responseBody);
        strRead = String.copyValueOf(strRead.toCharArray(),0,responseBody.length);
        return strRead;
    }
}
