package com.able.share.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.able.share.R;
import com.able.share.util.ShareStaticUtils;

/**
 * ======================================================
 * Created by Administrator able_fingerth on 2016/12/6.
 * <p/>
 * 版权所有，违者必究！
 * <详情描述/>
 */
public class ShareAdapter extends BaseAdapter {
    private Context context;
    private int[] iconArr ;
    private String[] shareArr ;


    public ShareAdapter( Context context,int[] iconArr, String[] shareArr) {
        this.iconArr = iconArr;
        this.shareArr = shareArr;
        this.context = context;
    }

    @Override
    public int getCount() {
        return iconArr.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_share_activity_grid_view, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        //屏幕適配
        //一行四個，間隔20dp   （屏幕寬-30*5）/4
        vh.icon.setLayoutParams(getIconLayoutParams(context));


        vh.icon.setImageResource(iconArr[pos]);
        vh.title.setText(shareArr[pos]);
        return convertView;
    }

    private class ViewHolder {
//        private LinearLayout layout;
        private ImageView icon;
        private TextView title;

        public ViewHolder(View convertView) {
//            this.layout = (LinearLayout) convertView.findViewById(R.id.item_layout);
            this.icon = (ImageView) convertView.findViewById(R.id.item_icon);
            this.title = (TextView) convertView.findViewById(R.id.item_title);
        }
    }

    private LinearLayout.LayoutParams getIconLayoutParams(Context context) {
        int with = (ShareStaticUtils.sysWidth - ShareStaticUtils.dp2px(context, 150)) / 4;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(with, with);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        return params;
    }
}

