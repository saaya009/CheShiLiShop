package com.example.administrator.cheshilishop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;
import com.example.administrator.cheshilishop.bean.ActivityBean;
import com.example.administrator.cheshilishop.bean.UserInfoBean;
import com.example.administrator.cheshilishop.bean.tree.MyActivityBean;
import com.example.administrator.cheshilishop.net.RestClient;
import com.example.administrator.cheshilishop.utils.DateUtil;
import com.example.administrator.cheshilishop.utils.ToastUtils;
import com.example.administrator.cheshilishop.utils.UrlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 作者：Ayase on 2017/11/8 11:04
 * 邮箱：ayase@ayase.cn
 */

public class ActivityDataActivity extends BaseActivity {

    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_type)
    TextView mTvType;
    @BindView(R.id.tv_starttime)
    TextView mTvStarttime;
    @BindView(R.id.tv_endtime)
    TextView mTvEndtime;
    @BindView(R.id.tv_descri)
    TextView mTvDescri;

    private MyActivityBean activity;

    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_data);
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

    }

    @Override
    protected void processLogic() {
        setTopTitle("活动详情");
        activity = (MyActivityBean) getIntent().getSerializableExtra("activity");
        mTvName.setText(activity.CampaignName);
        switch (activity.AppTypeID) {
            case "1":
                mTvType.setText("微信");
                break;
            default:
                mTvType.setText("全部");
                break;
        }
        mTvStarttime.setText(DateUtil.stampToDate6(activity.AddTime));
        mTvEndtime.setText(DateUtil.stampToDate6(activity.EndTime));
        mTvDescri.setText(activity.Descri);
    }

    @Override
    protected void onClickEvent(View paramView) {

    }

}
