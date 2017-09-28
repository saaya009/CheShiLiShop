package com.example.administrator.cheshilishop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;
import com.example.administrator.cheshilishop.adapter.SetStoreAdapter;
import com.example.administrator.cheshilishop.bean.StoreBean;
import com.example.administrator.cheshilishop.net.RestClient;
import com.example.administrator.cheshilishop.utils.ToastUtils;
import com.example.administrator.cheshilishop.utils.UrlUtils;
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
 * 作者：Ayase on 2017/8/8 15:38
 * 邮箱：ayase@ayase.cn
 */

public class SetStoreActivity extends BaseActivity {


    @BindView(R.id.lv_store)
    ListView lv_store;

    private List<StoreBean> list;
    private SetStoreAdapter adapter;

    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setstore);
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

    }

    @Override
    protected void setListener() {
        lv_store.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (list.get(i).Approved.equals("1")){
                    updateDefaultStore(list.get(i).ID);
                }else if (list.get(i).Approved.equals("2")){
                    Intent intent = new Intent(SetStoreActivity.this,ExamineActivity.class);
                    intent.putExtra("id",list.get(i).ID);
                    startActivity(intent);
                }

            }
        });
        topbar_iv_right.setOnClickListener(this);

    }

    @Override
    protected void processLogic() {
        setTopTitle("切换店面");
        topbar_iv_right.setText("添加店面");
        getData();
    }

    @Override
    protected void onClickEvent(View paramView) {
        switch (paramView.getId()){
            case R.id.topbar_iv_right://添加店面
                Intent intent = new Intent(this,AuthenticationActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    /**
     * 获取数据
     */
    private void getData() {
        final RequestParams params = new RequestParams();
        params.add("WToken", CheShiLiShopApplication.wtoken);
        params.add("Enable", "-1");
        params.add("Approved", "-1");
        RestClient.post(UrlUtils.queryStoreList(), params, this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    Log.d("更改店铺",result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if ("0".equals(Status)) {
                        JSONObject data = jsonObject.getJSONObject("Data");
                        String rows = data.getString("Rows");
                        list = new ArrayList<StoreBean>();
                        list = JSON.parseArray(rows,StoreBean.class);
                        adapter = new SetStoreAdapter(SetStoreActivity.this,list);
                        lv_store.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }else if ("-1".equals(Status)){
                        Intent intent = new Intent(SetStoreActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.show(SetStoreActivity.this, jsonObject.getString("Data"));
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
                errParams.add("Url",UrlUtils.queryServiceAppointDetail());
                errParams.add("PostData",params.toString());
                errParams.add("WToken",CheShiLiShopApplication.wtoken);
                RestClient.post(UrlUtils.insertErrLog(), errParams, SetStoreActivity.this, new AsyncHttpResponseHandler() {
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
     * 更改默认
     */
    private void updateDefaultStore(final String id) {
        final RequestParams params = new RequestParams();
        params.add("WToken", CheShiLiShopApplication.wtoken);
        params.add("StoreID",id);
        RestClient.post(UrlUtils.updateDefaultStore(), params, this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if ("0".equals(Status)) {
                        ToastUtils.show(SetStoreActivity.this,"更改成功");
                        CheShiLiShopApplication.storeID = id;
                        getData();
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                RequestParams errParams = new RequestParams();
                errParams.add("LogCont",new String(responseBody));
                errParams.add("Url",UrlUtils.queryServiceAppointDetail());
                errParams.add("PostData",params.toString());
                errParams.add("WToken",CheShiLiShopApplication.wtoken);
                RestClient.post(UrlUtils.insertErrLog(), errParams, SetStoreActivity.this, new AsyncHttpResponseHandler() {
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
    protected void onResume() {
        super.onResume();
        getData();
    }
}
