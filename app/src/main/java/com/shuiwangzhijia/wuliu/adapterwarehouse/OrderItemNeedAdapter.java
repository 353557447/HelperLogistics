package com.shuiwangzhijia.wuliu.adapterwarehouse;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.beanwarehouse.OrderDetailBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xxc on 2018/12/4.
 */

public class OrderItemNeedAdapter extends RecyclerView.Adapter<OrderItemNeedAdapter.ViewHolder> {
    private final Context mContext;
    private final List<OrderDetailBean.BackBean> mData;

    public OrderItemNeedAdapter(Context context, List<OrderDetailBean.BackBean> need) {
        this.mContext = context;
        this.mData = need;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_warehouse_bucket, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(mData.get(position).getBname());
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
