package com.able.share2017.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StaticUtils {
    public static final String DB_NAME = "talkNews";
    public static int sysWidth = 0;
    public static int sysHeight = 0;



    /**
     * 获取手机的分比率，高和宽
     */
    public static void getScreen(Activity activity) {
        if (StaticUtils.sysWidth <= 0 || StaticUtils.sysHeight <= 0) {
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            StaticUtils.sysWidth = dm.widthPixels;
            StaticUtils.sysHeight = dm.heightPixels;
        }
    }

    public static int getStatusBarHeight(Context c) {
        int result = 0;
        int resourceId = c.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = c.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 判斷手機號碼 大陆
     */
    public static boolean checkPhoneChina(String phone) {// ^(13[4-9]|15[7-9]|15[0-2]|18[6-8])[0-9]{8}$==^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$
        Pattern pattern = Pattern
                .compile("(^((13[0-9])|(14[5,7])|(15[^4,\\D])|(18[^4,\\D]))\\d{8}$)|(^[0-9]{8}$)");// ^0{0,1}(13[4-9]|15[7-9]|15[0-2]|18[7-8])[0-9]{8}$==^13\\d{9}||15[8,9]\\d{8}$
        Matcher matcher = pattern.matcher(phone);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 判斷手機號碼 香港
     */
    public static boolean checkPhone(String phone) {
        Pattern pattern = Pattern.compile("(^([2,3,5,6,7,8,9])\\d{7}$)");
        Matcher matcher = pattern.matcher(phone);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 判斷郵箱格式
     */
    public static boolean checkEamil(String userEamil) {// ^([a-z0-9A-Z]+[-|//.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?//.)+[a-zA-Z]{2,}$
        Pattern pattern = Pattern
                .compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(userEamil);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    /**
     * 修改数字的格式
     */
    public static String getDoubleToFormatString(Double money) {
        int position = (money + "").indexOf(".");
        int strA = Integer.parseInt((money + "").substring(0, position));
        int strB = Integer.parseInt((money + "").substring(position + 1));
        DecimalFormat df = new DecimalFormat();
        if (strB > 0) {// 有小数
            if (1000000 < strA && strA < 9999999) {
                df.applyPattern("0,000,000.00");
            } else if (100000 < strA && strA < 999999) {
                df.applyPattern("000,000.00");
            } else if (10000 < strA && strA < 99999) {
                df.applyPattern("00,000.00");
            } else if (1000 < strA && strA < 9999) {
                df.applyPattern("0,000.00");
            } else if (100 < strA && strA < 999) {
                df.applyPattern("000.00");
            } else if (10 < strA && strA < 99) {
                df.applyPattern("00.00");
            } else if (1 < strA && strA < 9) {
                df.applyPattern("0.00");
            }
        } else {// 没有小数
            if (1000000 < strA && strA < 9999999) {
                df.applyPattern("0,000,000");
            } else if (100000 < strA && strA < 999999) {
                df.applyPattern("000,000");
            } else if (10000 < strA && strA < 99999) {
                df.applyPattern("00,000");
            } else if (1000 < strA && strA < 9999) {
                df.applyPattern("0,000");
            } else if (100 < strA && strA < 999) {
                df.applyPattern("000");
            } else if (10 < strA && strA < 99) {
                df.applyPattern("00");
            } else if (1 < strA && strA < 9) {
                df.applyPattern("0");
            }
        }
        return df.format(money);
    }

    /**
     * 設置日期格式化
     */
    public static String format(int x) {
        String s = "" + x;
        if (s.length() == 1)
            s = "0" + s;
        return s;
    }

    /**
     * 震动一下
     */
    public static void virbate(Context context) {
        Vibrator vibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    /**
     * 根据URL地址得到一张图片
     */
    public static Bitmap returnBitMap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 根據本地地址獲取bitmap
     */
    public static Bitmap getLoacalBitmap(String path) {
        try {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPreferredConfig = Bitmap.Config.RGB_565;
            opt.inPurgeable = true;
            opt.inInputShareable = true;
            FileInputStream fis = new FileInputStream(path);
            return BitmapFactory.decodeStream(fis, null, opt); // /把流转化为Bitmap图片
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String saveMyBitmap(Bitmap mBitmap, String photo_path) {
        try {
            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();
            if (width > 512) {
                float scale = (float) 512 / width;
                Matrix matrix = new Matrix();
                matrix.postScale(scale, scale);
                mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height,
                        matrix, true);
                width = mBitmap.getWidth();
                height = mBitmap.getHeight();
            }
            if (height > 128) {
                float scale = (float) 512 / height;
                Matrix matrix = new Matrix();
                matrix.postScale(scale, scale);
                mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height,
                        matrix, true);
            }
            // File f = new File(Environment.getExternalStorageDirectory(),
            // "bigline.jpg");
            File f = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + "bigline.jpg");
            f.createNewFile();
            FileOutputStream fOut = null;
            fOut = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            return f.getPath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap rotateBitmap(Bitmap b, int degrees) {
        int d = degrees % 360;
        if (d != 0) {
            Matrix m = new Matrix();
            m.setRotate(d, (float) b.getWidth() / 2, (float) b.getHeight() / 2);
            try {
                Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
                        b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle();
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {
            }
        }
        return b;
    }

    /**
     * 对图片处理 ，保证不变形
     */
    public static Bitmap getBitmap(Bitmap bitmap, int width) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scale = (float) width / w;
        // 保证图片不变形.
        matrix.postScale(scale, scale);
        // w,h是原图的属性.
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }

    /**
     * 读取raw文件中的内容
     */
    public static String getContent(int paramInt, Activity context) {
        // 根据本地资源id，获取对应资源文件的输入流
        InputStream localInputStream = context.getResources().openRawResource(
                paramInt);
        StringBuilder localStringBuilder = new StringBuilder();
        byte[] arrayOfByte = new byte[4096];
        try {
            while (true) {
                int i = localInputStream.read(arrayOfByte);
                if (i == -1)
                    return localStringBuilder.toString();
                localStringBuilder.append(new String(arrayOfByte, 0, i));
            }
        } catch (IOException localIOException) {
            while (true)
                localIOException.printStackTrace();
        }
    }

    public static class Config {
        public static final boolean DEVELOPER_MODE = false;
    }

    // 定位service服務的全名
    public final static String locationServiceName = "com.bigline.android.service.local.LocationService";

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }
}
