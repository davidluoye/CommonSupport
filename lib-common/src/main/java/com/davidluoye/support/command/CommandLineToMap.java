
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