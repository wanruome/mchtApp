package com.zjsj.mchtapp.module.settting;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruomm.base.ioc.activity.BaseFragment;
import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.iocutil.AppStoreUtil;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.tools.TelePhoneUtil;
import com.ruomm.base.tools.ToastUtil;
import com.ruomm.base.tools.ViewUtil;
import com.ruomm.base.view.dialog.BaseDialogClickListener;
import com.ruomm.resource.dialog.MessageDialog;
import com.ruomm.resource.dialog.dal.DialogValue;
import com.ruomm.resource.ui.dal.ScreenSercureConfig;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.IntentFactory;
import com.zjsj.mchtapp.config.LoginUserFactory;
import com.zjsj.mchtapp.dal.store.AppScreenSecure;

public class SettingCommonFragment extends BaseFragment {
    @InjectAll
    Views views=new Views();
    class Views{
        @InjectView(id=R.id.setting_ly_safe)
        RelativeLayout setting_ly_safe;
        @InjectView(id=R.id.setting_ly_shortcut)
        RelativeLayout setting_ly_shortcut;
        @InjectView(id=R.id.setting_ly_version)
        RelativeLayout setting_ly_version;
        @InjectView(id=R.id.setting_text_version)
        TextView setting_text_version;
        @InjectView(id=R.id.setting_ly_screensecure)
        RelativeLayout setting_ly_screensecure;
        @InjectView(id=R.id.setting_switch_screensecure)
        ImageView setting_switch_screensecure;
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
        if(ScreenSercureConfig.isAppScreenSecure)
        {
            views.setting_switch_screensecure.setSelected(true);
        }
        else {
            views.setting_switch_screensecure.setSelected(false);
        }
    }
    private void setOnClickListener(){
        views.setting_ly_safe.setOnClickListener(myOnClickListener);
        views.setting_ly_shortcut.setOnClickListener(myOnClickListener);
        views.setting_ly_screensecure.setOnClickListener(myOnClickListener);
    }
    private View.OnClickListener myOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vID=v.getId();

            if(vID==R.id.setting_ly_safe)
            {
                callBackActivity("2");
            }
            else if(vID==R.id.setting_ly_shortcut)
            {
                ViewUtil.addShortcut(getActivity(),"付款码", BitmapFactory.decodeResource(getResources(),R.mipmap.paymentcode),IntentFactory.getRepaymentQrCodeActivity(), Activity.RESULT_CANCELED);
            }
            else if(vID==R.id.setting_ly_screensecure)
            {
                if(!views.setting_switch_screensecure.isSelected()){
                    MessageDialog messageDialog=new MessageDialog(mContext);
                    String appName=getString(R.string.app_name);
                    DialogValue dialogValue=new DialogValue("安全提示","截屏防护功能杜绝了其他手机进程在"+appName+"使用过程中截屏的风险，进一步提升了"+appName+"的安全性。\r\n温馨提示：启用截屏防护后，如出现黑屏等不兼容问题，重新安装"+appName+"客户端即可","取消","启用截屏防护");

                    messageDialog.setDialogValue(dialogValue);
                    messageDialog.setBaseDialogClick(new BaseDialogClickListener() {
                        @Override
                        public void onDialogItemClick(View v, Object tag) {
                            if(v.getId()==R.id.dialog_confirm){
                                AppScreenSecure appScreenSecure=new AppScreenSecure();
                                appScreenSecure.isScreenSecrue=true;
                                LoginUserFactory.saveAppScreenSecure(appScreenSecure);
                                views.setting_switch_screensecure.setSelected(true);
                            }

                        }
                    });
                    messageDialog.show();
                }
                else {
                    String appName=getString(R.string.app_name);
                    ToastUtil.makeOkToastThr(mContext,"截屏防护功能已开启，不可以主动关闭，如出现黑屏等不兼容问题，重新安装"+appName+"客户端即可");
                }
            }
        }
    };
}
