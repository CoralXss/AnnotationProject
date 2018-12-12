package com.coral.annotation.annote;

/**
 * Created by xss on 2017/9/28.
 */

@PersonAnnotation(id = 8, name = "xss")
public class Person {

    public void eat() {
        print("eating");
    }

    @MethodAnnotation
    public void walking() {
        print("Walking");
    }

    public void print(String msg) {
        System.out.print(msg);
    }
}
