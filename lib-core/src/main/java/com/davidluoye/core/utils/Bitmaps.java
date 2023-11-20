package com.davidluoye.core.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class Bitmaps {

    public @interface Corner {
        int CORNER_LEFT_TOP = 0x0001;
        int CORNER_LEFT_BOTTOM = 0x0002;
        int CORNER_RIGHT_TOP = 0x0004;
        int CORNER_RIGHT_BOTTOM = 0x0008;

        int CORNER_LEFT = CORNER_LEFT_TOP | CORNER_LEFT_BOTTOM;
        int CORNER_RIGHT = CORNER_RIGHT_TOP | CORNER_RIGHT_BOTTOM;
        int CORNER_TOP = CORNER_LEFT_TOP | CORNER_RIGHT_TOP;
        int CORNER_BOTTOM = CORNER_LEFT_BOTTOM | CORNER_RIGHT_BOTTOM;
    }

    public static Bitmap createRoundCornerBitmap(Bitmap bitmap, int radio, @Corner int corners) {
        try {
            final int width = bitmap.getWidth();
            final int height = bitmap.getHeight();

            Bitmap paintingBoard = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(paintingBoard);
            canvas.drawARGB(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT);
            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);

            // draw 4 corner
            final RectF rectF = new RectF(0, 0, width, height);
            canvas.drawRoundRect(rectF, radio, radio, paint);

            // remove left top corner
            if ((corners & Corner.CORNER_LEFT_TOP) != Corner.CORNER_LEFT_TOP) {
                final Rect blockLeftTop = new Rect(0, 0, radio, radio);
                canvas.drawRect(blockLeftTop, paint);
            }

            // remove right top corner
            if ((corners & Corner.CORNER_RIGHT_TOP) != Corner.CORNER_RIGHT_TOP) {
                final Rect blockRightTop = new Rect(width - radio, 0, width, radio);
                canvas.drawRect(blockRightTop, paint);
            }

            // remove left bottom corner
            if ((corners & Corner.CORNER_LEFT_BOTTOM) != Corner.CORNER_LEFT_BOTTOM) {
                final Rect blockRightTop = new Rect(0, height - radio, radio, height);
                canvas.drawRect(blockRightTop, paint);
            }

            // remove right bottom corner
            if ((corners & Corner.CORNER_RIGHT_BOTTOM) != Corner.CORNER_RIGHT_BOTTOM) {
                final Rect blockRightTop = new Rect(width - radio, height - radio, width, height);
                canvas.drawRect(blockRightTop, paint);
            }

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            final Rect src = new Rect(0, 0, width, height);
            canvas.drawBitmap(bitmap, src, src, paint);
            return paintingBoard;
        } catch (Exception exp) {
            return bitmap;
        }
    }
}
