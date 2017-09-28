package com.example.administrator.cheshilishop.activity;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
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
import com.example.administrator.cheshilishop.adapter.WalletAdapter;
import com.example.administrator.cheshilishop.bean.CommissionBean;
import com.example.administrator.cheshilishop.bean.WalletBean;
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
 * 交易明细
 * 作者：Ayase on 2017/9/27 15:03
 * 邮箱：ayase@ayase.cn
 */
public class AccountListActivity extends BaseActivity {


    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.lv_wallet)
    ListView mLvWallet;


    private LinearLayout layout_null;
    private Button btn_null;
    TimePickerView pvTime;

    private int page = 1;
    private int size = 10;
    private WalletAdapter adapter;
    private List<WalletBean> list = new ArrayList<WalletBean>();
    private MaterialRefreshLayout refreshLayout;
    private int year;
    private int month;

    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_accountlist);
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
        mTvDate.setOnClickListener(this);
        refreshLayout = findViewById(R.id.mrl_experience);;
        layout_null = findViewById(R.id.layout_null);
        btn_null = findViewById(R.id.btn_null);
        btn_null.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountListActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

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
    protected void processLogic() {
        setTopTitle("交易明细");

        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH)+1;
        mTvDate.setText(year+"年"+month+"月");
        getData(year, month, page);

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
                mTvDate.setText(getTime(date));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH)+1;
                getData(year, month, page);
                Log.d("时间",year+"==="+month);
            }
        });
    }

    @Override
    protected void onClickEvent(View paramView) {
        switch (paramView.getId()){
            case R.id.tv_date://选择时间
                pvTime.show();
                break;
        }
    }

    /**
     * 获取交易记录
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
        RestClient.post(UrlUtils.queryAccountList(), params, this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    Log.d("交易明细", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if ("0".equals(Status)) {
                        JSONObject data = jsonObject.getJSONObject("Data");
                        String rows = data.getString("Rows");
                        Log.d("Rows", rows);
                        List<WalletBean> mList = com.alibaba.fastjson.JSONObject.parseArray(rows, WalletBean.class);
                        if (page == 1) {
                            if (mList.size() == 0) {
                                layout_null.setVisibility(View.VISIBLE);
                                refreshLayout.setVisibility(View.GONE);
                            } else {
                                layout_null.setVisibility(View.GONE);
                                refreshLayout.setVisibility(View.VISIBLE);
                                list.clear();
                                list.addAll(mList);
                                adapter = new WalletAdapter(AccountListActivity.this, list, 1);
                                mLvWallet.setAdapter(adapter);
                                refreshLayout.finishRefresh();
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            if (mList.size() == 0) {
                                ToastUtils.show(AccountListActivity.this, "已加载完全部数据");
                            } else {
                                list.addAll(mList);
                                adapter.notifyDataSetChanged();
                            }
                            refreshLayout.finishRefreshLoadMore();
                        }
                    } else if ("-1".equals(Status)) {
                        Intent intent = new Intent(AccountListActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.show(AccountListActivity.this, jsonObject.getString("Data"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                RequestParams errParams = new RequestParams();
                errParams.add("LogCont", new String(responseBody));
                errParams.add("Url", UrlUtils.queryAccountList());
                errParams.add("PostData", params.toString());
                errParams.add("WToken", CheShiLiShopApplication.wtoken);
                RestClient.post(UrlUtils.insertErrLog(), errParams, AccountListActivity.this, new AsyncHttpResponseHandler() {
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
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy年MM月");
        return format.format(date);
    }

}
