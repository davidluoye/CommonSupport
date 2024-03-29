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

import android.util.ArrayMap;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class PropertyWriter {

    private final char mSplitter;
    private final Path path;
    public PropertyWriter(char splitter, Path path) {
        this.mSplitter = splitter;
        this.path = path;
    }

    public boolean write(ArrayMap<String, String> map) {
        try (OutputStream os = Files.newOutputStream(path, StandardOpenOption.CREATE)){
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
