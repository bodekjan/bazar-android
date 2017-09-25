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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.askarstudio.firstshop.R;
import com.askarstudio.firstshop.model.CartLib;
import com.askarstudio.firstshop.model.Mall;
import com.askarstudio.firstshop.model.MallLib;
import com.askarstudio.firstshop.model.TypeLib;
import com.askarstudio.firstshop.model.User;
import com.askarstudio.firstshop.model.UserLib;
import com.askarstudio.firstshop.utils.CommonHelper;
import com.askarstudio.firstshop.widgets.InfoDialog;
import com.askarstudio.firstshop.widgets.NiceSpinner;
import com.askarstudio.firstshop.widgets.TypefaceSpan;
import com.mikepenz.iconics.view.IconicsImageView;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

/**
 * Created by bodekjan on 2017/9/5.
 */
public class MallActivity extends AppCompatActivity {
    ImageView bigImage;
    TextView mallText;
    TextView mallPrice;
    TextView mallTotal;
    TextView mallDesc;
    TextView mallCount;
    TextView mallLeft;
    TextView countLabel;
    IconicsImageView plusBtn;
    IconicsImageView minusBtn;
    double mallPriceNum;
    double mallLeftNum;
    double mallTotalNum;
    int mallId;
    Button addCart;
    DecimalFormat df;
    IconicsImageView arrow;
    String mallUrl;
    String userPhone;
    int priceType = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.fbutton_color_alizarin));
        }
        arrow=(IconicsImageView)findViewById(R.id.backarrow);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                MallActivity.this.finish();
            }
        });
        df= new DecimalFormat("######0.00");
        mallPriceNum=0;
        mallLeftNum=0;
        mallTotalNum=0;
        addCart  = (Button) findViewById(R.id.addtocart);
        mallText = (TextView) findViewById(R.id.malltitle);
        mallPrice = (TextView) findViewById(R.id.mallprice);
        mallLeft = (TextView) findViewById(R.id.mallleft);
        countLabel = (TextView) findViewById(R.id.countlabel);
        countLabel.setText(getResources().getText(R.string.counttext));
        mallDesc = (TextView) findViewById(R.id.malldesc);
        mallCount = (TextView) findViewById(R.id.mallcount);
        mallTotal = (TextView) findViewById(R.id.malltotal);
        bigImage = (ImageView) findViewById(R.id.mallbigpic);
        plusBtn = (IconicsImageView) findViewById(R.id.detailplus);
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double count = Double.valueOf(mallCount.getText().toString());
                if((count)<mallLeftNum*1000){
                    count=count+100.0d;
                    mallCount.setText(count+"");
                    mallTotalNum = ((count)*mallPriceNum)/1000;
                    String total=getResources().getString(R.string.detail_total);
                    String totalText=String.format(total,df.format(mallTotalNum));
                    mallTotal.setText(totalText);
                }
                if(mallTotalNum>0){
                    addCart.setEnabled(true);
                    addCart.setBackgroundColor(getResources().getColor(R.color.red_hint));
                }
            }
        });
        minusBtn = (IconicsImageView) findViewById(R.id.detailminus);
        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double count = Double.valueOf(mallCount.getText().toString());
                if(count-100.0>0){
                    count=count-100.0d;
                    mallCount.setText(count+"");
                    mallTotalNum = ((count)*mallPriceNum)/1000;
                    String total=getResources().getString(R.string.detail_total);
                    String totalText=String.format(total,df.format(mallTotalNum));
                    mallTotal.setText(totalText);
                }
                if(mallTotalNum>0){
                    addCart.setEnabled(true);
                    addCart.setBackgroundColor(getResources().getColor(R.color.red_hint));
                }
            }
        });
        String price=getResources().getString(R.string.detail_price);
        String priceText=String.format(price,mallPriceNum);
        mallPrice.setText(priceText);
        String left=getResources().getString(R.string.detail_left);
        String leftText=String.format(left,mallLeftNum);
        mallLeft.setText(leftText);
        double count = Double.valueOf(mallCount.getText().toString());
        mallTotalNum = ((count)*mallPriceNum)/1000;
        String total=getResources().getString(R.string.detail_total);
        String totalText=String.format(total,df.format(mallTotalNum));
        mallTotal.setText(totalText);
        Picasso.with(MallActivity.this).load(Uri.parse("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505360878&di=66899b62c71bd461f42c3d8933ed6eb4&imgtype=jpg&er=1&src=http%3A%2F%2Fpic.qiantucdn.com%2F58pic%2F19%2F47%2F10%2F56d96b77cbde3_1024.jpg")).into(bigImage);
        mallId = this.getIntent().getIntExtra("gooditem",-1);
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserLib.get(MallActivity.this).checkUser() && CommonHelper.token!=""){
                    Mall mall = new Mall();
                    User user = new User();
                    user.userPhone = UserLib.get(MallActivity.this).getUser().userPhone;
                    mall.mallId = mallId;
                    mall.mallURL = mallUrl;
                    mall.mallPrice = (float)mallPriceNum;
                    mall.mallName = mallText.getText().toString();
                    mall.priceType = priceType;
                    CartLib.get(MallActivity.this).addCart(user,mall,Double.valueOf(mallCount.getText().toString()));
                    InfoDialog.Builder builder = new InfoDialog.Builder(MallActivity.this);
                    builder.setMessage("سېۋەتكە سېلىندى، سېۋەت كۆزنىكىدىن پۇل تۆلىسىڭىز بولىدۇ");
                    builder.setTitle("ئەسكەرتىش");
                    builder.setNegativeButton("بىلدىم",
                            new android.content.DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                    builder.create().show();
                }else{
                    InfoDialog.Builder builder = new InfoDialog.Builder(MallActivity.this);
                    builder.setMessage("تېخى كىرمەپسىز، ئالدى بىلەن ئەپكە تىزىملىتىپ كىرىپ ئاندىن مال سېتىۋالسىڭىز بولىدۇ");
                    builder.setTitle("ئەسكەرتىش");
                    builder.setNegativeButton("بىلدىم",
                            new android.content.DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                }
            }
        });
        if(mallId!=-1){
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
            s.setSpan(new TypefaceSpan(MallActivity.this, "ALKATIP.TTF"), 0, s.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spotsDialog = new SpotsDialog(MallActivity.this,s);
            spotsDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try{
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(CommonHelper.api+"goods/"+mallId)
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
                Toast.makeText(MallActivity.this,"NET ERR",Toast.LENGTH_LONG);
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
            mallUrl = jObject.getString("goodimg");
            Picasso.with(MallActivity.this).load(CommonHelper.media+Uri.parse(jObject.getString("goodimg"))).into(bigImage);
            mallText.setText(jObject.getString("goodname"));
            mallDesc.setText(jObject.getString("gooddesc"));
            mallPriceNum=jObject.getDouble("goodprice");
            mallLeftNum=jObject.getDouble("goodcount");
            priceType=jObject.getInt("pricetype");
            double count = Double.valueOf(mallCount.getText().toString());
            numFormater(count);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String numFormater(double count){
        String price=getResources().getString(R.string.detail_price);
        String priceText=String.format(price,mallPriceNum);
        mallPrice.setText(priceText);

        String left=getResources().getString(R.string.detail_left);
        String leftText=String.format(left,mallLeftNum);
        mallLeft.setText(leftText);

        mallTotalNum = ((count)*mallPriceNum)/1000;
        String total=getResources().getString(R.string.detail_total);
        String totalText=String.format(total,df.format(mallTotalNum));
        mallTotal.setText(totalText);
        if(mallTotalNum>=0){
            addCart.setEnabled(true);
            addCart.setBackgroundColor(getResources().getColor(R.color.red_hint));
        }else{
            addCart.setEnabled(false);
        }
        return "";
    }
}
