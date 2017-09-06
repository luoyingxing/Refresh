package com.lyx.refresh;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lyx.refresh.adapter.CommonAdapter;
import com.lyx.refresh.adapter.ViewHolder;
import com.lyx.refresh.entity.Info;
import com.lyx.refresh.view.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RefreshLayout layout;
    private ListView listView;
    private TextView refreshTV;
    private TextView loadTV;
    private CommonAdapter<Info> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("简单使用");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        layout = (RefreshLayout) findViewById(R.id.refresh_layout);
        listView = (ListView) findViewById(R.id.list_view);
        refreshTV = (TextView) findViewById(R.id.tv_refresh);
        loadTV = (TextView) findViewById(R.id.tv_load);

        layout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mHandler.sendMessageDelayed(mHandler.obtainMessage(), 2000);
                adapter.clear();
            }

            @Override
            public void onLoadMore() {
                mHandler.sendMessageDelayed(mHandler.obtainMessage(), 2000);
            }
        });

        layout.setOnStatusListener(new RefreshLayout.OnStatusListener() {

            @Override
            public void onRefreshInit() {
                refreshTV.setText("下拉刷新");
            }

            @Override
            public void onPrepareToRefresh() {
                refreshTV.setText("松开刷新");
            }

            @Override
            public void onRefreshing() {
                refreshTV.setText("正在刷新");
            }

            @Override
            public void onRefreshFinish() {
                refreshTV.setText("下拉刷新");
            }

            @Override
            public void onLoadInit() {
                loadTV.setText("上拉加载更多");
            }

            @Override
            public void onPrepareToLoadMore() {
                loadTV.setText("松开加载更多");
            }

            @Override
            public void onLoading() {
                loadTV.setText("正在加载");
            }

            @Override
            public void onLoadFinish() {
                loadTV.setText("上拉加载更多");
            }
        });

        adapter = new CommonAdapter<Info>(this, new ArrayList<Info>(), R.layout.item_recycler_view) {
            @Override
            public void convert(ViewHolder helper, Info info) {
                helper.setText(R.id.text, info.getInfoId() + ". " + info.getTitle());
                SimpleDraweeView imageView = helper.getView(R.id.image);
                imageView.setImageURI(Uri.parse(info.getUrl()));
            }
        };
        listView.setAdapter(adapter);

        adapter.addAll(getDataList());
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter.addAll(getDataList());
            layout.onRefreshComplete();
            layout.onLoadMoreComplete();
        }
    };

    private List<Info> getDataList() {
        List<Info> list = new ArrayList<>();
        list.add(new Info(3, "壁纸_海量高清精选", "http://img3.imgtn.bdimg.com/it/u=4150026489,1943935114&fm=26&gp=0.jpg"));
        list.add(new Info(3, "壁纸_海量高清精选", "http://img4.imgtn.bdimg.com/it/u=2787880723,3061645111&fm=26&gp=0.jpg"));
        list.add(new Info(3, "壁纸_海量高清精选", "http://img3.imgtn.bdimg.com/it/u=165337335,1524178590&fm=26&gp=0.jpg"));
        list.add(new Info(3, "壁纸_海量高清精选", "http://img0.imgtn.bdimg.com/it/u=442340884,1909164320&fm=26&gp=0.jpg"));
        list.add(new Info(3, "壁纸_海量高清精选", "http://img3.imgtn.bdimg.com/it/u=3407380450,178139315&fm=26&gp=0.jpg"));
        list.add(new Info(3, "壁纸_海量高清精选", "http://img3.imgtn.bdimg.com/it/u=1576567003,3635369844&fm=26&gp=0.jpg"));
        list.add(new Info(3, "壁纸_海量高清精选", "http://img4.imgtn.bdimg.com/it/u=766879173,2974934941&fm=26&gp=0.jpg"));
        list.add(new Info(3, "壁纸_海量高清精选", "http://img5.imgtn.bdimg.com/it/u=2950298496,1136505419&fm=26&gp=0.jpg"));
        list.add(new Info(3, "壁纸_海量高清精选", "http://img2.imgtn.bdimg.com/it/u=1447822691,2273074389&fm=26&gp=0.jpg"));
        list.add(new Info(3, "壁纸_海量高清精选", "http://img4.imgtn.bdimg.com/it/u=1711927594,1272105846&fm=26&gp=0.jpg"));
        list.add(new Info(3, "壁纸_海量高清精选", "http://img1.imgtn.bdimg.com/it/u=1542403978,833282943&fm=26&gp=0.jpg"));
        list.add(new Info(3, "壁纸_海量高清精选", "http://img4.imgtn.bdimg.com/it/u=156396060,3599398840&fm=26&gp=0.jpg"));
        return list;
    }

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}