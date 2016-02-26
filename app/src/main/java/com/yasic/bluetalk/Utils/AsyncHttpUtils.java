package com.yasic.bluetalk.Utils;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;

import java.util.PriorityQueue;

/**
 * Created by ESIR on 2016/2/26.
 */
public class AsyncHttpUtils {
    private PersistentCookieStore cookieStore;

    /**
     * 空构造函数
     */
    private AsyncHttpUtils(){}

    public static AsyncHttpClient client = new AsyncHttpClient();

    public static AsyncHttpUtils getUtilsInstance(){
        return AsyncHttpUtilsHolder.sInstance;
    }

    public PersistentCookieStore getCookieStoreInstance(){
        return cookieStore;
    }

    public void initCookieStore(Context context) {
        cookieStore = new PersistentCookieStore(context);
    }


    private static class AsyncHttpUtilsHolder{
        private static final AsyncHttpUtils sInstance = new AsyncHttpUtils();
    }
}
