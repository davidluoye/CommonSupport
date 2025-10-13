package com.davidluoye.views;

import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

public interface DrawableTranslator {
    default Drawable getForegroundDrawable() { return null; }
    default Drawable getBackgroundDrawable() { return null; }
    default Drawable getImageDrawable() { return null; }

    void setForegroundDrawable(@Nullable Drawable foreground);
    void setBackgroundDrawable(@Nullable Drawable background);
    void setImageDrawable(@Nullable Drawable drawable);
}
