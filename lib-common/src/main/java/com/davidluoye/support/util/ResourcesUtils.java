package com.davidluoye.support.util;

import android.content.res.Resources;
import android.view.View;
import android.view.ViewParent;

public class ResourcesUtils {

    public static View findViewById(String packageName, View root, String viewId) {
        final Resources res = root.getResources();

        int id = res.getIdentifier(viewId, "id", packageName);
        if (id > 0) {
            return root.findViewById(id);
        }

        id = res.getIdentifier(viewId, null, null);
        return root.getRootView().findViewById(id);
    }

    public static boolean notVisible(View view, View root) {
        return !isVisible(view, root);
    }

    public static boolean isVisible(View view, View root) {
        if (view == null || root == null) return false;
        if (root.getVisibility() != View.VISIBLE) return false;
        if (view.getVisibility() != View.VISIBLE) return false;

        ViewParent parent = view.getParent();
        while (parent != null) {
            if (parent == root) return true;
            if (parent instanceof View) {
                int visible = ((View) parent).getVisibility();
                if (visible != View.VISIBLE) return false;
            } else {
                // it is the top of view tree: ViewRootImpl
                return true;
            }
            parent = parent.getParent();
        }
        return true;
    }
}
