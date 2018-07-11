package com.zjsj.mchtapp.config.impl;

import com.ruomm.base.http.config.DataHttpListener;

public abstract class DataHttpCallBack implements DataHttpListener {

    @Override
    public boolean httpCallBackFilter(byte[] resultBytes, int status) {
        return false;
    }
}
