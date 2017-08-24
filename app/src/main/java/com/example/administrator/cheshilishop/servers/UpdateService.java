package com.example.administrator.cheshilishop.servers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.text.format.Time;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.administrator.cheshilishop.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/***
 * 功能描述：状态栏软件后台更新service
 * @author 作者 gqi
 * @version 1.0.0
 */
public class UpdateService extends Service {

    private NotificationManager notificationManager;

    private Notification notification;

    private String apkFileName;

    private int length;// 下载的文件大小

    private String savePath;

    private String apkDownloadUrl;

    private int progress;

    /** 更新完成 ***/
    private static final int DOWN_UPDATING = 1;

    /*** 正在更新 */
    private static final int DOWN_COMPLETE = 2;

    /** 更新失败 */
    private static final int DOWN_FAILED = 3;

    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case DOWN_UPDATING:// 下载中
                if (progress < 100) {
                    notification.contentView.setProgressBar(R.id.download_pb, 100, progress, false);
                    notification.contentView.setTextViewText(R.id.download_tv, getResources().getString(R.string.app_name)+"升级中"+progress+"%");
                    notification.flags |= Notification.FLAG_AUTO_CANCEL;
                    PendingIntent pendingIntent = PendingIntent.getActivity(UpdateService.this, 0, new Intent(), 0);
                    notification.contentIntent = pendingIntent;
                    notificationManager.notify(0, notification);
                }
                break;
            case DOWN_COMPLETE:// 更新完成
                Log.v("UPDATE", "下载完成，准备安装");
                notification.contentView.setProgressBar(R.id.download_pb, 100, 100, false);
                notification.contentView.setTextViewText(R.id.download_tv, "下载完成");
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                // notificationManager.cancelAll();
                notificationManager.notify(0, notification);
                // 准备安装
//                Intent intent = new Intent();
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.fromFile(new File(savePath)), "application/vnd.android.package-archive");
//                // PendingIntent pendingIntent = PendingIntent.getActivity(UpdateService.this,0,intent,0);
//                // notification.contentIntent = pendingIntent;
//                // notificationManager.notify(0,notification);
//                startActivity(intent)
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(UpdateService.this, "com.example.administrator.cheshilishop.fileprovider", new File(savePath));
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                } else {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(Uri.fromFile(new File(savePath)), "application/vnd.android.package-archive");
                }
                startActivity(intent);

                stopSelf();
                break;
            case DOWN_FAILED:// 更新失败
                Log.v("UPDATE", "下载失败");
                notification.contentView.setTextViewText(R.id.download_tv, "下载失败");
                notification.flags |= Notification.FLAG_AUTO_CANCEL;// 点击通知，通知消失
                PendingIntent pendingIntents = PendingIntent.getActivity(UpdateService.this, 0, new Intent(), 0);
                notification.contentIntent = pendingIntents;
                notificationManager.notify(0, notification);
                stopSelf();
                break;
            default:
                stopSelf();
                break;
            }
        }

    };

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        apkDownloadUrl = intent.getStringExtra("downloadurl");
        Log.v("UPDATE", "Service downloadurl:" + apkDownloadUrl);
        initNotify();
        reqDownloadApk(apkDownloadUrl);
        return Service.START_REDELIVER_INTENT;
        /**
         * 从Android官方文档中，我们知道onStartCommand有4种返回值： (1)START_STICKY：如果service进程被kill掉，保留service的状态为开始状态，但不保留递送的intent对象。随后系统会尝试重新创建service，
         * 由于服务状态为开始状态，所以创建服务后一定会调用onStartCommand(Intent,int,int)方法。如果在此期间没有任何启动命令被传递到service， 那么参数Intent将为null。
         * (2)START_NOT_STICKY：“非粘性的”。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统不会自动重启该服务。
         * (3)START_REDELIVER_INTENT：重传Intent。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统会自动重启该服务，并将Intent的值传入。
         * (4)START_STICKY_COMPATIBILITY：START_STICKY的兼容版本，但不保证服务被kill后一定能重启。
         */
    }

    private void initNotify() {

        notification = new Notification();
        // 初始化状态栏上的通知信息
        notification.icon = R.mipmap.ic_launcher;
        notification.tickerText = getResources().getString(R.string.app_name)+"升级中";
        notification.when = System.currentTimeMillis();
        // 不能手动清理
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // 初始化自定义进度条
        RemoteViews view = new RemoteViews(this.getPackageName(), R.layout.download_progress);
        view.setImageViewResource(R.id.download_iv,R.mipmap.ic_launcher);

        notification.contentView = view;
        view.setProgressBar(R.id.download_pb, 100, 0, false);

    }

    private void reqDownloadApk(String apkurl) {
        if (null == apkurl) {
            Log.v("UPDATE", "apkurl == null");
            return;
        }
        ReqDownloadApkTask downloadApkTask = new ReqDownloadApkTask();
        downloadApkTask.execute(apkurl);
    }

    private class ReqDownloadApkTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            URL apkUrl = null;
            HttpURLConnection conn = null;
            try {
                apkFileName = getFileName(params[0]);
                Log.v("UPDATE", "download apkfile url request:" + params[0]);
                apkUrl = new URL(params[0]);
                conn = (HttpURLConnection) apkUrl.openConnection();
                conn.setConnectTimeout(5 * 1000);
                conn.setRequestMethod("GET");
                length = conn.getContentLength();
                Log.v("UPDATE", "download apkfile getResponseCode=" + conn.getResponseCode());
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inStream = conn.getInputStream();
                    String sdPath = getSDPath();
                    if (sdPath == null) {
                        Toast.makeText(UpdateService.this, "SD卡不存在或者存储空间不足", Toast.LENGTH_SHORT).show();
                        return null;
                    }
                    savePath = sdPath + File.separator + apkFileName;

                    Log.v("UPDATE", "apkfile savePath=" + savePath);
                    FileOutputStream fos = new FileOutputStream(savePath);

                    int count = 0;
                    byte buf[] = new byte[1024];
                    int times = 0;
                    int numread = 0;
                    int start = 0;
                    while ((numread = inStream.read(buf)) > 0) {
                        if (start==0) {
                            progress = (int) (((float) count / length) * 100);
                            Log.v("UPDATE", "progress---" + progress);
                            myHandler.sendEmptyMessage(DOWN_UPDATING);
                            start = 1;
                        }
                        fos.write(buf, 0, numread);
                        count += numread;
                        if (count == length) {
                            myHandler.sendEmptyMessage(DOWN_COMPLETE);
                            break;
                        }
                        
                        if (times == 512) {
                            progress = (int) (((float) count / length) * 100);
                            Log.v("UPDATE", "progress====" + progress);
                            myHandler.sendEmptyMessage(DOWN_UPDATING);
                            times = 0;
                            continue;
                        }
                        times++;
                    }

                    fos.close();
                    inStream.close();
                }

            } catch (MalformedURLException e) {
                myHandler.sendEmptyMessage(DOWN_FAILED);
                e.printStackTrace();
            } catch (IOException e) {
                myHandler.sendEmptyMessage(DOWN_FAILED);
                e.printStackTrace();
            }

            return null;
        }
    }

    /**
     * @param path url路径
     * @return 得到文件名
     */
    private String getFileName(String path) {
        int index = path.lastIndexOf("/") + 1;
        return path.substring(index, path.length());
    }

    /*
     * 获取sd卡的路径 格式如下： /storage/sdcard0
     */
    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        return sdDir.toString();
    }

    /**
     * @return 获取当前日期，用于定义文件夹
     */
    public static String getCurDate() {
        Time localTime = new Time("Asia/Beijing");
        localTime.setToNow();
        return localTime.format("%Y-%m-%d");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
