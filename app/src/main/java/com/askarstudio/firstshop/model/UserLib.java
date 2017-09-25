package com.askarstudio.firstshop.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.askarstudio.firstshop.utils.MyDatabaseHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bodekjan on 2016/9/6.
 */
public class UserLib {
    private MyDatabaseHelper dbHelper;
    private Context mContext;
    private User user;
    private static UserLib mLib;
    private UserLib(Context context){
        mContext=context;
        dbHelper= new MyDatabaseHelper(mContext,"shop.db",null, 1);
        initUser();
    }
    private void initUser(){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("user",null,null,null,null,null,null);
        if(cursor.getCount()==0){
            cursor.close();
            sqLiteDatabase.close();
            return;
        }
        user = new User();
        if(cursor.moveToFirst()){
            do {
                user.userPhone=cursor.getString(cursor.getColumnIndex("userphone"));
                user.userAddress=cursor.getString(cursor.getColumnIndex("useraddress"));
                user.userPass=cursor.getString(cursor.getColumnIndex("userpass"));
                user.userPlace=cursor.getInt(cursor.getColumnIndex("userplace"));
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
    }
    public void setUser(User user){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        sqLiteDatabase.delete("user",null,null);
        sqLiteDatabase.execSQL("insert into user(userphone,useraddress,userpass,userplace) values (?,?,?,?)", new Object[] { user.userPhone,user.userAddress,user.userPass,user.userPlace });
        sqLiteDatabase.close();
        initUser();
    }
    public static UserLib get(Context context){
        if(mLib==null){
            mLib = new UserLib(context);
        }
        return mLib;
    }
    public User getUser(){
        return user;
    }
    public boolean checkUser() {
        if(user == null){
            return false;
        }
        return true;
    }
}
