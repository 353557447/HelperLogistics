package com.shuiwangzhijia.wuliu.adapterwarehouse;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.beanwarehouse.WarehouseGoodsBean;
import com.shuiwangzhijia.wuliu.dialogwarehouse.EditPurchaseAmountDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xxc on 2018/11/30.
 */

public class EditHandleAdapter extends RecyclerView.Adapter<EditHandleAdapter.ViewHolder> {
    private final Context mContext;
    private ArrayList<WarehouseGoodsBean> mData;
    private OnViewItemClickListener onViewItemClickListener;

    public EditHandleAdapter(Context context, ArrayList<WarehouseGoodsBean> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_edit_handle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
            final WarehouseGoodsBean bucketBean = (WarehouseGoodsBean) mData.get(position);
            holder.mName.setText(bucketBean.getBname());
            holder.mRightDishAccount.setText(bucketBean.getNum() + "");
            holder.mRightDishRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int countRemove = bucketBean.getNum();
                    if (countRemove <= 0) {
                        return;
                    }
                    countRemove--;
                    bucketBean.setNum(countRemove);
                    mData.set(position, bucketBean);
                    notifyDataSetChanged();
                }
            });
            holder.mRightDishAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int countAdd = bucketBean.getNum();
                    countAdd++;
                    bucketBean.setNum(countAdd);
                    mData.set(position, bucketBean);
                    notifyDataSetChanged();
                }
            });
        holder.mRightDishAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = mData.get(position).getNum();
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

    public interface OnViewItemClickListener {
        void doEditClick(int position, int count);
    }

    public void setOnViewItemClickListener(OnViewItemClickListener listener) {
        onViewItemClickListener = listener;
    }

    public WarehouseGoodsBean getItem(int position){
        WarehouseGoodsBean goodsBean = mData.get(position);
        return goodsBean;
    };

    @Override
    public int getItemCount() {
        return (mData == null) ? 0 : mData.size();
    }

    public void setData(ArrayList<WarehouseGoodsBean> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public ArrayList<WarehouseGoodsBean> getData() {
        return mData;
    }

//    public Object getItem(int position) {
//        if (mType == 0){
//            BucketBean bucketBean = (BucketBean) mData.get(position);
//            return bucketBean;
//        }else {
//            GoodsBean goodsBean = (GoodsBean) mData.get(position);
//            return goodsBean;
//        }
//    }

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
