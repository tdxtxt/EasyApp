package com.easy.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import comm.helper.DisplayHelper;
import comm.helper.ScreenHelper;

/**
 * Created by aierJun on 2016/12/21.
 */
public class FloatView extends ImageView {
    private int _xDelta;
    private int _yDelta;

    private long startTime = 0;
    private long endTime = 0;
    private String url="";

    public FloatView(Context context) {
        super(context);
    }

    public FloatView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setUrl(String url){
        this.url=url;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
// TODO Auto-generated method stub

        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                break;
            case MotionEvent.ACTION_UP:
                endTime = System.currentTimeMillis();
                //当从点击到弹起小于半秒的时候,则判断为点击,如果超过则不响应点击事件
                if ((endTime - startTime) <=0.1 * 1000L) {
                    if(TextUtils.isEmpty(url))return true;
//                    Intent intent = new Intent(getContext(), CommWebViewActivity.class);
//                    intent.putExtra("url", url);
//                    getContext().startActivity(intent);
                } else {
                    FrameLayout.LayoutParams layoutParamss = (FrameLayout.LayoutParams) getLayoutParams();
                    if (X - _xDelta < ScreenHelper.getScreenWidth(getContext()) / 2 - this.getWidth() / 2)
                        layoutParamss.leftMargin = 0;
                    else
                        layoutParamss.leftMargin = ScreenHelper.getScreenWidth(getContext()) - this.getWidth();
                    if (layoutParamss.topMargin <= 0)
                        layoutParamss.topMargin = 0;
                    else if (layoutParamss.topMargin >= ScreenHelper.getScreenHeight(getContext()) - this.getHeight() - DisplayHelper.dp2px(getContext(),80))
                        layoutParamss.topMargin = ScreenHelper.getScreenHeight(getContext()) - this.getHeight() - DisplayHelper.dp2px(getContext(),80);
                    else layoutParamss.topMargin = Y - _yDelta;

                    FloatView.this.setLayoutParams(layoutParamss);
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
                layoutParams.leftMargin = X - _xDelta;
                layoutParams.topMargin = Y - _yDelta;
                if (layoutParams.topMargin <= 0)
                    layoutParams.topMargin = 0;
                if (layoutParams.leftMargin <= 0)
                    layoutParams.leftMargin = 0;
                if (layoutParams.leftMargin > ScreenHelper.getScreenWidth(getContext()) - this.getWidth())
                    layoutParams.leftMargin = ScreenHelper.getScreenWidth(getContext()) - this.getWidth();
                if (layoutParams.topMargin >= ScreenHelper.getScreenHeight(getContext()) - this.getHeight() - DisplayHelper.dp2px(getContext(),80))
                    layoutParams.topMargin = ScreenHelper.getScreenHeight(getContext()) - this.getHeight() - DisplayHelper.dp2px(getContext(),80);
                FloatView.this.setLayoutParams(layoutParams);      //自己继承VIEW的this
                break;
        }
        invalidate();
        return true;
    }

}
