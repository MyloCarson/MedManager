package com.mylocarson.medmanager.utils;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by user on 05/04/2018.
 */

@SuppressWarnings("ALL")
public class MedManagerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // The default Realm file is "default.realm" in Context.getFilesDir();
        // we'll change it to "med_manager.realm"
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("med_manager.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    @Override
    public void onTerminate() {
        Realm.getDefaultInstance().close();
        super.onTerminate();
    }
}
