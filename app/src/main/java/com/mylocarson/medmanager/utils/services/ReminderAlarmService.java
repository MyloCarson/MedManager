package com.mylocarson.medmanager.utils.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.mylocarson.medmanager.R;
import com.mylocarson.medmanager.activities.ReminderActivity;
import com.mylocarson.medmanager.models.Reminder;
import com.mylocarson.medmanager.utils.Constants;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by user on 07/04/2018.
 */

@SuppressWarnings("ALL")
public class ReminderAlarmService extends IntentService {
    private static final String TAG  = ReminderAlarmService.class.getSimpleName();

    private static  final int NOTIFICATION_ID = 50;

    public ReminderAlarmService() {
        super(TAG);
    }

    /**
     * this method returns a pendingIntent for a given reminder
     *
     * @param context
     * @param reminderID
     **/
    public static PendingIntent getPendingIntent(Context context, String reminderID){
        Intent intent = new Intent(context,ReminderAlarmService.class);
        intent.putExtra(Constants.REMINDER_ID,reminderID);
        return PendingIntent.getService(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        assert intent != null;
        String reminder_ID = intent.getStringExtra(Constants.REMINDER_ID);

        Intent action =  new Intent(this, ReminderActivity.class);
        action.putExtra(Constants.REMINDER_ID,reminder_ID);
        PendingIntent operation = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] vibrationPattern = {1000,1000,1000,1000};
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(Constants.CHANNEL_ID,Constants.CHANNEL_NAME,importance);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(getApplicationContext(),channel.getId());
        }else{
            //noinspection deprecation
            builder = new NotificationCompat.Builder(getApplicationContext());
        }
        builder = builder.setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(Notification.PRIORITY_HIGH)
                .setColor(getResources().getColor(R.color.blue1))
                .setSound(sound)
                .setVibrate(vibrationPattern)
                .setContentIntent(operation)
                .setContentTitle("MedManager")
                .setContentText("Use ".concat(getReminderMedicationName(reminder_ID)).concat(" now!!!"))
                .setAutoCancel(true);
        assert notificationManager != null;
        notificationManager.notify(NOTIFICATION_ID,builder.build());


    }

    /** this method fetches the Medication Name for a given reminder
     * @param reminderID **/
    @SuppressWarnings("ConstantConditions")
    private String getReminderMedicationName(String reminderID){
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<Reminder> realmQuery = realm.where(Reminder.class);
        RealmResults<Reminder> realmResults = realmQuery.equalTo("id",reminderID).findAll();
        return realmResults.get(0).getMedicationName();
    }
}
