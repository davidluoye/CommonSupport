package com.davidluoye.views;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;

public class RoundImageView extends ImageView {

    private final RoundImageHelper mImageHelper;
    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mImageHelper = new RoundImageHelper(context, mDrawableTranslator);
        this.mImageHelper.buildAttribute(attrs, defStyleAttr);
    }

    public void setBackgroundAttribute(@Nullable RoundAttribute attribute) {
        if (mImageHelper != null) {
            mImageHelper.setBackgroundAttribute(attribute);
        }
    }

    public void setForegroundAttribute(@Nullable RoundAttribute attribute) {
        if (mImageHelper != null) {
            mImageHelper.setForegroundAttribute(attribute);
        }
    }

    public void setResourceAttribute(@Nullable RoundAttribute attribute) {
        if (mImageHelper != null) {
            mImageHelper.setResourceAttribute(attribute);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void setImageResource(int resId) {
        Drawable drawable = getContext().getDrawable(resId);
        if (mImageHelper == null) {
            super.setImageDrawable(drawable);
        } else {
            this.mImageHelper.setImageDrawable(drawable);
        }
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        Drawable drawable = getDrawableFromUri(getContext(), uri);
        if (mImageHelper == null) {
            super.setImageDrawable(drawable);
        } else {
            this.mImageHelper.setImageDrawable(drawable);
        }
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        if (mImageHelper == null) {
            super.setBackgroundDrawable(background);
        } else {
            this.mImageHelper.setBackgroundDrawable(background);
        }
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        if (mImageHelper == null) {
            super.setImageDrawable(drawable);
        } else {
            this.mImageHelper.setImageDrawable(drawable);
        }
    }

    @Override
    public void setForeground(Drawable foreground) {
        if (mImageHelper == null) {
            super.setForeground(foreground);
        } else {
            this.mImageHelper.setForegroundDrawable(foreground);
        }
    }

    private final DrawableTranslator mDrawableTranslator = new DrawableTranslator() {
        @Override
        public Drawable getForegroundDrawable() {
            return RoundImageView.super.getForeground();
        }

        @Override
        public Drawable getBackgroundDrawable() {
            return RoundImageView.super.getBackground();
        }

        @Override
        public Drawable getImageDrawable() {
            return RoundImageView.super.getDrawable();
        }

        @Override
        public void setForegroundDrawable(Drawable foreground) {
            RoundImageView.super.setForeground(foreground);
        }

        @Override
        public void setBackgroundDrawable(Drawable background) {
            RoundImageView.super.setBackgroundDrawable(background);
        }

        @Override
        public void setImageDrawable(@Nullable Drawable drawable) {
            RoundImageView.super.setImageDrawable(drawable);
        }
    };

    private static Drawable getDrawableFromUri(Context context, Uri uri) {
        final ContentResolver resolver = context.getContentResolver();
        try(InputStream stream = resolver.openInputStream(uri)) {
            if (stream != null) {
                return Drawable.createFromStream(stream, uri.toString());
            }
            throw new IOException("Failed to open input stream for URI: " + uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
