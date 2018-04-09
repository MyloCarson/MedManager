package com.mylocarson.medmanager.utils.providers;

import android.app.AlarmManager;
import android.content.Context;

/**
 * Created by user on 07/04/2018.
 */

public class AlarmMangerProvider {
    private static final String TAG = AlarmMangerProvider.class.getSimpleName();

    private static AlarmManager mAlarmManager;

    public static synchronized void injectAlarmManger(AlarmManager alarmManager){
        if (alarmManager != null) {
            throw new IllegalStateException("Alarm started already");
        }
        mAlarmManager = alarmManager;
    }

    public static synchronized  AlarmManager getAlarmManager (Context context){
        if (mAlarmManager == null){
            mAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        }
        return mAlarmManager;
    }
}
