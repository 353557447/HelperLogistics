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
import com.shuiwangzhijia.wuliu.beanwarehouse.OutDetailsBean;
import com.shuiwangzhijia.wuliu.interfaces.ShopCartInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 操作订单-修改数量 和回桶数量 适配器
 * created by wangsuli on 2018/8/17.
 */
public class HandleOrderAdapter extends RecyclerView.Adapter<HandleOrderAdapter.ViewHolder> {
    private final Context mContext;
    private List<OrderDetailBean.ActualBean> data;

    private ShopCartInterface shopCartImp;
    private OutDetailsBean mData;

    public HandleOrderAdapter(Context context, List<OrderDetailBean.ActualBean> actualGoods) {
        this.mContext  = context;
        this.data = actualGoods;
    }


    public List<OrderDetailBean.ActualBean> getData() {
        return data;
    }


    private boolean editFlag, deleteFlag;


    public int getPayStyle() {
        return payStyle;
    }

    public void setPayStyle(int payStyle) {
        this.payStyle = payStyle;
    }

    //    回桶的数量可修改， 确定订单中的数量：
//    status==1 根据pay_style==0 时数量只能减 不能加 可以减到0
//    status==2 根据pay_style==1 时 数量可以增加 ，可以减少
    private int payStyle = -1;//表明数字可加减

    public int getLimitCount() {
        return limitCount;
    }

    public void setLimitCount(int limitCount) {
        this.limitCount = limitCount;
    }

    private int limitCount;

    public ShopCartInterface getShopCartInterface() {
        return shopCartImp;
    }

    public void setShopCartInterface(ShopCartInterface shopCartImp) {
        this.shopCartImp = shopCartImp;
    }

//    public HandleOrderAdapter(Context context, boolean editFlag) {
//        mContext = context;
//        this.editFlag = editFlag;
//        data = new ArrayList<>();
//    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_warehouse_handle_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
//        if (deleteFlag) {
//            holder.deleteBtn.setVisibility(View.VISIBLE);
//            holder.deleteBtn.setTag(position);
//            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    deleteItem((Integer) view.getTag());
//                }
//            });
//        }
//        if (editFlag) {
//            holder.numTv.setVisibility(View.GONE);
//            holder.llAddCut.setVisibility(View.VISIBLE);
//
//        } else {
//            holder.numTv.setVisibility(View.VISIBLE);
//            holder.llAddCut.setVisibility(View.GONE);
//        }
        OrderDetailBean.ActualBean item = getItem(position);
        holder.name.setText(item.getGname());
        holder.rightDishAccount.setText(item.getNum()+"");
//        int count = 1;
//        holder.rightDishAccount.setText("" + count);
//        if (count <= 0) {
//            holder.rightDishRemove.setVisibility(View.GONE);
//            holder.rightDishAccount.setVisibility(View.GONE);
//        } else {
//            holder.rightDishRemove.setVisibility(View.VISIBLE);
//            holder.rightDishAccount.setVisibility(View.VISIBLE);
//            holder.rightDishAccount.setText(count + "");
//        }
        holder.rightDishAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderDetailBean.ActualBean GoodsBean = getItem(position);
                int countAdd = GoodsBean.getNum();
                countAdd++;
                GoodsBean.setNum(countAdd);
//                GoodsBean.setCheck(true);
                data.set(position, GoodsBean);
                notifyDataSetChanged();
//                if (shopCartImp != null) {
//                    shopCartImp.add(view, position);
//                }
            }
        });

        holder.rightDishRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderDetailBean.ActualBean GoodsBean = getItem(position);
                int countRemove = GoodsBean.getNum();
                if (countRemove <= 1) {
                    return;
                }
                countRemove--;
//                if (countRemove == 0)
//                    GoodsBean.setCheck(false);
                GoodsBean.setNum(countRemove);
                data.set(position, GoodsBean);
                notifyDataSetChanged();
//                if (shopCartImp != null) {
//                    shopCartImp.remove(view, position);
//                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return (data == null) ? 0 : data.size();
    }

    public void addData(List<OrderDetailBean.ActualBean> list) {
        data.addAll(list);
        notifyDataSetChanged();
    }

//    public void setData(List<GoodsBean> list) {
//        if (list == null) {
//            return;
//        }
//        data.clear();
//        data.addAll(list);
//        notifyDataSetChanged();
//    }

    public OrderDetailBean.ActualBean getItem(int position) {
        return data.get(position);
    }

    public void deleteItem(int position) {
        data.remove(position);
        notifyDataSetChanged();
    }

    public void setData(OutDetailsBean data) {
        mData = data;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.right_dish_remove)
        ImageView rightDishRemove;
        @BindView(R.id.right_dish_account)
        TextView rightDishAccount;
        @BindView(R.id.right_dish_add)
        ImageView rightDishAdd;
        @BindView(R.id.llAddCut)
        LinearLayout llAddCut;
//        @BindView(R.id.deleteBtn)
//        TextView deleteBtn;
//        @BindView(R.id.numTv)
//        TextView numTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
