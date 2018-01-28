package com.lipu.findnearbyplacesapp;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

/**
 * Created by mdmunirhossain on 1/28/18.
 */

public class NearbyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    // uncaught exception handler variable
    private Thread.UncaughtExceptionHandler defaultUEH;

    // handler listener
    private Thread.UncaughtExceptionHandler _unCaughtExceptionHandler =
            new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable ex) {

                    // here I do logging of exception to a db
                    PendingIntent myActivity = PendingIntent.getActivity(getApplicationContext(),
                            192837, new Intent(getApplicationContext(), SplashScreen.class),
                            PendingIntent.FLAG_ONE_SHOT);

                    AlarmManager alarmManager;
                    alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            15000, myActivity);
                    System.exit(2);

                    // re-throw critical exception further to the os (important)
                    defaultUEH.uncaughtException(thread, ex);
                }
            };

    public NearbyApplication() {
        defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        // setup handler for uncaught exception
        Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);
    }

}
