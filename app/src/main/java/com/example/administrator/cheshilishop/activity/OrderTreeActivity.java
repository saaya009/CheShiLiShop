package com.example.administrator.cheshilishop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;
import com.example.administrator.cheshilishop.adapter.SimpleTreeAdapter;
import com.example.administrator.cheshilishop.bean.FileBean;
import com.example.administrator.cheshilishop.bean.UserRegisterBean;
import com.example.administrator.cheshilishop.net.RestClient;
import com.example.administrator.cheshilishop.utils.ToastUtils;
import com.example.administrator.cheshilishop.utils.UrlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * 作者：Ayase on 2017/8/24 10:27
 * 邮箱：ayase@ayase.cn
 */
public class OrderTreeActivity extends BaseActivity {

    private List<FileBean> mDatas2 = new ArrayList<FileBean>();
    private ListView mTree;
    private SimpleTreeAdapter mAdapter;

    private List<UserRegisterBean> list = new ArrayList<UserRegisterBean>();
    private List<UserRegisterBean> list2 = new ArrayList<UserRegisterBean>();
    private List<UserRegisterBean> list3 = new ArrayList<UserRegisterBean>();

    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_ordedrtree);
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
        mTree = findViewById(R.id.id_tree);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic() {
        setTopTitle("推广树形图");
        getData();
    }

    @Override
    protected void onClickEvent(View paramView) {

    }

    private void initDatas() {
        for (int i = 0; i < list.size(); i++) {
            mDatas2.add(new FileBean(i + 1, 0, list.get(i).Mobile + "\n" + list.get(i).RealName));
            if (!TextUtils.isEmpty(list.get(i).Children)) {
                list2 = JSON.parseArray(list.get(i).Children, UserRegisterBean.class);
                for (int j = 0; j < list2.size(); j++) {
                    mDatas2.add(new FileBean(j + 1 + i + 1, i + 1, list2.get(j).Mobile + "\n" + list2.get(j).RealName));
//                    if (!TextUtils.isEmpty(list2.get(i).Children)) {
//                        list3 = JSON.parseArray(list2.get(i).Children, UserRegisterBean.class);
//                        for (int n = 0; n < list3.size(); j++) {
//                            mDatas2.add(new FileBean(mDatas2.size(), j + 1 + i + 1, list3.get(i).RealName, list3.get(i).Mobile));
//                        }
//                    }
                }
            }
            try {
                mAdapter = new SimpleTreeAdapter<FileBean>(mTree, this, mDatas2, 10);

            } catch (Exception e) {
                e.printStackTrace();
            }
            mTree.setAdapter(mAdapter);
        }
    }


    /**
     * 获取用户数据
     */
    private void getData() {
        RequestParams params = new RequestParams();
        params.add("WToken", CheShiLiShopApplication.wtoken);
        params.add("StoreID", CheShiLiShopApplication.storeID);
        RestClient.post(UrlUtils.queryUserRegisterJson(), params, OrderTreeActivity.this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.d("三级分销", result);
                    String Status = jsonObject.getString("Status");
                    if ("0".equals(Status)) {
                        list = JSON.parseArray(jsonObject.getString("Data"), UserRegisterBean.class);
                        initDatas();
                    } else if ("-1".equals(Status)) {
                        Intent intent = new Intent(OrderTreeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.show(OrderTreeActivity.this, jsonObject.getString("Data"));
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
