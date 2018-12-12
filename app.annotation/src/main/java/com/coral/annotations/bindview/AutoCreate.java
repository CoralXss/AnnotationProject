package com.coral.annotations.bindview;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xss on 2017/9/30.
 */

@Target(ElementType.TYPE)  //作用在类上
@Retention(RetentionPolicy.RUNTIME)//存活时间
public @interface AutoCreate {

}
