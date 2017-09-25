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
import com.askarstudio.firstshop.model.Cart;
import com.askarstudio.firstshop.model.MService;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by bodekjan on 2017/9/4.
 */
public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {
    Context mContext;
    private List<MService> mServices;
    DecimalFormat df;
    private OnItemClickLitener mOnItemClickListener = null;

    public void setOnItemClickListener(OnItemClickLitener listener) {
        this.mOnItemClickListener = listener;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView serviceName;
        TextView servicePrice;
        IconicsImageView serviceIcon;
        public ViewHolder(View view){
            super(view);
            mView =view;
            serviceName = (TextView) view.findViewById(R.id.servicename);
            servicePrice = (TextView) view.findViewById(R.id.serviceprice);
            serviceIcon = (IconicsImageView) view.findViewById(R.id.serviceicon);
        }
    }
    public void setData(List<MService> serviceList){
        mServices = serviceList;
    }
    public ServiceAdapter(List<MService> serviceList, Context context){
        mServices = serviceList;
        mContext = context;
        df= new DecimalFormat("######0.00");
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service,parent,false);
        ViewHolder holder = new ViewHolder(view);
        //holder.typeItem.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        MService service = mServices.get(position);
        holder.serviceName.setText(service.serviceName);
        String total=mContext.getResources().getString(R.string.bill_service);
        String totalText=String.format(total,df.format(service.servicePrice));
        holder.servicePrice.setText(totalText);
        if(service.status==1 || service.status ==2){
            IconicsDrawable icon = new IconicsDrawable(mContext)
                    .icon( MaterialDesignIconic.Icon.gmi_check_circle)
                    .color(mContext.getResources().getColor(R.color.fbutton_color_alizarin))
                    .sizeDp(22).paddingDp(2);
            holder.serviceIcon.setImageDrawable(icon);
        }else {
            IconicsDrawable icon = new IconicsDrawable(mContext)
                    .icon( MaterialDesignIconic.Icon.gmi_minus_circle)
                    .color(mContext.getResources().getColor(R.color.gray))
                    .sizeDp(22).paddingDp(2);
            holder.serviceIcon.setImageDrawable(icon);
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(view, position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mServices.size();
    }
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }
}
