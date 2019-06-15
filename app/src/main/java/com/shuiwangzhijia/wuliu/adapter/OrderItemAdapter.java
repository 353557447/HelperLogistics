package com.shuiwangzhijia.wuliu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
 * 配送中、赊账中、已完成嵌套recycleview适配器  isOrderList = true
 * 确认订单适配器 isOrderList = false
 * 订单详情适配器 isDetail=true
 *
 *
 * created by wangsuli on 2018/8/17.
 */
public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {
    private final Context mContext;

    private List<GoodsBean> data;
    private OnSaleClickListener mOnViewItemClickListener;
    private boolean isOrderList = false;

    public boolean isDetail() {
        return isDetail;
    }

    public void setDetail(boolean detail) {
        isDetail = detail;
    }

    private boolean isDetail = false;

    public OrderItemAdapter(Context context, boolean isOrderList) {
        mContext = context;
        this.isOrderList = isOrderList;
        data = new ArrayList<>();
    }

    public List<GoodsBean> getData() {
        return data;
    }

    public void setData(List<GoodsBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_order_info_new, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GoodsBean bean = data.get(position);
        holder.name.setText(bean.getGname());
        //配送中、赊账中、已完成界面
        if (isOrderList) {
            holder.price.setText("￥" + bean.getPrice());
            holder.num.setText("x" + bean.getNum());
        } else {
            //确认订单界面
            holder.price.setText("￥" + bean.getPrice());
            holder.num.setText("x" + bean.getCount());
        }
        //订单详情界面设置
        if (isDetail) {
            holder.llDetailInfo.setVisibility(View.VISIBLE);
            setTextStyle(holder.needNum, "需配送:", "x" + bean.getNum());
            setTextStyle(holder.realNum, "实际配送:", "x" + bean.getSnum());
        } else {
            //如果不是订单详情界面需配送和实际配送不显示
            holder.llDetailInfo.setVisibility(View.GONE);
        }
        Glide.with(mContext).load(Constant.GOODS_IMAGE_URL + bean.getPicturl()).placeholder(R.drawable.wutupian).into(holder.image);
    }

    private void setTextStyle(TextView text, String first, String content) {
        SpannableString spanString = new SpannableString(first + content);
        spanString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_333333)), first.length(), (content + first).length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);//颜色
        spanString.setSpan(new AbsoluteSizeSpan(14, true), first.length(), (content + first).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);//字体大小
        text.setText(spanString);
    }

    public GoodsBean getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return (data == null) ? 0 : data.size();
    }

    public void setOnSaleClickListener(OnSaleClickListener listener) {
        mOnViewItemClickListener = listener;
    }

    public interface OnSaleClickListener {
        void onSelectClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.num)
        TextView num;
        @BindView(R.id.needNum)
        TextView needNum;
        @BindView(R.id.realNum)
        TextView realNum;
        @BindView(R.id.llDetailInfo)
        LinearLayout llDetailInfo;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
