package com.shuiwangzhijia.wuliu.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.bean.SendOrderBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



public class CreateOrderAdapter extends RecyclerView.Adapter<CreateOrderAdapter.ViewHolder> {
    private final Context mContent;
    private final List<SendOrderBean> mData;

    public CreateOrderAdapter(Context context, List<SendOrderBean> pendingOrder) {
        this.mContent = context;
        this.mData = pendingOrder;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContent).inflate(R.layout.item_create_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SendOrderBean item = getItem(position);
        holder.mDeliverOrderNumber.setText(item.getOrder_sn());
        holder.mItemPendingTotal.setText("共"+item.getSnum()+"桶");
        holder.mItemPendingShopName.setText("水店名称："+item.getSname());
        holder.mItemPendingAdress.setText("送至："+item.getAddr());
        holder.mItemPendingRv.setLayoutManager(new GridLayoutManager(mContent,2));
        holder.mItemPendingRv.setHasFixedSize(true);
        List<SendOrderBean.GoodsBean> goods = item.getGoods();
        PendingItemAdapter adapter = new PendingItemAdapter(mContent,goods);
        holder.mItemPendingRv.setAdapter(adapter);
    }

    private void setTextStyle(TextView text, String first, String content, String second) {
        SpannableString spanString = new SpannableString(first + content + second);
        spanString.setSpan(new ForegroundColorSpan(mContent.getResources().getColor(R.color.color_ff3939)), first.length(), (content + first).length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);//颜色
        spanString.setSpan(new AbsoluteSizeSpan(24, true), first.length(), (content + first).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);//字体大小
        text.setText(spanString);
    }

    @Override
    public int getItemCount() {
        return (mData == null) ? 0 : mData.size();
    }

    private SendOrderBean getItem(int position){
        SendOrderBean sendOrderBean = mData.get(position);
        return sendOrderBean;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.deliver_order_number)
        TextView mDeliverOrderNumber;
        @BindView(R.id.item_pending_total)
        TextView mItemPendingTotal;
        @BindView(R.id.item_pending_shop_name)
        TextView mItemPendingShopName;
        @BindView(R.id.item_pending_adress)
        TextView mItemPendingAdress;
        @BindView(R.id.item_pending_rv)
        RecyclerView mItemPendingRv;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
