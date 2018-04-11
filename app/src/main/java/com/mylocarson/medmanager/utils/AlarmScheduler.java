package com.mylocarson.medmanager.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import com.mylocarson.medmanager.utils.providers.AlarmMangerProvider;
import com.mylocarson.medmanager.utils.services.ReminderAlarmService;

/**
 * Created by user on 07/04/2018.
 */

@SuppressWarnings("ALL")
public class AlarmScheduler {

    /**
     * this method cancels an alarm
     *
     * @param context
     * @param reminderID
     **/
    public void cancelAlarm (Context context , String reminderID){
        AlarmManager alarmManager = AlarmMangerProvider.getAlarmManager(context);

        PendingIntent pendingIntent = ReminderAlarmService.getPendingIntent(context,reminderID);
        alarmManager.cancel(pendingIntent);
    }

    /**
     * this method sets an alarm
     *
     * @param context
     * @param startDate
     * @param startTime
     * @param frequency
     * @param reminderID
     **/
    public void setAlarm(Context context, String startDate, String startTime, int frequency, String reminderID) {
        AlarmManager alarmManager = AlarmMangerProvider.getAlarmManager(context);

        PendingIntent pendingIntent = ReminderAlarmService.getPendingIntent(context, reminderID);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Utilities.calculateTimeInMillis(startDate, startTime), Utilities.calculateRepeatTime(frequency), pendingIntent);


    }

}
