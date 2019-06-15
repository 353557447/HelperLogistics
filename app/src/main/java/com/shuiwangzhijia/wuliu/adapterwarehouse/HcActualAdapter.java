package com.shuiwangzhijia.wuliu.adapterwarehouse;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.beanwarehouse.WarehouseGoodsBean;

import java.util.List;

/**
 * Created by xxc on 2018/11/30.
 */

public class HcActualAdapter extends RecyclerView.Adapter<HcActualAdapter.ViewHolder> {
    private final Context mContext;
    private final List<WarehouseGoodsBean> data;

    public HcActualAdapter(Context context, List<WarehouseGoodsBean> actual) {
        this.mContext = context;
        this.data = actual;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_actual_tihuo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WarehouseGoodsBean item = getItem(position);
        holder.mName.setText(item.getGname());
        holder.mNumber.setText("(x"+item.getNum()+")");
    }

    public List<WarehouseGoodsBean> getData(){
        return data;
    }

    @Override
    public int getItemCount() {
        return (data == null)?0:data.size();
    }

    public WarehouseGoodsBean getItem(int position){
        return data.get(position);
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
