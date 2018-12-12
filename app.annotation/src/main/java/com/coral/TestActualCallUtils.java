package com.coral;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by xss on 2017/9/30.
 */

public class TestActualCallUtils {

    public static String getHelloMsg() {

        try {
            Class<?> clazz = Class.forName("com.songwenju.aptproject.HelloWorld");

            Method method = clazz.getMethod("getMsg", clazz);
//            clazz.newInstance()

            return (String) method.invoke(clazz.newInstance());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return "None";
    }
}
