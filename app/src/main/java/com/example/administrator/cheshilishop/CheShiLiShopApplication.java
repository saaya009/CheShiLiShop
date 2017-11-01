package com.example.administrator.cheshilishop;

import android.app.Activity;
import android.app.Application;
import android.content.Context;


import com.example.administrator.cheshilishop.bean.StoreBean;
import com.example.administrator.cheshilishop.bean.UserInfoBean;
import com.mob.MobApplication;
import com.orhanobut.hawk.Hawk;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ayase
 */
public class CheShiLiShopApplication extends Application {
    private List<Activity> activityList = new LinkedList<Activity>();
    private static CheShiLiShopApplication instance;
    public static String wtoken = "";
    public static String storeID = "";
    public static UserInfoBean user = new UserInfoBean();
    public static StoreBean store = new StoreBean();
    public static StoreBean newStore = new StoreBean();
    public static Context applicationContext;
    public static String AddTime = "1509362267";
    public static int pic_limit = 5;
    public static List<List<Boolean>> status;

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
