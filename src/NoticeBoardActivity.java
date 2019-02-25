package com.example.nitesh.housingmanagmentapp;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

public class NoticeBoardActivity extends AppCompatActivity {
    private TextInputLayout notice;
    private Button submit;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);
        mAuth = FirebaseAuth.getInstance();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentuser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("NoticeBoardAdmin").child(uid);
        notice = (TextInputLayout) findViewById(R.id.textid);
        submit = (Button) findViewById(R.id.noticebtn);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatenotice()) {
                    return;
                } else {
                    String edit = notice.getEditText().getText().toString();
                    noticesubmit(edit);

                }
            }
        });
    }

    private void noticesubmit(final String edit) {
        final String currenttym = DateFormat.getDateTimeInstance().format(new Date());

        databaseReference.setValue(currenttym)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        HashMap<String, String> usermap = new HashMap<>();
                        usermap.put("notice", edit);
                        usermap.put("time", currenttym);
                        databaseReference.setValue(usermap);
                        Toast.makeText(NoticeBoardActivity.this, "Successfully Created", Toast.LENGTH_SHORT).show();
                        
                    }
                });


    }

    private boolean validatenotice() {
        String id = notice.getEditText().getText().toString().trim();
        if ((id.isEmpty())) {
            notice.setError("Field Cant Be Empty");
            return false;
        } else if (notice.getCounterMaxLength() > 250) {
            notice.setError("Maximum Limit exceed");
            return false;
        } else {
            notice.setError(null);
            return true;
        }
    }
}
