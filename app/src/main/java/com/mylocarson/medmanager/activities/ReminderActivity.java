package com.mylocarson.medmanager.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.mylocarson.medmanager.R;
import com.mylocarson.medmanager.adapters.ReminderAdapter;
import com.mylocarson.medmanager.models.Medication;
import com.mylocarson.medmanager.models.Reminder;
import com.mylocarson.medmanager.utils.AlarmScheduler;
import com.mylocarson.medmanager.utils.Constants;
import com.mylocarson.medmanager.utils.DrawerUtil;
import com.mylocarson.medmanager.utils.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ReminderActivity extends AppCompatActivity {
    private static  final String TAG = ReminderActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private AlertDialog alertDialog;

    @BindView(R.id.reminderRecycler)
    RecyclerView reminderRecycler;
    @BindView(R.id.addNewReminder2)
    Button addNewReminder;
    @BindView(R.id.emptyReminderStateLayout)
    LinearLayout emptyReminderStateLayout;


    private Realm realm;
    private String spinner_Value;
    private String selected_medication_id;

    private boolean isSelected = false;
    private ArrayList<Reminder> persistReminders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        realm = Realm.getDefaultInstance();
        DrawerUtil.getDrawer(this,toolbar);

        if (savedInstanceState != null) {
            this.persistReminders = savedInstanceState.getParcelableArrayList(Constants.REMINDER_ARRAYLIST);
            assert this.persistReminders != null;
            setupRecycler(this.persistReminders);
        }else{
            getAllReminders();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        persistReminders = savedInstanceState.getParcelableArrayList(Constants.REMINDER_ARRAYLIST);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(Constants.REMINDER_ARRAYLIST, persistReminders);
        super.onSaveInstanceState(outState);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.reminder_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.alarm:
                reminderForm(this);
                break;
        }
        return true;
    }

    @OnClick(R.id.addNewReminder)
    void addNewReminder(){
        reminderForm(this);
    }
    @OnClick(R.id.addNewReminder2)
    void addNewReminder2(){
        reminderForm(this);
    }

    private void reminderForm(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.reminder_entry, null);
        builder.setView(view);

        final EditText interval, startDate, endDate, startTime;
        Button addMed;
        Button close;
        final Spinner spinner;


        final AlarmScheduler alarmScheduler = new AlarmScheduler();

        spinner = view.findViewById(R.id.medName);
        interval = view.findViewById(R.id.medFrequency);
        startDate = view.findViewById(R.id.medStartDate);
        endDate = view.findViewById(R.id.medEndDate);
        addMed = view.findViewById(R.id.addNewMedication);
        startTime = view.findViewById(R.id.medStartTime);
        close = view.findViewById(R.id.close);

        RealmQuery<Medication> realmQuery = realm.where(Medication.class);
        RealmResults<Medication> realmResults = realmQuery.findAll();
        final ArrayList<String> arrayList = new ArrayList<>();
        final ArrayList<String> medicationIdArrayList = new ArrayList<>();

        arrayList.add("Medications");
        medicationIdArrayList.add("");

        for (Medication medication : realmResults){
            arrayList.add(medication.getName());
            medicationIdArrayList.add(medication.getId());
        }

        Object[] objectList = arrayList.toArray();
        String[] medNames = Arrays.copyOf(objectList, objectList.length , String[].class);


        ArrayAdapter<String> spinnerArrayAdapter = Utilities.arrayAdapter(this,medNames);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position >0){
                    spinner_Value = adapterView.getItemAtPosition(position).toString();
                    selected_medication_id = medicationIdArrayList.get(position);
                    isSelected =true;
                }else{
                    isSelected = false;
                }

                Log.d(TAG, "onItemSelected: "+spinner_Value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                isSelected = false;
            }
        });


        addMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String medicationName_string, start_time_string, interval_string, startDate_string, endDate_string;
                medicationName_string = spinner_Value;
                interval_string = interval.getText().toString();
                startDate_string = startDate.getText().toString();
                endDate_string = endDate.getText().toString();
                start_time_string = startTime.getText().toString();
                final String reminderID = UUID.randomUUID().toString();

                if (!isSelected){
                    Snackbar.make(spinner,"Select a medication.",Snackbar.LENGTH_SHORT).show();
                }else if(!Utilities.isEditTextValid(interval)){
                    Utilities.validateEditText(interval);
                }else if (Integer.parseInt(interval.getText().toString()) == 0){
                    Snackbar.make(spinner,"Frequency must be greater than 0",Snackbar.LENGTH_SHORT).show();
                }
                else if (!Utilities.isEditTextValid(startDate)){
                    Utilities.validateEditText(startDate);
                }else if (!Utilities.isEditTextValid(endDate)){
                    Utilities.validateEditText(endDate);
                }else if (!Utilities.validateStartDate(startDate_string,endDate_string)){
                    Snackbar snackbar = Snackbar.make(endDate,"Start Date is greater than End Date",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else if (!Utilities.isEditTextValid(startTime)) {
                    Utilities.validateEditText(startTime);
                }else{


                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(@NonNull Realm realm) {
                            Reminder reminder = realm.createObject(Reminder.class, reminderID);
                            reminder.setMedicationName(medicationName_string);
                            reminder.setFrequency(Integer.valueOf(interval_string));
                            reminder.setMedicationID(selected_medication_id);
                            reminder.setStartDate(startDate_string);
                            reminder.setEndDate(endDate_string);
                            reminder.setStartTime(start_time_string);
                            Log.e(TAG, "execute: " + reminder.toString());

                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(context, "" + Integer.valueOf(interval_string), Toast.LENGTH_SHORT).show();
                            if (Integer.valueOf(interval_string) == 1){
                                Toast.makeText(ReminderActivity.this, "Here", Toast.LENGTH_SHORT).show();
                                alarmScheduler.setAlarmNow(context, startDate_string, endDate_string, start_time_string, reminderID);
                            }else{
                                Toast.makeText(ReminderActivity.this, "Bigger here", Toast.LENGTH_SHORT).show();
                                alarmScheduler.setAlarm(context, startDate_string, start_time_string, Integer.parseInt(interval_string), reminderID);

                            }
                            getAllReminders();
                            alertDialog.dismiss();

                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(@NonNull Throwable error) {
                            Log.e(TAG, "onError: ",error );
                        }
                    });

                }
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.showDatePicker(context,endDate);
            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.showDatePicker(context,startDate);
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.showTimePicker(context, startTime);
            }
        });

    }

    private void setupRecycler(@NonNull ArrayList<Reminder> reminderArrayList) {
        if(!(reminderArrayList.size() > 0)){
            Utilities.toggleVisibility(1,emptyReminderStateLayout);
            Utilities.toggleVisibility(0,reminderRecycler,addNewReminder);
        }else{
            Utilities.toggleVisibility(1,reminderRecycler,addNewReminder);
            Utilities.toggleVisibility(0,emptyReminderStateLayout);
        }

        ReminderAdapter reminderAdapter = new ReminderAdapter(this,reminderArrayList);
        reminderRecycler.setAdapter(reminderAdapter);
        reminderRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        reminderRecycler.setLayoutManager(layoutManager);
        reminderRecycler.setItemAnimator(new DefaultItemAnimator());
    }

    private void getAllReminders(){
        ArrayList<Reminder> reminders = new ArrayList<>();
        RealmQuery<Reminder> realmQuery = realm.where(Reminder.class);
        RealmResults<Reminder> realmResults = realmQuery.findAll();
        reminders.addAll(realmResults);
        persistReminders.addAll(realmResults);
        setupRecycler(reminders);
    }



}
