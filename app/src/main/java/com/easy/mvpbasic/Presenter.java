package com.easy.mvpbasic;

/**
 * Created by Administrator on 2017/7/17.
 */

public interface Presenter<T> {
    //关联View
    void attachView(T view);
    //解除关联
    void detachView();
}
