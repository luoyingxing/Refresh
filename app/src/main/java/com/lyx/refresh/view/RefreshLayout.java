package com.lyx.refresh.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ScrollView;

import com.lyx.refresh.R;

/**
 * RefreshLayout
 * <p>
 * Created by luoyingxing on 2017/8/28.
 */

public class RefreshLayout extends ViewGroup {
    private static final String TAG = "RefreshLayout";
    private int mWindowWidth;
    private int mWindowHeight;
    private int mHeaderHeight;
    private int mFooterHeight;
    private View mHeaderView;
    private View mContentView;
    private View mFooterView;

    /**
     * 当RefreshLayout只有两个子View时，需要指明哪个字view是滑动的view，否则会抛出 RuntimeException：
     * please assign the pull view in RefreshLayout in xml with refresh:pullViewPosition="first"
     */
    private int mPullViewPosition;

    /**
     * 刷新、加载完毕后的回弹时间，（单位：毫秒）
     */
    private static final long mSpringBackAnimationTimeDelay = 260L;

    /**
     * 手指释放后的回弹时间，（单位：毫秒）
     */
    private static final long mSpringBackAnimationTimeDelayForRelease = 180L;

    /**
     * 设置 RefreshLayout 的子View能否进行回弹效果, 在xml 里设置refresh:spring="all|up|down" 即可
     */
    private int mSpring;
    /**
     * 是否可下拉回弹，决定于mSpring 的取值
     */
    private boolean mDownEnable;
    /**
     * 是否可上拉回弹，决定于mSpring 的取值
     */
    private boolean mUpEnable;

    /**
     * 当前拖动的状态
     */
    private Status mStatus = Status.INIT;

    public enum Status {
        INIT,
        PREPARE_TO_REFRESH,
        REFRESHING,
        PREPARE_TO_LOAD,
        LOADING,
        FINISH
    }

    public RefreshLayout(Context context) {
        super(context);
        initView(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mWindowWidth = metrics.widthPixels;
        mWindowHeight = metrics.heightPixels;

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RefreshLayout);
        mPullViewPosition = array.getInt(R.styleable.RefreshLayout_pullViewPosition, 0);

        mSpring = array.getInt(R.styleable.RefreshLayout_spring, 0);
        switch (mSpring) {
            case 1:
                mDownEnable = true;
                mUpEnable = true;
                break;
            case 2:
                mDownEnable = true;
                break;
            case 3:
                mUpEnable = true;
                break;
        }

        array.recycle();
    }

    @Override
    protected void onFinishInflate() {
        Log.d(TAG, "onFinishInflate()");
        super.onFinishInflate();
        int count = getChildCount();

        if (1 > count || 3 < count) {
            throw new RuntimeException("RefreshLayout can only include three child view, and must include at most one child view");
        }

        if (1 == count) {
            if (null == mContentView) {
                mContentView = getChildAt(0);
            }
        }

        if (2 == count) {
            if (0 == mPullViewPosition) {
                throw new RuntimeException("please assign the pull view in RefreshLayout in xml with refresh:pullViewPosition=\"first\"");
            }

            if (null == mContentView) {
                mContentView = getChildAt(mPullViewPosition - 1);
            }

            if (mPullViewPosition == 1) {
                if (null == mFooterView) {
                    mFooterView = getChildAt(1);
                }
            } else {
                if (null == mHeaderView) {
                    mHeaderView = getChildAt(0);
                }
            }
        }

        if (3 == count) {
            if (null == mHeaderView) {
                if (getChildAt(0) instanceof Footer) {
                    throw new RuntimeException("The Header view is can not implements Footer");
                } else {
                    mHeaderView = getChildAt(0);
                }
            }

            if (null == mContentView) {
                mContentView = getChildAt(1);
            }

            if (null == mFooterView) {
                if (getChildAt(2) instanceof Header) {
                    throw new RuntimeException("The Footer view is can not implements Header");
                } else {
                    mFooterView = getChildAt(2);
                }
            }
        }

        if (null == mContentView) {
            throw new RuntimeException("RefreshLayout must include one of pull view");
        }
    }

    @Override
    protected void onAttachedToWindow() {
        Log.d(TAG, "onAttachedToWindow()");
        super.onAttachedToWindow();
    }

