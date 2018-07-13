package com.zjsj.mchtapp.module.repayment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruomm.base.ioc.adapter.BaseAdapter_T;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.dal.response.RepaymentBankCard;

import java.util.List;

public class BankCardListAdapter extends BaseAdapter_T<RepaymentBankCard>  {

    /**
     * 在构造函数里面显式声明变量复制和设置数据源
     *
     * @param mContext
     */
    public BankCardListAdapter(Context mContext, List<RepaymentBankCard> listDatas) {
       super(mContext,listDatas);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holderView=null;
        if(null==convertView){
            holderView=new HolderView();
            convertView=mInflater.inflate(R.layout.repayment_bankcard_item,null);
            BaseUtil.initInjectAll(holderView,HolderView.class,convertView);
//            BaseUtil.initInjectedView(holderView,HolderView.class,convertView);
            convertView.setTag(holderView);
        }
        else {
            holderView= (HolderView)convertView.getTag();
        }

        RepaymentBankCard tmp=listDatas.get(position);
        if("01".equals(tmp.bankCardType)) {
            holderView.lv_text_name.setText("借记卡");
        }
        else   if("02".equals(tmp.bankCardType)) {
            holderView.lv_text_name.setText("贷记卡");
        }
        else {
            holderView.lv_text_name.setText("银行卡");
        }
        holderView.lv_text_number.setText(tmp.accountNo);
        return convertView;
    }
    class HolderView{
        @InjectView(id = R.id.lv_text_name)
        TextView lv_text_name;
        @InjectView(id = R.id.lv_text_number)
        TextView lv_text_number;
    }
}
