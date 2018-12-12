package com.coral;

/**
 * Created by xss on 2017/9/29.
 */

public interface ViewBinder<T> {

    void bindView(T host, Object object, ViewFinder finder);

    void unBindView(T host);

}
