package com.shuiwangzhijia.wuliu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.bean.GoodsBean;
import com.shuiwangzhijia.wuliu.interfaces.ShopCartInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 商品数据适配器
 * created by wangsuli on 2018/8/17.
 */
public class GoodsDetailAdapter extends RecyclerView.Adapter<GoodsDetailAdapter.ViewHolder> {
    private final Context mContext;

    public boolean isSale() {
        return isSale;
    }

    public void setSale(boolean sale) {
        isSale = sale;
        notifyDataSetChanged();
    }

    private boolean isSale;

    private OnSaleClickListener mOnViewItemClickListener;
    private ShopCartInterface shopCartImp;

    public List<GoodsBean> getData() {
        return data;
    }

    private List<GoodsBean> data;

    private boolean fromShop = false;

    public GoodsDetailAdapter(Context context, boolean isSale, boolean fromShop) {
        mContext = context;
        this.isSale = isSale;
        this.fromShop = fromShop;
        data = new ArrayList<>();
    }


    public ShopCartInterface getShopCartInterface() {
        return shopCartImp;
    }

    public void setShopCartInterface(ShopCartInterface shopCartImp) {
        this.shopCartImp = shopCartImp;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_goods_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final GoodsBean goodsBean = getItem(position);
        holder.name.setText(goodsBean.getGname());
        holder.price.setText("￥" + goodsBean.getPprice());
        Glide.with(mContext).load(goodsBean.getPicturl()).placeholder(R.drawable.wutupian).into(holder.image);
        int count = goodsBean.getCount();
       /* if(mModelShopCart.getShoppingSingleMap().containsKey(modelDish)){
            count = mModelShopCart.getShoppingSingleMap().get(modelDish);
        }*/
        if (count <= 0) {
            holder.rightDishRemove.setVisibility(View.GONE);
            holder.rightDishAccount.setVisibility(View.GONE);
        } else {
            holder.rightDishRemove.setVisibility(View.VISIBLE);
            holder.rightDishAccount.setVisibility(View.VISIBLE);
            holder.rightDishAccount.setText(count + "");
        }
        holder.rightDishAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int countAdd = goodsBean.getCount();
                countAdd++;
                goodsBean.setCount(countAdd);
                goodsBean.setCheck(true);
                notifyItemChanged(position);
                if (shopCartImp != null) {
                    shopCartImp.add(view, position);
                }
            }
        });

        holder.rightDishRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int countRemove = goodsBean.getCount();
                if (countRemove <= 0)
                    return;
                countRemove--;
                if (countRemove == 0)
                    goodsBean.setCheck(false);
                goodsBean.setCount(countRemove);
                notifyItemChanged(position);
                if (shopCartImp != null) {
                    shopCartImp.remove(view, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (data == null) ? 0 : data.size();
//        return 3;
    }

    public void setOnSaleClickListener(OnSaleClickListener listener) {
        mOnViewItemClickListener = listener;
    }

    public void setData(List<GoodsBean> list) {
        data = list;
        notifyDataSetChanged();
    }

    public GoodsBean getItem(int position) {
        return data.get(position);
    }

    public void deleteItem(int position) {
        data.remove(position);
        notifyDataSetChanged();
    }

    public interface OnSaleClickListener {
        void onSelectClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.right_dish_remove)
        ImageView rightDishRemove;
        @BindView(R.id.right_dish_account)
        TextView rightDishAccount;
        @BindView(R.id.right_dish_add)
        ImageView rightDishAdd;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
