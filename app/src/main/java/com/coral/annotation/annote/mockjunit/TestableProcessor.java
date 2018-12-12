package com.coral.annotation.annote.mockjunit;

import com.coral.annotation.annote.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by xss on 2017/9/29.
 * desc: 注解处理器，让程序中的注解起作用
 */

public class TestableProcessor {

    public static void process(String clazz) {
        int passed = 0;
        int failed = 0;
        try {
            Object obj = Class.forName(clazz).newInstance();
            for (Method method: Class.forName(clazz).getMethods()) {
                // 判断当前方法是否带有 Testable 注解决定是否运行指定方法
                if (method.isAnnotationPresent(Testable.class)) {
                    try {
                        method.invoke(obj);
                        passed++;
                    } catch (InvocationTargetException e) {
                        System.out.println("method " + method.getName()
                                + "execute error: <" + e.getCause() + ">");
                        e.printStackTrace(System.out);
                        failed++;
                    }
                }
            }
            // test3 test1 共运行2个方法，成功2个方法，失败0个方法
            Utils.pringMsg("共运行" + (failed + passed) + "个方法，成功"
                + passed + "个方法，失败" + failed + "个方法");
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        TestableProcessor.process("com.coral.annotation.annote.mockjunit.TestCase");
    }
}
