package com.lyx.refresh.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.lyx.refresh.imp.Header;

/**
 * HeaderLayout 自定义刷新头，实现Header接口
 * <p>
 * Created by luoyingxing on 2017/9/26.
 */

public class HeaderLayout extends FrameLayout implements Header {

    public HeaderLayout(@NonNull Context context) {
        super(context);
    }

    public HeaderLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onPullingDown(float percent, float pullHeight, int headerHeight, int extendHeight) {
        if (null != mOnPullingListener) {
            mOnPullingListener.onPulling(percent, pullHeight, headerHeight, extendHeight);
        }
    }

    @Override
    public void onInit() {
        if (null != mOnStatusListener) {
            mOnStatusListener.onInit();
        }
    }

    @Override
    public void onPrepareToRefresh() {
        if (null != mOnStatusListener) {
            mOnStatusListener.onPrepareToRefresh();
        }
    }

    @Override
    public void onRefreshing() {
        if (null != mOnStatusListener) {
            mOnStatusListener.onRefreshing();
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
         * 手指拖动下拉
         *
         * @param percent      下拉的百分比 0.00 - 1.00
         * @param pullHeight   下拉的距离
         * @param headerHeight Header的高度
         * @param extendHeight Header的扩展高度
         */
        void onPulling(float percent, float pullHeight, int headerHeight, int extendHeight);
    }

    private OnPullingListener mOnPullingListener;

    public void setOnPullingListener(OnPullingListener listener) {
        this.mOnPullingListener = listener;
    }

    public interface OnStatusListener {
        void onInit();

        void onPrepareToRefresh();

        void onRefreshing();

        void onFinish();
    }

    private OnStatusListener mOnStatusListener;

    public void setOnStatusListener(OnStatusListener listener) {
        this.mOnStatusListener = listener;
    }
}