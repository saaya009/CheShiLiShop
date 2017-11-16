package com.example.administrator.cheshilishop.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
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
import android.support.v7.app.AlertDialog;
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
import com.bumptech.glide.Glide;
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
import com.example.administrator.cheshilishop.widget.TimePickerView2;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.citywheel.CityPickerView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
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
 * 作者：Ayase on 2017/11/14
 * 邮箱：ayase@ayase.cn
 * 修改商店信息
 */
public class ChangeStoreActivity extends BaseActivity {

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
    @BindView(R.id.et_yingyemianji)
    EditText mEtYingyemianji;
    @BindView(R.id.et_bankname)
    EditText mEtBankname;
    @BindView(R.id.et_banknum)
    EditText mEtBanknum;
    @BindView(R.id.et_bank)
    EditText mEtBank;
    @BindView(R.id.tv_city1)
    TextView mTvCity1;
    @BindView(R.id.tv_endtime)
    TextView mTvEndtime;
    @BindView(R.id.tv_choose)
    TextView mTvChoose;

    //营业期限
    private String expired;

    private String ProvinceID;
    private String CityID;
    private StoreBean mStore;
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
    private TimePickerView2 startTime;
    private TimePickerView2 endTime;
    private TextView tv_starttime;
    private AlertDialog dialog;
    private String choose = "平方米";

