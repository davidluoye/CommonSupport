package com.davidluoye.views;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;

public class RoundDrawable extends DrawableWrapper {

    private final RectF mTempRect = new RectF();
    private final Path mClipPath = new Path();
    private final RoundAttribute mRoundAttribute;

    public RoundDrawable(Drawable drawable) {
        this(drawable, 0);
    }

    public RoundDrawable(Drawable drawable, float radius) {
        this(drawable, radius, radius, radius, radius);
    }

    public RoundDrawable(Drawable drawable,
                         float radiusLeftTop, float radiusRightTop,
                         float radiusLeftBottom, float radiusRightBottom) {
        this(drawable, radiusLeftTop, radiusLeftTop, radiusRightTop, radiusRightTop,
                radiusLeftBottom, radiusLeftBottom, radiusRightBottom, radiusRightBottom);
    }

    public RoundDrawable(Drawable drawable,
                           float radiusLeftTopX, float radiusLeftTopY,
                           float radiusRightTopX, float radiusRightTopY,
                           float radiusLeftBottomX, float radiusLeftBottomY,
                           float radiusRightBottomX, float radiusRightBottomY) {
        this(drawable, new RoundAttribute.Builder()
                .setRadiusLeftTopX(radiusLeftTopX).setRadiusLeftTopY(radiusLeftTopY)
                .setRadiusRightTopX(radiusRightTopX).setRadiusRightTopY(radiusRightTopY)
                .setRadiusLeftBottomX(radiusLeftBottomX).setRadiusLeftBottomY(radiusLeftBottomY)
                .setRadiusRightBottomX(radiusRightBottomX).setRadiusRightBottomY(radiusRightBottomY)
                .build());
    }

    public RoundDrawable(Drawable drawable, RoundAttribute attribute) {
        super(drawable);
        this.mRoundAttribute = attribute.obtain();
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        mTempRect.set(getBounds());
        mClipPath.reset();
        mClipPath.addRoundRect(mTempRect, mRoundAttribute.toArray(), Path.Direction.CCW);
        super.onBoundsChange(bounds);
    }

    @Override
    public final void draw(Canvas canvas) {
        int saveCount = canvas.save();
        canvas.clipPath(mClipPath);
        super.draw(canvas);
        canvas.restoreToCount(saveCount);
    }
}
