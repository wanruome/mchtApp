package com.zjsj.mchtapp.module.settting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.resource.ui.AppFragment;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.IntentFactory;

public class SettingPayPwdFragment extends AppFragment {
    @InjectAll
    Views views=new Views();
    class Views{
        @InjectView(id=R.id.setting_ly_paypwd_seting)
        RelativeLayout setting_ly_paypwd_seting;
        @InjectView(id=R.id.setting_ly_paypwd_modify)
        RelativeLayout setting_ly_paypwd_modify;
        @InjectView(id=R.id.setting_ly_paypwd_find)
        RelativeLayout setting_ly_paypwd_find;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       mView=LayoutInflater.from(mContext).inflate(R.layout.setting_paypwd_lay,null);
       BaseUtil.initInjectAll(this,mView);
        setOnClickListener();
       return mView;
    }

    private void setOnClickListener(){
        views.setting_ly_paypwd_seting.setOnClickListener(myOnClickListener);
        views.setting_ly_paypwd_modify.setOnClickListener(myOnClickListener);
        views.setting_ly_paypwd_find.setOnClickListener(myOnClickListener);
    }
    private View.OnClickListener myOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vID=v.getId();
            if(vID==R.id.setting_ly_paypwd_seting)
            {
                startActivity(IntentFactory.getPayInfoSetPwdActivity());
            }
            else  if(vID==R.id.setting_ly_paypwd_modify)
            {
                startActivity(IntentFactory.getPayInfoModifyPwdActivity());
            }
            else  if(vID==R.id.setting_ly_paypwd_find)
            {
                startActivity(IntentFactory.getPayInfoFindPwdActivity());
            }
        }
    };

}
