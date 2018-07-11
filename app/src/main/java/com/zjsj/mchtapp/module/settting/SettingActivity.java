package com.zjsj.mchtapp.module.settting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.intf.BaseEntryCallBackListener;
import com.ruomm.base.tools.TelePhoneUtil;
import com.ruomm.base.view.menutopview.MenuTopListener;
import com.ruomm.resource.ui.AppMultiActivity;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.IntentFactory;

import org.w3c.dom.Text;

public class SettingActivity extends AppMultiActivity implements BaseEntryCallBackListener{
    private String fragmentTag=null;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mymenutop.setMenuClickListener(myMenuTopListener);
        doFragmentTransactionByTag("1");
    }
    private MenuTopListener myMenuTopListener=new MenuTopListener() {
        @Override
        public void onMenuTopClick(View v, int vID) {
            if(vID==R.id.menutop_left)
            {
                if("3".equals(fragmentTag))
                {
                    doFragmentTransactionByTag("2");
                }
                else if("2".equals(fragmentTag))
                {
                    doFragmentTransactionByTag("1");
                }
                else {
                    finish();
                }
            }
        }
    };
    @Override
    public void onTagEntryCallBack(String tag, Object... entrys) {
        doFragmentTransactionByTag(tag);
    }
    public void doFragmentTransactionByTag(String tag)
    {
        fragmentTag=tag;
        if("1".equals(tag))
        {
            mymenutop.setCenterText("设置");
            mFManager.beginTransaction().replace(R.id.container_baseact,new SettingCommonFragment()).commit();
            fragmentTag=tag;
        }
        else if("2".equals(tag))
        {
            mymenutop.setCenterText("设置-安全设置");
            mFManager.beginTransaction().replace(R.id.container_baseact,new SettingSafeFragment()).commit();
            fragmentTag=tag;
        }
        else if("3".equals(tag))
        {
            mymenutop.setCenterText("设置-支付密码");
            mFManager.beginTransaction().replace(R.id.container_baseact,new SettingPayPwdFragment()).commit();
            fragmentTag=tag;

        }
    }

}
