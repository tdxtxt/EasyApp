package com.easy.mvpbasic;

/**
 * Created by Administrator on 2017/7/17.
 */

public interface IView<T> {
    /**
     * 加载成功
     */
    void success(T t);
    /**
     * 加载失败
     */
    void fail(String errorMessage);
    /**
     * 显示加载
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();
}
