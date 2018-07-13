package com.zjsj.mchtapp.module.repayment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.ruomm.base.http.okhttp.TextOKHttp;
import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.tools.StringUtils;
import com.ruomm.base.tools.ToastUtil;
import com.ruomm.base.view.menutopview.MenuTopListener;
import com.ruomm.baseconfig.debug.MLog;
import com.ruomm.resource.ui.AppMultiActivity;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.IntentFactory;
import com.zjsj.mchtapp.config.http.ApiConfig;
import com.zjsj.mchtapp.config.impl.TextHttpCallBack;
import com.zjsj.mchtapp.dal.response.RepaymentBankCard;
import com.zjsj.mchtapp.dal.response.base.ResultFactory;
import com.zjsj.mchtapp.module.repayment.adapter.BankCardListAdapter;

import org.greenrobot.eventbus.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BankCardListActivity extends AppMultiActivity {
    @InjectAll
    Views views=new Views();
    class Views{
        @InjectView(id=R.id.mListView)
        ListView mListView;
    }
    private List<RepaymentBankCard> listDatas=new ArrayList<>();
    BankCardListAdapter bankCardListAdapter=null;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setInitContentView(R.layout.repayment_backcardlist_lay);
        setMenuTop();
        bankCardListAdapter=new BankCardListAdapter(mContext,listDatas);
        views.mListView.setAdapter(bankCardListAdapter);
        doHttpGetBankCardList();
    }
    private void setMenuTop(){
        mymenutop.setRightImage(R.mipmap.icon_plus);

        mymenutop.setCenterText("银行卡管理");
        mymenutop.setMenuClickListener(new MenuTopListener() {
            @Override
            public void onMenuTopClick(View v, int vID) {
                if(vID==R.id.menutop_left)
                {
                    finish();
                }
                else  if(vID==R.id.menutop_right)
                {
                    startActivity(IntentFactory.getBindCardActivity());
                }
            }
        });
    }
//    private void doHttpGetBankCardList(){
//        for(int i=0;i<10;i++)
//        {
//            RepaymentBankCard repaymentBankCard=new RepaymentBankCard();
//            repaymentBankCard.bankName="中国"+i;
//            repaymentBankCard.accountNo="nihoa"+i;
//            listDatas.add(repaymentBankCard);
//        }
//        bankCardListAdapter.notifyDataSetChanged();
//    }
    private void doHttpGetBankCardList(){
        showLoading();
        Map<String,String> map=ApiConfig.createRequestMap(true);
        ApiConfig.signRequestMap(map);
        new TextOKHttp().setUrl(ApiConfig.BASE_URL+"app/repayment/doQueryBindCards").setRequestBodyText(map).doHttp(RepaymentBankCard.class, new TextHttpCallBack() {
            @Override
            public void httpCallBack(Object resultObject, String resultString, int status) {
                String error=ResultFactory.getErrorTip(resultObject,status);
                List<RepaymentBankCard> list=ResultFactory.getResult(resultObject,status);
                if(StringUtils.isEmpty(error)) {
                    if (null == list || list.size() <= 0) {
                        ToastUtil.makeFailToastThr(mContext,"你还没有绑定银行卡");
                    }
                    if (StringUtils.isEmpty(error)) {
                        ToastUtil.makeOkToastThr(mContext,"获取绑定银行卡成功");
                        listDatas.addAll(list);
                        bankCardListAdapter.notifyDataSetChanged();
                    }
                }
                else {
                    ToastUtil.makeFailToastThr(mContext,error);
                }
                dismissLoading();

            }
        });

    }
}
