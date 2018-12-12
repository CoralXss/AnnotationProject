package com.coral;

import android.app.Activity;
import android.view.View;

/**
 * Created by xss on 2017/9/29.
 * desc: 在 Activity UI 中根据 id 查找 View
 */

public class ActivityViewFinder implements ViewFinder {

    @Override
    public View findView(Object object, int id) {
        return ((Activity) object).findViewById(id);
    }
}
