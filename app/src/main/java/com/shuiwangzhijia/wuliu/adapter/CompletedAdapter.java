package com.shuiwangzhijia.wuliu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.bean.OutOrderBean;
import com.shuiwangzhijia.wuliu.utils.DateUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CompletedAdapter extends RecyclerView.Adapter<CompletedAdapter.ViewHolder> {

    private final Context mContext;
    private ArrayList<OutOrderBean> mData;
    private OnViewItemClickListener mOnViewItemClickListener;
    public CompletedAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_completed_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OutOrderBean item = getItem(position);
        holder.mDeliverConsignee.setText("提货人："+item.getUname());
        holder.mDeliverOrderNumber.setText("出货单号："+item.getOut_order());
        holder.mCompletedNeedtext.setText("需提货："+item.getTotal()+"");
        holder.mCompletedAlreadyText.setText("已提货："+item.getSum()+"");
        holder.mCompletedTuishui.setText(item.getTsum()+"");
        holder.mCompletedHuitong.setText(item.getHsum()+"");
        holder.mCompletedHead.setText("仓管员："+item.getCname());
        holder.mCompletedZaTong.setText(item.getZsum()+"");
//        holder.orderDate.setText("下单时间:" + DateUtils.getFormatDateStr(item.getTime() * 1000));
        holder.mCompletedTime.setText(DateUtils.getFormatDateStr(item.getCreate_time()*1000L));
        if (item.getStatus() == 3){
            holder.mCompletedStatus.setText("已完成");
        }

        holder.mCompletedSurebtn.setTag(position);
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnViewItemClickListener.onDetailClick((Integer) view.getTag());
            }
        });
        holder.mCompletedSurebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int)view.getTag();
                mOnViewItemClickListener.onLookDetails(tag);
            }
        });
    }

    public OutOrderBean getItem(int position){
        return mData.get(position);
    }

    @Override
    public int getItemCount() {
        return (mData == null) ? 0 : mData.size();
    }

    public void setData(ArrayList<OutOrderBean> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<OutOrderBean> result) {
        mData.addAll(result);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(CompletedAdapter.OnViewItemClickListener listener) {
        mOnViewItemClickListener = listener;
    }

    public interface OnViewItemClickListener {
        void onDetailClick(int position);

        void onLookDetails(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.deliver_consignee)
        TextView mDeliverConsignee;
        @BindView(R.id.callBtn)
        ImageView mCallBtn;
        @BindView(R.id.deliver_order_number)
        TextView mDeliverOrderNumber;
        @BindView(R.id.completed_needtext)
        TextView mCompletedNeedtext;
        @BindView(R.id.completed_already_text)
        TextView mCompletedAlreadyText;
        @BindView(R.id.completed_tuishui)
        TextView mCompletedTuishui;
        @BindView(R.id.completed_huitong)
        TextView mCompletedHuitong;
        @BindView(R.id.completed_head)
        TextView mCompletedHead;
        @BindView(R.id.completed_time)
        TextView mCompletedTime;
        @BindView(R.id.completed_status)
        TextView mCompletedStatus;
        @BindView(R.id.completed_surebtn)
        TextView mCompletedSurebtn;
        @BindView(R.id.completed_zatong)
        TextView mCompletedZaTong;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
