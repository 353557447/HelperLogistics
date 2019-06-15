package com.shuiwangzhijia.wuliu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.OrderDialogAdapter;
import com.shuiwangzhijia.wuliu.bean.StoreListBean;
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
public class CreateOrder extends Dialog {

    private final ArrayList<StoreListBean> data;
    private OnConfirmClickListener listener;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.cancelBtn)
    ImageView cancelBtn;
    @BindView(R.id.sureBtn)
    TextView sureBtn;
    private List<StoreListBean> selectData = new ArrayList();
    private final OrderDialogAdapter mAdapter;

    /**
     * 构造器
     *  @param context 上下文
     * @param data
     */
    public CreateOrder(Context context, ArrayList<StoreListBean> data, final OnConfirmClickListener listener) {
        super(context, R.style.MyDialogStyle);
        setContentView(R.layout.dialog_create_order);
        this.listener = listener;
        setCanceledOnTouchOutside(true);
        ButterKnife.bind(this);
        this.data = data;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new OrderDialogAdapter(context,data);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
        mAdapter.setOnItemClickListener(new OrderDialogAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mAdapter.setSelectIndex(position);
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
                int selectIndex = mAdapter.getSelectIndex();
                if (selectIndex == -1){
                    ToastUitl.showToastCustom("请选择仓库");
                }else {
                    listener.onConfirm(this, mAdapter.getItem(selectIndex).getCid());
                    dismiss();
                }
                break;
        }

    }


    public interface OnConfirmClickListener {
        void onConfirm(Dialog dialog, int cid);
    }

    @Override
    public void show() {
        super.show();
        this.getWindow().setWindowAnimations(R.style.DialogOutAndInStyle);
    }
}
