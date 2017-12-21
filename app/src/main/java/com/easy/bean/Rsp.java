package com.easy.bean;

/**
 * Created by Administrator on 2017/6/12.
 */

public class Rsp<T> {
    public String desc;
    public T data;
    public String status;

    public boolean isSuccess(){
        return "1".equals(status);
    }
}
