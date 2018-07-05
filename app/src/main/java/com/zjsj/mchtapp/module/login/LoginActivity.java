package com.zjsj.mchtapp.module.login;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.baseconfig.debug.MLog;
import com.ruomm.resource.ui.AppMultiActivity;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.util.keyboard.KeyboardUtil;

public class LoginActivity extends AppMultiActivity {
    @InjectAll
    Views views=new Views();
    class Views{
        @InjectView(id=R.id.edt_name)
        EditText edt_name;
        @InjectView(id=R.id.edt_pwd)
        EditText edt_pwd;
        @InjectView(id=R.id.ly_verifyCode)
        LinearLayout ly_verifyCode;
        @InjectView(id=R.id.edt_verifyCode)
        EditText edt_verifyCode;
        @InjectView(id=R.id.btn_verifyCode)
        Button btn_verifyCode;
        @InjectView(id=R.id.text_findpwd)
        TextView text_findpwd;
        @InjectView(id=R.id.btn_submit)
        Button btn_submit;
        @InjectView(id=R.id.ly_fast_register)
        LinearLayout ly_fast_register;

    }
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        hideMenuTopView();
        setInitContentView(R.layout.login_act);
        views.edt_pwd.setInputType(InputType.TYPE_NULL);
        views.edt_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtil.shared(LoginActivity.this,views.edt_pwd).showKeyboard();
            }
        });
//       ViewGroup viewGroup= (ViewGroup) findViewById(android.R.id.content);
//       int i=viewGroup.getChildCount();
//        MLog.i(i);
//        ViewGroup rootView = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
//
//        View view= LayoutInflater.from(this).inflate(R.layout.main_menu_lay,null);
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, RelativeLayout.TRUE);
//        viewGroup.addView(view,layoutParams);
//        views.btn_submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }
}
