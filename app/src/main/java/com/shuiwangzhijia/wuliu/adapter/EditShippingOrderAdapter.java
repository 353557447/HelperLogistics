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
import com.shuiwangzhijia.wuliu.bean.GoodsBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EditShippingOrderAdapter extends RecyclerView.Adapter<EditShippingOrderAdapter.ViewHolder> {
    private final Context mContext;
    private final List<GoodsBean> mData;
    private final int type;

    public EditShippingOrderAdapter(Context context, List<GoodsBean> goods, int i) {
        this.mContext = context;
        this.mData = goods;
        this.type = i;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_operation_goods, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (type == 1) {
            holder.mLlAddCut.setVisibility(View.GONE);
            holder.mNumber.setVisibility(View.VISIBLE);
            holder.mName.setText(mData.get(position).getGname());
            holder.mNumber.setText("(x" + mData.get(position).getNum() + ")");
        } else if (type == 2) {
            holder.mLlAddCut.setVisibility(View.VISIBLE);
            holder.mNumber.setVisibility(View.GONE);
            holder.mName.setText(mData.get(position).getGname());
            holder.mNumber.setText("(x" + mData.get(position).getAnum() + ")");
        }
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
        @BindView(R.id.number)
        TextView mNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
