package com.example.administrator.cheshilishop.utils;

import android.util.Base64;

/**
 * 作者：Ayase on 2017/8/23 15:44
 * 邮箱：ayase@ayase.cn
 */

public class InviteUtils {

    public static String getInviteCode(String id){
        int num = id.length()%3;
        switch (num){
            case 0:
                break;
            case 1:
                id = "00"+id;
                 break;
            case 2:
                id = "0"+id;
                break;

        }
        id = Base64.encodeToString(id.getBytes(),Base64.DEFAULT);
        return id;
    }
}
