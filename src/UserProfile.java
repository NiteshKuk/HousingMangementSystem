package com.example.nitesh.housingmanagmentapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class UserProfile extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private TextView txt1, txt2, txt3, txt4;
    private CircleImageView circleImageView;
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        String userprofile = getIntent().getStringExtra("userid");

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("HsmApplication").child(userprofile);


        txt1 = (TextView) findViewById(R.id.profiletextview);
        txt2 = (TextView) findViewById(R.id.userstatus);
        txt3 = (TextView) findViewById(R.id.userprofileemail);
        txt4 = (TextView) findViewById(R.id.usercontact);
        imageButton=(ImageButton)findViewById(R.id.back);
        circleImageView = (CircleImageView) findViewById(R.id.userprofle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.profiletoolbar);
        setSupportActionBar(toolbar);
        setTitle(txt1.getText().toString());
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfile.this,MessageActivity.class));
            }
        });



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String displayname = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                txt1.setText(displayname);
                txt3.setText(email);

                if (dataSnapshot.child("status").exists()){
                    String status = dataSnapshot.child("status").getValue().toString();
                    txt2.setText(status);
                }else{
                    txt2.setText("Status Does not Exist");
                    Toasty.error(UserProfile.this, "Status Does Not Exist" , Toast.LENGTH_SHORT, true).show();
                }


                if (dataSnapshot.child("contact").exists()) {
                    String phone = dataSnapshot.child("contact").getValue().toString();
                    txt4.setText(phone);
                }else
                {
                    txt4.setText("Phone No Does not Exist");
                    Toasty.error(UserProfile.this, "Phone Number Does Not Exist" , Toast.LENGTH_SHORT, true).show();

                }

                Picasso.get().load(image).placeholder(R.drawable.ic_person_outline_black_24dp).into(circleImageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toasty.error(UserProfile.this, "Something Went Wrong" + databaseError.getMessage(), Toast.LENGTH_LONG, true).show();
            }
        });
    }
}
