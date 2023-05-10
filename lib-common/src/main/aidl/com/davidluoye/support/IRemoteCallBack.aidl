package com.davidluoye.support;

import android.os.Bundle;

interface IRemoteCallBack {
    Bundle process(String key, in Bundle extra);
}