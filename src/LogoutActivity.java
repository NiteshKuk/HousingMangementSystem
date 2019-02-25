package com.example.nitesh.housingmanagmentapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogoutActivity extends AppCompatActivity {
    Button logout;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthlistener;



    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthlistener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        logout = (Button) findViewById(R.id.buttonlogout);
        mAuth=FirebaseAuth.getInstance();
        mAuthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent i = new Intent(LogoutActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            }

        };
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(LogoutActivity.this, LoginActivity.class));
                Toast.makeText(LogoutActivity.this, "SignOut", Toast.LENGTH_SHORT).show();
            }

        });

    }

}
