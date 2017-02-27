package com.able.share2017.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.able.share.util.ShareContext;
import com.able.share.util.ShareStaticUtils;
import com.able.share2017.R;
import com.able.share2017.util.DownImageUtils;
import com.able.share2017.util.VersionUtil;

import java.util.ArrayList;
import java.util.List;

public class ShareActivity extends AppCompatActivity {

    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ImageView iv_share = (ImageView) findViewById(R.id.iv_share);

//        private String shareText;//= "我要分享，這是我的分享內容。";
//        private String imagePath;//= "https://file04.easesales.com/upload/82A0021F4BF7AB8F/201611/5746723013590553903.jpg";
//        private ArrayList<Uri> imageUris;
//
//        private String description;  //描述，微信網頁分享用到
//        private String shareUrl;
        String imagePath1 = "http://pic17.nipic.com/20111122/6759425_152002413138_2.jpg";
        String imagePath2 = "http://pic.58pic.com/58pic/11/79/85/13t58PICsap.jpg";
        String imagePath3 = "http://pic41.nipic.com/20140520/18505720_144032556135_2.jpg";
        String imagePath4 = "http://img0.imgtn.bdimg.com/it/u=2152422253,1846971893&fm=23&gp=0.jpg";
        String imagePath5 = "http://f.hiphotos.baidu.com/image/h%3D360/sign=e105b9f1d61b0ef473e89e58edc651a1/b151f8198618367a9f738e022a738bd4b21ce573.jpg";
        String imagePath6 = "http://g.hiphotos.baidu.com/image/pic/item/4afbfbedab64034f36515206adc379310a551d15.jpg";
        String imagePath7 = "http://e.hiphotos.baidu.com/image/pic/item/7c1ed21b0ef41bd5e1d35af453da81cb39db3d29.jpg";
        list = new ArrayList<>();
        list.add(imagePath1);
        list.add(imagePath2);
        list.add(imagePath3);
        list.add(imagePath4);
        list.add(imagePath5);
        list.add(imagePath6);
        list.add(imagePath7);

        DownImageUtils.downImages(this, list, VersionUtil.getImgFiles());

    }

    public void clickShare(View view) {
        String shareText = "我要分享，這是我的分享內容。";
        String imagePath = "http://g.hiphotos.baidu.com/image/pic/item/4afbfbedab64034f36515206adc379310a551d15.jpg";

        ArrayList<Uri> imageUris = DownImageUtils.getImages(this, list, VersionUtil.getImgFiles());

        String description = "描述，微信網頁分享用到 https://developers.facebook.com";
        String shareUrl = "https://www.baidu.com";

        ShareContext shareContext = new ShareContext(shareText, imagePath, imageUris, description, shareUrl);
        ShareStaticUtils.startShare(this, shareContext);
    }
}
