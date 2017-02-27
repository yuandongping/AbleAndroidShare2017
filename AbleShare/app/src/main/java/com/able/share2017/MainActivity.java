package com.able.share2017;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.able.share.util.ShareStaticUtils;
import com.able.share2017.ui.ShareActivity;
import com.able.share2017.util.permission.PermissionUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ShareStaticUtils.getScreen(this);
        PermissionUtils.getPersimmions(this);
    }

    public void clickToShare(View v) {
        startActivity(new Intent(this, ShareActivity.class));
    }
}