    /**
     * need backup the margin，use system's MarginLayoutParams
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        // measure mHeaderView's height and width
        if (mHeaderView != null) {
            measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams params = (MarginLayoutParams) mHeaderView.getLayoutParams();
            int headerWidth = mHeaderView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int headerHeight = mHeaderView.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            mHeaderHeight = headerHeight;
        }

        // measure mFooterView's height and width
        if (mFooterView != null) {
            measureChild(mFooterView, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams params = (MarginLayoutParams) mFooterView.getLayoutParams();
            int footerWidth = mFooterView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int footerHeight = mFooterView.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            mFooterHeight = footerHeight;
        }

        // measure contentView's height and width
        measureChild(mContentView, widthMeasureSpec, heightMeasureSpec);
        MarginLayoutParams params = (MarginLayoutParams) mContentView.getLayoutParams();
        int contentWidth = mContentView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
        int contentHeight = mContentView.getMeasuredHeight() + params.topMargin + params.bottomMargin;

        setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth : contentWidth, (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight : contentHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout()");

        if (mHeaderView != null) {
            mHeaderView.layout(0,
                    (int) (mPullDownY + mPullUpY) - mHeaderView.getMeasuredHeight(),
                    mHeaderView.getMeasuredWidth(),
                    (int) (mPullDownY + mPullUpY));
        }

        mContentView.layout(0,
                (int) (mPullDownY + mPullUpY),
                mContentView.getMeasuredWidth(),
                (int) (mPullDownY + mPullUpY) + mContentView.getMeasuredHeight());


        if (mFooterView != null) {
            mFooterView.layout(0,
                    (int) (mPullDownY + mPullUpY) + mContentView.getMeasuredHeight(),
                    mFooterView.getMeasuredWidth(),
                    (int) (mPullDownY + mPullUpY) + mContentView.getMeasuredHeight() + mFooterView.getMeasuredHeight());
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Log.d(TAG, "dispatchDraw()");
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.d(TAG, "onDetachedFromWindow()");
        super.onDetachedFromWindow();
    }

    /**
     * @return true is can pull down , otherwise.
     */
    private boolean canPullDown() {
        if (mPullDownY > 0) {
            return true;
        }

        if (null != mContentView) {
            if (mContentView instanceof AbsListView) {
                int firstVisiblePosition = ((AbsListView) mContentView).getFirstVisiblePosition();
                int lastVisiblePosition = ((AbsListView) mContentView).getLastVisiblePosition();

                if (0 == firstVisiblePosition) {
                    View topChildView = ((AbsListView) mContentView).getChildAt(0);
                    return null != topChildView && 0 == topChildView.getTop();
                }
            }
        }
        return mDownEnable;
    }

    private boolean canPullUp() {
        if (mPullUpY < 0) {
            return true;
        }
        if (null != mContentView) {
            if (mContentView instanceof AbsListView) {
                int firstVisiblePosition = ((AbsListView) mContentView).getFirstVisiblePosition();
                int lastVisiblePosition = ((AbsListView) mContentView).getLastVisiblePosition();

                if (lastVisiblePosition == (((AbsListView) mContentView).getCount() - 1)) {
                    View bottomChildView = ((AbsListView) mContentView).getChildAt(lastVisiblePosition - firstVisiblePosition);
                    return null != bottomChildView && mContentView.getHeight() >= bottomChildView.getBottom();
                }
            }

        }
        return mUpEnable;
    }

    private float mPullDownY;
    private float mPullUpY;
    private float mLastY;
    private float mRadio = 4;
    private int mEvents;
    private boolean mCanPullDown;
    private boolean mCanPullUp;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getY();
                mEvents = 0;
                mCanPullDown = true;
                mCanPullUp = true;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                mEvents = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                if (requestInterceptTouchEvent(ev)) {
                    return true;
                }

                if (mEvents == 0) {
                    if (canPullDown() && mCanPullDown && mStatus != Status.REFRESHING) {
                        mPullDownY = mPullDownY + (ev.getY() - mLastY) / mRadio;
                        checkPullDown();
                        onPullingDown();
                    } else if (canPullUp() && mCanPullUp && mStatus != Status.LOADING) {
                        mPullUpY = mPullUpY + (ev.getY() - mLastY) / mRadio;
                        checkPullUp();
                        onPullingUp();
                    }
                } else {
                    mEvents = 0;
                }

                mLastY = ev.getY();
                updateRadio();
                updateLayout();
                updateLayoutStatusOnMove();

