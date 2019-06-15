package com.shuiwangzhijia.wuliu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.utils.ToastUitl;


/**
 * Created by wangsuli on 2017/11/9.
 */

public class EditPurchaseAmountDialog extends Dialog implements View.OnClickListener {
    private final TextView cutBtn, addBtn;
    private final EditText editNum;
    private TextView cancelBtn, confirmBtn;
    private Context mContext;
    private EditPurchaseAmountConfirmListener listener;
    private int currentCount;

    public EditPurchaseAmountDialog(Context context, int count, final EditPurchaseAmountConfirmListener listener) {
        super(context, R.style.MyDialogStyle);
        mContext = context;
        this.currentCount = count;
        this.listener = listener;
        setContentView(R.layout.dialog_edit_purchase_amount);
        editNum = (EditText) findViewById(R.id.editNum);
        cutBtn = (TextView) findViewById(R.id.cutBtn);
        addBtn = (TextView) findViewById(R.id.addBtn);
        cancelBtn = (TextView) findViewById(R.id.cancelBtn);
        confirmBtn = (TextView) findViewById(R.id.confirmBtn);

        cutBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = editable.toString();
                if (!TextUtils.isEmpty(str)) {
                    int count = Integer.parseInt(str);
                    currentCount = count;
                }
            }
        };
        editNum.setInputType(InputType.TYPE_CLASS_NUMBER);
        editNum.addTextChangedListener(watcher);
        setCount(count + "");
    }

    /**
     * 设置数字
     *
     * @param count
     */
    private void setCount(String count) {
        editNum.setText(count);
        editNum.setSelection(count.length());
    }

    private void showHint(String hint) {
        ToastUitl.showToastCustom(hint);
         }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cutBtn:
                if (currentCount > 0) {
                    currentCount = currentCount - 5;
                    if (currentCount <= 0){
                        currentCount = 0;
                    }
                    setCount(currentCount + "");
                } else {
                    showHint("购买数量不能小于0!");
                }
                break;
            case R.id.addBtn:
                currentCount = currentCount + 5;
                setCount(currentCount + "");
                break;
            case R.id.cancelBtn:
                dismiss();
                break;
            case R.id.confirmBtn:
//                if (currentCount < 5) {
//                    showHint("购买数量不能小于5!");
//                } else {
                    listener.onEditPurchaseAmountConfirm(currentCount);
                    dismiss();
//                }
                break;
        }

    }

    public interface EditPurchaseAmountConfirmListener {
        void onEditPurchaseAmountConfirm(int count);
    }

    @Override
    public void show() {
        super.show();
        this.getWindow().setWindowAnimations(R.style.DialogOutAndInStyle);
    }
}
