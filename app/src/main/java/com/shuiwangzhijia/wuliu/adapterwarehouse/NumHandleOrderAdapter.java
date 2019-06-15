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

/**
 * Created by xxc on 2018/11/30.
 */

public class NumHandleOrderAdapter extends RecyclerView.Adapter<NumHandleOrderAdapter.ViewHolder> {


    private final Context mContext;
    private final List<OrderDetailBean.NeedBean> data;

    public NumHandleOrderAdapter(Context context, List<OrderDetailBean.NeedBean> goods) {
        this.mContext = context;
        this.data = goods;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_need_tihuo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mName.setText(data.get(position).getGname());
        holder.mNumber.setText("x"+data.get(position).getNum());
    }

    @Override
    public int getItemCount() {
        return (data == null)?0:data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView mName;
        private final TextView mNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
            mNumber = (TextView) itemView.findViewById(R.id.numTv);
        }
    }
}
