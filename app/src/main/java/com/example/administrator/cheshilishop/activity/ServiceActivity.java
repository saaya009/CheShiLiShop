package com.example.administrator.cheshilishop.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;
import com.example.administrator.cheshilishop.adapter.MainSectionedAdapter;
import com.example.administrator.cheshilishop.adapter.ProductLeftListAdapter;
import com.example.administrator.cheshilishop.bean.ProductBean;
import com.example.administrator.cheshilishop.net.RestClient;
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
 * 服务管理
 * 作者：Ayase on 2017/8/7 14:30
 * 邮箱：ayase@ayase.cn
 */
public class ServiceActivity extends BaseActivity {


    @BindView(R.id.left_listview)
    ListView leftListview;
    @BindView(R.id.pinnedListView)
    PinnedHeaderListView pinnedListView;
    @BindView(R.id.layout_next)
    LinearLayout mLayoutNext;

    private boolean isScroll = true;
    private ProductLeftListAdapter adapter;
    private MainSectionedAdapter sectionedAdapter;

    private String[] leftStr;
    private boolean[] flagArray = {true, false, false, false, false, false, false, false, false};
    private List<ProductBean> mList;
    private List<String> list;
    private List<List<ProductBean>> lists;

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

    }

    @Override
    protected void setListener() {
        pinnedListView = findViewById(R.id.pinnedListView);


    }

    @Override
    protected void processLogic() {
        mLayoutNext.setVisibility(View.GONE);
        setTopTitle("服务管理");
        topbar_iv_right.setVisibility(View.VISIBLE);
        topbar_iv_right.setText("添加服务");
        Resources resource = getResources();
        ColorStateList csl = resource.getColorStateList(R.color.colorPrimaryDark);
        topbar_iv_right.setTextColor(csl);
        getData();
    }

    @Override
    protected void onClickEvent(View paramView) {
        switch (paramView.getId()) {
            case R.id.topbar_iv_right://添加服务
                Intent intent = new Intent(this, AddServiceActivity.class);
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
        params.add("StoreID", CheShiLiShopApplication.storeID);
        RestClient.post(UrlUtils.queryServiceList(), params, this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    list = new ArrayList<String>();
                    JSONObject jsonObject = new JSONObject(result);
                    Log.d("服务", result);
                    String status = jsonObject.getString("Status");
                    List<String> rlist = new ArrayList<String>();
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

                        sectionedAdapter = new MainSectionedAdapter(ServiceActivity.this, leftStr, lists);
                        pinnedListView.setAdapter(sectionedAdapter);
                        adapter = new ProductLeftListAdapter(ServiceActivity.this, leftStr, flagArray);
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
                        Intent intent = new Intent(ServiceActivity.this, LoginActivity.class);
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
                errParams.add("Url", UrlUtils.queryServiceAppointDetail());
                errParams.add("PostData", params.toString());
                errParams.add("WToken", CheShiLiShopApplication.wtoken);
                RestClient.post(UrlUtils.insertErrLog(), errParams, ServiceActivity.this, new AsyncHttpResponseHandler() {
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

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}
