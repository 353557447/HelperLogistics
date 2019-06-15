package com.shuiwangzhijia.wuliu.adapterwarehouse;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.beanwarehouse.OrderDetailBean;
import com.shuiwangzhijia.wuliu.uiwarehouse.OrderDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/12/4.
 */

public class OrderItemActualAdapter extends RecyclerView.Adapter<OrderItemActualAdapter.ViewHolder> {
    private final Context mContext;
    private final List<OrderDetailBean.ActualBean> mData;

    public OrderItemActualAdapter(OrderDetailActivity context, List<OrderDetailBean.ActualBean> actual) {
        this.mContext = context;
        this.mData = actual;
    }

    @Override
    public OrderItemActualAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_warehouse_bucket, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(mData.get(position).getGname());
        holder.num.setText("(x"+mData.get(position).getNum()+")");
    }

    @Override
    public int getItemCount() {
        return (mData == null)?0:mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.num)
        TextView num;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
