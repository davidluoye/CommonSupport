package com.davidluoye.core;

import android.os.Bundle;

interface IRemoteCallBack {
    Bundle process(String key, in Bundle extra);
}