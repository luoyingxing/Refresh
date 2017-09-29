package com.lyx.refresh.imp;

/**
 * Footer
 * <p>
 * Created by luoyingxing on 2017/8/28.
 */

public interface Footer {
    /**
     * 手指拖动上拉
     *
     * @param percent      上拉的百分比 0.00 - 1.00
     * @param pullHeight   上拉的距离
     * @param footerHeight footer的高度
     * @param extendHeight footer的扩展高度
     */
    void onPullingUp(float percent, float pullHeight, int footerHeight, int extendHeight);

    /**
     * 初始化状态
     */
    void onInit();

    /**
     * 准备好加载状态
     */
    void onPrepareToLoadMore();

    /**
     * 正在加载状态
     */
    void onLoading();

    /**
     * 加载结束状态
     */
    void onFinish();

}