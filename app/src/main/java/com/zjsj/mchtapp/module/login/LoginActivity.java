package com.zjsj.mchtapp.module.login;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.tools.EncryptUtils;
import com.ruomm.base.tools.RSAUtils;
import com.ruomm.base.tools.StringUtils;
import com.ruomm.baseconfig.debug.MLog;
import com.ruomm.resource.ui.AppMultiActivity;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.util.keyboard.KeyboardSafeInterface;
import com.zjsj.mchtapp.util.keyboard.KeyboardUtil;

import java.lang.reflect.Method;

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
    private KeyboardUtil keyboardUtil;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        hideMenuTopView();
        setInitContentView(R.layout.login_act);
        keyboardUtil=new KeyboardUtil(this,views.edt_pwd).setSymbolEnable(false).setSafeInterFace(new KeyboardSafeInterface() {
            @Override
            public String getShowStr(String str) {
                return "*";
            }

            @Override
            public String getEncryptStr(String str) {
                if(StringUtils.isEmpty(str))
                    return null;
                StringBuilder sb = new StringBuilder();
                for(int i=0;i<str.length();i++) {

                    sb.append(str.charAt(i));
                    sb.append("你好啊");

                }
                return sb.toString();
            }

            @Override
            public boolean isEncryptByChar() {
                return false;
            }

            @Override
            public String getDecryptStr(String encryptStr) {
                if(null==encryptStr)
                {
                    return encryptStr;
                }
                else{
                    return encryptStr.replace("你好啊","");
                }
            }
        }).bulider(KeyboardUtil.KEYMODE.LETTER_LOWER);
    }
}
