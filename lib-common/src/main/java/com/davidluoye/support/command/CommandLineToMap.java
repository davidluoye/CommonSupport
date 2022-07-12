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
package com.davidluoye.support.command;

import android.util.ArrayMap;

import java.util.List;

public class CommandLineToMap {

    private final CommandExecuteExt mExe;
    private final ArrayMap<String, List<String>> mKeyMap;

    public CommandLineToMap(String args[]) {
        mExe = new CommandExecuteExt(args);
        mKeyMap = new ArrayMap<String, List<String>>();
        execute();
    }

    private void execute() {
        String cmd = null;
        while ((cmd = mExe.getNextCmd()) != null) {
            List<String> values = mExe.getNextArgsList();
            mKeyMap.put(cmd, values);
        }
    }

    public ArrayMap<String, List<String>> getCommandToValue() {
        return new ArrayMap<>(mKeyMap);
    }
}