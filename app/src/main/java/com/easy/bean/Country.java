package com.easy.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/20.
 */

public class Country {
    public String name;
    public List<City> citys;
    public Country(String name,List<City> cities){
        this.name = name;
        this.citys = cities;
    }
}
