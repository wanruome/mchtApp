package com.zjsj.mchtapp.module.lockscreen;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.tools.DisplayUtil;
import com.ruomm.base.tools.ToastUtil;
import com.ruomm.base.view.gesturelock.GestureLock;
import com.ruomm.base.view.gesturelock.GestureLockView;
import com.ruomm.resource.ui.AppFragment;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.LoginUserFactory;
import com.zjsj.mchtapp.module.gesturelock.GestureLockActivity;
import com.zjsj.mchtapp.module.gesturelock.MyStyleLockView;

import java.util.List;

public class GestureFragment extends AppFragment {

    @InjectView(id=R.id.gesture_lock)
    private GestureLock gestureView;
    @InjectView(id=R.id.text_tip)
    private TextView text_tip;
    private int gestureViewWidth;
    private int gestureBlockGap;
    private List<Integer> listGestrues=null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView= LayoutInflater.from(mContext).inflate(R.layout.lockscreen_gesturelock_lay,null);
        listGestrues=mBundle.getIntegerArrayList("gestures");
        BaseUtil.initInjectAll(this,mView);
        int gestureViewWidth= DisplayUtil.getDispalyAbsWidth(mContext)*8/10;
        gestureBlockGap=gestureViewWidth/6;
        ViewGroup.LayoutParams layoutParams=gestureView.getLayoutParams();
        layoutParams.width=gestureViewWidth;
        layoutParams.height=gestureViewWidth;
        gestureView.requestLayout();
        setGestureAdapterVerify();
        return mView;
    }
    private void setGestureAdapterVerify(){
        text_tip.setText("验证手势密码");
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
        gestureView.setMode(GestureLock.MODE_NORMAL);
        gestureView.setOnGestureEventListener(new GestureLock.OnGestureEventListener(){

            @Override
            public void onGestureResult(boolean matched, List<Integer> gesturesResultLst) {

                if(matched)
                {
                    gestureView.clear();
                    gestureView.setTouchable(false);
                    getActivity().finish();
                }
                else {
                    ToastUtil.makeFailToastThr(mContext, "手势密码错误");
//                    gestureTouchDisable();
                }

            }

            @Override
            public void onUnmatchedExceedBoundary() {
                ToastUtil.makeFailToastThr(mContext, "手势密码错误");
            }

            @Override
            public void onBlockSelected(int position) {
//                Log.d("position", position + "");
            }

        });
    }
//    private void gestureTouchDisable()
//    {
//        gestureView.setTouchable(false);
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        gestureView.setTouchable(true);
//                        gestureView.clear();
//
//                    }
//                });
//            }
//        }, 3000);
//    }

}
