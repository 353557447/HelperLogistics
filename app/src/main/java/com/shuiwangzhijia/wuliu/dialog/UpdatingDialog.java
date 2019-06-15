package com.shuiwangzhijia.wuliu.dialog;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.bean.UpdateInfo;
import com.shuiwangzhijia.wuliu.http.DownloadListener;
import com.shuiwangzhijia.wuliu.http.DownloadUtil;
import com.shuiwangzhijia.wuliu.utils.CommonUtils;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 版本更新提示
 * Created by wangsuli on 2017/5/23.
 */
public class UpdatingDialog extends Dialog  {

    static final String TAG = UpdatingDialog.class.getSimpleName();
    private final int isUpdate;//1 建议更新 2 强制更新
    private final int downloadType;
    private String fileName;
    private final boolean autoUpdate;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.hint)
    TextView hint;
    @BindView(R.id.updateBtn)
    Button updateBtn;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.progressTv)
    TextView progressTv;
    @BindView(R.id.llProgress)
    LinearLayout llProgress;
    @BindView(R.id.cancelBtn)
    TextView cancelBtn;
    private String updateUrl;
    private Context mContext;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                case -1:
                    dismiss();
                    break;
                case 0:
                    llProgress.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    progressTv.setText(msg.arg2 + "%");
                    llProgress.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    CommonUtils.setAppFile(fileName);
                    install(mContext, (File) msg.obj);
                    dismiss();
                    break;
            }
        }
    };

    /**
     * 构造器
     *
     * @param context    上下文
     * @param updateInfo 更新信息
     * @param isUpdate
     */
    public UpdatingDialog(Context context, final UpdateInfo updateInfo, int isUpdate, boolean autoUpdate,int type) {
        super(context, R.style.MyDialogStyle);
        this.mContext = context;
        this.isUpdate = isUpdate;
        this.autoUpdate = autoUpdate;
        this.downloadType = type;
        setContentView(LayoutInflater.from(context).inflate(R.layout.dialog_updating, null));
        ButterKnife.bind(this);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        setCancelable(true);
        this.setCanceledOnTouchOutside(true);

        setCancelable(false);
        if (isUpdate == 2) {
            //强制更新
            cancelBtn.setVisibility(View.GONE);
            updateBtn.setText("立即更新");
        } else if (isUpdate == 1) {
            //建议更新
            updateBtn.setText("建议更新");
        }
        title.setText(autoUpdate ? "更新通知" : "水的快递水店 " + updateInfo.getVersion());
        hint.setText(updateInfo.getContent());
        updateUrl = updateInfo.getV_url();
        fileName = getApkFileName(updateUrl, "");
    }


    /**
     * 工具方法用来根据URL和版本号拿到要下载的文件名
     *
     * @param url
     * @param versionName
     * @return
     */
    public static String getApkFileName(String url, String versionName) {
        // 本地保存的文件名为，URL最后部分加上版本号
        String fileName = url.substring(url.lastIndexOf("/") + 1);

        int subindex = fileName.indexOf(".apk");
        if (subindex > -1) {
            fileName = fileName.substring(0, subindex);
        }
        return fileName + versionName + ".apk";
    }


    /**
     * 安装 app
     *
     * @param context
     * @param file
     */
    public static void install(Context context, File file) {
        String[] command = {"chmod", "777", file.getPath()};
        ProcessBuilder builder = new ProcessBuilder(command);
        try {
            builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context, "com.funengda.driver.fileprovider", file);//在AndroidManifest中的android:authorities值
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
        } else {
            uri = Uri.fromFile(file);
        }

        install.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(install);
    }

    /**
     * 开始更新
     */
    public void startUpdate() {
        llProgress.setVisibility(View.VISIBLE);
        DownloadUtil.downloadFile(updateUrl, fileName, new DownloadListener() {
            @Override
            public void onStart() {
                Log.e(TAG, "onStart: ");
                setMsg(0, 0, null);


            }

            @Override
            public void onProgress(int currentLength) {
//                float value = new BigDecimal(currentLength * 100).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
//                progressTv.setText(currentLength + "%");
                setMsg(1, currentLength, null);
            }

            @Override
            public void onFinish(File file) {
                setMsg(2, 0, file);
            }

            @Override
            public void onFailure() {
                setMsg(-1, 0, null);

            }
        });

    }

    @OnClick({R.id.updateBtn, R.id.cancelBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancelBtn:
                dismiss();
                break;
            case R.id.updateBtn:
                if (downloadType == 0){
                    launchAppDetail("com.funengda.driver","com.wandoujia.phoenix2");
                }else if (downloadType == 1){
                    updateBtn.setVisibility(View.GONE);
                    CommonUtils.deleteOldUpdateFile(CommonUtils.getAppFile());
                    startUpdate();
                }
                break;
        }
    }

    public void launchAppDetail(String appPkg, String marketPkg) {
        try {
            if (TextUtils.isEmpty(appPkg)) return;
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg)) {
                intent.setPackage(marketPkg);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            //showToast("检查到您手机没有安装豌豆荚，请安装后使用该功能");
            //https://www.wandoujia.com/apps/com.jdtx.shop.myshopfnd
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse("https://www.wandoujia.com/apps/com.funengda.driver");
            intent.setData(content_url);
            mContext.startActivity(intent);
        }
    }

    private void setMsg(int state, int progress, File file) {
        Message msg = new Message();
        msg.arg1 = state;
        msg.arg2 = progress;
        msg.obj = file;
        mHandler.sendMessage(msg);
    }


    @Override
    public void show() {
        super.show();
        this.getWindow().setWindowAnimations(R.style.DialogOutAndInStyle);
    }
}
