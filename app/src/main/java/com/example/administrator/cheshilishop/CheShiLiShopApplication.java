package com.example.administrator.cheshilishop;

import android.app.Activity;
import android.content.Context;


import com.example.administrator.cheshilishop.bean.UserInfoBean;
import com.mob.MobApplication;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ayase
 */
public class CheShiLiShopApplication extends MobApplication {
    private List<Activity> activityList = new LinkedList<Activity>();
    private static CheShiLiShopApplication instance;
    public static String wtoken = "";
    public static String url = "";
    public static UserInfoBean user = new UserInfoBean();
    public static Context applicationContext;
    public static int pic_limit = 5;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        applicationContext = this;
    }

    public static CheShiLiShopApplication getInstance() {
        return instance;
    }
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }
    public void exitActivity() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }
}