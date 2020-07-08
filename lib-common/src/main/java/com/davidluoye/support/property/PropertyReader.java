package com.davidluoye.support.property;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PropertyReader {

    private final char mSplitter;
    private final HashMap<String, String> mCache;
    public PropertyReader(char splitter) {
        this.mSplitter = splitter;
        this.mCache = new HashMap<>();
    }

    public Map getProperties() {
        return Collections.unmodifiableMap(mCache);
    }

    public boolean read(InputStream is) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = reader.readLine()) != null) {
                int index = line.indexOf(mSplitter);
                if (index > 0) {
                    String key = line.substring(0, index);
                    String value = index < (line.length() - 1) ? line.substring(index + 1) : "";
                    mCache.put(key, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
