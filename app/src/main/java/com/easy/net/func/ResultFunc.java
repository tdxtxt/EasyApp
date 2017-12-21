package com.easy.net.func;

import com.easy.net.HttpResult;

import rx.functions.Func1;

/**
 * Created by Administrator on 2017/7/17.
 */

public class ResultFunc<T> implements Func1<String, HttpResult<T>> {
    @Override
    public HttpResult<T> call(String result) {
        return null;
    }
}
