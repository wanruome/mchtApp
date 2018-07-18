package com.ruomm.base.tools.rootcheck;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

public class RootCheck {
    private  Handler handler=null;
    private RootCheckCallBack rootCheckCallBack=null;

    public void doRootCheck(RootCheckCallBack rootCheckCallBack)
    {
        if(null!=rootCheckCallBack)
        {
            this.rootCheckCallBack=rootCheckCallBack;
            handler=new Handler(Looper.getMainLooper());
        }

        new Thread(){
            @Override
            public void run() {
                super.run();
                doCallBackStart();
                boolean flag= RootCheckUtil.isDeviceRooted();
                SystemClock.sleep(3000);
                doCallBackResult(flag);
            }
        }.start();

    }
    public void doCallBackStart()
    {
        if(null==handler||null==rootCheckCallBack)
        {
            return ;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                rootCheckCallBack.checkStart();
            }
        });


    }
    public void doCallBackResult(final boolean isRoot)
    {
        if(null==handler||null==rootCheckCallBack)
        {
            return ;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                rootCheckCallBack.checkResult(isRoot);
            }
        });

    }
}
