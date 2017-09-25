package com.askarstudio.firstshop.utils;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.askarstudio.firstshop.R;
import com.askarstudio.firstshop.model.MType;
import com.askarstudio.firstshop.model.Mall;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by bodekjan on 2017/9/4.
 */
public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.ViewHolder> {
    Context mContext;
    private List<MType> mTypes;
    private OnItemClickLitener mOnItemClickListener = null;

    public void setOnItemClickListener(OnItemClickLitener listener) {
        this.mOnItemClickListener = listener;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView typeName;
        View typeItem;
        //ImageView typeImage;
        public ViewHolder(View view){
            super(view);
            typeItem = view;
            //typeImage = (ImageView) view.findViewById(R.id.typePic);
            typeName = (TextView) view.findViewById(R.id.typeName);
        }
    }
    public TypeAdapter(List<MType> typeList, Context context){
        mTypes = typeList;
        mContext = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.type_recycleritem,parent,false);
        ViewHolder holder = new ViewHolder(view);
        //holder.typeItem.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        MType type = mTypes.get(position);
        holder.typeName.setText(type.typeName);
        holder.typeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //view.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                mOnItemClickListener.onItemClick(view, mTypes.get(position).typeId);
            }
        });
        //从网络加载图片
        //Picasso.with(mContext).load(Uri.parse(type.typeImg)).into(holder.typeImage);
    }

    @Override
    public int getItemCount() {
        return mTypes.size();
    }
    public interface OnItemClickLitener {
        void onItemClick(View view, int typeId);
    }
}
