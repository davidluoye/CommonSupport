package com.davidluoye.support.util.list;

import java.util.LinkedList;
import java.util.Objects;

public class EntrySet<KEY, VALUE> {

    private static final LinkedList<EntrySet<?,?>> sCache = new LinkedList<>();

    public final Param key;
    public final Param value;
    private EntrySet(KEY key, VALUE value) {
        this.key = new Param(key);
        this.value = new Param(value);
    }

    public KEY key() { return key.find(); }

    public VALUE value() { return value.find(); }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof EntrySet)) {
            return false;
        }
        EntrySet<?, ?> p = (EntrySet<?, ?>) o;
        return Objects.equals(p.key, key) && Objects.equals(p.value, value);
    }

    @Override
    public int hashCode() {
        return key.hashCode() ^ value.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s, %s", key, value);
    }

    public void recycle() {
        synchronized (sCache) {
            this.key.set(null);
            this.value.set(null);
            sCache.add(this);
        }
    }

    public static <KEY, VALUE> EntrySet<KEY, VALUE> obtain(KEY key, VALUE value) {
        synchronized (sCache) {
            EntrySet<?,?> cache = sCache.poll();
            if (cache == null) {
                cache = new EntrySet<>(key, value);
            } else {
                cache.key.set(key);
                cache.value.set(value);
            }
            return (EntrySet<KEY, VALUE>)cache;
        }
    }

    public static class Param {
        private Object param;
        /* package */ Param(Object param) {
            this.param = param;
        }
        /* package */ <T> T set(Object reference) { Object old = this.param;
            this.param = reference;
            return (T)old;
        }

        public <T> T find() {
            return (T)this.param;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            return Objects.equals(param, o);
        }

        @Override
        public int hashCode() { return param == null ? 0 : Objects.hash(param); }
    }
}
