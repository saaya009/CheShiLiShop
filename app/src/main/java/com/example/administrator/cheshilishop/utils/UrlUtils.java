package com.example.administrator.cheshilishop.utils;

import android.net.Uri;

/**
 * Created by Administrator on 2017/7/26.
 * 请求地址拼接
 */

public class UrlUtils {
    public static String BASE_URL = "http://api.cheshili.com.cn:8000";

    /**
     * 登录
     */
    public static String login() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/CSL/Login/LG").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 注册
     */
    public static String reg() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/CSL/Login/RegisterUser").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 获取验证码
     */
    public static String getCode() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/Product/Info/SendVerifiedCode").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 找回密码
     */
    public static String getPwd() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/CSL/Login/ResetPwdByPhone").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 上传图片
     */
    public static String UploaImg() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/LeagueInfo/UpdateImg").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 获取OrderString
     */
    public static String orderString(String json) {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/CSL/A_Pay/GetOrderString").buildUpon()
                .appendEncodedPath(json);
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 获取我的成功返回页面
     */
    public static String queryOrder(String json) {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/CSL/A_Pay/QueryOrder").buildUpon()
                .appendEncodedPath(json);
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 查询服务预约List
     */
    public static String queryServiceAppointList() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/StoreService/QueryServiceAppointList").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 获取用户数据
     */
    public static String getInfoByToken() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/LeagueInfo/GetInfoByToken").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }
}
