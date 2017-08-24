package com.example.administrator.cheshilishop.utils;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.bean.VersionInfo;
import com.example.administrator.cheshilishop.net.RestClient;
import com.example.administrator.cheshilishop.servers.UpdateService;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;


/***
 * 功能描述：app更新
 * 
 * @author 作者 gqi
 * @version 1.0.0
 */
public class UpgradeHelper {
	/*** 新版本 不需要升级 300 **/
	private static final int NOTUPGRADE = 300;

	/*** 可选择升级 200 **/
	private static final int OPTIONALUPGRADE = 200;

	/*** 更新失败500 **/
	private static final int FAILEDUPGRADE = 500;

	/*** 当前app版本 **/
	private static String currAppVerNo;

	/*** 最新app版本 */
	private static String latestAppVerNo;

	/** 升级提示语 **/
	private static String appUpgradeNote;

	/** 最新app下载URL **/
	private static String apkurl;

	//static MyDialog myDialog;

	private static VersionInfo versionInfo;

	public static void checkAppVersion(final Context mContext,final boolean ishome) {

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
							checkUpdate(mContext,ishome);
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
	private static boolean checkUpdate(Context mContext,boolean ishome) {

		if (Long.valueOf(versionInfo.AddTime) > Long
				.valueOf(CheShiLiShopApplication.AddTime)) {
			// 可更新
			apkurl ="http://api.cheshili.com.cn/Img/"+ versionInfo.AppFile;
			currAppVerNo = getAppVersionName(mContext);
			latestAppVerNo = versionInfo.Version;
			appUpgradeNote = versionInfo.Descri;
			showCustomDialog(mContext);
			return true;
		}else{
			if(!ishome){
				ToastUtils.show(mContext,"已是最新版本");
			}
			return false;
		}
	}

	public static void showCustomDialog(final Context context) {
		String latestVersion = null;
		String currentVersion = null;
		View view = LayoutInflater.from(context).inflate(R.layout.upgradedialog, null);
		final Dialog dialog = new Dialog(context, R.style.mydialogstyle);
		dialog.setContentView(view);
		WindowManager manager = ((Activity) context).getWindowManager();
		Display display = manager.getDefaultDisplay();
		LayoutParams params = dialog.getWindow().getAttributes();
		params.width = (int) (display.getWidth()*0.8 );
		params.height = (int) (display.getHeight()*0.6 );
		dialog.getWindow().setAttributes(params);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		TextView tv_update = (TextView) view.findViewById(R.id.tv_dialog_update);
		ImageView tv_tipDialog_title = (ImageView)view.findViewById(R.id.tv_tipDialog_title);
		TextView tv_cancel = (TextView) view.findViewById(R.id.tv_dialog_cancel);
		TextView tv_newversion = (TextView) view.findViewById(R.id.tv_newversion);
		TextView tv_currversion = (TextView) view.findViewById(R.id.tv_currversion);
		LinearLayout ll_versionnote = (LinearLayout) view.findViewById(R.id.ll_versionnote);
		currentVersion = currAppVerNo;
		latestVersion = latestAppVerNo;
		if (currentVersion == null || latestVersion == null) {
			Log.i("UPGRADE", "currentVersion==null||latestVersion==null");
			return;
		}
		//tv_versionnote.setText("版本特性："+appUpgradeNote);
		TextView tv_versionnote = new TextView(context);
		tv_versionnote.setTextSize(16);
		tv_versionnote.setPadding(80,0,0,0);
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)ll_versionnote.getLayoutParams();
		tv_versionnote.setLayoutParams(lp);
		tv_versionnote.setText(appUpgradeNote.replace("|","\n").trim());

		ll_versionnote.addView(tv_versionnote);
		tv_update.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i("UPGRADE", "click upgrade");
				dialog.dismiss();
				ToastUtils.show(context,"升级");
				startUpgradeApp(context);
			}
		});
		tv_cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i("UPGRADE", "click cancel");
				ToastUtils.show(context,"取消升级");
				dialog.dismiss();
			}
		});

		String newVerString = "v"+ latestVersion;
		tv_newversion.setText(newVerString);
		tv_currversion.setText("当前版本为:"+ currentVersion);

	}

	public static void startUpgradeApp(Context context) {
		if (NetworkUtil.isNetworkValidate(context)) {
			if (apkurl != null) {
				Intent intent = new Intent();
				intent.setClass(context, UpdateService.class);
				Log.i("UPGRADE", "apkurl=========" + apkurl);
				intent.putExtra("downloadurl", apkurl);
				context.startService(intent);
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
