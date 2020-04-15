
package com.davidluoye.support.command;

import java.util.ArrayList;
import java.util.List;

public class CommandExecuteExt extends CommandExecute {

    public CommandExecuteExt(String args[]) {
        super(args);
    }

    public List<String> getNextArgsList() {
        final List<String> values = new ArrayList<String>();
        String v = getNextArg();
        do {
            if (v == null) {
                break;
            }
            values.add(v);
        } while ((v = getNextArg()) != null);
        return values;
    }

    public String[] getNextArgs() {
        List<String> values = getNextArgsList();
        return values.toArray(new String[] {});
    }
}
