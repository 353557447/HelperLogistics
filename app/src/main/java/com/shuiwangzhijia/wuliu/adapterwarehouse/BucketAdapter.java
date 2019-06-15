package com.shuiwangzhijia.wuliu.adapterwarehouse;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.beanwarehouse.StatisticalInfoBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 回桶情况适配器
 * created by wangsuli on 2018/8/17.
 */
public class BucketAdapter extends RecyclerView.Adapter<BucketAdapter.ViewHolder> {
    private final Context mContext;


    private List<StatisticalInfoBean.GoodsBean> mData;

    public BucketAdapter(Context context) {
        mContext = context;
        mData = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_warehouse_bucket_new, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StatisticalInfoBean.GoodsBean item = getItem(position);
        holder.name.setText(item.getGname());
        holder.num.setText("x" + item.getSnum());

    }

    public StatisticalInfoBean.GoodsBean getItem(int position) {
        return mData.get(position);
    }



    @Override
    public int getItemCount() {
        return (mData == null) ? 0 : mData.size();
    }


    public void setData(List<StatisticalInfoBean.GoodsBean> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public void addData(List<StatisticalInfoBean.GoodsBean> goods) {
        mData.addAll(goods);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
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
