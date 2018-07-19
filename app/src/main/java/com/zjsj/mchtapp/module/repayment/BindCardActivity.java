package com.zjsj.mchtapp.module.repayment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ruomm.base.http.okhttp.TextOKHttp;
import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.tools.FileUtils;
import com.ruomm.base.tools.IDCardUtils;
import com.ruomm.base.tools.StringUtils;
import com.ruomm.base.tools.ToastUtil;
import com.ruomm.base.tools.ViewUtil;
import com.ruomm.base.tools.regextool.RegexCallBack;
import com.ruomm.base.tools.regextool.RegexText;
import com.ruomm.base.tools.regextool.RegexUtil;
import com.ruomm.base.view.dialog.BaseDialogClickListener;
import com.ruomm.base.view.menutopview.MenuTopListener;
import com.ruomm.baseconfig.debug.MLog;
import com.ruomm.resource.ui.AppMultiActivity;
import com.ruomm.resource.ui.dal.WebUrlInfo;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.IntentFactory;
import com.zjsj.mchtapp.config.http.ApiConfig;
import com.zjsj.mchtapp.config.impl.TextHttpCallBack;
import com.zjsj.mchtapp.dal.response.RepaymentAreaDto;
import com.zjsj.mchtapp.dal.response.RepaymentBindCardDto;
import com.zjsj.mchtapp.dal.response.base.ResultDto;
import com.zjsj.mchtapp.dal.response.base.ResultFactory;
import com.zjsj.mchtapp.view.AreaWheelDialog;

import java.io.File;
import java.util.Map;

