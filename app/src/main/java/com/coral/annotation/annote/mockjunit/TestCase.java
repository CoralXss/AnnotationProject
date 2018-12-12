package com.coral.annotation.annote.mockjunit;

import com.coral.annotation.annote.Utils;

/**
 * Created by xss on 2017/9/29.
 * desc: 对使用 @Testable 注解的方法才是可测试的
 */
public class TestCase {

    @Testable
    public void test1() {
        Utils.pringMsg("test1");
    }

    public void test2() {
        Utils.pringMsg("test2");
    }

    @Testable
    public void test3() {
        Utils.pringMsg("test3");
    }
}
