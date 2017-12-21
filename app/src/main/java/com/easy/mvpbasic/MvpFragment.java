package com.easy.mvpbasic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.easy.act.fragment.BaseFragment;

/**
 * Created by Administrator on 2017/7/17.
 */

public abstract class MvpFragment<P extends BasePresenter> extends BaseFragment {
    protected P mvpPresenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mvpPresenter != null){
            mvpPresenter.detachView();
        }
    }

    protected abstract P createPresenter();
}
