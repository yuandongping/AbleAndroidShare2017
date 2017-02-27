package com.able.share2017.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;

/**
 * ======================================================
 * Created by Administrator -周晓明 on 2016/8/8.
 * <p>
 * 版权所有，违者必究！
 * <详情描述/>
 */
public class VersionUtil {
    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
            String versionName = info.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getVersionCode(Activity activity) {
        PackageManager packageManager = activity.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(activity.getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }


    public final static String CACHE_PATH = "ableshare";
    public final static String CACHE_PATH_APK = "ableshare/apk";
    public final static String CACHE_PATH_DEVICEID = "deviceId";
    public final static String CACHE_PATH_IMG = "ableshare/image";

    /**
     * 获取apk所在的文件夹
     *
     * @return x
     */
    public static File getApkFiles() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File dir = Environment.getExternalStorageDirectory();
            File destDir = new File(dir, CACHE_PATH);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            File apkFiles = new File(destDir, "apk");
            if (!apkFiles.exists()) {
                apkFiles.mkdirs();
            }
            return apkFiles;
        }
        return null;
    }
    public static File getImgFiles() {
        File dir = Environment.getExternalStorageDirectory();
        File destDir = new File(dir, CACHE_PATH);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File imgFiles = new File(destDir, "image");
        if (!imgFiles.exists()) {
            imgFiles.mkdirs();
        }
        return imgFiles;
    }

    public static File getDeviceIdFiles() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File dir = Environment.getExternalStorageDirectory();
            File destDir = new File(dir, CACHE_PATH);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            File apkFiles = new File(destDir, CACHE_PATH_DEVICEID);
            if (!apkFiles.exists()) {
                apkFiles.mkdirs();
            }
            return apkFiles;
        }
        return null;
    }

    public static void cleanApkFiles() {
        File destDir = VersionUtil.getApkFiles();
        if (destDir.listFiles() != null && destDir.listFiles().length > 0) {
            for (File f : destDir.listFiles()) {
                if (f.isFile()) {
                    f.delete();
                }
            }
        }
    }

    /**
     * 获取apk文件
     *
     * @param activity xx
     * @return x
     */
    public static File getApkFile(Activity activity) {
        return new File(getApkFiles(), getApkName(activity));
    }

    public static String getApkName(Activity activity) {
        return "update_ableshare_" + getVersionCode(activity) + ".apk";
    }


}
