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
 * Created by xxc on 2018/12/12.
 */

public class OperationZiyingAdapter extends RecyclerView.Adapter<OperationZiyingAdapter.ViewHolder> {

    private final Context mContext;
    private List<OrderShowBean.RecyclerBean> mData;
    private final int type;
    private OnViewItemClickListener mOnViewItemClickListener;

    public OperationZiyingAdapter(Context context, List<OrderShowBean.RecyclerBean> goods, int type) {
        this.mContext = context;
        this.mData = goods;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_operation_goods, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (type == 1){
            holder.mLlAddCut.setVisibility(View.VISIBLE);
            holder.mNumber.setVisibility(View.GONE);
            OrderShowBean.RecyclerBean mBucketBean = getItem(position);
            holder.mName.setText(mBucketBean.getGname());
            holder.mRightDishAccount.setText("" + mBucketBean.getNum());
            holder.mRightDishAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OrderShowBean.RecyclerBean BucketBean = getItem(position);
                    int countAdd = BucketBean.getNum();
                    countAdd++;
                    BucketBean.setNum(countAdd);
                    BucketBean.setCheck(true);
                    mData.set(position, BucketBean);
                    notifyDataSetChanged();
                }
            });
            holder.mRightDishRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OrderShowBean.RecyclerBean BucketBean = getItem(position);
                    int countRemove = BucketBean.getNum();
                    if (countRemove <= 1) {
                        mData.remove(position);
                        countRemove--;
                        BucketBean.setNum(countRemove);
                        notifyDataSetChanged();
                        return;
                    }
                    countRemove--;
                    if (countRemove == 0)
                        BucketBean.setCheck(false);
                    BucketBean.setNum(countRemove);
                    mData.set(position, BucketBean);
                    notifyDataSetChanged();
                }
            });
            holder.mRightDishAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int count = mData.get(position).getNum();
                    EditPurchaseAmountDialog dialog = new EditPurchaseAmountDialog(mContext, count, new EditPurchaseAmountDialog.EditPurchaseAmountConfirmListener() {
                        @Override
                        public void onEditPurchaseAmountConfirm(int count) {
                            if (mOnViewItemClickListener != null) {
                                mOnViewItemClickListener.doEditCount(position, count);
                            }
                        }
                    });
                    dialog.show();

                }
            });
        }else {
            holder.mLlAddCut.setVisibility(View.GONE);
            holder.mNumber.setVisibility(View.VISIBLE);
            holder.mName.setText(mData.get(position).getGname());
            holder.mNumber.setText("x" + mData.get(position).getSnum());
        }
    }

    @Override
    public int getItemCount() {
        return (mData == null) ? 0 : mData.size();
    }

    public OrderShowBean.RecyclerBean getItem(int position) {
        return mData.get(position);
    }

    public void setOnItemClickListener(OnViewItemClickListener listener) {
        mOnViewItemClickListener = listener;
    }

    public void setData(List<OrderShowBean.RecyclerBean> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public List<OrderShowBean.RecyclerBean> getData() {
        return mData;
    }

    public interface OnViewItemClickListener {
        void doEditCount(int position, int count);
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
        @BindView(R.id.number)
        TextView mNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
