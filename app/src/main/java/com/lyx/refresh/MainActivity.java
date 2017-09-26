package com.lyx.refresh;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lyx.refresh.adapter.CommonAdapter;
import com.lyx.refresh.adapter.ViewHolder;
import com.lyx.refresh.annotation.Id;
import com.lyx.refresh.annotation.IdParser;
import com.lyx.refresh.entity.Info;
import com.lyx.refresh.sample.FooterActivity;
import com.lyx.refresh.sample.HeaderActivity;
import com.lyx.refresh.sample.ImageActivity;
import com.lyx.refresh.sample.NewsActivity;
import com.lyx.refresh.sample.PersonalActivity;
import com.lyx.refresh.sample.WebActivity;
import com.lyx.refresh.view.RefreshLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Id(id = R.id.refresh_layout)
    private RefreshLayout mRefreshLayout;
    @Id(id = R.id.list_view)
    private ListView mListView;
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
    private ImageView mAvatarIV;

    private CommonAdapter<Info> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IdParser.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("简单使用");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        mAvatarIV = (ImageView) headerLayout.findViewById(R.id.iv_avatar);
        mAvatarIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PersonalActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
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

        mAdapter = new CommonAdapter<Info>(this, new ArrayList<Info>(), R.layout.item_recycler_view) {
            @Override
            public void convert(ViewHolder helper, Info info) {
                helper.setText(R.id.text, info.getInfoId() + ". " + info.getTitle());
                SimpleDraweeView imageView = helper.getView(R.id.image);
                imageView.setImageURI(Uri.parse(info.getUrl()));
            }
        };
        mListView.setAdapter(mAdapter);

        mAdapter.addAll(Info.getDataList());
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x101:
                    mAdapter.clear();
                    mAdapter.addAll(Info.getDataList());
                    mRefreshLayout.onRefreshComplete();
                    break;
                case 0x102:
                    mAdapter.addAll(Info.getDataList());
                    mRefreshLayout.onLoadMoreComplete();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_personal) {
            startActivity(new Intent(MainActivity.this, PersonalActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_personal) {
            startActivity(new Intent(MainActivity.this, PersonalActivity.class));
        } else if (id == R.id.nav_news) {
            startActivity(new Intent(MainActivity.this, NewsActivity.class));
        } else if (id == R.id.nav_web) {
            startActivity(new Intent(MainActivity.this, WebActivity.class));
        } else if (id == R.id.nav_image) {
            startActivity(new Intent(MainActivity.this, ImageActivity.class));
        } else if (id == R.id.nav_header) {
            startActivity(new Intent(MainActivity.this, HeaderActivity.class));
        } else if (id == R.id.nav_footer) {
            startActivity(new Intent(MainActivity.this, FooterActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}