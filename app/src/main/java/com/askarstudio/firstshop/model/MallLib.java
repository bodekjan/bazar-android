package com.askarstudio.firstshop.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.askarstudio.firstshop.utils.CommonHelper;
import com.askarstudio.firstshop.utils.MyDatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by bodekjan on 2016/9/6.
 */
public class MallLib {
    private MyDatabaseHelper dbHelper;
    private Context mContext;
    private List<Mall> malls;
    private static MallLib mLib;
    private MallLib(Context context){
        mContext=context;
        //dbHelper= new MyDatabaseHelper(mContext,"shop.db",null, 1);
    }
    public void initMall(String string){
        malls = new ArrayList<Mall>();
        try {
            JSONArray mallArray = new JSONArray(string);
            if(mallArray.length()<6){
                for(int i=0; i<9;i++)
                {
                    Mall mall = new Mall();
                    mall.mallName =  "قۇرۇق";
                    mall.mallURL ="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1504589084860&di=6319eb4c3ff50b9bacef156f98cd630b&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F15%2F41%2F24%2F82G58PICnZi_1024.png";
                    mall.mallId = -1;
                    mall.typeId = -1;
                    if(i>3 && i<7){
                        mall.mallstatus = 1;
                    }else{
                        mall.mallstatus = 0;
                    }
                    mall.mallCount = 0;
                    mall.mallPrice = 0;
                    mall.priceType = 0;
                    malls.add(mall);
                }
                return;
            }

            for(int i=0; i<mallArray.length();i++)
            {
                Mall mall = new Mall();
                mall.mallName =  mallArray.getJSONObject(i).getString("goodname");
                mall.mallURL = CommonHelper.media+mallArray.getJSONObject(i).getString("goodimg");
                mall.mallId = mallArray.getJSONObject(i).getInt("id");
                mall.typeId = mallArray.getJSONObject(i).getInt("typeid");
                mall.mallstatus = mallArray.getJSONObject(i).getInt("goodstatus");
                mall.mallCount = mallArray.getJSONObject(i).getInt("goodcount");
                mall.mallPrice = Float.valueOf(mallArray.getJSONObject(i).get("goodprice").toString());
                mall.priceType = mallArray.getJSONObject(i).getInt("pricetype");
                malls.add(mall);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static MallLib get(Context context){
        if(mLib==null){
            mLib = new MallLib(context);
        }
        return mLib;
    }
    public List<Mall> getForMain()
    {
        List<Mall> tempMalls = new ArrayList<Mall>();
        for(int i=0; i<malls.size() ; i++){
            if(malls.get(i).mallstatus ==2 ){
                tempMalls.add(malls.get(i));
            }
        }
        return tempMalls;
    }
    public List<Mall> getForNew()
    {
        List<Mall> tempMalls = new ArrayList<Mall>();
        for(int i=0; i<malls.size() ; i++){
            if(malls.get(i).mallstatus ==2 ){
                tempMalls.add(malls.get(i));
            }
        }
        return tempMalls;
    }
    public List<Mall> getForAll()
    {
        return malls;
    }
    public List<Mall> getForType(int tid)
    {
        List<Mall> tempMalls = new ArrayList<Mall>();
        for(int i=0; i<malls.size() ; i++){
            if(malls.get(i).typeId ==tid ){
                tempMalls.add(malls.get(i));
            }
        }
        return tempMalls;
    }
    public List<Mall> getForSearch(String key)
    {
        List<Mall> tempMalls = new ArrayList<Mall>();
        for(int i=0; i<malls.size() ; i++){
            if(malls.get(i).mallName.indexOf(key) !=-1 ){
                tempMalls.add(malls.get(i));
            }
        }
        return tempMalls;
    }
    public List<Mall> getForBanner()
    {
        List<Mall> tempMalls = new ArrayList<Mall>();
        for(int i=0; i<malls.size() ; i++){
            if(malls.get(i).mallstatus ==1 ){
                tempMalls.add(malls.get(i));
            }
        }
        return tempMalls;
    }
}
