package com.zjsj.mchtapp.module.repayment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ruomm.base.http.okhttp.TextOKHttp;
import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.iocutil.AppStoreUtil;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.tools.StringUtils;
import com.ruomm.base.tools.ToastUtil;
import com.ruomm.base.view.dialog.BaseDialogClickListener;
import com.ruomm.base.view.menutopview.MenuTopListener;
import com.ruomm.baseconfig.debug.MLog;
import com.ruomm.resource.dialog.MessageDialog;
import com.ruomm.resource.dialog.dal.DialogValue;
import com.ruomm.resource.ui.AppMultiActivity;
import com.ruomm.resource.ui.dal.WebUrlInfo;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.IntentFactory;
import com.zjsj.mchtapp.config.LoginUserFactory;
import com.zjsj.mchtapp.config.http.ApiConfig;
import com.zjsj.mchtapp.config.impl.TextHttpCallBack;
import com.zjsj.mchtapp.dal.event.BindCardResultEvent;
import com.zjsj.mchtapp.dal.event.LoginEvent;
import com.zjsj.mchtapp.dal.response.RepaymentBankCard;
import com.zjsj.mchtapp.dal.response.RepaymentBindCardDto;
import com.zjsj.mchtapp.dal.response.RepaymentUnBindCardDto;
import com.zjsj.mchtapp.dal.response.base.ResultDto;
import com.zjsj.mchtapp.dal.response.base.ResultFactory;
import com.zjsj.mchtapp.dal.store.UserBankCardStore;
import com.zjsj.mchtapp.module.repayment.adapter.BankCardListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Logger;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BankCardListActivity extends AppMultiActivity {
    @InjectAll
    Views views=new Views();
    class Views{
        @InjectView(id=R.id.mListView)
        ListView mListView;
        @InjectView(id=R.id.text_tip)
        TextView text_tip;
    }
    private List<RepaymentBankCard> listDatas=new ArrayList<>();
    BankCardListAdapter bankCardListAdapter=null;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        EventBus.getDefault().register(this);
        setInitContentView(R.layout.repayment_backcardlist_lay);
        setMenuTop();
        bankCardListAdapter=new BankCardListAdapter(mContext,listDatas);
        views.mListView.setAdapter(bankCardListAdapter);
        views.mListView.setOnItemLongClickListener(myOnItemLongClickListener);
        doHttpGetBankCardList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe
    public void onEventMainThrend(BindCardResultEvent event){
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
    private AdapterView.OnItemLongClickListener myOnItemLongClickListener=new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            RepaymentBankCard repaymentBankCard=(RepaymentBankCard)views.mListView.getAdapter().getItem(position);
            if(null!=repaymentBankCard)
            {
                doUnBindCardTask(repaymentBankCard);
            }
            return false;
        }
    };
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
        views.text_tip.setText("获取绑定银行卡");
        views.text_tip.setVisibility(View.GONE);
        Map<String,String> map=ApiConfig.createRequestMap(true);
        ApiConfig.signRequestMap(map);
        new TextOKHttp().setUrl(ApiConfig.BASE_URL+"app/repayment/doQueryBindCards").setRequestBodyText(map).doHttp(RepaymentBankCard.class, new TextHttpCallBack() {
            @Override
            public void httpCallBack(Object resultObject, String resultString, int status) {
                String error=ResultFactory.getErrorTip(resultObject,status);
                List<RepaymentBankCard> list=ResultFactory.getResult(resultObject,status);
                if(StringUtils.isEmpty(error)) {
                    listDatas.clear();
                    if (null == list || list.size() <= 0) {
                        ToastUtil.makeOkToastThr(mContext,"你还没有绑定银行卡");
                        views.text_tip.setText("你还没有绑定银行卡");
                        views.text_tip.setVisibility(View.VISIBLE);
                    }
                    else{
                        listDatas.addAll(list);
                        ToastUtil.makeOkToastThr(mContext,"获取绑定银行卡成功");
                        views.text_tip.setText("获取绑定银行卡成功");
                        views.text_tip.setVisibility(View.GONE);
                    }
                    LoginUserFactory.saveBankCardInfo(listDatas);
                    bankCardListAdapter.notifyDataSetChanged();
                }
                else {
                    ToastUtil.makeFailToastThr(mContext,error);
                    views.text_tip.setText(error);
                    views.text_tip.setVisibility(View.VISIBLE);
                }
                dismissLoading();

            }
        });
    }
    private void doUnBindCardTask(final RepaymentBankCard repaymentBankCard){
        if(null==repaymentBankCard)
        {
            return;
        }
        MessageDialog messageDialog=new MessageDialog(mContext);
        messageDialog.setDialogValue(new DialogValue("解除绑定","你是否要解绑卡号为"+repaymentBankCard.accountNo+"的银行卡吗？"));
        messageDialog.setBaseDialogClick(new BaseDialogClickListener() {
            @Override
            public void onDialogItemClick(View v, Object tag) {
                if(v.getId()==R.id.dialog_confirm)
                {
                    doHttpTaskUnBindCard(repaymentBankCard);
                }
            }
        });
        messageDialog.show();
    }
    private void doHttpTaskUnBindCard(final RepaymentBankCard repaymentBankCard){
        showLoading();
        Map<String,String> map=ApiConfig.createRequestMap(true);
        map.put("cardIndex",repaymentBankCard.cardIndex);
        ApiConfig.signRequestMap(map);
        new TextOKHttp().setUrl(ApiConfig.BASE_URL+"app/repayment/doUnBindCard").setRequestBodyText(map).doHttp(RepaymentUnBindCardDto.class, new TextHttpCallBack() {
            @Override
            public void httpCallBack(Object resultObject, String resultString, int status) {
                String errMsg= ResultFactory.getErrorTip(resultObject,status);
                if(!StringUtils.isEmpty(errMsg)){
                    ResultDto resultDto=(ResultDto) resultObject;
                    RepaymentUnBindCardDto resultUnBindCardDto=ResultFactory.getResult(resultObject,status);
                    if(null!=resultDto&&ResultFactory.ERR_REPAYMENT.equals(resultDto.code))
                    {
                        errMsg=null==resultUnBindCardDto?errMsg:resultUnBindCardDto.responseRemark;
                    }
                    ToastUtil.makeFailToastThr(mContext,errMsg);
                }
                else{
                    RepaymentUnBindCardDto resultUnBindCardDto=ResultFactory.getResult(resultObject,status);
                    listDatas.remove(repaymentBankCard);
                    LoginUserFactory.saveBankCardInfo(listDatas);
                    MLog.i(resultUnBindCardDto);
                    ToastUtil.makeOkToastThr(mContext,"解绑成功");
                    bankCardListAdapter.notifyDataSetChanged();
                }
                dismissLoading();

            }
        });
    }
}
