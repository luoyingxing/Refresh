package com.lyx.sample.sample;

import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lyx.sample.R;
import com.lyx.sample.annotation.Id;
import com.lyx.sample.annotation.IdParser;
import com.lyx.sample.entity.Image;
import com.lyx.sample.recycler.ViewHolder;
import com.lyx.sample.recycler.XAdapter;
import com.lyx.sample.utils.DpiUtils;
import com.lyx.sample.utils.FrescoUtils;
import com.lyx.refresh.widget.FooterLayout;
import com.lyx.refresh.widget.RefreshLayout;

import java.util.ArrayList;

public class FooterActivity extends AppCompatActivity {
    @Id(id = R.id.refresh_layout_footer)
    private RefreshLayout mRefreshLayout;
    @Id(id = R.id.footer_layout)
    private FooterLayout mFooterLayout;
    @Id(id = R.id.progress_bar)
    private ProgressBar mProgressBar;
    @Id(id = R.id.iv_loading)
    private SimpleDraweeView mImageView;
    @Id(id = R.id.rv_footer)
    private RecyclerView mRecyclerView;

    private XAdapter<Image> mXAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footer);
        IdParser.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_footer);
        toolbar.setTitle("自定义Footer");
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
        final int height = width / 4 * 3;

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

        mFooterLayout.setOnPullingListener(new FooterLayout.OnPullingListener() {
            @Override
            public void onPullingUp(float percent, float pullHeight, int footerHeight, int extendHeight) {
                int per = (int) (percent * 100);
                mProgressBar.setProgress(per);
            }

        });

        FrescoUtils.loadImage(this, mImageView, "res://com.lyx.refresh/" + R.mipmap.ic_moon);

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