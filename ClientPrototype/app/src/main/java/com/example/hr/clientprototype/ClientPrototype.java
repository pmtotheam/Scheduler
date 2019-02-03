package com.example.hr.clientprototype;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by H$R on 10/25/2018.
 */

public class ClientPrototype extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if(!FirebaseApp.getApps(this).isEmpty()){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }
}
