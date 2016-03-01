package com.yasic.bluetalk.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ESIR on 2016/2/26.
 */
public class DeveloperUtils {

    /**
     * byte[] 转化为字符串
     * @param responseBody 传入byte数组
     * @return 返回相应的字符串
     */
    public static String byteToString(byte[] responseBody){
        String strRead = new String(responseBody);
        strRead = String.copyValueOf(strRead.toCharArray(),0,responseBody.length);
        return strRead;
    }

    public static byte[] ListToByte(ArrayList<String> list){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        for (String element : list) {
            try {
                out.writeUTF(element);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    /**
     * 改变软键盘状态
     * @param open 传入是否开启软键盘的bool值
     * @param context 传入当前context
     */
    public static void changeSoftKeyboardStatus(boolean open,Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(open){
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
        else{
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }
}
