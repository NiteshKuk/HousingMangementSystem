package com.example.nitesh.housingmanagmentapp;

import android.app.ProgressDialog;
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

public class ChangeNameActivity extends AppCompatActivity {


    private TextInputLayout mname;
    private Button change;
    private ProgressDialog mprogress;
    //Firebase Code
    private DatabaseReference mstatusdatabase;
    private FirebaseUser mcurrentuser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Constant.color);
        setContentView(R.layout.activity_change_name);


        //Firebase
        mcurrentuser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mcurrentuser.getUid();
        mstatusdatabase = FirebaseDatabase.getInstance().getReference().child("HsmApplicationAdmin").child(current_uid);

        String status_value = getIntent().getStringExtra("name_value");


        mname = (TextInputLayout) findViewById(R.id.textchangestatus);
        mname.getEditText().setText(status_value);
        change = (Button) findViewById(R.id.confirm);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mprogress = new ProgressDialog(ChangeNameActivity.this);
                mprogress.setTitle("Saving Changes");
                mprogress.setMessage("Please Wait While We Save the Changes");
                mprogress.show();

                String status = mname.getEditText().getText().toString();
                mstatusdatabase.child("name").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mprogress.dismiss();
                            //startActivity(new Intent(ChangeStatusActivity.this,UserProfileActivity.class));
                            Toast.makeText(ChangeNameActivity.this, "Successfully Changed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ChangeNameActivity.this, "SomeThing Went Wrong Please Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

}

