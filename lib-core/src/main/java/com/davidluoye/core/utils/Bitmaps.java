package com.davidluoye.core.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    /**
     * Given an input bitmap, scales it to the given radius and makes their corners round.
     *
     * @param bitmap {@link Bitmap} to scale and crop
     * @param radius desired output radius
     * @param corners desired output corners
     * @return output bitmap which is cropped to a rounded corners.
     */
    public static Bitmap createRoundCornerBitmap(Bitmap bitmap, int radius, @Corner int corners) {
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
            canvas.drawRoundRect(rectF, radius, radius, paint);

            // remove left top corner
            if ((corners & Corner.CORNER_LEFT_TOP) != Corner.CORNER_LEFT_TOP) {
                final Rect blockLeftTop = new Rect(0, 0, radius, radius);
                canvas.drawRect(blockLeftTop, paint);
            }

            // remove right top corner
            if ((corners & Corner.CORNER_RIGHT_TOP) != Corner.CORNER_RIGHT_TOP) {
                final Rect blockRightTop = new Rect(width - radius, 0, width, radius);
                canvas.drawRect(blockRightTop, paint);
            }

            // remove left bottom corner
            if ((corners & Corner.CORNER_LEFT_BOTTOM) != Corner.CORNER_LEFT_BOTTOM) {
                final Rect blockRightTop = new Rect(0, height - radius, radius, height);
                canvas.drawRect(blockRightTop, paint);
            }

            // remove right bottom corner
            if ((corners & Corner.CORNER_RIGHT_BOTTOM) != Corner.CORNER_RIGHT_BOTTOM) {
                final Rect blockRightTop = new Rect(width - radius, height - radius, width, height);
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


    /**
     * Given an input bitmap, scales it to the given width/height and makes it round.
     *
     * @param input {@link Bitmap} to scale and crop
     * @param targetWidth desired output width
     * @param targetHeight desired output height
     * @return output bitmap scaled to the target width/height and cropped to an oval. The
     *         cropping algorithm will try to fit as much of the input into the output as possible,
     *         while preserving the target width/height ratio.
     */
    public static Bitmap getRoundedBitmap(Bitmap input, int targetWidth, int targetHeight) {
        if (input == null) {
            return null;
        }
        final Bitmap result = Bitmap.createBitmap(targetWidth, targetHeight, input.getConfig());
        final Canvas canvas = new Canvas(result);
        final Paint paint = new Paint();
        canvas.drawARGB(0, 0, 0, 0);
        paint.setAntiAlias(true);
        canvas.drawOval(0, 0, targetWidth, targetHeight, paint);

        // Specifies that only pixels present in the destination (i.e. the drawn oval) should
        // be overwritten with pixels from the input bitmap.
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        final int inputWidth = input.getWidth();
        final int inputHeight = input.getHeight();

        // Choose the largest scale factor that will fit inside the dimensions of the
        // input bitmap.
        final float scaleBy = Math.min((float) inputWidth / targetWidth,
                (float) inputHeight / targetHeight);

        final int xCropAmountHalved = (int) (scaleBy * targetWidth / 2);
        final int yCropAmountHalved = (int) (scaleBy * targetHeight / 2);

        final Rect src = new Rect(
                inputWidth / 2 - xCropAmountHalved,
                inputHeight / 2 - yCropAmountHalved,
                inputWidth / 2 + xCropAmountHalved,
                inputHeight / 2 + yCropAmountHalved);

        final RectF dst = new RectF(0, 0, targetWidth, targetHeight);
        canvas.drawBitmap(input, src, dst, paint);
        return result;
    }

    /**
     * Decodes the bitmap with the given sample size
     */
    public static Bitmap decodeBitmapFromBytes(byte[] bytes, int sampleSize) {
        final BitmapFactory.Options options;
        if (sampleSize <= 1) {
            options = null;
        } else {
            options = new BitmapFactory.Options();
            options.inSampleSize = sampleSize;
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }


    /**
     * Returns Width or Height of the picture, depending on which size is smaller. Doesn't actually
     * decode the picture, so it is pretty efficient to run.
     */
    public static int getSmallerExtentFromBytes(byte[] bytes) {
        final BitmapFactory.Options options = new BitmapFactory.Options();

        // don't actually decode the picture, just return its bounds
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

        // test what the best sample size is
        return Math.min(options.outWidth, options.outHeight);
    }

    /**
     * Finds the optimal sampleSize for loading the picture
     * @param originalSmallerExtent Width or height of the picture, whichever is smaller
     * @param targetExtent Width or height of the target view, whichever is bigger.
     *
     * If either one of the parameters is 0 or smaller, no sampling is applied
     */
    public static int findOptimalSampleSize(int originalSmallerExtent, int targetExtent) {
        // If we don't know sizes, we can't do sampling.
        if (targetExtent < 1) return 1;
        if (originalSmallerExtent < 1) return 1;

        // Test what the best sample size is. To do that, we find the sample size that gives us
        // the best trade-off between resulting image size and memory requirement. We allow
        // the down-sampled image to be 20% smaller than the target size. That way we can get around
        // unfortunate cases where e.g. a 720 picture is requested for 362 and not down-sampled at
        // all. Why 20%? Why not. Prove me wrong.
        int extent = originalSmallerExtent;
        int sampleSize = 1;
        while ((extent >> 1) >= targetExtent * 0.8f) {
            sampleSize <<= 1;
            extent >>= 1;
        }

        return sampleSize;
    }

}
