package com.example.administrator.cheshilishop.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 作者：Ayase on 2017/8/7 14:30
 * 邮箱：ayase@ayase.cn
 */
public class ProductActivity extends BaseActivity {


    @BindView(R.id.left_listview)
    ListView leftListview;
    @BindView(R.id.pinnedListView)
    PinnedHeaderListView pinnedListView;

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
        return new TopView(topbar_iv_back, topbar_tv_title);
    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void setListener() {
        pinnedListView = (PinnedHeaderListView) findViewById(R.id.pinnedListView);


    }

    @Override
    protected void processLogic() {
        getData();
    }

    @Override
    protected void onClickEvent(View paramView) {

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
        RequestParams params = new RequestParams();
        params.add("WToken", CheShiLiShopApplication.wtoken);
        params.add("Rows", "1000");
        params.add("StoreID", CheShiLiShopApplication.storeID);
        RestClient.post(UrlUtils.queryServiceList(), params, this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    list = new ArrayList<String>();
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("Status");
                    List<String> rlist = new ArrayList<String>();
                    if ("0".equals(status)) {
                        org.json.JSONArray data = jsonObject.getJSONArray("Data");
                        for (int i = 0;i<data.length();i++){
                            JSONObject object = data.getJSONObject(i);
                            rlist.add(object.getString("CategoryName"));
                        }
                        leftStr = new String[rlist.size()];
                        rlist.toArray(leftStr);

                        List<ProductBean> beanList = null;
                        for (int i = 0;i<leftStr.length;i++){
                            JSONObject object = data.getJSONObject(i);
                            beanList = new ArrayList<ProductBean>();
                            org.json.JSONArray Children = object.getJSONArray("Children");
                            for (int j = 0;j<Children.length();j++){

                            }
                            lists.add(beanList);
                        }
//                        rightStr = new String[lists.size()][beanList.size()];
//                        lists.toArray(rightStr);

                        sectionedAdapter = new MainSectionedAdapter(ProductActivity.this, leftStr, lists);
                        pinnedListView.setAdapter(sectionedAdapter);
                        adapter = new ProductLeftListAdapter(ProductActivity.this, leftStr, flagArray);
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
     * 去除数组里面相同的元素
     * @param list
     * @return
     */
    public static List<String> removeDuplicate(List<String> list){
        HashSet<String> hashSet = new HashSet<String>(list);
        list.clear();
        list.addAll(hashSet);
        return list;
    }
}
