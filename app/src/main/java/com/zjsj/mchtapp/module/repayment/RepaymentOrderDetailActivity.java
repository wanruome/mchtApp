package com.zjsj.mchtapp.module.repayment;

import android.os.Bundle;
import android.view.View;

import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.view.menutopview.MenuTopListener;
import com.ruomm.baseconfig.debug.MLog;
import com.ruomm.resource.ui.AppMultiActivity;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.dal.response.RepaymentOrderTransInfo;

public class RepaymentOrderDetailActivity extends AppMultiActivity {
    RepaymentOrderTransInfo repaymentOrderTransInfo;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setMenuTop();
        setInitContentView(R.layout.repayment_orderdetail_lay);
        repaymentOrderTransInfo= BaseUtil.serializableGet(getIntent(),RepaymentOrderTransInfo.class);
        MLog.i(repaymentOrderTransInfo);
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
