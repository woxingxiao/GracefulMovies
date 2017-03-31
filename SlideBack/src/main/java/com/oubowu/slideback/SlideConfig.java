package com.oubowu.slideback;

import android.support.annotation.FloatRange;

/**
 * Created by Oubowu on 2016/9/22 23:37.
 */
public class SlideConfig {

    private boolean mEdgeOnly;
    private boolean mLock;

    @FloatRange(from = 0.0,
            to = 1.0)
    private float mEdgePercent;
    @FloatRange(from = 0.0,
            to = 1.0)
    private float mSlideOutPercent;

    private float mSlideOutVelocity;

    private boolean mRotateScreen;

    public boolean isEdgeOnly() {
        return mEdgeOnly;
    }

    public boolean isLock() {
        return mLock;
    }

    public float getEdgePercent() {
        return mEdgePercent;
    }

    public float getSlideOutPercent() {
        return mSlideOutPercent;
    }

    public float getSlideOutVelocity() {
        return mSlideOutVelocity;
    }

    /**
     * 屏幕是否旋转，针对是否旋转两种方案实现侧滑
     *
     * @return true为屏幕旋转，false为屏幕不旋转
     */
    public boolean isRotateScreen() {
        return mRotateScreen;
    }

    private SlideConfig() {
    }

    public SlideConfig(Builder builder) {
        mEdgeOnly = builder.edgeOnly;
        mLock = builder.lock;
        mEdgePercent = builder.edgePercent;
        mSlideOutPercent = builder.slideOutPercent;
        mSlideOutVelocity = builder.slideOutVelocity;
        mRotateScreen = builder.rotateScreen;
    }

    public static class Builder {

        private boolean edgeOnly = false;
        private boolean lock = false;

        @FloatRange(from = 0.0,
                to = 1.0)
        private float edgePercent = 0.4f;
        @FloatRange(from = 0.0,
                to = 1.0)
        private float slideOutPercent = 0.1f;

        private float slideOutVelocity = 2000f;

        private boolean rotateScreen = false;

        public Builder() {
        }

        public SlideConfig create() {
            return new SlideConfig(this);
        }

        public Builder edgeOnly(boolean edgeOnly) {
            this.edgeOnly = edgeOnly;
            return this;
        }

        public Builder lock(boolean lock) {
            this.lock = lock;
            return this;
        }

        public Builder slideOutVelocity(float slideOutVelocity) {
            this.slideOutVelocity = slideOutVelocity;
            return this;
        }

        public Builder edgePercent(@FloatRange(from = 0.0,
                to = 1.0) float edgePercent) {
            this.edgePercent = edgePercent;
            return this;
        }

        public Builder slideOutPercent(@FloatRange(from = 0.0,
                to = 1.0) float slideOutPercent) {
            this.slideOutPercent = slideOutPercent;
            return this;
        }

        /**
         * 屏幕是否旋转，针对是否旋转两种方案实现侧滑
         *
         * @return true为屏幕旋转，false为屏幕不旋转
         */
        public Builder rotateScreen(boolean rotateScreen) {
            this.rotateScreen = rotateScreen;
            return this;
        }

    }

}
