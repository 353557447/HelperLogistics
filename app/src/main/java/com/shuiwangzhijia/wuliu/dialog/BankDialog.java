package com.shuiwangzhijia.wuliu.dialog;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.BankAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by wangsuli on 2018/8/20.
 */
public class BankDialog extends BottomDialog {
    private final BankAdapter mBankAdapter;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    public BankDialog(Context context, final OnConfirmClickListener listener) {
        super(context);
        setContentView(R.layout.dialog_bank);
        setCanceledOnTouchOutside(true);
        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mBankAdapter = new BankAdapter(context);
        mBankAdapter.setOnItemClickListener(new BankAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                listener.onConfirm(mBankAdapter.getItem(position));
            }
        });
        mRecyclerView.setAdapter(mBankAdapter);
    }

    public interface OnConfirmClickListener {
        void onConfirm(String bank);
    }
}
