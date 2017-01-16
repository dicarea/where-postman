package es.dicarea.postman.whereisthepostman;

import android.app.Application;
import android.content.Context;

public class CustomApp extends Application {
    private static CustomApp mInstance;

    public static CustomApp getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return mInstance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        mInstance = this;
        super.onCreate();

        AlarmHelper alarmHelper = new AlarmHelper(this);
        alarmHelper.startAlarm();
    }
}