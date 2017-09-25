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
public class BannerLib {
    private MyDatabaseHelper dbHelper;
    private Context mContext;
    private List<Banner> banners;
    private static BannerLib mLib;
    private BannerLib(Context context){
        mContext=context;
        //dbHelper= new MyDatabaseHelper(mContext,"shop.db",null, 1);
    }
    public void initBanner(String string){
        banners = new ArrayList<Banner>();
        try {
            JSONArray mallArray = new JSONArray(string);
            if(mallArray.length()<3){
                for(int i=0; i<4;i++)
                {
                    Banner banner = new Banner();
                    banner.bName =  "قۇرۇق";
                    banner.bDesc =  "قۇرۇق";
                    banner.bPic = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1504589084860&di=6319eb4c3ff50b9bacef156f98cd630b&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F15%2F41%2F24%2F82G58PICnZi_1024.png";
                    banners.add(banner);
                }
                return;
            }

            for(int i=0; i<mallArray.length();i++)
            {
                Banner banner = new Banner();
                banner.bId =  mallArray.getJSONObject(i).getInt("id");
                banner.bName =  mallArray.getJSONObject(i).getString("bannername");
                banner.bPic =  mallArray.getJSONObject(i).getString("bannerimg");
                banner.bDesc =  mallArray.getJSONObject(i).getString("bannerdesc");
                banners.add(banner);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static BannerLib get(Context context){
        if(mLib==null){
            mLib = new BannerLib(context);
        }
        return mLib;
    }
    public List<Banner> getForBanner()
    {
        return banners;
    }
}
