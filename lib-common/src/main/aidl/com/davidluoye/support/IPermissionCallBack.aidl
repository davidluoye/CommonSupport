package com.davidluoye.support;

import java.util.List;

interface IPermissionCallBack {
    oneway void onCallBack(in List<String> grantedPermission);
}
