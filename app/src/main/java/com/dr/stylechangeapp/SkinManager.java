package com.dr.stylechangeapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.lang.reflect.Method;

/**
 * 换肤管理器，用于加载皮肤插件apk的资源对象
 * 项目名称：StyleChangeApp
 * 类描述：
 * 创建人：yuliyan
 * 创建时间：2019/4/9 9:24 PM
 * 修改人：yuliyan
 * 修改时间：2019/4/9 9:24 PM
 * 修改备注：
 */

public class SkinManager {
    //外置app的资源对象
    private Resources resources;
    //上下文对象
    private Context context;
    
    //加载的皮肤的插件名
    private String skinPackge;
    
    
    private static SkinManager skinManager;
    
    private SkinManager() {
    
    }
    
    
    public static SkinManager getInstance() {
        if (skinManager == null) {
            synchronized (SkinManager.class) {
                if (skinManager == null) {
                    skinManager = new SkinManager();
                }
            }
        }
        
        return skinManager;
    }
    
    /**
     * 给Context赋值
     *
     * @param context
     */
    public void setContext(Context context) {
        this.context = context;
    }
    
    /**
     * 加载皮肤资源apk
     *
     * @param path
     */
    public void laodSkinApk(String path) {
        try {
            //获取到皮肤apk的包名
            PackageManager packageManager = context.getPackageManager();
            skinPackge = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES).packageName;
            //获取皮肤资源的文件对象（通过反射获取到AssetManger对象，提供低级别访问资源api 用来加载外置的资源包）
            AssetManager assetManager = AssetManager.class.newInstance();
            //通过反射技术获取到assetManager对象的addAssetPath的方法
            Method method = assetManager.getClass().getMethod("addAssetPath" ,String.class);
            //方法调用
            method.invoke(assetManager, path);
            //获取外置app的资源对象
            resources = new Resources(assetManager, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    /**
     * 从外置皮肤apk中获取到颜色的资源
     *
     * @return
     */
    public int getColor(int id) {
        if (resources == null) {
            return id;
        }
        //拿到资源的类型
        String resType = context.getResources().getResourceTypeName(id);
        //获取到资源的id名字
        String resName = context.getResources().getResourceName(id);
        //拿到要替换的皮肤控件的属性id
        int skinId = context.getResources().getIdentifier(resName, resType, skinPackge);
        if (skinId == 0) {
            return context.getResources().getColor(id);
        }
        return resources.getColor(skinId);
        
    }
}
