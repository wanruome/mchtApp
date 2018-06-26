package com.zjsj.mchtapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.tools.ToastUtil;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.ui.BaseAppActivity;

public class WelcomeActivity extends BaseAppActivity
{
    @InjectView(id=R.id.view_img)
    private ImageView view_img;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setInitContentView(R.layout.welcome_act);
        hideMenuTopView();
        view_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.makeFailToastThr(mContext,"测试一下");
                startActivity(new Intent(mContext,MainActivity.class));
            }
        });
    }
    public void myOnClick(View view)
    {
        ToastUtil.makeFailToastThr(mContext,"测试一下");
        startActivity(new Intent("mchtapp.mainActivity"));
    }
}
