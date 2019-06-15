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



public class CompletedDetailsAdapter extends RecyclerView.Adapter<CompletedDetailsAdapter.ViewHolder> {
    private final Context mContext;
    private final int mType;
    private List<CompletedDetailsBean.TotalGoodsBean> mData;

    public CompletedDetailsAdapter(Context context,int type) {
        this.mContext = context;
        this.mType = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_completed_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mType == 1){
            holder.mName.setText(mData.get(position).getGname());
            holder.mNumber.setText("(x"+mData.get(position).getNum()+")");
        }else if (mType == 2){
            holder.mName.setText(mData.get(position).getBname());
            holder.mNumber.setText("(x"+mData.get(position).getNum()+")");
        }else if (mType == 3){
            holder.mName.setText(mData.get(position).getGname());
            holder.mNumber.setText("(x"+mData.get(position).getNum()+")");
        }else if (mType == 4){
            holder.mName.setText(mData.get(position).getGname());
            holder.mNumber.setText("(x"+mData.get(position).getNum()+")");
        }
    }

    @Override
    public int getItemCount() {
            return (mData == null) ? 0 : mData.size();
    }

    public void setData(List<CompletedDetailsBean.TotalGoodsBean> data) {
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
