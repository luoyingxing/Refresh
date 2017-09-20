package com.lyx.refresh.entity;

import com.lyx.refresh.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Setting
 * <p>
 * Created by luoyingxing on 2017/9/20.
 */

public class Setting {
    private String title;
    private int icon;

    public Setting() {
    }

    public Setting(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public static List<Setting> getSettingList() {
        List<Setting> list = new ArrayList<>();
        list.add(new Setting("我的消息", R.mipmap.ic_setting_message));
        list.add(new Setting("美图相册", R.mipmap.ic_setting_gril));
        list.add(new Setting("我的分享", R.mipmap.ic_setting_share));
        list.add(new Setting("疑问解答", R.mipmap.ic_setting_help));
        return list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
