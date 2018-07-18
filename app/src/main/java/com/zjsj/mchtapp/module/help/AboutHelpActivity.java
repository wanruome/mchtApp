package com.zjsj.mchtapp.module.help;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.tools.AssetsUtil;
import com.ruomm.base.view.menutopview.MenuTopListener;
import com.ruomm.resource.ui.AppMultiActivity;
import com.zjsj.mchtapp.R;

public class AboutHelpActivity extends AppMultiActivity {
    @InjectAll
    Views views=new Views();
    class Views{
        @InjectView(id=R.id.text_tip)
        TextView text_tip;
    }
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setMenuTop();
        setInitContentView(R.layout.about_help_lay);
        String des= AssetsUtil.getString(mContext,"appdes/des_about_help.txt");
        views.text_tip.setText(des);
    }
    private void setMenuTop(){
        mymenutop.setCenterText("帮助");
        mymenutop.setMenuClickListener(new MenuTopListener() {
            @Override
            public void onMenuTopClick(View v, int vID) {
                if(vID== R.id.menutop_left)
                {
                    finish();
                }
            }
        });
    }
}
