package com.easy.act;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.easy.act.base.TitleActivity;
import com.easy.android.R;
import com.easy.callback.loadsir.EmptyCallback;
import com.easy.callback.loadsir.ErrorCallback;
import com.easy.callback.loadsir.LoadingCallback;
import com.hitomi.glideloader.GlideImageLoader;
import com.hitomi.tilibrary.style.index.NumberIndexIndicator;
import com.hitomi.tilibrary.style.progress.ProgressPieIndicator;
import com.hitomi.tilibrary.transfer.TransferConfig;
import com.hitomi.tilibrary.transfer.Transferee;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.internal.Utils;
import ch.ielse.view.imagewatcher.ImageWatcher;
import comm.imageload.ImageLoaderManager;
import comm.imageload.ImageLoaderOptions;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/6/12.
 */

public class SavActivity extends TitleActivity{
    String picUrl = "http://img.test02.kebuyer.com/img/bff8cc04d45541058eafdadee7e9d4286610c52f26ab4e038ffc360467019a7f.jpg?x-oss-process=image/resize,m_lfit,w_360,h_360,limit_0/auto-orient,1/sharpen,100/quality,q_61/format,jpg/interlace,1";
    ImageView imageView;
    ImageWatcher imageWatcher;
    Transferee transferee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_save);
        imageView = (ImageView) findViewById(R.id.iv);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                testRxjava();
                loadService.showCallback(ErrorCallback.class);
            }
        });
        reqApi();
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new BlurPopupWindow.Builder<>(v.getContext())
//                        .setContentView(R.layout.dialog_bottom_menu)
//                        .setGravity(Gravity.BOTTOM)
//                        .setAnimationDuration(300)
//                        .setScaleRatio(0.2f)
//                        .setBlurRadius(10)
//                        .setTintColor(0x30000000)
//                        .build()
//                        .show();
                loadService.showSuccess();
            }
        });
        transferee = Transferee.getDefault(mActivity);

        imageWatcher = ImageWatcher.Helper.with(mActivity) // 一般来讲， ImageWatcher 需要占据全屏的位置
//                        .setTranslucentStatus(!isTranslucentStatus ? Utils.calcStatusBarHeight(this) : 0) // 如果是透明状态栏，你需要给ImageWatcher标记 一个偏移值，以修正点击ImageView查看的启动动画的Y轴起点的不正确
                .setErrorImageRes(R.drawable.error) // 配置error图标 如果不介意使用lib自带的图标，并不一定要调用这个API
//                        .setOnPictureLongPressListener(this) // 长按图片的回调，你可以显示一个框继续提供一些复制，发送等功能
                .setLoader(new ImageWatcher.Loader() {
                    @Override
                    public void load(Context context, String url, final ImageWatcher.LoadCallback lc) {
//                        ImageLoaderManager.getInstance().showImage(ImageLoaderManager.getDefaultOptions());
                        Glide.with(context)./*asBitmap().*/load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                lc.onResourceReady(resource);
                            }
                        });
                    }
                })
                .create();
        ImageLoaderManager.getInstance().showImage(ImageLoaderManager.getDefaultOptions(imageView,picUrl));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                imageWatcher.show(imageView, Collections.singletonList(imageView),Collections.singletonList(picUrl));
                TransferConfig config = TransferConfig.build()
                        .setNowThumbnailIndex(0)
                        .setSourceImageList(Collections.singletonList("https://img.kebuyer.com/img/4d9c24662f6c4e93aea7a37c28da9fa6cc971f9867a7459fa666443590df5cf7.jpg"))
                        .setThumbnailImageList(Collections.singletonList("https://img.kebuyer.com/img/4d9c24662f6c4e93aea7a37c28da9fa6cc971f9867a7459fa666443590df5cf7.jpg?x-oss-process=image/resize,m_lfit,w_36,h_36,limit_0/auto-orient,1/sharpen,100/quality,q_61/format,jpg/interlace,1"))
                        .setMissDrawable(imageView.getDrawable())
                        .setErrorPlaceHolder(R.drawable.error)
                        .setOriginImageList(Collections.singletonList(imageView))
                        .setProgressIndicator(new ProgressPieIndicator())
                        .setIndexIndicator(new NumberIndexIndicator())
                        .setJustLoadHitImage(true)
                        .setImageLoader(GlideImageLoader.with(mActivity))
                        .create();
                transferee.apply(config).show();
            }
        });

    }
    int i= 0;
    @Override
    protected void reqApi(){
        loadService.showCallback(LoadingCallback.class);
        Observable.just(i++).delay(3,TimeUnit.SECONDS).compose(this.<Integer>applyMainSchedulers())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer number) {
                        if(number % 2 == 0){
                            loadService.showSuccess();
                        }else{
                            loadService.showCallback(EmptyCallback.class);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        loadService.showCallback(ErrorCallback.class);
                    }
                });
    }
}
