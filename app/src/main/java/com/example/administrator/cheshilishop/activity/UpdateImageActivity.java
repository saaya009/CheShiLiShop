package com.example.administrator.cheshilishop.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.ReceiverCallNotAllowedException;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;
import com.example.administrator.cheshilishop.dialog.SelectImgPopupWindow;
import com.example.administrator.cheshilishop.net.RestClient;
import com.example.administrator.cheshilishop.photochoose.CropImageActivity;
import com.example.administrator.cheshilishop.utils.ToastUtils;
import com.example.administrator.cheshilishop.utils.UrlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 更新商家图片
 * 作者：Ayase on 2017/11/14 16:47
 * 邮箱：ayase@ayase.cn
 */

public class UpdateImageActivity extends BaseActivity {

    @BindView(R.id.img1)
    ImageView mImg1;
    @BindView(R.id.img_close1)
    ImageView mImgClose1;
    @BindView(R.id.layout_img1)
    RelativeLayout mLayoutImg1;
    @BindView(R.id.img3)
    ImageView mImg3;
    @BindView(R.id.img_close3)
    ImageView mImgClose3;
    @BindView(R.id.layout_img3)
    RelativeLayout mLayoutImg3;
    @BindView(R.id.img4)
    ImageView mImg4;
    @BindView(R.id.img_close4)
    ImageView mImgClose4;
    @BindView(R.id.layout_img4)
    RelativeLayout mLayoutImg4;
    @BindView(R.id.img5)
    ImageView mImg5;
    @BindView(R.id.img_close5)
    ImageView mImgClose5;
    @BindView(R.id.layout_img5)
    RelativeLayout mLayoutImg5;
    @BindView(R.id.img6)
    ImageView mImg6;
    @BindView(R.id.img_close6)
    ImageView mImgClose6;
    @BindView(R.id.layout_img6)
    RelativeLayout mLayoutImg6;
    @BindView(R.id.btn_save)
    Button mBtnSave;

    private int img;
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
    private File newFile;
    private Uri contentUri;

    private String path = "";
    private String path3 = "";
    private String path4 = "";
    private String path5 = "";
    private String path6 = "";

