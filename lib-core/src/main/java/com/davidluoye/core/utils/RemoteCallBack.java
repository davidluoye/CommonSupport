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

package com.davidluoye.core.utils;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

import com.davidluoye.core.IRemoteCallBack;

public class RemoteCallBack implements Parcelable {

    public interface OnCallBack {
        Bundle onCallback(String key, Bundle extra);
    }

    private final IRemoteCallBack mRemote;
    private final OnCallBack callback;
    public RemoteCallBack(OnCallBack callback) {
        this.callback = callback;
        this.mRemote = new IRemoteCallBack.Stub() {
            @Override
            public Bundle process(String key, Bundle bundle) {
                if (callback != null) {
                    return callback.onCallback(key, bundle);
                }
                return null;
            }
        };
    }

    public final Bundle process(String key, Bundle extra) {
        // do local dispatch
        if (callback != null) {
            return callback.onCallback(key, extra);
        }

        // do remote dispatch
        if (mRemote != null) {
            try {
                return mRemote.process(key, extra);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /************************** for parcelable ******************************/
    protected RemoteCallBack(Parcel in) {
        this.mRemote = IRemoteCallBack.Stub.asInterface(in.readStrongBinder());
        this.callback = null;
    }

    public static final Creator<RemoteCallBack> CREATOR = new Creator<RemoteCallBack>() {
        @Override
        public RemoteCallBack createFromParcel(Parcel in) {
            return new RemoteCallBack(in);
        }

        @Override
        public RemoteCallBack[] newArray(int size) {
            return new RemoteCallBack[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStrongBinder(this.mRemote.asBinder());
    }

}
