package com.shuiwangzhijia.wuliu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.bean.BucketBean;
import com.socks.library.KLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 回桶情况适配器
 * created by wangsuli on 2018/8/17.
 */
public class BucketAdapter extends RecyclerView.Adapter<BucketAdapter.ViewHolder> {
    private final Context mContext;
    private final int mType;


    private List<BucketBean> mData;

    public BucketAdapter(Context context,int type,List<BucketBean> data) {
        mContext = context;
        mType = type;
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_bucket, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BucketBean item = getItem(position);
        KLog.d(mType);
        if (mType == 1){
            holder.name.setText(item.getGname());
            holder.num.setText("x" + item.getNum());
        }else if (mType == 2){
            holder.name.setText(item.getGname());
            holder.num.setText("x" + item.getNum());
        }else if (mType == 3){
            holder.name.setText(item.getGname());
            holder.num.setText("x" + item.getNum());
        }
    }

    public BucketBean getItem(int position) {
        return mData.get(position);
    }

    public void setData(List<BucketBean> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (mData == null) ? 0 : mData.size();
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
