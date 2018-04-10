package com.mylocarson.medmanager.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.mylocarson.medmanager.R;
import com.mylocarson.medmanager.utils.DrawerUtil;
import com.mylocarson.medmanager.utils.Utilities;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.email)
    private
    EditText email;

    @BindView(R.id.name)
    private
    EditText name;

    @BindView(R.id.toolbar)
    private
    Toolbar toolbar;

    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private AlertDialog alertDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        DrawerUtil.getDrawer(this, toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        alertDialog = loadingAlert();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();

            assert name != null;
            assert email != null;
            setViews(name, email);


            // Check if user's email is verified
            if (!user.isEmailVerified()) {
                showVerifyAlert(email).show();
            }

        }
    }

    @OnClick(R.id.updateProfile)
    void updateProfile() {
        if (!Utilities.isEditTextValid(name)) {
            Utilities.validateEditText(name);
        } else if (!Utilities.isEditTextValid(email)) {
            Utilities.validateEditText(email);
        } else {
            alertDialog.show();
            updateProfile(name.getText().toString(), email.getText().toString());
        }
    }

    @OnClick(R.id.signOut)
    void signout() {
        firebaseAuth.signOut();
        proceed();
    }

    @OnClick(R.id.editName)
    void editName() {
        name.setFocusable(true);
        name.setEnabled(true);
        name.setInputType(InputType.TYPE_CLASS_TEXT);
        name.setFocusableInTouchMode(true);
        name.setClickable(true);
    }

    @OnClick(R.id.editEmail)
    void editEmail() {
        email.setFocusable(true);
        email.setEnabled(true);
        email.setInputType(InputType.TYPE_CLASS_TEXT);
        email.setFocusableInTouchMode(true);
        email.setClickable(true);
    }

    private AlertDialog showVerifyAlert(String email) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("MedManager");
        builder.setMessage("Kindly verify your email address (" + email + ")");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                verifyEmail();
                dialogInterface.dismiss();
            }
        });

        return builder.create();
    }

    private void setViews(@NonNull String name, @NonNull String email) {
        this.email.setText(email);
        this.name.setText(name);
    }

    private void verifyEmail() {
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Snackbar.make(name, "Email Sent", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void proceed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void updateProfile(String displayname, final String email) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayname)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            updateEmail(email);
                        } else {
                            Snackbar.make(name, "Profile Updated Failed.", Snackbar.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void updateEmail(String email) {
        user.updateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        alertDialog.dismiss();
                        if (task.isSuccessful()) {

                            Snackbar.make(name, "Profile Updated Successfully.", Snackbar.LENGTH_SHORT).show();

                        } else {
                            Snackbar.make(name, "Profile Updated Failed.", Snackbar.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private AlertDialog loadingAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Updating ....");
        return builder.create();
    }



}
