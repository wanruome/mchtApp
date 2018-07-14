package com.zjsj.mchtapp.module.repayment.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruomm.base.ioc.adapter.BaseAdapter_T;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.dal.response.RepaymentBankCard;
import com.zjsj.mchtapp.dal.response.RepaymentOrderTransInfo;
import com.zjsj.mchtapp.util.StringMask;

import java.util.List;

public class RepaymentOrderAdapter extends BaseAdapter_T<RepaymentOrderTransInfo>  {

    /**
     * 在构造函数里面显式声明变量复制和设置数据源
     *
     * @param mContext
     */
    public RepaymentOrderAdapter(Context mContext, List<RepaymentOrderTransInfo> listDatas) {
       super(mContext,listDatas);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holderView=null;
        if(null==convertView){
            holderView=new HolderView();
            convertView=mInflater.inflate(R.layout.repayment_order_item,null);
            BaseUtil.initInjectAll(holderView,HolderView.class,convertView);
//            BaseUtil.initInjectedView(holderView,HolderView.class,convertView);
            convertView.setTag(holderView);
        }
        else {
            holderView= (HolderView)convertView.getTag();
        }

        RepaymentOrderTransInfo tmp=listDatas.get(position);
        holderView.lv_text_type.setText("消费");
        holderView.lv_text_amount.setText(tmp.amount+"元");
        holderView.lv_text_status.setText(transOrderStatus(tmp.orderState));
        holderView.lv_text_cardno.setText(StringMask.getMaskBankNo(tmp.accountNo));
        holderView.lv_text_ordertime.setText(tmp.orderTime);
        return convertView;
    }
    class HolderView{
        @InjectView(id = R.id.lv_text_type)
        TextView lv_text_type;
        @InjectView(id = R.id.lv_text_amount)
        TextView lv_text_amount;
        @InjectView(id = R.id.lv_text_status)
        TextView lv_text_status;
        @InjectView(id = R.id.lv_text_cardno)
        TextView lv_text_cardno;
        @InjectView(id = R.id.lv_text_ordertime)
        TextView lv_text_ordertime;
    }

    private static String transOrderStatus(String status)
    {
//        01	未支付	02	支付中
//        03	已支付	04	已作废
//        05	退款中	06	已撤销
//        07	已退货
        if("01".equals(status))
        {
            return "未支付";
        }
        else  if("02".equals(status))
        {
            return "支付中";
        }
        else  if("03".equals(status))
        {
            return "已支付";
        }
        else  if("04".equals(status))
        {
            return "已作废";
        }
        else  if("05".equals(status))
        {
            return "退款中";
        }
        else  if("06".equals(status))
        {
            return "已撤销";
        }
        else  if("07".equals(status))
        {
            return "已退货";
        }
        else
        {
            return "待审核";
        }

    }
}
