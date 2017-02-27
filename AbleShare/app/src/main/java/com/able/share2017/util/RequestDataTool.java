package com.able.share2017.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.able.share2017.net.MultipartRequest;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * ======================================================
 * Created by able-android -周晓明 on 2016/3/21.
 * <p/>
 * 版权所有，违者必究！
 * <详情描述/>
 */
public class RequestDataTool {

    private final static String TAG = "RequestDataTool";
    private Context context;
    private RequestQueue queue;

    private static RequestDataTool downLoadTool;

    public static RequestDataTool getInstance(Context context) {
        if (downLoadTool == null) {
            downLoadTool = new RequestDataTool(context);
        }
        return downLoadTool;
    }

    private RequestDataTool(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }

    public void requestDataUseGet(final String url, final SuccessfulRequestData successfulRequestData, final FailRequestData failRequestData) {
        queue.add(new MyStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LogUtils.setTag(TAG, "" + response);
                        if (successfulRequestData != null) {
                            successfulRequestData.xxJson(response);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LogUtils.setTag(TAG, "" + error.toString());
                        // ToastUtils.showToast((Activity) context, "網絡錯誤");
                        if (failRequestData != null) {
                            failRequestData.failUrl(url);
                        }

                    }
                }).setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f))//怕网络超时，//参数1；2重试的最大次数；3；
        );
    }

    public void requestDataUseGet(final String url, Map<String, String> map, final SuccessfulRequestData successfulRequestData, final FailRequestData failRequestData) {
        String requestUrl = url + "?";
        String xx = "";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            xx += "&" + entry.getKey() + "=" + entry.getValue();
        }
        if (xx.startsWith("&")) {
            String s = xx.replaceFirst("&", "");
            requestUrl = requestUrl + s;
        }
        LogUtils.setTag(TAG, requestUrl);
        queue.add(new MyStringRequest(Request.Method.GET, requestUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (successfulRequestData != null) {
                            successfulRequestData.xxJson(response);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LogUtils.setTag(TAG, "" + error.toString());
                        // ToastUtils.showToast((Activity) context, "網絡錯誤");
                        if (failRequestData != null) {
                            failRequestData.failUrl(url);
                        }

                    }
                }).setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f))
        );
    }

    /**
     * 帶tag的請求
     *
     * @param tag
     * @param url
     * @param map
     * @param successfulRequestData
     * @param failRequestData
     */
    public void requestDataUseGet(String tag, final String url, Map<String, String> map, final SuccessfulRequestData successfulRequestData, final FailRequestData failRequestData) {
        String requestUrl = url + "?";
        String xx = "";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            xx += "&" + entry.getKey() + "=" + entry.getValue();
        }
        if (xx.startsWith("&")) {
            String s = xx.replaceFirst("&", "");
            requestUrl = requestUrl + s;
        }
        LogUtils.setTag(TAG, "這個url為" + requestUrl);
        queue.add(new MyStringRequest(Request.Method.GET, requestUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (successfulRequestData != null) {
                            successfulRequestData.xxJson(response);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LogUtils.setTag(TAG, "網絡錯誤:" + "" + error.toString());
                        // ToastUtils.showToast((Activity) context, "網絡錯誤");
                        if (failRequestData != null) {
                            failRequestData.failUrl(url);
                        }

                    }
                }).setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f)).setTag(tag)
        );
    }

    public void requestImage(final String url, final SuccessfulRequestImage successfulRequestImage, final FailRequestData failRequestData) {
        queue.add(new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                if (successfulRequestImage != null) {
                    successfulRequestImage.xxBitmap(response);
                }
            }
        }, StaticUtils.sysWidth / 2, StaticUtils.sysHeight / 2, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.setTag(TAG, "網絡錯誤:" + "" + error.toString());
                // ToastUtils.showToast((Activity) context, "網絡錯誤");
                if (failRequestData != null) {
                    failRequestData.failUrl(url);
                }
            }
        }).setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f)));
    }


    /**
     * POST 请求数据
     *
     * @param url                   url
     * @param map                   map
     * @param successfulRequestData 成功调用的方法
     * @param failRequestData       失败的调用方法
     */
    public void requestDataUsePOST(final String url, final Map<String, String> map, final SuccessfulRequestData successfulRequestData, final FailRequestData failRequestData) {

        String requestUrl = url + "?";
        String xx = "";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            xx += "&" + entry.getKey() + "=" + entry.getValue();
        }
        if (xx.startsWith("&")) {
            String s = xx.replaceFirst("&", "");
            requestUrl = requestUrl + s;
        }
        LogUtils.setTag(TAG, "Post這個url為" + requestUrl);
        queue.add(new MyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (successfulRequestData != null) {
                    successfulRequestData.xxJson(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.setTag(TAG, "網絡錯誤:" + "" + error.toString());
                // ToastUtils.showToast((Activity) context, "網絡錯誤");
                if (failRequestData != null) {
                    failRequestData.failUrl(url);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        }.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f)));
    }

    /**
     * POST 请求数据,request带Tag，用于取消请求
     *
     * @param url                   url
     * @param map                   map
     * @param successfulRequestData 成功调用的方法
     * @param failRequestData       失败的调用方法
     */
    public void requestDataUsePOSTByTag(String tag, final String url, final Map<String, String> map, final SuccessfulRequestData successfulRequestData, final FailRequestData failRequestData) {

        String requestUrl = url + "?";
        String xx = "";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            xx += "&" + entry.getKey() + "=" + entry.getValue();
        }
        if (xx.startsWith("&")) {
            String s = xx.replaceFirst("&", "");
            requestUrl = requestUrl + s;
        }
        LogUtils.setTag(TAG, "Post這個url為:" + requestUrl);
        queue.add(new MyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (successfulRequestData != null) {
                    successfulRequestData.xxJson(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.setTag(TAG, "網絡錯誤:" + "" + error.toString());
                // ToastUtils.showToast((Activity) context, "網絡錯誤");
                if (failRequestData != null) {
                    failRequestData.failUrl(url);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        }.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f)).setTag(tag));
    }


    /**
     * 加载成功回调的方法
     */
    public interface SuccessfulRequestData {
        void xxJson(String response);
    }

    public interface SuccessfulRequestImage {
        void xxBitmap(Bitmap bitmap);
    }

    /**
     * 加载失败回调的方法
     */
    public interface FailRequestData {
        void failUrl(String url);
    }

    /**
     * 关闭volley的请求队列
     */
    public void exitVolley() {
        if (queue != null) {
            queue.cancelAll("log");
        }
    }

    /**
     * 关闭volley的请求队列，通过Tag
     */
    public void exitVolleyQueueBuyTag(String tag) {
        if (queue != null) {
            queue.cancelAll(tag);
            LogUtils.setTag(TAG, "取消了请求" + "tag=" + tag);
        }
    }


    class MyStringRequest extends StringRequest {

        public MyStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
        }

        public MyStringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(url, listener, errorListener);

        }

        /**
         * 重写以解决乱码问题
         * <p/>
         * 在服务器的返回的数据的header的中contentType加上charset=UTF-8的声明。
         * 如果在服务器的返回数据的header中没有指定字符集那么就会默认使用 ISO-8859-1 字符集。
         */
        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {
            String str = null;
            try {
                str = new String(response.data, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return Response.success(str, HttpHeaderParser.parseCacheHeaders(response));
        }
    }

    /**
     * 上傳文件（圖片）
     */
    public void upLoadFile(String url, Map<String, String> map, String filePartName,
                           File file, final SuccessfulRequestData successfulRequestData, final FailRequestData failRequestData) {

        MultipartRequest request = new MultipartRequest(url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.setTag(TAG, "網絡錯誤:" + "" + error.toString());
                // ToastUtils.showToast((Activity) context, "網絡錯誤");
                if (failRequestData != null) {
                    failRequestData.failUrl("");
                }
            }
        }, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (successfulRequestData != null) {
                    successfulRequestData.xxJson(response);
                }
            }
        }, filePartName, file, map);

        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        queue.add(request);
    }


}
