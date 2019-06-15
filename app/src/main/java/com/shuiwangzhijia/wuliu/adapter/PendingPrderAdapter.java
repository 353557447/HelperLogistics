package com.shuiwangzhijia.wuliu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.bean.SendOrderBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PendingPrderAdapter extends RecyclerView.Adapter<PendingPrderAdapter.ViewHolder> {


    private final Context mContext;
    private ArrayList<SendOrderBean> mData;
    private int mFromType;
    private OnViewItemClickListener mOnViewItemClickListener;
    //辅助的list来记录选中的item
    private List<Integer> checkPositionlist;
    public PendingPrderAdapter(Context context,int fromType) {
        this.mContext = context;
        this.mFromType = fromType;
        checkPositionlist = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_pending, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //防止复用导致checkbox多选的问题
        holder.mItemPendingCb.setTag(new Integer(position));
        if (checkPositionlist != null){
            holder.mItemPendingCb.setChecked(checkPositionlist.contains(new Integer(position))?true:false);
        }else {
            holder.mItemPendingCb.setChecked(false);
        }
        holder.mItemPendingCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.mItemPendingCb.isSelected()){
                    return;
                }
                if (holder.mItemPendingCb.isChecked()){
                    mOnViewItemClickListener.onSelectClick((int) view.getTag());
                    holder.mStatus.setText("可选");
                    if (!checkPositionlist.contains(holder.mItemPendingCb.getTag())){
                        checkPositionlist.add(new Integer(position));
                    }
                }else {
                    mOnViewItemClickListener.onDeleteClick((int) view.getTag());
                    holder.mStatus.setText("未配送");
                    if (checkPositionlist.contains(holder.mItemPendingCb.getTag())){
                        checkPositionlist.remove(new Integer(position));
                    }

                }
            }
        });
        SendOrderBean item = getItem(position);
        int flag = item.getFlag();
        int is_selected = item.getIs_selected();
        if (mFromType == 1 ){
            if (item.getFlag() == 1){
                holder.mItemPendingCb.setChecked(true);
            }
        }
        if (mFromType == 2){
            if (is_selected == 1){
                holder.mItemPendingCb.setSelected(true);
                holder.mStatus.setVisibility(View.VISIBLE);
                holder.mStatus.setText("已配送");
                holder.mStatus.setTextColor(mContext.getResources().getColor(R.color.white));
            }else {
                if (flag == 1){
                    holder.mItemPendingCb.setChecked(true);
                    holder.mStatus.setVisibility(View.VISIBLE);
                    holder.mStatus.setText("待配送");
                    holder.mStatus.setTextColor(Color.parseColor("#FFF000"));
                }else {
                    holder.mItemPendingCb.setSelected(false);
                    holder.mStatus.setVisibility(View.VISIBLE);
                    holder.mStatus.setText("可选");
                    holder.mStatus.setTextColor(Color.parseColor("#FFF000"));
                }
            }
        }
        holder.mDeliverOrderNumber.setText(""+item.getOrder_sn());
        holder.mItemPendingTotal.setText("共"+item.getSnum()+"桶");
        holder.mItemPendingShopName.setText("水店名称："+item.getSname());
        holder.mItemPendingAdress.setText("送至："+item.getAddr());
        holder.mItemPendingRv.setLayoutManager(new GridLayoutManager(mContext,2));
        holder.mItemPendingRv.setHasFixedSize(true);
        List<SendOrderBean.GoodsBean> goods = item.getGoods();
        PendingItemAdapter adapter = new PendingItemAdapter(mContext,goods);
        holder.mItemPendingRv.setAdapter(adapter);
    }

    private void setTextStyle(TextView text, String first, String content, String second) {
        SpannableString spanString = new SpannableString(first + content + second);
        spanString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_ff3939)), first.length(), (content + first).length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);//颜色
        spanString.setSpan(new AbsoluteSizeSpan(24, true), first.length(), (content + first).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);//字体大小
        text.setText(spanString);
    }

    @Override
    public int getItemCount() {
        return (mData == null)?0:mData.size();
    }

    public void setData(ArrayList<SendOrderBean> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<SendOrderBean> result) {
        mData.addAll(result);
        notifyDataSetChanged();
    }

    public SendOrderBean getItem(int position){
        SendOrderBean sendOrderBean = mData.get(position);
        return sendOrderBean;
    }

    public ArrayList<SendOrderBean> getData() {
        return mData;
    }

    public interface OnViewItemClickListener {
        void onSelectClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnViewItemClickListener listener) {
        mOnViewItemClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.item_pending_cb)
        CheckBox mItemPendingCb;
        @BindView(R.id.status)
        TextView mStatus;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
