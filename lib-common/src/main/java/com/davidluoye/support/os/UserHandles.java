package com.davidluoye.support.os;

import android.os.Binder;
import android.os.Process;
import android.os.UserHandle;

import com.davidluoye.support.util.Reflect;

import java.lang.reflect.Method;

public class UserHandles {

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
