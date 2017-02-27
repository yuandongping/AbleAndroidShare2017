package com.able.share.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.able.share.R;
import com.able.share.ShareActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ShareStaticUtils {

    public static final String TAG = "ShareStaticUtils";

    public static int sysWidth = 0;
    public static int sysHeight = 0;


    /**
     * 获取手机的分比率，高和宽
     */
    public static void getScreen(Activity activity) {
        if (ShareStaticUtils.sysWidth <= 0 || ShareStaticUtils.sysHeight <= 0) {
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            ShareStaticUtils.sysWidth = dm.widthPixels;
            ShareStaticUtils.sysHeight = dm.heightPixels;
        }
    }




    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 震动一下
     */
    public static void virbate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }


    /**
     * 获取Assect文件中的内容
     */
    public static String getStringAssetsData(Context context, String path) {
        String json = null;
        AssetManager am = context.getAssets();
        try {
            InputStream is = am.open(path);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] bt = new byte[1024];
            int len = 0;
            while ((len = is.read(bt)) != -1) {
                outputStream.write(bt, 0, len);
            }
            outputStream.close();
            is.close();
            return json = outputStream.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public static void startShare(Activity context, ShareContext shareContext) {
//        public static String shareText = "我要分享，這是我的分享內容。";
//        public static String imagePath = "https://file04.easesales.com/upload/82A0021F4BF7AB8F/201611/5746723013590553903.jpg";
//        public static ArrayList<Uri> imageUris;

        Intent intent = new Intent(context, ShareActivity.class);
        intent.putExtra("shareText", shareContext.getShareText());
        intent.putExtra("imagePath", shareContext.getImagePath());
        intent.putExtra("description", shareContext.getDescription());
        intent.putExtra("shareUrl", shareContext.getShareUrl());
        if (shareContext.getImageUris() != null) {
            intent.putExtra("imageUris", shareContext.getImageUris());
        }
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.share_activity_open, 0);
    }


}
