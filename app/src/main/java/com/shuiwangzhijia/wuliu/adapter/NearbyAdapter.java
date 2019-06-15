package com.shuiwangzhijia.wuliu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.bean.ShopBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.ViewHolder> {
    private final Context mContext;

    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private List<ShopBean> data;
    private boolean isNear;


    public NearbyAdapter(Context context, boolean isNear) {
        mContext = context;
        this.isNear = isNear;
        data = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_nearby, parent, false);
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
        ShopBean item = getItem(position);
        holder.name.setText(item.getSname());
        holder.address.setText(item.getAddress());
//        String dis = CalculateUtils.sub(item.getDistance(), 1) > 0 ? item.getDistance() + "Km" : item.getDistance() * 1000 + "m";
        holder.distance.setText("距离:" +  item.getDistance() + "km");
        holder.rightIv.setVisibility(isNear ? View.VISIBLE : View.GONE);
    }

    public ShopBean getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return (data == null) ? 0 : data.size();
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        onRecyclerViewItemClickListener = listener;
    }

    public void setData(List<ShopBean> result) {
        data = result;
        notifyDataSetChanged();
    }
    public void addData(List<ShopBean> result) {
        data .addAll(result);
        notifyDataSetChanged();
    }
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.address)
        TextView address;
        @BindView(R.id.distance)
        TextView distance;
        @BindView(R.id.rightIv)
        ImageView rightIv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
