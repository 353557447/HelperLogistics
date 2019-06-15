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

public class OperationHuitongAdapter extends RecyclerView.Adapter<OperationHuitongAdapter.ViewHolder> {
    private final Context mContext;
    private List<OrderShowBean.RecyclerBean> mData;
    private OnViewItemClickListener onViewItemClickListener;

    public OperationHuitongAdapter(Context context, List<OrderShowBean.RecyclerBean> recyclerBeanList) {
        this.mContext = context;
        this.mData = recyclerBeanList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_handle_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        OrderShowBean.RecyclerBean item = getItem(position);
        holder.mName.setText(item.getGname());
        holder.mRightDishAccount.setText(item.getNum() + "");
        holder.mRightDishAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderShowBean.RecyclerBean recyclerBean = getItem(position);
                int countAdd = recyclerBean.getNum();
                countAdd++;
                recyclerBean.setNum(countAdd);
                mData.set(position, recyclerBean);
                notifyDataSetChanged();
            }
        });
        holder.mRightDishRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderShowBean.RecyclerBean recyclerBean = getItem(position);
                int countRemove = recyclerBean.getNum();
                if (countRemove <= 0) {
                    return;
                }
                countRemove--;
                recyclerBean.setNum(countRemove);
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

    public OrderShowBean.RecyclerBean getItem(int position) {
        OrderShowBean.RecyclerBean recyclerBean = mData.get(position);
        return recyclerBean;
    }

    @Override
    public int getItemCount() {
        return (mData == null) ? 0 : mData.size();
    }

    public void setData(List<OrderShowBean.RecyclerBean> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public interface OnViewItemClickListener {
        void doEditClick(int position, int count);
    }

    public void setOnViewItemClickListener(OnViewItemClickListener listener) {
        onViewItemClickListener = listener;
    }

    public List<OrderShowBean.RecyclerBean> getData() {
        return mData;
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
