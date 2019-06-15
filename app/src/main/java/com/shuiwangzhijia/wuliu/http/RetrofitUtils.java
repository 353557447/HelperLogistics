package com.shuiwangzhijia.wuliu.http;


import android.widget.Toast;

import com.shuiwangzhijia.wuliu.base.App;
import com.shuiwangzhijia.wuliu.base.Constant;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求封装类
 * Created by wangsuli on 2018/8/22.
 */

public class RetrofitUtils {

    private static RetrofitUtils instances;


    public static RetrofitUtils getInstances() {
        if (instances == null) {
            instances = new RetrofitUtils();
        }
        return instances;
    }
/*
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x101:

                    if (FndUtils.isConnected(App.getContext())){

                    }else{
                        hint("网络已断开");
                    }
                    break;
            }
        }
    };*/

    public AppService create() {
        // handler.sendEmptyMessage(0x101);//通过handler发送一个更新数据的标记
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(new LoggingInterceptor());
        Retrofit retrofit = new Retrofit.Builder()
                .client(client.build())
                .baseUrl(Constant.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(AppService.class);
    }

    private void hint(String text) {
        Toast.makeText(App.getContext(), text, Toast.LENGTH_SHORT).show();
    }

}
