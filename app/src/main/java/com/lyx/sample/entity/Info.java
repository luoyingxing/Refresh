package com.lyx.sample.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Info implements Serializable {
    private Integer infoId;
    private String title;
    private String url;

    public Info() {
    }

    public Info(Integer infoId, String title, String url) {
        this.infoId = infoId;
        this.title = title;
        this.url = url;
    }

    public static List<Info> getDataList() {
        List<Info> list = new ArrayList<>();
        list.add(new Info(1, "壁纸_海量高清精选1", "http://img3.imgtn.bdimg.com/it/u=4150026489,1943935114&fm=26&gp=0.jpg"));
        list.add(new Info(2, "壁纸_海量高清精选2", "http://img4.imgtn.bdimg.com/it/u=2787880723,3061645111&fm=26&gp=0.jpg"));
        list.add(new Info(3, "壁纸_海量高清精选3", "http://img3.imgtn.bdimg.com/it/u=165337335,1524178590&fm=26&gp=0.jpg"));
        list.add(new Info(4, "壁纸_海量高清精选4", "http://img0.imgtn.bdimg.com/it/u=442340884,1909164320&fm=26&gp=0.jpg"));
        list.add(new Info(5, "壁纸_海量高清精选5", "http://img3.imgtn.bdimg.com/it/u=3407380450,178139315&fm=26&gp=0.jpg"));
        list.add(new Info(6, "壁纸_海量高清精选6", "http://img3.imgtn.bdimg.com/it/u=1576567003,3635369844&fm=26&gp=0.jpg"));
        list.add(new Info(7, "壁纸_海量高清精选7", "http://img4.imgtn.bdimg.com/it/u=766879173,2974934941&fm=26&gp=0.jpg"));
        list.add(new Info(8, "壁纸_海量高清精选8", "http://img5.imgtn.bdimg.com/it/u=2950298496,1136505419&fm=26&gp=0.jpg"));
        list.add(new Info(9, "壁纸_海量高清精选9", "http://img2.imgtn.bdimg.com/it/u=1447822691,2273074389&fm=26&gp=0.jpg"));
        list.add(new Info(10, "壁纸_海量高清精选10", "http://img4.imgtn.bdimg.com/it/u=1711927594,1272105846&fm=26&gp=0.jpg"));
        list.add(new Info(11, "壁纸_海量高清精选11", "http://img1.imgtn.bdimg.com/it/u=1542403978,833282943&fm=26&gp=0.jpg"));
        list.add(new Info(12, "壁纸_海量高清精选12", "http://img4.imgtn.bdimg.com/it/u=156396060,3599398840&fm=26&gp=0.jpg"));
        return list;
    }

    public Integer getInfoId() {
        return infoId;
    }

    public void setInfoId(Integer infoId) {
        this.infoId = infoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}