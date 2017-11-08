package com.example.administrator.cheshilishop.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;
import com.example.administrator.cheshilishop.adapter.BookingAdapter;
import com.example.administrator.cheshilishop.fragment.ActivityOneFragment;
import com.example.administrator.cheshilishop.fragment.ActivityTwoFragment;
import com.example.administrator.cheshilishop.fragment.ServericeAllFragment;
import com.example.administrator.cheshilishop.fragment.ServericeEndFragment;
import com.example.administrator.cheshilishop.fragment.ServericeVerifiedFragment;
import com.example.administrator.cheshilishop.widget.TopbarIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 作者：Ayase on 2017/11/8 10:14
 * 邮箱：ayase@ayase.cn
 */

public class ActivityManagementActivity extends BaseActivity {
    private TopbarIndicator ti_indicator;
    private ViewPager vp_financial;
    private List<String> type = Arrays.asList("活动列表","我的活动");
    private BookingAdapter adapter;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private ActivityOneFragment oneFragment;
    private ActivityTwoFragment twoFragment;

    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bookingmanagement);
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
        ti_indicator = (TopbarIndicator) findViewById(R.id.ti_indicator);
        vp_financial = (ViewPager) findViewById(R.id.vp_financial);
    }

    @Override
    protected void setListener() {
        topbar_iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityManagementActivity.this,FuWuActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void processLogic() {
        topbar_tv_title.setText("活动管理");
        Resources resource = getResources();
        ColorStateList csl = resource.getColorStateList(R.color.colorPrimaryDark);
        topbar_iv_right.setTextColor(csl);
        oneFragment = new ActivityOneFragment();
        twoFragment = new ActivityTwoFragment();
        mFragmentList.add(oneFragment);
        mFragmentList.add(twoFragment);

        adapter = new BookingAdapter(this.getSupportFragmentManager(), mFragmentList);
        vp_financial.setAdapter(adapter);
        vp_financial.setCurrentItem(0);

        ti_indicator.setVisibleTabCount(2);
        ti_indicator.setTitles(type);
        ti_indicator.setViewpager(vp_financial);
    }

    @Override
    protected void onClickEvent(View paramView) {

    }
}
