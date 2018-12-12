package com.coral;

import android.view.View;

/**
 * Created by xss on 2017/9/29.
 */

public interface ViewFinder {

    // 定义一个被绑定者查找 View 的接口
    View findView(Object object, int id);
}
