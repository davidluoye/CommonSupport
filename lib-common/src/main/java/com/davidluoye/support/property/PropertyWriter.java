package com.davidluoye.support.property;

import android.util.ArrayMap;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class PropertyWriter {

    private final char mSplitter;
    public PropertyWriter(char splitter) {
        this.mSplitter = splitter;
    }

    public boolean write(OutputStream os, ArrayMap<String, String> map) {
        try {
            BufferedWriter stream = new BufferedWriter(new OutputStreamWriter(os));
            final int size = map.size();
            for (int index = 0; index < size; index++) {
                String key = map.keyAt(index);
                String value = map.valueAt(index);
                stream.write(String.format("%s%s%s", key, mSplitter, (value == null) ? "" : value));
                stream.newLine();
            }
            stream.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
