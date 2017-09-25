package com.example.administrator.cheshilishop.activity;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;
import com.example.administrator.cheshilishop.adapter.CommissionAdapter;
import com.example.administrator.cheshilishop.bean.CommissionBean;
import com.example.administrator.cheshilishop.dialog.LoadingDialog;
import com.example.administrator.cheshilishop.net.RestClient;
import com.example.administrator.cheshilishop.utils.ToastUtils;
import com.example.administrator.cheshilishop.utils.UrlUtils;
import com.example.administrator.cheshilishop.widget.TimePickerView;
import com.example.administrator.cheshilishop.widget.refresh.MaterialRefreshLayout;
import com.example.administrator.cheshilishop.widget.refresh.MaterialRefreshListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 佣金管理
 * 作者：Ayase on 2017/8/9 15:13
 * 邮箱：ayase@ayase.cn
 */

public class CommissionActivity extends BaseActivity {

    @BindView(R.id.btn_time)
    Button mBtnTime;
    @BindView(R.id.lv_commission)
    ListView mLvCommission;
    @BindView(R.id.layout_main)
    LinearLayout mLayoutMain;
    @BindView(R.id.tv_yongjin)
    TextView mTvYongjin;
    @BindView(R.id.tv_leiji)
    TextView mTvLeiji;
    @BindView(R.id.btn_null)
    Button mBtnNull;
    @BindView(R.id.layout_null)
    LinearLayout mLayoutNull;
    @BindView(R.id.mrl_experience)
    MaterialRefreshLayout mMrlExperience;
    @BindView(R.id.tv_time)
    TextView mTvTime;


    TimePickerView pvTime;


    private int page = 1;
    private int size = 10;
    private int year;
    private int month;
    private CommissionAdapter adapter;

    private List<CommissionBean> list = new ArrayList<CommissionBean>();

    private LinearLayout layout_null;
    private MaterialRefreshLayout refreshLayout;

    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_commission);
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
        refreshLayout = findViewById(R.id.mrl_experience);
        ;
        layout_null = findViewById(R.id.layout_null);
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                page = 1;
                list.clear();
                getData(year, month, page);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                page++;
                getData(year, month, page);
            }
        });
    }

    @Override
    protected void setListener() {
        mBtnTime.setOnClickListener(this);
        mBtnNull.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        setTopTitle("佣金管理");
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        mTvTime.setText(year + "-" + month);
        mBtnTime.setText(year + "-" + month);
        getData(year, month, page);
        //获取总佣金
        queryCmnCount();
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
                mBtnTime.setText(getTime(date));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                Log.d("时间",year+"==="+month);
                getData(year, month+1, 1);
            }
        });


    }


    /**
     * 获取总佣金
     */
    private void queryCmnCount() {
        RequestParams params = new RequestParams();
        params.add("WToken", CheShiLiShopApplication.wtoken);
        params.add("StoreID", CheShiLiShopApplication.storeID);
        RestClient.post(UrlUtils.queryCmnCount(), params, this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.d("总佣金", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if ("0".equals(Status)) {
                        JSONObject data = jsonObject.getJSONObject("Data");
                        if (TextUtils.isEmpty(data.getString("Now_Amount"))) {
                            mTvYongjin.setText("0");
                        } else {
                            mTvYongjin.setText(data.getString("Now_Amount"));
                        }
                        if (TextUtils.isEmpty(data.getString("History_Amount"))) {
                            mTvLeiji.setText("0");
                        } else {
                            mTvLeiji.setText(data.getString("History_Amount"));
                        }


                    } else if ("-1".equals(Status)) {
                        Intent intent = new Intent(CommissionActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.show(CommissionActivity.this, jsonObject.getString("Data"));
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
    protected void onClickEvent(View paramView) {
        switch (paramView.getId()) {
            case R.id.btn_time://选择时间
                pvTime.show();
                break;
            case R.id.btn_null://去首页
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    /**
     * 获取佣金记录
     */
    private void getData(int year, int month, final int page) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 0);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);


        Date date = null;
        Date date2 = null;
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            date = df.parse(year + "-" + month + "-" + dayOfMonth);
            date2 = df.parse(year + "-" + month + "-1");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar endcal = Calendar.getInstance();
        endcal.setTime(date);
        long endTime = endcal.getTimeInMillis();
        Calendar startcal = Calendar.getInstance();
        startcal.setTime(date2);
        long startTims = startcal.getTimeInMillis();


        Log.d("日期1", "start=" + startTims);
        Log.d("日期2", "endTime=" + endTime);
        final RequestParams params = new RequestParams();
        params.add("WToken", CheShiLiShopApplication.wtoken);
        params.add("StartTime",(startTims + "").substring(0,(startTims + "").length()-3));
        params.add("EndTime", (endTime + "").substring(0,(endTime + "").length()-3));
        params.add("Rows", size + "");
        params.add("N", page + "");
        params.add("StoreID", CheShiLiShopApplication.storeID);
        RestClient.post(UrlUtils.queryCmnList(), params, this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    Log.d("查询佣金", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if ("0".equals(Status)) {
                        JSONObject data = jsonObject.getJSONObject("Data");
                        String rows = data.getString("Rows");
                        Log.d("Rows", rows);
                        List<CommissionBean> mList = com.alibaba.fastjson.JSONObject.parseArray(rows, CommissionBean.class);
                        if (page == 1) {
                            if (mList.size() == 0) {
                                layout_null.setVisibility(View.VISIBLE);
                                refreshLayout.setVisibility(View.GONE);
                            } else {
                                layout_null.setVisibility(View.GONE);
                                refreshLayout.setVisibility(View.VISIBLE);
                                list.clear();
                                list.addAll(mList);
                                adapter = new CommissionAdapter(CommissionActivity.this, list, 1);
                                mLvCommission.setAdapter(adapter);
                                refreshLayout.finishRefresh();
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            if (mList.size() == 0) {
                                ToastUtils.show(CommissionActivity.this, "已加载完全部数据");
                            } else {
                                list.addAll(mList);
                                adapter.notifyDataSetChanged();
                            }
                            refreshLayout.finishRefreshLoadMore();
                        }
                    } else if ("-1".equals(Status)) {
                        Intent intent = new Intent(CommissionActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.show(CommissionActivity.this, jsonObject.getString("Data"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                RequestParams errParams = new RequestParams();
                errParams.add("LogCont", new String(responseBody));
                errParams.add("Url", UrlUtils.queryServiceAppointDetail());
                errParams.add("PostData", params.toString());
                errParams.add("WToken", CheShiLiShopApplication.wtoken);
                RestClient.post(UrlUtils.insertErrLog(), errParams, CommissionActivity.this, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

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
                        RestClient.post(UrlUtils.insertErrLog(), errParams, CommissionActivity.this, new AsyncHttpResponseHandler() {
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
        });
    }

    public static String getTime(Date date) {
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM");
        return format.format(date);
    }
}
