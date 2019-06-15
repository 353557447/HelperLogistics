package com.shuiwangzhijia.wuliu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.bean.SendOrderBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PendingItemAdapter extends RecyclerView.Adapter<PendingItemAdapter.ViewHolder> {


    private final Context mContext;
    private final List<SendOrderBean.GoodsBean> mData;

    public PendingItemAdapter(Context context, List<SendOrderBean.GoodsBean> goods) {
        this.mContext = context;
        this.mData = goods;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pending_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mItemPendingName.setText(mData.get(position).getGname());
        holder.mItemPendingNumber.setText("(x"+mData.get(position).getSnum()+")");
    }

    @Override
    public int getItemCount() {
        return (mData == null) ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_pending_name)
        TextView mItemPendingName;
        @BindView(R.id.item_pending_number)
        TextView mItemPendingNumber;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
