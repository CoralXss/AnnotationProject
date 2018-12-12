package com.coral;

import android.app.Activity;
import android.view.View;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by xss on 2017/9/29.
 */

public class ViewBinderActualCallUtils {  // 类比 Butterknife

    // 默认声明一个Activity View查找器
    private static final ActivityViewFinder activityFinder = new ActivityViewFinder();
    private static final ItemViewFinder itemViewFinder = new ItemViewFinder();

    // 管理查找器Map
    private static final Map<String, ViewBinder> binderMap = new LinkedHashMap<>();

    public static void bind(Activity activity) {
        bind(activity, activity, activityFinder);
    }

    /**
     *  Butterknife 中的 target 只是用来当做 key，唯一区分一个类中的左右 @BindView 注解，并且放在同一个注解自动生成的文件中
     * @param target
     * @param view
     */
    public static void bind(Object target, View view) {
        bind(target, view, itemViewFinder);
    }

    /**
     * 注解绑定
     * @param host    注解View所在的类，也即 注解类
     * @param object  查找View的地方，Activity & View 可以自行查找，Fragment 则需要根据itemView查找
     * @param finder  UI 绑定提供接口者
     */
    private static void bind(Object host, Object object, ViewFinder finder) {
        String className = host.getClass().getName();

        ViewBinder binder = binderMap.get(className);
        if (binder == null) {
            try {
                Class<?> clazz = Class.forName(className + "$$ViewBinder");
                binder = (ViewBinder) clazz.newInstance();
                binderMap.put(className, binder);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (binder != null) {
            binder.bindView(host, object, finder);
        }
    }

    /**
     * 接触绑定 ActivityViewFinder
     * 从 Map 中移除
     * @param host
     */
    public void unBind(Object host) {
        String className = host.getClass().getName();
        ViewBinder binder = binderMap.get(className);
        if (binder != null) {
            binder.unBindView(host);
        }
        binderMap.remove(className);

    }

}