    private String start = "";
    private String end = "";
    private String storeId = "";

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
        return new TopView(topbar_iv_back, topbar_tv_title,topbar_iv_right);
    }

    @Override
    protected void findViewById() {
        tv_starttime = findViewById(R.id.tv_starttime);
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
        tv_starttime.setOnClickListener(this);
        mTvEndtime.setOnClickListener(this);
        mTvChoose.setOnClickListener(this);
        topbar_iv_right.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        setTopTitle("申请添加店面");
        storeId = getIntent().getStringExtra("id");
        getStore();

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
                mTvExpired.setText(getTime(date));
                expired = (date.getTime() + "").substring(0, (date.getTime() + "").length() - 3);
            }
        });

        // 营业时间开始选择器
        startTime = new TimePickerView2(this, TimePickerView2.Type.HOURS_MINS);
        // 控制时间范围
        startTime.setTime(new Date());
        startTime.setCyclic(false);
        startTime.setCancelable(true);
        // 时间选择后回调
        startTime.setOnTimeSelectListener(new TimePickerView2.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(String date) {
                tv_starttime.setText(date);
                start = date;
            }
        });

        // 营业时间结束选择器
        endTime = new TimePickerView2(this, TimePickerView2.Type.HOURS_MINS);
        // 控制时间范围
        endTime.setTime(new Date());
        endTime.setCyclic(false);
        endTime.setCancelable(true);
        // 时间选择后回调
        endTime.setOnTimeSelectListener(new TimePickerView2.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(String date) {
                mTvEndtime.setText(date);
                end = date;
            }
        });

        topbar_iv_right.setVisibility(View.VISIBLE);
        topbar_iv_right.setText("修改店铺图片");
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
                CityConfig cityConfig = new CityConfig.Builder(ChangeStoreActivity.this)
                        .title("选择地区")
                        .titleBackgroundColor("#E9E9E9")
                        .textSize(18)
                        .titleTextColor("#585858")
                        .textColor("#000000")
                        .confirTextColor("#0000FF")
                        .cancelTextColor("#000000")
                        .province("江苏")
                        .city("常州")
                        .district("新北区")
                        .visibleItemsCount(5)
                        .provinceCyclic(true)
                        .cityCyclic(true)
                        .districtCyclic(true)
                        .itemPadding(5)
                        .setCityInfoType(CityConfig.CityInfoType.BASE)
                        .setCityWheelType(CityConfig.WheelType.PRO_CITY_DIS)
                        .build();
                CityPickerView cityPicker = new CityPickerView(cityConfig);
                cityPicker.show();

                //监听方法，获取选择结果
                cityPicker.setOnCityItemClickListener(new CityPickerView.OnCityItemClickListener() {
                    @Override
                    public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {

                        Log.d("选择地区", district.getId());
                        mTvCity.setText(province.getName() + city.getName() + district.getName());
                        ProvinceID = province.getId();
                        CityID = city.getId();
                        CountyID = district.getId();
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
                        mEtType.setText("一站式");
                        bottomDialog.dismiss();
                    }
                });
                contentView.findViewById(R.id.layout2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        type = "1";
                        mEtType.setText("汽修厂");
                        bottomDialog.dismiss();
                    }
                });
                contentView.findViewById(R.id.layout3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        type = "2";
                        mEtType.setText("美容店");
                        bottomDialog.dismiss();
                    }
                });
                contentView.findViewById(R.id.layout4).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        type = "3";
                        mEtType.setText("轮胎");
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
            case R.id.tv_starttime://营业开始时间
                startTime.show();
                break;
            case R.id.tv_endtime://营业开始时间
                endTime.show();
                break;
            case R.id.tv_choose://选择面积单位
                final Dialog bottomDialog3 = new Dialog(this, R.style.BottomDialog);
                View contentView3 = LayoutInflater.from(this).inflate(R.layout.dialog_choose, null);
                bottomDialog3.setContentView(contentView3);
                ViewGroup.LayoutParams layoutParams3 = contentView3.getLayoutParams();
                layoutParams3.width = getResources().getDisplayMetrics().widthPixels;
                contentView3.setLayoutParams(layoutParams3);
                contentView3.findViewById(R.id.layout1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        choose = "平方米";
                        mTvChoose.setText("平方米");
                        bottomDialog3.dismiss();
                    }
                });
                contentView3.findViewById(R.id.layout2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        choose = "亩";
                        mTvChoose.setText("亩");
                        bottomDialog3.dismiss();
                    }
                });
                contentView3.findViewById(R.id.layout3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        choose = "公顷";
                        mTvChoose.setText("公顷");
                        bottomDialog3.dismiss();
                    }
                });
                contentView3.findViewById(R.id.layout4).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        choose = "平方千米";
                        mTvChoose.setText("平方千米");
                        bottomDialog3.dismiss();
                    }
                });
                bottomDialog3.getWindow().setGravity(Gravity.BOTTOM);
                bottomDialog3.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
                bottomDialog3.setCanceledOnTouchOutside(true);
                bottomDialog3.show();
                break;
            case R.id.topbar_iv_right://修改图片
                break;
        }
    }

    /**
     * 更新商店信息
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
            ToastUtils.show(this, "请上传店铺海报");
            return;
        }
        if (path2.length() == 0) {
            ToastUtils.show(this, "请上传营业执照");
            return;
        }
        try {
            params.put("Img", new File(path), "image/png");
            params.put("CerImg", new File(path2), "image/png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
            map.put("text", mEtDescri.getText().toString().trim());
            String jsonString = JSON.toJSONString(map);
            params.add("Descri", jsonString);
        } else {
            params.add("Descri", mEtDescri.getText().toString().trim());
        }
        if (start.length() > 0 && end.length() > 0) {
            params.add("OpenTime", start + "-" + end);
        } else {
            params.add("OpenTime", "");
        }
        params.add("OpenArea", mEtYingyemianji.getText().toString().trim() + choose);
        final LoadingDialog dialog = new LoadingDialog(ChangeStoreActivity.this);
        dialog.show();
        RestClient.post(UrlUtils.updateStore(), params, this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    dialog.dismiss();
                    String result = new String(responseBody);
                    Log.d("更新", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if ("0".equals(Status)) {
                        JSONObject data = jsonObject.getJSONObject("Data");
                        Intent intent = new Intent(ChangeStoreActivity.this, ExamineActivity.class);
                        intent.putExtra("id", data.getString("ID"));
                        startActivity(intent);
                        finish();
                    } else if ("-1".equals(Status)) {
                        Intent intent = new Intent(ChangeStoreActivity.this, LoginActivity.class);
                        startActivity(intent);

                        finish();
                    } else {
                        ToastUtils.show(ChangeStoreActivity.this, jsonObject.getString("Data"));
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
                RestClient.post(UrlUtils.insertErrLog(), errParams, ChangeStoreActivity.this, new AsyncHttpResponseHandler() {
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
     * 获取店铺信息
     */
    private void getStore() {
        final RequestParams params = new RequestParams();
        params.add("WToken", CheShiLiShopApplication.wtoken);
        params.add("ID", storeId);
        RestClient.post(UrlUtils.queryStoreDetail(), params, this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    Log.d("获取店铺", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if ("0".equals(Status)) {
                        String data = jsonObject.getString("Data");
                        mStore = JSON.parseObject(data, StoreBean.class);
                        loadData();
                    } else if ("-1".equals(Status)) {
                        Intent intent = new Intent(ChangeStoreActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.show(ChangeStoreActivity.this, jsonObject.getString("Data"));
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
                errParams.add("Url", UrlUtils.queryStoreDetail());
                errParams.add("PostData", params.toString());
                errParams.add("WToken", CheShiLiShopApplication.wtoken);
                RestClient.post(UrlUtils.insertErrLog(), errParams, ChangeStoreActivity.this, new AsyncHttpResponseHandler() {
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
     * 加载数据
     */
    private void loadData() {
        mEtShopname.setText(mStore.Name);
        mEtShortName.setText(mStore.ShortName);
        mEtChargeTitle.setText(mStore.ChargeTitle);
        mEtChargeMan.setText(mStore.ChargeMan);
        mEtMobile.setText(mStore.Phone);
        mEtTel.setText(mStore.Tel);
        mTvExpired.setText(mStore.Expired);
        switch (mStore.Type) {
            case "0":
                mEtType.setText("一站式");
                break;
            case "1":
                mEtType.setText("汽修厂");
                break;
            case "2":
                mEtType.setText("美容店");
                break;
            case "3":
                mEtType.setText("轮胎");
                break;
        }
        switch (mStore.WDeviceType) {
            case "0":
                mEtWdeviceType.setText("手工");
                break;
            case "1":
                mEtWdeviceType.setText("机器");
                break;
        }
        mEtWdeviceNum.setText(mStore.WDeviceNum);
        mEtWpostions.setText(mStore.WPostions);
        mEtAdress.setText(mStore.Address);
        if (Double.parseDouble(mStore.Latitude) != 0) {
            mEtLatitude.setText(mStore.Latitude);
        }
        if (Double.parseDouble(mStore.Longitude) != 0) {
            mEtLongitude.setText(mStore.Longitude);
        }
        if (!mStore.Descri.equals("[]")) {
            try {
                JSONObject descri = new JSONObject(mStore.Descri);
                mEtDescri.setText(descri.getString("text"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Glide.with(this).load(UrlUtils.BASE_URL + "/Img/" + mStore.Img).into(mImg1);
        Glide.with(this).load(UrlUtils.BASE_URL + "/Img/" + mStore.CerImg).into(mImg2);
        mEtBankname.setText(mStore.BankName);
        mEtBanknum.setText(mStore.BankNo);
        mEtBank.setText(mStore.BankCompany);
        mTvCity.setText(mStore.AddressNames);
        mEtYingyemianji.setText(mStore.OpenArea);
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(date);
    }


}
