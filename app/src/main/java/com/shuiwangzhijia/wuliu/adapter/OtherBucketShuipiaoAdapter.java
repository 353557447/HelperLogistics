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
import com.shuiwangzhijia.wuliu.bean.CanShuipiaoBean;
import com.shuiwangzhijia.wuliu.dialog.EditPurchaseAmountDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xxc on 2018/12/7.
 */

public class OtherBucketShuipiaoAdapter extends RecyclerView.Adapter<OtherBucketShuipiaoAdapter.ViewHolder>{

    private final Context mContext;
    private List<CanShuipiaoBean> mData;
    private OnViewItemClickListener mOnViewItemClickListener;
    public OtherBucketShuipiaoAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public OtherBucketShuipiaoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_handle_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OtherBucketShuipiaoAdapter.ViewHolder holder, final int position) {
        CanShuipiaoBean item = getItem(position);
        holder.name.setText(item.getS_name());
        holder.deleteBtn.setVisibility(View.VISIBLE);
        holder.deleteBtn.setTag(position);
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem((int)view.getTag());
            }
        });
        int count = item.getCount();
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
                CanShuipiaoBean BucketBean = getItem(position);
                int countAdd = BucketBean.getCount();
                countAdd++;
                BucketBean.setCount(countAdd);
                BucketBean.setCheck(true);
                mData.set(position, BucketBean);
                notifyDataSetChanged();
            }
        });

        holder.rightDishRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CanShuipiaoBean BucketBean = getItem(position);
                int countRemove = BucketBean.getCount();
                if (countRemove <= 1) {
                    return;
                }
                countRemove--;
                if (countRemove == 0)
                    BucketBean.setCheck(false);
                BucketBean.setCount(countRemove);
                mData.set(position, BucketBean);
                notifyDataSetChanged();
            }
        });
        holder.rightDishAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int count = mData.get(position).getCount();
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
    }

    private void deleteItem(int tag) {
        mData.remove(tag);
        notifyDataSetChanged();
    }

    public interface OnViewItemClickListener {
        void doEditCount(int position, int count);
    }
    public void setOnItemClickListener(OnViewItemClickListener listener) {
        mOnViewItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return (mData == null)?0:mData.size();
    }

    public CanShuipiaoBean getItem(int position){
        return mData.get(position);
    }

    public void setData(List<CanShuipiaoBean> data) {
//        if (list == null) {
//            return;
//        }
//        data.clear();
//        data.addAll(list);
        mData = data;
        notifyDataSetChanged();

    }

    public List<CanShuipiaoBean> getData(){
        return mData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
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
        @BindView(R.id.llAddCut)
        LinearLayout llAddCut;
        @BindView(R.id.deleteBtn)
        TextView deleteBtn;
        @BindView(R.id.numTv)
        TextView numTv;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
