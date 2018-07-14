package com.zjsj.mchtapp.view.adapter;

import android.text.TextUtils;

import com.ruomm.base.view.wheel.adaper.WheelAdapter;
import com.ruomm.base.view.wheel.wheelstring.WheelStringAdapter;
import com.zjsj.mchtapp.dal.response.RepayMentArea;

import java.util.List;

public class AreaCityAdapter implements WheelStringAdapter {
    RepayMentArea repayMentArea;

    public AreaCityAdapter(RepayMentArea repayMentArea) {
        this.repayMentArea = repayMentArea;
    }

    @Override
    public int getCount() {
        return repayMentArea.city.size();
    }

    @Override
    public String getItem(int index) {
        return repayMentArea.city.get(index);
    }
    public int getIndexByName(String tag)
    {
        if(TextUtils.isEmpty(tag))
        {
            return 0;
        }
        int index=0;
        for(int i=0;i<repayMentArea.city.size();i++)
        {
            if(tag.equals(repayMentArea.city.get(i)))
            {
                index=i;
                break;
            }
        }
        return index;
    }

//    @Override
//    public String getCalWidhtString() {
//        return "中华人民共和国中国人民解放军";
//    }
}