public class BindCardActivity extends AppMultiActivity {
    @InjectAll
    Views views=new Views();
    class Views{
        @InjectView(id=R.id.text_bankcard)
        TextView text_bankcard;
        @InjectView(id=R.id.edt_bankcard)
        EditText edt_bankcard;
        @InjectView(id=R.id.text_mobile)
        TextView text_mobile;
        @InjectView(id=R.id.edt_mobile)
        EditText edt_mobile;
        @InjectView(id=R.id.text_idcardno)
        TextView text_idcardno;
        @InjectView(id=R.id.edt_idcardno)
        EditText edt_idcardno;
        @InjectView(id=R.id.text_idcardname)
        TextView text_idcardname;
        @InjectView(id=R.id.edt_idcardname)
        EditText edt_idcardname;
        @InjectView(id=R.id.text_area)
        TextView text_area;
        @InjectView(id=R.id.text_select_area)
        TextView text_select_area;
        @InjectView(id=R.id.btn_submit)
        Button btn_submit;
    }
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setInitContentView(R.layout.repayment_bindcard_lay);
        setMenuTop();
        setViewData();
        getAreaList();


    }
    private void setMenuTop(){

        mymenutop.setCenterText("银行卡绑定");
        mymenutop.setMenuClickListener(new MenuTopListener() {
            @Override
            public void onMenuTopClick(View v, int vID) {
                if(vID==R.id.menutop_left)
                {
                    finish();
                }
            }
        });
    }
    private void setViewData()
    {
        ViewUtil.setTextParamNotEmpty(views.text_bankcard,mContext.getResources().getColor(R.color.text_red));
        ViewUtil.setTextParamNotEmpty(views.text_mobile,mContext.getResources().getColor(R.color.text_red));
        ViewUtil.setTextParamNotEmpty(views.text_idcardname,mContext.getResources().getColor(R.color.text_red));
        ViewUtil.setTextParamNotEmpty(views.text_idcardno,mContext.getResources().getColor(R.color.text_red));
        ViewUtil.setTextParamNotEmpty(views.text_area,mContext.getResources().getColor(R.color.text_red));
        views.text_select_area.setOnClickListener(myOnClickListener);
        views.btn_submit.setOnClickListener(myOnClickListener);
    }
    private View.OnClickListener myOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vID=v.getId();
            if(vID==R.id.text_select_area) {
                String wheelProvince = null;
                String wheelCity = null;
                String areaText = views.text_select_area.getText().toString();
                if (null != areaText) {
                    String[] areas = areaText.split("-");
                    if (null != areas && areas.length == 2) {
                        wheelProvince = areas[0];
                        wheelCity = areas[1];
                    }
                }
                new AreaWheelDialog(mContext, repaymentAreaDto.cityData, wheelProvince, wheelCity, new BaseDialogClickListener() {
                    @Override
                    public void onDialogItemClick(View v, Object tag) {
                        if (v.getId() == R.id.wheelbtn_over) {
                            views.text_select_area.setText((String) v.getTag());
                        }

                    }
                }).show();
            }
            else if(vID==R.id.btn_submit)
            {
                doHttpBindCard();
            }
        }
    };
    private void doHttpBindCard() {
        boolean flag = RegexText.with(new RegexCallBack() {
            @Override
            public void errorRegex(TextView v, String value, String errorInfo) {
                ToastUtil.makeFailToastThr(mContext, errorInfo);
            }
        }).doRegexSize(views.edt_bankcard, 10, 20, "请填写正确的银行卡号", "必须填写银行卡号")
                .doRegex(views.edt_mobile, RegexUtil.MOBILE_NUM, "请填写正确的手机号", "必须填写手机号").builder();
        if (!flag){
            return;
        }
        String cardNo=views.edt_bankcard.getText().toString();
        String mobile=views.edt_mobile.getText().toString();
        String idCardName=views.edt_idcardname.getText().toString();
        String idCardNo=views.edt_idcardno.getText().toString();
        String area=views.text_select_area.getText().toString();
        if(!StringUtils.isEmpty(idCardNo)&&!IDCardUtils.isIDCardValidate(idCardNo))
        {
            ToastUtil.makeFailToastThr(mContext, "开户证件号必须为身份证号");
            return ;
        }
        if(null==area||!area.contains("-")){
            ToastUtil.makeFailToastThr(mContext, "请选择区域");
            return ;
        }
        showLoading();
        Map<String,String> map=ApiConfig.createRequestMap(true);
//        map.put("accountNo",cardNo);
        map.put("mobileNo",mobile);
        map.put("name",idCardName);
        map.put("idcardNo",idCardNo);
        map.put("area",area);
        map.put("dataEncrypt",ApiConfig.getPassWordEncrypt(false));
        map.put("accountNo",ApiConfig.getPassWord(cardNo,ApiConfig.getPassWordEncrypt(false)));
        ApiConfig.signRequestMap(map);
        new TextOKHttp().setUrl(ApiConfig.BASE_URL+"/app/repayment/doBindCard").setRequestBodyText(map).doHttp(RepaymentBindCardDto.class, new TextHttpCallBack() {
            @Override
            public void httpCallBack(Object resultObject, String resultString, int status) {
                String errMsg= ResultFactory.getErrorTip(resultObject,status);
                if(!StringUtils.isEmpty(errMsg)){
                    ResultDto resultDto=(ResultDto) resultObject;
                    RepaymentBindCardDto resultBindCardDto=ResultFactory.getResult(resultObject,status);
                    if(null!=resultDto&&ResultFactory.ERR_REPAYMENT.equals(resultDto.code))
                    {
                        errMsg=null==resultBindCardDto?errMsg:resultBindCardDto.responseRemark;
                    }
                    ToastUtil.makeFailToastThr(mContext,errMsg);
                }
                else{
                    RepaymentBindCardDto resultBindCardDto=ResultFactory.getResult(resultObject,status);
                    MLog.i(resultBindCardDto);
                    String data=null;
                    if(ApiConfig.BASE_URL.contains("192.168.")){
                        data=resultBindCardDto.transInfo.replace("183.129.219.202","192.168.100.85");
                    }
                    else {
                        data=resultBindCardDto.transInfo;
                    }
                    WebUrlInfo webUrlInfo=new WebUrlInfo();
                    webUrlInfo.setWebData(data);
                    Intent intent=IntentFactory.getBindCardWebInfoActivity();
                    BaseUtil.serializablePut(intent,webUrlInfo);
                    startActivity(intent);
                    finish();

                }
                dismissLoading();

            }
        });

    }
    RepaymentAreaDto repaymentAreaDto=null;
    private void getAreaList(){
        repaymentAreaDto=readCityData();
        showLoading();
        Map<String,String> map= ApiConfig.createRequestMap(true);
        if(null!=repaymentAreaDto){
            map.put("version",repaymentAreaDto.version);
        }

        map.put("format","dict");
        ApiConfig.signRequestMap(map);
        new TextOKHttp().setUrl(ApiConfig.BASE_URL+"app/repayment/doQueryAllCitys").setRequestBodyText(map).doHttp(RepaymentAreaDto.class, new TextHttpCallBack() {
            @Override
            public void httpCallBack(Object resultObject, String resultString, int status) {
                String errMsg= ResultFactory.getErrorTip(resultObject,status);
                if(!StringUtils.isEmpty(errMsg))
                {
                    ToastUtil.makeFailToastThr(mContext,errMsg);
                    finish();
                }
                else{
                    RepaymentAreaDto resultAreaDto=ResultFactory.getResult(resultObject,status);
                    if(null==resultAreaDto){
                        ToastUtil.makeFailToastThr(mContext,"获取区域信息失败");
                        finish();
                    }
                    else{
                        if(null!=repaymentAreaDto&&null!=repaymentAreaDto.version&&repaymentAreaDto.version.equals(resultAreaDto.version))
                        {
                            ToastUtil.makeFailToastThr(mContext,"获取区域信息成功");
                        }
                        else{
                            writeCityData(resultAreaDto);
                            repaymentAreaDto=resultAreaDto;
                            ToastUtil.makeFailToastThr(mContext,"获取区域信息成功");
                        }
                    }
                }
                dismissLoading();

            }
        });
    }
    private RepaymentAreaDto readCityData(){
        RepaymentAreaDto repaymentAreaDto=null;
        try{
            String cityDataPath=FileUtils.getPathContext(mContext,"files"+ File.separator+"repaymentCityData");
//        File file=FileUtils.createFileInContext(mContext,"files"+ File.separator+"repaymentCityData");

            String cityDataJson=FileUtils.readFile(cityDataPath,"UTF-8");
            repaymentAreaDto= JSON.parseObject(cityDataJson,RepaymentAreaDto.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            repaymentAreaDto=null;
        }

        return repaymentAreaDto;
    }
    private void writeCityData(RepaymentAreaDto resultAreaDto){
        if(null==resultAreaDto)
        {
            return;
        }
        try{
            String cityDataPath=FileUtils.getPathContext(mContext,"files"+ File.separator+"repaymentCityData");
            FileUtils.writeFile(cityDataPath,JSON.toJSONString(resultAreaDto),false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
