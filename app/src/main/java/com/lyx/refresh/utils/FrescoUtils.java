package com.lyx.refresh.utils;

import android.content.Context;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lyx.refresh.R;

/**
 * FrescoUtils
 * <p>
 * Created by luoyingxing on 2017/9/20.
 */

public class FrescoUtils {

    public static void loadImage(Context context, SimpleDraweeView imageView, String imageUrl, int width, int height) {
        ViewParent parent = imageView.getParent();
        if (parent instanceof LinearLayout) {
            imageView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        } else if (parent instanceof RelativeLayout) {
            imageView.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        }
        GenericDraweeHierarchy hierarchy =
                new GenericDraweeHierarchyBuilder(context.getResources())
                        .setFadeDuration(800)
                        .setPlaceholderImage(context.getResources().getDrawable(R.mipmap.ic_launcher), ScalingUtils.ScaleType.CENTER_INSIDE)
                        .setFailureImage(context.getResources().getDrawable(R.mipmap.ic_launcher), ScalingUtils.ScaleType.CENTER_INSIDE)
                        .setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                        .build();
        DraweeHolder mDrawHolder = DraweeHolder.create(hierarchy, context);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(imageUrl)
                .setOldController(mDrawHolder.getController())
                .setAutoPlayAnimations(true)
                .build();
        imageView.setController(controller);
        imageView.setHierarchy(hierarchy);
    }

    public static void loadImage(Context context, SimpleDraweeView imageView, String imageUrl) {
        GenericDraweeHierarchy hierarchy =
                new GenericDraweeHierarchyBuilder(context.getResources())
                        .setFadeDuration(800)
                        .setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                        .build();
        DraweeHolder mDrawHolder = DraweeHolder.create(hierarchy, context);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(imageUrl)
                .setOldController(mDrawHolder.getController())
                .setAutoPlayAnimations(true)
                .build();
        imageView.setController(controller);
        imageView.setHierarchy(hierarchy);
    }
}