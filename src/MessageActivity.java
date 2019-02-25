package com.example.nitesh.housingmanagmentapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    private RecyclerView mrecyclerView;
    private List<Users> user_list;
    private messageadapter messageadapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        user_list = new ArrayList<>();

        mrecyclerView = (RecyclerView) findViewById(R.id.recylerview);
        messageadapter = new messageadapter(user_list, this);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
        mrecyclerView.setAdapter(messageadapter);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("HsmApplication");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String uid = dataSnapshot1.getRef().getKey();
                    Users user = dataSnapshot1.getValue(Users.class).withid(uid);

                    user_list.add(user);
                    messageadapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}