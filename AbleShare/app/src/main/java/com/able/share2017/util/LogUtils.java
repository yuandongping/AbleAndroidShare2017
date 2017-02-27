package com.able.share2017.util;


import android.util.Log;

/**
 * ======================================================
 * Created by able-android -周晓明 on 2016/5/6.
 * <p>
 * 版权所有，违者必究！
 * <详情描述/>
 */
public class LogUtils {
    public static void setLog(String name, String text) {
        Log.v("log", "==" + name + "==" + text);
    }

    public static void setTag(String tag, String text) {
        Log.v(tag,  text);
    }

    //{"OrderNo":"T160600038","point":["未付款","已完成"],"state":0,"productSum":1,"products":[{"ProductName":"Six god","Quantity":1,"Price":"HKD 0.02","ProductImg":"https://file01.easesales.com/upload/CB769F2A7DC7F476/201604/4730682403558410498.jpg","propertys":[{"Property":"容量","PropertyValue":"100ml"},{"Property":"six god","PropertyValue":""}]}]}
}
