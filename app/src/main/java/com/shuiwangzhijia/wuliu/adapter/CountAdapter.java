package com.shuiwangzhijia.wuliu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.bean.CountBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 统计适配器
 * created by wangsuli on 2018/8/17.
 */
public class CountAdapter extends RecyclerView.Adapter<CountAdapter.ViewHolder> {
    private final Context mContext;
    private final boolean isOrder;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private List<CountBean> data;


    public CountAdapter(Context context,boolean isOrder) {
        mContext = context;
        data = new ArrayList<>();
        this.isOrder=isOrder;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_count, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRecyclerViewItemClickListener.onItemClick((int) view.getTag());
            }
        });
        CountBean bean = data.get(position);
        if(isOrder){
            holder.center.setVisibility(View.INVISIBLE);
        }else{
            holder.center.setText("" + bean.getNum());
        }
        holder.name.setText(bean.getGname());
        holder.num.setText("￥" + bean.getNum());
    }

    @Override
    public int getItemCount() {
        return (data == null) ? 0 : data.size();
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        onRecyclerViewItemClickListener = listener;
    }

    public void setData(List<CountBean> result) {
        data = result;
        notifyDataSetChanged();
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.center)
        TextView center;
        @BindView(R.id.num)
        TextView num;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
