package com.yasic.bluetalk.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.net.PortUnreachableException;

/**
 * Created by ESIR on 2016/3/1.
 */
public class ChatListSQLiteUtils extends SQLiteOpenHelper {
    final String SQL_CREATE_CHATLIST = "create table ChatItem (" +
            "_id integer primary key autoincrement, " +
            "NICKNAME varchar, " +
            "ACCOUNT varchar,MESSAGE varchar,MESSAGETIME varchar,LOCALACCOUNT varchar)";

    public ChatListSQLiteUtils(Context context, String name, int version){
        super(context,name,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CHATLIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
