package com.example.nitesh.housingmanagmentapp;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Notice_Reply_Activity extends AppCompatActivity {
    private TextView txt, txt1, txt2, txt4,txt5,txt6;
    private DatabaseReference databaseReference;
    private DatabaseReference mdatabaseReference;
    private FirebaseAuth mAuth;
    private TextInputLayout rply;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice__reply_);

        mAuth = FirebaseAuth.getInstance();
        String userid = getIntent().getStringExtra("userid");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("NoticeBoardUser").child(userid);
        databaseReference.keepSynced(true);
        mdatabaseReference = FirebaseDatabase.getInstance().getReference().child("NoticeBoardUser").child(userid);
        databaseReference.keepSynced(true);

        txt = (TextView) findViewById(R.id.rplytext);
        txt1 = (TextView) findViewById(R.id.rplytextroom);
        txt2 = (TextView) findViewById(R.id.rplytextroommessage);
        txt4 = (TextView) findViewById(R.id.rplytexttime);
        txt5=(TextView)findViewById(R.id.rplymessage);
        txt6=(TextView)findViewById(R.id.rplymessagetime);
        rply=(TextInputLayout)findViewById(R.id.textrply);
        btn=(Button)findViewById(R.id.buttonregisterrply);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String displayname = dataSnapshot.child("name").getValue().toString();
                String room = dataSnapshot.child("room").getValue().toString();
                String notice = dataSnapshot.child("notice").getValue().toString();
                String time = dataSnapshot.child("time").getValue().toString();

                txt.setText(displayname);
                txt1.setText(room);
                txt2.setText(notice);
                txt4.setText(time);
                /*String time1 = DateFormat.format("dd-MM-yyyy hh:mm:ss a",new Date()).toString();
                txt4.setText(time1);*/

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("reply").exists()){
                    String message=dataSnapshot.child("reply").getValue().toString();
                    txt5.setText(message);

                }else{
                    txt5.setText("No Reply Found");

                }
                if (dataSnapshot.child("replytime").exists()){
                    String time=dataSnapshot.child("replytime").getValue().toString();
                    txt6.setText(time);
                }else
                {
                    txt6.setText("No Time Found");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateroom()){
                    return;
                }else{
                    String reply=rply.getEditText().getText().toString();

                    submit(reply);
                }
            }
        });


    }

    private void submit(final String reply) {
        final String userid = getIntent().getStringExtra("userid");
        final String currenttym = java.text.DateFormat.getDateTimeInstance().format(new Date());
        FirebaseDatabase.getInstance().getReference().child("NoticeBoardUser").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> usermap = new HashMap<String, Object>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    usermap.put(snapshot.getKey(), snapshot.getValue());
                }
                usermap.put("reply", reply);
                usermap.put("replytime",currenttym);
                FirebaseDatabase.getInstance().getReference().child("NoticeBoardUser").child(userid).updateChildren(usermap);
                Toast.makeText(Notice_Reply_Activity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                rply.getEditText().setText("");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private boolean validateroom() {
        String upadteroom = rply.getEditText().getText().toString().trim();
        if ((upadteroom.isEmpty())) {
            rply.setError("Field Cant Be Empty");
            return false;
        } else if (rply.getCounterMaxLength() > 250) {
            rply.setError("Maximum Limit exceed");
            return false;
        } else {
            rply.setError(null);
            return true;
        }
    }



}
