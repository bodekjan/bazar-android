package com.askarstudio.firstshop.views;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.askarstudio.firstshop.R;
import com.askarstudio.firstshop.model.BannerLib;
import com.askarstudio.firstshop.model.MType;
import com.askarstudio.firstshop.model.MallLib;
import com.askarstudio.firstshop.model.ServiceLib;
import com.askarstudio.firstshop.model.TypeLib;
import com.askarstudio.firstshop.model.User;
import com.askarstudio.firstshop.model.UserLib;
import com.askarstudio.firstshop.utils.CommonHelper;
import com.askarstudio.firstshop.widgets.NiceSpinner;
import com.askarstudio.firstshop.widgets.TypefaceSpan;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
// love github
public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 21;
    public static final String TAG = "MainActivity";
    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    public static final String INDEX="indexpage";
    public static final String LIST="infopage";
    public static final String CART="cartpage";
    public static final String SETTING="settingpage";
    FragmentManager fragmentManager;
    private MainFragment mainFragment;
    private TypeFragment typeFragment;
    private CartFragment cartFragment;
    private SettingFragment settingFragment;
    public int typeId = -100;
    Button button;
    IconicsImageView indexTabIcon;
    IconicsImageView listTabIcon;
    IconicsImageView cartTabIcon;
    IconicsImageView settingTabIcon;
    TextView indexTabLabel;
    TextView listTabLabel;
    TextView cartTabLabel;
    TextView settingTabLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.fbutton_color_alizarin));
        }
        indexTabIcon=(IconicsImageView)findViewById(R.id.tabindeximg);
        listTabIcon=(IconicsImageView)findViewById(R.id.tablistimg);
        cartTabIcon=(IconicsImageView)findViewById(R.id.tabcartimg);
        settingTabIcon=(IconicsImageView)findViewById(R.id.tabsettingimg);
        indexTabLabel=(TextView)findViewById(R.id.tabindextext);
        listTabLabel=(TextView)findViewById(R.id.tablisttext);
        cartTabLabel=(TextView)findViewById(R.id.tabcarttext);
        settingTabLabel=(TextView)findViewById(R.id.tabsettingtext);
        fragmentManager=getFragmentManager();
        new InitData().execute("");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    void initLayout(){
        Intent intentFrom=getIntent();
        if(intentFrom.getStringExtra("page")!=null){
            setFragment(intentFrom.getStringExtra("page"));
        }else{
            setFragment(INDEX);
        }
        LinearLayout linearLayoutMap=(LinearLayout)findViewById(R.id.tabindex);
        linearLayoutMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(INDEX);
            }
        });
        LinearLayout linearLayoutList=(LinearLayout)findViewById(R.id.tablist);
        linearLayoutList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(LIST);
            }
        });
        LinearLayout linearLayoutCart=(LinearLayout)findViewById(R.id.tabcart);
        linearLayoutCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(CART);
            }
        });
        LinearLayout linearLayoutStting=(LinearLayout)findViewById(R.id.tabsetting);
        linearLayoutStting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(SETTING);
            }
        });
    }
    class InitData extends AsyncTask<String, Integer, String> {
        // 可变长的输入参数，与AsyncTask.exucute()对应
        SpotsDialog spotsDialog;
        SpannableString s;
        @Override
        protected void onPreExecute() {
            // 任务启动，可以在这里显示一个对话框，这里简单处理
            //message.setText(R.string.task_started);
            s = new SpannableString( "مۇلازىمىرغا ئۇلىنىۋاتىدۇ...");
            s.setSpan(new TypefaceSpan(MainActivity.this, "ALKATIP.TTF"), 0, s.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spotsDialog = new SpotsDialog(MainActivity.this,s);
            spotsDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try{
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(CommonHelper.api+"types")
                        .get()
                        .build();
                try {
                    Response response=okHttpClient.newCall(request).execute();
                    if(response.isSuccessful()){
                        //打印服务端返回结
                        String responseStr = response.body().string();
                        TypeLib.get(MainActivity.this).initType(responseStr);
                        Log.i(TAG,responseStr);
                    }
                } catch (IOException e)
                {
                    e.printStackTrace();
                    return "error";
                }
                //////////////////////////////
                OkHttpClient okHttpClient2 = new OkHttpClient();
                Request request2 = new Request.Builder()
                        .url(CommonHelper.api+"goods")
                        .get()
                        .build();
                try {
                    Response response2=okHttpClient2.newCall(request2).execute();
                    if(response2.isSuccessful()){
                        //打印服务端返回结
                        String responseStr2 = response2.body().string();
                        MallLib.get(MainActivity.this).initMall(responseStr2);
                        Log.i(TAG,responseStr2);
                    }
                } catch (IOException e)
                {
                    e.printStackTrace();
                    return "error";
                }//////////////////////////////
                OkHttpClient okHttpClient5 = new OkHttpClient();
                Request request5 = new Request.Builder()
                        .url(CommonHelper.api+"banner")
                        .get()
                        .build();
                try {
                    Response response5=okHttpClient5.newCall(request5).execute();
                    if(response5.isSuccessful()){
                        //打印服务端返回结
                        String responseStr5 = response5.body().string();
                        BannerLib.get(MainActivity.this).initBanner(responseStr5);
                        Log.i(TAG,responseStr5);
                    }
                } catch (IOException e)
                {
                    e.printStackTrace();
                    return "error";
                }//////////////////////////////
                OkHttpClient okHttpClient4 = new OkHttpClient();
                Request request4 = new Request.Builder()
                        .url(CommonHelper.api+"service")
                        .get()
                        .build();
                try {
                    Response response4=okHttpClient2.newCall(request4).execute();
                    if(response4.isSuccessful()){
                        //打印服务端返回结
                        String responseStr4 = response4.body().string();
                        ServiceLib.get(MainActivity.this).initService(responseStr4);
                        Log.i(TAG,responseStr4);
                    }
                } catch (IOException e)
                {
                    e.printStackTrace();
                    return "error";
                }
                ///////////////////////////////////////////////
                if(UserLib.get(MainActivity.this).checkUser()){
                    User user =UserLib.get(MainActivity.this).getUser();
                    JSONObject json = new JSONObject();
                    json.put("userphone", user.userPhone);
                    json.put("userpass",user.userPass);

                    OkHttpClient okHttpClient3 = new OkHttpClient();
                    RequestBody requestBody3 = RequestBody.create(JSON,json.toString());
                    Request request3 = new Request.Builder()
                            .url(CommonHelper.api+"generate")
                            .post(requestBody3)
                            .build();
                    try {
                        Response response3=okHttpClient3.newCall(request3).execute();
                        if(response3.isSuccessful()){
                            //打印服务端返回结
                            String responseStr3 = response3.body().string();
                            JSONObject jsonToken = new JSONObject(responseStr3);
                            if(jsonToken.get("message").toString().equals("success")){
                                CommonHelper.token = jsonToken.get("token").toString();
                                return "success";
                            }
                        }
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                        //return "error";
                    }
                    return "success";
                }else{
                    return "success";
                }
            } catch(Exception e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String result) {

            if(result.equals("success"))
            {
                initLayout();
                spotsDialog.dismiss();
            }else {
                FrameLayout layout = (FrameLayout)findViewById(R.id.fragments);
                layout.setVisibility(View.GONE);
                ImageView image = (ImageView)findViewById(R.id.neterror);
                image.setVisibility(View.VISIBLE);
                spotsDialog.dismiss();
            }
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            // 更新进度
            System.out.println(""+values[0]);
        }
    }
    public void setFragment(String page){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (page){
            case INDEX:
                if(mainFragment==null) mainFragment=MainFragment.newInstance("");
                if (!mainFragment.isAdded()) {
                    transaction.replace(R.id.fragments, mainFragment);
                    transaction.commit();
                } else {
                    transaction.show(mainFragment);
                }
                break;
            case LIST:
                if(typeFragment ==null) typeFragment = TypeFragment.newInstance(typeId);
                typeFragment.tId = typeId;
                if (!typeFragment.isAdded()) {
                    transaction.replace(R.id.fragments, typeFragment);
                    transaction.commit();
                } else {
                    transaction.show(typeFragment);
                }
                break;
            case CART:
                if(cartFragment==null) cartFragment=CartFragment.newInstance("");
                if (!cartFragment.isAdded()) {
                    transaction.replace(R.id.fragments, cartFragment);
                    transaction.commit();
                } else {
                    transaction.show(cartFragment);
                }
                break;
            case SETTING:
                if(settingFragment==null) settingFragment=SettingFragment.newInstance("");
                if (!settingFragment.isAdded()) {
                    transaction.replace(R.id.fragments, settingFragment);
                    transaction.commit();
                } else {
                    transaction.show(settingFragment);
                }
                break;
        }
        changeTabState(page);
    }
    private void changeTabState(String page){
        indexTabIcon.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_home).sizeDp(22).color(getResources().getColor(android.R.color.darker_gray)));
        listTabIcon.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_list).sizeDp(22).color(getResources().getColor(android.R.color.darker_gray)));
        cartTabIcon.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_shopping_cart).sizeDp(22).color(getResources().getColor(android.R.color.darker_gray)));
        settingTabIcon.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_verified_user).sizeDp(22).color(getResources().getColor(android.R.color.darker_gray)));
        indexTabLabel.setTextColor(getResources().getColor(R.color.textcolor));
        listTabLabel.setTextColor(getResources().getColor(R.color.textcolor));
        cartTabLabel.setTextColor(getResources().getColor(R.color.textcolor));
        settingTabLabel.setTextColor(getResources().getColor(R.color.textcolor));
        switch (page){
            case INDEX:
                indexTabIcon.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_home).sizeDp(22).color(getResources().getColor(R.color.fbutton_color_alizarin)));
                indexTabLabel.setTextColor(getResources().getColor(R.color.fbutton_color_alizarin));
                break;
            case LIST:
                listTabIcon.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_list).sizeDp(22).color(getResources().getColor(R.color.fbutton_color_alizarin)));
                listTabLabel.setTextColor(getResources().getColor(R.color.fbutton_color_alizarin));
                break;
            case CART:
                cartTabIcon.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_shopping_cart).sizeDp(22).color(getResources().getColor(R.color.fbutton_color_alizarin)));
                cartTabLabel.setTextColor(getResources().getColor(R.color.fbutton_color_alizarin));
                break;
            case SETTING:
                settingTabIcon.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_verified_user).sizeDp(22).color(getResources().getColor(R.color.fbutton_color_alizarin)));
                settingTabLabel.setTextColor(getResources().getColor(R.color.fbutton_color_alizarin));
                break;
        }
    }
    public void checkUser(){
        if(UserLib.get(MainActivity.this).checkUser()!=true)
        {

        }
    }
}
