package com.ricoh.wm.my.utils;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by 2017063001 on 2018/5/21.
 */
public class MyDbHelper extends SQLiteOpenHelper {

    private static final String CREATE_BOOK = "create table Book("
                                            +"id integer primary key autoincrement,"
                                            +"author text,"
                                            +"price real,"
                                            +"pages integer,"
                                            +"name text)";
    private static final String CREATE_CATEGORY = "create table Category("
            +"id integer primary key autoincrement,"
            +"category_name text,"
            +"category_code integer)";

    private Context mContext;

    /**
     *
     * @param context  上下文
     * @param name  数据库名称
     * @param factory   游标工厂
     * @param version   数据库版本号
     */
    public MyDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;

    }
    /**只在创建数据库的时候调用一次，myDbHelper = new MyDbHelper(getContext(),"BookerStore.db",null,
     *          如果当BookerStore.db数据库已经存在了，不会调用onCreate
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库的时候创建一个 book的表
        db.execSQL(CREATE_BOOK);
        db.execSQL(CREATE_CATEGORY);
        Toast.makeText(mContext, "表创建成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      /*  db.execSQL("drop table if exists Book");
        db.execSQL("drop table if exists Category");
        onCreate(db);*/
    }
}
