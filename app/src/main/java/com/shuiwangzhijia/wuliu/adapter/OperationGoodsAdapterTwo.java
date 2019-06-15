package com.shuiwangzhijia.wuliu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.bean.OrderShowBean;
import com.shuiwangzhijia.wuliu.dialog.EditPurchaseAmountDialog;
import com.shuiwangzhijia.wuliu.interfaces.ShopCartInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xxc on 2018/12/7.
 */

public class OperationGoodsAdapterTwo extends RecyclerView.Adapter<OperationGoodsAdapterTwo.ViewHolder> {
    private final Context mContext;
    private final List<OrderShowBean.GoodsBean> mData;
    private ShopCartInterface shopCartImp;
    private OnViewItemClickListener mOnViewItemClickListener;
    private OnViewLanItenClickListener mOnViewLanItemClickListener;

    public OperationGoodsAdapterTwo(Context context, List<OrderShowBean.GoodsBean> goods) {
        this.mContext = context;
        this.mData = goods;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_operation_goods_two, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mName.setText(mData.get(position).getGname());
        setTextStyle(holder.mNumber,"配送:",""+mData.get(position).getSnum(),"");
        holder.mRightDishAccountLeft.setText(""+mData.get(position).getW_water());//退水
        holder.mRightDishAccountRight.setText(""+mData.get(position).getR_water());//烂水
        holder.mRightDishAddLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderShowBean.GoodsBean goodsBean = getItem(position);
                int countAdd = goodsBean.getW_water();
                countAdd++;
                if (goodsBean.getNum() < countAdd || goodsBean.getR_water()+goodsBean.getW_water() >= goodsBean.getNum()) {
                    return;
                }
                goodsBean.setW_water(countAdd);
                goodsBean.setSnum(mData.get(position).getSnum()-1);
                mData.set(position, goodsBean);
                notifyDataSetChanged();
                if (shopCartImp != null) {
                    //这里的回调是用来就算总价的
                    shopCartImp.add(view, position);
                }
            }
        });
        holder.mRightDishRemoveLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderShowBean.GoodsBean goodsBean = getItem(position);
                int countRemove = goodsBean.getW_water();
                goodsBean.getNum();
                if (countRemove <= 0) {
                    return;
                }
                countRemove--;
                goodsBean.setW_water(countRemove);
                goodsBean.setSnum(mData.get(position).getSnum()+1);
                mData.set(position, goodsBean);
                notifyDataSetChanged();
                if (shopCartImp != null) {
                    shopCartImp.remove(view, position);
                }
            }
        });
        holder.mRightDishAccountLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int count = mData.get(position).getSnum();
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
        holder.mRightDishAddRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderShowBean.GoodsBean goodsBean = getItem(position);
                int countAdd = goodsBean.getR_water();
                countAdd++;
                if (goodsBean.getR_water()+goodsBean.getW_water() >= goodsBean.getNum()){
                    return;
                }
                goodsBean.setR_water(countAdd);
                goodsBean.setSnum(mData.get(position).getSnum()-1);
                mData.set(position, goodsBean);
                notifyDataSetChanged();
                if (shopCartImp != null) {
                    //这里的回调是用来就算总价的
                    shopCartImp.add(view, position);
                }
            }
        });
        holder.mRightDishRemoveRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderShowBean.GoodsBean goodsBean = getItem(position);
                int countRemove = goodsBean.getR_water();
                goodsBean.getNum();
                if (countRemove <= 0) {
                    return;
                }
                countRemove--;
                goodsBean.setR_water(countRemove);
                goodsBean.setSnum(mData.get(position).getSnum()+1);
                mData.set(position, goodsBean);
                notifyDataSetChanged();
                if (shopCartImp != null) {
                    shopCartImp.remove(view, position);
                }
            }
        });
        holder.mRightDishAccountRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int count = mData.get(position).getSnum();
                EditPurchaseAmountDialog dialog = new EditPurchaseAmountDialog(mContext, count, new EditPurchaseAmountDialog.EditPurchaseAmountConfirmListener() {
                    @Override
                    public void onEditPurchaseAmountConfirm(int count) {
                        if (mOnViewLanItemClickListener != null) {
                            mOnViewLanItemClickListener.doEditCount(position, count);
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    private void setTextStyle(TextView text, String first, String content, String second) {
        SpannableString spanString = new SpannableString(first + content + second);
        spanString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_333333)), first.length(), (content + first).length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);//颜色
        spanString.setSpan(new AbsoluteSizeSpan(16, true), first.length(), (content + first).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);//字体大小
        text.setText(spanString);
    }

    @Override
    public int getItemCount() {
        return (mData == null) ? 0 : mData.size();
    }

    public List<OrderShowBean.GoodsBean> getData() {
        return mData;
    }

    public ShopCartInterface getShopCartInterface() {
        return shopCartImp;
    }

    public void setShopCartInterface(ShopCartInterface shopCartImp) {
        this.shopCartImp = shopCartImp;
    }

    public interface OnViewItemClickListener {
        void doEditCount(int position, int count);
    }

    public interface OnViewLanItenClickListener{
        void doEditCount(int positon,int count);
    }

    public void setOnItemLanItenClickListener(OnViewLanItenClickListener listener){
        mOnViewLanItemClickListener = listener;
    }

    public void setOnItemClickListener(OnViewItemClickListener listener) {
        mOnViewItemClickListener = listener;
    }

    public OrderShowBean.GoodsBean getItem(int position) {
        OrderShowBean.GoodsBean goodsBean = mData.get(position);
        return goodsBean;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView mName;
        @BindView(R.id.number)
        TextView mNumber;
        @BindView(R.id.right_dish_remove_Left)
        ImageView mRightDishRemoveLeft;
        @BindView(R.id.right_dish_account_Left)
        TextView mRightDishAccountLeft;
        @BindView(R.id.right_dish_add_Left)
        ImageView mRightDishAddLeft;
        @BindView(R.id.llAddCutLeft)
        LinearLayout mLlAddCutLeft;
        @BindView(R.id.right_dish_remove_right)
        ImageView mRightDishRemoveRight;
        @BindView(R.id.right_dish_account_right)
        TextView mRightDishAccountRight;
        @BindView(R.id.right_dish_add_right)
        ImageView mRightDishAddRight;
        @BindView(R.id.llAddCutRight)
        LinearLayout mLlAddCutRight;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
