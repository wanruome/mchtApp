package com.zjsj.mchtapp.module.repayment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.view.menutopview.MenuTopListener;
import com.ruomm.baseconfig.debug.MLog;
import com.ruomm.resource.ui.AppMultiActivity;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.core.RepaymentConfigFactory;
import com.zjsj.mchtapp.dal.response.RepaymentOrderTransInfo;

public class RepaymentOrderDetailActivity extends AppMultiActivity {
    @InjectAll
    Views views=new Views();
    class Views{
        @InjectView(id=R.id.text_order_amount)
        TextView text_order_amount;
        @InjectView(id=R.id.text_order_orderState)
        TextView text_order_orderState;
        @InjectView(id=R.id.text_order_tranType)
        TextView text_order_tranType;
        @InjectView(id=R.id.text_order_outcmp)
        TextView text_order_outcmp;
        @InjectView(id=R.id.text_order_incmp)
        TextView text_order_incmp;
        @InjectView(id=R.id.text_order_remark)
        TextView text_order_remark;
        @InjectView(id=R.id.text_order_time)
        TextView text_order_time;
        @InjectView(id=R.id.text_order_finger)
        TextView text_order_finger;


    }
    RepaymentOrderTransInfo repaymentOrderTransInfo;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setMenuTop();
        setInitContentView(R.layout.repayment_orderdetail_lay);
        repaymentOrderTransInfo= BaseUtil.serializableGet(getIntent(),RepaymentOrderTransInfo.class);
        setDataForViews();
    }
    private void setDataForViews()
    {
        views.text_order_amount.setText(repaymentOrderTransInfo.amount+"元");
        views.text_order_orderState.setText(RepaymentConfigFactory.parseOrderState(repaymentOrderTransInfo.orderState));
        views.text_order_tranType.setText("消费");
        views.text_order_outcmp.setText(repaymentOrderTransInfo.bankName+"("+repaymentOrderTransInfo.accountNo+")");
        views.text_order_incmp.setText(repaymentOrderTransInfo.merchantName);
        views.text_order_time.setText(repaymentOrderTransInfo.orderTime);
        views.text_order_finger.setText(repaymentOrderTransInfo.orderNo);
    }
    private void setMenuTop(){

        mymenutop.setCenterText("交易详情");
        mymenutop.setMenuClickListener(new MenuTopListener() {
            @Override
            public void onMenuTopClick(View v, int vID) {
                if(vID== R.id.menutop_left)
                {
                    finish();
                }
            }
        });
    }
}
