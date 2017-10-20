package com.example.administrator.cheshilishop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;
import com.example.administrator.cheshilishop.adapter.MainSectioned2Adapter;
import com.example.administrator.cheshilishop.adapter.ProductLeftListAdapter;
import com.example.administrator.cheshilishop.bean.ProductBean;
import com.example.administrator.cheshilishop.net.RestClient;
import com.example.administrator.cheshilishop.utils.Log;
import com.example.administrator.cheshilishop.utils.LogUtil;
import com.example.administrator.cheshilishop.utils.ToastUtils;
import com.example.administrator.cheshilishop.utils.UrlUtils;
import com.example.administrator.cheshilishop.widget.PinnedHeaderListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 添加服务
 * 作者：Ayase on 2017/9/14 14:30
 * 邮箱：ayase@ayase.cn
 */
public class AddServiceActivity extends BaseActivity {


    @BindView(R.id.left_listview)
    ListView leftListview;
    @BindView(R.id.pinnedListView)
    PinnedHeaderListView pinnedListView;
    @BindView(R.id.btn_seva)
    Button mBtnSeva;
    @BindView(R.id.layout_next)
    LinearLayout mLayoutNext;

    private boolean isScroll = true;
    private ProductLeftListAdapter adapter;
    private MainSectioned2Adapter sectionedAdapter;
    private List<List<Boolean>> originalFoundedDevicesState; //有一个保存状态的list

