package com.example.nitesh.housingmanagmentapp;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateMember extends AppCompatActivity {
    private TextView txtname, txtid;
    private DatabaseReference musersdatabase;
    private CircleImageView circleImageView;
    private TextInputLayout mroom, mbuilding, mvechile, mcontact;
    private Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_member);

        String user = getIntent().getStringExtra("user_id");


        txtid = (TextView) findViewById(R.id.updatetextid);
        txtname = (TextView) findViewById(R.id.updatetextname);
        circleImageView = (CircleImageView) findViewById(R.id.updateimage);
        mroom = (TextInputLayout) findViewById(R.id.textroom);
        mbuilding = (TextInputLayout) findViewById(R.id.textbuilding);
        mvechile = (TextInputLayout) findViewById(R.id.textvechicle);
        mcontact = (TextInputLayout) findViewById(R.id.textcontact);
        update = (Button) findViewById(R.id.update);
        musersdatabase = FirebaseDatabase.getInstance().getReference().child("HsmApplication").child(user);

        musersdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String displayname = dataSnapshot.child("name").getValue().toString();
                String id = dataSnapshot.child("id").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                txtid.setText(id);
                txtname.setText(displayname);
                Picasso.get().load(image).placeholder(R.drawable.ic_person_outline_black_24dp).into(circleImageView);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(validateroom()) | !validatecontact() | !validatevechile() | !validatebuilding()) {
                    return;
                } else {

                    String room = mroom.getEditText().getText().toString();
                    String building = mbuilding.getEditText().getText().toString();
                    String vechile = mvechile.getEditText().getText().toString();
                    String contact = mcontact.getEditText().getText().toString();

                    register_info(room, building, vechile, contact);
                }
            }
        });


    }

    private void register_info(final String room, final String building, final String vechile, final String contact) {
        final String usernew = getIntent().getStringExtra("user_id");
        FirebaseDatabase.getInstance().getReference().child("HsmApplication").child(usernew).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> usermap = new HashMap<String, Object>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    usermap.put(snapshot.getKey(), snapshot.getValue());
                }
                usermap.put("room", room);
                usermap.put("building", building);
                usermap.put("vechile", vechile);
                usermap.put("contact", contact);
                FirebaseDatabase.getInstance().getReference().child("HsmApplication").child(usernew).updateChildren(usermap);
                Toast.makeText(UpdateMember.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private boolean validateroom() {
        String upadteroom = mroom.getEditText().getText().toString().trim();
        if ((upadteroom.isEmpty())) {
            mroom.setError("Field Cant Be Empty");
            return false;
        } else if (mroom.getCounterMaxLength() > 4) {
            mroom.setError("Maximum Limit exceed");
            return false;
        } else {
            mroom.setError(null);
            return true;
        }
    }

    private boolean validatebuilding() {
        String upadtebuilding = mbuilding.getEditText().getText().toString().trim();
        if ((upadtebuilding.isEmpty())) {
            mbuilding.setError("Field Cant Be Empty");
            return false;
        } else if (mbuilding.getCounterMaxLength() > 2) {
            mbuilding.setError("Maximum Limit exceed");
            return false;
        } else {
            mbuilding.setError(null);
            return true;
        }
    }

    private boolean validatevechile() {
        String upadtevechile = mvechile.getEditText().getText().toString().trim();
        if ((upadtevechile.isEmpty())) {
            mvechile.setError("Field Cant Be Empty");
            return false;
        } else {
            mvechile.setError(null);
            return true;
        }
    }

    private boolean validatecontact() {
        String upadtecontact = mcontact.getEditText().getText().toString().trim();
        if ((upadtecontact.isEmpty())) {
            mcontact.setError("Field Cant Be Empty");
            return false;
        } else if (mcontact.getCounterMaxLength() > 10) {
            mcontact.setError("Maximum Limit exceed");
            return false;
        } else {
            mcontact.setError(null);
            return true;
        }
    }
}
