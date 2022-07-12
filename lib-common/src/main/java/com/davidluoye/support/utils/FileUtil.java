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

import java.io.File;

public class FileUtil {

    /**
     * Delete a file or directory
     *
     * @param file target file or directory
     * @return true if delete success
     */
    public static final boolean delete(File file) {
        if (!file.exists()) {
            return true;
        }

        if (file.isFile()) {
            return file.delete();
        }

        boolean success = true;
        File[] files = file.listFiles();
        for (File child : files) {
            success &= delete(child);
        }

        success &= file.delete();
        return success;
    }
}
