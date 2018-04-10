package com.mylocarson.medmanager.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.mylocarson.medmanager.R;
import com.mylocarson.medmanager.models.Medication;
import com.mylocarson.medmanager.models.Reminder;
import com.mylocarson.medmanager.utils.AlarmScheduler;
import com.mylocarson.medmanager.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmQuery;

@SuppressWarnings("ALL")
public class MedicationDetailsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    private
    Toolbar toolbar;

    @BindView(R.id.medDescription)
    private
    TextView medDescription;

    @BindView(R.id.medInterval)
    private
    TextView medInterval;

    @BindView(R.id.startDate)
    private
    TextView startDate;

    @BindView(R.id.endDate)
    private
    TextView endate;

    private String reminder_id;
    private Bundle data;
    private Realm realm;
    private Medication medication = new Medication();
    private Reminder reminder = new Reminder();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_details);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

//
        realm = Realm.getDefaultInstance();
        context = this;

        Intent intent = getIntent();
        if (intent!=null){
            data = intent.getExtras();
            String medication_id = data.getString(Constants.MEDICATION_ID);
            reminder_id = data.getString(Constants.REMINDER_ID);

            reminder = fetchReminder(reminder_id);
            medication = fetchMedication(medication_id);
            getSupportActionBar().setTitle(medication.getName().concat(" Reminder"));
            setupViews(medication, reminder);
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.medication_details_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete :
                confirmDelete().show();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private Reminder fetchReminder(String reminder_id){
        RealmQuery<Reminder>  realmQuery = realm.where(Reminder.class);

        return realmQuery.equalTo("id", reminder_id).findFirst();

    }

    private Medication fetchMedication(String medication_id){
        RealmQuery<Medication> realmQuery = realm.where(Medication.class);
        return realmQuery.equalTo("id", medication_id).findFirst();
    }

    private void setupViews(Medication medication, Reminder reminder){
        medDescription.setText(medication.getDescription());
        String frequencyString = reminder.getFrequency() == 1 ? Integer.toString(reminder.getFrequency()) + " time" :
                Integer.toString(reminder.getFrequency()) + " times";

        medInterval.setText(frequencyString);
        startDate.setText(reminder.getStartDate());
        endate.setText(reminder.getEndDate());
    }

    private AlertDialog confirmDelete (){
        final AlertDialog.Builder  builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.app_name);
        builder.setMessage("Are you sure you want to delete this reminder ?");
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteReminder();
                dialogInterface.dismiss();
            }
        });
        return builder.create();
    }

    private void deleteReminder(){
        if (reminder!=null){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    reminder.deleteFromRealm();
                    new AlarmScheduler().cancelAlarm(context,reminder_id);
                    Intent intent  = new Intent(context,ReminderActivity.class);
                    startActivity(intent);
                }
            });

        }
    }
}
