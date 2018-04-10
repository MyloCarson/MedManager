package com.mylocarson.medmanager.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import com.mylocarson.medmanager.utils.providers.AlarmMangerProvider;
import com.mylocarson.medmanager.utils.services.ReminderAlarmService;

/**
 * Created by user on 07/04/2018.
 */

@SuppressWarnings("ALL")
public class AlarmScheduler {

    public void setAlarm(Context context, String startDate, String startTime, int frequency, String reminderID) {
        AlarmManager alarmManager = AlarmMangerProvider.getAlarmManager(context);


        PendingIntent pendingIntent = ReminderAlarmService.getPendingIntent(context,reminderID);

        if (Build.VERSION.SDK_INT >=23 ){
            for (long alarmTime : Utilities.calculateAlarmtimeInMillis(context, startDate, startTime, frequency)) {
                Toast.makeText(context, "Alarm created for Lollipop " +alarmTime, Toast.LENGTH_SHORT).show();
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
            }
        }else if (Build.VERSION.SDK_INT >=19){
            for (long alarmTime : Utilities.calculateAlarmtimeInMillis(context, startDate, startTime, frequency)) {
                Toast.makeText(context, "Alarm created for Kitkat " +alarmTime, Toast.LENGTH_SHORT).show();

                alarmManager.setExact(AlarmManager.RTC_WAKEUP,alarmTime,pendingIntent);
            }

        }else{
            for (long alarmTime : Utilities.calculateAlarmtimeInMillis(context, startDate, startTime, frequency)) {
                Toast.makeText(context, "Alarm created for just  " +alarmTime, Toast.LENGTH_SHORT).show();
                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
            }
        }

    }

    public void setAlarmNow(Context context, String startDate, String endDate, String startTime, String reminderID) {
        AlarmManager alarmManager = AlarmMangerProvider.getAlarmManager(context);


        PendingIntent pendingIntent = ReminderAlarmService.getPendingIntent(context,reminderID);

        if (Build.VERSION.SDK_INT >= 23) {
            for (long alarmTime : Utilities.getArrayListOfLongTimeMillis(startDate, endDate, startTime)) {
                Toast.makeText(context, "Alarm created for Lollipop " + alarmTime, Toast.LENGTH_SHORT).show();
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
            }
        } else if (Build.VERSION.SDK_INT >= 19) {
            for (long alarmTime : Utilities.getArrayListOfLongTimeMillis(startDate, endDate, startTime)) {
                Toast.makeText(context, "Alarm created for Kitkat " + alarmTime, Toast.LENGTH_SHORT).show();

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
            }

        } else {
            for (long alarmTime : Utilities.getArrayListOfLongTimeMillis(startDate, endDate, startTime)) {
                Toast.makeText(context, "Alarm created for just  " + alarmTime, Toast.LENGTH_SHORT).show();
                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
            }
        }

    }

//    public void setRepeatingAlarm(Context context,String startDate,String endDate,int repeatTime ,String reminderID){
//        AlarmManager alarmManager = AlarmMangerProvider.getAlarmManager(context);
//
//        PendingIntent pendingIntent = ReminderAlarmService.getPendingIntent(context,reminderID);
//        for (long alarmTime : Utilities.getArrayListOfLongTimeMillis(startDate,endDate)) {
//            Toast.makeText(context, "Alarm created for  everyone " +alarmTime, Toast.LENGTH_SHORT).show();
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, Utilities.calculateRepeatTime(repeatTime), pendingIntent);
//        }
//    }

    public void cancelAlarm (Context context , String reminderID){
        AlarmManager alarmManager = AlarmMangerProvider.getAlarmManager(context);

        PendingIntent pendingIntent = ReminderAlarmService.getPendingIntent(context,reminderID);
        alarmManager.cancel(pendingIntent);
    }
}
