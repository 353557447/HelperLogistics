package com.shuiwangzhijia.wuliu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.DriverAdapter;
import com.shuiwangzhijia.wuliu.bean.DriverBean;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.utils.ToastUitl;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 司机弹窗
 * Created by wangsuli on 2017/5/23.
 */
public class DriverDialog extends Dialog {

    private final DriverAdapter mDriverAdapter;
    private OnConfirmClickListener listener;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.cancelBtn)
    ImageView cancelBtn;
    @BindView(R.id.sureBtn)
    TextView sureBtn;

    /**
     * 构造器
     *
     * @param context 上下文
     */
    public DriverDialog(Context context, final OnConfirmClickListener listener) {
        super(context, R.style.MyDialogStyle);
        setContentView(R.layout.dialog_driver);
        this.listener = listener;
        setCanceledOnTouchOutside(true);
        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mDriverAdapter = new DriverAdapter(context);
        mDriverAdapter.setOnItemClickListener(new DriverAdapter.OnRecyclerViewItemClickListener() {

            @Override
            public void onItemClick(int position) {
                mDriverAdapter.setSelectIndex(position);
            }
        });
        mRecyclerView.setAdapter(mDriverAdapter);
        getDriverList();
    }

    private void getDriverList() {
        RetrofitUtils.getInstances().create().getDriverList().enqueue(new Callback<EntityObject<ArrayList<DriverBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<DriverBean>>> call, Response<EntityObject<ArrayList<DriverBean>>> response) {
                EntityObject<ArrayList<DriverBean>> body = response.body();
                if (body.getCode() == 200) {
                    mDriverAdapter.setData(body.getResult());
                }

            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<DriverBean>>> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.cancelBtn, R.id.sureBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancelBtn:
                dismiss();
                break;
            case R.id.sureBtn:
                int selectIndex = mDriverAdapter.getSelectIndex();
                if (selectIndex == -1) {
                    ToastUitl.showToastCustom("请选择司机");
                } else {
                    listener.onConfirm(this, mDriverAdapter.getItem(selectIndex).getId());
                    dismiss();
                }
                break;
        }

    }


    public interface OnConfirmClickListener {
        void onConfirm(Dialog dialog, int driverId);
    }

    @Override
    public void show() {
        super.show();
        this.getWindow().setWindowAnimations(R.style.DialogOutAndInStyle);
    }
}
