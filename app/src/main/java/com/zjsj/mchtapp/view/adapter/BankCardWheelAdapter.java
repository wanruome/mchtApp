package com.zjsj.mchtapp.view.adapter;

import com.ruomm.base.view.wheel.wheelstring.WheelStringAdapter;
import com.zjsj.mchtapp.dal.response.RepaymentBankCard;

import java.util.List;

public class BankCardWheelAdapter implements WheelStringAdapter {
    private List<RepaymentBankCard> listDatas;

    public BankCardWheelAdapter(List<RepaymentBankCard> listDatas) {
        this.listDatas = listDatas;
    }

    @Override
    public int getCount() {
        return null==listDatas?0:listDatas.size();
    }

    @Override
    public String getItem(int index) {
       return listDatas.get(index).accountNo;
    }
}
