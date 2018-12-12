package com.coral.annotation.annote.inherited;

import java.lang.annotation.Annotation;

/**
 * Created by xss on 2017/9/29.
 */

public class Client {

    @Deprecated
    public void client() {

    }

    public void test() {
        try {
            // 先获取 Method type，再获取方法上的注解
            Annotation[] annotations = getClass().getMethod("client").getAnnotations();
            for (Annotation annotation: annotations) {
                System.out.println(annotation.annotationType().getName());
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @InheritedTest
    static class SupperClass {

    }

    static class SubClass extends SupperClass {

        public SubClass() {
            // 类 Type 的注解
            for (Annotation annotation: SubClass.class.getAnnotations()) {
                // 以下打印 @com.coral.annotation.annote.mockjunit.Testable()
                System.out.println(annotation);
            }
        }
    }

    public static void main(String[] args) {
        new Client().test(); // java.lang.Deprecated
//        new SubClass();
    }
}
