package com.shuiwangzhijia.wuliu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.base.Constant;
import com.shuiwangzhijia.wuliu.bean.GoodsBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 采购适配器
 * created by wangsuli on 2018/8/17.
 */
public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.ViewHolder> {
    public static final int TYPE_ALL = 0;
    public static final int TYPE_ALL_1 = 1;
    public static final int TYPE_ALL_2 = 2;
    public static final int TYPE_ALL_3 = 3;
    private final Context mContext;
    private OnSaleClickListener mOnViewItemClickListener;
    private List<GoodsBean> data;

    public void setSale(boolean sale) {
        isSale = sale;
        notifyDataSetChanged();
    }

    private boolean isSale = true;

    public PurchaseAdapter(Context context, boolean isSale) {
        mContext = context;
        this.isSale = isSale;
        data = new ArrayList<>();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_purchase, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.addBtn.setTag(position);
        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnViewItemClickListener.onSelectClick((int) view.getTag());
            }
        });
        GoodsBean bean = getItem(position);
        bean.setCount(1);
        Glide.with(mContext).load(Constant.GOODS_IMAGE_URL + bean.getPicturl()).placeholder(R.drawable.wutupian).into(holder.image);
        holder.title.setText(bean.getGname());
        holder.price.setText("￥" + bean.getPrice());
    }


    @Override
    public int getItemCount() {
        return (data == null) ? 0 : data.size();
    }

    public void setOnSaleClickListener(OnSaleClickListener listener) {
        mOnViewItemClickListener = listener;
    }

    public void setData(ArrayList<GoodsBean> result) {
        data = result;
        notifyDataSetChanged();
    }
    public void addData(ArrayList<GoodsBean> result) {
        data.addAll(result);
        notifyDataSetChanged();
    }
    public GoodsBean getItem(int position) {
        return data.get(position);
    }

    public interface OnSaleClickListener {
        void onSelectClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.addBtn)
        TextView addBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
