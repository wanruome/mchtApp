package com.zjsj.mchtapp.module.help;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ruomm.base.http.okhttp.TextOKHttp;
import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.tools.StringUtils;
import com.ruomm.base.tools.ToastUtil;
import com.ruomm.base.tools.regextool.RegexCallBack;
import com.ruomm.base.tools.regextool.RegexText;
import com.ruomm.base.tools.regextool.RegexUtil;
import com.ruomm.base.view.menutopview.MenuTopListener;
import com.ruomm.resource.ui.AppMultiActivity;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.LoginUserFactory;
import com.zjsj.mchtapp.config.http.ApiConfig;
import com.zjsj.mchtapp.config.impl.TextHttpCallBack;
import com.zjsj.mchtapp.dal.response.base.ResultFactory;

import java.util.Map;

public class FeedBackActivity extends AppMultiActivity {
    @InjectAll
    Views views=new Views();
    class Views{
        @InjectView(id=R.id.edt_content)
        EditText edt_content;
        @InjectView(id=R.id.btn_submit)
        Button btn_submit;
    }
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setInitContentView(R.layout.feedback_lay);
        setMenuTop();
        setOnClickListener();
    }
    private void setMenuTop(){
        mymenutop.setCenterText("意见反馈");
        mymenutop.setMenuClickListener(new MenuTopListener() {
            @Override
            public void onMenuTopClick(View v, int vID) {
                if(vID==R.id.menutop_left)
                {
                    finish();
                }
            }
        });
    }
    private void setOnClickListener()
    {
        views.btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doHttpTaskFeedBack();
            }
        });
    }
    private void doHttpTaskFeedBack(){
        boolean flag=RegexText.with(new RegexCallBack() {
            @Override
            public void errorRegex(TextView v, String value, String errorInfo) {
                ToastUtil.makeFailToastThr(mContext,errorInfo);
            }
        }).doRegexSize(views.edt_content,10,200,"意见反馈内容至少10字符，最多200字符","请填写意见反馈内容").builder();
        if(!flag)
        {return;}
        showLoading();
        Map<String,String> map= ApiConfig.createRequestMap(true);
        map.put("contact", LoginUserFactory.getLoginUserInfo().userId);
        map.put("feedBackTitle", "0000");
        map.put("feedBackContent", views.edt_content.getText().toString());
        ApiConfig.signRequestMap(map);
        new TextOKHttp().setUrl(ApiConfig.BASE_URL+"app/feedBack/doFeedBack").setRequestBodyText(map).doHttp(String.class, new TextHttpCallBack() {
            @Override
            public void httpCallBack(Object resultObject, String resultString, int status) {
                String errMsg= ResultFactory.getErrorTip(resultObject,status);
                if(StringUtils.isEmpty(errMsg))
                {
                    ToastUtil.makeOkToastThr(mContext,"意见反馈成功");
                    finish();
                }
                else{
                    ToastUtil.makeFailToastThr(mContext,errMsg);
                }
                dismissLoading();
            }
        });
    }
}
