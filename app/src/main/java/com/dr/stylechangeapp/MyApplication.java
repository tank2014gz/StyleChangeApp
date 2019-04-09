package com.dr.stylechangeapp;

import android.app.Application;
import android.os.Environment;

import java.io.File;

/**
 * 项目名称：StyleChangeApp
 * 类描述：
 * 创建人：yuliyan
 * 创建时间：2019/4/9 10:14 PM
 * 修改人：yuliyan
 * 修改时间：2019/4/9 10:14 PM
 * 修改备注：
 */
public class MyApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        File file = new File(Environment.getExternalStorageDirectory(), "skin.apk");
        SkinManager.getInstance().laodSkinApk(file.getAbsolutePath());
    }
}
