package com.example.administrator.cheshilishop.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;
import com.example.administrator.cheshilishop.utils.Config;
import com.example.administrator.cheshilishop.utils.PreferenceUtil;
import com.orhanobut.hawk.Hawk;
import com.yanzhenjie.permission.AndPermission;

/**
 * 引导页
 */
public class SplashActivity extends BaseActivity {

    private ViewPager mViewPager;
    /**
     * 引导页图片数组
     */
    private int[] views_id = new int[]{R.mipmap.page_welcome_1, R.mipmap.page_welcome_2, R.mipmap.page_welcome_3};
    private View[] views;
    private WelcomeViewPageAdapter mWelcomeViewPageAdapter;
    private MyHandler mMyHandler;
    private ImageView image_splash;

    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);
    }


    @Override
    protected BaseActivity getAct() {
        return this;
    }

    @Override
    protected TopView getTopViews() {
        return null;
    }


    @Override
    protected void findViewById() {
        mViewPager = findViewById(R.id.vp_page_welcome);
        image_splash = findViewById(R.id.image_splash);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic() {
        boolean isAppFirstStart = PreferenceUtil.getBoolean(Config.IS_APP_FRIST_START, true);
        if (isAppFirstStart) {//第一次启动APP 跳转至欢迎页面
            mViewPager.setVisibility(View.VISIBLE);
            PreferenceUtil.putBoolean(Config.IS_APP_FRIST_START, false);
            views = new View[views_id.length];
            for (int i = 0; i < views_id.length; i++) {
                View view = new View(this);
                ViewPager.LayoutParams params = new ViewPager.LayoutParams();
                params.width = ViewPager.LayoutParams.MATCH_PARENT;
                params.height = ViewPager.LayoutParams.MATCH_PARENT;
                view.setLayoutParams(params);
                view.setBackgroundResource(views_id[i]);
                views[i] = view;
            }
            mWelcomeViewPageAdapter = new WelcomeViewPageAdapter(views);
            mViewPager.setAdapter(mWelcomeViewPageAdapter);
            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (position == 2) {
                        views[position].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                startActivity(intent);
                                SplashActivity.this.finish();
                            }
                        });
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else {
            Log.d("启动","123");
            image_splash.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.GONE);
            mMyHandler = new MyHandler();
            mMyHandler.sendEmptyMessageDelayed(0, 2500);//启动页展示毫秒后跳转首页

//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
//            finish();
        }
    }

    @Override
    protected void onClickEvent(View paramView) {

    }


    /**
     * 引导页 adapter
     */
    class WelcomeViewPageAdapter extends PagerAdapter {
        private View[] mViews;

        public WelcomeViewPageAdapter(View[] views) {
            this.mViews = views;
        }

        @Override
        public int getCount() {
            return mViews.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViews[position]);
            return mViews[position];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViews[position]);
        }
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        }
    }

}

