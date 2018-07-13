package com.zjsj.mchtapp.config.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruomm.base.http.config.ResponseParse;
import com.ruomm.base.tools.StringUtils;
import com.zjsj.mchtapp.dal.response.base.ResultDto;

public class ResponseParseImpl implements ResponseParse {
    @Override
    public Object parseResponseText(String resourceString, Class<?> cls) {

        ResultDto resultBase=null;
        try{
            JSONObject jsonObject=JSON.parseObject(resourceString);
            resultBase=new ResultDto();
            resultBase.code=jsonObject.getString("code");
            resultBase.msg=jsonObject.getString("msg");

            try{
                String data=jsonObject.getString("data");
                if(StringUtils.isEmpty(data))
                {
                    resultBase.data=null;
                }

                else if(cls.getName().equals(String.class.getName()))
                {
                    resultBase.data=data;
                }
                else if(data.startsWith("[")&&data.endsWith("]"))
                {
                    resultBase.data=JSON.parseArray(data,cls);
                }
                else{
                    resultBase.data=JSON.parseObject(data,cls);
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }catch (Exception e)
        {
            e.printStackTrace();
            resultBase=null;
        }

        return resultBase;

    }
}
