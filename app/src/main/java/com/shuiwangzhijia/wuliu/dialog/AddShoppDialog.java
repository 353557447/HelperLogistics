package com.shuiwangzhijia.wuliu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.AddShopAdapter;
import com.shuiwangzhijia.wuliu.bean.GoodsBean;
import com.shuiwangzhijia.wuliu.utils.ToastUitl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 自营桶弹窗
 * Created by wangsuli on 2017/5/23.
 */
public class AddShoppDialog extends Dialog {

    private OnConfirmClickListener listener;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.cancelBtn)
    ImageView cancelBtn;
    @BindView(R.id.sureBtn)
    TextView sureBtn;
    private List<GoodsBean> selectData = new ArrayList<>();
    private String data;
    private final AddShopAdapter mAdapter;

    public  void  setData(){

    }

    /**
     * 构造器
     *
     * @param context 上下文
     */
    public AddShoppDialog(Context context, List<GoodsBean> data, final OnConfirmClickListener listener) {
        super(context, R.style.MyDialogStyle);
        setContentView(R.layout.dialog_self_bucket_shuipiao);
        this.listener = listener;
        setCanceledOnTouchOutside(true);
        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new AddShopAdapter(context,true);
        mAdapter.setGoodsData(data);
        mAdapter.setOnItemClickListener(new AddShopAdapter.OnRecyclerViewItemClickListener() {

            @Override
            public void onItemClick(int position) {
                Object item = mAdapter.getItem(position);
                if (item instanceof GoodsBean)
                    ((GoodsBean) item).setCheck(!((GoodsBean) item).isCheck());
                mAdapter.notifyDataSetChanged();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }


    @OnClick({R.id.cancelBtn, R.id.sureBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancelBtn:
                dismiss();
                break;
            case R.id.sureBtn:
                List<GoodsBean> data = mAdapter.getGoodsData();
                if(data==null)
                    return;
                for (GoodsBean bean : data) {
                    if (bean.isCheck()) {
//                        int count = bean.getCount();
//                        bean.setCount(count > 0 ? count : 1);
                        selectData.add(bean);
                    }
                }
                if (selectData.size() == 0) {
                    ToastUitl.showToastCustom("请添加自营桶");
                    return;
                }
                listener.onConfirm(this, selectData);
                dismiss();
                break;
        }

    }


    public interface OnConfirmClickListener {
        void onConfirm(Dialog dialog, List<GoodsBean> data);
    }

    @Override
    public void show() {
        super.show();
        this.getWindow().setWindowAnimations(R.style.DialogOutAndInStyle);
    }
}