package com.ruomm.base.http.config.impl;

import com.ruomm.base.http.config.DataHttpListener;
import com.ruomm.base.http.config.TextHttpListener;

public abstract class DataHttpCallBack implements DataHttpListener {

    @Override
    public boolean httpCallBackFilter(byte[] resultBytes, int status) {
        return false;
    }
}
