package com.askarstudio.firstshop.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.askarstudio.firstshop.R;
import com.askarstudio.firstshop.model.User;
import com.askarstudio.firstshop.model.UserLib;
import com.askarstudio.firstshop.utils.CommonHelper;
import com.askarstudio.firstshop.widgets.InfoDialog;
import com.askarstudio.firstshop.widgets.NiceSpinner;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    Button button;
    JSONArray placeArray;
    NiceSpinner niceSpinner;
    EditText userPhone;
    EditText userAddress;
    EditText userPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.fbutton_color_alizarin));
        }
        button = (Button) findViewById(R.id.btnSignIn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userPass.getText().toString().length()<6){
                    InfoDialog.Builder builder = new InfoDialog.Builder(RegActivity.this);
                    builder.setMessage("مەخپىي نۇمىرىڭىز چۇقۇم ئالتە ھەرىپتىن ئېشىشى كېرەك، مەخمىي نۇمۇرنى قايتا تولدۇرۇڭ");
                    builder.setTitle("ئەسكەرتىش");
                    builder.setNegativeButton("بىلدىم",
                            new android.content.DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                    builder.create().show();
                    return;
                }
                if(userAddress.getText().toString().equals("")){
                    InfoDialog.Builder builder = new InfoDialog.Builder(RegActivity.this);
                    builder.setMessage("ئادرىسنى قۇرۇق قالدۇرماي توغرا تولدۇرۇڭ، بولمىسا بىز مالنى قەيەرگە يەتكۈزۈشنى بىلمەي قالىمىز");
                    builder.setTitle("ئەسكەرتىش");
                    builder.setNegativeButton("بىلدىم",
                            new android.content.DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                    builder.create().show();
                    return;
                }
                boolean isPhone = CommonHelper.isMobile(userPhone.getText().toString());
                if(!isPhone) {
                    InfoDialog.Builder builder = new InfoDialog.Builder(RegActivity.this);
                    builder.setMessage("تولدۇرغان نۇمىرىڭىز يانفۇن نۇمىرى ئەمەستەك قىلىدۇ، ئەپنى نورمال ئىشلىتەلىشىڭىز ئۈچۈن توغرا يانفۇن نۇمىرى كىرگۈزۈڭ");
                    builder.setTitle("ئەسكەرتىش");
                    builder.setNegativeButton("بىلدىم",
                            new android.content.DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                    builder.create().show();
                    return;
                }
                new SignTask().execute("");
            }
        });
        userPhone = (EditText) findViewById(R.id.userphone);
        userAddress = (EditText) findViewById(R.id.useraddress);
        userPass = (EditText) findViewById(R.id.userpass);
    }
    class SignTask extends AsyncTask<String, Integer, String> {
        // 可变长的输入参数，与AsyncTask.exucute()对应
        int currentPlace = 0;
        String uPhone = "";
        String uAddress = "";
        String uPass = "";
        @Override
        protected void onPreExecute() {
            // 任务启动，可以在这里显示一个对话框，这里简单处理
            //message.setText(R.string.task_started);
            try {
                currentPlace = ((JSONObject)placeArray.get(niceSpinner.getSelectedIndex())).getInt("id");
                uPhone = userPhone.getText().toString();
                uPass = userPass.getText().toString();
                uAddress = userAddress.getText().toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        protected String doInBackground(String... params) {
            String s = "ERR";
            try{
                JSONObject json = new JSONObject();
                json.put("userphone", uPhone);
                json.put("useraddress",uAddress);
                json.put("userplace", currentPlace);
                json.put("userpass", uPass);

                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = RequestBody.create(JSON,json.toString());
                Request request = new Request.Builder()
                        .url(CommonHelper.api+"user")
                        .post(requestBody)
                        .build();
                try {
                    Response response=okHttpClient.newCall(request).execute();
                    if(response.isSuccessful()){
                        //打印服务端返回结果
                        if(response.code()==200) {
                            JSONObject jsonToken = new JSONObject(response.body().string());
                            Log.e("TOKEEEEEEEEEEEEEEEEEEEN",jsonToken.get("token").toString());
                            s = "SUCCESS";
                            User user = new User();
                            user.userPlace=currentPlace;
                            user.userPass=uPass;
                            user.userAddress=uAddress;
                            user.userPhone=uPhone;
                            UserLib.get(RegActivity.this).setUser(user);
                            CommonHelper.token = jsonToken.get("token").toString();
                        }else if(response.code()==201){
                            Log.e("AAA",response.body().string());
                            s = "HAS";
                        }
                    }else{
                        if(response.code()==500){
                            s = "SERR";
                        }
                    }
                } catch (IOException e)
                {
                    e.printStackTrace();
                }

            } catch(Exception e) {
                e.printStackTrace();
            }
            return s;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String result) {
            // 返回HTML页面的内容
            //message.setText(result);
            if(result.equals("ERR")){
                InfoDialog.Builder builder = new InfoDialog.Builder(RegActivity.this);
                builder.setMessage("تور يۇلىدا مەسىلە كۆرۈلدى، بىردەم تۇرۇپ قايتا سىناپ كۆرۈڭ");
                builder.setTitle("ئەسكەرتىش");
                builder.setNegativeButton("بىلدىم",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
            }else if(result.equals("SERR")){
                InfoDialog.Builder builder = new InfoDialog.Builder(RegActivity.this);
                builder.setMessage("مۇلازىمىردا كاشىلا كۆرۈلدى، ھازىر جىددىي رىمۇنت قىلىنىۋاتىدۇ، سەل تۇرۇپ قايتا سىناڭ");
                builder.setTitle("ئەسكەرتىش");
                builder.setNegativeButton("بىلدىم",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
            }else if(result.equals("HAS")){
                InfoDialog.Builder builder = new InfoDialog.Builder(RegActivity.this);
                builder.setMessage("مەزكۇر يانفۇن نۇمىرى ئىشلىتىلىپ بولۇپتۇ، باشقا يانفۇن نۇمىرى ئارقىلىق تىزىملىتىڭ");
                builder.setTitle("ئەسكەرتىش");
                builder.setNegativeButton("بىلدىم",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
            }else if(result.equals("SUCCESS")){
                InfoDialog.Builder builder = new InfoDialog.Builder(RegActivity.this);
                builder.setMessage("مۇبارەك بولسۇن! تىزىملىتىش مۇۋاپىقىيەتلىك بولدى، ھازىر باش كۆزنەككە قايتىپ مال سېتىۋالسىڭىز بولىدۇ.");
                builder.setTitle("ئەسكەرتىش");
                builder.setNegativeButton("بىلدىم",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                builder.create().show();
            }
        }



        @Override
        protected void onProgressUpdate(Integer... values) {
            // 更新进度
            System.out.println(""+values[0]);
            //message.setText(""+values[0]);
        }

    }
    class PlaceTask extends AsyncTask<String, Integer, String> {
        // 可变长的输入参数，与AsyncTask.exucute()对应
        @Override
        protected void onPreExecute() {
            // 任务启动，可以在这里显示一个对话框，这里简单处理
            //message.setText(R.string.task_started);
        }
        @Override
        protected String doInBackground(String... params) {

            try{
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(CommonHelper.api+"places")
                        .get()
                        .build();
                try {
                    Response response=okHttpClient.newCall(request).execute();
                    if(response.isSuccessful()){
                        //打印服务端返回结果

                        String responseStr = response.body().string();
                        placeArray = new JSONArray(responseStr);
                        Log.i(TAG,responseStr);
                        return "success";
                    }
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                return "error";
            } catch(Exception e) {
                e.printStackTrace();

            }
            return null;

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("success"))
            {
                ArrayList<Object> dataset = new ArrayList<Object>();
                for(int i=0 ; i<placeArray.length(); i++){
                    try {
                        dataset.add(placeArray.getJSONObject(i).getString("placename"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                niceSpinner = (NiceSpinner) findViewById(R.id.nice_spinner);
                Typeface tf= Typeface.createFromAsset(RegActivity.this.getAssets(), "fonts/ALKATIP.TTF");
                niceSpinner.setTypeface(tf);
                niceSpinner.setGravity(Gravity.RIGHT);
                if(dataset.size()!=0){
                    niceSpinner.attachDataSource(dataset);
                }
            }
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            // 更新进度
            System.out.println(""+values[0]);
            //message.setText(""+values[0]);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        new PlaceTask().execute("");
    }
}
