package com.shuiwangzhijia.wuliu.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.bean.TicketBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 水票适配器
 * created by wangsuli on 2018/8/17.
 */
public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {
    private final Context mContext;


    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private List<TicketBean> data;
    private int type;

    public TicketAdapter(Context context, int type) {
        mContext = context;
        this.type = type;
        data = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_ticket, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.state.setTag(position);
        holder.state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRecyclerViewItemClickListener != null)
                    onRecyclerViewItemClickListener.onUpDownClick((Integer) view.getTag());
            }
        });
        holder.check.setTag(position);
        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRecyclerViewItemClickListener != null)
                    onRecyclerViewItemClickListener.onSelectClick((Integer) view.getTag());
            }
        });
        holder.buyBtn.setTag(position);
        holder.buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRecyclerViewItemClickListener != null)
                    onRecyclerViewItemClickListener.onPayClick((Integer) view.getTag());
            }
        });
      /*  TicketBean bean = getItem(position);
        holder.ticketName.setText(bean.getGname());
        holder.shopName.setText(bean.getSname());
        if (bean.getStart() > 0 && bean.getEnd() > 0) {
            holder.limitDate.setText("有效期限:" + DateUtils.getYMDTime(bean.getStart()) + "~" + DateUtils.getYMDTime(bean.getEnd()));
        } else {
            holder.limitDate.setText("有效期限:" + "永久有效");

        }
        switch (type) {
            case -2:
                //选择水票
                if (bean.getCuse() == 1) {
                    holder.check.setVisibility(View.VISIBLE);
                    holder.itemView.setBackgroundResource(R.drawable.keyongshuipiao);
                } else {
                    holder.itemView.setBackgroundResource(R.drawable.bukeyong);
                    holder.check.setVisibility(View.GONE);
                }
                holder.check.setChecked(bean.isCheck());
                setNumStyle(holder.leftTicket, "剩余", bean.getSum() + "", "张");
                holder.totalTicket.setText("共" + bean.getTotal_num() + "张");
                break;
            case -1:
                //优惠水票
                holder.itemView.setBackgroundResource(R.drawable.keyongshuipiao);
                holder.price.setVisibility(View.VISIBLE);
                holder.ticketPrice.setVisibility(View.VISIBLE);
                holder.buyBtn.setVisibility(View.VISIBLE);
                holder.totalTicket.setVisibility(View.VISIBLE);
                holder.ticketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                holder.totalTicket.setText("共" + bean.getSnum() + "张");
                setNumStyle(holder.leftTicket, "￥", bean.getSprice() + "", " ");
                holder.ticketPrice.setText("" + bean.getPprice());
                break;
            case 1:
                //可用水票
                holder.itemView.setBackgroundResource(R.drawable.keyongshuipiao);
                holder.totalTicket.setText("共" + bean.getSnum() + "张");
                setNumStyle(holder.leftTicket, "剩余", bean.getSum() + "", "张");
                holder.totalTicket.setText("共" + bean.getTotal() + "张");
                break;
            case 2:
                //已使用
                holder.itemView.setBackgroundResource(R.drawable.yishiyong);
                setNumStyle(holder.leftTicket, "已使用", bean.getSum() + "", "张");
                holder.totalTicket.setText("共" + bean.getTotal() + "张");
                break;
            case 3:
                //已过期
                holder.itemView.setBackgroundResource(R.drawable.yiguoqi);
                setNumStyle(holder.leftTicket, "剩余", bean.getSum() + "", "张");
                holder.totalTicket.setText("共" + bean.getTotal() + "张");
                break;
            case 5:
                //店铺水票
                holder.itemView.setBackgroundResource(R.drawable.keyongshuipiao);
                holder.price.setVisibility(View.VISIBLE);
                holder.ticketPrice.setVisibility(View.VISIBLE);
                holder.buyBtn.setVisibility(View.GONE);
                holder.totalTicket.setVisibility(View.VISIBLE);
                holder.state.setVisibility(View.VISIBLE);
                holder.ticketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                holder.totalTicket.setText("共" + bean.getSnum() + "张");
                setNumStyle(holder.leftTicket, "￥", bean.getSprice() + "", " ");
                holder.ticketPrice.setText("" + bean.getPprice());
                if (bean.getIs_up() == 1) {
                    holder.state.setText("正在售卖");
                } else {
                    holder.state.setText("暂停售卖");
                }
                holder.state.setSelected(bean.getIs_up() == 1 ? true : false);
                break;
        }*/
    }

    private void setNumStyle(TextView text, String first, String content, String second) {
        SpannableString spanString = new SpannableString(first + content + second);
        spanString.setSpan(new AbsoluteSizeSpan(18, true), first.length(), (content + first).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);//字体大小
        spanString.setSpan(new StyleSpan(Typeface.BOLD), first.length(), (content + first).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);//字体加粗
        text.setText(spanString);
    }

    public TicketBean getItem(int position) {
        return data.get(position);
    }

    public void deleteItem(int position) {
        data.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
//        return (data == null) ? 0 : data.size();
    return 3;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        onRecyclerViewItemClickListener = listener;
    }

    public void setData(ArrayList<TicketBean> result) {
        for (TicketBean bean : result) {
            bean.setCheck(bean.getUse() == 1 ? true : false);
        }
        data = result;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<TicketBean> result) {
        data.addAll(result);
        notifyDataSetChanged();
    }

    public interface OnRecyclerViewItemClickListener {
        void onSelectClick(int position);

        void onPayClick(int position);

        void onUpDownClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ticketName)
        TextView ticketName;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.ticketPrice)
        TextView ticketPrice;
        @BindView(R.id.leftTicket)
        TextView leftTicket;
        @BindView(R.id.totalTicket)
        TextView totalTicket;
        @BindView(R.id.buyBtn)
        TextView buyBtn;
        @BindView(R.id.shopName)
        TextView shopName;
        @BindView(R.id.limitDate)
        TextView limitDate;

        @BindView(R.id.check)
        CheckBox check;
        @BindView(R.id.state)
        TextView state;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
