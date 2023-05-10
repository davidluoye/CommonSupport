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

package com.david.demo;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.support.annotation.NonNull;

import com.davidluoye.support.utils.RemoteReference;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

public class RemoteReferenceTest {

    private final RemoteReference<FakeRemote> mReference;
    public RemoteReferenceTest() {
        this.mReference = new RemoteReference<>();
    }

    @Test
    public void testSet() {
        RemoteReference<FakeRemote> ref = new RemoteReference<>();
        Assert.assertNull(ref.get());

        ref.set(null);
        Assert.assertNull(ref.get());

        final FakeRemote remote = new FakeRemote();
        ref.set(remote);
        Assert.assertEquals(remote, ref.get());

        remote.kill();
        Assert.assertNull(ref.get());

        ref.set(new FakeRemote());
        Assert.assertNotEquals(ref.get(), remote);

        remote.kill();
        Assert.assertNotEquals(ref.get(), null);
    }

    private static class FakeRemote implements IInterface {
        private final Binder binder;
        private final AtomicReference<IBinder.DeathRecipient> death;
        public FakeRemote() {
            this.death = new AtomicReference<>();
            this.binder = new Binder() {
                @Override
                public void linkToDeath(@NonNull DeathRecipient recipient, int flags) {
                    death.set(recipient);
                }
            };
        }

        public void kill() {
            if (this.death.get() != null) {
                this.death.get().binderDied();
                this.death.set(null);
            }
        }

        @Override
        public IBinder asBinder() {
            return binder;
        }
    }
}
