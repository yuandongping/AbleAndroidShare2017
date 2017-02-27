package com.able.share2017.util.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * ======================================================
 * Created by able-android -周晓明 on 2016/7/28.
 * <p>
 * 版权所有，违者必究！
 * <详情描述/>
 */
public class Android6Permission {

    //    group:android.permission-group.STORAGE
    //文件读写
    public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";

    //摄像头权限
    public final static String CAMERA = "android.permission.CAMERA";
    //日历
    public static final String READ_CALENDAR = "android.permission.READ_CALENDAR";
    public static final String WRITE_CALENDAR = "android.permission.WRITE_CALENDAR";

    public static final String WRITE_CONTACTS = "android.permission.WRITE_CONTACTS";
    public static final String READ_CONTACTS = "android.permission.READ_CONTACTS";

    public static final String ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";
    public static final String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";

    public static final String RECORD_AUDIO = "android.permission.RECORD_AUDIO";

    //    group:android.permission-group.PHONE
    // permission:android.permission.READ_CALL_LOG
    public static final String READ_PHONE_STATE = "permission:android.permission.READ_PHONE_STATE";
//    permission:android.permission.CALL_PHONE
//    permission:android.permission.WRITE_CALL_LOG
//    permission:android.permission.USE_SIP
//    permission:android.permission.PROCESS_OUTGOING_CALLS
//    permission:com.android.voicemail.permission.ADD_VOICEMAIL

//    group:android.permission-group.SENSORS
//    permission:android.permission.BODY_SENSORS


//    group:android.permission-group.SMS
//    permission:android.permission.READ_SMS
//    permission:android.permission.RECEIVE_WAP_PUSH
//    permission:android.permission.RECEIVE_MMS
//    permission:android.permission.RECEIVE_SMS
//    permission:android.permission.SEND_SMS
//    permission:android.permission.READ_CELL_BROADCASTS

    /**
     * 申请权限
     *
     * @param activity       activity
     * @param primissionsStr 权限的名称
     */
    public static void requestPermissions(Activity activity, String primissionsStr) {
        if (ContextCompat.checkSelfPermission(activity, primissionsStr) != PackageManager.PERMISSION_GRANTED) {
            int permsRequestCode = 200;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String[] perms = {primissionsStr};
                activity.requestPermissions(perms, permsRequestCode);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{primissionsStr}, permsRequestCode);
            }
        }
    }

    public static void requestPermissions(Activity activity, String primissionsStr, int code) {
        if (ContextCompat.checkSelfPermission(activity, primissionsStr) != PackageManager.PERMISSION_GRANTED) {
            int permsRequestCode = code;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String[] perms = {primissionsStr};
                activity.requestPermissions(perms, permsRequestCode);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{primissionsStr}, permsRequestCode);
            }
        }
    }


}
