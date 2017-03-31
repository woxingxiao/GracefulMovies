package com.oubowu.slideback;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.util.Stack;

/**
 * Created by Oubowu on 2016/9/20 3:28.
 */
public class ActivityHelper implements Application.ActivityLifecycleCallbacks {

    private static Stack<Activity> mActivityStack;

    public ActivityHelper() {
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        mActivityStack.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

        // Log.e("TAG", "ActivityHelper-销毁: " + activity);
        mActivityStack.remove(activity);

    }

    public Activity getPreActivity() {
        if (mActivityStack == null) {
            return null;
        }
        int size = mActivityStack.size();
        if (size < 2) {
            return null;
        }
        return mActivityStack.elementAt(size - 2);
    }

    public void finishAllActivity() {
        if (mActivityStack == null) {
            return;
        }
        for (Activity activity : mActivityStack) {
            activity.finish();
        }
    }

    public void printAllActivity() {
        if (mActivityStack == null) {
            return;
        }
        for (int i = 0; i < mActivityStack.size(); i++) {
            Log.e("TAG", "位置" + i + ": " + mActivityStack.get(i));
        }
    }

    /**
     * 强制删掉activity，用于用户快速滑动页面的时候，因为页面还没来得及destroy导致的问题
     *
     * @param activity 删掉的activity
     */
    void postRemoveActivity(Activity activity) {
        if (mActivityStack != null) {
            mActivityStack.remove(activity);
        }
    }

}
