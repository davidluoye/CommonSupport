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
package com.davidluoye.support.utils;

import android.os.SystemClock;

import java.io.FileDescriptor;
import java.io.PrintWriter;

public class QuickClick {
    private final int count;
    private final long millis;
    private final long[] hitsRecord;
    public QuickClick(int count, long millis){
        this.count = count;
        this.millis = millis;
        this.hitsRecord = new long[count];
    }

    public void clean() {
        for (int i = 0; i < hitsRecord.length; i++) {
            hitsRecord[i] = 0;
        }
    }

    public boolean handleClick(){
        System.arraycopy(hitsRecord, 1, hitsRecord, 0, count - 1);
        hitsRecord[count - 1] = SystemClock.uptimeMillis();
        if (hitsRecord[0] + millis >= SystemClock.uptimeMillis()) {
            return true;
        }
        return false;
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        writer.append("QuickClick: count = " + count + ", millis = " + millis);
        writer.append("{");
        for (int i = 0; i < count; i++) {
            if(i > 0){
                writer.append(",");
            }
            writer.append(String.valueOf(hitsRecord[i]));
        }
        writer.append("}");
    }
}
