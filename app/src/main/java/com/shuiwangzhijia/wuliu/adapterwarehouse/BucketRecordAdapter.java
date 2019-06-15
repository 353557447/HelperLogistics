package com.shuiwangzhijia.wuliu.adapterwarehouse;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.beanwarehouse.BucketRecordBean;
import com.shuiwangzhijia.wuliu.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 记录适配器
 * created by wangsuli on 2018/8/17.
 */
public class BucketRecordAdapter extends RecyclerView.Adapter<BucketRecordAdapter.ViewHolder> {
    private final Context mContext;


    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private List<BucketRecordBean> data;

    public BucketRecordAdapter(Context context) {
        mContext = context;
        data = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_warehouse_bucket_record, parent, false);
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
        BucketRecordBean item = getItem(position);
        holder.shopName.setText(item.getSname());
        holder.date.setText(DateUtils.getFormatDateStr(item.getUpdate_time() * 1000));
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        holder.recyclerView.setHasFixedSize(true);
        RecordItemAdapter mRecordAdapter2 = new RecordItemAdapter(mContext);
        mRecordAdapter2.setData(item.getData());
        holder.recyclerView.setAdapter(mRecordAdapter2);
        holder.depositTv.setText("押金总金额：" + item.getTprice() + "元");
        holder.totalNumTv.setText("共计：" + item.getTnum() + "个桶");
    }

    public BucketRecordBean getItem(int position) {
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

    public void setData(ArrayList<BucketRecordBean> result) {
        data = result;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<BucketRecordBean> result) {
        data.addAll(result);
        notifyDataSetChanged();
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.shopName)
        TextView shopName;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;
        @BindView(R.id.depositTv)
        TextView depositTv;
        @BindView(R.id.totalNumTv)
        TextView totalNumTv;
        @BindView(R.id.llTotal)
        LinearLayout llTotal;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
