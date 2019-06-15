package com.shuiwangzhijia.wuliu.base;

import android.app.Application;
import android.content.Context;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.utils.CommonUtils;
import com.shuiwangzhijia.wuliu.utils.ToastUtils;
import com.shuiwangzhijia.wuliu.view.FndHeader;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * created by wangsuli on 2018/8/20.
 */
public class App extends Application {
    private static App mApp;
    private static Context mApplicationContext;
    private static IWXAPI iwxapi;

    public static Application getInstance() {
        return mApp;
    }

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
  /*  SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
        @Override
        public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
            layout.setPrimaryColorsId(R.color.waterColor, android.R.color.white);//全局设置主题颜色
            return new WaterDropHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        }
    });*/
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.waterColor, android.R.color.white);//全局设置主题颜色
                return new FndHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
       // KLog.init(true, "Kai");
        ToastUtils.init(this);
        //讯飞
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5b967215");
        initData();
        initBugly();
        initWeixin();
    }

    private void initWeixin() {
        iwxapi = WXAPIFactory.createWXAPI(this, Constant.WXAPPID, true); //初始化微信api
        iwxapi.registerApp(Constant.WXAPPID); //注册appid  appid可以在开发平台获取
    }

    public static IWXAPI getIwxapi() {
        return iwxapi;
    }

    /**
     * 初始化腾讯bug管理平台
     */
    private void initBugly() {
        /* Bugly SDK初始化
        * 参数1：上下文对象
        * 参数2：APPID，平台注册时得到,注意替换成你的appId
        * 参数3：是否开启调试模式，调试模式下会输出'CrashReport'tag的日志
        * 注意：如果您之前使用过Bugly SDK，请将以下这句注释掉。
        */
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setAppVersion(CommonUtils.getAppVersionName(this));
        strategy.setAppPackageName("com.funengda.driver");
        strategy.setAppReportDelay(20000);                          //Bugly会在启动20s后联网同步数据

        /*  第三个参数为SDK调试模式开关，调试模式的行为特性如下：
            输出详细的Bugly SDK的Log；
            每一条Crash都会被立即上报；
            自定义日志将会在Logcat中输出。
            建议在测试阶段建议设置成true，发布时设置为false。*/

        CrashReport.initCrashReport(getApplicationContext(), "5d8f88bb88", true ,strategy);

        //Bugly.init(getApplicationContext(), "1374455732", false);
    }

    private void initData() {
        mApplicationContext = getApplicationContext();
    }

    // 获取ApplicationContext
    public static Context getContext() {
        return mApplicationContext;
    }

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new WaterDropHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }
}
