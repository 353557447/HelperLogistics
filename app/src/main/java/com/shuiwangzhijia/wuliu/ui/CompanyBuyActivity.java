package com.shuiwangzhijia.wuliu.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.base.BaseAct;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 企业采购
 * created by wangsuli on 2018/8/31.
 */
public class CompanyBuyActivity extends BaseAct {
    @BindView(R.id.companyEdit)
    EditText companyEdit;
    @BindView(R.id.addressEdit)
    EditText addressEdit;
    @BindView(R.id.contactsEdit)
    EditText contactsEdit;
    @BindView(R.id.phoneEdit)
    EditText phoneEdit;
    @BindView(R.id.companyPhoneEdit)
    EditText companyPhoneEdit;
    @BindView(R.id.countEdit)
    EditText countEdit;
    @BindView(R.id.factoryEdit)
    EditText factoryEdit;
    @BindView(R.id.hintEdit)
    EditText hintEdit;
    @BindView(R.id.count)
    TextView count;
    @BindView(R.id.sureBtn)
    TextView sureBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_buy);
        ButterKnife.bind(this);
        setTitle("企业采购");
    }

    @OnClick(R.id.sureBtn)
    public void onViewClicked() {
    }
}
