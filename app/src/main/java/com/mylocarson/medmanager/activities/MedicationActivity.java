package com.mylocarson.medmanager.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mylocarson.medmanager.R;
import com.mylocarson.medmanager.adapters.MedicationAdapter;
import com.mylocarson.medmanager.models.Medication;
import com.mylocarson.medmanager.utils.Constants;
import com.mylocarson.medmanager.utils.DrawerUtil;
import com.mylocarson.medmanager.utils.Utilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class MedicationActivity extends AppCompatActivity {

//    Views
    @BindView(R.id.toolbar)
    private
    Toolbar toolbar;
    @BindView(R.id.emptyMedicationStateLayout)
    private
    LinearLayout emptyMedicationStateLayout;
    @BindView(R.id.medRecycler)
    private
    RecyclerView medRecycler;
    @BindView(R.id.addNewMedication2)
    private Button addNewMedication2;

//
private Realm realm;
    private AlertDialog alertDialog;
    private ArrayList<Medication> persistMedications = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        DrawerUtil.getDrawer(this,toolbar);

        realm = Realm.getDefaultInstance();
        if (savedInstanceState !=null){
            this.persistMedications = savedInstanceState.getParcelableArrayList(Constants.MEDICATION_ARRAYLIST);
            setupRecycler(this.persistMedications);
        }else{
            getAllMedications();
        }


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        persistMedications = savedInstanceState.getParcelableArrayList(Constants.MEDICATION_ARRAYLIST);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(Constants.MEDICATION_ARRAYLIST,persistMedications);
        super.onSaveInstanceState(outState);
    }

    @OnClick(R.id.addNewMedication)
    void addNewMedication(){
        medicationForm(this);
    }

    @OnClick(R.id.addNewMedication2)
    void addNewMedication2(){
        medicationForm(this);
    }


    private void medicationForm(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.medication_entry, null);
        builder.setView(view);

        final EditText medicationName,description;
        Button addMed,close;
//        TextView close;

        medicationName = view.findViewById(R.id.medName);
        description = view.findViewById(R.id.medDescription);
        addMed = view.findViewById(R.id.addNewMedication);
        close = view.findViewById(R.id.close);


        addMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String medicationName_string,description_string;
                if (!Utilities.isEditTextValid(medicationName)){
                    Utilities.validateEditText(medicationName);
                }else if (!Utilities.isEditTextValid(description)) {
                    Utilities.validateEditText(description);
                }else{
                    medicationName_string = medicationName.getText().toString();
                    description_string = description.getText().toString();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(@NonNull Realm realm) {
                            Medication medication = realm.createObject(Medication.class, UUID.randomUUID().toString());
                            medication.setName(medicationName_string);
                            medication.setDescription(description_string);
                            medication.setDateCreated(new Date().toString());
                            Log.e(MainActivity.class.getSimpleName(), "execute: "+medication.toString() );
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
//                            Toast.makeText(MedicationActivity.this, "Inserted", Toast.LENGTH_SHORT).show();
                            getAllMedications();
                            alertDialog.dismiss();
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(@NonNull Throwable error) {
//                            Toast.makeText(MedicationActivity.this,"Error",Toast.LENGTH_SHORT).show();
                            Snackbar.make(medRecycler, "Error with database ", Snackbar.LENGTH_SHORT).show();
                            Log.e(MainActivity.class.getSimpleName(),"Realm Error",error);
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
                alertDialog.cancel();
            }
        });


    }

    private void setupRecycler(ArrayList<Medication> medicationArrayList){
        if (!(medicationArrayList.size() > 0)){
            Utilities.toggleVisibility(1,emptyMedicationStateLayout);
            Utilities.toggleVisibility(0,medRecycler,addNewMedication2);
        }else {
            Utilities.toggleVisibility(1,medRecycler,addNewMedication2);
            Utilities.toggleVisibility(0,emptyMedicationStateLayout);
        }
        medRecycler.setAdapter(new MedicationAdapter(this,medicationArrayList));
        medRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        medRecycler.setLayoutManager(layoutManager);
        medRecycler.setItemAnimator(new DefaultItemAnimator());
        medRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }

    private void getAllMedications(){
        ArrayList<Medication> medicationArrayList = new ArrayList<>();
        RealmQuery<Medication> medicationRealmQuery = realm.where(Medication.class);
        RealmResults<Medication> realmResults = medicationRealmQuery.findAll();
        medicationArrayList.addAll(realmResults);
        persistMedications.addAll(realmResults);
        setupRecycler(medicationArrayList);
        for (Medication medication : realmResults){
            Log.d(MainActivity.class.getSimpleName(), "getAllMedications: "+medication.toString());
        }

    }
}
