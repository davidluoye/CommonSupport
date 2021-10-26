package com.davidluoye.support.util;

import android.view.View;
import android.view.ViewGroup;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Views {

    public static String dumpView(View view, String prefix) {
        StringWriter sw = new StringWriter();
        dumpViewHierarchy(prefix, new PrintWriter(sw), view);
        return sw.toString();
    }

    private static void dumpViewHierarchy(String prefix, PrintWriter writer, View view) {
        writer.print(prefix);
        if (view == null) {
            writer.println("null");
            return;
        }
        writer.println(view.toString());
        if (!(view instanceof ViewGroup)) {
            return;
        }
        ViewGroup grp = (ViewGroup)view;
        final int N = grp.getChildCount();
        if (N <= 0) {
            return;
        }
        prefix = prefix + "  ";
        for (int i=0; i<N; i++) {
            dumpViewHierarchy(prefix, writer, grp.getChildAt(i));
        }
    }
}
