package com.shuiwangzhijia.wuliu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.bean.CanShuipiaoBean;
import com.shuiwangzhijia.wuliu.bean.OrderShowBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 添加自营桶适配器
 * created by wangsuli on 2018/8/17.
 */
public class TianjiaShuipiaoAdapter extends RecyclerView.Adapter<TianjiaShuipiaoAdapter.ViewHolder> {
    private final Context mContext;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;


    public List<OrderShowBean.RecyclerBean> getGoodsData() {
        return goodsData;
    }

    public void setGoodsData(List<OrderShowBean.RecyclerBean> goodsData) {
        this.goodsData = goodsData;
        notifyDataSetChanged();
    }

    private List<OrderShowBean.RecyclerBean> goodsData;

    public List<CanShuipiaoBean> getBucketData() {
        return bucketData;
    }

    public void setBucketData(List<CanShuipiaoBean> bucketData) {
        this.bucketData = bucketData;
    }

    private List<CanShuipiaoBean> bucketData;
    private boolean fromHandler;


    public TianjiaShuipiaoAdapter(Context context, boolean fromHandler) {
        mContext = context;
        this.fromHandler=fromHandler;
        if(fromHandler){
            goodsData=new ArrayList<>();
        }else{
            bucketData = new ArrayList<>();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_driver, parent, false);
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
        Object item = getItem(position);
        if(item instanceof CanShuipiaoBean){
            holder.name.setText(((CanShuipiaoBean) item).getS_name() );
            holder.check.setSelected(((CanShuipiaoBean) item).isCheck());
        }
        if(item instanceof OrderShowBean.RecyclerBean){
            holder.name.setText(((OrderShowBean.RecyclerBean) item).getGname() );
            holder.check.setSelected(((OrderShowBean.RecyclerBean) item).isCheck());
        }
    }

    public Object getItem(int position) {
        if(fromHandler){
            return goodsData.get(position);
        }else{
            return bucketData.get(position);
        }
    }

    @Override
    public int getItemCount() {
        if(fromHandler){
            return (goodsData == null) ? 0 : goodsData.size();
        }else{
            return (bucketData == null) ? 0 : bucketData.size();
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        onRecyclerViewItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.check)
        ImageView check;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
