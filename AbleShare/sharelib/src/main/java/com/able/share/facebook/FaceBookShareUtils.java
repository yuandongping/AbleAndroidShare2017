package com.able.share.facebook;

import android.app.Activity;
import android.net.Uri;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

/**
 * ======================================================
 * Created by Administrator able_fingerth on 2017/2/23.
 * <p/>
 * 版权所有，违者必究！
 * <详情描述/>
 */
public class FaceBookShareUtils {

    private Activity mActivity;
    private ShareDialog shareDialog;
    private CallbackManager callBackManager;
    public static final int SHARE_REQUEST_CODE = 0x111;
    private ShareLinkContent.Builder shareLinkContentBuilder;

    public FaceBookShareUtils(Activity activity, CallbackManager callBackManager, FacebookCallback facebookCallback) {
        this.mActivity = activity;
        this.callBackManager = callBackManager;
        shareDialog = new ShareDialog(mActivity);
        //注册分享状态监听回调接口
        shareDialog.registerCallback(callBackManager, facebookCallback, FaceBookShareUtils.SHARE_REQUEST_CODE);
        shareLinkContentBuilder = new ShareLinkContent.Builder();
    }

    /**
     * 分享
     */
    public void share(String contentTitle, String imageUrl, String desc, String shareUrl) {

        shareLinkContentBuilder.setContentTitle(contentTitle)
                .setImageUrl(Uri.parse(imageUrl))
                .setContentDescription(desc)
                .setContentUrl(Uri.parse("https://developers.facebook.com"));
        ShareLinkContent shareLinkContent = shareLinkContentBuilder.build();
        if (shareDialog.canShow(ShareLinkContent.class)) {
            shareDialog.show(mActivity, shareLinkContent);
        }
    }

}
