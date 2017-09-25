package com.askarstudio.firstshop.model;

import android.content.Context;

import com.askarstudio.firstshop.utils.MyDatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bodekjan on 2016/9/6.
 */
public class ServiceLib {
    private MyDatabaseHelper dbHelper;
    private Context mContext;
    private List<MService> services;
    private static ServiceLib mLib;
    private ServiceLib(Context context){
        mContext=context;
        //dbHelper= new MyDatabaseHelper(mContext,"shop.db",null, 1);
    }
    public void initService(String string){
        services = new ArrayList<MService>();
        try {
            JSONArray sArray = new JSONArray(string);
            for(int i=0; i<sArray.length();i++)
            {
                MService service = new MService();
                service.id = sArray.getJSONObject(i).getInt("id");
                service.serviceName=sArray.getJSONObject(i).getString("servicename");
                service.servicePrice =sArray.getJSONObject(i).getDouble("serviceprice");
                service.status = sArray.getJSONObject(i).getInt("servicestatus");
                services.add(service);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static ServiceLib get(Context context){
        if(mLib==null){
            mLib = new ServiceLib(context);
        }
        return mLib;
    }
    public List<MService> getForAll()
    {
        return services;
    }
}
