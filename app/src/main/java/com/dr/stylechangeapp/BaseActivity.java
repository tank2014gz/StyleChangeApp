package com.dr.stylechangeapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;

/**
 * 项目名称：StyleChangeApp
 * 类描述：
 * 创建人：yuliyan
 * 创建时间：2019/4/9 8:33 PM
 * 修改人：yuliyan
 * 修改时间：2019/4/9 8:33 PM
 * 修改备注：
 */
public class BaseActivity extends Activity {
    
    SkinFactory skinFactory = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SkinManager.getInstance().setContext(this);
        skinFactory = new SkinFactory();
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), skinFactory);
    }
    
    
    public void changeSkin() {
        skinFactory.apply();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        changeSkin();
    }
}
