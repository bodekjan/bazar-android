package com.askarstudio.firstshop.views;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.askarstudio.firstshop.R;
import com.askarstudio.firstshop.model.MType;
import com.askarstudio.firstshop.model.Mall;
import com.askarstudio.firstshop.model.MallLib;
import com.askarstudio.firstshop.model.TypeLib;
import com.askarstudio.firstshop.utils.MallAdapter;
import com.askarstudio.firstshop.utils.TypeAdapter;

import java.util.ArrayList;
import java.util.List;

public class TypeFragment extends Fragment {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    public int tId;
    RecyclerView recyclerView;
    RecyclerView typesView;
    MallAdapter mainAdapter;
    TypeAdapter typeAdapter;
    List<Mall> mMalls;
    List<MType> mTypes;
    public static TypeFragment newInstance(int id){
        TypeFragment fragment = new TypeFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("id",id);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, null);
        if ((ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }else{
        }
        mMalls = new ArrayList<Mall>();
        mTypes = new ArrayList<MType>();
        mTypes = TypeLib.get(getActivity()).getForAll();
        if(tId!=-100){
            mMalls = MallLib.get(getActivity()).getForType(tId);
        }else{
            mMalls = MallLib.get(getActivity()).getForAll();
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.mallrecycler);
        typesView = (RecyclerView) view.findViewById(R.id.typerecycler);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        StaggeredGridLayoutManager typeLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        typesView.setLayoutManager(typeLayoutManager);
        mainAdapter = new MallAdapter(mMalls,getActivity());
        typeAdapter = new TypeAdapter(mTypes,getActivity());
        typeAdapter.setOnItemClickListener(new TypeAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int typeId) {
                Log.d("DEBUG",typeId+"");
                typeAdapter.notifyDataSetChanged();
                //view.setBackgroundColor(getActivity().getResources().getColor(R.color.red_hint));
                mMalls = MallLib.get(getActivity()).getForType(typeId);
                mainAdapter.setData(mMalls);
                mainAdapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(mainAdapter);
        typesView.setAdapter(typeAdapter);
        return view;
    }

    @Override
    public void onStart() {

        //mainAdapter.notifyDataSetChanged();
        //typeAdapter.notify();
        super.onStart();
    }
}
