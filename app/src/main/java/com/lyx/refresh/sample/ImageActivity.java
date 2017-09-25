package com.lyx.refresh.sample;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lyx.refresh.R;
import com.lyx.refresh.annotation.Id;
import com.lyx.refresh.annotation.IdParser;
import com.lyx.refresh.entity.Image;
import com.lyx.refresh.recycler.ViewHolder;
import com.lyx.refresh.recycler.XAdapter;
import com.lyx.refresh.utils.DpiUtils;
import com.lyx.refresh.utils.FrescoBuilder;
import com.lyx.refresh.view.RefreshLayout;

import java.util.ArrayList;

public class ImageActivity extends AppCompatActivity {
    @Id(id = R.id.refresh_layout_image)
    private RefreshLayout mRefreshLayout;
    @Id(id = R.id.rv_image)
    private RecyclerView mRecyclerView;
    @Id(id = R.id.tv_refresh)
    private TextView mRefreshTV;
    @Id(id = R.id.tv_load)
    private TextView mLoadTV;
    @Id(id = R.id.iv_refresh)
    private ImageView mRefreshIV;
    @Id(id = R.id.iv_load)
    private ImageView mLoadIV;
    @Id(id = R.id.pb_refresh)
    private ProgressBar mRefreshBar;
    @Id(id = R.id.pb_load)
    private ProgressBar mLoadBar;

    private XAdapter<Image> mXAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        IdParser.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_image);
        toolbar.setTitle("图片相册");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        mXAdapter = new XAdapter<Image>(this, new ArrayList<Image>(), R.layout.item_image_list) {
            @Override
            public void convert(ViewHolder holder, Image item) {
                holder.setText(R.id.tv_image_title, "相册 " + item.getTitle());

                SimpleDraweeView imageView = holder.getView(R.id.iv_image_image);
                new FrescoBuilder(ImageActivity.this, imageView, item.getUrl()) {
                    @Override
                    public double reSize(int imageWidth, int imageHeight) {
                        return ((DpiUtils.getWidth() - DpiUtils.dipTopx(2)) * 1.0) / 2 / imageWidth;
                    }
                }.builder();
            }
        };

        mRecyclerView.setAdapter(mXAdapter);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(2));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        ((StaggeredGridLayoutManager) mRecyclerView.getLayoutManager()).setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //防止第一行到顶部有空白区域
                ((StaggeredGridLayoutManager) mRecyclerView.getLayoutManager()).invalidateSpanAssignments();
            }
        });


        mXAdapter.setOnItemClickListener(new XAdapter.OnItemClickListeners<Image>() {
            @Override
            public void onItemClick(ViewHolder holder, Image item, int position) {
                Toast.makeText(ImageActivity.this, "第" + position + "张", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(ViewHolder holder, Image item, int position) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mXAdapter.addAll(Image.getImageList());

        mRefreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mHandler.sendEmptyMessageDelayed(0x101, 2000);
            }

            @Override
            public void onLoadMore() {
                mHandler.sendEmptyMessageDelayed(0x102, 2000);
            }
        });

        //头部和底部如果没有实现Footer 或者 Header, 可以通过监听 RefreshLayout 的 OnStatusListener事件来处理顶部和底部的一些操作
        mRefreshLayout.setOnStatusListener(new RefreshLayout.OnStatusListener() {

            @Override
            public void onRefreshInit() {
                mRefreshTV.setText("下拉刷新");
                mRefreshBar.setVisibility(View.INVISIBLE);
                mRefreshIV.setVisibility(View.VISIBLE);
                mRefreshIV.setImageResource(R.drawable.ic_refresh_arrow_down_white);
            }

            @Override
            public void onPrepareToRefresh() {
                mRefreshTV.setText("松开刷新");
                mRefreshIV.setImageResource(R.drawable.ic_refresh_arrow_up_white);
            }

            @Override
            public void onRefreshing() {
                mRefreshTV.setText("正在刷新");
                mRefreshBar.setVisibility(View.VISIBLE);
                mRefreshIV.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onRefreshFinish() {
                mRefreshTV.setText("下拉刷新");
                mRefreshBar.setVisibility(View.INVISIBLE);
                mRefreshIV.setVisibility(View.VISIBLE);
                mRefreshIV.setImageResource(R.drawable.ic_refresh_arrow_down_white);
            }

            @Override
            public void onLoadInit() {
                mLoadTV.setText("上拉加载更多");
                mLoadBar.setVisibility(View.INVISIBLE);
                mLoadIV.setVisibility(View.VISIBLE);
                mLoadIV.setImageResource(R.drawable.ic_refresh_arrow_up_white);
            }

            @Override
            public void onPrepareToLoadMore() {
                mLoadTV.setText("松开加载更多");
                mLoadIV.setImageResource(R.drawable.ic_refresh_arrow_down_white);
            }

            @Override
            public void onLoading() {
                mLoadTV.setText("正在加载");
                mLoadBar.setVisibility(View.VISIBLE);
                mLoadIV.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadFinish() {
                mLoadTV.setText("上拉加载更多");
                mLoadBar.setVisibility(View.INVISIBLE);
                mLoadIV.setVisibility(View.VISIBLE);
                mLoadIV.setImageResource(R.drawable.ic_refresh_arrow_up_white);
            }
        });
    }


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x101:
                    mXAdapter.clear();
                    mXAdapter.addAll(Image.getImageList());
                    mRefreshLayout.onRefreshComplete();
                    break;
                case 0x102:
                    mXAdapter.addAll(Image.getImageList());
                    mRefreshLayout.onLoadMoreComplete();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        int mSpace;

        public SpaceItemDecoration(int space) {
            this.mSpace = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int itemCount = mXAdapter.getItemCount();
            int pos = parent.getChildAdapterPosition(view);

            outRect.left = mSpace;
            outRect.top = mSpace;
            outRect.bottom = mSpace;

            if (pos != (itemCount - 1)) {
                outRect.right = mSpace;
            } else {
                outRect.right = 0;
            }
        }
    }
}
