/*
 * Copyright 2021 The authors David Yang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.davidluoye.support.os;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;


import com.davidluoye.support.list.ArrayEntry;
import com.davidluoye.support.list.EntrySet;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

public class RemoteCallBacks<CALLBACK extends IInterface, COOKIE> {

    @FunctionalInterface
    public interface BiiConsumer<KEY, VALUE> {
        boolean interrupt(KEY key, VALUE value);
    }

    private final boolean defRevertOrder;
    private final ArrayEntry<IBinder, Node> entries;

    public RemoteCallBacks() {
        this(true);
    }

    public RemoteCallBacks(boolean defRevertOrder) {
        this.entries = new ArrayEntry<>();
        this.defRevertOrder = defRevertOrder;
    }

    public boolean put(CALLBACK callBack) {
        return put(callBack, null);
    }

    public boolean put(CALLBACK callback, COOKIE cookie) {
        synchronized (this) {
            IBinder token = callback.asBinder();
            Node node = new Node(token, callback, cookie);
            if (node.linkDied()) {
                entries.removeKey(token); // consider lru logic
                entries.put(token, node);
                return true;
            }
            return false;
        }
    }

    public boolean remove(CALLBACK callback) {
        synchronized (this) {
            IBinder token = callback.asBinder();
            Node node = entries.removeKey(token);
            if (node != null) {
                node.unlinkDied();
                return true;
            }
            return false;
        }
    }

    public void broadcast(BiConsumer<CALLBACK, COOKIE> consumer) {
        broadcast(consumer, defRevertOrder);
    }

    public void broadcast(BiConsumer<CALLBACK, COOKIE> consumer, boolean reverseOrder) {
        synchronized (this) {
            List<Node> nodes = entries.values();
            final int step = reverseOrder ? -1 : 1;
            final int head = reverseOrder ? nodes.size() - 1 : 0;
            for (int index = head; index >= 0 && index < nodes.size(); index += step) {
                Node node = nodes.get(index);
                consumer.accept(node.callback, node.cookie);
            }
        }
    }

    public EntrySet<CALLBACK, COOKIE> findBreak(BiiConsumer<CALLBACK, COOKIE> consumer) {
        return findBreak(consumer, defRevertOrder);
    }

    public EntrySet<CALLBACK, COOKIE> findBreak(BiiConsumer<CALLBACK, COOKIE> consumer, boolean reverseOrder) {
        synchronized (this) {
            List<Node> nodes = entries.values();
            final int step = reverseOrder ? -1 : 1;
            final int head = reverseOrder ? nodes.size() - 1 : 0;
            for (int index = head; index >= 0 && index < nodes.size(); index += step) {
                Node node = nodes.get(index);
                if (consumer.interrupt(node.callback, node.cookie)) {
                    return EntrySet.obtain(node.callback, node.cookie);
                }
            }
            return null;
        }
    }

    public CALLBACK getCallBackFromCookie(COOKIE cookie) {
        return getCallBackFromCookie(cookie, defRevertOrder);
    }

    public CALLBACK getCallBackFromCookie(COOKIE cookie, boolean reverseOrder) {
        EntrySet<CALLBACK, COOKIE> entry = findBreak((key, value) -> Objects.equals(value, cookie), reverseOrder);
        return entry != null ? entry.key() : null;
    }

    public COOKIE getCookieFromCallBack(CALLBACK callback) {
        Node node = entries.getValue(callback.asBinder());
        return node != null ? node.cookie : null;
    }

    public boolean hasCookie(COOKIE cookie) {
        return getCallBackFromCookie(cookie) != null;
    }

    public int size() {
        return entries.size();
    }

    private final class Node implements IBinder.DeathRecipient {
        private final CALLBACK callback;
        private final COOKIE cookie;
        private final IBinder token;
        public Node(IBinder token, CALLBACK callback, COOKIE cookie) {
            this.token = token;
            this.callback = callback;
            this.cookie = cookie;
        }

        public boolean linkDied() {
            try {
                token.linkToDeath(this, 0);
                return true;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return false;
        }

        public void unlinkDied() {
            token.unlinkToDeath(this, 0);
        }

        @Override
        public void binderDied() {
            synchronized (entries) {
                entries.removeKey(token);
            }
        }
    }
}
