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

import android.os.Binder;
import android.os.Process;
import android.os.UserHandle;

import com.davidluoye.support.utils.Reflect;

import java.lang.reflect.Method;

public class UserHandles {

    private static int def(Integer value, int defValue) { return value != null ? value : defValue; }

    public static final int USER_ALL = def(Reflect.getStaticField(UserHandle.class, "USER_ALL"), -1);
    public static final UserHandle ALL = Reflect.getStaticField(UserHandle.class, "ALL");

    public static final int USER_CURRENT = def(Reflect.getStaticField(UserHandle.class, "USER_CURRENT"), -2);
    public static final UserHandle CURRENT = Reflect.getStaticField(UserHandle.class, "CURRENT");

    public static final int USER_CURRENT_OR_SELF = def(Reflect.getStaticField(UserHandle.class, "USER_CURRENT_OR_SELF"), -3);
    public static final UserHandle CURRENT_OR_SELF = Reflect.getStaticField(UserHandle.class, "CURRENT_OR_SELF");

    public static final int USER_NULL = def(Reflect.getStaticField(UserHandle.class, "USER_NULL"), -10000);
    private static final UserHandle NULL = Reflect.getStaticField(UserHandle.class, "NULL");

    public static final int USER_SYSTEM = def(Reflect.getStaticField(UserHandle.class, "USER_SYSTEM"), 0);
    public static final UserHandle SYSTEM = Reflect.getStaticField(UserHandle.class, "SYSTEM");

    public static boolean isSameUser(int uid1, int uid2) {
        return getUserId(uid1) == getUserId(uid2);
    }

    public static UserHandle getUserHandleForUid(int uid) {
        return of(getUserId(uid));
    }

    public static int getCallingUserId() {
        return getUserId(Binder.getCallingUid());
    }

    public static int myUserId() {
        return getUserId(Process.myUid());
    }

    public static boolean isSystem(UserHandle handle) { return handle.equals(SYSTEM); }

    public static int getUserId(int uid) {
        Method method = Reflect.getStaticMethod(UserHandle.class, "getUserId", new Class[]{int.class});
        if (method != null) {
            Integer res = Reflect.callStaticMethod(method, new Object[]{uid});
            return res != null ? res : 0;
        }
        return 0;
    }

    public static UserHandle of(int userId) {
        Method method = Reflect.getStaticMethod(UserHandle.class, "of", new Class[]{int.class});
        if (method != null) {
            return Reflect.callStaticMethod(method, new Object[]{userId});
        }
        return null;
    }
}
