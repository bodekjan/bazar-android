package com.askarstudio.firstshop.views;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.askarstudio.firstshop.R;
import com.askarstudio.firstshop.model.Cart;
import com.askarstudio.firstshop.model.CartLib;
import com.askarstudio.firstshop.model.Mall;
import com.askarstudio.firstshop.model.MallLib;
import com.askarstudio.firstshop.model.TypeLib;
import com.askarstudio.firstshop.model.UserLib;
import com.askarstudio.firstshop.utils.CartAdapter;
import com.askarstudio.firstshop.utils.CommonHelper;
import com.askarstudio.firstshop.utils.MallAdapter;
import com.askarstudio.firstshop.utils.TypeAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    List<Cart> mCarts;
    CartAdapter cartAdapter;
    RecyclerView recyclerView;
    TextView mallTotal;
    double mallTotalNum;
    DecimalFormat df;
    Button buBtn;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    public static CartFragment newInstance(String point){
        CartFragment fragment = new CartFragment();
        if(point!=null){
            Bundle bundle=new Bundle();
            bundle.putSerializable("point",point);
            fragment.setArguments(bundle);
        }
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, null);
        if ((ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }else{

        }
        df= new DecimalFormat("######0.00");
        buBtn = (Button)view.findViewById(R.id.buycart);
        buBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),BillActivity.class);
                //intent.put("carts", mCarts);
                startActivity(intent);
            }
        });

        mallTotal = (TextView) view.findViewById(R.id.malltotal);
        recyclerView = (RecyclerView) view.findViewById(R.id.cartrecycler);
        if(UserLib.get(getActivity()).checkUser() && CommonHelper.token!=""){
            mCarts = new ArrayList<Cart>();
            mCarts = CartLib.get(getActivity()).getCarts(UserLib.get(getActivity()).getUser());
            cartAdapter = new CartAdapter(mCarts,getActivity());
            cartAdapter.setOnItemClickListener(new CartAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int cartId) {
                    CartLib.get(getActivity()).deleteCart(cartId);
                    mCarts = CartLib.get(getActivity()).getCarts(UserLib.get(getActivity()).getUser());
                    cartAdapter.setData(mCarts);
                    cartAdapter.notifyDataSetChanged();
                    mallTotalNum=0d;
                    for(int i=0;i<mCarts.size();i++){
                        double itemTotal = mCarts.get(i).itemCount*mCarts.get(i).itemPrice;
                        if(mCarts.get(i).cartType==0){
                            itemTotal = itemTotal/1000;
                        }
                        mallTotalNum +=itemTotal;
                    }
                    String total=getResources().getString(R.string.detail_total);
                    String totalText=String.format(total,df.format(mallTotalNum));
                    mallTotal.setText(totalText);
                }
            });
            recyclerView.setAdapter(cartAdapter);
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            buBtn.setEnabled(true);
            buBtn.setBackgroundColor(getResources().getColor(R.color.red_hint));
            mallTotalNum=0d;
            for(int i=0;i<mCarts.size();i++){
                double itemTotal = mCarts.get(i).itemCount*mCarts.get(i).itemPrice;
                if(mCarts.get(i).cartType==0){
                    itemTotal = itemTotal/1000;
                }
                mallTotalNum +=itemTotal;
            }
            String total=getResources().getString(R.string.detail_total);
            String totalText=String.format(total,df.format(mallTotalNum));
            mallTotal.setText(totalText);
        }
        return view;
    }
    @Override
    public void onStart() {

        //mainAdapter.notifyDataSetChanged();
        //typeAdapter.notify();
        super.onStart();
    }
}
