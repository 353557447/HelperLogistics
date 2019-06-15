package com.shuiwangzhijia.wuliu.adapterwarehouse;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.beanwarehouse.WarehouseGoodsBean;
import com.shuiwangzhijia.wuliu.dialogwarehouse.EditPurchaseAmountDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/11/30.
 */

public class HcTuiShuiAdapter extends RecyclerView.Adapter<HcTuiShuiAdapter.ViewHolder> {
    private final Context mContext;
    private List<WarehouseGoodsBean> data;
    private OnViewItemClickListener onViewItemClickListener;


    public HcTuiShuiAdapter(Context context, List<WarehouseGoodsBean> refund) {
        this.mContext = context;
        this.data = refund;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tuishui_number, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        WarehouseGoodsBean item = getItem(position);
        holder.mName.setText(item.getGname());
        holder.mRightDishAccount.setText(item.getNum()+"");

        holder.mRightDishAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WarehouseGoodsBean goodsBean = getItem(position);
                int countAdd = goodsBean.getNum();
                countAdd++;
                goodsBean.setNum(countAdd);
                data.set(position, goodsBean);
                notifyDataSetChanged();
            }
        });
        holder.mRightDishRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WarehouseGoodsBean goodsBean = getItem(position);
                int countRemove = goodsBean.getNum();
                if (countRemove <= 0) {
                    return;
                }
                countRemove--;
                goodsBean.setNum(countRemove);
                data.set(position, goodsBean);
                notifyDataSetChanged();
            }
        });
        holder.mRightDishAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int num = data.get(position).getNum();
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
        return (data == null) ? 0 : data.size();
    }

    public WarehouseGoodsBean getItem(int position) {
        return data.get(position);
    }

    public void setData(List<WarehouseGoodsBean> mData) {
        data = mData;
        notifyDataSetChanged();
    }

    public interface OnViewItemClickListener {
        void doEditClick(int position, int count);
    }

    public void setOnViewItemClickListener(OnViewItemClickListener listener) {
        onViewItemClickListener = listener;
    }

    public List<WarehouseGoodsBean> getData() {
        return data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView mName;
        @BindView(R.id.numTv)
        TextView mNumTv;
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
            ButterKnife.bind(this,itemView);
        }
    }
}
