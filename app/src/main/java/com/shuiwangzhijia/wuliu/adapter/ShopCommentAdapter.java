package com.shuiwangzhijia.wuliu.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.bean.CommentBean;
import com.shuiwangzhijia.wuliu.utils.DensityUtils;
import com.shuiwangzhijia.wuliu.utils.GridDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 商家评论适配器
 * created by wangsuli on 2018/8/21.
 */
public class ShopCommentAdapter extends RecyclerView.Adapter<ShopCommentAdapter.ViewHolder> {
    private final Context mContext;

    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private List<CommentBean> data;


    public ShopCommentAdapter(Context context) {
        mContext = context;
        data = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_shop_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        holder.imageRecyclerView.setHasFixedSize(true);
        holder.imageRecyclerView.addItemDecoration(new GridDividerItemDecoration(DensityUtils.dp2px(5), mContext.getResources().getColor(R.color.color_f5f5f5)));
        CommentBean item = getItem(position);
        holder.name.setText(item.getName());
        holder.description.setText("" + item.getComment());
        holder.date.setText("" + item.getUpdate_time());
        holder.startNum.setRating(item.getScore());
        ImageAdapter imageAdapter = new ImageAdapter(mContext, true);
        imageAdapter.setCommentImageList(item.getList());
        /*imageAdapter.setOnItemClickListener(new ImageAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });*/
        holder.imageRecyclerView.setAdapter(imageAdapter);

    }

    public CommentBean getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return (data == null) ? 0 : data.size();
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        onRecyclerViewItemClickListener = listener;
    }

    public void setData(List<CommentBean> result) {
        data = result;
        notifyDataSetChanged();
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.startNum)
        RatingBar startNum;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.imageRecyclerView)
        RecyclerView imageRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
