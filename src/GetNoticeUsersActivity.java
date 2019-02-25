package com.example.nitesh.housingmanagmentapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GetNoticeUsersActivity extends AppCompatActivity {

    private RecyclerView mrecyclerView;
    private List<NoticeClass> user_list;
    private noticeadapter messageadapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_notice_users);


        user_list = new ArrayList<>();

        mrecyclerView = (RecyclerView) findViewById(R.id.recylerview_notice);
        messageadapter = new noticeadapter(user_list, this);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(GetNoticeUsersActivity.this));
        mrecyclerView.setAdapter(messageadapter);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("NoticeBoardUser");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String uid = dataSnapshot1.getRef().getKey();
                    NoticeClass user = dataSnapshot1.getValue(NoticeClass.class).withid(uid);

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
