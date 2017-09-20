package com.lyx.refresh.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * News
 * <p>
 * Created by luoyingxing on 2017/9/20.
 */

public class News {
    private String title;
    private String image;
    private String source;
    private long time;

    public News() {
    }

    public News(String title, String image, String source, long time) {
        this.title = title;
        this.image = image;
        this.source = source;
        this.time = time;
    }

    public static List<News> getNewsList() {
        List<News> list = new ArrayList<>();
        list.add(new News("IPO太严，借壳太贵，体育企业上市困难重重", "http://images.tmtpost.com/uploads/images/2017/09/aa6d3a7b12f3da5b7d28ef4e5773e4e115058914736065.jpg?imageMogr2/quality/85/thumbnail/660x460/gravity/center/crop/!660x340&ext=.jpg", "体育产业生态圈", System.currentTimeMillis()));
        list.add(new News("胡彦斌：3年收获400个员工 一个月工资几百万", "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1520536669,2132767062&fm=27&gp=0.jpg", "网易", System.currentTimeMillis()));
        list.add(new News("杜兰特为自己言论道歉 湖人官宣博古特加盟", "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2063821736,604156735&fm=27&gp=0.jpg", "篮球晚报", System.currentTimeMillis()));
        list.add(new News("国安主场战上港海报：迎队史800场里程碑", "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2184684013,876079282&fm=11&gp=0.jpg", "微博", System.currentTimeMillis()));
        list.add(new News("暴力鸟：要回馈巴萨信任 在梅西身边踢球太赞", "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3670039759,2467258570&fm=111&gp=0.jpg", "腾讯体育", System.currentTimeMillis()));
        list.add(new News("前富力主帅将执教罗马尼亚国家队 直言梦想成真", "https://b.bdstatic.com/boxlib/20170920/2017092017303589542796276.jpg", "腾讯体育", System.currentTimeMillis()));
        list.add(new News("鼓浪屿：听罢琴声听涛声的“中国最美城区”", "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1324034082,3055821221&fm=27&gp=0.jpg", "中国新闻网", System.currentTimeMillis()));
        return list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}