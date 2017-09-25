package com.askarstudio.firstshop.views;

import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.askarstudio.firstshop.R;
import com.askarstudio.firstshop.model.Cart;
import com.askarstudio.firstshop.model.CartLib;
import com.askarstudio.firstshop.model.MService;
import com.askarstudio.firstshop.model.Mall;
import com.askarstudio.firstshop.model.ServiceLib;
import com.askarstudio.firstshop.model.User;
import com.askarstudio.firstshop.model.UserLib;
import com.askarstudio.firstshop.utils.BillAdapter;
import com.askarstudio.firstshop.utils.CartAdapter;
import com.askarstudio.firstshop.utils.CommonHelper;
import com.askarstudio.firstshop.utils.ServiceAdapter;
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
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

/**
 * Created by bodekjan on 2017/9/5.
 */
public class BillActivity extends AppCompatActivity {
    IconicsImageView arrow;
    EditText uPhone;
    List<Cart> mCarts;
    List<MService> mServices;
    EditText uAddress;
    RecyclerView recyclerView;
    BillAdapter cartAdapter;
    RecyclerView serviceView;
    ServiceAdapter serviceAdapter;
    TextView mallTotal;
    double totalAll = 0;
    DecimalFormat df;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.fbutton_color_alizarin));
        }
        df= new DecimalFormat("######0.00");
        arrow=(IconicsImageView)findViewById(R.id.backarrow);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                BillActivity.this.finish();
            }
        });
        uPhone=(EditText) findViewById(R.id.uphone);
        uAddress=(EditText)findViewById(R.id.uaddress);
        User user = UserLib.get(this).getUser();
        uPhone.setText(user.userPhone);
        uAddress.setText(user.userAddress);
        mallTotal = (TextView) findViewById(R.id.malltotal);
        recyclerView = (RecyclerView) findViewById(R.id.cartrecycler);
        mCarts = new ArrayList<Cart>();
        mCarts = CartLib.get(this).getCarts(user);
        cartAdapter = new BillAdapter(mCarts,this);
        recyclerView.setAdapter(cartAdapter);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        StaggeredGridLayoutManager layoutManager2 = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        serviceView = (RecyclerView) findViewById(R.id.servicerecycler);
        mServices = new ArrayList<MService>();
        mServices = ServiceLib.get(this).getForAll();
        serviceAdapter = new ServiceAdapter(mServices,this);
        serviceAdapter.setOnItemClickListener(new ServiceAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if(mServices.get(position).status == 0){
                    mServices.get(position).status=2;
                }else if(mServices.get(position).status == 2){
                    mServices.get(position).status=0;
                }
                serviceAdapter.setData(mServices);
                serviceAdapter.notifyDataSetChanged();
                calculateTotal();
            }
        });
        serviceView.setAdapter(serviceAdapter);
        serviceView.setLayoutManager(layoutManager2);
        serviceView.setNestedScrollingEnabled(false);
        calculateTotal();
    }
    public void calculateTotal(){
        totalAll = 0;
        for(int i= 0 ;i<mCarts.size();i++){
            double tmp = 0;
            tmp = mCarts.get(i).itemPrice*mCarts.get(i).itemCount;
            if(mCarts.get(i).cartType==0){
                tmp = tmp/1000;
            }
            totalAll = totalAll+tmp;
        }
        for(int j=0; j<mServices.size();j++){
            double tmp = 0;
            if(mServices.get(j).status==1 || mServices.get(j).status ==2){
                tmp = mServices.get(j).servicePrice;
            }
            totalAll = totalAll+tmp;
        }
        String total=getResources().getString(R.string.detail_total);
        String totalText=String.format(total,df.format(totalAll));
        mallTotal.setText(totalText);
    }
}
