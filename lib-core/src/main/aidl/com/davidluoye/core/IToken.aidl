package com.davidluoye.core;

interface IToken {
    int fromPid();
    int fromUid();
    String fromPackage();
    String string();

    String getValue(String key);
    String setValue(String key, String value);
}
