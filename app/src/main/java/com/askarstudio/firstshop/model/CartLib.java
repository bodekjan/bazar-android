package com.askarstudio.firstshop.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.askarstudio.firstshop.utils.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bodekjan on 2016/9/6.
 */
public class CartLib {
    private MyDatabaseHelper dbHelper;
    private Context mContext;
    private List<Cart> carts;
    private static CartLib mLib;
    private CartLib(Context context){
        mContext=context;
        dbHelper= new MyDatabaseHelper(mContext,"shop.db",null, 1);
        initCart();
    }
    private void initCart(){
        carts = new ArrayList<Cart>();
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("cart",null,null,null,null,null,null);

        if(cursor.getCount()==0){
            cursor.close();
            sqLiteDatabase.close();
            return;
        }

        if(cursor.moveToFirst()){
            do {
                Cart cart = new Cart();
                cart.cartId = cursor.getInt(cursor.getColumnIndex("id"));
                cart.itemId = cursor.getInt(cursor.getColumnIndex("itemid"));
                cart.itemCount = cursor.getDouble(cursor.getColumnIndex("itemcount"));
                cart.itemName = cursor.getString(cursor.getColumnIndex("itemname"));
                cart.itemPrice = cursor.getDouble(cursor.getColumnIndex("itemprice"));
                cart.itemStatus = cursor.getInt(cursor.getColumnIndex("itemstatus"));
                cart.itemURL = cursor.getString(cursor.getColumnIndex("itemurl"));
                cart.userPhone = cursor.getString(cursor.getColumnIndex("userphone"));
                cart.cartType = cursor.getInt(cursor.getColumnIndex("carttype"));
                carts.add(cart);
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
    }
    public static CartLib get(Context context){
        if(mLib==null){
            mLib = new CartLib(context);
        }
        return mLib;
    }
    public List<Cart> getCarts(User user){
            List<Cart> cartsTemp = new ArrayList<Cart>();
        for(int i=0; i<carts.size();i++){
            if(carts.get(i).userPhone.equals(user.userPhone)){
                cartsTemp.add(carts.get(i));
            }
        }
        return cartsTemp;
    }
    public void deleteCart(int cartId){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        sqLiteDatabase.delete("cart", "id=?",new String[] { cartId+"" });
        sqLiteDatabase.close();
        initCart();
    }
    public void addCart(User user, Mall mall , double count){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("itemid", mall.mallId);
        values.put("itemname", mall.mallName);
        values.put("userphone", user.userPhone);
        values.put("itemurl", mall.mallURL);
        values.put("itemcount", count);
        values.put("itemprice", mall.mallPrice);
        values.put("carttype" , mall.priceType);
        values.put("itemstatus", 1);
        long rowid = sqLiteDatabase.insert("cart", null, values);//返回新添记录的行号，与主键id无
        sqLiteDatabase.close();
        initCart();
    }
}
