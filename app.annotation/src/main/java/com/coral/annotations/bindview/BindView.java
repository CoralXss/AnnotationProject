package com.coral.annotations.bindview;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xss on 2017/9/29.
 * desc: 声明一个 BindView 注解，生命周期为 Class,作用域为成员变量
 *       （仅实现绑定View控件，所以只声明一个 BindView 注解）
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface BindView {

    int value();
}
