package com.example.administrator.cheshilishop.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Administrator on 2017/7/26.
 */

public class HttpUtils {

    /**
     * post的方式请求
     */
    public static String loginByPost(String path, String data) {
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
//            //至少要设置的两个请求头
//            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            connection.setRequestProperty("Content-Length", data.length() + "");

            //post的方式提交实际上是留的方式提交给服务器
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data.getBytes());

            //获得结果码
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                //请求成功
                InputStream is = connection.getInputStream();
                return IoUtil.inputStream2String(is);
            } else {
                //请求失败
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