    private String[] leftStr;
    private boolean[] flagArray = {true, false, false, false, false, false, false, false, false};
    private List<ProductBean> mList;
    private List<String> list;
    private List<List<ProductBean>> lists;
    private String productIDs = "";

    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_product);
        ButterKnife.bind(this);
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
        mBtnSeva.setOnClickListener(this);
    }

    @Override
    protected void setListener() {
        pinnedListView = findViewById(R.id.pinnedListView);
    }

    @Override
    protected void processLogic() {
        setTopTitle("请选择添加服务");
        mLayoutNext.setVisibility(View.VISIBLE);
        getData();
    }

    @Override
    protected void onClickEvent(View paramView) {
        switch (paramView.getId()) {
            case R.id.btn_seva://保存
                for (int i = 0; i < CheShiLiShopApplication.status.size(); i++) {
                    for (int j = 0; j < CheShiLiShopApplication.status.get(i).size(); j++) {
                        if (CheShiLiShopApplication.status.get(i).get(j)) {
                            productIDs = productIDs + lists.get(i).get(j).ID + ",";
                            Log.d("保存添加", productIDs);
                        }
                    }
                }
                addService();
                break;
        }
    }

    /**
     * 批量添加服务
     */
    private void addService() {
        final RequestParams params = new RequestParams();
        params.add("WToken", CheShiLiShopApplication.wtoken);
        params.add("StoreID", CheShiLiShopApplication.storeID);
        if (productIDs.length() == 0){
            ToastUtils.show(AddServiceActivity.this,"请选择服务");
            return;
        }
        productIDs = productIDs.substring(0, productIDs.length() - 1);
        params.add("ProductIDs", productIDs);
        RestClient.post(UrlUtils.addServices(), params, this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    android.util.Log.d("添加服务", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if ("0".equals(Status)) {
                        ToastUtils.show(AddServiceActivity.this, "添加服务成功");
                        finish();
                    } else if ("-1".equals(Status)) {
                        Intent intent = new Intent(AddServiceActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.show(AddServiceActivity.this, jsonObject.getString("Data"));
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
                errParams.add("Url", UrlUtils.addServices());
                errParams.add("PostData", params.toString());
                errParams.add("WToken", CheShiLiShopApplication.wtoken);
                RestClient.post(UrlUtils.insertErrLog(), errParams, AddServiceActivity.this, new AsyncHttpResponseHandler() {
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
     * 获取数据
     */
    private void getData() {
        RestClient.get(UrlUtils.queryProductServiceJson(), this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    list = new ArrayList<String>();
                    JSONObject jsonObject = new JSONObject(result);
                    LogUtil.d("服务", result);
                    String status = jsonObject.getString("Status");
                    final List<String> rlist = new ArrayList<String>();
                    if ("0".equals(status)) {
                        lists = new ArrayList<List<ProductBean>>();
                        JSONArray data = jsonObject.getJSONArray("Data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            rlist.add(object.getString("CategoryName"));
                        }
                        leftStr = new String[rlist.size()];
                        rlist.toArray(leftStr);

                        for (int i = 0; i < leftStr.length; i++) {
                            JSONObject object2 = data.getJSONObject(i);
                            List<ProductBean> beanList = new ArrayList<ProductBean>();
                            JSONArray Children = object2.getJSONArray("Children");
                            for (int j = 0; j < Children.length(); j++) {
                                String product = Children.getString(j);
                                Log.d("服务2", product);
                                ProductBean productBean = JSON.parseObject(product, ProductBean.class);
                                beanList.add(productBean);
                            }
                            lists.add(beanList);
                        }

                        originalFoundedDevicesState = new ArrayList<List<Boolean>>();
                        for (int i = 0; i < lists.size(); i++) {
                            List<Boolean> flag = new ArrayList<Boolean>();
                            for (int j = 0; j < lists.get(i).size(); j++) {
                                flag.add(j, false);
                            }
                            originalFoundedDevicesState.add(i, flag);
                        }
                        sectionedAdapter = new MainSectioned2Adapter(AddServiceActivity.this, leftStr, lists, originalFoundedDevicesState);
                        pinnedListView.setAdapter(sectionedAdapter);
                        pinnedListView.setItemsCanFocus(false);
                        pinnedListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


                        adapter = new ProductLeftListAdapter(AddServiceActivity.this, leftStr, flagArray);
                        leftListview.setAdapter(adapter);
                        leftListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                                isScroll = false;

                                for (int i = 0; i < leftStr.length; i++) {
                                    if (i == position) {
                                        flagArray[i] = true;
                                    } else {
                                        flagArray[i] = false;
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                int rightSection = 0;
                                for (int i = 0; i < position; i++) {
                                    rightSection += sectionedAdapter.getCountForSection(i) + 1;
                                }
                                pinnedListView.setSelection(rightSection);

                            }

                        });

                        pinnedListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(AbsListView arg0, int scrollState) {
                                switch (scrollState) {
                                    // 当不滚动时
                                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                                        // 判断滚动到底部
                                        if (pinnedListView.getLastVisiblePosition() == (pinnedListView.getCount() - 1)) {
                                            leftListview.setSelection(ListView.FOCUS_DOWN);
                                        }

                                        // 判断滚动到顶部
                                        if (pinnedListView.getFirstVisiblePosition() == 0) {
                                            leftListview.setSelection(0);
                                        }

                                        break;
                                }
                            }

                            int y = 0;
                            int x = 0;
                            int z = 0;

                            @Override
                            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                if (isScroll) {
                                    for (int i = 0; i < lists.size(); i++) {
                                        if (i == sectionedAdapter.getSectionForPosition(pinnedListView.getFirstVisiblePosition())) {
                                            flagArray[i] = true;
                                            x = i;
                                        } else {
                                            flagArray[i] = false;
                                        }
                                    }
                                    if (x != y) {
                                        adapter.notifyDataSetChanged();
                                        y = x;
                                        //左侧ListView滚动到最后位置
                                        if (y == leftListview.getLastVisiblePosition()) {
                                            leftListview.setSelection(z);
                                        }
                                        //左侧ListView滚动到第一个位置
                                        if (x == leftListview.getFirstVisiblePosition()) {
                                            leftListview.setSelection(z);
                                        }
                                        if (firstVisibleItem + visibleItemCount == totalItemCount - 1) {
                                            leftListview.setSelection(ListView.FOCUS_DOWN);
                                        }
                                    }
                                } else {
                                    isScroll = true;
                                }
                            }

                        });
                    } else if ("-1".equals(status)) {
                        Intent intent = new Intent(AddServiceActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
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
                errParams.add("Url", UrlUtils.queryProductServiceJson());
                errParams.add("WToken", CheShiLiShopApplication.wtoken);
                RestClient.post(UrlUtils.insertErrLog(), errParams, AddServiceActivity.this, new AsyncHttpResponseHandler() {
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
     * 去除数组里面相同的元素
     *
     * @param list
     * @return
     */
    public static List<String> removeDuplicate(List<String> list) {
        HashSet<String> hashSet = new HashSet<String>(list);
        list.clear();
        list.addAll(hashSet);
        return list;
    }

}
