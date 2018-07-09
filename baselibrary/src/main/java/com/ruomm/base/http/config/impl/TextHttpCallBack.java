package com.ruomm.base.http.config.impl;

import com.ruomm.base.http.config.DataHttpListener;
import com.ruomm.base.http.config.TextHttpListener;

public abstract class TextHttpCallBack implements TextHttpListener{


    @Override
    public boolean httpCallBackFilter(String resultString, int status) {
        return false;
    }
}
