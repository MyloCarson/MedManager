package com.mylocarson.medmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.mylocarson.medmanager.BuildConfig;
import com.mylocarson.medmanager.R;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int FIREBASE_UI_CODE = 1001;

    @BindView(R.id.login)
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            proceed();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED && data!=null ){
            if (requestCode == FIREBASE_UI_CODE) {
//                IdpResponse response = IdpResponse.fromResultIntent(data);

                if (resultCode == RESULT_OK) {
                    // Successfully signed in
                    proceed();
                    // ...
                } else {
                    // Sign in failed, check response for error code
                    // ...
                    Snackbar.make(login, "Login Failed",Snackbar.LENGTH_SHORT).show();
                }
            }
        }

    }

    @OnClick(R.id.login)
    void login(){
        fireBaseSignIn();
    }


    private void fireBaseSignIn(){
        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                .setAvailableProviders(Collections.singletonList(
                        new AuthUI.IdpConfig.GoogleBuilder().build()
                        )
                )
                .setLogo(R.mipmap.ic_launcher)
                .build();
        startActivityForResult(intent,FIREBASE_UI_CODE);
    }

    private void proceed(){
        Intent intent = new Intent(this,MedicationActivity.class);
        startActivity(intent);
    }
}
