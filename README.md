# Refresh
## 功能描述（JDK1.8）
Refresh是一个支持下拉刷新和上拉加载的智能库，核心布局RefreshLayout，继承ViewGroup，支持嵌套ListView、GridView、RecyclerView，刷新布局和加载布局随你定义，
可以是普通的View或ViewGroup。若需要监听下拉和上拉的值属性变化，可以自定义View或者ViewGroup，实现Header和Footer接口即可。核心回调函数是：onPullingDown（）和
onPullingUp（），RefreshLayout远不止实现上拉和加载功能，还可实现回弹效果，支持WebView、ScrollView。具体使用请参考demo，代码不多，轻量入门级。
 
## 版本描述
暂无。
 
## gradle
暂不支持gradle配置。
 
## 使用
build.gradle需支持以下依赖:
- compile 'com.android.support:design:25.3.1'

## 效果图
![](https://github.com/luoyingxing/image/blob/master/base.gif)
![](https://github.com/luoyingxing/image/blob/master/personal.gif)
![](https://github.com/luoyingxing/image/blob/master/github.gif)
![](https://github.com/luoyingxing/image/blob/master/recyclerView.gif)
![](https://github.com/luoyingxing/image/blob/master/news.gif)
![](https://github.com/luoyingxing/image/blob/master/header.gif)
![](https://github.com/luoyingxing/image/blob/master/footer.gif)

## 简单使用
- 在main.xml文件中声明：

        <com.lyx.refresh.view.RefreshLayout
            xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:app="http://schemas.android.com/apk/res-auto"
            xmlns:tools="http://schemas.android.com/tools"
            android:id="@+id/refresh_layout"
            android:layout_width="match_parent"
            android:layout_height="match_parent">
  
        <include layout="@layout/layout_main_refresh_header" />
    
        <ListView
            android:id="@+id/list_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="@android:color/white"
            android:divider="@color/colorAccent"
            android:dividerHeight="1dp" />

        <include layout="@layout/layout_main_refresh_footer" />
    
        </com.lyx.refresh.view.RefreshLayout>

- RefreshLayout在xml中可以配置以下参数：

- 当RefreshLayout只包含两个子View的时候，需要通过pullViewPosition指明第几个是需要滚动的View。
<code>app:pullViewPosition = "first" </code>

- 当RefreshLayout包含WebView或者ScrollView的时候，需要通过spring指明该View是只允许上拉（pull）还是下拉（down），或者都允许（all）。
<code>app:spring = "all" </code>

- 在MainActivity.java中直接设置监听事件:

         mRefreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
               //在这里进行刷新操作
            }

            @Override
            public void onLoadMore() {
                //在这里进行加载操作
            }
        });

- 另外如果你想监听和处理刷新和加载布局的变化，你可以设置监听OnStatusListener：

        mRefreshLayout.setOnStatusListener(new RefreshLayout.OnStatusListener() {

            @Override
            public void onRefreshInit() {
            }

            @Override
            public void onPrepareToRefresh() {
            }

            @Override
            public void onRefreshing() {
            }

            @Override
            public void onRefreshFinish() {
            }

            @Override
            public void onLoadInit() {
            }

            @Override
            public void onPrepareToLoadMore() {
            }

            @Override
            public void onLoading() {
            }

            @Override
            public void onLoadFinish() {
            }
        });

## 自定义刷新头

- 实现Header接口就行，例如HeaderLayout.java：

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

- 然后在Activity中实现你自定义的HeaderLayout的监听方法：

        mHeaderLayout.setOnPullingListener(new HeaderLayout.OnPullingListener() {
            @Override
            public void onPulling(float percent, float pullHeight, int headerHeight, int extendHeight) {
                //监听下拉属性值的变化，做响应的处理
            }
        });
        
       mHeaderLayout.setOnStatusListener(new HeaderLayout.OnStatusListener() {

            @Override
            public void onInit() {
            }

            @Override
            public void onPrepareToRefresh() {
            }

            @Override
            public void onRefreshing() {
            }

            @Override
            public void onFinish() {
            }
        });

## 自定义Footer
跟自定义Header类似，具体请查看demo。

## Q&A
仍待完善。

## 联系
如果有紧急事件可联系作者或加QQ：
- Q Q： 602390502
- 邮箱： luoyingxing@126.com

## Wiki
- [TimeAxis实现时光轴](https://github.com/luoyingxing/TimeAxis.git)
