package com.ruomm.base.tools.permission;

import java.security.Permission;
import java.util.List;

public interface PermissionHelperCallBack {
    public void grantedCallBack(List<PermissionBean> listPermissionBeans,boolean isAllGranted);
}
