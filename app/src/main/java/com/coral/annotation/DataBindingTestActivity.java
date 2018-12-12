package com.coral.annotation;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.coral.ViewBinderActualCallUtils;
import com.coral.annotation.databinding.ActivityDataBindingTestBinding;
import com.coral.annotation.model.UserEntity;

public class DataBindingTestActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_data_binding_test;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDataBindingTestBinding dataBindingTestBinding = DataBindingUtil.setContentView(this, getLayoutId());
        ViewBinderActualCallUtils.bind(this);
        dataBindingTestBinding.setUser(new UserEntity("Coral", "24"));


    }




}
