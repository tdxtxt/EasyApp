package com.easy.net.subscriber;

import com.easy.net.HttpResult;

import rx.Subscriber;

/**
 * Created by Administrator on 2017/7/17.
 */

public abstract class HttpResultSubscriber<T> extends Subscriber<HttpResult<T>> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e != null) {
            e.printStackTrace();
            if (e.getMessage() == null) {
                _onError(new Throwable(e.toString()));
            } else {
                _onError(new Throwable(e.getMessage()));
            }
        } else {
            _onError(new Exception("null message"));
        }
    }

    @Override
    public void onNext(HttpResult<T> t) {
        if (!t.error)
            onSuccess(t.results);
        else
            _onError(new Throwable("error=true"));
    }

    public abstract void onSuccess(T t);

    public abstract void _onError(Throwable e);
}
