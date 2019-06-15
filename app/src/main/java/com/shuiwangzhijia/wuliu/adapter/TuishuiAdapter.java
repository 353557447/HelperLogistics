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

public class TuishuiAdapter extends RecyclerView.Adapter<TuishuiAdapter.ViewHolder> {
    private final Context mContext;
    private List<OrderShowBean.RecyclerBean> mData;
    private final List<OrderShowBean.GoodsBean> peisongData;
    private OnViewItemClickListener mOnViewItemClickListener;

    public TuishuiAdapter(Context context, List<OrderShowBean.RecyclerBean> refundWaterBeanList, List<OrderShowBean.GoodsBean> peisongData) {
        this.mContext = context;
        this.mData = refundWaterBeanList;
        this.peisongData = peisongData;
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
        holder.mRightDishAccount.setText(item.getSnum()+"");
        holder.mRightDishAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderShowBean.RecyclerBean refundWaterBean = getItem(position);
                int countAdd = refundWaterBean.getSnum();
                countAdd++;
//                if (peisongData.get(position).getSnum()<countAdd){
//                    ToastUitl.showToastCustom("退水数量不能超过配送数量");
//                    return;
//                }
                refundWaterBean.setSnum(countAdd);
                mData.set(position,refundWaterBean);
                notifyDataSetChanged();
            }
        });
        holder.mRightDishRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderShowBean.RecyclerBean refundWaterBean = getItem(position);
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

    public OrderShowBean.RecyclerBean getItem(int position){
        OrderShowBean.RecyclerBean refundWaterBean = mData.get(position);
        return refundWaterBean;
    }

    public interface OnViewItemClickListener{
        void doEditClick(int positon,int count);
    }

    public void setData(List<OrderShowBean.RecyclerBean> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void setOnViewItemClickListener(OnViewItemClickListener listener){
        mOnViewItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return (mData == null)?0:mData.size();
    }

    public  List<OrderShowBean.RecyclerBean> getData(){
        return mData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
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
