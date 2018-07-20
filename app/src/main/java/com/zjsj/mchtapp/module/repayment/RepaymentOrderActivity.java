package com.zjsj.mchtapp.module.repayment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ruomm.base.http.okhttp.TextOKHttp;
import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.tools.StringUtils;
import com.ruomm.base.tools.ToastUtil;
import com.ruomm.base.view.dialog.BaseDialogClickListener;
import com.ruomm.base.view.menutopview.MenuTopListener;
import com.ruomm.resource.dialog.DayStartEndWheelDialog;
import com.ruomm.resource.ui.AppMultiActivity;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.IntentFactory;
import com.zjsj.mchtapp.config.LoginUserFactory;
import com.zjsj.mchtapp.config.http.ApiConfig;
import com.zjsj.mchtapp.config.impl.TextHttpCallBack;
import com.zjsj.mchtapp.core.RepaymentConfigFactory;
import com.zjsj.mchtapp.dal.response.RepaymentBankCard;
import com.zjsj.mchtapp.dal.response.RepaymentBindCardDto;
import com.zjsj.mchtapp.dal.response.RepaymentOrderDto;
import com.zjsj.mchtapp.dal.response.RepaymentOrderTransInfo;
import com.zjsj.mchtapp.dal.response.base.ResultDto;
import com.zjsj.mchtapp.dal.response.base.ResultFactory;
import com.zjsj.mchtapp.module.repayment.adapter.RepaymentOrderAdapter;
import com.zjsj.mchtapp.util.StringMask;
import com.zjsj.mchtapp.view.BankCardWheelDialog;
import com.zjsj.mchtapp.view.ListStringWheelDialog;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RepaymentOrderActivity extends AppMultiActivity{
    @InjectAll
    Views views=new Views();
    class Views{
        @InjectView(id=R.id.ly_query_transType)
        LinearLayout  ly_query_transType;
        @InjectView(id=R.id.text_query_transType)
        TextView text_query_transType;
        @InjectView(id=R.id.ly_query_bankCard)
        LinearLayout  ly_query_bankCard;
        @InjectView(id=R.id.text_query_bankCard)
        TextView text_query_bankCard;
        @InjectView(id=R.id.ly_query_orderState)
        LinearLayout  ly_query_orderState;
        @InjectView(id=R.id.text_query_orderState)
        TextView text_query_orderState;
        @InjectView(id=R.id.ly_query_orderTime)
        LinearLayout  ly_query_orderTime;
        @InjectView(id=R.id.text_query_orderTime)
        TextView text_query_orderTime;
        @InjectView(id=R.id.mListView)
        ListView mListView;
        @InjectView(id=R.id.text_tip)
        TextView text_tip;
    }
    List<String> transTypeList= RepaymentConfigFactory.getTransTypeList();
    List<RepaymentBankCard> listBankCards=new ArrayList<RepaymentBankCard>();
    private RepaymentBankCard queryBankCard;
    List<String> orderStateDesList= RepaymentConfigFactory.getOrderStateDes();
    List<RepaymentOrderTransInfo> listDatasQuery=new ArrayList<RepaymentOrderTransInfo>();
    List<RepaymentOrderTransInfo> listDatas=new ArrayList<RepaymentOrderTransInfo>();

    private RepaymentOrderAdapter repaymentOrderAdapter;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setMenuTop();
        setInitContentView(R.layout.repayment_orders_lay);
        setViewDatas();

    }
    private void setViewDatas(){
        List<RepaymentBankCard> listBankTemp= LoginUserFactory.getBankCardInfo();
        if(null==listBankTemp||listBankTemp.size()<=0)
        {
            ToastUtil.makeFailToastThr(mContext,"你还没有绑定银行卡");
            finish();
            return;
        }
        RepaymentBankCard firstBankCard=new RepaymentBankCard();
        firstBankCard.cardIndex="0";
        firstBankCard.accountNo="全部";
        listBankCards.add(firstBankCard);
        listBankCards.addAll(listBankTemp);

        views.text_query_transType.setText("全部");
        views.text_query_bankCard.setText("全部");
        views.text_query_orderState.setText("全部");
        views.text_query_orderTime.setText("当天");
//        views.text_query_orderTime.setText("20180528-20180530");
        views.ly_query_transType.setOnClickListener(myOnClickListener);
        views.ly_query_bankCard.setOnClickListener(myOnClickListener);
        views.ly_query_orderState.setOnClickListener(myOnClickListener);
        views.ly_query_orderTime.setOnClickListener(myOnClickListener);
        repaymentOrderAdapter=new RepaymentOrderAdapter(mContext,listDatas);
        views.mListView.setAdapter(repaymentOrderAdapter);
        views.mListView.setOnItemClickListener(myOnItemClickListener);
        doHttpTaskQueryOders();
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
            if(vID==R.id.ly_query_transType){
                ListStringWheelDialog listStringWheelDialog=new ListStringWheelDialog(mContext, transTypeList,views.text_query_transType.getText().toString(), new BaseDialogClickListener() {
                    @Override
                    public void onDialogItemClick(View v, Object tag) {
                        int vID=v.getId();
                        if(vID==R.id.wheelbtn_over)
                        {
                            views.text_query_transType.setText((String)v.getTag());
                            updateUiBySelection();
                        }
                    }
                });
                listStringWheelDialog.setDialogTitile("选择交易类型");
                listStringWheelDialog.show();
            }
            if(vID==R.id.ly_query_bankCard)
            {
                BankCardWheelDialog bankCardWheelDialog=new BankCardWheelDialog(mContext, listBankCards,queryBankCard, new BaseDialogClickListener() {
                    @Override
                    public void onDialogItemClick(View v, Object tag) {
                        int vID=v.getId();
                        if(vID==R.id.wheelbtn_over)
                        {
                            queryBankCard=(RepaymentBankCard) v.getTag();
                            views.text_query_bankCard.setText(queryBankCard.accountNo);
                            updateUiBySelection();
                        }

                    }
                });
                bankCardWheelDialog.show();
            }
            if(vID==R.id.ly_query_orderState)
            {
                ListStringWheelDialog listStringWheelDialog=new ListStringWheelDialog(mContext, orderStateDesList,views.text_query_orderState.getText().toString(), new BaseDialogClickListener() {
                    @Override
                    public void onDialogItemClick(View v, Object tag) {
                        int vID=v.getId();
                        if(vID==R.id.wheelbtn_over)
                        {
                            views.text_query_orderState.setText((String)v.getTag());
                            updateUiBySelection();
                        }
                    }
                });
                listStringWheelDialog.setDialogTitile("选择交易状态");
                listStringWheelDialog.show();
            }
            if(vID==R.id.ly_query_orderTime)
            {
                DayStartEndWheelDialog dayStartEndWheelDialog=new DayStartEndWheelDialog(mContext, new BaseDialogClickListener() {
                    @Override
                    public void onDialogItemClick(View v, Object tag) {
                        int vID=v.getId();
                        if(vID==R.id.wheelbtn_over)
                        {
                            views.text_query_orderTime.setText((String) v.getTag());
                            doHttpTaskQueryOders();
                        }
                        else if(vID==R.id.wheelbtn_clear)
                        {
                            views.text_query_orderTime.setText("当天");
                            doHttpTaskQueryOders();
                        }
                    }
                });
                dayStartEndWheelDialog.setDateValue( views.text_query_orderTime.getText().toString());
                dayStartEndWheelDialog.show();
            }
        }
    };
    private AdapterView.OnItemClickListener myOnItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            RepaymentOrderTransInfo repaymentOrderTransInfo=(RepaymentOrderTransInfo)repaymentOrderAdapter.getItem(position);
            Intent intent=IntentFactory.getRepaymentOrderDetailActivity();
            BaseUtil.serializablePut(intent,repaymentOrderTransInfo);
            startActivity(intent);

        }
    };
    private void doHttpTaskQueryOders(){
        showLoading();
        String moblie=StringUtils.isEmpty(RepaymentConfigFactory.ORDER_DEBUG_MOBILE)?LoginUserFactory.getLoginUserInfo().mobile:RepaymentConfigFactory.ORDER_DEBUG_MOBILE;
        String orderDate=views.text_query_orderTime.getText().toString();

        Map<String,String> map= ApiConfig.createRequestMap(true);
        map.put("mobileNo",moblie);
        if(StringUtils.getLength(orderDate)>=8)
        {
            map.put("orderDate",orderDate);
        }
        ApiConfig.signRequestMap(map);
        new TextOKHttp().setUrl(ApiConfig.BASE_URL+"app/repayment/doQueryOrders").setRequestBodyText(map).doHttp(RepaymentOrderDto.class, new TextHttpCallBack() {
            @Override
            public void httpCallBack(Object resultObject, String resultString, int status) {
                String errMsg= ResultFactory.getErrorTip(resultObject,status);
                listDatasQuery.clear();
                if(!StringUtils.isEmpty(errMsg)){
                    ResultDto resultDto=(ResultDto) resultObject;
                    RepaymentOrderDto repaymentOrderDto=ResultFactory.getResult(resultObject,status);
                    if(null!=resultDto&&ResultFactory.ERR_REPAYMENT.equals(resultDto.code))
                    {
                        errMsg=null==repaymentOrderDto?errMsg:repaymentOrderDto.responseRemark;
                    }
                    ToastUtil.makeFailToastThr(mContext,errMsg);
                    views.text_tip.setText(errMsg);
                }
                else {
                    listDatasQuery.clear();
                    RepaymentOrderDto repaymentOrderDto=ResultFactory.getResult(resultObject,status);
                    if(null==repaymentOrderDto.transInfo||repaymentOrderDto.transInfo.size()<=0)
                    {
                        ToastUtil.makeOkToastThr(mContext,"没有查询到符合条件的订单");
                        views.text_tip.setText("没有查询到符合条件的订单");
                    }
                    else{
                        listDatasQuery.addAll(repaymentOrderDto.transInfo);
                        views.text_tip.setText(null);
                    }
                }
                updateUiBySelection();
                dismissLoading();
            }
        });
    }
    private void updateUiBySelection(){

        listDatas.clear();
        if(listDatasQuery.size()<=0)
        {
            repaymentOrderAdapter.notifyDataSetChanged();
            return;
        }
        List<RepaymentOrderTransInfo> listByBank=new ArrayList<>();
        for(RepaymentOrderTransInfo tmp:listDatasQuery){
            if(null!=queryBankCard&&!"0".equals(queryBankCard.cardIndex)) {
                if(StringUtils.getLength(tmp.accountNo)>0&&queryBankCard.accountNo.endsWith(tmp.accountNo)){
                    listByBank.add(tmp);
                }
            }
            else {
                listByBank.add(tmp);
            }
        }

        List<RepaymentOrderTransInfo> listByType=new ArrayList<>();
        String orderState=RepaymentConfigFactory.getOrderStateByDes(views.text_query_orderState.getText().toString());
        for(RepaymentOrderTransInfo tmp:listByBank){
            if(StringUtils.isEmpty(orderState)||orderState.equals("全部"))
            {
                listByType.add(tmp);
            }
            else{
                if(orderState.equals(tmp.orderState))
                {
                    listByType.add(tmp);
                }
            }
        }
        if(null!=listByType&&listByType.size()>0)
        {
            listDatas.addAll(listByType);
            views.text_tip.setText(null);
        }
        else{

            views.text_tip.setText("没有查询到符合条件的订单");
        }
        repaymentOrderAdapter.notifyDataSetChanged();
    }
