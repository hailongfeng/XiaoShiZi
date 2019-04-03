package edu.children.xiaoshizi.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import edu.children.xiaoshizi.DemoApplication;

public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "ActivityLife";
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        android.util.Log.d(TAG,activity.getClass().getSimpleName()+"...onCreate");
        DemoApplication.getInstance().addActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
//        android.util.Log.d(TAG,activity.getClass().getSimpleName()+"...onResumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
//        android.util.Log.d(TAG,activity.getClass().getSimpleName()+"...onPaused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
//        android.util.Log.d(TAG,activity.getClass().getSimpleName()+"...onStopped");
    }
    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//        android.util.Log.d(TAG,activity.getClass().getSimpleName()+"...onSaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        android.util.Log.d(TAG,activity.getClass().getSimpleName()+"...onActivityDestroyed");
        DemoApplication.getInstance().removeActivity(activity);

    }
}