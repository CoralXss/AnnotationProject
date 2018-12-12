package com.coral.annotation;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

//import com.coral.ViewBinderActualCallUtils;

/**
 * Created by xss on 2017/9/30.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public abstract int getLayoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void initView() {

    }
}
