package com.example.nitesh.housingmanagmentapp;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class HousingManagementApp extends Application {
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Picasso.Builder builder=new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this,Integer.MAX_VALUE));
        Picasso built=builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
        mAuth=FirebaseAuth.getInstance();
        /*databaseReference=FirebaseDatabase.getInstance().getReference().child("HsmApplicationAdmin")
                .child(mAuth.getCurrentUser().getUid());
*/
       /* databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               *//* if (dataSnapshot !=null) {
                    databaseReference.child("online").onDisconnect().setValue(false);


                }*//*
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/
    }
}
