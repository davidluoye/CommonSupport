package com.davidluoye.support.util;

import com.davidluoye.support.IToken;

import java.util.Objects;

public class Token<T> extends IToken.Stub {
    public final T identity;
    public Token(T identity) {
        this.identity = identity;
    }

    @Override
    public String string() {
        return toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Token<?> token = (Token<?>) o;
        return identity == token.identity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(identity);
    }

    @Override
    public String toString() {
        String id = Integer.toHexString(System.identityHashCode(identity));
        return String.format("Token{%s %s}", id, identity);
    }
}
