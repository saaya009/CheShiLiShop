package com.example.administrator.cheshilishop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;
import com.example.administrator.cheshilishop.adapter.BookingAllAdapter;
import com.example.administrator.cheshilishop.adapter.WalletAdapter;
import com.example.administrator.cheshilishop.bean.BookingBean;
import com.example.administrator.cheshilishop.bean.WalletBean;
import com.example.administrator.cheshilishop.net.RestClient;
import com.example.administrator.cheshilishop.utils.ToastUtils;
import com.example.administrator.cheshilishop.utils.UrlUtils;
import com.example.administrator.cheshilishop.widget.refresh.MaterialRefreshLayout;
import com.example.administrator.cheshilishop.widget.refresh.MaterialRefreshListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 我的钱包
 * 作者：Ayase on 2017/9/27 14:09
 * 邮箱：ayase@ayase.cn
 */

public class MyWalletActivity extends BaseActivity {

    @BindView(R.id.tv_accountCount)
    TextView mTvAccountCount;
    @BindView(R.id.lv_wallet)
    ListView mLvWallet;
    @BindView(R.id.layout_other)
    RelativeLayout mLayoutOther;


    private LinearLayout layout_null;
    private Button btn_null;

    private int page = 1;
    private int size = 10;
    private WalletAdapter adapter;
    private List<WalletBean> list = new ArrayList<WalletBean>();
    private MaterialRefreshLayout refreshLayout;

    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_mywallet);
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
        mLayoutOther.setOnClickListener(this);
        refreshLayout = findViewById(R.id.mrl_experience);;
        layout_null = findViewById(R.id.layout_null);
        btn_null = findViewById(R.id.btn_null);
        btn_null.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyWalletActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                page = 1;
                list.clear();
                queryAccountList(page);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                page++;
                queryAccountList(page);
            }
        });
    }

    @Override
    protected void processLogic() {
        setTopTitle("我的钱包");
        queryAccountCount();
        queryAccountList(page);
    }

    @Override
    protected void onClickEvent(View paramView) {
        switch (paramView.getId()){
            case R.id.layout_other://更多
                Intent intent = new Intent(this,AccountListActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 获取余额
     */
    private void queryAccountCount(){
        RequestParams params = new RequestParams();
        params.add("WToken", CheShiLiShopApplication.wtoken);
        RestClient.post(UrlUtils.queryAccountCount(), params, MyWalletActivity.this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    android.util.Log.d("查询余额", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if ("0".equals(Status)) {
                        mTvAccountCount.setText(jsonObject.getString("Data"));
                    } else if ("-1".equals(Status)) {
                        Intent intent = new Intent(MyWalletActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.show(MyWalletActivity.this, jsonObject.getString("Data"));
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
     * 获取我的账单列表
     */
    private void queryAccountList(final int page){
        final RequestParams params = new RequestParams();
        params.add("WToken",CheShiLiShopApplication.wtoken);
        params.add("StoreID",CheShiLiShopApplication.storeID);
        params.add("N",page+"");
        params.add("Rows",size+"");
        params.add("Status","-1");
        RestClient.post(UrlUtils.queryAccountList(), params,this, new AsyncHttpResponseHandler() {
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
                                adapter = new WalletAdapter(MyWalletActivity.this, list,1);
                                mLvWallet.setAdapter(adapter);
                                refreshLayout.finishRefresh();
                            }
                        } else {
                            if (mList.size() == 0) {
                                ToastUtils.show(MyWalletActivity.this, "已加载完全部数据");
                            } else {
                                list.addAll(mList);
                                adapter.notifyDataSetChanged();
                            }
                            refreshLayout.finishRefreshLoadMore();
                        }
                    }else {
                        ToastUtils.show(MyWalletActivity.this,jsonObject.getString("Data"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                RequestParams errParams = new RequestParams();
                try {
                    errParams.add("LogCont", URLEncoder.encode(new String(responseBody),"UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                errParams.add("Url",UrlUtils.queryAccountList());
                errParams.add("PostData",params.toString());
                errParams.add("WToken",CheShiLiShopApplication.wtoken);
                RestClient.post(UrlUtils.insertErrLog(), errParams, MyWalletActivity.this, new AsyncHttpResponseHandler() {
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

}
