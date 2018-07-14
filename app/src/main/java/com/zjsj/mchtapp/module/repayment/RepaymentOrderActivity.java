package com.zjsj.mchtapp.module.repayment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ruomm.base.http.okhttp.TextOKHttp;
import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.tools.StringUtils;
import com.ruomm.base.tools.ToastUtil;
import com.ruomm.base.view.dialog.BaseDialogClickListener;
import com.ruomm.base.view.menutopview.MenuTopListener;
import com.ruomm.resource.dialog.DayStartEndWheelDialog;
import com.ruomm.resource.ui.AppMultiActivity;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.LoginUserFactory;
import com.zjsj.mchtapp.config.http.ApiConfig;
import com.zjsj.mchtapp.config.impl.TextHttpCallBack;
import com.zjsj.mchtapp.dal.response.RepaymentBankCard;
import com.zjsj.mchtapp.dal.response.RepaymentBindCardDto;
import com.zjsj.mchtapp.dal.response.RepaymentOrderDto;
import com.zjsj.mchtapp.dal.response.RepaymentOrderTransInfo;
import com.zjsj.mchtapp.dal.response.base.ResultDto;
import com.zjsj.mchtapp.dal.response.base.ResultFactory;
import com.zjsj.mchtapp.module.repayment.adapter.RepaymentOrderAdapter;
import com.zjsj.mchtapp.view.BankCardWheelDialog;
import com.zjsj.mchtapp.view.ListStringWheelDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RepaymentOrderActivity extends AppMultiActivity{
    @InjectAll
    Views views=new Views();
    class Views{
        @InjectView(id=R.id.ly_query_ordertime)
        LinearLayout  ly_query_ordertime;
        @InjectView(id=R.id.ly_query_bankcard)
        LinearLayout  ly_query_bankcard;
        @InjectView(id=R.id.ly_query_mobile)
        LinearLayout  ly_query_mobile;
        @InjectView(id=R.id.text_query_ordertime)
        TextView text_query_ordertime;
        @InjectView(id=R.id.text_query_bankcard)
        TextView  text_query_bankcard;
        @InjectView(id=R.id.text_query_mobile)
        TextView  text_query_mobile;
        @InjectView(id=R.id.mListView)
        ListView mListView;
    }
    List<RepaymentBankCard> listBankCards=null;
    private RepaymentBankCard queryBankCard;
    List<String> phoneNumbers=new ArrayList<String>();
    List<RepaymentOrderTransInfo> listDatas=new ArrayList<RepaymentOrderTransInfo>();
    private RepaymentOrderAdapter repaymentOrderAdapter;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setMenuTop();
        setInitContentView(R.layout.repayment_orders_lay);
        setViewDatas();
        doHttpTask();
    }
    private void setViewDatas(){
        listBankCards= LoginUserFactory.getBankCardInfo();
        if(null==listBankCards||listBankCards.size()<=0)
        {
            ToastUtil.makeFailToastThr(mContext,"你还没有绑定银行卡");
            finish();
            return;
        }
        phoneNumbers.add(LoginUserFactory.getLoginUserInfo().mobile);
        for(RepaymentBankCard tmp:listBankCards)
        {
            if(StringUtils.isEmpty(tmp.mobileNo))
            {
                continue;
            }
            if(!phoneNumbers.contains(tmp.mobileNo))
            {
                phoneNumbers.add(tmp.mobileNo);
            }
        }
        views.text_query_mobile.setText(LoginUserFactory.getLoginUserInfo().mobile);
        views.ly_query_bankcard.setOnClickListener(myOnClickListener);
        views.ly_query_mobile.setOnClickListener(myOnClickListener);
        views.ly_query_ordertime.setOnClickListener(myOnClickListener);
        repaymentOrderAdapter=new RepaymentOrderAdapter(mContext,listDatas);
    }
    private void setMenuTop(){

        mymenutop.setCenterText("交易记录");
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
    private View.OnClickListener myOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vID=v.getId();
            if(vID==R.id.ly_query_bankcard)
            {
                BankCardWheelDialog bankCardWheelDialog=new BankCardWheelDialog(mContext, listBankCards,queryBankCard, new BaseDialogClickListener() {
                    @Override
                    public void onDialogItemClick(View v, Object tag) {
                        int vID=v.getId();
                        if(vID==R.id.wheelbtn_over)
                        {
                            queryBankCard=(RepaymentBankCard) v.getTag();
                            views.text_query_bankcard.setText(queryBankCard.accountNo);
                            doHttpTask();
                        }
                        else if(vID==R.id.wheelbtn_clear)
                        {
                            queryBankCard=null;
                            views.text_query_bankcard.setText(null);
                            doHttpTask();
                        }
                    }
                });
                bankCardWheelDialog.show();
            }
            if(vID==R.id.ly_query_mobile)
            {
                ListStringWheelDialog listStringWheelDialog=new ListStringWheelDialog(mContext, phoneNumbers,null, new BaseDialogClickListener() {
                    @Override
                    public void onDialogItemClick(View v, Object tag) {
                        int vID=v.getId();
                        if(vID==R.id.wheelbtn_over)
                        {
                            views.text_query_mobile.setText((String)v.getTag());
                            doHttpTask();
                        }
                        else if(vID==R.id.wheelbtn_clear)
                        {
                            views.text_query_mobile.setText(null);
                            doHttpTask();
                        }
                    }
                });
                listStringWheelDialog.show();
            }
            if(vID==R.id.ly_query_ordertime)
            {
                DayStartEndWheelDialog dayStartEndWheelDialog=new DayStartEndWheelDialog(mContext, new BaseDialogClickListener() {
                    @Override
                    public void onDialogItemClick(View v, Object tag) {
                        int vID=v.getId();
                        if(vID==R.id.wheelbtn_over)
                        {
                            views.text_query_ordertime.setText((String) v.getTag());
                            doHttpTask();
                        }
                        else if(vID==R.id.wheelbtn_clear)
                        {
                            views.text_query_ordertime.setText(null);
                            doHttpTask();
                        }
                    }
                });
                dayStartEndWheelDialog.setDateValue( views.text_query_ordertime.getText().toString());
                dayStartEndWheelDialog.show();
            }
        }
    };
    private void doHttpTask(){
        String cardIndex=null==queryBankCard?null:queryBankCard.cardIndex;
        String mobile=views.text_query_mobile.getText().toString();
        String orderDate=views.text_query_ordertime.getText().toString();
        if(StringUtils.isEmpty(cardIndex)&&StringUtils.isEmpty(mobile))
        {
            ToastUtil.makeFailToastThr(mContext,"卡号和手机号必须选择一个");
            return;
        }
        showLoading();
        Map<String,String> map= ApiConfig.createRequestMap(true);
        if(!StringUtils.isEmpty(cardIndex))
        {
            map.put("cardIndex",cardIndex);
        }
        if(!StringUtils.isEmpty(mobile))
        {
            map.put("mobileNo",mobile);
        }
        if(!StringUtils.isEmpty(orderDate))
        {
            map.put("orderDate",orderDate);
        }
        ApiConfig.signRequestMap(map);
        new TextOKHttp().setUrl(ApiConfig.BASE_URL+"app/repayment/doQueryOrders").setRequestBodyText(map).doHttp(RepaymentOrderDto.class, new TextHttpCallBack() {
            @Override
            public void httpCallBack(Object resultObject, String resultString, int status) {
                String errMsg= ResultFactory.getErrorTip(resultObject,status);
                if(!StringUtils.isEmpty(errMsg)){
                    ResultDto resultDto=(ResultDto) resultObject;
                    RepaymentOrderDto repaymentOrderDto=ResultFactory.getResult(resultObject,status);
                    if(null!=resultDto&&ResultFactory.ERR_REPAYMENT.equals(resultDto.code))
                    {
                        errMsg=null==repaymentOrderDto?errMsg:repaymentOrderDto.responseRemark;
                    }
                    ToastUtil.makeFailToastThr(mContext,errMsg);
                }
                else {
                    listDatas.clear();
                    RepaymentOrderDto repaymentOrderDto=ResultFactory.getResult(resultObject,status);
                    if(null==repaymentOrderDto.transInfo||repaymentOrderDto.transInfo.size()<=0)
                    {
                        ToastUtil.makeOkToastThr(mContext,"没有查询到符合条件的订单");
                    }
                    else{
                        listDatas.addAll(repaymentOrderDto.transInfo);
                    }
                    repaymentOrderAdapter.notifyDataSetChanged();
                }
                dismissLoading();
            }
        });
    }
}
