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
package com.davidluoye.core.property;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PropertyReader {

    private final char mSplitter;
    private final HashMap<String, String> mCache;
    private final Path path;
    public PropertyReader(char splitter, Path path) {
        this.mSplitter = splitter;
        this.mCache = new HashMap<>();
        this.path = path;
    }

    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(mCache);
    }

    public boolean read() {
        try(InputStream is = Files.newInputStream(path, StandardOpenOption.READ)) {
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
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
