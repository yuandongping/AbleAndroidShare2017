package com.able.share.util;

import android.net.Uri;

import java.util.ArrayList;

/**
 * ======================================================
 * Created by Administrator able_fingerth on 2016/12/6.
 * <p/>
 * 版权所有，违者必究！
 * <详情描述/>
 */
public class ShareContext {

    private String shareText;//= "我要分享，這是我的分享內容。";
    private String imagePath;//= "https://file04.easesales.com/upload/82A0021F4BF7AB8F/201611/5746723013590553903.jpg";
    private ArrayList<Uri> imageUris;

    private String description;  //描述，微信網頁分享用到
    private String shareUrl;


    public ShareContext(String shareText, String imagePath, ArrayList<Uri> imageUris, String description, String shareUrl) {
        this.shareText = shareText;
        this.imagePath = imagePath;
        this.imageUris = imageUris;
        this.description = description;
        this.shareUrl = shareUrl;
    }


    public String getShareText() {
        return shareText;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Uri> getImageUris() {
        return imageUris;
    }

    public String getShareUrl() {
        return shareUrl;
    }
}
