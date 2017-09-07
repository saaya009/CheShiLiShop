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

import com.alibaba.fastjson.JSON;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.activity.LoginActivity;
import com.example.administrator.cheshilishop.activity.MainActivity;
import com.example.administrator.cheshilishop.adapter.OrderAdapter;
import com.example.administrator.cheshilishop.bean.UserRegisterBean;
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

import cz.msebera.android.httpclient.Header;

/**
 * 2级代理
 */
public class OrderTwoFragment extends Fragment {

    private ListView lv_order;
    private LinearLayout layout_null;
    private Button btn_null;

    private OrderAdapter adapter;

    private List<UserRegisterBean> list = new ArrayList<UserRegisterBean>();
    private List<UserRegisterBean> list2 = new ArrayList<UserRegisterBean>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        initView(view);
        getData();
        return view;
    }


    private void initView(View view) {
        lv_order = (ListView) view.findViewById(R.id.lv_order);
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
        lv_order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> view, View view1, int i, long l) {

            }
        });

    }

    /**
     * 获取用户数据
     */
    private void getData() {
        final RequestParams params = new RequestParams();
        params.add("WToken", CheShiLiShopApplication.wtoken);
        params.add("StoreID", CheShiLiShopApplication.storeID);
        RestClient.post(UrlUtils.queryUserRegisterJson(), params, getActivity(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.d("三级分销2", result);
                    String Status = jsonObject.getString("Status");
                    if ("0".equals(Status)) {
                        list = JSON.parseArray(jsonObject.getString("Data"), UserRegisterBean.class);
                        for (UserRegisterBean user : list) {
                            list2.addAll(JSON.parseArray(user.Children, UserRegisterBean.class));
                        }
                        adapter = new OrderAdapter(getActivity(), list2);
                        lv_order.setAdapter(adapter);
                    } else if ("-1".equals(Status)) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        ToastUtils.show(getActivity(), jsonObject.getString("Data"));
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
}
