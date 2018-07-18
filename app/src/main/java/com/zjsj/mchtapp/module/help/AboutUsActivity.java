package com.zjsj.mchtapp.module.help;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.tools.AssetsUtil;
import com.ruomm.base.tools.FileUtils;
import com.ruomm.base.tools.TelePhoneUtil;
import com.ruomm.base.view.menutopview.MenuTopListener;
import com.ruomm.base.view.menutopview.MenuTopView;
import com.ruomm.resource.ui.AppMultiActivity;
import com.ruomm.resource.ui.AppSimpleActivity;
import com.zjsj.mchtapp.R;

import org.w3c.dom.Text;

public class AboutUsActivity extends AppSimpleActivity {
    @InjectAll
    Views views=new Views();
    class Views{
        @InjectView(id=R.id.text_tip)
        TextView text_tip;
        @InjectView(id=R.id.text_appverison)
        TextView text_appverison;
        @InjectView(id=R.id.mymenutop)
        MenuTopView mymenutop;
    }
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setInitContentView(R.layout.about_us_new_lay);
        setMenuTop();
        views.text_appverison.setText(TelePhoneUtil.getPackageVersionName(mContext));
        String des= AssetsUtil.getString(mContext,"appdes/des_about_us.txt");
        views.text_tip.setText(des);
    }
    private void setMenuTop(){
        views.mymenutop.setCenterText("关于我们");
        views.mymenutop.setMenuClickListener(new MenuTopListener() {
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
