package com.shuiwangzhijia.wuliu.adapterwarehouse;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.beanwarehouse.WarehouseGoodsBean;
import com.shuiwangzhijia.wuliu.beanwarehouse.OrderDetailBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 订单信息适配器
 * created by wangsuli on 2018/8/17.
 */
public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {
    private final Context mContext;
    private final List<OrderDetailBean.NeedBean> mData;


    //    private List<GoodsBean> data;
    private OnSaleClickListener mOnViewItemClickListener;

    public OrderItemAdapter(Context context, List<OrderDetailBean.NeedBean> result) {
        this.mContext = context;
        this.mData = result;

//        data = new ArrayList<>();
    }

//    public List<GoodsBean> getData() {
//        return data;
//    }

    public void setData(List<WarehouseGoodsBean> data) {
//        this.data = data;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_warehouse_need_tihuo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        if (position == 0){
//            setTextStyle(holder.title, "需提货", "(" + mData.getNeed_count() + ")");
//        }else {
//            setTextStyle(holder.title, "实际提货", "(" + mData.getActual_count() + ")");
//        }
//        holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        holder.recyclerView.setHasFixedSize(true);
//        OrderItemNeedAdapter adapter = new OrderItemNeedAdapter(mContext, mData);
//        BucketAdapter mBucketAdapter = new BucketAdapter(mContext);
//        holder.recyclerView.setAdapter(adapter);
        OrderDetailBean.NeedBean item = getItem(position);
        holder.mName.setText(item.getGname());
        holder.mNumTv.setText("(x"+item.getNum()+")");
    }

    private void setTextStyle(TextView text, String first, String content) {
        SpannableString spanString = new SpannableString(first + content);
        spanString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_999999)), first.length(), (content + first).length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);//颜色
        text.setText(spanString);
    }

    public OrderDetailBean.NeedBean getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getItemCount() {
        return (mData == null) ? 0 : mData.size();
    }

    public void setOnSaleClickListener(OnSaleClickListener listener) {
        mOnViewItemClickListener = listener;
    }

    public interface OnSaleClickListener {
        void onSelectClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView mName;
        @BindView(R.id.numTv)
        TextView mNumTv;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
