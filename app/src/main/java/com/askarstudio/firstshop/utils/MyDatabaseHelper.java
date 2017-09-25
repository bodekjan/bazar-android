package com.askarstudio.firstshop.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bodekjan on 2016/6/8.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_PLACE="create table user("
            +"id integer primary key autoincrement,"
            +"userphone varchar(255),"
            +"useraddress varchar(255),"
            +"userpass varchar(255),"
            +"userplace integer)";
    public static final String CREATE_CART="create table cart("
            +"id integer primary key autoincrement,"
            +"itemname varchar(255),"
            +"itemurl varchar(255),"
            +"itemid integer,"
            +"itemprice numeric,"
            +"itemcount numeric,"
            +"userphone varchar(255),"
            +"carttype integer,"
            +"itemstatus integer)";
    private Context mContext;
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PLACE);
        db.execSQL(CREATE_CART);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
        db.execSQL("drop table if exists cart");
        onCreate(db);
    }
}
