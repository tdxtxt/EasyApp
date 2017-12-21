package com.easy.act.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easy.android.R;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 类描述：
 * 创建人：tdx
 * 创建时间：2017/11/24 13:46
 * 传参：
 * 返回:
 */
public class TitleActivity extends BaseActivity {
    @BindView(R.id.iv_back) ImageView mBtnBack;
    @BindView(R.id.tv_title_center) TextView mTvTitle;
    @BindView(R.id.layout_content) FrameLayout mContentLayout;

    protected LoadService loadService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_title);   //加载 activity_title 布局 ，并获取标题及两侧按钮
        loadService = LoadSir.getDefault().register(mContentLayout, new Callback.OnReloadListener() {
            @Override
            public void onReload(View view) {
                if(view.findViewById(R.id.btn_retry) != null){
                    (view.findViewById(R.id.btn_retry)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reqApi();
                            Toast.makeText(v.getContext(), "重新加载", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if(view.findViewById(R.id.btn_startAct) != null){
                    (view.findViewById(R.id.btn_startAct)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reqApi();
                            Toast.makeText(v.getContext(), "去逛逛吧", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        });
    }
    @OnClick(R.id.iv_back)   //给 button1 设置一个点击事件
    public void clickBack(){
        finish();
    }
    //设置标题内容
    public void setTitle(String title) {
        mTvTitle.setText(title);
    }
    //取出FrameLayout并调用父类removeAllViews()方法
    @Override
    public void setContentView(int layoutResID) {
        mContentLayout.removeAllViews();
        View.inflate(this, layoutResID, mContentLayout);
        onContentChanged();
//        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view) {
        mContentLayout.removeAllViews();
        mContentLayout.addView(view);
        onContentChanged();
//        ButterKnife.bind(this);
    }
    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view,params);
        mContentLayout.removeAllViews();
        mContentLayout.addView(view, params);
        onContentChanged();
//        ButterKnife.bind(this);
    }
}
