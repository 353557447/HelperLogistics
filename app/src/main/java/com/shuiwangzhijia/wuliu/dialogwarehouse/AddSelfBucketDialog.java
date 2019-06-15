package com.shuiwangzhijia.wuliu.dialogwarehouse;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapterwarehouse.SelfBucketAdapter;
import com.shuiwangzhijia.wuliu.beanwarehouse.BucketBean;
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
public class AddSelfBucketDialog extends Dialog {

    private final SelfBucketAdapter mSelfBucketAdapter;
    private OnConfirmClickListener listener;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.cancelBtn)
    ImageView cancelBtn;
    @BindView(R.id.sureBtn)
    TextView sureBtn;
    private List<BucketBean> selectData = new ArrayList<>();

    /**
     * 构造器
     *
     * @param context 上下文
     */
    public AddSelfBucketDialog(Context context, List<BucketBean> data, final OnConfirmClickListener listener) {
        super(context, R.style.MyDialogStyle);
        setContentView(R.layout.dialog_self_bucket);
        this.listener = listener;
        setCanceledOnTouchOutside(true);
        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mSelfBucketAdapter = new SelfBucketAdapter(context, false);
        mSelfBucketAdapter.setBucketData(data);
        mSelfBucketAdapter.setOnItemClickListener(new SelfBucketAdapter.OnRecyclerViewItemClickListener() {

            @Override
            public void onItemClick(int position) {
                Object item = mSelfBucketAdapter.getItem(position);
                if (item instanceof BucketBean)
                    ((BucketBean) item).setCheck(!((BucketBean) item).isCheck());
                mSelfBucketAdapter.notifyDataSetChanged();
            }
        });
        mRecyclerView.setAdapter(mSelfBucketAdapter);
    }

    @OnClick({R.id.cancelBtn, R.id.sureBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancelBtn:
                dismiss();
                break;
            case R.id.sureBtn:
                List<BucketBean> data = mSelfBucketAdapter.getBucketData();
                if (data != null){
                    for (BucketBean bean : data) {
                        if (bean.isCheck()) {
//                        int count = bean.getCount();
//                        bean.setCount(count > 0 ? count : 1);
                            selectData.add(bean);
                        }
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
        void onConfirm(Dialog dialog, List<BucketBean> data);
    }

    @Override
    public void show() {
        super.show();
        this.getWindow().setWindowAnimations(R.style.DialogOutAndInStyle);
    }
}
