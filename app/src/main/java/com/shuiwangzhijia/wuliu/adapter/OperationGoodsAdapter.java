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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xxc on 2018/12/7.
 */

public class OperationGoodsAdapter extends RecyclerView.Adapter<OperationGoodsAdapter.ViewHolder> {
    private final Context mContext;
    private final List<OrderShowBean.GoodsBean> mData;
    private ShopCartInterface shopCartImp;
    private PeisongSureAdapter.OnViewItemClickListener mOnViewItemClickListener;

    public OperationGoodsAdapter(Context context, List<OrderShowBean.GoodsBean> goods) {
        this.mContext = context;
        this.mData = goods;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_operation_goods, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mName.setText(mData.get(position).getGname());
//        holder.mNumber.setText("x"+mData.get(position).getSnum());
        holder.mRightDishAccount.setText(""+mData.get(position).getSnum());
        holder.mRightDishAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderShowBean.GoodsBean goodsBean = getItem(position);
                int countAdd = goodsBean.getSnum();
                countAdd++;
                if (goodsBean.getNum() < countAdd){
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

    public List<OrderShowBean.GoodsBean> getData() {
        return mData;
    }

    public ShopCartInterface getShopCartInterface() {
        return shopCartImp;
    }

    public void setShopCartInterface(ShopCartInterface shopCartImp) {
        this.shopCartImp = shopCartImp;
    }

    public interface OnViewItemClickListener{
        void doEditCount(int position,int count);
    }

    public void setOnItemClickListener(PeisongSureAdapter.OnViewItemClickListener listener){
        mOnViewItemClickListener = listener;
    }

    public OrderShowBean.GoodsBean getItem(int position){
        OrderShowBean.GoodsBean goodsBean = mData.get(position);
        return goodsBean;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView mName;
        @BindView(R.id.right_dish_remove)
        ImageView mRightDishRemove;
        @BindView(R.id.right_dish_account)
        TextView mRightDishAccount;
        @BindView(R.id.right_dish_add)
        ImageView mRightDishAdd;
        @BindView(R.id.llAddCut)
        LinearLayout mLlAddCut;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
