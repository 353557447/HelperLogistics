package com.shuiwangzhijia.wuliu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.bean.CalculateBean;
import com.shuiwangzhijia.wuliu.bean.GoodsBean;
import com.shuiwangzhijia.wuliu.dialog.EditPurchaseAmountDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;


public class EditAdditionalOrderAdapter extends RecyclerView.Adapter<EditAdditionalOrderAdapter.ViewHolder> {
    private final Context mContext;
    private List<GoodsBean> mData;
    private final int type;
    private OperationHuitongAdapter.OnViewItemClickListener onViewItemClickListener;

    public EditAdditionalOrderAdapter(Context context, List<GoodsBean> goods, int i) {
        this.mContext = context;
        this.mData = goods;
        this.type = i;
    }


    public void setData(List<GoodsBean> data) {
        mData = data;
        for (int i = 0; i < mData.size(); i++) {
            mData.get(i).setAnum(1);
        }
        notifyDataSetChanged();
    }

    public  List<GoodsBean> getData(){
        return mData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_operation_goods_new, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (type == 1) {

        } else if (type == 2) {
            holder.mName.setText(mData.get(position).getGname());
            holder.mRightDishAccount.setText(mData.get(position).getAnum()+"");
        }

        holder.mRightDishAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoodsBean item = getItem(position);
                int countAdd = item.getAnum();
                countAdd++;
                item.setAnum(countAdd);
                mData.set(position, item);
                notifyDataSetChanged();
                EventBus.getDefault().post(new CalculateBean());
            }
        });
        holder.mRightDishRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoodsBean item = getItem(position);
                int countRemove = item.getAnum();
                if (countRemove <= 1) {
                    mData.remove(position);
                    countRemove--;
                    item.setAnum(countRemove);
                    notifyDataSetChanged();
                    EventBus.getDefault().post(new CalculateBean());
                }else {
                    countRemove--;
                    item.setAnum(countRemove);
                    mData.set(position, item);
                    notifyDataSetChanged();
                    EventBus.getDefault().post(new CalculateBean());
                }
            }
        });
        holder.mRightDishAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int num = mData.get(position).getAnum();
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

    public GoodsBean getItem(int position){
        return mData.get(position);
    }

    public interface OnViewItemClickListener {
        void doEditClick(int position, int count);
    }

    public void setOnViewItemClickListener(OperationHuitongAdapter.OnViewItemClickListener listener) {
        onViewItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return (mData == null) ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView mName;
        @BindView(R.id.right_dish_remove)
        RelativeLayout mRightDishRemove;
        @BindView(R.id.right_dish_account)
        TextView mRightDishAccount;
        @BindView(R.id.right_dish_add)
        RelativeLayout mRightDishAdd;
        @BindView(R.id.llAddCut)
        LinearLayout mLlAddCut;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
