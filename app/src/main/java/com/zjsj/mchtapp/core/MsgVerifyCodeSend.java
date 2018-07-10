package com.zjsj.mchtapp.core;

import com.ruomm.base.http.config.impl.TextHttpCallBack;
import com.ruomm.base.http.okhttp.TextOKHttp;
import com.ruomm.base.tools.StringUtils;
import com.zjsj.mchtapp.config.http.ApiConfig;
import com.zjsj.mchtapp.dal.event.MsgSendEvent;
import com.zjsj.mchtapp.dal.response.MsgSendDto;
import com.zjsj.mchtapp.dal.response.base.ResultFactory;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import java.util.Map;

public class MsgVerifyCodeSend {
    public void sendMessTypeNoLogin(String functionId, String msgAddr, final String tag)
    {

        Map<String, String> map = ApiConfig.createRequestMap(false);
        // /13656655336
        // map.put("userId", "13355667788");
        // map.put("userId", );
        map.put("msgFunction", functionId);
        map.put("msgAddr", msgAddr);
        new TextOKHttp().setUrl(ApiConfig.BASE_URL+"app/msg/doSendMsg").setRequestBodyText(map).doHttp(MsgSendDto.class, new TextHttpCallBack() {
            @Override
            public void httpCallBack(Object resultObject, String resultString, int status) {
               String errorTip=ResultFactory.getErrorTip(resultObject,status);
               MsgSendEvent msgSendEvent=new MsgSendEvent();
               if(StringUtils.isEmpty(errorTip))
               {
                   msgSendEvent.errMsg=errorTip;
                   msgSendEvent.isOK=true;
                   msgSendEvent.tag=tag;

               }
               else{
                   msgSendEvent.errMsg=errorTip;
                   msgSendEvent.isOK=true;
                   msgSendEvent.tag=tag;
               }
                EventBus.getDefault().post(msgSendEvent);
            }
        });
    }
    public void sendMessTypeLogin(String functionId)
    {

    }
    public void sendMessTypeAuth(String functionId)
    {

    }
}
