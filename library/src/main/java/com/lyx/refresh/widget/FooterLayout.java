package com.lyx.refresh.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.lyx.refresh.imp.Footer;

/**
 * FooterLayout 自定义加载，实现Footer接口
 * <p>
 * Created by luoyingxing on 2017/9/26.
 */

public class FooterLayout extends FrameLayout implements Footer {

    public FooterLayout(@NonNull Context context) {
        super(context);
    }

    public FooterLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FooterLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onPullingUp(float percent, float pullHeight, int footerHeight, int extendHeight) {
        if (null != mOnPullingListener) {
            mOnPullingListener.onPullingUp(percent, pullHeight, footerHeight, extendHeight);
        }
    }

    @Override
    public void onInit() {
        if (null != mOnStatusListener) {
            mOnStatusListener.onInit();
        }
    }

    @Override
    public void onPrepareToLoadMore() {
        if (null != mOnStatusListener) {
            mOnStatusListener.onPrepareToLoadMore();
        }
    }

    @Override
    public void onLoading() {
        if (null != mOnStatusListener) {
            mOnStatusListener.onLoading();
        }
    }

    @Override
    public void onFinish() {
        if (null != mOnStatusListener) {
            mOnStatusListener.onFinish();
        }
    }

    public interface OnPullingListener {
        /**
         * 手指拖动上拉
         *
         * @param percent      上拉的百分比 0.00 - 1.00
         * @param pullHeight   上拉的距离
         * @param footerHeight footer的高度
         * @param extendHeight footer的扩展高度
         */
        void onPullingUp(float percent, float pullHeight, int footerHeight, int extendHeight);
    }

    private OnPullingListener mOnPullingListener;

    public void setOnPullingListener(OnPullingListener listener) {
        this.mOnPullingListener = listener;
    }

    public interface OnStatusListener {
        void onInit();

        void onPrepareToLoadMore();

        void onLoading();

        void onFinish();
    }

    private OnStatusListener mOnStatusListener;

    public void setOnStatusListener(OnStatusListener listener) {
        this.mOnStatusListener = listener;
    }
}