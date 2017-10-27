package com.example.administrator.cheshilishop.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;
import com.example.administrator.cheshilishop.bean.UserInfoBean;
import com.example.administrator.cheshilishop.dialog.LoadingDialog;
import com.example.administrator.cheshilishop.dialog.SelectImgPopupWindow;
import com.example.administrator.cheshilishop.net.RestClient;
import com.example.administrator.cheshilishop.photochoose.CropImageActivity;
import com.example.administrator.cheshilishop.utils.ToastUtils;
import com.example.administrator.cheshilishop.utils.UpgradeHelper;
import com.example.administrator.cheshilishop.utils.UrlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orhanobut.hawk.Hawk;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 用户资料
 * Created by Administrator on 2017/8/3.
 */
public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.topbar_iv_back)
    ImageView mTopbarIvBack;
    @BindView(R.id.topbar_tv_title)
    TextView mTopbarTvTitle;
    @BindView(R.id.topbar_iv_right)
    TextView mTopbarIvRight;
    @BindView(R.id.top_view)
    RelativeLayout mTopView;
    @BindView(R.id.img_right)
    ImageView mImgRight;
    @BindView(R.id.img_avatar)
    CircleImageView mImgAvatar;
    @BindView(R.id.tv_username)
    TextView mTvUsername;
    @BindView(R.id.layout_img)
    RelativeLayout mLayoutImg;
    @BindView(R.id.layout_changepassword)
    RelativeLayout mLayoutChangepassword;
    @BindView(R.id.layout_version)
    RelativeLayout mLayoutVersion;
    @BindView(R.id.layout_userinfo)
    LinearLayout mLayoutUserinfo;
    @BindView(R.id.btn_exit)
    Button mBtnExit;
    @BindView(R.id.layout_mywallet)
    RelativeLayout mLayoutMywallet;
    @BindView(R.id.layout_push)
    RelativeLayout mLayoutPush;
    @BindView(R.id.tv_realname)
    TextView mTvRealname;
    @BindView(R.id.layout_name)
    RelativeLayout mLayoutName;

    private EditText et_name;

    private SelectImgPopupWindow selectImgPopupWindow;
    private static String localTempImageFileName = "";
    public static final String IMAGE_PATH = "cheshilishop";
    private static final int FLAG_CHOOSE_IMG = 5;
    private static final int FLAG_CHOOSE_PHONE = 6;
    private static final int FLAG_MODIFY_FINISH = 7;
    public static final File FILE_SDCARD = Environment
            .getExternalStorageDirectory();
    public static final File FILE_LOCAL = new File(FILE_SDCARD, IMAGE_PATH);
    public static final File FILE_PIC_SCREENSHOT = new File(FILE_LOCAL,
            "images/screenshots");

    private MyReceiver receiver = new MyReceiver();
    private File newFile;
    private Uri contentUri;
    private LoadingDialog mDialog;

    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_userinfo);
        ButterKnife.bind(this);
    }

    @Override
    protected BaseActivity getAct() {
        return this;
    }

    @Override
    protected TopView getTopViews() {
        return new TopView(topbar_iv_back, topbar_tv_title);
    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void setListener() {
        mLayoutImg.setOnClickListener(this);
        mLayoutChangepassword.setOnClickListener(this);
        mBtnExit.setOnClickListener(this);
        mLayoutUserinfo.setOnClickListener(this);
        mLayoutVersion.setOnClickListener(this);
        mLayoutMywallet.setOnClickListener(this);
        mLayoutPush.setOnClickListener(this);
        mLayoutName.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        setTopTitle("账户信息");
        getData();
    }

    @Override
    protected void onClickEvent(View paramView) {
        switch (paramView.getId()) {
            case R.id.layout_img://修改头像
                selectImgPopupWindow = new SelectImgPopupWindow(context, itemOnClick);
                selectImgPopupWindow.showAtLocation(findViewById(R.id.layout_userinfo),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.layout_changepassword://修改密码
                Intent intent = new Intent(UserInfoActivity.this, GetPwdActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_exit://退出
                CheShiLiShopApplication.storeID = "";
                CheShiLiShopApplication.wtoken = "";
                CheShiLiShopApplication.user = null;
                CheShiLiShopApplication.store = null;
                Hawk.delete("wtoken");
                intent = new Intent(UserInfoActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.layout_version://版本更新
                UpgradeHelper.checkAppVersion(UserInfoActivity.this, false);
                break;
            case R.id.layout_mywallet://我的钱包
                intent = new Intent(UserInfoActivity.this, MyWalletActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_push://清空缓存
                push();
                break;
            case R.id.layout_name://修改姓名
                intent = new Intent(UserInfoActivity.this, SetNameActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 清空缓存
     */
    private void push() {
        RequestParams params = new RequestParams();
        params.add("WToken", CheShiLiShopApplication.wtoken);
        RestClient.post(UrlUtils.flushRedisByLeagueID(), params, this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    Log.d("清空缓存", result);
                    JSONObject json = new JSONObject(result);
                    String Status = json.getString("Status");
                    if ("0".equals(Status)) {
                        ToastUtils.show(UserInfoActivity.this, "已清空");
                    } else if ("-1".equals(Status)) {
                        Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.show(UserInfoActivity.this, json.getString("Data"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    /**
     * 选择图片
     */
    private View.OnClickListener itemOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            selectImgPopupWindow.dismiss();
            switch (v.getId()) {
                //拍照
                case R.id.btn_photograph:
                    startCamera();
                    break;
                //图库选择图片
                case R.id.btn_pickphoto:
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, FLAG_CHOOSE_IMG);
                    break;
            }
        }
    };

    /**
     * 获取数据
     */
    public void getData() {
        mDialog = new LoadingDialog(this);
        mDialog.show();
        final RequestParams params = new RequestParams();
        params.add("WToken", CheShiLiShopApplication.wtoken);
        RestClient.post(UrlUtils.getInfoByToken(), params, this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                mDialog.dismiss();
                try {
                    Log.d("个人信息", result);
                    JSONObject json = new JSONObject(result);
                    String Status = json.getString("Status");
                    if ("0".equals(Status)) {
                        CheShiLiShopApplication.user = JSON.parseObject(json.getString("Data"), UserInfoBean.class);
                        if (!TextUtils.isEmpty(CheShiLiShopApplication.user.Img)) {
                            Glide.with(UserInfoActivity.this)
                                    .load(UrlUtils.BASE_URL + "/Img/" + CheShiLiShopApplication.user.Img)
                                    .into(mImgAvatar);
                        }
                        mTvRealname.setText(CheShiLiShopApplication.user.RealName);
                        mTvUsername.setText(CheShiLiShopApplication.user.Mobile);
                    } else if ("-1".equals(Status)) {
                        Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.show(UserInfoActivity.this, json.getString("Data"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                RequestParams errParams = new RequestParams();
                try {
                    errParams.add("LogCont", URLEncoder.encode(new String(responseBody), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                errParams.add("Url", UrlUtils.getInfoByToken());
                errParams.add("PostData", params.toString());
                errParams.add("WToken", CheShiLiShopApplication.wtoken);
                RestClient.post(UrlUtils.insertErrLog(), errParams, UserInfoActivity.this, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FLAG_CHOOSE_IMG && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                if (!TextUtils.isEmpty(uri.getAuthority())) {
                    Cursor cursor = getContentResolver().query(uri,
                            new String[]{MediaStore.Images.Media.DATA},
                            null, null, null);
                    if (null == cursor) {
                        Toast.makeText(this, "图片没找到", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    cursor.moveToFirst();
                    String path = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));
                    cursor.close();
                    Intent intent = new Intent(this, CropImageActivity.class);
                    intent.putExtra("path", path);
                    startActivityForResult(intent, FLAG_MODIFY_FINISH);
                } else {
                    Intent intent = new Intent(this, CropImageActivity.class);
                    intent.putExtra("path", uri.getPath());
                    startActivityForResult(intent, FLAG_MODIFY_FINISH);
                }
            }
        } else if (requestCode == FLAG_CHOOSE_PHONE && resultCode == Activity.RESULT_OK) {
            File f = new File(FILE_PIC_SCREENSHOT, localTempImageFileName);
            Intent intent = new Intent(this, CropImageActivity.class);
            intent.putExtra("path", f.getAbsolutePath());
            startActivityForResult(intent, FLAG_MODIFY_FINISH);
        } else if (requestCode == FLAG_MODIFY_FINISH && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String path = data.getStringExtra("path");
                uploadPic(path);
            }
        }
        if (resultCode == RESULT_OK && requestCode == 1000) {
            Intent intent = new Intent(this, CropImageActivity.class);
            intent.putExtra("path", newFile.getAbsolutePath());
            startActivityForResult(intent, FLAG_MODIFY_FINISH);
        }
    }

    /**
     * 上传头像
     *
     * @param path
     */
    private void uploadPic(String path) {
        mDialog = new LoadingDialog(this);
        mDialog.show();
        final RequestParams params = new RequestParams();
        try {
            params.put("Img", new File(path), "image/png");
            Log.d("头像", path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        params.add("WToken", CheShiLiShopApplication.wtoken);
        params.add("Platform", "0");
        RestClient.post(UrlUtils.UploaImg(), params, UserInfoActivity.this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                mDialog.dismiss();
                try {
                    Log.d("刷新", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if ("0".equals(Status)) {
                        getData();
                        ToastUtils.show(UserInfoActivity.this, "上传成功");
                    } else {
                        ToastUtils.show(UserInfoActivity.this, jsonObject.getString("Data"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                RequestParams errParams = new RequestParams();
                try {
                    errParams.add("LogCont", URLEncoder.encode(new String(responseBody), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                errParams.add("Url", UrlUtils.UploaImg());
                errParams.add("PostData", params.toString());
                errParams.add("WToken", CheShiLiShopApplication.wtoken);
                RestClient.post(UrlUtils.insertErrLog(), errParams, UserInfoActivity.this, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
            }
        });
    }


    private static class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, "com.example.administrator.cheshilishop.fileprovider", new File(Environment.getExternalStorageDirectory() + "/myApp.apk"));
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.parse(Environment.getExternalStorageDirectory() + "/myApp.apk"),
                        "application/vnd.android.package-archive");
            }
            context.startActivity(intent);

        }
    }

    /**
     * 打开相机获取图片
     */
    private void startCamera() {
        File imagePath = new File(Environment.getExternalStorageDirectory(), "images");
        if (!imagePath.exists()) imagePath.mkdirs();
        newFile = new File(imagePath, "head_image.jpg");

        //第二参数是在manifest.xml定义 provider的authorities属性
        contentUri = FileProvider.getUriForFile(this, "com.example.administrator.cheshilishop.fileprovider", newFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //兼容版本处理，因为 intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION) 只在5.0以上的版本有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ClipData clip =
                    ClipData.newUri(getContentResolver(), "A photo", contentUri);
            intent.setClipData(clip);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            List<ResolveInfo> resInfoList =
                    getPackageManager()
                            .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                grantUriPermission(packageName, contentUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        startActivityForResult(intent, 1000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        getData();
        super.onResume();
    }
}
