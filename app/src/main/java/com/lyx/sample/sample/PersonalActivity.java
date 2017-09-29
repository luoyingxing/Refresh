package com.lyx.sample.sample;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.lyx.sample.R;
import com.lyx.sample.adapter.CommonAdapter;
import com.lyx.sample.adapter.ViewHolder;
import com.lyx.sample.annotation.Id;
import com.lyx.sample.annotation.IdParser;
import com.lyx.sample.entity.Setting;

import java.util.ArrayList;


public class PersonalActivity extends AppCompatActivity {
    @Id(id = R.id.lv_personal)
    private ListView mListView;

    private CommonAdapter<Setting> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        IdParser.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_personal);
        toolbar.setTitle("个人中心");
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
        mAdapter = new CommonAdapter<Setting>(this, new ArrayList<Setting>(), R.layout.item_personal_setting) {
            @Override
            public void convert(ViewHolder helper, Setting info) {
                helper.setText(R.id.tv_setting_title, info.getTitle());
                helper.setImageResource(R.id.iv_setting_icon, info.getIcon());
            }
        };
        mListView.setAdapter(mAdapter);

        mAdapter.addAll(Setting.getSettingList());
    }
}