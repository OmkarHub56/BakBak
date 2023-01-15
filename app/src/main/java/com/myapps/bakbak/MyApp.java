package com.myapps.bakbak;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myapps.bakbak.Activities.FriendsListActivity;

public class MyApp extends Application implements Application.ActivityLifecycleCallbacks {

    FirebaseAuth auth;
    FirebaseDatabase database;

    private int activityReferences=0;
    private boolean isActivityChangingConfigurations = false;

    @Override
    public void onCreate() {
        super.onCreate();

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        /* Enable disk persistence  */
        database.setPersistenceEnabled(true);

        setValueAfterCheckingUserExistence(true);
        registerActivityLifecycleCallbacks(this);

//        Log.i("mnd",String.valueOf(activityReferences));

    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
        if(activity.getClass()== FriendsListActivity.class){
            setValueAfterCheckingUserExistence(true);

        }
        Log.i("gh",activity.getClass().toString()+" created");
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        Log.i("gh",activity.getClass().toString()+" start");

        isActivityChangingConfigurations = activity.isChangingConfigurations();
//        Log.i("mnd+",String.valueOf(activityReferences));
//        if(activity.getClass()== FriendsListActivity.class){
////            setValueAfterCheckingUserExistence(false);
//            setValueAfterCheckingUserExistence(true);
//
//
//        }
        if (++activityReferences==1 && !isActivityChangingConfigurations) {
            // App enters foreground
            setValueAfterCheckingUserExistence(true);
        }
        Log.i("gh",String.valueOf(activityReferences));
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        Log.i("gh",activity.getClass().toString()+" resumed");

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        Log.i("gh",activity.getClass().toString()+" paused");

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        Log.i("gh",activity.getClass().toString()+" stopped");

        isActivityChangingConfigurations = activity.isChangingConfigurations();
        if (--activityReferences==0 && !isActivityChangingConfigurations) {
            // App enters foreground
            setValueAfterCheckingUserExistence(false);
        }
        Log.i("gh",String.valueOf(activityReferences));

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
//        activityReferences--;
        Log.i("gh",activity.getClass().toString()+" destroyed");
        Log.i("gh",String.valueOf(activityReferences));

    }

    public void setValueAfterCheckingUserExistence(boolean value){
        if(auth.getUid()!=null){
            DatabaseReference userExists=database.getReference().child(ConstantsClass.USERS).child(auth.getUid());
            ValueEventListener listener=new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // to check if user exists
                    if(snapshot.exists()){
                        userExists.child("online").setValue(value);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            userExists.addValueEventListener(listener);
            new Handler().postDelayed(() -> {
                userExists.removeEventListener(listener);
            },1500);
        }
    }
}
