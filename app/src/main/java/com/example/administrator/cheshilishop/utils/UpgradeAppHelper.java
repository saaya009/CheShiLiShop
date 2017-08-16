package com.example.administrator.cheshilishop.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.bean.VersionInfo;
import com.example.administrator.cheshilishop.dialog.LoadingDialog;
import com.example.administrator.cheshilishop.net.RestClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * 功能描述：app更新
 */
public class UpgradeAppHelper {
    /***
     * 新版本 不需要升级 300
     **/
    private static final int NOTUPGRADE = 300;

    /***
     * 可选择升级 200
     **/
    private static final int OPTIONALUPGRADE = 200;

    /***
     * 更新失败500
     **/
    private static final int FAILEDUPGRADE = 500;

    /***
     * 当前app版本
     **/
    private static String currAppVerNo;

    /***
     * 最新app版本
     */
    private static String latestAppVerNo;

    /**
     * 升级提示语
     **/
    private static String appUpgradeNote;

    /**
     * 最新app下载URL
     **/
    private static String apkurl;

    static LoadingDialog myDialog;



    private static VersionInfo versionInfo;

    public static void checkAppVersion(final Context mContext) {

        RestClient.get(UrlUtils.queryAppNewVersion(), mContext, new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int Status = jsonObject.getInt("Status");
                    switch (Status) {
                        case 0:
                            String data = jsonObject.getString("Data");
                            versionInfo =  JSON.parseObject(data,VersionInfo.class);
                            checkUpdate(mContext);
                            break;
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private static boolean checkUpdate(Context mContext) {
        if (Long.valueOf(versionInfo.AddTime) > Long
                .valueOf(CheShiLiShopApplication.AddTime)) {
            // 可更新
            apkurl ="http://api.cheshili.com.cn/Img"+ versionInfo.AppFile;
//                apkurl = "http://api.cheshili.com.cn/Img/App/C-iSyz7aSxEoHiyr.apk";
            currAppVerNo = getAppVersionName(mContext);
            latestAppVerNo = versionInfo.Version;
            appUpgradeNote = versionInfo.Descri;
            showCustomDialog(mContext);
            return true;
        }
        return false;
    }

    public static void showCustomDialog(final Context context) {

        String latestVersion = null;
        String currentVersion = null;

        View view = LayoutInflater.from(context).inflate(
                R.layout.upgradedialog, null);
        final Dialog dialog = new Dialog(context, R.style.mydialogstyle);

        TextView tvUpgrade = (TextView) view
                .findViewById(R.id.upgrade_dialog_tv_confirm);
        TextView tvCancel = (TextView) view
                .findViewById(R.id.upgrade_dialog_tv_cancel);
        TextView tv_NewVer = (TextView) view.findViewById(R.id.newversion);
        TextView tv_CurrVer = (TextView) view.findViewById(R.id.currversion);
        TextView tv_VerNote = (TextView) view.findViewById(R.id.versionnote);

        currentVersion = currAppVerNo;
        latestVersion = latestAppVerNo;
        if (currentVersion == null || latestVersion == null) {
            Log.i("UPGRADE", "currentVersion==null||latestVersion==null");
            return;
        }
        tv_VerNote.setText(appUpgradeNote);
        tvUpgrade.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("UPGRADE", "click upgrade");
                dialog.dismiss();
                // 升级的代码
                startUpgradeApp(context);
            }
        });

        // tvCancel.setText("取消");
        tvCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("UPGRADE", "click cancel");
                dialog.dismiss();
            }
        });

        String newVerString = "发现新版本:" + latestVersion;
        tv_NewVer.setText(newVerString);
        tv_CurrVer.setText("当前版本为:" + currentVersion);

        dialog.show();
        dialog.setContentView(view);
        WindowManager manager = ((Activity) context).getWindowManager();
        Display display = manager.getDefaultDisplay();

        LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (display.getWidth() * 0.9);
        params.height = (int) (display.getHeight() * 0.4);
        dialog.getWindow().setAttributes(params);
    }



    public static void startUpgradeApp(Context context) {
        if (NetworkUtil.isNetworkValidate(context)) {
            if (apkurl != null) {
                Intent serviceIntent = new Intent(context, DownloadService.class);
                serviceIntent.setData(Uri.parse(apkurl));
                context.startService(serviceIntent);
                Log.d("startUpgradeApp","升级服务");
            }
        } else {
            ToastUtils.show(context, "当前网络不可用");
        }
    }

    /*
     * 获取本地app的versioncode,对应AndroidManifest.xml中
     * 的android:versionCode="2015020701"
     */
    public static String getAppVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packinfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packinfo.versionCode + "";
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getAppVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packinfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packinfo.versionName + "";
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
