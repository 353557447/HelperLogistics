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
 * Created by xxc on 2019/1/10.
 */

public class NumberAdapter extends RecyclerView.Adapter<NumberAdapter.ViewHolder> {

    private final Context context;
    private final List<OrderDetailBean.NeedBean> data;

    public NumberAdapter(Context context, List<OrderDetailBean.NeedBean> need) {
        this.context = context;
        this.data = need;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_huitong_ziyingtong, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mName.setText(data.get(position).getGname());
            holder.mNumber.setText("x"+data.get(position).getNum());
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
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
