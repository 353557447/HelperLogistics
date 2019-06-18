package com.shuiwangzhijia.wuliu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.bean.DSearchResultBean;
import com.shuiwangzhijia.wuliu.beanwarehouse.OrderDetailBean;
import com.shuiwangzhijia.wuliu.utils.ToastUitl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DSearchOrderSonAdapter extends RecyclerView.Adapter<DSearchOrderSonAdapter.ViewHolder> {
    private final Context context;
    private final List<DSearchResultBean.DataBean.GoodsBean> data;
    private OnViewItemClickListener onViewItemClickListener;

    public DSearchOrderSonAdapter(Context context, List<DSearchResultBean.DataBean.GoodsBean> actual) {
        this.context = context;
        this.data = actual;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_real, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        DSearchResultBean.DataBean.GoodsBean item = data.get(position);
        holder.mName.setText(item.getGname());
        holder.mRightDishAccount.setText("(x"+item.getNum()+")");
    }

    @Override
    public int getItemCount() {
        return data == null?0:data.size();
    }


    public interface OnViewItemClickListener {
        void doEditClick(int position, int count);
    }

    public void setOnViewItemClickListener(OnViewItemClickListener listener) {
        onViewItemClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView mName;
        @BindView(R.id.right_dish_remove)
        ImageView mRightDishRemove;
        @BindView(R.id.right_dish_account)
        TextView mRightDishAccount;
        @BindView(R.id.right_dish_add)
        ImageView mRightDishAdd;
        @BindView(R.id.llAddCut)
        LinearLayout mLlAddCut;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
