package com.zjsj.mchtapp.core;

import com.ruomm.base.tools.StringUtils;
import com.zjsj.mchtapp.dal.local.RepaymentTranType;

import java.util.ArrayList;
import java.util.List;

public class RepaymentConfigFactory {
    public static final String ORDER_DEBUG_MOBILE="18100000000";
    public static  List<String> getTransTypeList(){
        //        01	未支付	02	支付中
        //        03	已支付	04	已作废
        //        05	退款中	06	已撤销
        //        07	已退货
        List<String> list=new ArrayList<String>();
        list.add("全部");
        list.add("消费");
        return list;
    }
    public static  List<String> getOrderStateDes(){
        //        01	未支付	02	支付中
        //        03	已支付	04	已作废
        //        05	退款中	06	已撤销
        //        07	已退货
        List<String> list=new ArrayList<String>();
        list.add("全部");
        list.add("未支付");
        list.add("支付中");
        list.add("已支付");
        list.add("已作废");
        list.add("退款中");
        list.add("已撤销");
        list.add("已退货");
        return list;
    }
    public static String getOrderStateByDes(String orderStateDes)
    {
        if(StringUtils.isEmpty(orderStateDes)){
            return null;
        }
        List<String>  list=getOrderStateDes();
        int index=list.indexOf(orderStateDes);
        if(index<=0)
        {
            return null;
        }
        else{
            return "0"+index;
        }
    }

}
