package com.shuiwangzhijia.wuliu.adapterwarehouse;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.beanwarehouse.OrderDetailBean;
import com.shuiwangzhijia.wuliu.utils.ToastUitl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OrderRealAdapter extends RecyclerView.Adapter<OrderRealAdapter.ViewHolder> {
    private final Context context;
    private final List<OrderDetailBean.ActualBean> data;
    private OnViewItemClickListener onViewItemClickListener;

    public OrderRealAdapter(Context context, List<OrderDetailBean.ActualBean> actual) {
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
        OrderDetailBean.ActualBean item = getItem(position);
        holder.mName.setText(item.getGname());
        holder.mRightDishAccount.setText("(x"+item.getNum()+")");
        holder.mRightDishRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderDetailBean.ActualBean goodsBeanX = getItem(position);
                int countRemove = goodsBeanX.getNum();
                if (countRemove<=1){
                    ToastUitl.showToastCustom("出货数量不能为0");
                    return;
                }
                countRemove--;
                goodsBeanX.setNum(countRemove);
                data.set(position,goodsBeanX);
                notifyDataSetChanged();
            }
        });
        holder.mRightDishAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderDetailBean.ActualBean goodsBeanX = getItem(position);
                int countAdd = goodsBeanX.getNum();
                countAdd++;
                goodsBeanX.setNum(countAdd);
                data.set(position,goodsBeanX);
                notifyDataSetChanged();
            }
        });
//        holder.mRightDishAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int num = data.get(position).getNum();
//                EditPurchaseAmountDialog dialog = new EditPurchaseAmountDialog(context, num, new EditPurchaseAmountDialog.EditPurchaseAmountConfirmListener() {
//                    @Override
//                    public void onEditPurchaseAmountConfirm(int count) {
//                        if (onViewItemClickListener != null) {
//                            onViewItemClickListener.doEditClick(position, count);
//                        }
//                    }
//                });
//                dialog.show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return data == null?0:data.size();
    }

    public OrderDetailBean.ActualBean getItem(int position) {
        return data.get(position);
    }

    public List<OrderDetailBean.ActualBean> getData(){
        return data;
    };

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