//    private void doHttpTask(){
//        String cardIndex=null==queryBankCard?null:queryBankCard.cardIndex;
//        String mobile=views.text_query_mobile.getText().toString();
//        String orderDate=views.text_query_ordertime.getText().toString();
//        if(StringUtils.isEmpty(cardIndex)&&StringUtils.isEmpty(mobile))
//        {
//            ToastUtil.makeFailToastThr(mContext,"卡号和手机号必须选择一个");
//            return;
//        }
//        showLoading();
//        Map<String,String> map= ApiConfig.createRequestMap(true);
//        if(!StringUtils.isEmpty(cardIndex))
//        {
//            map.put("cardIndex",cardIndex);
//        }
//        if(!StringUtils.isEmpty(mobile))
//        {
//            map.put("mobileNo",mobile);
//        }
//        if(!StringUtils.isEmpty(orderDate))
//        {
//            map.put("orderDate",orderDate);
//        }
//        ApiConfig.signRequestMap(map);
//        new TextOKHttp().setUrl(ApiConfig.BASE_URL+"app/repayment/doQueryOrders").setRequestBodyText(map).doHttp(RepaymentOrderDto.class, new TextHttpCallBack() {
//            @Override
//            public void httpCallBack(Object resultObject, String resultString, int status) {
//                String errMsg= ResultFactory.getErrorTip(resultObject,status);
//                if(!StringUtils.isEmpty(errMsg)){
//                    ResultDto resultDto=(ResultDto) resultObject;
//                    RepaymentOrderDto repaymentOrderDto=ResultFactory.getResult(resultObject,status);
//                    if(null!=resultDto&&ResultFactory.ERR_REPAYMENT.equals(resultDto.code))
//                    {
//                        errMsg=null==repaymentOrderDto?errMsg:repaymentOrderDto.responseRemark;
//                    }
//                    ToastUtil.makeFailToastThr(mContext,errMsg);
//                }
//                else {
//                    listDatas.clear();
//                    RepaymentOrderDto repaymentOrderDto=ResultFactory.getResult(resultObject,status);
//                    if(null==repaymentOrderDto.transInfo||repaymentOrderDto.transInfo.size()<=0)
//                    {
//                        ToastUtil.makeOkToastThr(mContext,"没有查询到符合条件的订单");
//                    }
//                    else{
//                        listDatas.addAll(repaymentOrderDto.transInfo);
//                    }
//                    repaymentOrderAdapter.notifyDataSetChanged();
//                }
//                dismissLoading();
//            }
//        });
//    }
}
