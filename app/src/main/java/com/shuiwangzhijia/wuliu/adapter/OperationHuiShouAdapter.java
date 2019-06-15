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

public class OperationHuiShouAdapter extends RecyclerView.Adapter<OperationHuiShouAdapter.ViewHolder> {
    private final Context mContext;
    private List<OrderShowBean.RecyclerBean> mData;
    private OnViewItemClickListener onViewItemClickListener;

    public OperationHuiShouAdapter(Context context, List<OrderShowBean.RecyclerBean> recycler) {
        this.mContext = context;
        this.mData = recycler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_operation_goods_new, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        OrderShowBean.RecyclerBean item = getItem(position);
        holder.mName.setText(mData.get(position).getGname());
        holder.mRightDishAccount.setText(""+mData.get(position).getSnum());
        holder.mRightDishAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderShowBean.RecyclerBean recyclerBean = getItem(position);
                int countAdd = recyclerBean.getSnum();
                countAdd++;
                recyclerBean.setSnum(countAdd);
                mData.set(position, recyclerBean);
                notifyDataSetChanged();
            }
        });
        holder.mRightDishRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderShowBean.RecyclerBean recyclerBean = getItem(position);
                int countRemove = recyclerBean.getSnum();
                if (countRemove <= 0) {
                    return;
                }
                countRemove--;
                recyclerBean.setSnum(countRemove);
                mData.set(position, recyclerBean);
                notifyDataSetChanged();
            }
        });
        holder.mRightDishAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int num = mData.get(position).getNum();
                EditPurchaseAmountDialog dialog = new EditPurchaseAmountDialog(mContext, num, new EditPurchaseAmountDialog.EditPurchaseAmountConfirmListener() {
                    @Override
                    public void onEditPurchaseAmountConfirm(int count) {
                        if (onViewItemClickListener != null) {
                            onViewItemClickListener.doEditClick(position, count);
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

    public OrderShowBean.RecyclerBean getItem(int position) {
        OrderShowBean.RecyclerBean recyclerBean = mData.get(position);
        return recyclerBean;
    }

    public List<OrderShowBean.RecyclerBean> getData() {
        return mData;
    }

    public interface OnViewItemClickListener {
        void doEditClick(int position, int count);
    }

    public void setOnViewItemClickListener(OnViewItemClickListener listener) {
        onViewItemClickListener = listener;
    }

    public void setData(List<OrderShowBean.RecyclerBean> data) {
        mData = data;
        notifyDataSetChanged();
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
