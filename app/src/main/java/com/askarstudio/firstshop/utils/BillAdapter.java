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
import com.mikepenz.iconics.view.IconicsImageView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by bodekjan on 2017/9/4.
 */
public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {
    Context mContext;
    private List<Cart> mCart;
    DecimalFormat df;
    private OnItemClickLitener mOnItemClickListener = null;

    public void setOnItemClickListener(OnItemClickLitener listener) {
        this.mOnItemClickListener = listener;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView mallName;
        TextView mallCount;
        ImageView mallPic;
        //View typeItem;
        //ImageView typeImage;
        public ViewHolder(View view){
            super(view);
            //typeItem = view;
            //typeImage = (ImageView) view.findViewById(R.id.typePic);
            mallName = (TextView) view.findViewById(R.id.carditemname);
            mallCount = (TextView) view.findViewById(R.id.carditemcount);
            mallPic = (ImageView) view.findViewById(R.id.cartpic);
        }
    }
    public void setData(List<Cart> cartList){
        mCart = cartList;
    }
    public BillAdapter(List<Cart> cartList, Context context){
        mCart = cartList;
        mContext = context;
        df= new DecimalFormat("######0.00");
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill,parent,false);
        ViewHolder holder = new ViewHolder(view);
        //holder.typeItem.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Cart cart = mCart.get(position);
        holder.mallName.setText(cart.itemName);
        String total=mContext.getResources().getString(R.string.bill_service);
        if(cart.cartType==0){
            String totalText=String.format(total,df.format((cart.itemCount*cart.itemPrice)/1000));
            holder.mallCount.setText(totalText);
        }else{
            String totalText=String.format(total,df.format((cart.itemCount*cart.itemPrice)));
            holder.mallCount.setText(totalText);
        }

        Picasso.with(mContext).load(CommonHelper.media+Uri.parse(cart.itemURL)).into(holder.mallPic);
    }

    @Override
    public int getItemCount() {
        return mCart.size();
    }
    public interface OnItemClickLitener {
        void onItemClick(View view, int typeId);
    }
}
