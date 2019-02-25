package com.example.nitesh.housingmanagmentapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class Employers extends AppCompatActivity {
    private static final Pattern Password_pattern = Pattern.compile("^" +
            "(?=.*[0-9])" +         //at least 1 digit
            "(?=.*[a-z])" +         //at least 1 lower case letter
            "(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[@#$%^&+=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{4,}" +               //at least 4 characters
            "$");


    private CircleImageView circleImageView;
    private Button imagebutton;
    private TextInputLayout eid;
    private TextInputLayout ename;
    private TextInputLayout eemail;
    private TextInputLayout epassword;
    private TextInputLayout eaddress;
    private TextInputLayout econtact;
    private TextInputLayout econtact2;
    private Button eregister;


    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employers);
        mAuth = FirebaseAuth.getInstance();


        eid = (TextInputLayout) findViewById(R.id.employeeid);
        ename = (TextInputLayout) findViewById(R.id.employeename);
        eemail = (TextInputLayout) findViewById(R.id.employeeEmail);
        epassword = (TextInputLayout) findViewById(R.id.employeepasword);
        eaddress = (TextInputLayout) findViewById(R.id.employeeaddress);
        econtact = (TextInputLayout) findViewById(R.id.employeecontact);
        econtact2 = (TextInputLayout) findViewById(R.id.employeecontact2);
        eregister = (Button) findViewById(R.id.ebuttonregister);


        eregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!valiadteid() | !valiadatename() | !valiadateaddress() | !valiadatecontact() | !valiadateconatact1() | !valiadateEmail() | !valiadatePassword()) {
                    return;
                } else {
                    String id = eid.getEditText().getText().toString();
                    String name = ename.getEditText().getText().toString();
                    String email = eemail.getEditText().getText().toString();
                    String password = epassword.getEditText().getText().toString();
                    String address = eaddress.getEditText().getText().toString();
                    String contact = econtact.getEditText().getText().toString();
                    String contact1 = econtact2.getEditText().getText().toString();

                    register_info(id, name, email, password, address, contact, contact1);
                }
            }
        });


    }

    private void register_info(final String id, final String name, final String email, final String password, final String address, final String contact, final String contact1) {


        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = currentuser.getUid();

                    databaseReference = FirebaseDatabase.getInstance().getReference().child("HsmApplicationEmployee").child(uid);
                    HashMap<String, String> usermap = new HashMap<>();
                    usermap.put("name", name);
                    usermap.put("email", email);
                    usermap.put("password", password);
                    usermap.put("id", id);
                    usermap.put("address", address);
                    usermap.put("contact", contact);
                    usermap.put("contact2", contact1);
                    usermap.put("image", "default");
                    usermap.put("thumb_image", "default");

                    databaseReference.setValue(usermap);
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "createUserWithEmail:success");
                    Toast.makeText(Employers.this, "SuccessFully Registered", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Employers.this, UpdateEmployer.class));
                }
            }

            ;
        /*FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentuser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("HsmApplicationEmployess").push().child(uid);
        HashMap<String, String> usermap = new HashMap<>();
        usermap.put("name", name);
        usermap.put("address", address);
        usermap.put("primarycontact", contact);
        usermap.put("id", id);
        usermap.put("secondarycontact", contact1);
        usermap.put("image", "default");
        usermap.put("thumb_image", "default");

        databaseReference.setValue(usermap);

        Toast.makeText(this, "Successfully Register", Toast.LENGTH_SHORT).show();*/

        });


    }

    private boolean valiadteid() {
        String id = eid.getEditText().getText().toString().trim();
        if ((id.isEmpty())) {
            eid.setError("Field Cant Be Empty");
            return false;
        } else if (eid.getCounterMaxLength() > 4) {
            eid.setError("Maximum Limit exceed");
            return false;
        } else {
            eid.setError(null);
            return true;
        }
    }

    private boolean valiadateEmail() {
        String emailinput = eemail.getEditText().getText().toString().trim();
        if (emailinput.isEmpty()) {
            eemail.setError("Field Cant Be Empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailinput).matches()) {
            eemail.setError("Please Enter a Valid Email-Id");
            return false;

        } else {
            eemail.setError(null);
            return true;
        }
    }

    private boolean valiadatePassword() {
        String passwordinput = epassword.getEditText().getText().toString().trim();
        if (passwordinput.isEmpty()) {
            epassword.setError("Field Cant Be Empty");
            return false;
        } else if (!Password_pattern.matcher(passwordinput).matches()) {
            epassword.setError(" Password To Weak....... \n \n Please use At Least 1 Digit,1 LowerCase Letter,1 UpperCase Letter,1 Special Letter,No White Spaces And AtLeast 4Char ");
            return false;
        } else {
            epassword.setError(null);
            return true;
        }
    }


    private boolean valiadatename() {
        String nameinput = ename.getEditText().getText().toString().trim();
        if (nameinput.isEmpty()) {
            ename.setError("Field Cant Be Empty");
            return false;
        } else if (ename.getCounterMaxLength() > 10) {
            ename.setError("Maximum Limit exceed");
            return false;
        } else {
            ename.setError(null);
            return true;
        }
    }

    private boolean valiadateaddress() {
        String nameinput = eaddress.getEditText().getText().toString().trim();
        if (nameinput.isEmpty()) {
            eaddress.setError("Field Cant Be Empty");
            return false;
        } else {
            eaddress.setError(null);
            return true;
        }
    }

    private boolean valiadatecontact() {
        String nameinput = econtact.getEditText().getText().toString().trim();
        if (nameinput.isEmpty()) {
            econtact.setError("Field Cant Be Empty");
            return false;
        } else {
            econtact.setError(null);
            return true;
        }
    }

    private boolean valiadateconatact1() {
        String nameinput = econtact2.getEditText().getText().toString().trim();
        if (nameinput.isEmpty()) {
            econtact2.setError("Field Cant Be Empty");
            return false;
        } else {
            econtact2.setError(null);
            return true;
        }
    }


}
