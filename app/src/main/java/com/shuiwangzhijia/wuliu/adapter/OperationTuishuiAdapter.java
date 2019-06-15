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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xxc on 2018/12/7.
 */

public class OperationTuishuiAdapter extends RecyclerView.Adapter<OperationTuishuiAdapter.ViewHolder> {
    private final Context mContext;
    private List<OrderShowBean.GoodsBean> mData;
    private OnViewItemClickListener mOnViewItemClickListener;

    public OperationTuishuiAdapter(Context context, List<OrderShowBean.GoodsBean> refund_water) {
        this.mContext = context;
        this.mData = refund_water;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_operation_goods_new, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        OrderShowBean.GoodsBean item = getItem(position);
        holder.mName.setText(item.getGname());
        holder.mRightDishAccount.setText(item.getSnum()+"");
        holder.mRightDishAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderShowBean.GoodsBean refundWaterBean = getItem(position);
                int countAdd = refundWaterBean.getSnum();
                countAdd++;
                refundWaterBean.setSnum(countAdd);
                mData.set(position,refundWaterBean);
                notifyDataSetChanged();
            }
        });
        holder.mRightDishRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderShowBean.GoodsBean refundWaterBean = getItem(position);
                int countRemove = refundWaterBean.getSnum();
                if (countRemove<=0){
                    return;
                }
                countRemove--;
                refundWaterBean.setSnum(countRemove);
                mData.set(position,refundWaterBean);
                notifyDataSetChanged();
            }
        });
        holder.mRightDishAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int snum = mData.get(position).getSnum();
                EditPurchaseAmountDialog dialog = new EditPurchaseAmountDialog(mContext, snum, new EditPurchaseAmountDialog.EditPurchaseAmountConfirmListener() {
                    @Override
                    public void onEditPurchaseAmountConfirm(int count) {
                        if (mOnViewItemClickListener!=null){
                            mOnViewItemClickListener.doEditClick(position,count);
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

    public interface OnViewItemClickListener {
        void doEditClick(int position, int count);
    }

    public void setOnViewItemClickListener(OnViewItemClickListener listener) {
        mOnViewItemClickListener = listener;
    }

    public void setData(List<OrderShowBean.GoodsBean> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public OrderShowBean.GoodsBean getItem(int position){
        OrderShowBean.GoodsBean refundWaterBean = mData.get(position);
        return refundWaterBean;
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
