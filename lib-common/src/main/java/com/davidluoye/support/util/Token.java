package com.davidluoye.support.util;

import android.os.Process;

import com.davidluoye.support.IToken;
import com.davidluoye.support.app.AppGlobals;

import java.util.Objects;

public class Token<T> extends IToken.Stub {
    public final T identity;
    public Token(T identity) {
        this.identity = identity;
    }

    @Override
    public final int fromPid() {
        return Process.myPid();
    }

    @Override
    public final int fromUid() {
        return Process.myUid();
    }

    @Override
    public final String fromPackage() {
        return AppGlobals.getPackageName();
    }

    @Override
    public String string() {
        return toString();
    }

    @Override
    public String getValue(String key) {
        return null;
    }

    @Override
    public String setValue(String key, String value) {
        return null;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Token<?> token = (Token<?>) o;
        return identity == token.identity;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(identity);
    }

    @Override
    public final String toString() {
        String id = Integer.toHexString(System.identityHashCode(identity));
        return String.format("Token{%s %s}", id, identity);
    }
}
