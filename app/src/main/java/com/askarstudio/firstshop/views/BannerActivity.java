package com.askarstudio.firstshop.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.askarstudio.firstshop.R;
import com.askarstudio.firstshop.model.CartLib;
import com.askarstudio.firstshop.model.Mall;
import com.askarstudio.firstshop.model.User;
import com.askarstudio.firstshop.model.UserLib;
import com.askarstudio.firstshop.utils.CommonHelper;
import com.askarstudio.firstshop.widgets.InfoDialog;
import com.askarstudio.firstshop.widgets.TypefaceSpan;
import com.mikepenz.iconics.view.IconicsImageView;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;

import dmax.dialog.SpotsDialog;

/**
 * Created by bodekjan on 2017/9/5.
 */
public class BannerActivity extends AppCompatActivity {
    ImageView bigImage;
    TextView mallText;
    int bannerId;
    IconicsImageView arrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.fbutton_color_alizarin));
        }
        arrow=(IconicsImageView)findViewById(R.id.backarrow);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                BannerActivity.this.finish();
            }
        });
        mallText = (TextView) findViewById(R.id.malltitle);
        bigImage = (ImageView) findViewById(R.id.mallbigpic);
        bannerId = this.getIntent().getIntExtra("banneritem",-1);
        if(bannerId!=-1){
            new InitData().execute("");
        }
    }
    class InitData extends AsyncTask<String, Integer, String> {
        // 可变长的输入参数，与AsyncTask.exucute()对应
        SpotsDialog spotsDialog;
        SpannableString s;
        JSONObject jsonObject;
        @Override
        protected void onPreExecute() {
            // 任务启动，可以在这里显示一个对话框，这里简单处理
            //message.setText(R.string.task_started);
            s = new SpannableString( "مۇلازىمىرغا ئۇلىنىۋاتىدۇ...");
            s.setSpan(new TypefaceSpan(BannerActivity.this, "ALKATIP.TTF"), 0, s.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spotsDialog = new SpotsDialog(BannerActivity.this,s);
            spotsDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try{
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(CommonHelper.api+"banner/"+bannerId)
                        .get()
                        .build();
                try {
                    Response response=okHttpClient.newCall(request).execute();
                    if(response.isSuccessful()){
                        //打印服务端返回结
                        String responseStr = response.body().string();
                        jsonObject = new JSONObject(responseStr);
                        return "success";
                    }
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                return "error";
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
                initLayout(jsonObject);
                spotsDialog.dismiss();
            }else {
                spotsDialog.dismiss();
                Toast.makeText(BannerActivity.this,"NET ERR",Toast.LENGTH_LONG);
            }
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            // 更新进度
            System.out.println(""+values[0]);
        }
    }
    public void initLayout(JSONObject jObject){
        try {
            Picasso.with(BannerActivity.this).load(CommonHelper.media+Uri.parse(jObject.getString("bannerimg"))).into(bigImage);
            mallText.setText(jObject.getString("bannerdesc"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
