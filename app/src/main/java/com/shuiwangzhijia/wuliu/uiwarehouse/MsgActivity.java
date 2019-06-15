package com.shuiwangzhijia.wuliu.uiwarehouse;

import android.os.Bundle;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.base.BaseAct;


/**消息页面
 * created by wangsuli on 2018/8/17.
 */
public class MsgActivity extends BaseAct {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        setTitle("消息");
    }
}
