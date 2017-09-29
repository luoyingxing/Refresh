package com.lyx.refresh.imp;

/**
 * Header
 * <p>
 * Created by luoyingxing on 2017/8/28.
 */

public interface Header {
    /**
     * 手指拖动下拉
     *
     * @param percent      下拉的百分比 0.00 - 1.00
     * @param pullHeight   下拉的距离
     * @param headerHeight Header的高度
     * @param extendHeight Header的扩展高度
     */
    void onPullingDown(float percent, float pullHeight, int headerHeight, int extendHeight);

    /**
     * 初始化状态
     */
    void onInit();

    /**
     * 准备好刷新状态
     */
    void onPrepareToRefresh();

    /**
     * 正在刷新状态
     */
    void onRefreshing();

    /**
     * 刷新结束状态
     */
    void onFinish();
}