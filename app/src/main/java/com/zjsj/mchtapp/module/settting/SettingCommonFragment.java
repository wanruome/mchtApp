package com.zjsj.mchtapp.module.settting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruomm.base.ioc.activity.BaseFragment;
import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.tools.TelePhoneUtil;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.IntentFactory;

public class SettingCommonFragment extends BaseFragment {
    @InjectAll
    Views views=new Views();
    class Views{
        @InjectView(id=R.id.setting_ly_safe)
        RelativeLayout setting_ly_safe;
        @InjectView(id=R.id.setting_ly_version)
        RelativeLayout setting_ly_version;
        @InjectView(id=R.id.setting_text_version)
        TextView setting_text_version;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       mView=LayoutInflater.from(mContext).inflate(R.layout.setting_common_lay,null);
       BaseUtil.initInjectAll(this,mView);
        setDataForView();
        setOnClickListener();
       return mView;
    }
    private void setDataForView(){
        views.setting_text_version.setText(TelePhoneUtil.getPackageVersionName(mContext));
    }
    private void setOnClickListener(){
        views.setting_ly_safe.setOnClickListener(myOnClickListener);
    }
    private View.OnClickListener myOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vID=v.getId();

            if(vID==R.id.setting_ly_safe)
            {
                callBackActivity("2");
            }
        }
    };
}
