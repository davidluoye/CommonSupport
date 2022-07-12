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

package com.davidluoye.support.except;

import android.util.AndroidRuntimeException;

public class DuplicateCallerException extends AndroidRuntimeException {
    public DuplicateCallerException(String name) {
        super(name);
    }

    public DuplicateCallerException(String name, Throwable cause) {
        super(name, cause);
    }

    public DuplicateCallerException(Exception cause) {
        super(cause);
    }
}
