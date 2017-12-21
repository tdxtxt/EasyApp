package com.easy.act.business.login;

import com.easy.bean.City;
import com.easy.bean.Country;
import com.easy.mvpbasic.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.internal.util.ActionSubscriber;

/**
 * Created by Administrator on 2017/7/17.
 */

public class LoginPresenter extends BasePresenter<LoginImpl>{
    List<Country> data;
    public void initData(){
        if(data == null) data = new ArrayList<>();
        List<City> oneCities = new ArrayList<>();
        oneCities.add(new City("上海"));oneCities.add(new City("重庆"));oneCities.add(new City("北京"));
        Country oneCoun = new Country("中国",oneCities);

        List<City> twoCities = new ArrayList<>();
        twoCities.add(new City("华盛顿"));twoCities.add(new City("德州"));
        Country twoCoun = new Country("美国",twoCities);

        List<City> threeCities = new ArrayList<>();
        twoCities.add(new City("柏林"));threeCities.add(new City("汉堡"));threeCities.add(new City("海德堡"));
        Country threeCoun = new Country("德国",threeCities);

        data.add(oneCoun);
        data.add(twoCoun);
        data.add(threeCoun);

    }
    public LoginPresenter(LoginImpl view){
        attachView(view);
    }
    public void login(String accout,String password){
        mvpView.showLoading();
        addSubscription(Observable.from(data), new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {

            }
        });
    }

}
