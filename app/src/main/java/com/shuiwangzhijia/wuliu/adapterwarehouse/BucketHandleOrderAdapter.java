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
import com.shuiwangzhijia.wuliu.beanwarehouse.BucketBean;
import com.shuiwangzhijia.wuliu.interfaces.ShopCartInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 操作订单-修改数量 和回桶数量 适配器
 * created by wangsuli on 2018/8/17.
 */
public class BucketHandleOrderAdapter extends RecyclerView.Adapter<BucketHandleOrderAdapter.ViewHolder> {
    private final Context mContext;

    private ShopCartInterface shopCartImp;
    private OnViewItemClickListener mOnViewItemClickListener;


    public List<BucketBean> getData() {
        return data;
    }

    private List<BucketBean> data;
    private boolean editFlag;
    private boolean isQuitBucket;


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

    public BucketHandleOrderAdapter(Context context, boolean editFlag) {
        mContext = context;
        this.editFlag = editFlag;
        data = new ArrayList<>();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_warehouse_handle_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
      /*  BucketBean mBucketBean = getItem(position);
        holder.name.setText(mBucketBean.getBname());
        if (editFlag) {
            holder.deleteBtn.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(Constant.GOODS_IMAGE_URL + mBucketBean.getB_picturl()).into(holder.image);
            holder.deleteBtn.setTag(position);
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItem((Integer) view.getTag());
                }
            });
        }
        int count = mBucketBean.getCount();
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
                BucketBean BucketBean = getItem(position);
                int countAdd = BucketBean.getCount();
                countAdd++;
                BucketBean.setCount(countAdd);
                BucketBean.setCheck(true);
                data.set(position, BucketBean);
                notifyDataSetChanged();
                if (shopCartImp != null) {
                    shopCartImp.add(view, position);
                }

            }
        });

        holder.rightDishRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BucketBean BucketBean = getItem(position);
                int countRemove = BucketBean.getCount();
                if (countRemove <= 1 && limitCount == 1) {
                    return;
                }
                countRemove--;
                if (countRemove == 0)
                    BucketBean.setCheck(false);
                BucketBean.setCount(countRemove);
                data.set(position, BucketBean);
                notifyDataSetChanged();
                if (shopCartImp != null) {
                    shopCartImp.remove(view, position);
                }
            }
        });
        holder.rightDishAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int count = data.get(position).getCount();
                EditPurchaseAmountDialog dialog = new EditPurchaseAmountDialog(mContext, count, new EditPurchaseAmountDialog.EditPurchaseAmountConfirmListener() {
                    @Override
                    public void onEditPurchaseAmountConfirm(int count) {
                        if (mOnViewItemClickListener != null) {
                            mOnViewItemClickListener.doEditCount(position, count);
                        }
                    }
                });
                dialog.show();

            }
        });
        if (isQuitBucket) {
            holder.llAddCut.setVisibility(View.INVISIBLE);
            holder.image.setVisibility(View.VISIBLE);
            holder.deleteBtn.setVisibility(View.GONE);
            holder.numTv.setVisibility(View.VISIBLE);
            holder.numTv.setText("x"+mBucketBean.getNum());
        }*/
    }
    public void setOnItemClickListener(OnViewItemClickListener listener) {
        mOnViewItemClickListener = listener;
    }
    public interface OnViewItemClickListener {
        void doEditCount(int position, int count);
    }

    @Override
    public int getItemCount() {
//        return (data == null) ? 0 : data.size();
    return 3;
    }

    public void addData(List<BucketBean> list) {
        data.addAll(list);
        notifyDataSetChanged();
    }

    public void setData(List<BucketBean> list) {
        if (list == null) {
            return;
        }
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    public BucketBean getItem(int position) {
        return data.get(position);
    }

    public void deleteItem(int position) {
        data.remove(position);
        notifyDataSetChanged();
    }

    public boolean isQuitBucket() {
        return isQuitBucket;
    }

    public void setQuitBucket(boolean quitBucket) {
        isQuitBucket = quitBucket;
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
