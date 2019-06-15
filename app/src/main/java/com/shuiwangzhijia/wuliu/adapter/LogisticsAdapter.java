package com.shuiwangzhijia.wuliu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 物流进度适配器
 * created by wangsuli on 2018/8/17.
 */
public class LogisticsAdapter extends RecyclerView.Adapter<LogisticsAdapter.ViewHolder> {
    private final Context mContext;
    private final String[] logisticsTitle;
    private int state = -1;


    public LogisticsAdapter(Context context, int state) {
        mContext = context;
        this.state = state;
        logisticsTitle = mContext.getResources().getStringArray(R.array.logisticsTitle);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_logistics, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.title.setText(logisticsTitle[position]);
        holder.line.setVisibility(position == getItemCount()-1 ? View.GONE : View.VISIBLE);
        holder.title.setSelected(state > position ? true : false);
        holder.image.setSelected(state > position ? true : false);
        holder.line.setSelected(state > position ? true : false);
    }

    public String getItem(int position) {
        return logisticsTitle[position];
    }

    @Override
    public int getItemCount() {
        return logisticsTitle.length;
    }


    public interface OnRecyclerViewItemClickListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.line)
        ImageView line;
        @BindView(R.id.title)
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
