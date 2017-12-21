package com.easy.other;

import com.google.gson.Gson;
import com.orhanobut.hawk.Parser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/12.
 */

public class MyParser implements Parser {
    @Override
    public <T> T fromJson(String content, Type type) throws Exception {
        return null;
    }

    @Override
    public String toJson(Object body) {
        return new Gson().toJson(body);
    }
}
