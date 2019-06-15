package com.shuiwangzhijia.wuliu.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.bean.CashBean;
import com.shuiwangzhijia.wuliu.dialog.BankDialog;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 提现页面
 * created by wangsuli on 2018/8/20.
 */
public class CashActivity extends BaseAct {
    @BindView(R.id.editMoney)
    EditText editMoney;
    @BindView(R.id.allBtn)
    TextView allBtn;
    @BindView(R.id.editBankName)
    EditText editBankName;
    @BindView(R.id.editBankNo)
    EditText editBankNo;
    @BindView(R.id.editBank)
    TextView editBank;
    @BindView(R.id.editHint)
    EditText editHint;
    @BindView(R.id.postBtn)
    TextView postBtn;
    @BindView(R.id.yuan)
    TextView yuan;
    @BindView(R.id.hintMoney)
    TextView hintMoney;
    private BankDialog bankDialog;
    private String id = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);
        ButterKnife.bind(this);
        setTitle("提现");
        getInfo();
    }

    private void getInfo() {
        RetrofitUtils.getInstances().create().getCashInfo().enqueue(new Callback<EntityObject<CashBean>>() {
            @Override
            public void onResponse(Call<EntityObject<CashBean>> call, Response<EntityObject<CashBean>> response) {
                EntityObject<CashBean> body = response.body();
                if (body.getCode() == 200) {
                    CashBean result = body.getResult();
                    int status = result.getStatus();
                    if (status == 1 || status == 3) {
                        hintMoney.setText(result.getBanlance());
                        editBankName.setText(result.getAccount());
                        editBankNo.setText(result.getCard_no());
                        editBank.setText(result.getBank());
                        editHint.setText(result.getRemark());
                        id = result.getId() + "";
                        postBtn.setText("审核中");
                        postBtn.setBackgroundResource(R.drawable.gray_rectangle);
                        allBtn.setSelected(false);
                        allBtn.setClickable(false);
                        editBank.setClickable(false);
                    } else {
                        postBtn.setBackgroundResource(R.drawable.blue_rectangle);
                        postBtn.setSelected(true);
                        postBtn.setText("提交申请");
                    }
                } else {
                    hint(body.getMsg());
                }

            }

            @Override
            public void onFailure(Call<EntityObject<CashBean>> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.allBtn, R.id.editBank, R.id.postBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.allBtn:
                String money = hintMoney.getText().toString();
                editMoney.setText(money);
                break;
            case R.id.editBank:
                bankDialog = new BankDialog(this, new BankDialog.OnConfirmClickListener() {
                    @Override
                    public void onConfirm(String back) {
                        editBank.setText(back);
                        bankDialog.dismiss();
                    }
                });
                if (!bankDialog.isShowing()) {
                    bankDialog.show();
                }
                break;
            case R.id.postBtn:
                postCash();
                break;
        }
    }

    private void postCash() {
        if (!TextUtils.isEmpty(id)) {
            RetrofitUtils.getInstances().create().postCash("", "", "", "", "", id).enqueue(new Callback<EntityObject<CashBean>>() {
                @Override
                public void onResponse(Call<EntityObject<CashBean>> call, Response<EntityObject<CashBean>> response) {
                    EntityObject<CashBean> body = response.body();
                    hint(body.getMsg());
                }

                @Override
                public void onFailure(Call<EntityObject<CashBean>> call, Throwable t) {

                }
            });
        } else {
            String money = editMoney.getText().toString();
            String account = editBankName.getText().toString();
            String cardNo = editBankNo.getText().toString();
            String bank = editBank.getText().toString();
            String remark = editHint.getText().toString();
            if (TextUtils.isEmpty(money)) {
                hint("提现金额不能为空");
                return;
            }
            if (TextUtils.isEmpty(account)) {
                hint("请输入开户姓名");
                return;
            }
            if (TextUtils.isEmpty(cardNo)) {
                hint("银行卡号不能为空");
                return;
            }
            if (TextUtils.isEmpty(bank)) {
                hint("请选择银行");
                return;
            }
            RetrofitUtils.getInstances().create().postCash(money, remark, account, bank, cardNo, id).enqueue(new Callback<EntityObject<CashBean>>() {
                @Override
                public void onResponse(Call<EntityObject<CashBean>> call, Response<EntityObject<CashBean>> response) {
                    EntityObject<CashBean> body = response.body();
                    hint(body.getMsg());
                }

                @Override
                public void onFailure(Call<EntityObject<CashBean>> call, Throwable t) {

                }
            });
        }
    }

    private void hint(String text) {
        Toast.makeText(CashActivity.this, text, Toast.LENGTH_SHORT).show();
    }

}
