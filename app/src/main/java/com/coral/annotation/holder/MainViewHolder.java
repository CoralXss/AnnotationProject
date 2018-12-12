package com.coral.annotation.holder;

import android.view.View;
import android.widget.TextView;

import com.coral.ViewBinderActualCallUtils;
import com.coral.annotation.R;
import com.coral.annotations.bindview.BindView;

/**
 * Created by xss on 2017/9/30.
 */

public class MainViewHolder {

    @BindView(R.id.tv_name)
    public TextView tv_name;

    public MainViewHolder(View view) {
        ViewBinderActualCallUtils.bind(this, view);
    }

    public void updateView(Class clazz) {
        tv_name.setText(clazz.getSimpleName());
    }
}
