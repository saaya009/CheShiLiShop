package com.example.administrator.cheshilishop.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;
import com.example.administrator.cheshilishop.bean.StoreBean;
import com.example.administrator.cheshilishop.dialog.LoadingDialog;
import com.example.administrator.cheshilishop.dialog.SelectImgPopupWindow;
import com.example.administrator.cheshilishop.net.RestClient;
import com.example.administrator.cheshilishop.photochoose.CropImageActivity;
import com.example.administrator.cheshilishop.utils.ToastUtils;
import com.example.administrator.cheshilishop.utils.UrlUtils;
import com.example.administrator.cheshilishop.widget.TimePickerView;
import com.lljjcoder.city_20170724.CityPickerView;
import com.lljjcoder.city_20170724.bean.CityBean;
import com.lljjcoder.city_20170724.bean.DistrictBean;
import com.lljjcoder.city_20170724.bean.ProvinceBean;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 作者：Ayase on 2017/8/29 13:55
 * 邮箱：ayase@ayase.cn
 * 填写商店信息
 */
public class WriteActivity extends BaseActivity {


    @BindView(R.id.tv_shopname)
    TextView mTvShopname;
    @BindView(R.id.et_shopname)
    EditText mEtShopname;
    @BindView(R.id.et_shortName)
    EditText mEtShortName;
    @BindView(R.id.tv_people)
    TextView mTvPeople;
    @BindView(R.id.et_chargeMan)
    EditText mEtChargeMan;
    @BindView(R.id.et_chargeTitle)
    EditText mEtChargeTitle;
    @BindView(R.id.tv_mobile)
    TextView mTvMobile;
    @BindView(R.id.et_mobile)
    EditText mEtMobile;
    @BindView(R.id.et_tel)
    EditText mEtTel;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_expired)
    TextView mTvExpired;
    @BindView(R.id.tv_type)
    TextView mTvType;
    @BindView(R.id.et_type)
    TextView mEtType;
    @BindView(R.id.tv_WDeviceType)
    TextView mTvWDeviceType;
    @BindView(R.id.et_wdeviceType)
    TextView mEtWdeviceType;
    @BindView(R.id.et_wdeviceNum)
    EditText mEtWdeviceNum;
    @BindView(R.id.et_wpostions)
    EditText mEtWpostions;
    @BindView(R.id.tv_city)
    TextView mTvCity;
    @BindView(R.id.layout_city)
    RelativeLayout mLayoutCity;
    @BindView(R.id.et_adress)
    EditText mEtAdress;
    @BindView(R.id.et_Longitude)
    EditText mEtLongitude;
    @BindView(R.id.et_Latitude)
    EditText mEtLatitude;
    @BindView(R.id.et_descri)
    EditText mEtDescri;
    @BindView(R.id.btn_commit)
    Button mBtnCommit;
    @BindView(R.id.layout_main)
    LinearLayout mLayoutMain;
    @BindView(R.id.img_close1)
    ImageView mImgClose1;
    @BindView(R.id.layout_img2)
    RelativeLayout mLayoutImg2;
    @BindView(R.id.img1)
    ImageView mImg1;
    @BindView(R.id.img2)
    ImageView mImg2;
    @BindView(R.id.layout_img1)
    RelativeLayout mLayoutImg1;
    @BindView(R.id.img_close2)
    ImageView mImgClose2;
    @BindView(R.id.et_yingyetime)
    TextView mEtYingyetime;
    @BindView(R.id.et_yingyemianji)
    EditText mEtYingyemianji;
    @BindView(R.id.et_bankname)
    EditText mEtBankname;
    @BindView(R.id.et_banknum)
    EditText mEtBanknum;
    @BindView(R.id.et_bank)
    EditText mEtBank;

    //营业期限
    private String expired;

    private String ProvinceID;
    private String CityID;
    private String CountyID;
    //店铺类型
    private String type = "";
    //洗车设备
    private String wdeviceType;

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
    private File newFile2;
    private Uri contentUri;
    private Uri contentUri2;
    private LoadingDialog mDialog;
    private String path = "";
    private String path2 = "";
    private int conut = 0;
    private TimePickerView pvTime;

    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_write);
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
        mBtnCommit.setOnClickListener(this);
        mTvExpired.setOnClickListener(this);
        mTvCity.setOnClickListener(this);
        mEtType.setOnClickListener(this);
        mEtWdeviceType.setOnClickListener(this);
        mLayoutImg1.setOnClickListener(this);
        mLayoutImg2.setOnClickListener(this);
        mImgClose1.setOnClickListener(this);
        mImgClose2.setOnClickListener(this);
        mEtYingyetime.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        setTopTitle("申请添加店面");

        // 时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        // 控制时间范围
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        // 时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                mTvTime.setText(getTime(date));
                expired = date.getTime() + "";
            }
        });
    }

    @Override
    protected void onClickEvent(View paramView) {
        switch (paramView.getId()) {
            case R.id.btn_commit://提交
                commit();
                break;
            case R.id.tv_expired://营业期限
                pvTime.show();
                break;
            case R.id.tv_city://选择所在地
                CityPickerView cityPicker = new CityPickerView.Builder(WriteActivity.this)
                        .textSize(20)
                        .title("    ")
                        .backgroundPop(0xa0000000)
                        .titleBackgroundColor("#ffffff")
                        .titleTextColor("#FFFFFF")
                        .backgroundPop(0xa0000000)
                        .confirTextColor("#3F51B5")
                        .cancelTextColor("#3F51B5")
                        .textColor(Color.parseColor("#000000"))
                        .provinceCyclic(true)
                        .cityCyclic(false)
                        .districtCyclic(false)
                        .visibleItemsCount(7)
                        .itemPadding(10)
                        .onlyShowProvinceAndCity(false)
                        .build();
                cityPicker.show();

                //监听方法，获取选择结果
                cityPicker.setOnCityItemClickListener(new CityPickerView.OnCityItemClickListener() {
                    @Override
                    public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {

                        if (!TextUtils.isEmpty(district.getId())) {
                            Log.d("选择地区", district.getId());
                            mTvCity.setText(province.getName() + city.getName() + district.getName());
                            ProvinceID = province.getId();
                            CityID = city.getId();
                            CountyID = district.getId();
                            CheShiLiShopApplication.address = province.getName() + city.getName() + district.getName();
                        }

                    }

                    @Override
                    public void onCancel() {

                    }
                });
                break;
            case R.id.et_type://店面类型
                final Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
                View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_type, null);
                bottomDialog.setContentView(contentView);
                ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
                layoutParams.width = getResources().getDisplayMetrics().widthPixels;
                contentView.setLayoutParams(layoutParams);
                contentView.findViewById(R.id.layout1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        type = "0";
                        mEtType.setText("旗舰店");
                        bottomDialog.dismiss();
                    }
                });
                contentView.findViewById(R.id.layout2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        type = "1";
                        mEtType.setText("综合店");
                        bottomDialog.dismiss();
                    }
                });
                contentView.findViewById(R.id.layout3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        type = "2";
                        mEtType.setText("专营店");
                        bottomDialog.dismiss();
                    }
                });
                bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
                bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
                bottomDialog.setCanceledOnTouchOutside(true);
                bottomDialog.show();
                break;
            case R.id.et_wdeviceType://洗车设备
                final Dialog bottomDialog2 = new Dialog(this, R.style.BottomDialog);
                View contentView2 = LayoutInflater.from(this).inflate(R.layout.dialog_wdevicetype, null);
                bottomDialog2.setContentView(contentView2);
                ViewGroup.LayoutParams layoutParams2 = contentView2.getLayoutParams();
                layoutParams2.width = getResources().getDisplayMetrics().widthPixels;
                contentView2.setLayoutParams(layoutParams2);
                contentView2.findViewById(R.id.layout1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        wdeviceType = "0";
                        mEtWdeviceType.setText("手工");
                        bottomDialog2.dismiss();
                    }
                });
                contentView2.findViewById(R.id.layout2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        wdeviceType = "1";
                        mEtWdeviceType.setText("机器");
                        bottomDialog2.dismiss();
                    }
                });
                bottomDialog2.getWindow().setGravity(Gravity.BOTTOM);
                bottomDialog2.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
                bottomDialog2.setCanceledOnTouchOutside(true);
                bottomDialog2.show();
                break;
            case R.id.layout_img1://门面照片
                img = 1;
                selectImgPopupWindow = new SelectImgPopupWindow(context, itemOnClick);
                selectImgPopupWindow.showAtLocation(findViewById(R.id.layout_main),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.layout_img2://营业执照
                img = 2;
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
            case R.id.img_close2://删除图片
                mImg2.setVisibility(View.GONE);
                path2 = "";
                mImgClose1.setVisibility(View.GONE);
                mLayoutImg1.setEnabled(true);
                break;
            case R.id.et_yingyetime://选择营业时间
                break;
        }
    }

    /**
     * 提交商店信息
     */
    private void commit() {

        final RequestParams params = new RequestParams();
        params.add("WToken", CheShiLiShopApplication.wtoken);
        String name = mEtShopname.getText().toString().trim();
        if (name.length() == 0) {
            ToastUtils.show(this, "店铺名不能为空");
            return;
        }
        params.add("Name", name);
        params.add("ShortName", mEtShortName.getText().toString().trim());
        String chargeMan = mEtChargeMan.getText().toString().trim();
        if (chargeMan.length() == 0) {
            ToastUtils.show(this, "责任人不能为空");
            return;
        }
        params.add("ChargeMan", chargeMan);
        params.add("ChargeTitle", mEtChargeTitle.getText().toString().trim());
        final String phone = mEtMobile.getText().toString().trim();
        if (phone.length() != 11) {
            ToastUtils.show(this, "手机号码不正确");
            return;
        }
        String bankName = mEtBankname.getText().toString().trim();
        String bankNo = mEtBanknum.getText().toString().trim();
        String bankCompany = mEtBank.getText().toString().trim();

        if (bankName.length() == 0) {
            ToastUtils.show(this, "银行开户名不能为空");
            return;
        }
        if (bankNo.length() == 0) {
            ToastUtils.show(this, "银行账号不能为空");
            return;
        }
        if (bankCompany.length() == 0) {
            ToastUtils.show(this, "开户银行不能为空");
            return;
        }
        if (path.length() == 0) {
            ToastUtils.show(this, "请上传店铺图片");
            return;
        }
        if (path2.length() == 0) {
            ToastUtils.show(this, "请上传店铺图片");
            return;
        }

        params.add("Phone", phone);
        params.add("Tel", mEtTel.getText().toString().trim());
        params.add("Expired", expired);
        params.add("Type", type);
        params.add("WDeviceType", wdeviceType);
        params.add("WDeviceNum", mEtWdeviceNum.getText().toString().trim());
        params.add("WPostions", mEtWpostions.getText().toString().trim());
        params.add("ProvinceID", ProvinceID);
        params.add("CityID", CityID);
        params.add("CountyID", CountyID);
        params.add("BankName", bankName);
        params.add("BankNo", bankNo);
        params.add("BankCompany", bankCompany);
        params.add("Address", mEtAdress.getText().toString().trim());
        params.add("Longitude", mEtLongitude.getText().toString().trim());
        params.add("Latitude", mEtLatitude.getText().toString().trim());
        if (mEtDescri.getText().toString().trim().length() > 0) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("text1", mEtDescri.getText().toString().trim());
            String jsonString = JSON.toJSONString(map);
            params.add("Descri", jsonString);
        } else {
            params.add("Descri", mEtDescri.getText().toString().trim());
        }

        params.add("OpenTime", mEtYingyetime.getText().toString().trim());
        params.add("OpenArea", mEtYingyemianji.getText().toString().trim());
        RestClient.post(UrlUtils.addStore(), params, this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    Log.d("添加店铺", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if ("0".equals(Status)) {
                        String data = jsonObject.getString("Data");
                        CheShiLiShopApplication.newStore = JSON.parseObject(data, StoreBean.class);
                        mDialog = new LoadingDialog(WriteActivity.this);
                        mDialog.show();
                        uploadPic(path);
                        uploadPic(path2);
                    } else if ("-1".equals(Status)) {
                        Intent intent = new Intent(WriteActivity.this, LoginActivity.class);
                        startActivity(intent);

                        finish();
                    } else {
                        ToastUtils.show(WriteActivity.this, jsonObject.getString("Data"));
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
                errParams.add("Url", UrlUtils.queryServiceAppointDetail());
                errParams.add("PostData", params.toString());
                errParams.add("WToken", CheShiLiShopApplication.wtoken);
                RestClient.post(UrlUtils.insertErrLog(), errParams, WriteActivity.this, new AsyncHttpResponseHandler() {
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
                    if (img == 1) {
                        startCamera();
                    } else {
                        startCamera2();
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

    /**
     * 打开相机获取图片
     */
    private void startCamera2() {
        File imagePath = new File(Environment.getExternalStorageDirectory(), "images");
        if (!imagePath.exists()) imagePath.mkdirs();
        newFile2 = new File(imagePath, "shop2_image.jpg");

        //第二参数是在manifest.xml定义 provider的authorities属性
        contentUri2 = FileProvider.getUriForFile(this, "com.example.administrator.cheshilishop.fileprovider", newFile2);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //兼容版本处理，因为 intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION) 只在5.0以上的版本有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ClipData clip =
                    ClipData.newUri(getContentResolver(), "b photo", contentUri2);
            intent.setClipData(clip);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            List<ResolveInfo> resInfoList =
                    getPackageManager()
                            .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                grantUriPermission(packageName, contentUri2,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri2);
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
            if (img == 1) {
                intent.putExtra("path", newFile.getAbsolutePath());
                startActivityForResult(intent, FLAG_MODIFY_FINISH);
            } else {
                intent.putExtra("path", newFile2.getAbsolutePath());
                startActivityForResult(intent, FLAG_MODIFY_FINISH);
            }

        } else if (requestCode == FLAG_MODIFY_FINISH && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                ContentResolver contentProvider = getContentResolver();
                ParcelFileDescriptor mInputPFD;
                if (img == 1) {
                    mImg1.setVisibility(View.VISIBLE);
                    mImg1.setImageURI(Uri.fromFile(new File(data.getStringExtra("path"))));
                    mLayoutImg1.setEnabled(false);
                    mImgClose1.setVisibility(View.VISIBLE);
                    path = data.getStringExtra("path");
                } else {
                    mImg1.setVisibility(View.VISIBLE);
                    mImg2.setImageURI(Uri.fromFile(new File(data.getStringExtra("path"))));
                    mLayoutImg2.setEnabled(false);
                    mImgClose2.setVisibility(View.VISIBLE);
                    path2 = data.getStringExtra("path");
                }

            }
        }
    }


    /**
     * 上传图片
     *
     * @param path
     */
    private void uploadPic(final String path) {

        final RequestParams params = new RequestParams();
        try {
            params.put("Img[]", new File(path), "image/png");
            Log.d("商家图片", path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        params.add("WToken", CheShiLiShopApplication.wtoken);
        params.add("ID", CheShiLiShopApplication.newStore.ID);
        RestClient.post(UrlUtils.addStoreImage(), params, WriteActivity.this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                conut++;
                if (conut == 2) {
                    mDialog.dismiss();
                    Intent intent = new Intent(WriteActivity.this, ExamineActivity.class);
                    startActivity(intent);
                    finish();
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
                errParams.add("Url", UrlUtils.queryServiceAppointDetail());
                errParams.add("PostData", params.toString());
                errParams.add("WToken", CheShiLiShopApplication.wtoken);
                RestClient.post(UrlUtils.insertErrLog(), errParams, WriteActivity.this, new AsyncHttpResponseHandler() {
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

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(date);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
