package com.jeffersonantunes.ave.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfigFirebase {

    //FIREBASE
    private static FirebaseDatabase dbAve;
    private static DatabaseReference dbAveReference;
    private static FirebaseAuth auth;

    public static DatabaseReference getDbAveReference() {

        if (dbAveReference == null){

            dbAve = FirebaseDatabase.getInstance();
            dbAveReference = dbAve.getReference();

        }

        return dbAveReference;
    }

    public static FirebaseAuth getAuth(){

        if (auth == null){
            auth = FirebaseAuth.getInstance();
        }

        return auth;
    }
}
