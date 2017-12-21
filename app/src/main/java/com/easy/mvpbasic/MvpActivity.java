package com.easy.mvpbasic;


import android.os.Bundle;
import com.easy.act.base.BaseActivity;
import comm.helper.ActivityStackManager;

/**
 * Created by Administrator on 2017/7/17.
 */

public abstract class MvpActivity<P extends BasePresenter> extends BaseActivity {
    //创建 Presenter对象，
    public P mvpPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mvpPresenter = createPresenter();
        super.onCreate(savedInstanceState);
        ActivityStackManager.getInstance().addActivity(this);

    }
    //提供统一的接口
    protected abstract P createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
    }
}
