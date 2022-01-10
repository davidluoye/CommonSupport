package com.davidluoye.support;

interface IToken {
    int getCallerPid();
    int getCallerUid();
    String getCallerPackage();

    String getValue(String key);
    String setValue(String key, String value);
}
