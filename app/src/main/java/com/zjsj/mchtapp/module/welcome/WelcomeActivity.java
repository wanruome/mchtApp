package com.zjsj.mchtapp.module.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.resource.ui.BaseAppActivity;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.module.main.MainActivity;

public class WelcomeActivity extends BaseAppActivity {
//    @InjectView(id=R.id.view_img)
//    ImageView view_img;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setInitContentView(R.layout.welcome_act);
//        hideMenuTopView();
        mymenutop.setCenterText("你好啊");
//        view_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(mContext, MainActivity.class);
//                startActivity(intent);
//            }
//        });

    }
}
