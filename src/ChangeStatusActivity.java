package com.example.nitesh.housingmanagmentapp;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class ChangeStatusActivity extends AppCompatActivity {

    private TextInputLayout mstatus;
    private Button change;
    private ProgressDialog mprogress;
    //Firebase Code
    private DatabaseReference mstatusdatabase;
    private FirebaseUser mcurrentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Constant.color);
        setContentView(R.layout.activity_change_status);

        //Firebase
        mcurrentuser= FirebaseAuth.getInstance().getCurrentUser();
        String current_uid=mcurrentuser.getUid();
        mstatusdatabase= FirebaseDatabase.getInstance().getReference().child("HsmApplicationAdmin").child(current_uid);

        String status_value=getIntent().getStringExtra("status_value");


        mstatus=(TextInputLayout)findViewById(R.id.textchangestatus);
        mstatus.getEditText().setText(status_value);
        change=(Button)findViewById(R.id.confirm);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mprogress=new ProgressDialog(ChangeStatusActivity.this);
                mprogress.setTitle("Saving Changes");
                mprogress.setMessage("Please Wait While We Save the Changes");
                mprogress.show();

                String status=mstatus.getEditText().getText().toString();
                mstatusdatabase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            mprogress.dismiss();
                            //startActivity(new Intent(ChangeStatusActivity.this,UserProfileActivity.class));
                            Toast.makeText(ChangeStatusActivity.this, "Successfully Changed", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ChangeStatusActivity.this, "SomeThing Went Wrong Please Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}
