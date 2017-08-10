package com.example.administrator.cheshilishop.activity;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;
import com.example.administrator.cheshilishop.adapter.BookingAllAdapter;
import com.example.administrator.cheshilishop.adapter.CommissionAdapter;
import com.example.administrator.cheshilishop.bean.BookingBean;
import com.example.administrator.cheshilishop.bean.CommissionBean;
import com.example.administrator.cheshilishop.net.RestClient;
import com.example.administrator.cheshilishop.utils.ToastUtils;
import com.example.administrator.cheshilishop.utils.UrlUtils;
import com.example.administrator.cheshilishop.widget.refresh.MaterialRefreshLayout;
import com.example.administrator.cheshilishop.widget.refresh.MaterialRefreshListener;
import com.example.administrator.cheshilishop.widget.wheelview.ChangeDatePopwindow;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
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
        refreshLayout = findViewById(R.id.mrl_experience);;
        layout_null = findViewById(R.id.layout_null);
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                page = 1;
                list.clear();
                getData(year,month,page);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                page++;
                getData(year,month,page);
            }
        });
    }

    @Override
    protected void setListener() {
        mBtnTime.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        setTopTitle("佣金管理");
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH );
        getData(year,month,page);
    }

    @Override
    protected void onClickEvent(View paramView) {
        switch (paramView.getId()) {
            case R.id.btn_time://选择时间
                final String[] str = new String[10];
                ChangeDatePopwindow mChangeBirthDialog = new ChangeDatePopwindow(CommissionActivity.this);
                mChangeBirthDialog.setDate("2017", "8", "20");
                mChangeBirthDialog.showAtLocation(mLayoutMain, Gravity.BOTTOM, 0, 0);
                mChangeBirthDialog.setBirthdayListener(new ChangeDatePopwindow.OnBirthListener() {

                    @Override
                    public void onClick(String year, String month, String day) {
                        // TODO Auto-generated method stub
                        Toast.makeText(CommissionActivity.this, year + "-" + month + "-" + day, Toast.LENGTH_LONG).show();
                        StringBuilder sb = new StringBuilder();
//                        sb.append(year.substring(0, year.length() - 1)).append("-").append(month.substring(0, day.length() - 1)).append("-").append(day);
                        str[0] = year.substring(0,year.length()) + "-" + month.substring(0,month.length()-1);
                        str[1] = sb.toString();
                        Log.d("时间", str[0]);
                        mBtnTime.setText(str[0]);
                        CommissionActivity.this.year = Integer.parseInt(year.substring(0,year.length()));
                        CommissionActivity.this.month = Integer.parseInt(month.substring(0,month.length()-1));
                        getData( CommissionActivity.this.year,CommissionActivity.this.month,page);
                    }
                });
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
            date = df.parse(year+"-"+month+"-"+dayOfMonth);
            date2 = df.parse(year+"-"+month+"-1");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar endcal = Calendar.getInstance();
        endcal.setTime(date);
        long endTime = endcal.getTimeInMillis();
        Calendar startcal = Calendar.getInstance();
        startcal.setTime(date2);
        long startTims = startcal.getTimeInMillis();


        Log.d("日期1","start="+startTims);
        Log.d("日期2","endTime="+endTime);
        RequestParams params = new RequestParams();
        params.add("WToken", CheShiLiShopApplication.wtoken);
//        params.add("StartTime",startTims+"");
        params.add("EndTime",endTime+"");
        params.add("Rows",size+"");
        params.add("N",page+"");
        params.add("StoreID",CheShiLiShopApplication.storeID);
        RestClient.post(UrlUtils.queryCmnList(), params, this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if ("0".equals(Status)){
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
                                adapter = new CommissionAdapter(CommissionActivity.this, list,1);
                                mLvCommission.setAdapter(adapter);
                                refreshLayout.finishRefresh();
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
                    }else {
                        ToastUtils.show(CommissionActivity.this,jsonObject.getString("Data"));
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
