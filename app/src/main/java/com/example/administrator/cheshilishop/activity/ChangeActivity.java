package com.example.administrator.cheshilishop.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;
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
    @BindView(R.id.tv_enable)
    TextView mTvEnable;
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

    private AmountView mAmountView;
    private Amount2View mAmountView2;

    private String gapPrice;
    private String LimitNum;
    private String enable = "0";
    private String appointtype = "6";
    private String id;
    private String productID;
    private String AppointCont = "";

    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_change);
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

        mTvEnable.setOnClickListener(this);
        mTvAppointType.setOnClickListener(this);
        mBtnCommit.setOnClickListener(this);
        topbar_iv_right.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        setTopTitle("编辑服务");
        id = getIntent().getExtras().getString("ID");
        productID = getIntent().getExtras().getString("productID");
        topbar_iv_right.setVisibility(View.VISIBLE);
        topbar_iv_right.setText("删除");
    }

    @Override
    protected void onClickEvent(View paramView) {
        switch (paramView.getId()) {
            case R.id.tv_enable://是否启用
                final Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
                View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_enable, null);
                bottomDialog.setContentView(contentView);
                ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
                layoutParams.width = getResources().getDisplayMetrics().widthPixels;
                contentView.setLayoutParams(layoutParams);
                contentView.findViewById(R.id.layout1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomDialog.dismiss();
                        mTvEnable.setText("启用");
                        enable = 0 + "";
                    }
                });
                contentView.findViewById(R.id.layout2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomDialog.dismiss();
                        mTvEnable.setText("禁用");
                        enable = 1 + "";
                    }
                });
                bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
                bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
                bottomDialog.setCanceledOnTouchOutside(true);
                bottomDialog.show();
                break;
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
                        mCb1.setClickable(false);
                        mCb2.setClickable(false);
                        mCb3.setClickable(false);
                        mCb4.setClickable(false);
                        mCb5.setClickable(false);
                        mCb6.setClickable(false);
                        mCb7.setClickable(false);
                        mCb1.setChecked(false);
                        mCb2.setChecked(false);
                        mCb3.setChecked(false);
                        mCb4.setChecked(false);
                        mCb5.setChecked(false);
                        mCb6.setChecked(false);
                        mCb7.setChecked(false);
                    }
                });
                contentView2.findViewById(R.id.layout2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomDialog2.dismiss();
                        appointtype = 1 + "";
                        mLayoutTime.setOnClickListener(null);
                        mTvAppointType.setText("一次预约");
                        mCb1.setClickable(false);
                        mCb2.setClickable(false);
                        mCb3.setClickable(false);
                        mCb4.setClickable(false);
                        mCb5.setClickable(false);
                        mCb6.setClickable(false);
                        mCb7.setClickable(false);
                        mCb1.setChecked(false);
                        mCb2.setChecked(false);
                        mCb3.setChecked(false);
                        mCb4.setChecked(false);
                        mCb5.setChecked(false);
                        mCb6.setChecked(false);
                        mCb7.setChecked(false);
                    }
                });
                contentView2.findViewById(R.id.layout3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomDialog2.dismiss();
                        appointtype = 2 + "";
                        mLayoutTime.setOnClickListener(null);
                        mTvAppointType.setText("每天");
                        mCb1.setClickable(false);
                        mCb2.setClickable(false);
                        mCb3.setClickable(false);
                        mCb4.setClickable(false);
                        mCb5.setClickable(false);
                        mCb6.setClickable(false);
                        mCb7.setClickable(false);
                        mCb1.setChecked(false);
                        mCb2.setChecked(false);
                        mCb3.setChecked(false);
                        mCb4.setChecked(false);
                        mCb5.setChecked(false);
                        mCb6.setChecked(false);
                        mCb7.setChecked(false);
                    }
                });
                contentView2.findViewById(R.id.layout4).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomDialog2.dismiss();
                        appointtype = 3 + "";
                        mLayoutTime.setOnClickListener(null);
                        mTvAppointType.setText("法定工作日");
                        mCb1.setClickable(false);
                        mCb2.setClickable(false);
                        mCb3.setClickable(false);
                        mCb4.setClickable(false);
                        mCb5.setClickable(false);
                        mCb6.setClickable(false);
                        mCb7.setClickable(false);
                        mCb1.setChecked(false);
                        mCb2.setChecked(false);
                        mCb3.setChecked(false);
                        mCb4.setChecked(false);
                        mCb5.setChecked(false);
                        mCb6.setChecked(false);
                        mCb7.setChecked(false);
                    }
                });
                contentView2.findViewById(R.id.layout5).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomDialog2.dismiss();
                        appointtype = 4 + "";
                        mLayoutTime.setOnClickListener(null);
                        mTvAppointType.setText("法定节假日");
                        mCb1.setClickable(false);
                        mCb2.setClickable(false);
                        mCb3.setClickable(false);
                        mCb4.setClickable(false);
                        mCb5.setClickable(false);
                        mCb6.setClickable(false);
                        mCb7.setClickable(false);
                        mCb1.setChecked(false);
                        mCb2.setChecked(false);
                        mCb3.setChecked(false);
                        mCb4.setChecked(false);
                        mCb5.setChecked(false);
                        mCb6.setChecked(false);
                        mCb7.setChecked(false);
                    }
                });
                contentView2.findViewById(R.id.layout6).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomDialog2.dismiss();
                        appointtype = 5 + "";
                        mLayoutTime.setOnClickListener(null);
                        mTvAppointType.setText("周五至周日");
                        mCb1.setClickable(false);
                        mCb2.setClickable(false);
                        mCb3.setClickable(false);
                        mCb4.setClickable(false);
                        mCb5.setClickable(false);
                        mCb6.setClickable(false);
                        mCb7.setClickable(false);
                        mCb1.setChecked(false);
                        mCb2.setChecked(false);
                        mCb3.setChecked(false);
                        mCb4.setChecked(false);
                        mCb5.setChecked(false);
                        mCb6.setChecked(false);
                        mCb7.setChecked(false);
                    }
                });
                contentView2.findViewById(R.id.layout7).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomDialog2.dismiss();
                        appointtype = 6 + "";
                        mTvAppointType.setText("自定义");
                        mCb1.setClickable(true);
                        mCb2.setClickable(true);
                        mCb3.setClickable(true);
                        mCb4.setClickable(true);
                        mCb5.setClickable(true);
                        mCb6.setClickable(true);
                        mCb7.setClickable(true);
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
        }
    }

    /**
     * 删除
     */
    private void delete() {
        final RequestParams params = new RequestParams();
        params.add("WToken", CheShiLiShopApplication.wtoken);
        params.add("ID", id);
        RestClient.post(UrlUtils.delCategory(), params, this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
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

    /**
     * 提交数据
     */
    private void commit() {
        final RequestParams params = new RequestParams();
        params.add("WToken", CheShiLiShopApplication.wtoken);
        params.add("ID", id);
        params.add("StoreID", CheShiLiShopApplication.storeID);
        params.add("ProductID", productID);
        params.add("GapPrice", gapPrice);
        params.add("Descri", mEtDescri.getText().toString().trim());
        params.add("Enable", enable);
        params.add("AppointType", appointtype);
        params.add("LimitNum", LimitNum);
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
        if ("6".equals(appointtype)){
            AppointCont = AppointCont.substring(AppointCont.length() - 1, AppointCont.length());
            Log.d("星期", AppointCont);
            params.add("AppointCont", AppointCont);
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
