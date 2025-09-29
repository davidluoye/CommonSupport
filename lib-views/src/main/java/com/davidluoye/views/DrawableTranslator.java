package com.davidluoye.views;

import android.graphics.drawable.Drawable;

public interface DrawableTranslator {
    default Drawable getForegroundDrawable() { return null; }
    default Drawable getBackgroundDrawable() { return null; }
    default Drawable getImageDrawable() { return null; }

    void setForegroundDrawable(Drawable foreground);
    void setBackgroundDrawable(Drawable background);
    void setImageDrawable(Drawable drawable);
}