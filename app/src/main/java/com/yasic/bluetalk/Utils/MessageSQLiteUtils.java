package com.yasic.bluetalk.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ESIR on 2016/3/1.
 */
public class MessageSQLiteUtils extends SQLiteOpenHelper{
    final String SQL_CREATE_CHATLIST = "create table MessageItem (" +
            "_id integer primary key autoincrement, " +
            "NICKNAME varchar, " +
            "ACCOUNT varchar,MESSAGE varchar,MESSAGETIME varchar,LOCALACCOUNT varchar)";

    public MessageSQLiteUtils(Context context, String name, int version){
        super(context,name,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CHATLIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static void insertDB(Context context, String NICKNAME,String ACCOUNT,
                                String MESSAGE, String MESSAGETIME,String LOCALACCOUNT){
        MessageSQLiteUtils messageSQLiteUtils = new MessageSQLiteUtils(context, "MessageList", 1);
        ContentValues values = new ContentValues();
        values.put("NICKNAME",NICKNAME);
        values.put("ACCOUNT",ACCOUNT);
        values.put("MESSAGE", MESSAGE);
        values.put("MESSAGETIME", MESSAGETIME);
        values.put("LOCALACCOUNT", LOCALACCOUNT);
        messageSQLiteUtils.getReadableDatabase().insert("MessageItem",null,values);
    }
}
