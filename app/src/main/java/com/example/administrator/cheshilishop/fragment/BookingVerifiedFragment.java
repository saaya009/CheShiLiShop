package com.example.administrator.cheshilishop.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.adapter.BookingAllAdapter;
import com.example.administrator.cheshilishop.bean.BookingBean;
import com.example.administrator.cheshilishop.net.RestClient;
import com.example.administrator.cheshilishop.utils.ToastUtils;
import com.example.administrator.cheshilishop.utils.UrlUtils;
import com.example.administrator.cheshilishop.widget.refresh.MaterialRefreshLayout;
import com.example.administrator.cheshilishop.widget.refresh.MaterialRefreshListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class BookingVerifiedFragment extends Fragment {

    private ListView lv_Booking;
    private LinearLayout layout_null;

    private int page = 1;
    private int size = 10;
    private BookingAllAdapter adapter;
    private List<BookingBean> list = new ArrayList<BookingBean>();
    private MaterialRefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookingall, container, false);
        initView(view);
        getData(page);
        return view;
    }

    private void initView(View view) {
        lv_Booking = (ListView) view.findViewById(R.id.lv_Booking);
        refreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.mrl_experience);;
        layout_null = (LinearLayout) view.findViewById(R.id.layout_null);
        lv_Booking.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> view, View view1, int i, long l) {

            }
        });

        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                page = 1;
                list.clear();
                getData(page);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                page++;
                getData(page);
            }
        });
    }

    /**
     * 获取用户数据
     * @param page
     */
    private void getData(final int page) {;
        RequestParams params = new RequestParams();
        params.add("WToken", CheShiLiShopApplication.wtoken);
        params.add("StoreID","108");
        params.add("N",page+"");
        params.add("Rows",size+"");
        params.add("Status", "4");
        RestClient.post(UrlUtils.queryServiceAppointList(), params,getActivity(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    Log.d("刷新", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if ("0".equals(Status)){
                        JSONObject data = jsonObject.getJSONObject("Data");
                        String rows = data.getString("Rows");
                        List<BookingBean> mList = com.alibaba.fastjson.JSONObject.parseArray(rows, BookingBean.class);
                        if (page == 1) {
                            if (mList.size() == 0) {
                                layout_null.setVisibility(View.VISIBLE);
                                refreshLayout.setVisibility(View.GONE);
                            } else {
                                layout_null.setVisibility(View.GONE);
                                refreshLayout.setVisibility(View.VISIBLE);
                                list.clear();
                                list.addAll(mList);
                                adapter = new BookingAllAdapter(getActivity(), list,1);
                                lv_Booking.setAdapter(adapter);
                                refreshLayout.finishRefresh();
                            }
                        } else {
                            if (mList.size() == 0) {
                                ToastUtils.show(getActivity(), "已加载完全部数据");
                            } else {
                                list.addAll(mList);
                                adapter.notifyDataSetChanged();
                            }
                            refreshLayout.finishRefreshLoadMore();
                        }
                    }else {
                        ToastUtils.show(getActivity(),jsonObject.getString("Data"));
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