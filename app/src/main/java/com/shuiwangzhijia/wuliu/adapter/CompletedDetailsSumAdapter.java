package com.shuiwangzhijia.wuliu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.bean.CompletedDetailsBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xxc on 2018/12/6.
 */

public class CompletedDetailsSumAdapter extends RecyclerView.Adapter<CompletedDetailsSumAdapter.ViewHolder> {
    private final Context mContext;
    private List<CompletedDetailsBean.SumGoodsBean> mData;

    public CompletedDetailsSumAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_completed_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mName.setText(mData.get(position).getGname());
            holder.mNumber.setText("x"+mData.get(position).getSnum()+"(桶)");
    }

    @Override
    public int getItemCount() {
            return (mData == null) ? 0 : mData.size();
    }

    public void setData(List<CompletedDetailsBean.SumGoodsBean> data) {
        mData = data;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView mName;
        @BindView(R.id.number)
        TextView mNumber;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
