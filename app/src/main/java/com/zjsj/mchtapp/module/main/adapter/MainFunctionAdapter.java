package com.zjsj.mchtapp.module.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruomm.base.ioc.adapter.BaseAdapter_T;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.tools.DisplayUtil;
import com.squareup.picasso.Picasso;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.module.main.dal.MainFunctionItem;

import java.util.ArrayList;
import java.util.List;

public class MainFunctionAdapter   extends BaseAdapter_T<MainFunctionItem> {
    public static List<MainFunctionItem> getFunctionItemList(){
        List<MainFunctionItem> lst=new ArrayList<>();
        MainFunctionItem item1=new MainFunctionItem();
        item1.name="二维码支付";
        item1.imageRes=R.mipmap.home_fun_qrcodepament;
        item1.actionName="repayment.RepaymentQrCodeActivity";
        lst.add(item1);
        MainFunctionItem item2=new MainFunctionItem();
        item2.name="交易记录";
        item2.imageRes=R.mipmap.home_fun_tradehistory;
        item2.actionName="repayment.RepaymentOrderActivity";
        lst.add(item2);
        MainFunctionItem item3=new MainFunctionItem();
        item3.name="银行卡管理";
        item3.imageRes=R.mipmap.home_fun_bankcard;
//        item3.actionName="repayment.BindCardActivity";
        item3.actionName="repayment.BankCardListActivity";
        lst.add(item3);
        return lst;
    }
    private int imageSize;
    public MainFunctionAdapter(Context mContext) {
        super(mContext,getFunctionItemList());
        imageSize= DisplayUtil.getDispalyAbsWidth(mContext)/6;

    }
    public MainFunctionAdapter(Context mContext, List<MainFunctionItem> listDatas) {
        super(mContext,listDatas);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MainFunctionItem item=listDatas.get(position);
        HolerView holerView=null;
        if(null==convertView){
            convertView=mInflater.inflate(R.layout.main_function_layout,null);
            holerView=new HolerView();
            BaseUtil.initInjectAll(holerView,HolerView.class,convertView);
            convertView.setTag(holerView);
        }
        else{
            holerView=(HolerView)convertView.getTag();
        }
        holerView.lv_text_name.setText(item.name);
//        holerView.lv_img_name.setImageResource(item.imageRes);
        Picasso.with(mContext).load(item.imageRes).resize(imageSize,imageSize).into( holerView.lv_img_name);
        return convertView;
    }

    class HolerView {
        @InjectView(id=R.id.lv_text_name)
        TextView lv_text_name;
        @InjectView(id=R.id.lv_img_name)
        ImageView lv_img_name;
    }
}
