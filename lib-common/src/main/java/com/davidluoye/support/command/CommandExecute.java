
package com.davidluoye.support.command;

import com.davidluoye.support.log.ILogger;
import com.davidluoye.support.util.StringUtil;

/** A convenient class to map command to values. */
public class CommandExecute {
    private static final ILogger LOGGER = ILogger.logger(CommandExecute.class);

    /** Input commands string line, this should be always to log with woring or error.*/
    protected final String mArgsLineString;
    private final String[] mArgs;
    private int mArgIndex;
    private String mCurrentCmd;
    private String mCurrentArg;

    public CommandExecute(String args[]) {
        mArgs = args;
        mArgIndex = -1;
        mCurrentCmd = null;
        mCurrentArg = null;
        mArgsLineString = StringUtil.join(args);
    }

    private String tryFindCmd(int index) {
        if (index >= mArgs.length) {
            return null;
        }

        String arg = mArgs[index];
        if (arg == null) {
            LOGGER.e("error, should not get here. commands[%s] is {%s}?", index, mArgsLineString);
            return null;
        }

        if (arg.equals("--")) {
            LOGGER.e("error, invalid commands[%s]='--' in {%s}", index, mArgsLineString);
            return null;
        }

        if (arg.startsWith("---")) {
            LOGGER.e("error, invalid commands[%s]={%s}", index, mArgsLineString);
            return null;
        }

        if (arg.startsWith("--") || arg.startsWith("-")) {
            return arg.trim();
        }

        LOGGER.d("get end of " + mArgsLineString);
        return arg;
    }

    public final String getCurrentCmd() {
        return mCurrentCmd;
    }

    public final String getCurrentArg() {
        return mCurrentArg;
    }

    public final String getNextCmd() {
        int index = mArgIndex;
        for (; index < mArgs.length - 1;) {
            index++;
            String cmd = tryFindCmd(index);
            if (cmd != null) {
                mArgIndex = index;
                mCurrentCmd = cmd;
                return cmd;
            }
        }
        return null;
    }

    public final String getNextArg() {
        int index = mArgIndex;
        while (index < mArgs.length - 1) {
            index++;
            String arg = mArgs[index];
            if (arg.startsWith("-")) {
                return null;
            }
            mArgIndex = index;
            mCurrentArg = arg;
            return arg;
        }
        return null;
    }
}
