package com.example.administrator.cheshilishop.utils;

import android.os.Environment;


public class Config {
    public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String IMAGE_PATH = ROOT_PATH+"/brickuser/images/photo";
    public static final String IS_APP_FRIST_START = "isAppFirstStart";
}
