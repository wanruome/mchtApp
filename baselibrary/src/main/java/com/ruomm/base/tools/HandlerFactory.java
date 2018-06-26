package com.ruomm.base.tools;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by Niu on 2018/1/11.
 */

public class HandlerFactory {
    // 异步请求回调所需的handler
    private Handler UIHandler = null;
    // 异步请求回调到JsonOkHttp线程的Looper
    private Looper myLooper = null;
    /**
     * 注册Looper
     */
    public void initLopper() {
        if (null == Looper.myLooper()) {
                Looper.prepare();
                myLooper = Looper.myLooper();
                UIHandler = new Handler(myLooper);
                new Thread() {
                    @Override
                    public void run() {
                        Looper.loop();
                    };
                }.start();
        }
        else {
                UIHandler = new Handler(Looper.myLooper());
        }
    }
    public Handler getUIHandler(){
        return this.UIHandler;
    }
    /**
     * 退出Lopper线程
     */
    public void quitLooper() {
        if (null != UIHandler) {
            UIHandler = null;
        }
        if (null != myLooper) {
            myLooper.quit();
            myLooper = null;
        }
    }
}