                if (cancelPull()) {
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                requestRefreshLayoutOnDone();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void checkPullDown() {
        if (mPullDownY < 0) {
            mPullDownY = 0;
            mCanPullDown = false;
            mCanPullUp = true;
        }
        if (mPullDownY > getMeasuredHeight()) {
            mPullDownY = getMeasuredHeight();
        }
    }

    private void onPullingDown() {
        if (null != mHeaderView && mHeaderView instanceof Header) {
            int extend = mPullDownY > mHeaderHeight ? (int) (mHeaderHeight - mPullDownY) : 0;
            float percent = mPullDownY > mHeaderHeight ? 1 : mPullDownY / mHeaderHeight;
            ((Header) mHeaderView).onPullingDown(percent, mPullDownY, mHeaderHeight, extend);
        }
    }

    private void onPullingUp() {
        if (null != mFooterView && mFooterView instanceof Footer) {
            int extend = -mPullUpY > mFooterHeight ? (int) (mFooterHeight + mPullUpY) : 0;
            float percent = -mPullUpY > mFooterHeight ? 1 : -mPullUpY / mFooterHeight;
            ((Footer) mFooterView).onPullingUp(percent, -mPullUpY, mFooterHeight, extend);
        }
    }

    private void checkPullUp() {
        if (mPullUpY > 0) {
            mPullUpY = 0;
            mCanPullDown = true;
            mCanPullUp = false;
        }
        if (mPullUpY < -getMeasuredHeight()) {
            mPullUpY = -getMeasuredHeight();
        }
    }

    private void updateRadio() {
        mRadio = (float) (4 + 4 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (mPullDownY + Math.abs(mPullUpY))));
    }

    private void updateLayout() {
        if ((mPullDownY > 0) || (mPullUpY < 0)) {
            requestLayout();
        }
    }

    private boolean cancelPull() {
        // 防止下拉过程中误触发长按事件和点击事件
        return (mPullDownY + Math.abs(mPullUpY)) > 6;
    }

    private void updateLayoutStatusOnMove() {
        if (mPullDownY > 0 && mStatus != Status.REFRESHING && null != mHeaderView) {
            if (mPullDownY <= mHeaderHeight && (mStatus == Status.PREPARE_TO_REFRESH || mStatus == Status.FINISH)) {
                mStatus = Status.INIT;
                if (mHeaderView instanceof Header) {
                    ((Header) mHeaderView).onInit();
                }

                if (null != mOnStatusListener) {
                    mOnStatusListener.onRefreshInit();
                }
            }

            if (mPullDownY >= mHeaderHeight && mStatus == Status.INIT) {
                mStatus = Status.PREPARE_TO_REFRESH;
                if (mHeaderView instanceof Header) {
                    ((Header) mHeaderView).onPrepareToRefresh();
                }

                if (null != mOnStatusListener) {
                    mOnStatusListener.onPrepareToRefresh();
                }
            }
        } else if (mPullUpY < 0 && mStatus != Status.LOADING && null != mFooterView) {

            if (-mPullUpY <= mFooterHeight && (mStatus == Status.PREPARE_TO_LOAD || mStatus == Status.FINISH)) {
                mStatus = Status.INIT;
                if (mFooterView instanceof Footer) {
                    ((Footer) mFooterView).onInit();
                }

                if (null != mOnStatusListener) {
                    mOnStatusListener.onLoadInit();
                }
            }

            if (-mPullUpY >= mFooterHeight && mStatus == Status.INIT) {
                mStatus = Status.PREPARE_TO_LOAD;
                if (mFooterView instanceof Footer) {
                    ((Footer) mFooterView).onPrepareToLoadMore();
                }

                if (null != mOnStatusListener) {
                    mOnStatusListener.onPrepareToLoadMore();
                }
            }
        }
    }

    /**
     * 手指释放后
     */
    private void requestRefreshLayoutOnDone() {
        if (mStatus == Status.PREPARE_TO_REFRESH && null != mHeaderView) {
            springBackAnimationToRefresh();
        } else if (mStatus == Status.PREPARE_TO_LOAD && null != mFooterView) {
            springBackAnimationToLoad();
        } else {
            if (mStatus == Status.REFRESHING && null != mHeaderView) {
                mPullDownY = mHeaderHeight;
            } else if (mStatus == Status.LOADING && null != mFooterView) {
                mPullUpY = -mFooterHeight;
            } else {
                if (mHeaderView instanceof Header) {
                    ((Header) mHeaderView).onInit();
                }
                if (mFooterView instanceof Footer) {
                    ((Footer) mFooterView).onInit();
                }

                if (null != mOnStatusListener) {
                    mOnStatusListener.onRefreshInit();
                    mOnStatusListener.onLoadInit();
                }
                springBackAnimation();
            }
        }
    }

