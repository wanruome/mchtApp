package com.ruomm.base.tools.permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class PermissionHelper {
    private final Context mContext;
    private static final String PACKAGE = "package:";
    private List<PermissionBean> list=new ArrayList<>();
    private int REQUEST_CODE_START=100;
    private PermissionHelperCallBack permissionHelperCallBack;

    public PermissionHelper(Context mContext, PermissionHelperCallBack permissionHelperCallBack) {
        super();
        this.mContext = mContext;
        this.permissionHelperCallBack=permissionHelperCallBack;
    }

    public void setPermissions(String[] permissions) {
        list.clear();
        if(null!=permissions&&permissions.length>0) {
            for (int i = 0; i < permissions.length; i++) {
                PermissionBean tmp = new PermissionBean();
                tmp.permission = permissions[i];
                tmp.requestCode = REQUEST_CODE_START + i;
                tmp.granted = false;
                list.add(tmp);
            }
        }
    }
    public void setPermissions(String[][] permissions)
    {
        list.clear();
        if(null!=permissions&&permissions.length>0) {
            for (int i = 0; i < permissions.length; i++) {
                PermissionBean tmp = new PermissionBean();
                tmp.permission = permissions[i][0];
                tmp.permissionName=permissions[i][1];
                tmp.requestCode = REQUEST_CODE_START + i;
                tmp.granted = false;
                list.add(tmp);
            }
        }
    }
    public void checkPermissions(){
        if(list.size()>0) {
            PermissionBean permissionBean = list.get(0);
            checkPermission(permissionBean);
        }
        else{
            checkPermission(null);
        }
    }
    private void checkPermission(PermissionBean permissionBean)
    {
        if(null==permissionBean)
        {
            boolean allGranted=checkAllGranted();
//            if(!allGranted)
//            {
//                showMissingPermissionDialog();
//            }
            if(null!=permissionHelperCallBack)
            {
                permissionHelperCallBack.grantedCallBack(list,getDialogName(),allGranted);
            }
            return;
        }
        permissionBean.granted= ContextCompat.checkSelfPermission(mContext, permissionBean.permission) == PackageManager.PERMISSION_GRANTED;
        if(permissionBean.granted)
        {
            int nextIndex=permissionBean.requestCode-REQUEST_CODE_START+1;
            if(nextIndex>=list.size())
            {
                checkPermission(null);
            }
            else{
                checkPermission(list.get(nextIndex));
            }
        }
        else{
            requestPermission(permissionBean);
        }
    }
    private void requestPermission(PermissionBean permissionBean){
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, permissionBean.permission)) {
            // 如果用户以前拒绝过改权限申请，则给用户提示
           permissionBean.granted=false;
            int nextIndex=permissionBean.requestCode-REQUEST_CODE_START+1;
            if(nextIndex>=list.size())
            {
                checkPermission(null);
            }
            else{
                checkPermission(list.get(nextIndex));
            }
        }
        else {
            // 进行权限请求
            ActivityCompat.requestPermissions((Activity) mContext, new String[] { permissionBean.permission }, permissionBean.requestCode);
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        int index=requestCode-REQUEST_CODE_START;
        if(index>=list.size())
        {
            checkPermission(null);
        }
        PermissionBean permissionBean=list.get(index);
        if(grantResults[0]>=0)
        {
            permissionBean.granted=true;
        }
        else{
            permissionBean.granted=false;
        }
        int nextIndex=index+1;
        if(nextIndex>=list.size())
        {
            checkPermission(null);
        }
        else{
            checkPermission(list.get(nextIndex));
        }
    }
    private boolean checkAllGranted(){
        boolean granted=true;
        for(PermissionBean tmp:list)
        {
            if(!tmp.granted)
            {
                granted=false;
                break;
            }
        }
        return granted;
    }
    private String getDialogName()
    {
        String denyName=getDenyNames();
        if(TextUtils.isEmpty(denyName)) {
            return "当前应用缺少必要权限。\n\n请点击\"设置\"-\"权限\"-打开所需权限。\n\n最后点击两次后退按钮，即可返回。";
        }
        else{
            return "当前应用缺少 "+denyName+" 等权限。\n\n请点击\"设置\"-\"权限\"-打开所需权限。\n\n最后点击两次后退按钮，即可返回。";
        }
    }
//    // 显示缺失权限提示
//    private void showMissingPermissionDialog() {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        final AlertDialog alertDialog = builder.create();
//        String denyName=getDenyNames();
//        if(TextUtils.isEmpty(denyName)) {
//            builder.setMessage("当前应用缺少必要权限。\n\n请点击\"设置\"-\"权限\"-打开所需权限。\n\n最后点击两次后退按钮，即可返回。");
//        }
//        else{
//            builder.setMessage("当前应用缺少 "+denyName+" 等权限。\n\n请点击\"设置\"-\"权限\"-打开所需权限。\n\n最后点击两次后退按钮，即可返回。");
//        }
//        // 拒绝, 退出应用
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                alertDialog.dismiss();
//            }
//        });
//
//        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                startAppSettings();
//            }
//        });
//
//        builder.show();
//    }
    private String getDenyNames()
    {
        StringBuilder sb=new StringBuilder();
        for(PermissionBean tmp:list)
        {
            if(!tmp.granted&& !TextUtils.isEmpty(tmp.permissionName)){
                if(sb.length()<=0) {
                    sb.append(tmp.permissionName);
                }
                else{
                    sb.append("、").append(tmp.permissionName);
                }
            }
        }
        return sb.toString();
    }

    // 启动应用的设置
    public void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE + mContext.getPackageName()));
        mContext.startActivity(intent);
    }

}
