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
public class DealActivity extends AppCompatActivity {
    IconicsImageView arrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.fbutton_color_alizarin));
        }
        arrow=(IconicsImageView)findViewById(R.id.backarrow);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                DealActivity.this.finish();
            }
        });
    }
}
