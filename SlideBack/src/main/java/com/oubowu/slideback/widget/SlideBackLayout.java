package com.oubowu.slideback.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewGroupCompat;
import android.support.v4.widget.ViewDragHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.oubowu.slideback.SlideConfig;
import com.oubowu.slideback.callbak.OnInternalStateListener;

/**
 * Created by Oubowu on 2016/9/22 0022 15:24.
 */
public class SlideBackLayout extends FrameLayout {

    private static final int MIN_FLING_VELOCITY = 400;
    private final String mTestName;
    private boolean mCheckPreContentView;
    private boolean mIsFirstAttachToWindow;
    private ViewDragHelper mDragHelper;
    private View mContentView;
    private CacheDrawView mCacheDrawView;
    private ShadowView mShadowView;
    private View mPreContentView;
    private Drawable mPreDecorViewDrawable;
    private int mScreenWidth;

    private boolean mEdgeOnly = false;
    private boolean mLock = false;

    @FloatRange(from = 0.0,
            to = 1.0)
    private float mSlideOutRangePercent = 0.4f;

    @FloatRange(from = 0.0,
            to = 1.0)
    private float mEdgeRangePercent = 0.1f;

    private float mSlideOutRange;
    private float mEdgeRange;

    private float mSlideOutVelocity;

    private boolean mIsEdgeRangeInside;

    private OnInternalStateListener mOnInternalStateListener;

    private float mDownX;

    private float mSlidDistantX;

    private boolean mRotateScreen;

    private boolean mCloseFlagForWindowFocus;
    private boolean mCloseFlagForDetached;
    private boolean mEnableTouchEvent;

    public SlideBackLayout(Context context, View contentView, View preContentView, Drawable preDecorViewDrawable, SlideConfig config, @NonNull OnInternalStateListener onInternalStateListener) {
        super(context);
        mContentView = contentView;
        mPreContentView = preContentView;
        mPreDecorViewDrawable = preDecorViewDrawable;
        mOnInternalStateListener = onInternalStateListener;

        initConfig(config);

        if (preContentView instanceof LinearLayout) {
            mTestName = "1号滑动";
        } else {
            mTestName = "2号滑动";
        }
        // Log.e("TAG", "创建了: " + mTestName);

    }

    private void initConfig(SlideConfig config) {

        if (config == null) {
            config = new SlideConfig.Builder().create();
        }

        mScreenWidth = getResources().getDisplayMetrics().widthPixels;

        final float density = getResources().getDisplayMetrics().density;
        final float minVel = MIN_FLING_VELOCITY * density;

        ViewGroupCompat.setMotionEventSplittingEnabled(this, false);
        SlideLeftCallback slideLeftCallback = new SlideLeftCallback();
        mDragHelper = ViewDragHelper.create(this, 1.0f, slideLeftCallback);
        // 最小拖动速度
        mDragHelper.setMinVelocity(minVel);
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);

        mCacheDrawView = new CacheDrawView(getContext());
        mCacheDrawView.setVisibility(INVISIBLE);
        addView(mCacheDrawView);

        mShadowView = new ShadowView(getContext());
        mShadowView.setVisibility(INVISIBLE);
        addView(mShadowView, mScreenWidth / 28, LayoutParams.MATCH_PARENT);

        addView(mContentView);

        mEdgeOnly = config.isEdgeOnly();
        mLock = config.isLock();
        mRotateScreen = config.isRotateScreen();

        mSlideOutRangePercent = config.getSlideOutPercent();
        mEdgeRangePercent = config.getEdgePercent();

        mSlideOutRange = mScreenWidth * mSlideOutRangePercent;
        mEdgeRange = mScreenWidth * mEdgeRangePercent;
        mSlideOutVelocity = config.getSlideOutVelocity();

        mSlidDistantX = mScreenWidth / 20.0f;

        mContentView.setFitsSystemWindows(false);