    /**
     * 手指释放、刷新完成、加载完成后的回弹效果
     */
    private void springBackAnimation() {
        if (mPullDownY != 0) {
            //刷新完成后回弹
            float totalHeight = mPullDownY;
            ValueAnimator anim = ValueAnimator.ofFloat(totalHeight, 0);
            anim.setDuration(mSpringBackAnimationTimeDelay).start();
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mPullDownY = (float) animation.getAnimatedValue();
                    requestLayout();
                }
            });

        } else if (mPullUpY != 0) {
            //加载完成后回弹
            float totalHeight = mPullUpY;
            ValueAnimator anim = ValueAnimator.ofFloat(totalHeight, 0);
            anim.setDuration(mSpringBackAnimationTimeDelay).start();
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mPullUpY = (float) animation.getAnimatedValue();
                    requestLayout();
                }
            });
        }
    }

    /**
     * 下拉刷新，手指释放后执行
     */
    private void springBackAnimationToRefresh() {
        float startHeight = mPullDownY;
        float endHeight = mHeaderHeight;

        ValueAnimator anim = ValueAnimator.ofFloat(startHeight, endHeight);
        anim.setDuration(mSpringBackAnimationTimeDelayForRelease).start();
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPullDownY = (float) animation.getAnimatedValue();
                requestLayout();
            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mStatus = Status.REFRESHING;
                if (mHeaderView instanceof Header) {
                    ((Header) mHeaderView).onRefreshing();
                }

                if (null != mOnStatusListener) {
                    mOnStatusListener.onRefreshing();
                }

                mPullDownY = mHeaderHeight;
                // 刷新操作
                if (mOnRefreshListener != null) {
                    mOnRefreshListener.onRefresh();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    /**
     * 上拉加载，手指释放后执行
     */
    private void springBackAnimationToLoad() {
        float startHeight = mPullUpY;
        float endHeight = -mFooterHeight;

        ValueAnimator anim = ValueAnimator.ofFloat(startHeight, endHeight);
        anim.setDuration(mSpringBackAnimationTimeDelayForRelease).start();
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPullUpY = (float) animation.getAnimatedValue();
                requestLayout();
            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mStatus = Status.LOADING;
                if (mFooterView instanceof Footer) {
                    ((Footer) mFooterView).onLoading();
                }

                if (null != mOnStatusListener) {
                    mOnStatusListener.onLoading();
                }

                mPullUpY = -mFooterHeight;
                // 加载操作
                if (mOnRefreshListener != null) {
                    mOnRefreshListener.onLoadMore();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    public void onRefreshComplete() {
        springBackAnimation();
        requestLayout();
        mStatus = Status.INIT;

        if (null != mHeaderView && mHeaderView instanceof Header) {
            ((Header) mHeaderView).onFinish();
        }

        if (null != mOnStatusListener) {
            mOnStatusListener.onRefreshFinish();
        }
    }

    public void onLoadMoreComplete() {
        springBackAnimation();
        requestLayout();
        mStatus = Status.INIT;

        if (null != mFooterView && mFooterView instanceof Footer) {
            ((Footer) mFooterView).onFinish();
        }

        if (null != mOnStatusListener) {
            mOnStatusListener.onLoadFinish();
        }
    }

    /**
     * 回调监听刷新和加载更多
     */
    private OnRefreshListener mOnRefreshListener;

    /**
     * 回调监听所有的动作状态
     */
    private OnStatusListener mOnStatusListener;

    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefreshListener = listener;
    }

    public void setOnStatusListener(OnStatusListener listener) {
        mOnStatusListener = listener;
    }

    public interface OnRefreshListener {
        void onRefresh();

        void onLoadMore();
    }

    public interface OnStatusListener {
        void onRefreshInit();

        void onPrepareToRefresh();

        void onRefreshing();

        void onRefreshFinish();

        void onLoadInit();

        void onPrepareToLoadMore();

        void onLoading();

        void onLoadFinish();
    }

    /**
     * 请求拦截事件
     *
     * @param ev MotionEvent
     * @return 是否拦截事件
     */
    private boolean requestInterceptTouchEvent(MotionEvent ev) {
        if (mContentView instanceof WebView || mContentView instanceof ScrollView) {
            Rect local = new Rect();
            mContentView.getLocalVisibleRect(local);
            //TODO 1、向下滚动时，若没有到达webView的底部，则拦截事件  2、当向上滑动时，若没有到达顶部，则拦截事件
            int scrollY = mContentView.getScrollY();
            float newY = ev.getY() - mLastY;
            float total = mContentView.getPivotY() + mContentView.getMeasuredHeight();
            if ((scrollY > 0 && newY > 0) || ((local.bottom < total) && newY < 0)) {
                mLastY = ev.getY();
                return super.dispatchTouchEvent(ev);
            }
        }
        return false;
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return ev.getActionMasked() == MotionEvent.ACTION_DOWN && mStatus == Status.LOADING  || super.onInterceptTouchEvent(ev);
//    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        Log.d(TAG, "requestDisallowInterceptTouchEvent()");
//        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }
}