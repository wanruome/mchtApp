package com.zjsj.mchtapp.module.gesturelock;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.view.gesturelock.GestureLock;
import com.ruomm.base.view.gesturelock.GestureLockView;
import com.ruomm.resource.ui.AppMultiActivity;
import com.ruomm.resource.ui.AppSimpleActivity;
import com.zjsj.mchtapp.R;

public class GestureLockActivity extends AppMultiActivity{
    @InjectView(id=R.id.gesture_lock)
    private GestureLock gestureView;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setInitContentView(R.layout.gesturelock_act);
        gestureView = (GestureLock) findViewById(R.id.gesture_lock);
        gestureView.setAdapter(new GestureLock.GestureLockAdapter() {
            @Override
            public int getDepth() {
                return 3;
            }

            @Override
            public int[] getCorrectGestures() {
                return new int[]{0, 3, 6, 7, 8, 5, 2, 1, 4};
            }

            @Override
            public int getUnmatchedBoundary() {
                return 300;
            }

            @Override
            public int getBlockGapSize(){
                return 300;
            }

            @Override
            public GestureLockView getGestureLockViewInstance(Context context, int position) {
                return new MyStyleLockView(context);
//                if(position % 2 == 0){
//                    return new MyStyleLockView(context);
//                }else{
//                    return new NexusStyleLockView(context);
//                }
            }
        });

        gestureView.setOnGestureEventListener(new GestureLock.OnGestureEventListener(){

            @Override
            public void onGestureEvent(boolean matched) {
                Toast.makeText(GestureLockActivity.this, "Match:" + matched, Toast.LENGTH_SHORT).show();
                gestureView.clear();
            }

            @Override
            public void onUnmatchedExceedBoundary() {
                Toast.makeText(GestureLockActivity.this, "输入5次错误!30秒后才能输入", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBlockSelected(int position) {
                Log.d("position", position + "");
            }

        });
    }
}
