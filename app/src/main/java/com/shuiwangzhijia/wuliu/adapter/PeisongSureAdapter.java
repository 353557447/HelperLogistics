package com.shuiwangzhijia.wuliu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.bean.OrderShowBean;
import com.shuiwangzhijia.wuliu.dialog.EditPurchaseAmountDialog;
import com.shuiwangzhijia.wuliu.interfaces.ShopCartInterface;
import com.shuiwangzhijia.wuliu.utils.ToastUitl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xxc on 2018/12/7.
 */

public class PeisongSureAdapter extends RecyclerView.Adapter<PeisongSureAdapter.ViewHolder> {
    private static final String TAG = "PeisongSureAdapter";
    private final Context mContext;
    private final List<OrderShowBean.GoodsBean> mData;
    private OnViewItemClickListener mOnViewItemClickListener;
    private ShopCartInterface shopCartImp;
    //    回桶的数量可修改， 确定订单中的数量：
//    status==1 根据pay_style==0 时数量只能减 不能加 可以减到0
//    status==2 根据pay_style==1 时 数量可以增加 ，可以减少
    private int payStyle = -1;//表明数字可加减

    public int getPayStyle() {
        return payStyle;
    }

    public void setPayStyle(int payStyle) {
        this.payStyle = payStyle;
    }

    public PeisongSureAdapter(Context context) {
        this.mContext = context;
        this.mData = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_handle_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final OrderShowBean.GoodsBean item = getItem(position);
        holder.mName.setText(item.getGname());
        //每一个的值
        holder.mRightDishAccount.setText(item.getSnum()+"");
        holder.mRightDishAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderShowBean.GoodsBean goodsBean = getItem(position);
                int countAdd = goodsBean.getSnum();
                countAdd++;
                if (goodsBean.getNum() < countAdd) {
                    //数量确认
                    ToastUitl.showToastCustom("新数量超过订单数量");
                    return;
                }
                goodsBean.setSnum(countAdd);
                mData.set(position,goodsBean);
                notifyDataSetChanged();
                if (shopCartImp != null) {
                    //这里的回调是用来就算总价的
                    shopCartImp.add(view, position);
                }
            }
        });
        holder.mRightDishRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderShowBean.GoodsBean goodsBean = getItem(position);
                int countRemove = goodsBean.getSnum();
                goodsBean.getNum();
                if (countRemove <= 0){
                    return;
                }
                countRemove--;
                goodsBean.setSnum(countRemove);
                mData.set(position,goodsBean);
                notifyDataSetChanged();
                if (shopCartImp != null){
                    shopCartImp.remove(view,position);
                }
            }
        });

        holder.mRightDishAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int count = mData.get(position).getSnum();
                EditPurchaseAmountDialog dialog = new EditPurchaseAmountDialog(mContext, count, new EditPurchaseAmountDialog.EditPurchaseAmountConfirmListener() {
                    @Override
                    public void onEditPurchaseAmountConfirm(int count) {
                        if (mOnViewItemClickListener != null){
                            mOnViewItemClickListener.doEditCount(position,count);
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mData == null) ? 0 : mData.size();
    }

    public void setData(List<OrderShowBean.GoodsBean> list) {
        if (list == null) {
            return;
        }
//            for (OrderShowBean.GoodsBean bean : list) {
//                bean.setSnum(bean.getNum());
//            }
        mData.clear();
        mData.addAll(list);
        notifyDataSetChanged();
    }

    public OrderShowBean.GoodsBean getItem(int position){
        OrderShowBean.GoodsBean goodsBean = mData.get(position);
        return goodsBean;
    }

    public ShopCartInterface getShopCartInterface() {
        return shopCartImp;
    }

    public void setShopCartInterface(ShopCartInterface shopCartImp) {
        this.shopCartImp = shopCartImp;
    }

    public List<OrderShowBean.GoodsBean> getData(){
        return mData;
    }

    public interface OnViewItemClickListener{
        void doEditCount(int position,int count);
    }

    public void setOnItemClickListener(OnViewItemClickListener listener){
        mOnViewItemClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView mName;
        @BindView(R.id.image)
        ImageView mImage;
        @BindView(R.id.right_dish_remove)
        ImageView mRightDishRemove;
        @BindView(R.id.right_dish_account)
        TextView mRightDishAccount;
        @BindView(R.id.right_dish_add)
        ImageView mRightDishAdd;
        @BindView(R.id.llAddCut)
        LinearLayout mLlAddCut;
        @BindView(R.id.deleteBtn)
        TextView mDeleteBtn;
        @BindView(R.id.numTv)
        TextView mNumTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
