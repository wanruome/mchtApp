package com.zjsj.mchtapp.config.impl;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.ruomm.base.ioc.task.StopToBcakGroundTask;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.IntentFactory;
import com.zjsj.mchtapp.config.http.ApiConfig;

public class StopToBcakGroundTaskImpl implements StopToBcakGroundTask {

    @Override
    public void doTaskStopToBcakGround(Context mContext) {
        String id = "app_local_channel_01";
        String name="应用本地通知";
        NotificationManager notificationManager = (NotificationManager)  mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
//            Toast.makeText(this, mChannel.toString(), Toast.LENGTH_SHORT).show();
//            Log.i(TAG, mChannel.toString());
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(mContext)
                    .setChannelId(id)
                    .setContentTitle("商户服务App")
                    .setContentText("商户服务App进入后台，请注意应用安全")
                    .setSmallIcon(R.mipmap.icon_app_logo).build();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext)
                    .setContentTitle("商户服务App")
                    .setContentText("商户服务App进入后台，请注意应用安全")
                    .setSmallIcon(R.mipmap.icon_app_logo)
                    .setOngoing(true);
//                    .setChannel(id);//无效
            notification = notificationBuilder.build();
        }
        notificationManager.notify(IntentFactory.Notification_GoToBackGround,notification);

    }
    @Override
    public void doTaskResumeFormBcakGround(Context mContext) {
        NotificationManager notificationManager = (NotificationManager)  mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(IntentFactory.Notification_GoToBackGround);
    }

}
