package com.askarstudio.firstshop.views;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.askarstudio.firstshop.R;
import com.askarstudio.firstshop.model.User;
import com.askarstudio.firstshop.model.UserLib;
import com.askarstudio.firstshop.utils.CommonHelper;
import com.askarstudio.firstshop.widgets.InfoDialog;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    Button button;
    EditText userPhone;
    EditText userPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.fbutton_color_alizarin));
        }
        button = (Button) findViewById(R.id.btnLog);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoginTask().execute("");
            }
        });
        userPhone = (EditText) findViewById(R.id.userphone);
        userPass = (EditText) findViewById(R.id.userpass);
    }
    class LoginTask extends AsyncTask<String, Integer, String> {
        // 可变长的输入参数，与AsyncTask.exucute()对应
        String uPhone = "";
        String uPass = "";
        @Override
        protected void onPreExecute() {
            // 任务启动，可以在这里显示一个对话框，这里简单处理
            //message.setText(R.string.task_started);

            uPhone = userPhone.getText().toString();
            uPass = userPass.getText().toString();
        }
        @Override
        protected String doInBackground(String... params) {
            String s = "ERR";
            try{
                JSONObject json = new JSONObject();
                json.put("userphone", uPhone);
                json.put("userpass", uPass);
                OkHttpClient okHttpClient = new OkHttpClient();
                //Log.d("AAA",json.toString());
                RequestBody requestBody = RequestBody.create(JSON,json.toString());
                Request request = new Request.Builder()
                        .url(CommonHelper.api+"generate")
                        .post(requestBody)
                        .build();
                try {
                    Response response=okHttpClient.newCall(request).execute();
                    if(response.isSuccessful()){
                        //打印服务端返回结果
                        JSONObject jsonToken = new JSONObject(response.body().string());
                        if(jsonToken.get("message").equals("success")){
                            s = "SUCCESS";
                            User user = new User();
                            user.userPhone=uPhone;
                            user.userAddress = jsonToken.get("uaddress").toString();
                            user.userPlace = (int)jsonToken.get("uplease");
                            user.userPass=uPass;
                            UserLib.get(LoginActivity.this).setUser(user);
                            CommonHelper.token = jsonToken.get("token").toString();
                        }else{
                            s = "WRONG";
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
                InfoDialog.Builder builder = new InfoDialog.Builder(LoginActivity.this);
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
                InfoDialog.Builder builder = new InfoDialog.Builder(LoginActivity.this);
                builder.setMessage("مۇلازىمىردا كاشىلا كۆرۈلدى، ھازىر جىددىي رىمۇنت قىلىنىۋاتىدۇ، سەل تۇرۇپ قايتا سىناڭ");
                builder.setTitle("ئەسكەرتىش");
                builder.setNegativeButton("بىلدىم",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
            } else if(result.equals("WRONG")){
                InfoDialog.Builder builder = new InfoDialog.Builder(LoginActivity.this);
                builder.setMessage("مەخپىي نۇمۇر خاتا ئىكەن، توغرا مەخپىي نۇمۇرنى كىرگۈزۈڭ، تېخى تىزىملاتمىغان بولسىڭىز تىزىملىتىش كۆزنىكىگە يۆتكۈلۈپ تىزىملىتىڭ");
                builder.setTitle("ئەسكەرتىش");
                builder.setNegativeButton("بىلدىم",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
            }else if(result.equals("SUCCESS")){
                InfoDialog.Builder builder = new InfoDialog.Builder(LoginActivity.this);
                builder.setMessage("كىرىش مۇۋاپىقىيەتلىك بولدى، ئەمدى باش كۆزنەككە يۆتكۈلۈپ مال سېتىۋالسىڭىز بولىدۇ.");
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
}
