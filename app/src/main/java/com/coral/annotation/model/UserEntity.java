package com.coral.annotation.model;

/**
 * Created by xss on 2017/9/30.
 */

public class UserEntity {

    public UserEntity() {

    }

    public UserEntity(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String name;

    public String age;
}
