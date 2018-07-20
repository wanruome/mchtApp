package com.zjsj.mchtapp.module.settting;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruomm.base.http.okhttp.TextOKHttp;
import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.tools.StringUtils;
import com.ruomm.base.tools.ToastUtil;
import com.ruomm.base.tools.regextool.RegexCallBack;
import com.ruomm.base.tools.regextool.RegexText;
import com.ruomm.base.view.dialog.BaseDialogClickListener;
import com.ruomm.resource.dialog.MessageDialog;
import com.ruomm.resource.dialog.dal.DialogValue;
import com.ruomm.resource.ui.AppFragment;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.IntentFactory;
import com.zjsj.mchtapp.config.LoginUserFactory;
import com.zjsj.mchtapp.config.http.ApiConfig;
import com.zjsj.mchtapp.config.impl.TextHttpCallBack;
import com.zjsj.mchtapp.dal.response.PayInfoDto;
import com.zjsj.mchtapp.dal.response.base.ResultDto;
import com.zjsj.mchtapp.dal.response.base.ResultFactory;
import com.zjsj.mchtapp.dal.store.UserFingerPrint;
import com.zjsj.mchtapp.dal.store.UserGesturesInfo;
import com.zjsj.mchtapp.dal.ui.PayInfoModifyNoPwdFlagModel;
import com.zjsj.mchtapp.view.PasswordDialog;

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
    PasswordDialog passwordDialog;
    private ImageView currentSetImageView=null;
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
        if(null!=userGesturesInfo&&userGesturesInfo.isEnable)
        {
            views.setting_switch_gesture.setSelected(true);
        }
        else{
            views.setting_switch_gesture.setSelected(false);
        }
        UserFingerPrint userFingerPrint=LoginUserFactory.getUserFingerPrint();
        if(null!=userFingerPrint&&userFingerPrint.isEnable){
            views.setting_switch_fingerprint.setSelected(true);
        }
        else {
            views.setting_switch_fingerprint.setSelected(false);
        }
        updateUiNoPwdFlag();

    }

    private void updateUiNoPwdFlag()
    {
        PayInfoDto payInfoDto=LoginUserFactory.getPayInfo();
        if(null==payInfoDto)
        {
            views.setting_switch_nopaypwd.setSelected(false);
        }
        else if("1".equals(payInfoDto.noPwdFlag))
        {
            views.setting_switch_nopaypwd.setSelected(true);
        }
        else {
            views.setting_switch_nopaypwd.setSelected(false);
        }
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
//                passwordDialog=new PasswordDialog(getActivity());
//                passwordDialog.setBaseDialogClick(new BaseDialogClickListener() {
//                    @Override
//                    public void onDialogItemClick(View v, Object tag) {
//                        String data=(String)v.getTag();
//                        if(StringUtils.getLength(data)<=2)
//                        {
//                            ToastUtil.makeFailToastThr(mContext,"请输入支付密码");
//                        }
//                        else{
//                            doHttpTaskModifyNoPwdFlag(data);
//                            passwordDialog.dismiss();
//                        }
//                    }
//                });
//                passwordDialog.setAutoDisMisss(false);
//                passwordDialog.show();
                String contentMsg=views.setting_switch_nopaypwd.isSelected()?"是否关闭免密支付":"是否开启免密支付";
                MessageDialog messageDialog=new MessageDialog(mContext);
                messageDialog.setDialogValue(new DialogValue("修改免密状态",contentMsg));
                messageDialog.setBaseDialogClick(new BaseDialogClickListener() {
                    @Override
                    public void onDialogItemClick(View v, Object tag) {
                        int vID=v.getId();
                        if(vID==R.id.dialog_confirm)
                        {
                            PayInfoModifyNoPwdFlagModel payInfoModifyNoPwdFlagModel=new PayInfoModifyNoPwdFlagModel();
                            payInfoModifyNoPwdFlagModel.isUsePayPwd=false;
                            Intent intent=IntentFactory.getPayInfoModifyNoPwdFlagActivity();
                            if(views.setting_switch_nopaypwd.isSelected())
                            {
                                payInfoModifyNoPwdFlagModel.noPwdFlag="0";
                            }
                            else {
                                payInfoModifyNoPwdFlagModel.noPwdFlag="1";
                            }
                            BaseUtil.serializablePut(intent,payInfoModifyNoPwdFlagModel);
                            startActivityForResult(intent,IntentFactory.Request_PayInfoModifyNoPwdFlagActivity);
                        }
                    }
                });
                messageDialog.show();
            }
            else if(vID==R.id.setting_switch_fingerprint)
            {
                if(Build.VERSION.SDK_INT <Build.VERSION_CODES.M){
                    ToastUtil.makeShortToast(mContext,"Android 6.0以下不支持开启指纹");
                    return;
                }
//                if(v.isSelected()){
//                    MessageDialog messageDialog=new MessageDialog(mContext);
//                    messageDialog.setDialogValue(new DialogValue("确认删除指纹登录吗？"));
//                    messageDialog.setBaseDialogClick(new BaseDialogClickListener() {
//                        @Override
//                        public void onDialogItemClick(View v, Object tag) {
//                            if(v.getId()==R.id.dialog_confirm)
//                            {
//                                boolean tmp=LoginUserFactory.saveUserFingerPrint(false);
//                                if(tmp) {
//                                    views.setting_switch_fingerprint.setSelected(false);
//                                }
//                            }
//                        }
//                    });
//                    messageDialog.show();
//                }
//                else {
//                    startActivityForResult(IntentFactory.getFingerPrintActivity(), IntentFactory.Request_FingerPrintActivity);
//                }
                passwordDialog=new PasswordDialog(getActivity());
                passwordDialog.setBaseDialogClick(new BaseDialogClickListener() {
                    @Override
                    public void onDialogItemClick(View v, Object tag) {
                        String data=(String)v.getTag();
                        if(StringUtils.getLength(data)<=2)
                        {
                            if(views.setting_switch_fingerprint.isSelected()){
                                ToastUtil.makeFailToastThr(mContext,"请输入用户密码来删除验证指纹");
                            }
                            else {

                                ToastUtil.makeFailToastThr(mContext,"请输入用户密码来设置验证指纹");
                            }

                        }
                        else{
                            doHttpTaskVerifyPassWord(data);
                            currentSetImageView=views.setting_switch_fingerprint;
                            passwordDialog.dismiss();
                        }
                    }
                });
                passwordDialog.setAutoDisMisss(false);
                passwordDialog.show();

            }
            else if(vID==R.id.setting_switch_gesture)
            {
//                if(v.isSelected()){
//                    MessageDialog messageDialog=new MessageDialog(mContext);
//                    messageDialog.setDialogValue(new DialogValue("确认删除手势密码吗？"));
//                    messageDialog.setBaseDialogClick(new BaseDialogClickListener() {
//                        @Override
//                        public void onDialogItemClick(View v, Object tag) {
//                            if(v.getId()==R.id.dialog_confirm)
//                            {
//                                LoginUserFactory.saveUserGesturesInfo(null);
//                                views.setting_switch_gesture.setSelected(false);
//                            }
//                        }
//                    });
//                    messageDialog.show();
//                }
//                else {
//                    startActivityForResult(IntentFactory.getGestureLockActivity(), IntentFactory.Request_GestureLockActivity);
//                }
                passwordDialog=new PasswordDialog(getActivity());
                passwordDialog.setBaseDialogClick(new BaseDialogClickListener() {
                    @Override
                    public void onDialogItemClick(View v, Object tag) {
                        String data=(String)v.getTag();
                        if(StringUtils.getLength(data)<=2)
                        {
                            if(views.setting_switch_gesture.isSelected()){

                                ToastUtil.makeFailToastThr(mContext,"请输入用户密码来删除手势密码");
                            }
                            else {
                                ToastUtil.makeFailToastThr(mContext,"请输入用户密码来设置手势密码");
                            }

                        }
                        else{
                            currentSetImageView=views.setting_switch_gesture;
                            doHttpTaskVerifyPassWord(data);
                            passwordDialog.dismiss();
                        }
                    }
                });
                passwordDialog.setAutoDisMisss(false);
                passwordDialog.show();
            }
            else if(vID==R.id.setting_ly_paypwd)
            {
               callBackActivity("3");
            }
        }
    };
    private void doHttpTaskVerifyPassWord(String pwd){
        showLoading();
        if(StringUtils.isEmpty(pwd))
        {
            ToastUtil.makeFailToastThr(mContext,"请输入正确的密码");
            return;
        }


        showLoading();
        Map<String,String> map=ApiConfig.createRequestMap(true);
        map.put("pwdEncrypt", ApiConfig.getPassWordEncrypt(true));
        map.put("pwd",  ApiConfig.getPassWord(pwd, ApiConfig.getPassWordEncrypt(true)));
        ApiConfig.signRequestMap(map);
        new TextOKHttp().setUrl(ApiConfig.BASE_URL+"app/userAccount/doVerifyPassword").setRequestBodyText(map).doHttp(String.class, new TextHttpCallBack() {
            @Override
            public void httpCallBack(Object resultObject, String resultString, int status) {
                String errMsg=ResultFactory.getErrorTip(resultObject,status);
                if(StringUtils.isEmpty(errMsg))
                {
                    updateUiByCurrentSetView();
                }
                else {
                    ToastUtil.makeFailToastThr(mContext,"请输入正确的密码");
                }
                dismissLoading();

            }
        });
    }
    private void updateUiByCurrentSetView()
    {
        if(null==currentSetImageView)
        {
            return ;
        }
        if(currentSetImageView==views.setting_switch_fingerprint)
        {
            if(currentSetImageView.isSelected())
            {
                boolean tmp=LoginUserFactory.saveUserFingerPrint(false);
                if(tmp) {
                    views.setting_switch_fingerprint.setSelected(false);
                }
            }
            else {
                startActivityForResult(IntentFactory.getFingerPrintActivity(), IntentFactory.Request_FingerPrintActivity);
            }
        }
        else if(currentSetImageView==views.setting_switch_gesture)
        {
            if(currentSetImageView.isSelected())
            {
                LoginUserFactory.saveUserGesturesInfo(null);
                views.setting_switch_gesture.setSelected(false);
            }
            else {
                startActivityForResult(IntentFactory.getGestureLockActivity(), IntentFactory.Request_GestureLockActivity);
            }
        }
    }
    private void doHttpTaskModifyNoPwdFlag(String pwd)
    {
        showLoading();
        Map<String,String> map= ApiConfig.createRequestMap(true);
        map.put("payPwd",ApiConfig.getPassWord(pwd,ApiConfig.getPassWordEncrypt(true)));
        map.put("payPwdEncrypt",ApiConfig.getPassWordEncrypt(true));
        if(views.setting_switch_nopaypwd.isSelected()){
            map.put("noPwdFlag","0");
        }
        else {
            map.put("noPwdFlag","1");
        }
        ApiConfig.signRequestMap(map);
        new TextOKHttp().setUrl(ApiConfig.BASE_URL+"app/repaymentPayInfo/doModifyNoPwdFlag").setRequestBodyText(map).doHttp(PayInfoDto.class, new TextHttpCallBack() {
            @Override
            public void httpCallBack(Object resultObject, String resultString, int status) {
                String errTip= ResultFactory.getErrorTip(resultObject,status);
                if(!StringUtils.isEmpty(errTip))
                {
                    ToastUtil.makeFailToastThr(mContext,errTip);

                }
                else {
                    PayInfoDto payInfoDtoResult=ResultFactory.getResult(resultObject,status);
                    if(null!=payInfoDtoResult){
                        ToastUtil.makeOkToastThr(mContext, "修改免密状态成功");
                        LoginUserFactory.getPayInfo().noPwdFlag=payInfoDtoResult.noPwdFlag;
                        LoginUserFactory.savePayInfoDto(LoginUserFactory.getPayInfo());
                    }
                    else{
                        ToastUtil.makeFailToastThr(mContext,"修改免密状态失败");
                    }
                    updateUiNoPwdFlag();
                }
                dismissLoading();
            }
        });

    }
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
                    callBackActivity("1");
                }
                else {
                    PayInfoDto payInfoDto=ResultFactory.getResult(resultObject,status);
                    if(null!=payInfoDto){
                        ToastUtil.makeOkToastThr(mContext, "获取支付信息成功");
                        LoginUserFactory.savePayInfoDto(payInfoDto);
                        updateUiNoPwdFlag();
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
        else if(requestCode==IntentFactory.Request_FingerPrintActivity&&resultCode== Activity.RESULT_OK)
        {
            ToastUtil.makeOkToastThr(mContext,"指纹设置成功");
            views.setting_switch_fingerprint.setSelected(true);
        }
        else if(requestCode==IntentFactory.Request_PayInfoModifyNoPwdFlagActivity&&resultCode== Activity.RESULT_OK){
            updateUiNoPwdFlag();
        }
    }
}
