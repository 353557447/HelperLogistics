package com.shuiwangzhijia.wuliu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.base.Constant;
import com.shuiwangzhijia.wuliu.bean.GoodsBean;
import com.shuiwangzhijia.wuliu.interfaces.ShopCartInterface;
import com.shuiwangzhijia.wuliu.utils.ToastUitl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 操作订单-修改数量 和回桶数量 适配器
 * created by wangsuli on 2018/8/17.
 */
public class HandleOrderAdapter extends RecyclerView.Adapter<HandleOrderAdapter.ViewHolder> {
    private static final String TAG = "HandleOrderAdapter";
    private final Context mContext;
    private ShopCartInterface shopCartImp;


    public List<GoodsBean> getData() {
        return data;
    }

    private List<GoodsBean> data;
    private boolean isRecycler;


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

    public HandleOrderAdapter(Context context, boolean isRecycler) {
        mContext = context;
        this.isRecycler = isRecycler;
        data = new ArrayList<>();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_handle_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        GoodsBean GoodsBean = getItem(position);
        holder.name.setText(GoodsBean.getGname());
        if (isRecycler) {
            holder.deleteBtn.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(Constant.GOODS_IMAGE_URL + GoodsBean.getPicturl()).into(holder.image);
            holder.deleteBtn.setTag(position);
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItem((Integer) view.getTag());
                }
            });

        }
        int count = GoodsBean.getCount();
        holder.rightDishAccount.setText("" + count);
        if (count <= 0) {
            holder.rightDishRemove.setVisibility(View.GONE);
            holder.rightDishAccount.setVisibility(View.GONE);
        } else {
            holder.rightDishRemove.setVisibility(View.VISIBLE);
            holder.rightDishAccount.setVisibility(View.VISIBLE);
            holder.rightDishAccount.setText(count + "");
        }
        holder.rightDishAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoodsBean GoodsBean = getItem(position);
                int countAdd = GoodsBean.getCount();
                countAdd++;
                if (!isRecycler && payStyle == 0 && GoodsBean.getNum() < countAdd) {
                    //数量确认
                    ToastUitl.showToastCustom("新数量超过订单数量");
                    return;
                }
                GoodsBean.setCount(countAdd);
                GoodsBean.setCheck(true);
                data.set(position, GoodsBean);
                notifyDataSetChanged();
                if (shopCartImp != null) {
                    shopCartImp.add(view, position);
                }

            }
        });

        holder.rightDishRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoodsBean GoodsBean = getItem(position);
                int countRemove = GoodsBean.getCount();
                if (countRemove <= 1 && limitCount == 1) {
                    return;
                }
                countRemove--;
                if (countRemove == 0)
                    GoodsBean.setCheck(false);
                GoodsBean.setCount(countRemove);
                data.set(position, GoodsBean);
                notifyDataSetChanged();
                if (shopCartImp != null) {
                    shopCartImp.remove(view, position);
                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return (data == null) ? 0 : data.size();
    }

    public void addData(List<GoodsBean> list) {
        data.addAll(list);
        notifyDataSetChanged();
    }

    public void setData(List<GoodsBean> list) {
        if (list == null) {
            return;
        }
        if (!isRecycler) {
            for (GoodsBean bean : list) {
                bean.setCount(bean.getNum());
            }
        }
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    public GoodsBean getItem(int position) {
        return data.get(position);
    }

    public void deleteItem(int position) {
        data.remove(position);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.right_dish_remove)
        ImageView rightDishRemove;
        @BindView(R.id.right_dish_account)
        TextView rightDishAccount;
        @BindView(R.id.right_dish_add)
        ImageView rightDishAdd;
        @BindView(R.id.deleteBtn)
        TextView deleteBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