        if (mRotateScreen) {
            mContentView.findViewById(android.R.id.content).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 屏蔽上个内容页的点击事件
                }
            });
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                // 优化侧滑的逻辑，不要一有稍微的滑动就被ViewDragHelper拦截掉了
                if (event.getX() - mDownX < mSlidDistantX) {
                    return false;
                }
                break;
        }

        if (mLock) {
            return false;
        }

        if (mEdgeOnly) {
            float x = event.getX();
            mIsEdgeRangeInside = isEdgeRangeInside(x);
            return mIsEdgeRangeInside && mDragHelper.shouldInterceptTouchEvent(event);
        } else {
            return mDragHelper.shouldInterceptTouchEvent(event);
        }

    }

    private boolean isEdgeRangeInside(float x) {
        return x <= mEdgeRange;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mLock) {
            return super.onTouchEvent(event);
        }

        if (!mEdgeOnly || mIsEdgeRangeInside) {
            if (!mEnableTouchEvent) {
                return super.onTouchEvent(event);
            }
            if (mCloseFlagForDetached || mCloseFlagForWindowFocus) {
                // 针对快速滑动的时候，页面关闭的时候移除上个页面的时候，布局重新调整，这时候我们把contentView设为invisible，
                // 但是还是可以响应DragHelper的处理，所以这里根据页面关闭的标志位不给处理事件了
                // Log.e("TAG", mTestName + "都要死了，还处理什么触摸事件！！");
                return super.onTouchEvent(event);
            }
            mDragHelper.processTouchEvent(event);
        } else {
            return super.onTouchEvent(event);
        }
        return true;
    }

    private void addPreContentView() {
        if (mPreContentView.getParent() != SlideBackLayout.this) {
            // Log.e("TAG", mTestName + ": 我要把上个页面的内容页加到我这里啦！");
            mPreContentView.setTag("notScreenOrientationChange");
            ((ViewGroup) mPreContentView.getParent()).removeView(mPreContentView);
            SlideBackLayout.this.addView(mPreContentView, 0);
            mShadowView.setVisibility(VISIBLE);
        }
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    public void isComingToFinish() {
        if (mRotateScreen) {
            mCloseFlagForDetached = true;
            mCloseFlagForWindowFocus = false;
            mOnInternalStateListener.onClose(null);
            mPreContentView.setX(0);
        }
    }

    public String getTestName() {
        return mTestName;
    }

    public void updatePreContentView(View contentView) {
        mPreContentView = contentView;
        mCacheDrawView.drawCacheView(mPreContentView);
    }

    class SlideLeftCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mContentView;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return Math.max(Math.min(mScreenWidth, left), 0);
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mScreenWidth;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (releasedChild == mContentView) {

                if (xvel > mSlideOutVelocity) {
                    mDragHelper.settleCapturedViewAt(mScreenWidth, 0);
                    invalidate();
                    return;
                }

                if (mContentView.getLeft() < mSlideOutRange) {
                    mDragHelper.settleCapturedViewAt(0, 0);
                } else {
                    mDragHelper.settleCapturedViewAt(mScreenWidth, 0);
                }

                invalidate();
            }
        }

        @Override
        public void onViewDragStateChanged(int state) {
            switch (state) {
                case ViewDragHelper.STATE_IDLE:
                    if (mContentView.getLeft() == 0) {
                        // 2016/9/22 0022 回到原处
                        mOnInternalStateListener.onOpen();
                    } else if (mContentView.getLeft() == mScreenWidth) {
                        // 2016/9/22 0022 结束Activity

                        // 这里再绘制一次是因为在屏幕旋转的模式下，remove了preContentView后布局会重新调整
                        if (mRotateScreen && mCacheDrawView.getVisibility() == INVISIBLE) {
                            mCacheDrawView.setBackground(mPreDecorViewDrawable);
                            mCacheDrawView.drawCacheView(mPreContentView);
                            mCacheDrawView.setVisibility(VISIBLE);
                            // Log.e("TAG", mTestName + ": 这里再绘制一次是因为在屏幕旋转的模式下，remove了preContentView后布局会重新调整");
                            mCloseFlagForWindowFocus = true;
                            mCloseFlagForDetached = true;
                            // Log.e("TAG", mTestName + ": 滑动到尽头了这个界面要死了，把preContentView给回上个Activity");
                            // 这里setTag是因为下面的回调会把它移除出当前页面，这时候会触发它的onDetachedFromWindow事件，
                            // 而它的onDetachedFromWindow实际上是来处理屏幕旋转的，所以设置个tag给它，让它知道是当前界面移除它的，并不是屏幕旋转导致的
                            mPreContentView.setTag("notScreenOrientationChange");
                            mOnInternalStateListener.onClose(true);
                            mPreContentView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mCacheDrawView.setBackground(mPreDecorViewDrawable);
                                    mCacheDrawView.drawCacheView(mPreContentView);
                                }
                            }, 10);
                        } else if (!mRotateScreen) {
                            mCloseFlagForWindowFocus = true;
                            mCloseFlagForDetached = true;
                            mOnInternalStateListener.onClose(true);
                        }

                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {

            if (!mRotateScreen && mCacheDrawView.getVisibility() == INVISIBLE) {
                mCacheDrawView.setBackground(mPreDecorViewDrawable);
                mCacheDrawView.drawCacheView(mPreContentView);
                mCacheDrawView.setVisibility(VISIBLE);
            } else if (mRotateScreen) {

                if (!mCheckPreContentView) {

                    // 在旋转屏幕的模式下，这里的检查很有必要，比如一个滑动activity先旋转了屏幕，然后再返回上个滑动activity的时候，由于屏幕旋转上个activity会重建，步骤是：
                    // 上个activity会先新建一个activity，再把之前的销毁，所以新建的activity调SlideBackLayout.attach的时候传的上个activity实际上是要删掉的activity
                    // (因为要删掉的activity的destroy有延时的，还没销毁掉)，这就出错了;
                    // 所以这里还要在当前页面取得焦点的时候回调，去检查下看是不是上个activity改了，改了再重新赋值

                    mCheckPreContentView = true;
                    // 只需要检查一次上个Activity是不是变了
                    // Log.e("TAG","只需要检查一次上个Activity是不是变了");
                    mOnInternalStateListener.onCheckPreActivity(SlideBackLayout.this);
                }

                addPreContentView();

            }

            if (mShadowView.getVisibility() != VISIBLE) {
                mShadowView.setVisibility(VISIBLE);
            }

            final float percent = left * 1.0f / mScreenWidth;

            mOnInternalStateListener.onSlide(percent);

            if (mRotateScreen) {
                // // Log.e("TAG", "滑动上个页面");
                mPreContentView.setX(-mScreenWidth / 2 + percent * (mScreenWidth / 2));
            } else {
                mCacheDrawView.setX(-mScreenWidth / 2 + percent * (mScreenWidth / 2));
            }
            mShadowView.setX(mContentView.getX() - mShadowView.getWidth());
            mShadowView.redraw(1 - percent);
        }
    }


    public void edgeOnly(boolean edgeOnly) {
        mEdgeOnly = edgeOnly;
    }

    public boolean isEdgeOnly() {
        return mEdgeOnly;
    }

    public void lock(boolean lock) {
        mLock = lock;
    }

    public boolean isLock() {
        return mLock;
    }

    public void setSlideOutRangePercent(float slideOutRangePercent) {
        mSlideOutRangePercent = slideOutRangePercent;
        mSlideOutRange = mScreenWidth * mSlideOutRangePercent;
    }

    public float getSlideOutRangePercent() {
        return mSlideOutRangePercent;
    }

    public void setEdgeRangePercent(float edgeRangePercent) {
        mEdgeRangePercent = edgeRangePercent;
        mEdgeRange = mScreenWidth * mEdgeRangePercent;
    }

    public float getEdgeRangePercent() {
        return mEdgeRangePercent;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        // // Log.e("TAG", "onWindowFocusChanged(): " + this + " ; " + hasWindowFocus);

        if (hasWindowFocus) {

            mEnableTouchEvent = true;

            //            Log.e("TAG", "SlideBackLayout-378行-onWindowFocusChanged(): " + hasWindowFocus);

            // 当前页面
            if (!mIsFirstAttachToWindow) {
                mIsFirstAttachToWindow = true;
                // Log.e("TAG", mTestName + ": 第一次窗口取得焦点");
            } /*else if (mRotateScreen && mPreContentView.getParent() != SlideBackLayout.this) {
                // Log.e("TAG", mTestName + ": 从其他Activity返回来了 ");
            }*/
        } else {
            if (mRotateScreen) {
                // 1.跳转到另外一个Activity，例如也是需要滑动的，这时候就需要取当前Activity的contentView，所以这里把preContentView给回上个Activity
                if (mCloseFlagForWindowFocus) {
                    mCloseFlagForWindowFocus = false;
                    // Log.e("TAG", mTestName + ": onWindowFocusChanged前已经调了关闭");
                } else {
                    // Log.e("TAG", mTestName + ": 跳转到另外一个Activity，取这个Activity的contentView前把preContentView给回上个Activity");
                    mOnInternalStateListener.onClose(false);
                }
            }
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mEnableTouchEvent = false;
        // // Log.e("TAG", "SlideBackLayout-345行-onDetachedFromWindow(): " + this);
        if (mRotateScreen) {
            // 1.旋转屏幕的时候必调此方法，这里掉onClose目的是把preContentView给回上个Activity
            if (mCloseFlagForDetached) {
                mCloseFlagForDetached = false;
                // Log.e("TAG", mTestName + ": onDetachedFromWindow(): " + "已经调了关闭");
            } else {
                if (getTag() != null && getTag().equals("notScreenOrientationChange")) {
                    // 说明是手动删的不关旋转屏幕的事，所以不处理
                    // Log.e("TAG", mTestName + ":说明是手动删的不关旋转屏幕的事，所以不处理");
                    setTag(null);
                } else {
                    // Log.e("TAG", mTestName + ":屏幕旋转了，重建界面: 把preContentView给回上个Activity");
                    mOnInternalStateListener.onClose(false);
                }
            }
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        // Log.e("TAG", mTestName + ": SlideBackLayout-338行-onConfigurationChanged(): " + mScreenWidth);
        ViewGroup.LayoutParams layoutParams = mShadowView.getLayoutParams();
        layoutParams.width = mScreenWidth / 28;
        layoutParams.height = LayoutParams.MATCH_PARENT;
    }

}
