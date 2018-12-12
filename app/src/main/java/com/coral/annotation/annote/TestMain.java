package com.coral.annotation.annote;

import java.lang.reflect.Method;

/**
 * Created by xss on 2017/9/28.
 */

public class TestMain {

    public static void main(String[] args) {
        try {
            Class<Person> personClass = Person.class;

            Method[] methods = personClass.getMethods();
            for (Method m : methods) {
                if (m.isAnnotationPresent(MethodAnnotation.class)) {
                    m.invoke(personClass.newInstance());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
