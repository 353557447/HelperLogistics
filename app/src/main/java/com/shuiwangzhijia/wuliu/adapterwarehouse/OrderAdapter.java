package com.shuiwangzhijia.wuliu.adapterwarehouse;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.beanwarehouse.OrderDetailBean;
import com.shuiwangzhijia.wuliu.beanwarehouse.WarehouseOutOrderBean;
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
    private ArrayList<WarehouseOutOrderBean> mData;
    private OrderRealAdapter mMRealAdapter;


    public OrderAdapter(Context context, int type) {
        mContext = context;
        this.type = type;
        mData = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(mContext).inflate(R.layout.item_warehouse_order, parent, false);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_new_warehouse_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final WarehouseOutOrderBean item = getItem(position);
        holder.orderId.setText(item.getOut_order());
        holder.name.setText("提货人：" + item.getUname());
        final int order_type = item.getOrder_type();
        if (order_type == 0){
            holder.mStatus.setText("配送");
            holder.mStatus.setBackgroundResource(R.drawable.blue_rectangle);
        }else if (order_type == 1){
            holder.mStatus.setText("自提");
            holder.mStatus.setBackgroundResource(R.drawable.orange_rectangle);
        }
        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnViewItemClickListener.onCallClick(getItem(position).getPhone());
            }
        });
        switch (type) {
            case 0:
                List<OrderDetailBean.ActualBean> goods = item.getGoods();
//                if (order_type == 0){
                    holder.firstBtn.setText("确认提货");
//                }else if (order_type == 1){
//                    holder.firstBtn.setText("出货操作");
//                }
                holder.llDetailInfo.setVisibility(View.GONE);
                holder.mRv.setVisibility(View.VISIBLE);
                holder.mStatus.setVisibility(View.VISIBLE);
                holder.mRv.setLayoutManager(new GridLayoutManager(mContext,2));
                holder.mRv.setHasFixedSize(true);
                mMRealAdapter = new OrderRealAdapter(mContext, goods);
                holder.mRv.setAdapter(mMRealAdapter);
                holder.chuhuoshijian.setText("下单时间:");
                holder.orderDate.setText("" + DateUtils.getFormatDateStr(item.getCreate_time() * 1000L));
                holder.remark.setText("备注:" + item.getRemark());
                holder.count.setText("共"+item.getTotal()+"桶");
               // setTextStyle(holder.count, "共", "" + item.getTotal(), "桶");
                break;
            case 1:
                holder.llDetailInfo.setVisibility(View.VISIBLE);
                holder.mRv.setVisibility(View.GONE);
//                holder.mStatus.setVisibility(View.GONE);
                holder.orderDate.setText("" + DateUtils.getFormatDateStr(item.getCreate_time() * 1000L));
                if (order_type == 0){
                    holder.remark.setText("仓管员:" + item.getSname());
                }else if (order_type == 1){
                    holder.remark.setVisibility(View.GONE);
                }
                holder.returnWaterNum.setText(""+item.getTsum());
                holder.returnBucketNum.setText(""+item.getHsum());
                holder.otherBucketNum.setText(""+item.getZsum());
                holder.postNum.setText("已提货：" + item.getSum());
                holder.needNum.setText("需提货：" + item.getTotal());
                if (item.getStatus() == 1) {
                    holder.firstBtn.setText("查看详情");
                    holder.count.setText("出货中");
                } else {
                    holder.firstBtn.setText("回仓操作");
                    holder.count.setText("确认回桶");
                }
                break;
            case 2:
                holder.firstBtn.setText("查看详情");
                holder.llDetailInfo.setVisibility(View.VISIBLE);
                holder.mRv.setVisibility(View.GONE);
//                holder.mStatus.setVisibility(View.GONE);
                holder.count.setText("已完成");
                holder.orderDate.setText("" + DateUtils.getFormatDateStr(item.getCreate_time() * 1000L));
                if (order_type == 0){
                    holder.remark.setText("仓管员:" + item.getSname());
                }else if (order_type == 1){
                    holder.remark.setVisibility(View.GONE);
                }
                holder.returnWaterNum.setText(""+item.getTsum());
                holder.returnBucketNum.setText(""+item.getHsum());
                holder.otherBucketNum.setText(""+item.getZsum());
                holder.postNum.setText("已提货：" + item.getSum());
                holder.needNum.setText("需提货：" + item.getTotal());
                break;
        }
        holder.firstBtn.setTag(position);
        holder.firstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) view.getTag();
                switch (type) {
                    case 0:
//                        if (order_type == 0){
                            mOnViewItemClickListener.onHandleOrderClick(tag, mMRealAdapter,order_type);
//                        }else if (order_type == 1)
//                            mOnViewItemClickListener.onHuichangOprea(tag);
                        break;
                    case 1:
                        if (item.getStatus() == 1) {
                            mOnViewItemClickListener.onDetailClick(tag);
                        } else {
                            mOnViewItemClickListener.onHuichangOprea(tag);
                        }
                        break;
                    case 2:
                        mOnViewItemClickListener.onDetailClick(tag);
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

    }

    private void setTextStyle(TextView text, String first, String content, String second) {
        SpannableString spanString = new SpannableString(first + content + second);
        spanString.setSpan(new AbsoluteSizeSpan(24, true), first.length(), (content + first).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);//字体大小
        text.setTextColor(mContext.getResources().getColor(R.color.color_DF3A44));
        text.setText(spanString);
    }

    public WarehouseOutOrderBean getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getItemCount() {
        return (mData == null) ? 0 : mData.size();
    }

    public void setOnItemClickListener(OnViewItemClickListener listener) {
        mOnViewItemClickListener = listener;
    }

    public void setData(ArrayList<WarehouseOutOrderBean> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public ArrayList<WarehouseOutOrderBean> getData(){
        return mData;
    }

    public void addData(ArrayList<WarehouseOutOrderBean> result) {
        mData.addAll(result);
        notifyDataSetChanged();
    }


    public interface OnViewItemClickListener {
        void onCallClick(String phone);

        void onDetailClick(int position);

        void onHandleOrderClick(int position, OrderRealAdapter adapter, int order);

        void onHuichangOprea(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.callBtn)
        ImageView callBtn;
        @BindView(R.id.orderId)
        TextView orderId;
        @BindView(R.id.needNum)
        TextView needNum;
        @BindView(R.id.postNum)
        TextView postNum;
        @BindView(R.id.returnWaterNum)
        TextView returnWaterNum;
        @BindView(R.id.returnBucketNum)
        TextView returnBucketNum;
        @BindView(R.id.otherBucketNum)
        TextView otherBucketNum;
        @BindView(R.id.llDetailInfo)
        LinearLayout llDetailInfo;
        @BindView(R.id.orderDate)
        TextView orderDate;
        @BindView(R.id.remark)
        TextView remark;
        @BindView(R.id.count)
        TextView count;
        @BindView(R.id.firstBtn)
        TextView firstBtn;
        @BindView(R.id.rv)
        RecyclerView mRv;
        @BindView(R.id.status)
        TextView mStatus;
        @BindView(R.id.chuhuoshijian)
        TextView chuhuoshijian;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
