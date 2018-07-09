package com.zjsj.mchtapp.config.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruomm.base.http.config.ResponseParse;
import com.ruomm.base.tools.StringUtils;
import com.zjsj.mchtapp.dal.response.base.ResultBase;

public class AppResponseParse implements ResponseParse {
    @Override
    public Object parseResponseText(String resourceString, Class<?> cls) {

        ResultBase resultBase=null;
        try{
            JSONObject jsonObject=JSON.parseObject(resourceString);
            resultBase=new ResultBase();
            resultBase.code=jsonObject.getString("code");
            resultBase.msg=jsonObject.getString("msg");

            try{
                String data=jsonObject.getString("data");
                if(StringUtils.isEmpty(data))
                {
                    resultBase.data=null;
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
