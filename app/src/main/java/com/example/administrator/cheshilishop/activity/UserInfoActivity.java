package com.example.administrator.cheshilishop.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
import com.example.administrator.cheshilishop.adapter.BookingAllAdapter;
import com.example.administrator.cheshilishop.bean.BookingBean;
import com.example.administrator.cheshilishop.bean.UserInfoBean;
import com.example.administrator.cheshilishop.dialog.SelectImgPopupWindow;
import com.example.administrator.cheshilishop.net.RestClient;
import com.example.administrator.cheshilishop.photochoose.CropImageActivity;
import com.example.administrator.cheshilishop.utils.ToastUtils;
import com.example.administrator.cheshilishop.utils.UrlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
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
                Intent intent = new Intent(UserInfoActivity.this,GetPwdActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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
                    String status = Environment.getExternalStorageState();
                    if (status.equals(Environment.MEDIA_MOUNTED)) {
                        try {
                            localTempImageFileName = "";
                            localTempImageFileName = String.valueOf((new Date())
                                    .getTime()) + ".jpg";
                            File filePath = FILE_PIC_SCREENSHOT;
                            if (!filePath.exists()) {
                                filePath.mkdirs();
                            }
                            Intent intent = new Intent(
                                    MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(filePath, localTempImageFileName);
                            // localTempImgDir和localTempImageFileName是自己定义的名字
                            Uri u = Uri.fromFile(f);
                            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                            startActivityForResult(intent, FLAG_CHOOSE_PHONE);
                        } catch (ActivityNotFoundException e) {
                            //
                        }
                    }

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
        RequestParams params = new RequestParams();
        params.add("WToken", CheShiLiShopApplication.wtoken);
        RestClient.post(UrlUtils.getInfoByToken(), params, this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject json = new JSONObject(result);
                    String Status = json.getString("Status");
                    if ("0".equals(Status)) {
                        CheShiLiShopApplication.user = JSON.parseObject(json.getString("Data"), UserInfoBean.class);
                        Glide.with(UserInfoActivity.this)
                                .load(CheShiLiShopApplication.user.Img)
                                .into(mImgAvatar);
                        mTvUsername.setText(CheShiLiShopApplication.user.NickName);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FLAG_CHOOSE_IMG && resultCode == android.app.Activity.RESULT_OK) {
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
        } else if (requestCode == FLAG_CHOOSE_PHONE && resultCode == android.app.Activity.RESULT_OK) {
            File f = new File(FILE_PIC_SCREENSHOT, localTempImageFileName);
            Intent intent = new Intent(this, CropImageActivity.class);
            intent.putExtra("path", f.getAbsolutePath());
            startActivityForResult(intent, FLAG_MODIFY_FINISH);
        } else if (requestCode == FLAG_MODIFY_FINISH && resultCode == android.app.Activity.RESULT_OK) {
            if (data != null) {
                String path = data.getStringExtra("path");
                uploadPic(path);
            }
        }
    }

    /**
     * 上传头像
     * @param path
     */
    private void uploadPic(String path) {
        RequestParams params = new RequestParams();
        try {
            params.put("avatar", new File(path));
            Log.d("头像",path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        params.add("WToken",CheShiLiShopApplication.wtoken);
        params.add("Platform","3");
        RestClient.post(UrlUtils.UploaImg(), params, UserInfoActivity.this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    Log.d("刷新", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if ("0".equals(Status)){
                        getData();
                        ToastUtils.show(UserInfoActivity.this,"上传成功");
                    }else {
                        ToastUtils.show(UserInfoActivity.this,jsonObject.getString("Data"));
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
}