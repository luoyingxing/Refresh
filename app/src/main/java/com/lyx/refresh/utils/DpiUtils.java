package com.lyx.refresh.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import com.lyx.refresh.App;


/**
 * DpiUtils
 * <p/>
 * Created by luoyingxing on 2017/9/25.
 */
public class DpiUtils {

    private static final String TAG = "DpiUtils";

    // 当前屏幕的densityDpi
    private static float mDmDensityDpi = 0.0f;
    private static int mWidth = 0;
    private static int mHeight = 0;

    private static DisplayMetrics mDisplayMetrics;
    private static float mScale = 1.0f;

    // 设计分辨率
    private static int mDesignWidth = 0;
    private static int mDesignHeight = 0;
    private static float mScaleX = 1.0f;
    private static float mScaleY = 1.0f;

    static {
        setContext(App.getApp());
    }

    public static void setContext(Context context) {
        // 获取当前屏幕
        mDisplayMetrics = context.getResources().getDisplayMetrics();
        // 设置DensityDpi,mWidth,mHeight
        mDmDensityDpi = mDisplayMetrics.densityDpi;
        mWidth = mDisplayMetrics.widthPixels;
        mHeight = mDisplayMetrics.heightPixels;
        // 密度因子
        mScale = getDmDensityDpi() / 160;
        Log.i(TAG, " dmDensityDpi:" + mDmDensityDpi + " mWidth:" + mWidth + " mHeight:" + mHeight);
    }

    public static int dipTopx(float dipValue) {
        return (int) (dipValue * mScale + 0.5f);

    }

    public static int pxTodip(float pxValue) {
        return (int) (pxValue / mScale + 0.5f);
    }

    public static float getDmDensityDpi() {
        return mDmDensityDpi;
    }

    public static int getWidth() {
        return mWidth;
    }

    public static int getHeight() {
        return mHeight;
    }

    public static void SetDesignResolution(int width, int height) {
        mDesignWidth = width;
        mDesignHeight = height;
        mScaleX = (float) mWidth / mDesignWidth;
        mScaleY = (float) mHeight / mDesignHeight;
        Log.i(TAG, "mScaleX" + mScaleX + ", mScaleY" + mScaleY);
    }

    public static float getScaleX() {
        return mScaleX;
    }

    public static float getScaleY() {
        return mScaleY;
    }

    public static int toRealX(int x) {
        return (int) (x * mScaleX);
    }

    public static int toRealY(int y) {
        return (int) (y * mScaleY);
    }

    public static int toDesignX(int x) {
        return (int) (x / mScaleX);
    }

    public static int toDesignY(int y) {
        return (int) (y / mScaleY);
    }

}