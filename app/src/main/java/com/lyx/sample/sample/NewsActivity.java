package com.lyx.sample.sample;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lyx.sample.R;
import com.lyx.sample.adapter.CommonAdapter;
import com.lyx.sample.adapter.ViewHolder;
import com.lyx.sample.annotation.Id;
import com.lyx.sample.annotation.IdParser;
import com.lyx.sample.entity.News;
import com.lyx.sample.utils.FrescoUtils;
import com.lyx.refresh.widget.RefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class NewsActivity extends AppCompatActivity {
    @Id(id = R.id.refresh_layout_news)
    private RefreshLayout mRefreshLayout;
    @Id(id = R.id.lv_news)
    private ListView mListView;
    @Id(id = R.id.iv_refresh_news)
    private SimpleDraweeView mRefreshIV;
    @Id(id = R.id.iv_load_news)
    private SimpleDraweeView mLoadIV;

    private CommonAdapter<News> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        IdParser.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_news);
        toolbar.setTitle("新闻动态");
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

        FrescoUtils.loadImage(this, mRefreshIV, "res://com.lyx.refresh/" + R.mipmap.ic_nes_refresh, 172, 200);
        FrescoUtils.loadImage(this, mLoadIV, "res://com.lyx.refresh/" + R.mipmap.ic_nes_load, 60, 111);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter = new CommonAdapter<News>(this, new ArrayList<News>(), R.layout.item_news) {
            @Override
            public void convert(ViewHolder helper, News info) {
                helper.setText(R.id.tv_news_title, info.getTitle());

                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                helper.setText(R.id.tv_news_source, info.getSource() + "  " + format.format(new Date(info.getTime())));

                SimpleDraweeView image = helper.getView(R.id.iv_news_image);
                image.setImageURI(Uri.parse(info.getImage()));
            }
        };
        mListView.setAdapter(mAdapter);
        mAdapter.addAll(News.getNewsList());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRefreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mHandler.sendEmptyMessageDelayed(0x101, 3000);
            }

            @Override
            public void onLoadMore() {
                mHandler.sendEmptyMessageDelayed(0x102, 3000);
            }
        });
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x101:
                    mAdapter.clear();
                    mAdapter.addAll(News.getNewsList());
                    mRefreshLayout.onRefreshComplete();
                    break;
                case 0x102:
                    mAdapter.addAll(News.getNewsList());
                    mRefreshLayout.onLoadMoreComplete();
                    break;
            }
            super.handleMessage(msg);
        }
    };
}