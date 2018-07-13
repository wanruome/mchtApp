package com.zjsj.mchtapp.module.lockscreen;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.resource.ui.AppMultiActivity;
import com.zjsj.mchtapp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import javax.xml.transform.Result;

public class LockScreenActivity extends AppMultiActivity {
    private boolean isGestureEnable=false;
    private boolean  isFingerEnable=false;
    private ArrayList<Integer> gestures;
    @InjectAll
    Views views=new Views();
    class  Views{
        @InjectView(id=R.id.text_change)
        TextView text_change;
        @InjectView(id=R.id.text_verify)
        TextView text_verify;
    }
    private int mode=0;
    private boolean isShowVerify=false;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setInitContentView(R.layout.lockscreen_lay);
        mymenutop.setLeftImage(0);
        mymenutop.setCenterText("解锁应用");
        isGestureEnable= getIntent().getBooleanExtra("isGestureEnable",false);
        isFingerEnable= getIntent().getBooleanExtra("isFingerEnable",false);
        gestures=getIntent().getIntegerArrayListExtra("gestures");
        if(isFingerEnable){
            showFragmentFingerprint();
        }
        else{
            showFragmentGesture();
        }
        if(isFingerEnable&&isGestureEnable){
            views.text_change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mode==1)
                    {
                        showFragmentGesture();

                    }
                    else if(mode==2)
                    {
                        showFragmentFingerprint();

                    }
                }
            });
        }
        else {
            views.text_change.setVisibility(View.GONE);
        }
        views.text_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShowVerify){
                    if(mode==1)
                    {
                        showFragmentFingerprint();
                    }
                    else {
                        showFragmentGesture();
                    }
                }
                else {
                    showFragmentVerifyPassWord();
                }
            }
        });


    }
    private void showFragmentGesture(){
        mode=2;
        isShowVerify=false;
        if(isFingerEnable&&isGestureEnable) {
            views.text_change.setVisibility(View.VISIBLE);
            views.text_change.setText("切换为指纹解锁验证");
        }
        else {
            views.text_change.setVisibility(View.GONE);
        }
        views.text_verify.setText("验证用户密码，清除指纹手势密码");
        GestureFragment mGestureFragment=new GestureFragment();
        Bundle bundle=new Bundle();
        bundle.putIntegerArrayList("gestures",gestures);
        mGestureFragment.setArguments(bundle);
        mFManager.beginTransaction().replace(R.id.container_fragment,mGestureFragment).commit();
    }
    private void showFragmentFingerprint(){
        mode=1;
        isShowVerify=false;
        if(isFingerEnable&&isGestureEnable) {
            views.text_change.setVisibility(View.VISIBLE);
            views.text_change.setText("切换为手势图案验证");
        }
        else {
            views.text_change.setVisibility(View.GONE);
        }
        views.text_verify.setText("验证用户密码，清除指纹手势密码");
        mFManager.beginTransaction().replace(R.id.container_fragment,new FingerPrintFragment()).commit();
    }
    private void showFragmentVerifyPassWord(){
        isShowVerify=true;
        views.text_change.setVisibility(View.GONE);
        views.text_verify.setText("返回，继续使用指纹或手势密码验证");
        mFManager.beginTransaction().replace(R.id.container_fragment,new VerifyPasswordFragment()).commit();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }

}
