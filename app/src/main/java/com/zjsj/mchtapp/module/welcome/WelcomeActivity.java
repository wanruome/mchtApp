package com.zjsj.mchtapp.module.welcome;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.ruomm.base.tools.permission.PermissionBean;
import com.ruomm.base.tools.permission.PermissionHelper;
import com.ruomm.base.tools.permission.PermissionHelperCallBack;
import com.ruomm.baseconfig.debug.MLog;
import com.ruomm.resource.ui.AppMultiActivity;
import com.ruomm.resource.ui.AppSimpleActivity;
import com.zjsj.mchtapp.R;

import java.util.List;
import java.util.jar.Manifest;

public class WelcomeActivity extends AppSimpleActivity {
//    @InjectView(id=R.id.view_img)
//    ImageView view_img;
    PermissionHelper permissionHelper=null;
    boolean isGrant=false;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setInitContentView(R.layout.welcome_act);
//        showProgressDialog(null);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isGrant) {
            if (null == permissionHelper) {
                permissionHelper = new PermissionHelper(mContext, permissionHelperCallBack);
                permissionHelper.setPermissions(new String[][]{
                        {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储卡"},
                        {android.Manifest.permission.READ_PHONE_STATE, "手机状态"},
                        {android.Manifest.permission.CAMERA, "存储卡"},
                        {android.Manifest.permission.CALL_PHONE, "相机"}
                });
                permissionHelper.checkPermissions();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    PermissionHelperCallBack permissionHelperCallBack=new PermissionHelperCallBack() {
        @Override
        public void grantedCallBack(List<PermissionBean> listPermissionBeans, boolean isAllGranted) {
            MLog.i("权限申请完毕");
            isGrant=isAllGranted;
            if(isGrant)
            {
                gotoMainActivity();
            }
        }
    };
    private void gotoMainActivity(){

    }
}
