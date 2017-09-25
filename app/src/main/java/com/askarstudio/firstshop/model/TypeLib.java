package com.askarstudio.firstshop.model;

import android.content.Context;

import com.askarstudio.firstshop.utils.CommonHelper;
import com.askarstudio.firstshop.utils.MyDatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bodekjan on 2016/9/6.
 */
public class TypeLib {
    private MyDatabaseHelper dbHelper;
    private Context mContext;
    private List<MType> types;
    private static TypeLib mLib;
    private TypeLib(Context context){
        mContext=context;
        //dbHelper= new MyDatabaseHelper(mContext,"shop.db",null, 1);
    }
    public void initType(String string){
        types = new ArrayList<MType>();
        try {
            JSONArray typeArray = new JSONArray(string);
            if(typeArray.length()<3){
                for(int i=0; i<3;i++)
                {
                    MType type = new MType();
                    type.typeName = "قۇرۇق";
                    type.typeImg = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1504589084860&di=6319eb4c3ff50b9bacef156f98cd630b&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F15%2F41%2F24%2F82G58PICnZi_1024.png";
                    type.typeId = -1;
                    types.add(type);
                }
                return;
            }
            for(int i=0; i<typeArray.length();i++)
            {
                MType type = new MType();
                type.typeName = typeArray.getJSONObject(i).getString("typename");
                type.typeImg = CommonHelper.media+typeArray.getJSONObject(i).getString("typeimg");
                type.typeId = typeArray.getJSONObject(i).getInt("id");
                types.add(type);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public static TypeLib get(Context context){
        if(mLib==null){
            mLib = new TypeLib(context);
        }
        return mLib;
    }
    public List<MType> getForMain()
    {
        List<MType> tempMalls = new ArrayList<MType>();
        for(int i=0; i<3 ; i++){
            tempMalls.add(types.get(i));
        }
        return tempMalls;
    }
    public List<MType> getForAll()
    {
        List<MType> tempMalls = new ArrayList<MType>();
        for(int i=0; i<types.size() ; i++){
            tempMalls.add(types.get(i));
        }
        return tempMalls;
    }
}
