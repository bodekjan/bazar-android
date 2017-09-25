package com.askarstudio.firstshop.views;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.askarstudio.firstshop.R;
import com.askarstudio.firstshop.model.BannerLib;
import com.askarstudio.firstshop.model.MType;
import com.askarstudio.firstshop.model.Mall;
import com.askarstudio.firstshop.model.MallLib;
import com.askarstudio.firstshop.model.TypeLib;
import com.askarstudio.firstshop.model.UserLib;
import com.askarstudio.firstshop.utils.CommonHelper;
import com.askarstudio.firstshop.utils.GlideImageLoader;
import com.askarstudio.firstshop.utils.MallAdapter;
import com.mikepenz.iconics.view.IconicsImageView;
import com.squareup.okhttp.MediaType;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainFragment extends Fragment {
    private MainActivity parent;
    List<Mall> newMallList;
    List<com.askarstudio.firstshop.model.Banner> bannerList;
    List<MType> newTypeList;
    MallAdapter mainAdapter;
    MallAdapter newAdapter;
    RecyclerView recyclerView;
    RecyclerView newRecView;
    IconicsImageView searchBtn;
    EditText searchText;
    TextView searchTitle;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    public static MainFragment newInstance(String point){
        MainFragment fragment = new MainFragment();
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
        View view = inflater.inflate(R.layout.fragment_main, null);
        parent = (MainActivity)getActivity();
        if ((ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }else{
            Banner banner = (Banner) view.findViewById(R.id.banner);
            //设置banner样式
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
            //设置图片加载器
            banner.setImageLoader(new GlideImageLoader());
            //设置图片集合
            List<String> images =new ArrayList<>();
            List<String> names =new ArrayList<>();
            bannerList = BannerLib.get(getActivity()).getForBanner();
            for(int  i=0; i<bannerList.size();i++){
                images.add(CommonHelper.media+bannerList.get(i).bPic);
                names.add(bannerList.get(i).bName);
            }
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    int id = bannerList.get(position).bId;
                    Intent intent=new Intent(getActivity(),BannerActivity.class);
                    intent.putExtra("banneritem", id);
                    startActivity(intent);
                }
            });
            banner.setImages(images);
            //设置banner动画效果
            banner.setBannerAnimation(Transformer.DepthPage);
            //设置标题集合（当banner样式有显示title时）
            banner.setBannerTitles(names);
            //设置自动轮播，默认为true
            banner.isAutoPlay(true);
            //设置轮播时间
            banner.setDelayTime(3500);
            //设置指示器位置（当banner模式中有指示器时）
            banner.setIndicatorGravity(BannerConfig.CENTER);
            //banner设置方法全部调用完毕时最后调用
            banner.start();
        }
        LinearLayout typeOne = (LinearLayout)view.findViewById(R.id.typeone);
        LinearLayout typeTwo = (LinearLayout)view.findViewById(R.id.typetwo);
        LinearLayout typeTree = (LinearLayout)view.findViewById(R.id.typetree);
        LinearLayout typeFour = (LinearLayout)view.findViewById(R.id.typefour);
        newMallList = MallLib.get(getActivity()).getForNew();
        searchBtn = (IconicsImageView) view.findViewById(R.id.mainsearch);
        searchText = (EditText)view.findViewById(R.id.searchtext);
        searchTitle = (TextView)view.findViewById(R.id.searchtitle);
        recyclerView = (RecyclerView) view.findViewById(R.id.mallrecycler);
        newRecView = (RecyclerView) view.findViewById(R.id.newrecycler);
        newRecView.setNestedScrollingEnabled(false);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchText.getText().toString()==""){
                    return;
                }
                searchTitle.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setNestedScrollingEnabled(false);
                List<Mall> mMalls = MallLib.get(getActivity()).getForSearch(searchText.getText().toString());
                mainAdapter = new MallAdapter(mMalls,getActivity());
                recyclerView.setAdapter(mainAdapter);
            }
        });
        StaggeredGridLayoutManager newManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        newRecView.setLayoutManager(newManager);
        newAdapter = new MallAdapter(newMallList,getActivity());
        newRecView.setAdapter(newAdapter);

        newTypeList = TypeLib.get(getActivity()).getForMain();
        TextView typeOneText = (TextView)view.findViewById(R.id.typeonetext);typeOneText.setText(newTypeList.get(0).typeName);
        TextView typeTwoText = (TextView)view.findViewById(R.id.typetwotext);typeTwoText.setText(newTypeList.get(1).typeName);
        TextView typeTreeText = (TextView)view.findViewById(R.id.typetreetext);typeTreeText.setText(newTypeList.get(2).typeName);
        ImageView typeOnePic = (ImageView)view.findViewById(R.id.typeoneimg);
        Picasso.with(getActivity()).load(Uri.parse(newTypeList.get(0).typeImg)).into(typeOnePic);
        ImageView typeTwoPic = (ImageView)view.findViewById(R.id.typetwoimg);
        Picasso.with(getActivity()).load(Uri.parse(newTypeList.get(1).typeImg)).into(typeTwoPic);
        ImageView typeTreePic = (ImageView)view.findViewById(R.id.typetreeimg);
        Picasso.with(getActivity()).load(Uri.parse(newTypeList.get(2).typeImg)).into(typeTreePic);
        typeOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.typeId = newTypeList.get(0).typeId;
                parent.setFragment(parent.LIST);
            }
        });
        typeTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.typeId = newTypeList.get(1).typeId;
                parent.setFragment(parent.LIST);
            }
        });
        typeTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.typeId = newTypeList.get(2).typeId;
                parent.setFragment(parent.LIST);
            }
        });
        typeFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.typeId = -100;
                parent.setFragment(parent.LIST);
            }
        });
        return view;
    }
}
