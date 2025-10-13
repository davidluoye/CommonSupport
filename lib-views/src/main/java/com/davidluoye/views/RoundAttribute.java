package com.davidluoye.views;


import androidx.annotation.NonNull;

public class RoundAttribute {
    public final float radiusLeftTopX;
    public final float radiusLeftTopY;
    public final float radiusRightTopX;
    public final float radiusRightTopY;
    public final float radiusRightBottomX;
    public final float radiusRightBottomY;
    public final float radiusLeftBottomX;
    public final float radiusLeftBottomY;

    private RoundAttribute(float radiusLeftTopX, float radiusLeftTopY,
                           float radiusRightTopX, float radiusRightTopY,
                           float radiusRightBottomX, float radiusRightBottomY,
                           float radiusLeftBottomX, float radiusLeftBottomY) {
        this.radiusLeftTopX = radiusLeftTopX;
        this.radiusLeftTopY = radiusLeftTopY;
        this.radiusRightTopX = radiusRightTopX;
        this.radiusRightTopY = radiusRightTopY;
        this.radiusRightBottomX = radiusRightBottomX;
        this.radiusRightBottomY = radiusRightBottomY;
        this.radiusLeftBottomX = radiusLeftBottomX;
        this.radiusLeftBottomY = radiusLeftBottomY;
    }

    public float[] toArray() {
        return new float[] {this.radiusLeftTopX, this.radiusLeftTopY,
                this.radiusRightTopX, this.radiusRightTopY,
                this.radiusRightBottomX, this.radiusRightBottomY,
                this.radiusLeftBottomX, this.radiusLeftBottomY};
    }

    public RoundAttribute obtain() {
        return new RoundAttribute(this.radiusLeftTopX, this.radiusLeftTopY,
                this.radiusRightTopX, this.radiusRightTopY,
                this.radiusRightBottomX, this.radiusRightBottomY,
                this.radiusLeftBottomX, this.radiusLeftBottomY);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("{%s, %s, %s, %s, %s, %s, %s, %s}",
                this.radiusLeftTopX, this.radiusLeftTopY,
                this.radiusRightTopX, this.radiusRightTopY,
                this.radiusRightBottomX, this.radiusRightBottomY,
                this.radiusLeftBottomX, this.radiusLeftBottomY);
    }

    public static RoundAttribute buildEmpty() {
        return new Builder().build();
    }

    public static final class Builder {
        private float radiusLeftTopX;
        private float radiusLeftTopY;
        private float radiusRightTopX;
        private float radiusRightTopY;
        private float radiusRightBottomX;
        private float radiusRightBottomY;
        private float radiusLeftBottomX;
        private float radiusLeftBottomY;

        public Builder() {}

        public Builder setRadiusLeftTopX(float radius) {
            this.radiusLeftTopX = radius;
            return this;
        }

        public Builder setRadiusLeftTopY(float radius) {
            this.radiusLeftTopY = radius;
            return this;
        }

        public Builder setRadiusRightTopX(float radius) {
            this.radiusRightTopX = radius;
            return this;
        }

        public Builder setRadiusRightTopY(float radius) {
            this.radiusRightTopY = radius;
            return this;
        }

        public Builder setRadiusLeftBottomX(float radius) {
            this.radiusLeftBottomX = radius;
            return this;
        }

        public Builder setRadiusLeftBottomY(float radius) {
            this.radiusLeftBottomY = radius;
            return this;
        }

        public Builder setRadiusRightBottomX(float radius) {
            this.radiusRightBottomX = radius;
            return this;
        }

        public Builder setRadiusRightBottomY(float radius) {
            this.radiusRightBottomY = radius;
            return this;
        }

        public RoundAttribute build() {
            return new RoundAttribute(this.radiusLeftTopX, this.radiusLeftTopY,
                    this.radiusRightTopX, this.radiusRightTopY,
                    this.radiusRightBottomX, this.radiusRightBottomY,
                    this.radiusLeftBottomX, this.radiusLeftBottomY);
        }

        public RoundAttribute build(float leftTopX, float leftTopY, float rightTopX, float rightTopY,
                                    float rightBottomX, float rightBottomY, float leftBottomX, float leftBottomY) {
            return new RoundAttribute(leftTopX, leftTopY, rightTopX, rightTopY,
                    rightBottomX, rightBottomY, leftBottomX, leftBottomY);
        }
    }

    public static final class Circle {
        private final RoundAttribute.Builder builder;
        public Circle() {
            this.builder = new Builder();
        }

        public Circle setLeftTopRadius(float radius) {
            this.builder.setRadiusLeftTopX(radius).setRadiusLeftTopY(radius);
            return this;
        }

        public Circle setRightTopRadius(float radius) {
            this.builder.setRadiusRightTopX(radius).setRadiusRightTopY(radius);
            return this;
        }

        public Circle setLeftBottomRadius(float radius) {
            this.builder.setRadiusLeftBottomX(radius).setRadiusLeftBottomY(radius);
            return this;
        }

        public Circle setRightBottomRadius(float radius) {
            this.builder.setRadiusRightBottomX(radius).setRadiusRightBottomY(radius);
            return this;
        }

        public RoundAttribute create() {
            return this.builder.build();
        }

        public RoundAttribute create(float leftTop, float rightTop, float rightBottom, float leftBottom) {
            return this.setLeftTopRadius(leftTop)
                    .setRightTopRadius(rightTop)
                    .setRightBottomRadius(rightBottom)
                    .setLeftBottomRadius(leftBottom)
                    .create();
        }
    }
}
