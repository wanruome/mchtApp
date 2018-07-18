package com.zjsj.mchtapp.module.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruomm.base.http.okhttp.TextOKHttp;
import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.tools.DisplayUtil;
import com.ruomm.base.tools.StringUtils;
import com.ruomm.base.tools.ToastUtil;
import com.ruomm.base.view.dialog.BaseDialogClickListener;
import com.ruomm.base.view.percentview.RelativeLayout_PercentHeight;
import com.ruomm.baseconfig.debug.MLog;
import com.ruomm.resource.dialog.EditTextDialog;
import com.ruomm.resource.dialog.MessageDialog;
import com.ruomm.resource.dialog.dal.DialogValue;
import com.ruomm.resource.ui.AppFragment;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.IntentFactory;
import com.zjsj.mchtapp.config.LoginUserFactory;
import com.zjsj.mchtapp.config.http.ApiConfig;
import com.zjsj.mchtapp.config.impl.TextHttpCallBack;
import com.zjsj.mchtapp.dal.event.LoginEvent;
import com.zjsj.mchtapp.dal.event.TokenEvent;
import com.zjsj.mchtapp.dal.response.base.ResultFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Map;

public class MainMenuFragment extends AppFragment {
    @InjectAll
    Views views=new Views();
    class Views{
        @InjectView(id=R.id.activity_main_menu)
        LinearLayout activity_main_menu;
        @InjectView(id=R.id.ly_userinfo)
        RelativeLayout_PercentHeight ly_userinfo;
        @InjectView(id=R.id.img_header)
        ImageView img_header;
        @InjectView(id=R.id.text_name)
        TextView text_name;
        @InjectView(id=R.id.menu_exit_app)
        LinearLayout menu_exit_app;
        @InjectView(id=R.id.menu_setting)
        LinearLayout menu_setting;
        @InjectView(id=R.id.menu_help)
        LinearLayout menu_help;
        @InjectView(id=R.id.menu_feedback)
        LinearLayout menu_feedback;
        @InjectView(id=R.id.menu_about_us)
        LinearLayout menu_about_us;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.main_menu_lay, null);
        BaseUtil.initInjectAll(this,mView);
        views.ly_userinfo.setHeightPercent(0.6f);
        int height=DisplayUtil.getNavigationBarHeight(mContext);
        LinearLayout menu_exit_app=mView.findViewById(R.id.menu_exit_app);
        LinearLayout.LayoutParams layoutParams=(LinearLayout.LayoutParams)menu_exit_app.getLayoutParams();
        layoutParams.bottomMargin=height;
        updateUiByUserInfo();
//        menu_exit_app.requestLayout();
        EventBus.getDefault().register(this);
        //设置监听
        setViewClickListener();
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThrend(LoginEvent event){
        updateUiByUserInfo();
    }
    @Subscribe
    public void onEventMainThrend(TokenEvent event){
        if(event.isInValid)
        {
            updateUiByUserInfo();
        }
    }
    private void updateUiByUserInfo()
    {
        if(LoginUserFactory.isLogin())
        {
            String nickName=StringUtils.isEmpty(LoginUserFactory.getLoginUserInfo().nickName)?"你好，请设置昵称":"你好，"+LoginUserFactory.getLoginUserInfo().nickName;
            views.text_name.setText(nickName);
        }
        else{
            views.text_name.setText("你好，请登录");
        }
    }
    private void  setViewClickListener(){
        views.activity_main_menu.setOnClickListener(myOnClickListener);
        views.menu_exit_app.setOnClickListener(myOnClickListener);
        views.menu_setting.setOnClickListener(myOnClickListener);
        views.menu_help.setOnClickListener(myOnClickListener);
        views.menu_feedback.setOnClickListener(myOnClickListener);
        views.menu_about_us.setOnClickListener(myOnClickListener);
        views.text_name.setOnClickListener(myOnClickListener);

    }
    private View.OnClickListener myOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vID=v.getId();
            if(vID==R.id.menu_exit_app)
            {
                showLogoutDialog();
            }
            else if(vID==R.id.menu_setting)
            {
                if(isAppLogin(true)){
                    startActivity(IntentFactory.getSettingActivity());
                }

            }
            else if(vID==R.id.text_name)
            {
                if(isAppLogin(true)){
                    EditTextDialog editTextDialog=new EditTextDialog(mContext);
                    editTextDialog.setDialogTitle("输入用户昵称");
                    editTextDialog.setBaseDialogClick(new BaseDialogClickListener() {
                        @Override
                        public void onDialogItemClick(View v, Object tag) {
                            String data=(String)v.getTag();
                            MLog.i(data);
                            doHttpTaskModifyNickName(data);
                        }
                    });
                    editTextDialog.show();
                }
            }
            else if (vID==R.id.menu_feedback)
            {
                if(isAppLogin(true)){
                   startActivity(IntentFactory.getFeedBackActivity());
                }
            }
            else if (vID==R.id.menu_about_us)
            {
                startActivity(IntentFactory.getAboutUsActivity());
            }
            else if (vID==R.id.menu_help)
            {
                startActivity(IntentFactory.getAboutHelpActivity());
            }
        }
    };
    private void showLogoutDialog()
    {
        if(!LoginUserFactory.isLogin()){
            return;
        }
        else {
            MessageDialog messageDialog=new MessageDialog(mContext);
            messageDialog.setDialogValue(new DialogValue("是否退出登录?"));
            messageDialog.setBaseDialogClick(new BaseDialogClickListener() {
                @Override
                public void onDialogItemClick(View v, Object tag) {
                    if(v.getId()==R.id.dialog_confirm)
                    {
                        doHttpTaskLogout();
                    }
                }
            });
            messageDialog.show();
        }
    }
    private void doHttpTaskModifyNickName(final String nickName){
        int lengthNickName=StringUtils.getLength(nickName);
        if(lengthNickName<1)
        {
            ToastUtil.makeFailToastThr(mContext,"请填写昵称");
            return;
        }
        if(lengthNickName>30)
        {
            ToastUtil.makeFailToastThr(mContext,"昵称不能超过30位");
            return;
        }
        showLoading();
        Map<String, String> map = ApiConfig.createRequestMap(true);
        map.put("nickName",nickName);
        ApiConfig.signRequestMap(map);
        new TextOKHttp().setUrl(ApiConfig.BASE_URL + "app/userAccount/doModifyUserInfo")
                .setRequestBodyText(map).doHttp(String.class, new TextHttpCallBack() {
            @Override
            public void httpCallBack(Object resultObject, String resultString, int status) {
                String errMsg= ResultFactory.getErrorTip(resultObject,status);
                if(StringUtils.isEmpty(errMsg))
                {

                    ToastUtil.makeOkToastThr(mContext,"昵称修改成功");
                    LoginUserFactory.getLoginUserInfo().nickName=nickName;
                    LoginUserFactory.saveLoginForModify();
                    updateUiByUserInfo();
                }
                else{
                    ToastUtil.makeFailToastThr(mContext,errMsg);
                }
                dismissLoading();
            }
        });

    }
    private void doHttpTaskLogout(){
        showLoading();
        Map<String, String> map = ApiConfig.createRequestMap(true);
        ApiConfig.signRequestMap(map);
        new TextOKHttp().setUrl(ApiConfig.BASE_URL + "app/userAccount/doLogout")
                .setRequestBodyText(map).doHttp(String.class, new TextHttpCallBack() {
            @Override
            public void httpCallBack(Object resultObject, String resultString, int status) {
                    String errMsg= ResultFactory.getErrorTip(resultObject,status);
                    if(StringUtils.isEmpty(errMsg))
                    {
                        LoginUserFactory.doLogout();
                        ToastUtil.makeOkToastThr(mContext,"退出成功");
                    }
                    else{
                        ToastUtil.makeFailToastThr(mContext,errMsg);
                    }
                    dismissLoading();
            }
        });

    }
    private boolean isAppLogin(boolean isShowDialog){
        if(LoginUserFactory.isLogin())
        {
            return true;
        }
        else{
            if(!isShowDialog){
                startActivity(IntentFactory.getLoinActivity());
            }
            else{
                MessageDialog messageDialog=new MessageDialog(mContext);
                messageDialog.setDialogValue(new DialogValue("登录","是否登录应用"));
                messageDialog.setBaseDialogClick(new BaseDialogClickListener() {
                    @Override
                    public void onDialogItemClick(View v, Object tag) {
                        if(v.getId()==R.id.dialog_confirm)
                        {
                            startActivity(IntentFactory.getLoinActivity());
                        }
                    }
                });
                messageDialog.show();
            }
            return false;
        }
    }
}
