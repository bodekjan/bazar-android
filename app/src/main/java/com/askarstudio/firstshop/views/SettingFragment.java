package com.askarstudio.firstshop.views;

import android.Manifest;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.askarstudio.firstshop.R;
import com.askarstudio.firstshop.model.UserLib;
import com.askarstudio.firstshop.utils.CommonHelper;
import com.askarstudio.firstshop.widgets.InfoDialog;

public class SettingFragment extends Fragment {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    Button sign;
    Button login;
    TextView phone;
    LinearLayout mDealBtn;
    LinearLayout mAddressBtn;
    LinearLayout mManualBtn;
    LinearLayout mAbout;
    public static SettingFragment newInstance(String point){
        SettingFragment fragment = new SettingFragment();
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
        View view = inflater.inflate(R.layout.fragment_setting, null);
        if ((ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }else{

        }
        phone = (TextView) view.findViewById(R.id.phone);
        sign = (Button) view.findViewById(R.id.btnSign);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),RegActivity.class);
                startActivity(intent);
            }
        });
        login = (Button) view.findViewById(R.id.btnLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
            }
        });
        mDealBtn = (LinearLayout) view.findViewById(R.id.mdealbtn);
        mDealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),DealActivity.class);
                getActivity().startActivity(i);
            }
        });
        mAddressBtn = (LinearLayout) view.findViewById(R.id.maddressbtn);
        mAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialog.Builder builder = new InfoDialog.Builder(getActivity());
                builder.setMessage("مەزكۇر ئەپ تېخى ئۈندىدار ئورگان تەرەپ سېستىمىسىغا ئۇلانمىدى، ھازىرچە ھەمبەھىرلىگىلى بولمايدۇ.");
                builder.setTitle("ئەسكەرتىش");
                builder.setNegativeButton("بىلدىم",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
            }
        });
        mManualBtn = (LinearLayout) view.findViewById(R.id.mmanualbtn);
        mManualBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),WebActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("url","file:///android_asset/money.html");
                bundle.putSerializable("title",getResources().getText(R.string.app_name).toString());
                i.putExtras(bundle);
                getActivity().startActivity(i);
            }
        });
        mAbout = (LinearLayout) view.findViewById(R.id.maboutbtn);
        mAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),WebActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("url","file:///android_asset/about.html");
                bundle.putSerializable("title",getResources().getText(R.string.app_name).toString());
                i.putExtras(bundle);
                getActivity().startActivity(i);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        if(UserLib.get(getActivity()).checkUser() && CommonHelper.token!=""){
            sign.setVisibility(View.GONE);
            login.setVisibility(View.GONE);
            phone.setVisibility(View.VISIBLE);
            phone.setText(UserLib.get(getActivity()).getUser().userPhone);
        }
        super.onStart();
    }
}
