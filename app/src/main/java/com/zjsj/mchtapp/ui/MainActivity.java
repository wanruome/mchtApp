package com.zjsj.mchtapp.ui;

import android.os.Bundle;

import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.ui.BaseAppActivity;

public class MainActivity extends BaseAppActivity{

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setInitContentView(R.layout.welcome_act);
    }
}
