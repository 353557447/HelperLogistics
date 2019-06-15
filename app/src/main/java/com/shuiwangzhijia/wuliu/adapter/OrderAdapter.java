package com.shuiwangzhijia.wuliu.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.bean.OrderBean;
import com.shuiwangzhijia.wuliu.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 订单适配器
 * created by wangsuli on 2018/8/17.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private final Context mContext;
    private final int type;

    private OnViewItemClickListener mOnViewItemClickListener;


    public void setData(List<OrderBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    private List<OrderBean> data;


    public OrderAdapter(Context context, int type) {
        mContext = context;
        this.type = type;
        data = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        OrderBean item = getItem(position);
        holder.orderId.setText("订单号:" + item.getOrder_sn());
        TextPaint paint = holder.orderId.getPaint();
        paint.setFakeBoldText(true);
        holder.orderDate.setText("下单时间:" + DateUtils.getFormatDateStr(item.getTime() * 1000));
        holder.shopName.setText(item.getSname());
        holder.address.setText(item.getAddress());
        holder.remark.setText(item.getRemarks());
        int isout = item.getIsout();

        //送达时间
        String update_time = item.getGoods().get(0).getUpdate_time();
        if (update_time != null){
            holder.mDeliveryTime.setText(DateUtils.getFormatDateStr(Long.parseLong(update_time)*1000));
        }

        holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        holder.recyclerView.setHasFixedSize(true);
        OrderItemAdapter mOrderAdapter = new OrderItemAdapter(mContext, true);
        mOrderAdapter.setData(item.getGoods());
        holder.recyclerView.setAdapter(mOrderAdapter);
        //联系商家
        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnViewItemClickListener.onCallClick(getItem(position).getPhone());
            }
        });
        setTextStyle(holder.count, "共", "" + item.getSnum(), "桶");
        setTextStyle(holder.totalMoney,"合计：","￥"+item.getTprice(),"");
        switch (type) {
            case 1:
                holder.secondBtn.setText("转单");
                holder.firstBtn.setText("操作订单");
                holder.mDeliveryTimeLinear.setVisibility(View.GONE);
                holder.mPaisongStatus.setText("送至：");
                //未出货
                if (isout == 0){
                    holder.status.setVisibility(View.VISIBLE);
                    holder.status.setText("未提货");
                    holder.status.setTextColor(mContext.getResources().getColor(R.color.textstatus_other));
                    holder.firstBtn.setVisibility(View.GONE);
                    holder.secondBtn.setVisibility(View.VISIBLE);
                }else if (isout == 1){
                    //已出货
                    holder.status.setVisibility(View.VISIBLE);
                    holder.status.setText("已提货");
                    holder.status.setTextColor(mContext.getResources().getColor(R.color.textstatus));
                    holder.firstBtn.setVisibility(View.VISIBLE);
                    holder.secondBtn.setVisibility(View.GONE);
                }else {
                    holder.status.setVisibility(View.VISIBLE);
                    holder.status.setText("未提货");
                    holder.status.setTextColor(mContext.getResources().getColor(R.color.textstatus_other));
                    holder.firstBtn.setVisibility(View.GONE);
                    holder.secondBtn.setVisibility(View.GONE);
                }
                break;
            case 2:
                holder.firstBtn.setText("收款");
                holder.firstBtn.setVisibility(View.VISIBLE);
                holder.secondBtn.setVisibility(View.GONE);
                holder.mDeliveryTimeLinear.setVisibility(View.VISIBLE);
                holder.mPaisongStatus.setText("已送达：");
                break;
            case 3:
                if (DateUtils.IsToday(item.getTime() * 1000)) {
                    holder.firstBtn.setText("追加订单");
                    holder.firstBtn.setVisibility(View.GONE);
                } else {
                    holder.firstBtn.setVisibility(View.GONE);
                }
                holder.secondBtn.setVisibility(View.GONE);
                holder.mDeliveryTimeLinear.setVisibility(View.VISIBLE);
                holder.mPaisongStatus.setText("已送达：");
                break;
        }
        holder.firstBtn.setTag(position);
        holder.secondBtn.setTag(position);
        holder.secondBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) view.getTag();
                mOnViewItemClickListener.onTurnDriverClick(getItem(tag).getOrder_sn());
            }
        });
        holder.firstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) view.getTag();
                OrderBean bean = getItem(tag);
                switch (type) {
                    case 1:
                        mOnViewItemClickListener.onHandleOrderClick(tag);
                        break;
                    case 2:
                        mOnViewItemClickListener.onReceiveMoneyClick(bean);
                        break;
                    case 3:
                        mOnViewItemClickListener.onRePayOrderClick(tag);
                        break;
                }
            }
        });
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnViewItemClickListener.onDetailClick((Integer) view.getTag());
            }
        });
        holder.recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return holder.itemView.onTouchEvent(event);
            }
        });
    }

    private void setTextStyle(TextView text, String first, String content, String second) {
        SpannableString spanString = new SpannableString(first + content + second);
        spanString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_ff3939)), first.length(), (content + first).length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);//颜色
        spanString.setSpan(new AbsoluteSizeSpan(17, true), first.length(), (content + first).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);//字体大小
        text.setText(spanString);
    }

    public OrderBean getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return (data == null) ? 0 : data.size();

    }

    public void setOnItemClickListener(OnViewItemClickListener listener) {
        mOnViewItemClickListener = listener;
    }

    public void addData(ArrayList<OrderBean> result) {
        data.addAll(result);
        notifyDataSetChanged();
    }


    public interface OnViewItemClickListener {
        void onReceiveMoneyClick(OrderBean data);

        void onTurnDriverClick(String orderNo);

        void onCallClick(String phone);

        void onDetailClick(int position);

        void onHandleOrderClick(int position);

        void onRePayOrderClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.orderId)
        TextView orderId;
        @BindView(R.id.orderDate)
        TextView orderDate;
        @BindView(R.id.shopName)
        TextView shopName;
        @BindView(R.id.callBtn)
        TextView callBtn;
        @BindView(R.id.address)
        TextView address;
        @BindView(R.id.remark)
        TextView remark;
        @BindView(R.id.delivery_time)
        TextView mDeliveryTime;
        @BindView(R.id.delivery_time_linear)
        LinearLayout mDeliveryTimeLinear;
        @BindView(R.id.paisong_status)
        TextView mPaisongStatus;
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;
        @BindView(R.id.totalMoney)
        TextView totalMoney;
        @BindView(R.id.count)
        TextView count;
        @BindView(R.id.secondBtn)
        TextView secondBtn;
        @BindView(R.id.firstBtn)
        TextView firstBtn;
        @BindView(R.id.status)
        TextView status;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
