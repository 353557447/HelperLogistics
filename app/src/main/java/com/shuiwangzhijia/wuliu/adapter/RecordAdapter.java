package com.shuiwangzhijia.wuliu.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.bean.RecordBean;
import com.shuiwangzhijia.wuliu.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 记录适配器
 * created by wangsuli on 2018/8/17.
 */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private final Context mContext;


    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private List<RecordBean> data;
    private int type;

    public RecordAdapter(Context context, int type) {
        mContext = context;
        this.type = type;
        data = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRecyclerViewItemClickListener.onItemClick((Integer) view.getTag());
            }
        });
        RecordBean item = getItem(position);
        holder.shopName.setText(item.getSname());
        holder.date.setText(DateUtils.getFormatDateStr(item.getUpdate_time() * 1000));
        switch (type) {
            case 1:
                holder.count.setText("" + item.getNum());
                setTextStyle(holder.first, "" + item.getStay_water(), "\n" + "待送水");
                setTextStyle(holder.second, item.getAlready_water(), "\n" + "已完成");
                break;
            case 2:
                holder.count.setText("" + item.getNum());
                setTextStyle(holder.first, "" + item.getPayment(), "\n" + "扫码支付");
                setTextStyle(holder.second, "" + item.getDriver_payment(), "\n" + "司机代付");
                break;
            case 3:
                holder.count.setText("" + item.getNum());
                setTextStyle(holder.first, "" + item.getSelfnum(), "\n" + "自营桶");
                setTextStyle(holder.second, "" + item.getZnum(), "\n" + "杂桶");
                holder.second.setTag(position);
                holder.second.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onRecyclerViewItemClickListener.onOtherBucketClick((Integer) view.getTag());
                    }
                });
                holder.first.setTag(position);
                holder.first.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onRecyclerViewItemClickListener.onSelfBucketClick((Integer) view.getTag());
                    }
                });
                break;
            case 4:
                holder.llInfo.setVisibility(View.GONE);
                holder.recyclerView.setVisibility(View.VISIBLE);
                holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                holder.recyclerView.setHasFixedSize(true);
                RecordItemAdapter mRecordAdapter = new RecordItemAdapter(mContext);
                mRecordAdapter.setData(item.getData());
                holder.recyclerView.setAdapter(mRecordAdapter);
                holder.count.setText("" + item.getTnum());
                break;
            case 5:
                holder.llInfo.setVisibility(View.GONE);
                holder.count.setText("" + item.getTnum());
                break;
        }

    }


    private void setTextStyle(TextView text, String num, String title) {
        SpannableString spanString = new SpannableString(num + title);
        ForegroundColorSpan colorSpan = null;
        if (Double.valueOf(num) == 0) {
            colorSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_999999));
        } else {
            colorSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_30adfd));
        }
        spanString.setSpan(colorSpan, 0, spanString.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);//颜色
        text.setText(spanString);
    }

    public RecordBean getItem(int position) {
        return data.get(position);
    }

    public void deleteItem(int position) {
        data.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (data == null) ? 0 : data.size();
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        onRecyclerViewItemClickListener = listener;
    }

    public void setData(ArrayList<RecordBean> result) {
        data = result;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<RecordBean> result) {
        data.addAll(result);
        notifyDataSetChanged();
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(int position);
        void onSelfBucketClick(int sid);
        void onOtherBucketClick(int sid);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.shopName)
        TextView shopName;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.count)
        TextView count;
        @BindView(R.id.first)
        TextView first;
        @BindView(R.id.second)
        TextView second;
        @BindView(R.id.llInfo)
        LinearLayout llInfo;
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
