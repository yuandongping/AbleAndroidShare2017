package com.able.share;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.able.share.adapter.ShareAdapter;
import com.able.share.constants.Constants;
import com.able.share.constants.Language;
import com.able.share.facebook.FaceBookShareUtils;
import com.able.share.sina.AccessTokenKeeper;
import com.able.share.util.FileSizeUtil;
import com.able.share.util.ShareContext;
import com.able.share.util.ShareStaticUtils;
import com.able.share.wx.Util;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONArray;
import org.json.JSONObject;

public class ShareActivity extends Activity implements IWeiboHandler.Response, AdapterView.OnItemClickListener, View.OnClickListener {
    @SuppressWarnings("unused")
    private static final String TAG = "ShareActivity";

    private int[] iconArr = {R.drawable.ic_con_sina_weibo_sdk_logo, R.drawable.ic_con_wx_person_sdk_logo, R.drawable.ic_con_wx_moments_sdk_logo, R.drawable.ic_con_facebook_sdk_logo, R.drawable.ic_con_ohter_logo};
    private String[] shareArr;
    private RelativeLayout prent_layout;
    private ShareContext shareContext;
    private Constants constants;
    private Language language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_lib);
        ShareStaticUtils.getScreen(this);
        Intent intent = getIntent();
        shareContext = new ShareContext(intent.getStringExtra("shareText"), intent.getStringExtra("imagePath"),
                intent.<Uri>getParcelableArrayListExtra("imageUris"), intent.getStringExtra("description"),
                intent.getStringExtra("shareUrl"));

        //appkey 和語言 賦值
        getAppKeyAndLanguage();


        shareArr = new String[]{language.sinna, language.wechat_person, language.wechat_moments, language.facebook, language.other};

        //TODO 1.***************************************************************************************
        // TODO 新浪分享==============================================================================
        // 创建微博分享接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, constants.APP_KEY);

        // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
        // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
        // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
        mWeiboShareAPI.registerApp();

        // 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
        // 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
        // 执行成功，返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
        // 失败返回 false，不调用上述回调
        if (savedInstanceState != null) {
            mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
        }
        //TODO 新浪分享==============================================================================
        //TODO***************************************************************************************

        //TODO 2.***************************************************************************************
        // TODO 微信==============================================================================
        api = WXAPIFactory.createWXAPI(this, constants.WX_APP_ID);

        // TODO 微信==============================================================================
        //TODO***************************************************************************************

        initView();
    }


    private void initView() {

        prent_layout = (RelativeLayout) findViewById(R.id.prent_layout);
        prent_layout.setOnClickListener(this);

        TextView cancelView = (TextView) findViewById(R.id.cancel_tv);
        cancelView.setText(language.cancel);
        cancelView.setOnClickListener(this);
        GridView gridView = (GridView) findViewById(R.id.grid_view);
        //gridView 的高度  一個item高度 上下padding為10dp ，文字20dp，圖片高度
        int iconWith = (ShareStaticUtils.sysWidth - ShareStaticUtils.dp2px(this, 150)) / 4;
        int gridHight = iconWith + ShareStaticUtils.dp2px(this, 40);
        int mCount = 0;
        if (iconArr.length < 2) {
            mCount = iconArr.length + 1;
        } else {
            mCount = iconArr.length - 1;
        }
        int h = gridHight * (mCount / 4 + 1) + 30;
        Log.v("gridHight", "iconWith=" + iconWith + "; gridHight=" + gridHight + "; h = " + h);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, gridHight * (mCount / 4 + 1) + 30);
        params.addRule(RelativeLayout.ABOVE, R.id.cancel_tv);
        gridView.setLayoutParams(params);

        gridView.setAdapter(new ShareAdapter(this, iconArr, shareArr));

        gridView.setOnItemClickListener(this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.prent_layout || view.getId() == R.id.cancel_tv) {
            closeThis();
        }
    }

    /**
     * gridViewd的點擊事件
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0://新浪
                //1.先下載圖片
                downImageBitmap(new XXX() {
                    @Override
                    public void xx(Bitmap bitmap) {
                        sendMultiMessage(bitmap);
                        closeThis();
                    }
                });

                break;
            case 1://微信朋友
                sendWeb();
                closeThis();
                break;
            case 2://微信朋友圈
                try {
                    Intent localIntent = new Intent();
                    localIntent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI"));
                    localIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    localIntent.setType("image/*");
                    localIntent.putExtra("Kdescription", "" + shareContext.getShareText());
                    localIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, shareContext.getImageUris());
                    startActivity(localIntent);
                    ((ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE)).setText("" + shareContext.getShareText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                closeThis();
                break;
            case 3://Facebook
                shareFaceBook();

                closeThis();

                break;
            case 4://其他
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, language.share);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareContext.getShareText() + "  " + shareContext.getDescription());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, language.share));
                closeThis();
                break;
            default:
                break;
        }
    }

    private void closeThis() {
        prent_layout.setBackgroundColor(Color.TRANSPARENT);
        this.finish();
        overridePendingTransition(0, R.anim.share_activity_close);
    }

    //============================================================================================
    //Facebook分享==================================================================================
    private CallbackManager callBackManager;

    /**
     * 分享到facebook
     */
    public void shareFaceBook() {

        callBackManager = CallbackManager.Factory.create();
        new FaceBookShareUtils(this, callBackManager, facebookCallback)
                .share(shareContext.getShareText(), shareContext.getImagePath(), shareContext.getDescription(), shareContext.getShareUrl());
    }

    /**
     * facebook分享状态回调
     */
    private FacebookCallback facebookCallback = new FacebookCallback() {

        @Override
        public void onSuccess(Object o) {
            Toast.makeText(ShareActivity.this, "onSuccess", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(ShareActivity.this, "onCancel", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(FacebookException error) {
            Toast.makeText(ShareActivity.this, "onError", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (FaceBookShareUtils.SHARE_REQUEST_CODE == requestCode) {
            callBackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    //Facebook分享==================================================================================
    // ============================================================================================


    // ============================================================================================
    //新浪分享==================================================================================
    private IWeiboShareAPI mWeiboShareAPI = null;

    /**
     * 接收微客户端博请求的数据。
     * 当微博客户端唤起当前应用并进行分享时，该方法被调用。
     *
     * @BaseResponse BaseResponse 微博请求数据对象
     * @see {@link IWeiboShareAPI#handleWeiboRequest}
     */
    @Override
    public void onResponse(BaseResponse baseResp) {
        if (baseResp != null) {
            switch (baseResp.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    Toast.makeText(this, language.weibosdk_demo_toast_share_success, Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    Toast.makeText(this, language.weibosdk_demo_toast_share_canceled, Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    Toast.makeText(this, language.weibosdk_demo_toast_share_failed + "Error Message: " + baseResp.errMsg, Toast.LENGTH_LONG).show();
                    closeThis();
                    break;
                default:
                    break;
            }


        }

    }


    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 注意：当 {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 时，支持同时分享多条消息，
     * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
     */
    private void sendMultiMessage(Bitmap bitmap) {
        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.textObject = getTextObj();

        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(bitmap);
        weiboMessage.imageObject = imageObject;


        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        AuthInfo authInfo = new AuthInfo(this, constants.APP_KEY, constants.REDIRECT_URL, constants.SCOPE);
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(getApplicationContext());
        String token = "";
        if (accessToken != null) {
            token = accessToken.getToken();
        }
        mWeiboShareAPI.sendRequest(this, request, authInfo, token, new WeiboAuthListener() {

            @Override
            public void onWeiboException(WeiboException arg0) {
                arg0.printStackTrace();
                Log.v(TAG, "onWeiboException");
            }

            @Override
            public void onComplete(Bundle bundle) {
                // TODO Auto-generated method stub
                Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                AccessTokenKeeper.writeAccessToken(getApplicationContext(), newToken);
//                Toast.makeText(getApplicationContext(), "onAuthorizeComplete token = " + newToken.getToken(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Log.v(TAG, "onCancel");
            }
        });
    }


    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = shareContext.getShareText();
        return textObject;
    }


    //新浪分享==================================================================================
    //============================================================================================


    //============================================================================================
    // 微信==================================================================================

    private IWXAPI api;

    public void sendWeb() {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = shareContext.getShareUrl();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = shareContext.getShareText();
        msg.description = shareContext.getDescription();
//        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_con_wx_moments_sdk_logo);
//        Bitmap thumb = BitmapFactory.decodeFile(shareContext.getImageUris().get(0).getPath());
        if (shareContext.getImageUris().size() > 0) {
            Bitmap thumb = FileSizeUtil.compressBitmap(shareContext.getImageUris().get(0).getPath(), 75, 75);
            msg.thumbData = Util.bmpToByteArray(thumb, true);
        }


        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    // 微信==================================================================================
    //============================================================================================


    private void downImageBitmap(final XXX x) {

        final ProgressDialog mProgressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        mProgressDialog.setMessage(language.loading);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
        Point size = new Point();
        mProgressDialog.getWindow().getWindowManager().getDefaultDisplay().getSize(size);
        //记得用mProgressDialog来得到这个界面的大小，实际上不加就是得到当前监听器匿名类对象的界面宽度</span>

        int width = size.x;//获取界面的宽度像素
        int height = size.y;
        WindowManager.LayoutParams params = mProgressDialog.getWindow().getAttributes(); //一定要用mProgressDialog得到当前界面的参数对象，否则就不是设置ProgressDialog的界面了
        params.alpha = 1.0f;//设置进度条背景透明度
        params.height = height / 9;//设置进度条的高度
        params.gravity = Gravity.BOTTOM;//设置ProgressDialog的重心
        params.width = 2 * width / 3;//设置进度条的宽度
        params.dimAmount = 0f;//设置半透明背景的灰度，范围0~1，系统默认值是0.5，1表示背景完全是黑色的,0表示背景不变暗，和原来的灰度一样
        mProgressDialog.getWindow().setAttributes(params);//把参数设置给进度条，注意，一定要先show出来才可以再设置，不然就没效果了，因为只有当界面显示出来后才可以获得它的屏幕尺寸及参数等一些信息


        String imagePath = shareContext.getImagePath();

        Glide.with(this).load(imagePath).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                mProgressDialog.dismiss();
//                shareIv.setImageBitmap(resource);
                x.xx(resource);
            }
        });
    }


    public interface XXX {
        void xx(Bitmap bitmap);
    }


    /**
     * 獲取appkey和語言
     */
    private void getAppKeyAndLanguage() {
        constants = new Constants();
        String appKeyStr = ShareStaticUtils.getStringAssetsData(this, "able_share_sdk.json");
        try {
//            "Id":"1",//新浪微博
//                    "SortId":"1",
//                    "AppKey":"2874733904",//* 必填
//                    "AppSecret":"",
//                    "RedirectUrl":"http://bigline.com.hk",
//                    "ShareByAppClient":"true",
//                    "Enable":"true"
            JSONArray jsonArray = new JSONArray(appKeyStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String id = object.getString("Id");
                if (TextUtils.equals("1", id)) {
                    constants.APP_KEY = object.getString("AppKey");
                    constants.REDIRECT_URL = object.getString("RedirectUrl");
                    constants.SCOPE = "";
                }
                if (TextUtils.equals("2", id)) {
                    constants.WX_APP_ID = object.getString("AppKey");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        language = new Language();
        String languageStr = ShareStaticUtils.getStringAssetsData(this, "able_share_language.json");
        try {
//            "sinna": "新浪微博",
//                    "wechat_person": "微信朋友",
//                    "wechat_moments": "微信朋友圈",
//                    "facebook": "Facebook",
//                    "other": "其他",
//                    "cancel": "取消",
//                    "loading": "正在啟動...",
//                    "share": "分享",
//                    "weibosdk_demo_toast_share_success": "分享成功",
//                    "weibosdk_demo_toast_share_failed": "分享失败",
//                    "weibosdk_demo_toast_share_canceled": "取消分享"
            JSONObject jsonObject = new JSONObject(languageStr);
            language.sinna = jsonObject.getString("sinna");
            language.wechat_person = jsonObject.getString("wechat_person");
            language.wechat_moments = jsonObject.getString("wechat_moments");
            language.facebook = jsonObject.getString("facebook");
            language.other = jsonObject.getString("other");
            language.cancel = jsonObject.getString("cancel");
            language.loading = jsonObject.getString("loading");
            language.share = jsonObject.getString("share");
            language.weibosdk_demo_toast_share_success = jsonObject.getString("weibosdk_demo_toast_share_success");
            language.weibosdk_demo_toast_share_failed = jsonObject.getString("weibosdk_demo_toast_share_failed");
            language.weibosdk_demo_toast_share_canceled = jsonObject.getString("weibosdk_demo_toast_share_canceled");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
