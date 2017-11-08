package com.example.administrator.cheshilishop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.activity.FuWuDataActivity;
import com.example.administrator.cheshilishop.activity.MainActivity;
import com.example.administrator.cheshilishop.adapter.ActivityAdapter;
import com.example.administrator.cheshilishop.adapter.FuWuAdapter;
import com.example.administrator.cheshilishop.bean.ActivityBean;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * 服务管理-全部
 */
public class ActivityOneFragment extends Fragment {

    private ListView lv_Booking;
    private LinearLayout layout_null;
    private Button btn_null;

    private int page = 1;
    private int size = 10;
    private ActivityAdapter adapter;
    private List<ActivityBean> list = new ArrayList<ActivityBean>();
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
        btn_null = view.findViewById(R.id.btn_null);
        btn_null.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        lv_Booking.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> view, View view1, int i, long l) {
                Intent intent = new Intent(getActivity(), FuWuDataActivity.class);
                intent.putExtra("ServiceID",list.get(i).ID);
                intent.putExtra("type",9);
                startActivity(intent);
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
    private void getData(final int page) {
        final RequestParams params = new RequestParams();
        params.add("N",page+"");
        params.add("Rows",size+"");
        RestClient.post(UrlUtils.queryCampaignList(), params,getActivity(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.d("活动管理", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if ("0".equals(Status)){
                        JSONObject data = jsonObject.getJSONObject("Data");
                        String rows = data.getString("Rows");
                        Log.d("Rows", rows);
                        List<ActivityBean> mList = com.alibaba.fastjson.JSONObject.parseArray(rows, ActivityBean.class);
                        if (page == 1) {
                            if (mList.size() == 0) {
                                layout_null.setVisibility(View.VISIBLE);
                                refreshLayout.setVisibility(View.GONE);
                            } else {
                                layout_null.setVisibility(View.GONE);
                                refreshLayout.setVisibility(View.VISIBLE);
                                list.clear();
                                list.addAll(mList);
                                adapter = new ActivityAdapter(getActivity(), list);
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
                RequestParams errParams = new RequestParams();
                try {
                    errParams.add("LogCont", URLEncoder.encode(new String(responseBody),"UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                errParams.add("Url",UrlUtils.queryServiceAppointDetail());
                errParams.add("PostData",params.toString());
                errParams.add("WToken",CheShiLiShopApplication.wtoken);
                RestClient.post(UrlUtils.insertErrLog(), errParams, getActivity(), new AsyncHttpResponseHandler() {
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
    public void onResume() {
        getData(page);
        super.onResume();
    }
}
