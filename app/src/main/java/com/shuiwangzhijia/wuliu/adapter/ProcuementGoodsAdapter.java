package com.shuiwangzhijia.wuliu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.base.Constant;
import com.shuiwangzhijia.wuliu.bean.GoodsBean;
import com.shuiwangzhijia.wuliu.dialog.EditPurchaseAmountDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/12/11.
 */

public class ProcuementGoodsAdapter extends RecyclerView.Adapter<ProcuementGoodsAdapter.ViewHolder> {
    private final Context mContext;
    private ArrayList<GoodsBean> mData;
    private OnViewItemClickListener mOnViewItemClickListener;
    public ProcuementGoodsAdapter(Context context) {
        this.mContext = context;
        mData = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_procuement_goods, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mAddBtn.setTag(position);
        holder.mCutBtn.setTag(position);
        GoodsBean item = getItem(position);
        holder.mName.setText(item.getGname());
        holder.mPrice.setText(item.getPrice());
        Glide.with(mContext).load(Constant.GOODS_IMAGE_URL + item.getPicturl()).placeholder(R.drawable.wutupian).into(holder.mImage);
        holder.mNum.setText(item.getNum()+"");
        holder.mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnViewItemClickListener.onAddClick((int)view.getTag());
            }
        });
        holder.mCutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnViewItemClickListener.onCutClick((int)view.getTag());
            }
        });
        holder.mNum.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public int getItemCount() {
        return (mData == null)?0:mData.size();
    }

    public GoodsBean getItem(int positino){
        return mData.get(positino);
    }

    public void setData(ArrayList<GoodsBean> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public List<GoodsBean> getData(){
        return mData;
    }

    public void addData(ArrayList<GoodsBean> result) {
        mData.addAll(result);
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }

    public interface OnViewItemClickListener{
        void onAddClick(int position);

        void onCutClick(int position);

        void doEditCount(int position, int count);
    }

    public void setOnItemClickListener(OnViewItemClickListener listener) {
        mOnViewItemClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView mImage;
        @BindView(R.id.name)
        TextView mName;
        @BindView(R.id.price)
        TextView mPrice;
        @BindView(R.id.cutBtn)
        RelativeLayout mCutBtn;
        @BindView(R.id.num)
        TextView mNum;
        @BindView(R.id.addBtn)
        RelativeLayout mAddBtn;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
