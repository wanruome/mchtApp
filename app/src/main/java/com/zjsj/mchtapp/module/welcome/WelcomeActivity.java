package com.zjsj.mchtapp.module.welcome;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.tools.ToastUtil;
import com.ruomm.base.tools.permission.PermissionBean;
import com.ruomm.base.tools.permission.PermissionHelper;
import com.ruomm.base.tools.permission.PermissionHelperCallBack;
import com.ruomm.baseconfig.debug.MToast;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.ui.BaseAppActivity;
import com.zjsj.mchtapp.module.main.MainActivity;

import java.util.List;

public class WelcomeActivity extends BaseAppActivity
{
    @InjectView(id=R.id.view_img)
    private ImageView view_img;
    private PermissionHelper permissionHelper=null;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setInitContentView(R.layout.welcome_act);
        hideMenuTopView();
        permissionHelper=new PermissionHelper(WelcomeActivity.this,permissionHelperCallBack);
        view_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                permissionHelper.setPermissions(new String[]{
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.CALL_PHONE,
//                        Manifest.permission.CAMERA,
//                        Manifest.permission.READ_CONTACTS});
//                permissionHelper.setPermissions(new String[][]{
//                        {Manifest.permission.READ_EXTERNAL_STORAGE,"存储卡"},
//                        {Manifest.permission.CALL_PHONE,"打电话"},
//                        {Manifest.permission.CAMERA,"相机"},
//                        {Manifest.permission.READ_CONTACTS,"短信"}});
//                permissionHelper.checkPermissions();
            }
        });
    }
    private PermissionHelperCallBack permissionHelperCallBack=new PermissionHelperCallBack() {
        @Override
        public void grantedCallBack(List<PermissionBean> listPermissionBeans, boolean isAllGranted) {

        }
    };
    public void myOnClick(View view)
    {

    }

    @Override
    protected void onResume() {
        super.onResume();
        permissionHelper.setPermissions(new String[][]{
                {Manifest.permission.READ_EXTERNAL_STORAGE,"存储卡"},
                {Manifest.permission.CALL_PHONE,"打电话"},
                {Manifest.permission.CAMERA,"相机"},
                {Manifest.permission.READ_CONTACTS,"短信"}});
        permissionHelper.checkPermissions();
//        new PermissionHelper(WelcomeActivity.this).permissionsCheck(Manifest.permission.READ_EXTERNAL_STORAGE,0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}
