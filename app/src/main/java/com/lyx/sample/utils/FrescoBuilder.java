package com.lyx.sample.utils;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.lyx.sample.R;

/**
 * FrescoBuilder
 * <p>
 * Created by luoyingxing on 2017/9/25.
 */

public class FrescoBuilder {
    private Context mContext;
    private SimpleDraweeView mView;
    private String mUrl;

    public FrescoBuilder(Context context, SimpleDraweeView imageView, String url) {
        mContext = context;
        mView = imageView;
        mUrl = url;
    }

    public void builder() {
        loadImage(mView, mUrl);
    }

    private void loadImage(final SimpleDraweeView imageView, String uri) {
        GenericDraweeHierarchy hierarchy =
                new GenericDraweeHierarchyBuilder(mContext.getResources())
                        .setFadeDuration(1000)
                        .setPlaceholderImage(mContext.getResources().getDrawable(R.mipmap.img_default), ScalingUtils.ScaleType.CENTER_INSIDE)
                        .setFailureImage(mContext.getResources().getDrawable(R.mipmap.img_default), ScalingUtils.ScaleType.CENTER_INSIDE)
                        .setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                        .build();
        DraweeHolder mDrawHolder = DraweeHolder.create(hierarchy, mContext);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setControllerListener(new ControllerListener<ImageInfo>() {
                    @Override
                    public void onSubmit(String id, Object callerContext) {
                    }

                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        reSize(imageView, imageInfo);
                    }

                    @Override
                    public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
                    }

                    @Override
                    public void onIntermediateImageFailed(String id, Throwable throwable) {
                    }

                    @Override
                    public void onFailure(String id, Throwable throwable) {
                    }

                    @Override
                    public void onRelease(String id) {
                    }
                })
                .setOldController(mDrawHolder.getController())
                .setAutoPlayAnimations(true)
                .build();

        imageView.setController(controller);
        imageView.setHierarchy(hierarchy);
    }

    /**
     * mode for reSize
     * double ratio = ((DpiUtils.getWidth() - DpiUtils.dipTopx(20)) * 1.0) / imgW;
     *
     * @param imageView SimpleDraweeView
     * @param imageInfo imageInfo
     */
    private void reSize(SimpleDraweeView imageView, ImageInfo imageInfo) {
        int imgW = imageInfo.getWidth();
        int imgH = imageInfo.getHeight();
        double ratio = reSize(imgW, imgH);
        int lastW = (int) (imgW * ratio);
        int lastH = (int) (imgH * ratio);
        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        lp.width = lastW;
        lp.height = lastH;
        imageView.setLayoutParams(lp);
    }

    /**
     * implement this method for reSize the ImageView
     * <p>
     * radio = FinalWidth / imageWidth
     *
     * @param imageWidth imageView's width
     * @return double
     */
    public double reSize(int imageWidth, int imageHeight) {
        return 0;
    }

}