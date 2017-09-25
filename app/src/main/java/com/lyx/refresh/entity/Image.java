package com.lyx.refresh.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Image
 * <p>
 * Created by luoyingxing on 2017/9/25.
 */

public class Image {
    private String title;
    private String url;

    public Image() {
    }

    public Image(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public static List<Image> getImageList() {
        List<Image> list = new ArrayList<>();
        list.add(new Image(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img5.imgtn.bdimg.com/it/u=3255078302,3579388021&fm=200&gp=0.jpg"));
        list.add(new Image(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img4.imgtn.bdimg.com/it/u=913403609,2243190407&fm=27&gp=0.jpg"));
        list.add(new Image(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img2.imgtn.bdimg.com/it/u=2116257978,594320936&fm=27&gp=0.jpg    "));
        list.add(new Image(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img3.imgtn.bdimg.com/it/u=428230071,3244237116&fm=200&gp=0.jpg"));
        list.add(new Image(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img5.imgtn.bdimg.com/it/u=1364942285,2796934020&fm=27&gp=0.jpg   "));
        list.add(new Image(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img4.imgtn.bdimg.com/it/u=1057879844,60572288&fm=200&gp=0.jpg    "));
        list.add(new Image(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img1.imgtn.bdimg.com/it/u=350286811,2708244372&fm=200&gp=0.jpg"));
        list.add(new Image(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img1.imgtn.bdimg.com/it/u=2487503658,1494418591&fm=200&gp=0.jpg"));
        list.add(new Image(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img4.imgtn.bdimg.com/it/u=2017651482,888909736&fm=200&gp=0.jpg"));
        list.add(new Image(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img5.imgtn.bdimg.com/it/u=4127645007,1273356270&fm=27&gp=0.jpg"));
        list.add(new Image(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img1.imgtn.bdimg.com/it/u=2683431478,4189181113&fm=27&gp=0.jpg"));
        list.add(new Image(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img3.imgtn.bdimg.com/it/u=2035340593,2888279388&fm=27&gp=0.jpg"));
        list.add(new Image(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img0.imgtn.bdimg.com/it/u=394962981,3860542191&fm=27&gp=0.jpg"));
        list.add(new Image(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img0.imgtn.bdimg.com/it/u=3523099303,3893734731&fm=27&gp=0.jpg"));
        list.add(new Image(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img5.imgtn.bdimg.com/it/u=1168105733,202741434&fm=27&gp=0.jpg"));
        list.add(new Image(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img5.imgtn.bdimg.com/it/u=751652762,2322387069&fm=27&gp=0.jpg"));
        list.add(new Image(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())), "http://img5.imgtn.bdimg.com/it/u=2408941929,751522124&fm=11&gp=0.jpg"));
        return list;
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