    private Map<Integer, String> map = new HashMap<Integer, String>();

    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_updateimg);
        ButterKnife.bind(this);
    }

    @Override
    protected BaseActivity getAct() {
        return this;
    }

    @Override
    protected TopView getTopViews() {
        return new TopView(topbar_tv_title, topbar_iv_back);
    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void setListener() {
        mLayoutImg1.setOnClickListener(this);
        mLayoutImg3.setOnClickListener(this);
        mLayoutImg4.setOnClickListener(this);
        mLayoutImg5.setOnClickListener(this);
        mLayoutImg6.setOnClickListener(this);
        mImgClose1.setOnClickListener(this);
        mImgClose3.setOnClickListener(this);
        mImgClose4.setOnClickListener(this);
        mImgClose5.setOnClickListener(this);
        mImgClose6.setOnClickListener(this);
        mBtnSave.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {

    }

    @Override
    protected void onClickEvent(View paramView) {
        switch (paramView.getId()) {
            case R.id.layout_img1:
                img = 1;
                selectImgPopupWindow = new SelectImgPopupWindow(context, itemOnClick);
                selectImgPopupWindow.showAtLocation(findViewById(R.id.layout_main),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.layout_img3:
                img = 3;
                selectImgPopupWindow = new SelectImgPopupWindow(context, itemOnClick);
                selectImgPopupWindow.showAtLocation(findViewById(R.id.layout_main),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.layout_img4:
                img = 4;
                selectImgPopupWindow = new SelectImgPopupWindow(context, itemOnClick);
                selectImgPopupWindow.showAtLocation(findViewById(R.id.layout_main),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.layout_img5:
                img = 5;
                selectImgPopupWindow = new SelectImgPopupWindow(context, itemOnClick);
                selectImgPopupWindow.showAtLocation(findViewById(R.id.layout_main),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.layout_img6:
                img = 6;
                selectImgPopupWindow = new SelectImgPopupWindow(context, itemOnClick);
                selectImgPopupWindow.showAtLocation(findViewById(R.id.layout_main),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.img_close1://删除图片
                mImg1.setVisibility(View.GONE);
                path = "";
                mImgClose1.setVisibility(View.GONE);
                mLayoutImg1.setEnabled(true);
                break;
            case R.id.img_close3://删除图片
                mImg3.setVisibility(View.GONE);
                path3 = "";
                mImgClose3.setVisibility(View.GONE);
                mLayoutImg3.setEnabled(true);
                break;
            case R.id.img_close4://删除图片
                mImg4.setVisibility(View.GONE);
                path4 = "";
                mImgClose4.setVisibility(View.GONE);
                mLayoutImg4.setEnabled(true);
                break;
            case R.id.img_close5://删除图片
                mImg5.setVisibility(View.GONE);
                path5 = "";
                mImgClose5.setVisibility(View.GONE);
                mLayoutImg5.setEnabled(true);
                break;
            case R.id.img_close6://删除图片
                mImg6.setVisibility(View.GONE);
                path = "";
                mImgClose6.setVisibility(View.GONE);
                mLayoutImg6.setEnabled(true);
                break;
            case R.id.btn_save:
                sava(img);
                break;
        }
    }

    //上传图片
    private void sava(final int img) {
        final RequestParams params = new RequestParams();
        try {
            params.put("Img[]", new File(path), "image/png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        params.add("WToken", CheShiLiShopApplication.wtoken);
        params.add("ID", CheShiLiShopApplication.storeID);
        RestClient.post(UrlUtils.addStoreImage(), params, UpdateImageActivity.this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    Log.d("上传图片", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if ("0".equals(Status)) {
                        JSONObject data = jsonObject.getJSONObject("Data");
                        JSONArray imgUrl = data.getJSONArray("ImgUrl");
                        map.put(img, imgUrl.getString(0));
                        commit(img, imgUrl.getString(0));
                    } else if ("-1".equals(Status)) {
                        Intent intent = new Intent(UpdateImageActivity.this, LoginActivity.class);
                        startActivity(intent);

                        finish();
                    } else {
                        ToastUtils.show(UpdateImageActivity.this, jsonObject.getString("Data"));
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
     * 提交图片
     */
    private void commit(int img,String imgUrl) {
        RequestParams params = new RequestParams();
        params.add("WToken", CheShiLiShopApplication.wtoken);
        params.add("ID", CheShiLiShopApplication.storeID);
        switch (img){
            case 1:
                params.add("Type","3");
                break;
            default:
                params.add("Type","1");
                break;
        }
        params.add("ImgUrl",imgUrl);
        RestClient.post(UrlUtils.updateStoreImg(), params, UpdateImageActivity.this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    Log.d("上传图片", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if ("0".equals(Status)) {
                        JSONObject data = jsonObject.getJSONObject("Data");

                    } else if ("-1".equals(Status)) {
                        Intent intent = new Intent(UpdateImageActivity.this, LoginActivity.class);
                        startActivity(intent);

                        finish();
                    } else {
                        ToastUtils.show(UpdateImageActivity.this, jsonObject.getString("Data"));
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
     * 打开相机获取图片
     */
    private void startCamera() {
        File imagePath = new File(Environment.getExternalStorageDirectory(), "images");
        if (!imagePath.exists()) imagePath.mkdirs();
        newFile = new File(imagePath, "shop_image.jpg");

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
        } else if (resultCode == RESULT_OK && requestCode == 1000) {
            Intent intent = new Intent(this, CropImageActivity.class);
            intent.putExtra("path", newFile.getAbsolutePath());
            startActivityForResult(intent, FLAG_MODIFY_FINISH);
        } else if (requestCode == FLAG_MODIFY_FINISH && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                ContentResolver contentProvider = getContentResolver();
                ParcelFileDescriptor mInputPFD;
                switch (img) {
                    case 1:
                        mImg1.setVisibility(View.VISIBLE);
                        mImg1.setImageURI(Uri.fromFile(new File(data.getStringExtra("path"))));
                        mLayoutImg1.setEnabled(false);
                        mImgClose1.setVisibility(View.VISIBLE);
                        path = data.getStringExtra("path");
                        break;
                    case 3:
                        mImg3.setVisibility(View.VISIBLE);
                        mImg3.setImageURI(Uri.fromFile(new File(data.getStringExtra("path"))));
                        mLayoutImg3.setEnabled(false);
                        mImgClose3.setVisibility(View.VISIBLE);
                        path3 = data.getStringExtra("path");
                        break;
                    case 4:
                        mImg4.setVisibility(View.VISIBLE);
                        mImg4.setImageURI(Uri.fromFile(new File(data.getStringExtra("path"))));
                        mLayoutImg4.setEnabled(false);
                        mImgClose4.setVisibility(View.VISIBLE);
                        path4 = data.getStringExtra("path");
                        break;
                    case 5:
                        mImg5.setVisibility(View.VISIBLE);
                        mImg5.setImageURI(Uri.fromFile(new File(data.getStringExtra("path"))));
                        mLayoutImg5.setEnabled(false);
                        mImgClose5.setVisibility(View.VISIBLE);
                        path5 = data.getStringExtra("path");
                        break;
                    case 6:
                        mImg6.setVisibility(View.VISIBLE);
                        mImg6.setImageURI(Uri.fromFile(new File(data.getStringExtra("path"))));
                        mLayoutImg6.setEnabled(false);
                        mImgClose6.setVisibility(View.VISIBLE);
                        path6 = data.getStringExtra("path");
                        break;
                }
            }
        }
    }

}
