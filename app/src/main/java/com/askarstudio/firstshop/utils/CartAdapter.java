package com.askarstudio.firstshop.utils;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.askarstudio.firstshop.R;
import com.askarstudio.firstshop.model.Cart;
import com.askarstudio.firstshop.model.MType;
import com.askarstudio.firstshop.model.Mall;
import com.mikepenz.iconics.view.IconicsImageView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by bodekjan on 2017/9/4.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    Context mContext;
    private List<Cart> mCart;
    DecimalFormat df;
    private OnItemClickLitener mOnItemClickListener = null;

    public void setOnItemClickListener(OnItemClickLitener listener) {
        this.mOnItemClickListener = listener;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView mallName;
        TextView mallPrice;
        TextView mallCount;
        TextView myTotal;
        ImageView mallPic;
        IconicsImageView removeBtn;
        //View typeItem;
        //ImageView typeImage;
        public ViewHolder(View view){
            super(view);
            //typeItem = view;
            //typeImage = (ImageView) view.findViewById(R.id.typePic);
            mallName = (TextView) view.findViewById(R.id.carditemname);
            mallPrice = (TextView) view.findViewById(R.id.carditemprice);
            mallCount = (TextView) view.findViewById(R.id.carditemcount);
            myTotal = (TextView) view.findViewById(R.id.cardmycount);
            mallPic = (ImageView) view.findViewById(R.id.cartpic);
            removeBtn = (IconicsImageView) view.findViewById(R.id.cartlistbtn);
        }
    }
    public void setData(List<Cart> cartList){
        mCart = cartList;
    }
    public CartAdapter(List<Cart> cartList, Context context){
        mCart = cartList;
        mContext = context;
        df= new DecimalFormat("######0.00");
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart,parent,false);
        ViewHolder holder = new ViewHolder(view);
        //holder.typeItem.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Cart cart = mCart.get(position);
        holder.mallName.setText(cart.itemName);
        String total=mContext.getResources().getString(R.string.bill_service);
        if(cart.cartType == 0){
            String price=mContext.getResources().getString(R.string.detail_price);
            String priceText=String.format(price,df.format(cart.itemPrice));
            holder.mallPrice.setText(priceText);
            holder.mallCount.setText(cart.itemCount+"گىرام ئالىمەن");
            String totalText=String.format(total,df.format((cart.itemCount*cart.itemPrice)/1000));
            holder.myTotal.setText(totalText);
        }else if(cart.cartType == 1){
            String price=mContext.getResources().getString(R.string.detail_price);
            String priceText=String.format(price,df.format(cart.itemPrice));
            holder.mallPrice.setText(priceText);
            holder.mallCount.setText(cart.itemCount+"كىلوگىرام ئالىمەن");
            String totalText=String.format(total,df.format((cart.itemCount*cart.itemPrice)));
            holder.myTotal.setText(totalText);
        }else if(cart.cartType == 2){
            String price=mContext.getResources().getString(R.string.detail_sprice);
            String priceText=String.format(price,df.format(cart.itemPrice));
            holder.mallPrice.setText(priceText);
            holder.mallCount.setText(cart.itemCount+"دانە ئالىمەن");
            String totalText=String.format(total,df.format((cart.itemCount*cart.itemPrice)));
            holder.myTotal.setText(totalText);
        }




        Picasso.with(mContext).load(CommonHelper.media+Uri.parse(cart.itemURL)).into(holder.mallPic);
//        holder.typeItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //view.setBackgroundColor(mContext.getResources().getColor(R.color.white));
//                mOnItemClickListener.onItemClick(view, mTypes.get(position).typeId);
//            }
//        });
        //从网络加载图片
        //Picasso.with(mContext).load(Uri.parse(type.typeImg)).into(holder.typeImage);
        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(view, mCart.get(position).cartId);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mCart.size();
    }
    public interface OnItemClickLitener {
        void onItemClick(View view, int typeId);
    }
}
