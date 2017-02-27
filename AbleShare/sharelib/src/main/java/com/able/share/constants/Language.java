package com.able.share.constants;

/**
 * ======================================================
 * Created by Administrator able_fingerth on 2016/12/5.
 * <p/>
 * 版权所有，违者必究！
 * <详情描述/>
 */
public class Language {
    public Language(){}
    public Language(String sinna, String wechat_person, String wechat_moments, String facebook, String other, String cancel, String share, String loading, String weibosdk_demo_toast_share_success, String weibosdk_demo_toast_share_failed, String weibosdk_demo_toast_share_canceled) {
        this.sinna = sinna;
        this.wechat_person = wechat_person;
        this.wechat_moments = wechat_moments;
        this.facebook = facebook;
        this.other = other;
        this.cancel = cancel;
        this.share = share;
        this.loading = loading;
        this.weibosdk_demo_toast_share_success = weibosdk_demo_toast_share_success;
        this.weibosdk_demo_toast_share_failed = weibosdk_demo_toast_share_failed;
        this.weibosdk_demo_toast_share_canceled = weibosdk_demo_toast_share_canceled;
    }

    //    private String[] shareArr = {"新浪微博", "微信朋友", "微信朋友圈", "Facebook", "其他"};
    public String sinna;//= "新浪微博";
    public String wechat_person;// = "微信朋友";
    public String wechat_moments;// = "微信朋友圈";
    public String facebook;// = "Facebook";
    public String other;//= "其他";

    public String cancel;//= "取消";
    public String share;// = "分享";
    public String loading;//= "正在啟動...";
    //TODO***************************************************************************************
    // TODO 新浪分享==============================================================================
    public String weibosdk_demo_toast_share_success;//= "分享成功";
    public String weibosdk_demo_toast_share_failed;//= "分享失败";
    public String weibosdk_demo_toast_share_canceled;//= "取消分享";
    //TODO 新浪分享==============================================================================
    //TODO***************************************************************************************


}
