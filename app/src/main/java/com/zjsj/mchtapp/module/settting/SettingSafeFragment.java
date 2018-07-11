package com.zjsj.mchtapp.module.settting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruomm.base.http.config.impl.TextHttpCallBack;
import com.ruomm.base.http.okhttp.TextOKHttp;
import com.ruomm.base.ioc.activity.BaseFragment;
import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.tools.StringUtils;
import com.ruomm.base.tools.TelePhoneUtil;
import com.ruomm.base.tools.ToastUtil;
import com.ruomm.base.view.dialog.BaseDialogClickListener;
import com.ruomm.resource.dialog.MessageDialog;
import com.ruomm.resource.dialog.dal.DialogValue;
import com.ruomm.resource.ui.AppFragment;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.IntentFactory;
import com.zjsj.mchtapp.config.LoginUserFactory;
import com.zjsj.mchtapp.config.http.ApiConfig;
import com.zjsj.mchtapp.dal.response.PayInfoDto;
import com.zjsj.mchtapp.dal.response.base.ResultDto;
import com.zjsj.mchtapp.dal.response.base.ResultFactory;
import com.zjsj.mchtapp.dal.store.UserGesturesInfo;

import java.util.Map;

public class SettingSafeFragment extends AppFragment {
    @InjectAll
    Views views=new Views();
    class Views{
        @InjectView(id=R.id.setting_switch_nopaypwd)
        ImageView setting_switch_nopaypwd;
        @InjectView(id=R.id.setting_switch_fingerprint)
        ImageView setting_switch_fingerprint;
        @InjectView(id=R.id.setting_switch_gesture)
        ImageView setting_switch_gesture;
        @InjectView(id=R.id.setting_ly_paypwd)
        RelativeLayout setting_ly_paypwd;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       mView=LayoutInflater.from(mContext).inflate(R.layout.setting_safe_lay,null);
       BaseUtil.initInjectAll(this,mView);
       setDataForView();
       setOnClickListener();
       doHttpTaskGetPayInfo();
       return mView;
    }
    private void setDataForView(){
        UserGesturesInfo userGesturesInfo= LoginUserFactory.getUserGesturesInfo();
        if(null==userGesturesInfo)
        {
            views.setting_switch_gesture.setSelected(false);
        }
        else{
            views.setting_switch_gesture.setSelected(true);
        }
        views.setting_switch_fingerprint.setSelected(true);
    }
    private void setOnClickListener(){
        views.setting_switch_nopaypwd.setOnClickListener(myOnClickListener);
        views.setting_switch_fingerprint.setOnClickListener(myOnClickListener);
        views.setting_switch_gesture.setOnClickListener(myOnClickListener);
        views.setting_ly_paypwd.setOnClickListener(myOnClickListener);
    }
    private View.OnClickListener myOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vID=v.getId();
            if(vID==R.id.setting_switch_nopaypwd)
            {

            }
            else if(vID==R.id.setting_switch_fingerprint)
            {

            }
            else if(vID==R.id.setting_switch_gesture)
            {
                if(v.isSelected()){
                    MessageDialog messageDialog=new MessageDialog(mContext);
                    messageDialog.setDialogValue(new DialogValue("确认删除手势密码吗？"));
                    messageDialog.setBaseDialogClick(new BaseDialogClickListener() {
                        @Override
                        public void onDialogItemClick(View v, Object tag) {
                            if(v.getId()==R.id.dialog_confirm)
                            {
                                LoginUserFactory.delUserGesturesInfo();
                                views.setting_switch_gesture.setSelected(false);
                            }
                        }
                    });
                    messageDialog.show();

                }
                else {
                    startActivityForResult(IntentFactory.getGestureLockActivity(), IntentFactory.Request_GestureLockActivity);
                }
            }
            else if(vID==R.id.setting_ly_paypwd)
            {
               callBackActivity("3",null);
            }
        }
    };
    private void doHttpTaskGetPayInfo(){
        if(null!= LoginUserFactory.getPayInfo()){
            return;
        }
        showLoading();
        Map<String,String> map= ApiConfig.createRequestMap(true);
        ApiConfig.signRequestMap(map);
        new TextOKHttp().setUrl(ApiConfig.BASE_URL+"/app/repaymentPayInfo/doGetPayInfo").setRequestBodyText(map).doHttp(PayInfoDto.class, new TextHttpCallBack() {
            @Override
            public void httpCallBack(Object resultObject, String resultString, int status) {

                String errTip= ResultFactory.getErrorTip(resultObject,status);
                ResultDto resultDto=(ResultDto)resultObject;
                if(!StringUtils.isEmpty(errTip))
                {
                    ToastUtil.makeFailToastThr(mContext,errTip);
                }
                else {
                    PayInfoDto payInfoDto=ResultFactory.getResult(resultObject,status);
                    if(null!=payInfoDto){
                        ToastUtil.makeOkToastThr(mContext, "获取支付信息成功");
                        LoginUserFactory.setPayInfoDto(payInfoDto);
                    }
                    else{
                        callBackActivity("1");
                    }

                }
                dismissLoading();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IntentFactory.Request_GestureLockActivity&&resultCode== Activity.RESULT_OK)
        {
            ToastUtil.makeOkToastThr(mContext,"设置手势密码成功");
            views.setting_switch_gesture.setSelected(true);
        }
    }
}
