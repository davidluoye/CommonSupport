package com.david.support.util;

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
