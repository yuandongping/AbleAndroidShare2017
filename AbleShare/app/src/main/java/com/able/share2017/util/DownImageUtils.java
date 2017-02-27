package com.able.share2017.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.able.share2017.util.permission.PermissionUtils;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ======================================================
 * Created by Administrator able_fingerth on 2017/2/23.
 * <p/>
 * 版权所有，违者必究！
 * <详情描述/>
 */
public class DownImageUtils {
    public final static String TAG = "DownImageUtils";

    public static void downImages(Activity activity, List<String> imagePathList, File imgFiles) {
        if (PermissionUtils.checkWriteExternalStorage(activity)) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                for (int i = 0; i < imagePathList.size(); i++) {
                    String fileName;// /
                    if (imagePathList.get(i).length() > 15) {
                        fileName = imagePathList.get(i).substring(imagePathList.get(i).length() - 15, imagePathList.get(i).length()).trim();
                    } else {
                        fileName = imagePathList.get(i).trim();
                    }

                    final File imageFile = new File(imgFiles, fileName);
                    if (!imageFile.exists()) {
                        RequestDataTool.getInstance(activity).requestImage(imagePathList.get(i), new RequestDataTool.SuccessfulRequestImage() {
                            @Override
                            public void xxBitmap(Bitmap bitmap) {
                                ByteArrayOutputStream output = new ByteArrayOutputStream();//初始化一个流对象
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);//把bitmap100%高质量压缩 到 output对象里
                                bitmap.recycle();//自由选择是否进行回收
                                byte[] result = output.toByteArray();//转换成功了
                                try {
                                    output.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    FileUtils.writeByteArrayToFile(imageFile, result);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new RequestDataTool.FailRequestData() {
                            @Override
                            public void failUrl(String url) {
                                LogUtils.setTag(TAG, "RequestDataTool图片數據失誤url：" + url);
                            }
                        });

                        LogUtils.setTag(TAG, i + "、图片位置" + imageFile.getAbsolutePath());
                    } else {
                        LogUtils.setTag(TAG, i + "、已經下載了，图片位置:" + imageFile.getAbsolutePath());
                    }
                }

            }
        }
    }

    public static ArrayList<Uri> getImages(Activity activity, List<String> imagePathList, File imgFiles) {
        if (PermissionUtils.checkWriteExternalStorage(activity)) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                ArrayList<Uri> imageUris = new ArrayList<>();
                for (int i = 0; i < imagePathList.size(); i++) {
                    String fileName;// /
                    if (imagePathList.get(i).length() > 15) {
                        fileName = imagePathList.get(i).substring(imagePathList.get(i).length() - 15, imagePathList.get(i).length()).trim();
                    } else {
                        fileName = imagePathList.get(i).trim();
                    }
                    File imageFile = new File(imgFiles, fileName);
                    if (imageFile.exists()) {
                        imageUris.add(Uri.fromFile(imageFile)); // Add your image URIs here
                        LogUtils.setTag(TAG, i + "、取出图片位置:" + imageFile);
                    } else {
                        LogUtils.setTag(TAG, i + "、取出图片位置: 不存在！");
                    }
                }

                return imageUris;
            }
        }
//        else {
        //ToastUtils.showToast(this, "去打開文件管理權限");
//        }
        return new ArrayList<>();
    }


}
