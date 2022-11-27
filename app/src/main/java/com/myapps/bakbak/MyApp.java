package com.myapps.bakbak;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MyApp extends Application implements Application.ActivityLifecycleCallbacks {


    private int activityReferences = 0;
    private boolean isActivityChangingConfigurations = false;
    FirebaseDatabase database;
    @Override
    public void onCreate() {
        super.onCreate();
        /* Enable disk persistence  */
        database=FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("online").setValue(true);

        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        if (++activityReferences == 1 && !isActivityChangingConfigurations) {
            // App enters foreground
            database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("online").setValue(true);
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        isActivityChangingConfigurations = activity.isChangingConfigurations();
        if (--activityReferences == 0 && !isActivityChangingConfigurations) {
            // App enters background
            database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("online").setValue(false);
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
}
