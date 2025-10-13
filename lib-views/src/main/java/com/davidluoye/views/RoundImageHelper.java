package com.davidluoye.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class RoundImageHelper implements DrawableTranslator {

    private final Context mContext;
    private final DrawableTranslator mTranslator;

    private RoundAttribute mBackgroundAttribute;
    private RoundAttribute mForegroundAttribute;
    private RoundAttribute mResourceAttribute;
    /* package */ RoundImageHelper(Context context, DrawableTranslator translator) {
        this.mContext = context;
        this.mTranslator = translator;
    }

    public void buildAttribute(@Nullable AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            final TypedArray a = mContext.obtainStyledAttributes(
                    attrs, R.styleable.RoundImageView, defStyleAttr, 0);
            try {
                setBackgroundAttribute(buildRoundAttribute(mContext, a, R.styleable.RoundImageView_backgroundCorner));
                setForegroundAttribute(buildRoundAttribute(mContext, a, R.styleable.RoundImageView_foregroundCorner));
                setResourceAttribute(buildRoundAttribute(mContext, a, R.styleable.RoundImageView_resourceCorner));
            } finally {
                a.recycle();
            }
        }
    }

    public void setBackgroundAttribute(@Nullable RoundAttribute attribute) {
        this.mBackgroundAttribute = attribute == null ? RoundAttribute.buildEmpty() : attribute.obtain();
        Drawable drawable = this.mTranslator.getBackgroundDrawable();
        if (drawable != null) {
            this.setBackgroundDrawable(new RoundDrawable(drawable, mBackgroundAttribute));
        }
    }

    public void setForegroundAttribute(@Nullable RoundAttribute attribute) {
        this.mForegroundAttribute = attribute == null ? RoundAttribute.buildEmpty() : attribute.obtain();
        Drawable drawable = this.mTranslator.getForegroundDrawable();
        if (drawable != null) {
            this.setForegroundDrawable(new RoundDrawable(drawable, mForegroundAttribute));
        }
    }

    public void setResourceAttribute(@Nullable RoundAttribute attribute) {
        this.mResourceAttribute = attribute == null ? RoundAttribute.buildEmpty() : attribute.obtain();
        Drawable drawable = this.mTranslator.getImageDrawable();
        if (drawable != null) {
            this.setImageDrawable(new RoundDrawable(drawable, mResourceAttribute));
        }
    }

    @Override
    public void setBackgroundDrawable(@Nullable Drawable background) {
        if (background == null) {
            this.mTranslator.setBackgroundDrawable(null);
            return;
        }

        if (background instanceof RoundDrawable) {
            this.mTranslator.setBackgroundDrawable(background);
            return;
        }
        this.mTranslator.setBackgroundDrawable(new RoundDrawable(background, mBackgroundAttribute));
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        if (drawable == null) {
            this.mTranslator.setImageDrawable(null);
            return;
        }

        if (drawable instanceof RoundDrawable) {
            this.mTranslator.setImageDrawable(drawable);
            return;
        }
        this.mTranslator.setImageDrawable(new RoundDrawable(drawable, mResourceAttribute));
    }

    @Override
    public void setForegroundDrawable(@Nullable Drawable foreground) {
        if (foreground == null) {
            this.mTranslator.setForegroundDrawable(null);
            return;
        }

        if (foreground instanceof RoundDrawable) {
            this.mTranslator.setForegroundDrawable(foreground);
            return;
        }
        this.mTranslator.setForegroundDrawable(new RoundDrawable(foreground, mForegroundAttribute));
    }

    private static RoundAttribute buildRoundAttribute(Context context, TypedArray a, int styleableRes) {
        if (!a.hasValue(styleableRes)) return null;
        int resId = a.getResourceId(styleableRes, 0);
        if (resId == 0) return null;
        TypedArray res = context.obtainStyledAttributes(resId, R.styleable.RoundAttribute);
        try {
            RoundAttribute.Builder builder = new RoundAttribute.Builder();
            builder.setRadiusLeftTopX(res.getDimension(R.styleable.RoundAttribute_radiusLeftTopX, 0));
            builder.setRadiusLeftTopY(res.getDimension(R.styleable.RoundAttribute_radiusLeftTopY, 0));
            builder.setRadiusRightTopX(res.getDimension(R.styleable.RoundAttribute_radiusRightTopX, 0));
            builder.setRadiusRightTopY(res.getDimension(R.styleable.RoundAttribute_radiusRightTopY, 0));
            builder.setRadiusLeftBottomX(res.getDimension(R.styleable.RoundAttribute_radiusLeftBottomX, 0));
            builder.setRadiusLeftBottomY(res.getDimension(R.styleable.RoundAttribute_radiusLeftBottomY, 0));
            builder.setRadiusRightBottomX(res.getDimension(R.styleable.RoundAttribute_radiusRightBottomX, 0));
            builder.setRadiusRightBottomY(res.getDimension(R.styleable.RoundAttribute_radiusRightBottomY, 0));
            return builder.build();
        } finally {
            res.recycle();
        }
    }
}
