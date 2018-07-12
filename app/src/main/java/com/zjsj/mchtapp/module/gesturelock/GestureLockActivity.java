package com.zjsj.mchtapp.module.gesturelock;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.extend.Thread_CanStop;
import com.ruomm.base.tools.DisplayUtil;
import com.ruomm.base.tools.ListUtils;
import com.ruomm.base.tools.ToastUtil;
import com.ruomm.base.view.gesturelock.GestureLock;
import com.ruomm.base.view.gesturelock.GestureLockView;
import com.ruomm.base.view.menutopview.MenuTopListener;
import com.ruomm.resource.ui.AppMultiActivity;
import com.ruomm.resource.ui.AppSimpleActivity;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.LoginUserFactory;

import java.util.List;

public class GestureLockActivity extends AppMultiActivity{
    @InjectView(id=R.id.gesture_lock)
    private GestureLock gestureView;
    @InjectView(id=R.id.text_tip)
    private TextView text_tip;
    private int gestureViewWidth;
    private int gestureBlockGap;
    private List<Integer> listGestrues=null;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setInitContentView(R.layout.gesturelock_act);
        setMenuTop();
        int gestureViewWidth= DisplayUtil.getDispalyAbsWidth(mContext)*8/10;
        gestureBlockGap=gestureViewWidth/6;
        ViewGroup.LayoutParams layoutParams=gestureView.getLayoutParams();
        layoutParams.width=gestureViewWidth;
        layoutParams.height=gestureViewWidth;
        gestureView.requestLayout();
        setGestureAdapterEdit();
    }
    private void setMenuTop(){
        mymenutop.setCenterText("设置手势密码");
        mymenutop.setMenuClickListener(new MenuTopListener() {
            @Override
            public void onMenuTopClick(View v, int vID) {
                if(vID==R.id.menutop_left)
                {
                    finish();
                }
            }
        });
    }
    private void setGestureAdapterEdit(){
        text_tip.setText("第一步：绘制手势密码");
        gestureView.setAdapter(new GestureLock.GestureLockAdapter() {

            @Override
            public int getDepth() {
                return 3;
            }

            @Override
            public List<Integer> getCorrectGestures() {
                return null;
//                return new int[]{1, 2, 3, 4, 5, 6, 7, 8};
            }

            @Override
            public int getUnmatchedBoundary() {
                return 5;
            }

            @Override
            public int getBlockGapSize(){
                return gestureBlockGap;
            }

            @Override
            public GestureLockView getGestureLockViewInstance(Context context, int position) {
                return new MyStyleLockView(context);
            }
        });
        gestureView.setMode(GestureLock.MODE_EDIT);
        gestureView.setOnGestureEventListener(new GestureLock.OnGestureEventListener(){

            @Override
            public void onGestureResult(boolean matched, List<Integer> gesturesResultLst) {

                    if(ListUtils.getSize(gesturesResultLst)<6)
                    {
                        ToastUtil.makeFailToastThr(mContext,"手势密码至少需要6个点,请重新绘制");
                        gestureView.clear();
                    }
                    else{
                        listGestrues=gesturesResultLst;
                        setGestureAdapterVerify();
                        gestureView.clear();
                    }

            }

            @Override
            public void onUnmatchedExceedBoundary() {
//                Toast.makeText(GestureLockActivity.this, "输入5次错误!30秒后才能输入", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBlockSelected(int position) {
//                Log.d("position", position + "");
            }

        });
    }
    private void setGestureAdapterVerify(){
        text_tip.setText("第二步：确认绘制手势密码");
        gestureView.setAdapter(new GestureLock.GestureLockAdapter() {

            @Override
            public int getDepth() {
                return 3;
            }

            @Override
            public List<Integer> getCorrectGestures() {
               return listGestrues;
            }

            @Override
            public int getUnmatchedBoundary() {
                return 1;
            }

            @Override
            public int getBlockGapSize(){
                return gestureBlockGap;
            }

            @Override
            public GestureLockView getGestureLockViewInstance(Context context, int position) {
                return new MyStyleLockView(context);
            }
        });
        gestureView.setMode(GestureLock.MODE_NORMAL);
        gestureView.setOnGestureEventListener(new GestureLock.OnGestureEventListener(){

            @Override
            public void onGestureResult(boolean matched, List<Integer> gesturesResultLst) {

               if(matched)
               {
                   gestureView.clear();
                   gestureView.setTouchable(false);
                   boolean tmp=LoginUserFactory.saveUserGesturesInfo(listGestrues);
                   if(tmp){
                    setResult(Activity.RESULT_OK);
                   }
                   finish();
               }
               else {
                   ToastUtil.makeFailToastThr(mContext, "两次手势密码不一致，请重新绘制");
                   gestureTouchDisable();
               }

            }

            @Override
            public void onUnmatchedExceedBoundary() {
//                Toast.makeText(GestureLockActivity.this, "输入5次错误!30秒后才能输入", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBlockSelected(int position) {
//                Log.d("position", position + "");
            }

        });
    }
    private void gestureTouchDisable()
    {
        gestureView.setTouchable(false);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                GestureLockActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gestureView.setTouchable(true);
                        gestureView.clear();
                        setGestureAdapterEdit();

                    }
                });
            }
        }, 3000);
    }

}
