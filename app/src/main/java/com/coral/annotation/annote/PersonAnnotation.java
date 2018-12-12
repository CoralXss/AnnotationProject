package com.coral.annotation.annote;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by xss on 2017/9/28.
 */

// 给自定义的 Annotation 添加方法
@Target(ElementType.TYPE)
public @interface PersonAnnotation {

    int id() default 1;

    String name() default  "coral";
}
