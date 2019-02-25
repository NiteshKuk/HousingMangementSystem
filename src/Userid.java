package com.example.nitesh.housingmanagmentapp;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

public class Userid {
    @Exclude
    public String Userid;
    public <T extends Userid>T withid(@NonNull final String id){
        this.Userid=id;
        return (T)this;
    }


}
