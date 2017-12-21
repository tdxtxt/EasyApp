package com.easy.bean;

/**
 * Created by Administrator on 2017/7/20.
 */

public class City {
    public String name;
    public City(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "name:" + this.name + ";";
    }
}
