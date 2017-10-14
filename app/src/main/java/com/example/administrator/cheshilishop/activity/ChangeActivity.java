package com.example.administrator.cheshilishop.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;
import com.example.administrator.cheshilishop.bean.ProductBean;
import com.example.administrator.cheshilishop.dialog.TwoButtonAndContentCustomDialog;
import com.example.administrator.cheshilishop.net.RestClient;
import com.example.administrator.cheshilishop.utils.ToastUtils;
import com.example.administrator.cheshilishop.utils.UrlUtils;
import com.example.administrator.cheshilishop.widget.Amount2View;
import com.example.administrator.cheshilishop.widget.AmountView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 编辑服务
 * 作者：Ayase on 2017/9/8 15:03
 * 邮箱：ayase@ayase.cn
 */
public class ChangeActivity extends BaseActivity {

    @BindView(R.id.et_Descri)
    EditText mEtDescri;
    @BindView(R.id.tv_appointType)
    TextView mTvAppointType;
    @BindView(R.id.cb_1)
    CheckBox mCb1;
    @BindView(R.id.cb_2)
    CheckBox mCb2;
    @BindView(R.id.cb_3)
    CheckBox mCb3;
    @BindView(R.id.cb_4)
    CheckBox mCb4;
    @BindView(R.id.cb_5)
    CheckBox mCb5;
    @BindView(R.id.cb_6)
    CheckBox mCb6;
    @BindView(R.id.cb_7)
    CheckBox mCb7;
    @BindView(R.id.btn_commit)
    Button mBtnCommit;
    @BindView(R.id.layout_time)
    LinearLayout mLayoutTime;
    @BindView(R.id.layout)
    LinearLayout mLayout;
    @BindView(R.id.btn_choose)
    ImageView mBtnChoose;
    @BindView(R.id.tv_type)
    TextView mTvType;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.layout_data)
    RelativeLayout mLayoutData;


    private AmountView mAmountView;
    private Amount2View mAmountView2;

    private String gapPrice;
    private String LimitNum;
    private String enable = "1";
    private String appointtype = "0";
    private ProductBean product;
    private String AppointCont = "";
    private boolean flag = false;
    final int DATE_DIALOG = 1;

    int mYear, mMonth, mDay;

    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_change);
        ButterKnife.bind(this);
        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    protected BaseActivity getAct() {
        return this;
    }

    @Override
    protected TopView getTopViews() {
        return new TopView(topbar_iv_back, topbar_tv_title, topbar_iv_right);
    }

    @Override
    protected void findViewById() {
        mAmountView = findViewById(R.id.amount_view);
        mAmountView2 = findViewById(R.id.amount_view2);
    }

    @Override
    protected void setListener() {

        mAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                LimitNum = amount + "";
            }
        });
        mAmountView2.setOnAmountChangeListener(new Amount2View.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, double amount) {
                gapPrice = amount + "";
            }
        });

        mTvAppointType.setOnClickListener(this);
        mBtnCommit.setOnClickListener(this);
        topbar_iv_right.setOnClickListener(this);
        mBtnChoose.setOnClickListener(this);
        mTvDate.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        setTopTitle("编辑服务");
        product = (ProductBean) getIntent().getSerializableExtra("ProductBean");  ;
        topbar_iv_right.setVisibility(View.GONE);
        topbar_iv_right.setText("删除");
        mAmountView2.setAmount(Double.parseDouble(product.GapPrice));
        mEtDescri.setText(product.Descri);

        if (product.Enable.equals("1")){
            mTvType.setText("启用");
            mBtnChoose.setImageResource(R.mipmap.icon_choose_on);
            enable = 1 + "";
        }else {
            mTvType.setText("禁用");
            mBtnChoose.setImageResource(R.mipmap.icon_choose_off);
            enable = 0 + "";
        }
        switch (product.AppointType){
            case "0":
                appointtype = 0 + "";
                mTvAppointType.setText("不使用");
                mLayout.setVisibility(View.GONE);
                mLayoutData.setVisibility(View.GONE);
                break;
            case "1":
                appointtype = 1 + "";
                mLayoutTime.setOnClickListener(null);
                mTvAppointType.setText("一次预约");
                AppointCont = "";
                mLayout.setVisibility(View.GONE);
                mLayoutData.setVisibility(View.VISIBLE);
                break;
            case "2":
                appointtype = 2 + "";
                mLayoutTime.setOnClickListener(null);
                mTvAppointType.setText("每天");
                mLayout.setVisibility(View.GONE);
                mLayoutData.setVisibility(View.GONE);
                break;
            case "3":
                appointtype = 3 + "";
                mLayoutTime.setOnClickListener(null);
                mTvAppointType.setText("法定工作日");
                mLayout.setVisibility(View.GONE);
                mLayoutData.setVisibility(View.GONE);
                break;
            case "4":
                appointtype = 4 + "";
                mLayoutTime.setOnClickListener(null);
                mTvAppointType.setText("法定节假日");
                mLayout.setVisibility(View.GONE);
                mLayoutData.setVisibility(View.GONE);
                break;
            case "5":
                appointtype = 5 + "";
                mLayoutTime.setOnClickListener(null);
                mTvAppointType.setText("周五至周日");
                mLayout.setVisibility(View.GONE);
                mLayoutData.setVisibility(View.GONE);
                break;
            case "6":
                appointtype = 6 + "";
                AppointCont = "";
                mTvAppointType.setText("自定义");
                mLayout.setVisibility(View.VISIBLE);
                mLayoutData.setVisibility(View.GONE);
                break;

        }
    }

    @Override
    protected void onClickEvent(View paramView) {
        switch (paramView.getId()) {
//            case R.id.tv_enable://是否启用
//                final Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
//                View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_enable, null);
//                bottomDialog.setContentView(contentView);
//                ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
//                layoutParams.width = getResources().getDisplayMetrics().widthPixels;
//                contentView.setLayoutParams(layoutParams);
//                contentView.findViewById(R.id.layout1).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        bottomDialog.dismiss();
//                        mTvEnable.setText("启用");
//                        enable = 1 + "";
//                    }
//                });
//                contentView.findViewById(R.id.layout2).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        bottomDialog.dismiss();
//                        mTvEnable.setText("禁用");
//                        enable = 0 + "";
//                    }
//                });
//                bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
//                bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
//                bottomDialog.setCanceledOnTouchOutside(true);
//                bottomDialog.show();
//                break;
            case R.id.tv_appointType://日期类型
                final Dialog bottomDialog2 = new Dialog(this, R.style.BottomDialog);
                View contentView2 = LayoutInflater.from(this).inflate(R.layout.dialog_appointtype, null);
                bottomDialog2.setContentView(contentView2);
                ViewGroup.LayoutParams layoutParams2 = contentView2.getLayoutParams();
                layoutParams2.width = getResources().getDisplayMetrics().widthPixels;
                contentView2.setLayoutParams(layoutParams2);
                contentView2.findViewById(R.id.layout1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomDialog2.dismiss();
                        appointtype = 0 + "";
                        mTvAppointType.setText("不使用");
                        mLayout.setVisibility(View.GONE);
                        mLayoutData.setVisibility(View.GONE);
                    }
                });
                contentView2.findViewById(R.id.layout2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomDialog2.dismiss();
                        appointtype = 1 + "";
                        mLayoutTime.setOnClickListener(null);
                        mTvAppointType.setText("一次预约");
                        AppointCont = "";
                        mLayout.setVisibility(View.GONE);
                        mLayoutData.setVisibility(View.VISIBLE);
                    }
                });
                contentView2.findViewById(R.id.layout3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomDialog2.dismiss();
                        appointtype = 2 + "";
                        mLayoutTime.setOnClickListener(null);
                        mTvAppointType.setText("每天");
                        mLayout.setVisibility(View.GONE);
                        mLayoutData.setVisibility(View.GONE);
                    }
                });
                contentView2.findViewById(R.id.layout4).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomDialog2.dismiss();
                        appointtype = 3 + "";
                        mLayoutTime.setOnClickListener(null);
                        mTvAppointType.setText("法定工作日");
                        mLayout.setVisibility(View.GONE);
                        mLayoutData.setVisibility(View.GONE);
                    }
                });
                contentView2.findViewById(R.id.layout5).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomDialog2.dismiss();
                        appointtype = 4 + "";
                        mLayoutTime.setOnClickListener(null);
                        mTvAppointType.setText("法定节假日");
                        mLayout.setVisibility(View.GONE);
                        mLayoutData.setVisibility(View.GONE);
                    }
                });
                contentView2.findViewById(R.id.layout6).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomDialog2.dismiss();
                        appointtype = 5 + "";
                        mLayoutTime.setOnClickListener(null);
                        mTvAppointType.setText("周五至周日");
                        mLayout.setVisibility(View.GONE);
                        mLayoutData.setVisibility(View.GONE);
                    }
                });
                contentView2.findViewById(R.id.layout7).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomDialog2.dismiss();
                        appointtype = 6 + "";
                        AppointCont = "";
                        mTvAppointType.setText("自定义");
                        mLayout.setVisibility(View.VISIBLE);
                        mLayoutData.setVisibility(View.GONE);
                    }
                });
                bottomDialog2.getWindow().setGravity(Gravity.BOTTOM);
                bottomDialog2.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
                bottomDialog2.setCanceledOnTouchOutside(true);
                bottomDialog2.show();
                break;
            case R.id.btn_commit://
                commit();
                break;
            case R.id.topbar_iv_right://
                delete();
                break;
            case R.id.btn_choose://选择状态
                if (!flag) {
                    mTvType.setText("启用");
                    mBtnChoose.setImageResource(R.mipmap.icon_choose_on);
                    enable = 1 + "";
                    naService(enable);
                } else {
                    mTvType.setText("禁用");
                    mBtnChoose.setImageResource(R.mipmap.icon_choose_off);
                    enable = 0 + "";
                    naService(enable);
                }
                flag = !flag;
                break;
            case R.id.tv_date://只有一次选择日期
                showDialog(DATE_DIALOG);
                break;
        }
    }

    /**
     * 设置服务是否可用
     * @param enable
     */
    private void naService(String enable) {
        RequestParams params = new RequestParams();
        params.add("WToken",CheShiLiShopApplication.wtoken);
        params.add("ID",product.ID);
        params.add("Enable",enable);
        RestClient.post(UrlUtils.NAService(), params, ChangeActivity.this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    Log.d("禁用服务",result);
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
     * 删除
     */
    private void delete() {
        final RequestParams params = new RequestParams();
        params.add("WToken", CheShiLiShopApplication.wtoken);
        params.add("ID", product.ID);
        RestClient.post(UrlUtils.delCategory(), params, this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    Log.d("删除",result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if ("0".equals(Status)) {
                        ToastUtils.show(ChangeActivity.this, "删除成功");
                        finish();
                    } else if ("-1".equals(Status)) {
                        Intent intent = new Intent(ChangeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.show(ChangeActivity.this, jsonObject.getString("Data"));
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
                errParams.add("Url", UrlUtils.delCategory());
                errParams.add("PostData", params.toString());
                errParams.add("WToken", CheShiLiShopApplication.wtoken);
                RestClient.post(UrlUtils.insertErrLog(), errParams, ChangeActivity.this, new AsyncHttpResponseHandler() {
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
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                DatePickerDialog dialog = new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
                DatePicker dp = dialog.getDatePicker();
                dp.setMinDate(new Date().getTime());
                return dialog;
        }
        return null;
    }

    /**
     * 设置日期 利用StringBuffer追加
     */
    public void display() {
        mTvDate.setText(new StringBuffer().append(mYear + 1).append("-").append(mMonth).append("-").append(mDay).append(" "));
        AppointCont = mTvDate.getText().toString().trim();
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year-1;
            mMonth = monthOfYear+1;
            mDay = dayOfMonth;
            display();
        }
    };


    /**
     * 提交数据
     */
    private void commit() {
        final RequestParams params = new RequestParams();
        params.add("WToken", CheShiLiShopApplication.wtoken);
        params.add("ID", product.ID);
        params.add("StoreID", CheShiLiShopApplication.storeID);
        params.add("ProductID", product.ProductID);
        params.add("GapPrice", gapPrice);
        params.add("Descri", mEtDescri.getText().toString().trim());
        params.add("Enable", enable);
        params.add("AppointType", appointtype);
        params.add("LimitNum", "99999");
        if (mCb1.isChecked()) {
            AppointCont = AppointCont + "1,";
        }
        if (mCb2.isChecked()) {
            AppointCont = AppointCont + "2,";
        }
        if (mCb3.isChecked()) {
            AppointCont = AppointCont + "3,";
        }
        if (mCb4.isChecked()) {
            AppointCont = AppointCont + "4,";
        }
        if (mCb5.isChecked()) {
            AppointCont = AppointCont + "5,";
        }
        if (mCb6.isChecked()) {
            AppointCont = AppointCont + "6,";
        }
        if (mCb7.isChecked()) {
            AppointCont = AppointCont + "7,";
        }

        if ("6".equals(appointtype) && !TextUtils.isEmpty(AppointCont)) {
            AppointCont = AppointCont.substring(AppointCont.length() - 1, AppointCont.length());
            Log.d("星期", AppointCont);
            params.add("AppointCont", AppointCont);
        } else if ("6".equals(appointtype) && TextUtils.isEmpty(AppointCont)) {
            ToastUtils.show(ChangeActivity.this, "请选择可预约时间！");
            return;
        }
        if ("1".equals(appointtype) && TextUtils.isEmpty(AppointCont)){
            ToastUtils.show(ChangeActivity.this, "请选择可预约时间！");
            return;
        }
        RestClient.post(UrlUtils.updateService(), params, this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    Log.d("更改店铺", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if ("0".equals(Status)) {
                        ToastUtils.show(ChangeActivity.this, "修改成功");
                        finish();
                    } else if ("-1".equals(Status)) {
                        Intent intent = new Intent(ChangeActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        ToastUtils.show(ChangeActivity.this, jsonObject.getString("Data"));
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
                errParams.add("Url", UrlUtils.updateService());
                errParams.add("PostData", params.toString());
                errParams.add("WToken", CheShiLiShopApplication.wtoken);
                RestClient.post(UrlUtils.insertErrLog(), errParams, ChangeActivity.this, new AsyncHttpResponseHandler() {
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            TwoButtonAndContentCustomDialog dialog2 = new TwoButtonAndContentCustomDialog(
                    this, R.style.Translucent_NoTitle) {
                @Override
                public void doConfirm() {
                    super.doConfirm();
                    finish();
                }
            };
            dialog2.show();
            dialog2.setContent("您还没有保存，确定退出吗？");
            dialog2.setTitle("退出");
            dialog2.setCancel("取消");
            dialog2.setConfirm("确认");
        }
        return true;
    }

}
