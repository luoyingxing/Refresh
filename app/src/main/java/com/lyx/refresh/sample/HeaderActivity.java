package com.lyx.refresh.sample;

import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lyx.refresh.R;
import com.lyx.refresh.annotation.Id;
import com.lyx.refresh.annotation.IdParser;
import com.lyx.refresh.entity.Image;
import com.lyx.refresh.recycler.ViewHolder;
import com.lyx.refresh.recycler.XAdapter;
import com.lyx.refresh.utils.DpiUtils;
import com.lyx.refresh.utils.FrescoUtils;
import com.lyx.refresh.view.HeaderLayout;
import com.lyx.refresh.view.RefreshLayout;

import java.util.ArrayList;

public class HeaderActivity extends AppCompatActivity {
    @Id(id = R.id.refresh_layout_header)
    private RefreshLayout mRefreshLayout;
    @Id(id = R.id.header_layout)
    private HeaderLayout mHeaderLayout;
    @Id(id = R.id.iv_header)
    private SimpleDraweeView mImageView;
    @Id(id = R.id.iv_awards)
    private SimpleDraweeView mAwards;
    @Id(id = R.id.rv_header)
    private RecyclerView mRecyclerView;

    private XAdapter<Image> mXAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header);
        IdParser.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_header);
        toolbar.setTitle("自定义Header");
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
        final int width = (int) (((DpiUtils.getWidth() - DpiUtils.dipTopx(2)) * 1.0) / 2);
        final int height = width / 4 * 5;

        mXAdapter = new XAdapter<Image>(this, new ArrayList<Image>(), R.layout.item_image_list) {
            @Override
            public void convert(ViewHolder holder, Image item) {
                holder.setText(R.id.tv_image_title, "相册 " + item.getTitle());

                SimpleDraweeView imageView = holder.getView(R.id.iv_image_image);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                imageView.setImageURI(Uri.parse(item.getUrl()));
            }
        };

        mRecyclerView.setAdapter(mXAdapter);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(2));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

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

        final int screenWidth = DpiUtils.getWidth();

        mHeaderLayout.setOnPullingListener(new HeaderLayout.OnPullingListener() {
            @Override
            public void onPulling(float percent, float pullHeight, int headerHeight, int extendHeight) {
                int per = (int) (percent * 100);
                mImageView.setX(screenWidth * percent);
            }
        });

        mHeaderLayout.setOnStatusListener(new HeaderLayout.OnStatusListener() {
            @Override
            public void onInit() {
                mAwards.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPrepareToRefresh() {

            }

            @Override
            public void onRefreshing() {
                mAwards.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                mAwards.setVisibility(View.INVISIBLE);
            }
        });

        FrescoUtils.loadImage(this, mAwards, "res://com.lyx.refresh/" + R.mipmap.ic_header_awards);
        FrescoUtils.loadImage(this, mImageView, "res://com.lyx.refresh/" + R.mipmap.ic_header_run, 82, 100);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mXAdapter.addAll(Image.getImageList());
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