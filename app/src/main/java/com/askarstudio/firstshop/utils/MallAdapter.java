package com.askarstudio.firstshop.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.askarstudio.firstshop.R;
import com.askarstudio.firstshop.model.Mall;
import com.askarstudio.firstshop.views.MallActivity;
import com.askarstudio.firstshop.views.MallKActivity;
import com.askarstudio.firstshop.views.MallSActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by bodekjan on 2017/9/4.
 */
public class MallAdapter extends RecyclerView.Adapter<MallAdapter.ViewHolder> {
    Context mContext;
    private List<Mall> mMalls;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mallImage;
        TextView mallName;
        TextView mallPrice;
        View mallView;
        public ViewHolder(View view){
            super(view);
            mallView = view;
            mallImage = (ImageView) view.findViewById(R.id.mallPic);
            mallName = (TextView) view.findViewById(R.id.mallName);
            mallPrice = (TextView) view.findViewById(R.id.mallPrice);
        }
    }
    public void setData(List<Mall> mallList){
        mMalls = mallList;
    }
    public MallAdapter(List<Mall> mallList, Context context){
        mMalls = mallList;
        mContext = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mall_recycleritem,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.mallView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                int mallId = mMalls.get(position).mallId;
                if(mMalls.get(position).priceType == 0){
                    Intent intent=new Intent(mContext,MallActivity.class);
                    intent.putExtra("gooditem", mallId);
                    mContext.startActivity(intent);
                }else if(mMalls.get(position).priceType == 1) {
                    Intent intent=new Intent(mContext,MallKActivity.class);
                    intent.putExtra("gooditem", mallId);
                    mContext.startActivity(intent);
                }else if(mMalls.get(position).priceType == 2) {
                    Intent intent=new Intent(mContext,MallSActivity.class);
                    intent.putExtra("gooditem", mallId);
                    mContext.startActivity(intent);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Mall mall = mMalls.get(position);
        holder.mallName.setText(mall.mallName);
        holder.mallPrice.setText(mall.mallPrice+"يۈەن");

        //从网络加载图片
        Picasso.with(mContext).load(Uri.parse(mall.mallURL)).into(holder.mallImage);
    }

    @Override
    public int getItemCount() {
        return mMalls.size();
    }
}
