package com.example.nitesh.housingmanagmentapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class RegisterMember extends AppCompatActivity {
    private static final Pattern Password_pattern = Pattern.compile("^" +
            "(?=.*[0-9])" +         //at least 1 digit
            "(?=.*[a-z])" +         //at least 1 lower case letter
            "(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[@#$%^&+=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{4,}" +               //at least 4 characters
            "$");


    //Android Activity
    private TextInputLayout mid, mname, memail, mpassword;
    private Button mbutton;


    //Firebase Console
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_member);
        mAuth = FirebaseAuth.getInstance();

        mid = (TextInputLayout) findViewById(R.id.textid);
        mname = (TextInputLayout) findViewById(R.id.textname);
        memail = (TextInputLayout) findViewById(R.id.textemail);
        mpassword = (TextInputLayout) findViewById(R.id.textpassword);
        mbutton = (Button) findViewById(R.id.buttonregister);

        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!valiadteid() | !validateemail() | !valiadatename() | !validatepassword()) {
                    return;
                } else {
                    String id = mid.getEditText().getText().toString();
                    String email = memail.getEditText().getText().toString();
                    String name = mname.getEditText().getText().toString();
                    String password = mpassword.getEditText().getText().toString();

                    register_info(id, email, name, password);
                }
            }
        });

    }

    private void register_info(final String id, final String email, final String name, final String password) {


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = currentuser.getUid();

                            mDatabase = FirebaseDatabase.getInstance().getReference().child("HsmApplication").child(uid);
                            HashMap<String, String> usermap = new HashMap<>();
                            usermap.put("name", name);
                            usermap.put("email", email);
                            usermap.put("password", password);
                            usermap.put("id", id);
                            usermap.put("status", "default");
                            usermap.put("image", "default");
                            usermap.put("thumb_image", "default");

                            mDatabase.setValue(usermap);
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            Toast.makeText(RegisterMember.this, "SuccessFully Registered", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(RegisterMember.this, MemberDetails.class);
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            String error=task.getException().getMessage();
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterMember.this, "Authentication failed." + error,
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    private boolean valiadteid() {
        String id = mid.getEditText().getText().toString().trim();
        if ((id.isEmpty())) {
            mid.setError("Field Cant Be Empty");
            return false;
        } else if (mid.getCounterMaxLength() > 4) {
            mid.setError("Maximum Limit exceed");
            return false;
        } else {
            mid.setError(null);
            return true;
        }
    }

    private boolean validateemail() {
        String emailinput = memail.getEditText().getText().toString().trim();
        if (emailinput.isEmpty()) {
            memail.setError("Field Cant Be Empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailinput).matches()) {
            memail.setError("Please Enter a Valid Email-Id");
            return false;

        } else {
            memail.setError(null);
            return true;
        }
    }

    private boolean valiadatename() {
        String nameinput = mname.getEditText().getText().toString().trim();
        if (nameinput.isEmpty()) {
            mname.setError("Field Cant Be Empty");
            return false;
        } else {
            mname.setError(null);
            return true;
        }
    }

    private boolean validatepassword() {
        String passwordinput = mpassword.getEditText().getText().toString().trim();
        if (passwordinput.isEmpty()) {
            mpassword.setError("Field Cant Be Empty");
            return false;
        } else if (!Password_pattern.matcher(passwordinput).matches()) {
            mpassword.setError(" Password To Weak....... \n \n Please use At Least 1 Digit,1 LowerCase Letter,1 UpperCase Letter,1 Special Letter,No White Spaces And AtLeast 4Char ");
            return false;
        } else {
            mpassword.setError(null);
            return true;
        }
    }

}
