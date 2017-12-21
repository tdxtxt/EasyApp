package com.easy.net;

/**
 * Created by Administrator on 2017/7/17.
 */

public class HttpResult<T> {

    public boolean error;
    //@SerializedName(value = "results", alternate = {"result"})
    public T results;
}
