package com.dr.stylechangeapp;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：StyleChangeApp
 * 类描述：
 * 创建人：yuliyan
 * 创建时间：2019/4/9 8:34 PM
 * 修改人：yuliyan
 * 修改时间：2019/4/9 8:34 PM
 * 修改备注：
 */
public class SkinFactory implements LayoutInflater.Factory2 {
    //搜集所有换肤的控件
    private List<SkinView> viewList = new ArrayList<>();
    
    //所有控件的前缀
    private static final String[] prxfixList = {
        "android.widget",
        "android.view",
        "android.webkit"
    };
    
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        //        Log.e("Dongran", name);
        //
        //        for (int i = 0; i < attrs.getAttributeCount(); i++) {
        //            String attrName = attrs.getAttributeName(i);
        //            String attrValue = attrs.getAttributeValue(i);
        //            Log.e("Dongran>>>", attrName + ":" + attrValue);
        //        }
        //搜集要进行换肤的View
        View view = null;
        //区分自定义控件和系统控件
        if (name.contains(".")) {
            view = onCreateView(name, context, attrs);
            
        } else {
            for (String s : prxfixList) {
                view = onCreateView(s + name, context, attrs);
                if (view != null) {
                    break;
                }
            }
        }
        
        //如何view不能为空，就搜集需要换肤的控件
        if (view != null) {
            parseView(view, name, attrs);
        }
        
        return view;
    }
    
    public void apply() {
        if (viewList.size() > 0) {
            for (SkinView item : viewList) {
                item.apply();
            }
        }
    }
    
    /**
     * 搜集需要换肤的控件的集合的方法
     *
     * @param view
     * @param name
     * @param attrs
     */
    private void parseView(View view, String name, AttributeSet attrs) {
        //单个控件的属性的对象的集合
        List<SkinItem> items = new ArrayList<>();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            //获取属性的名字
            String attrName = attrs.getAttributeName(i);
            //获取到的是属性的值，获取到的是16进制的id
            String attrValue = attrs.getAttributeValue(i);
            
            if (attrName.contains("backgroud")
                || attrName.contains("textColor")
                || attrName.contains("src")) {
                //获取到id
                int id = Integer.parseInt(attrValue.substring(1));
                //根据id获取属性的类型
                String typeName = view.getResources().getResourceTypeName(id);
                //根据id获取属性名字
                String entryName = view.getResources().getResourceEntryName(id);
                
                SkinItem skinItem = new SkinItem(attrName, id, entryName, typeName);
                items.add(skinItem);
                
            }
        }
        if (items != null && items.size() > 0) {
            SkinView skinView = new SkinView(view, items);
            viewList.add(skinView);
            skinView.apply();
        }
        
    }
    
    /**
     * 通过反射技术获取到view控件并且返回
     *
     * @param name
     * @param context
     * @param attrs
     * @return
     */
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        
        View view = null;
        try {
            //通过反射获取到控件的class对象
            Class clazz = context.getClassLoader().loadClass(name);
            //获取到构造方法
            Constructor<? extends View> viewContructor = clazz.getConstructor(new Class[]{Context.class, AttributeSet.class});
            //            view = viewContructor.newInstance()
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        
        return view;
    }
    
    /**
     * 单个需要换肤的控件的封装
     */
    
    class SkinItem {
        //属性的名字
        private String name;
        //属性资源文件的名字 @drawable colorPrimary
        private int resouceId;
        //属性资源的名字
        String entryName;
        //属性资源文件的类型
        String typeName;
        
        public SkinItem(String name, int resouceId, String entryName, String typeName) {
            this.name = name;
            this.resouceId = resouceId;
            this.entryName = entryName;
            this.typeName = typeName;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public int getResouceId() {
            return resouceId;
        }
        
        public void setResouceId(int resouceId) {
            this.resouceId = resouceId;
        }
        
        public String getEntryName() {
            return entryName;
        }
        
        public void setEntryName(String entryName) {
            this.entryName = entryName;
        }
        
        public String getTypeName() {
            return typeName;
        }
        
        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }
    }
    
    
    /**
     * 对单个需要换肤控件的封装
     */
    
    class SkinView {
        //控件本身的对象
        View view;
        List<SkinItem> list;
        
        public SkinView(View view, List<SkinItem> ites) {
            this.view = view;
            this.list = ites;
        }
        
        /**
         * 给单个控件换肤
         */
        public void apply() {
            for (SkinItem skinItem : list) {
                if (skinItem.getName().equals("background")) {
                    
                    if (skinItem.typeName.equals("color")) {
                        view.setBackgroundColor(SkinManager.getInstance().getColor(skinItem.resouceId));
                    } else if (skinItem.typeName.equals("drawable")) {
                        //                        view.setBackgroundDrawable(SkinManager.getInstance());
                    }
                    
                    
                } else if (skinItem.getName().equals("textColor")) {
                
                }
            }
        }
    }
    
    
}
