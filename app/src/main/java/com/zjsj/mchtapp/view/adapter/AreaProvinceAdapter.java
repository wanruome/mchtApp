package com.zjsj.mchtapp.view.adapter;

import android.text.TextUtils;

import com.ruomm.base.view.wheel.adaper.WheelAdapter;
import com.ruomm.base.view.wheel.wheelstring.WheelStringAdapter;
import com.zjsj.mchtapp.dal.response.RepayMentArea;

import java.util.List;

public class AreaProvinceAdapter implements WheelStringAdapter {
    List<RepayMentArea> cityData;

    public AreaProvinceAdapter( List<RepayMentArea> cityData) {
        this.cityData = cityData;
    }

    @Override
    public int getCount() {
        return cityData.size();
    }

    @Override
    public String getItem(int index) {
        return cityData.get(index).province;
    }
    public int getIndexByName(String tag)
    {
        if(TextUtils.isEmpty(tag))
        {
            return 0;
        }
        int index=0;
        for(int i=0;i<cityData.size();i++)
        {
            if(tag.equals(cityData.get(i).province))
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
