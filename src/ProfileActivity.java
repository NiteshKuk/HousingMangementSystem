package com.example.nitesh.housingmanagmentapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthlistener;
    private DatabaseReference databaseReference;
    private StorageReference mimagereference;
    private FirebaseUser mcurrentuser;
    TextView textname, textemail, textstatus;
    Button statusbutton;
    Button ibutton;
    Button namebutton;
    CircleImageView circleImageView;

    private static final int GALLERY_PICK = 1;

    private ProgressDialog mprogressdialog;

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthlistener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        textname = (TextView) findViewById(R.id.displayname);
        textemail = (TextView) findViewById(R.id.displayemail);
        textstatus = (TextView) findViewById(R.id.displaystatus);
        circleImageView = (CircleImageView) findViewById(R.id.profile_image);

        mAuth = FirebaseAuth.getInstance();
        mimagereference = FirebaseStorage.getInstance().getReference();
        mAuthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            }
        };
        mcurrentuser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mcurrentuser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("HsmApplicationAdmin").child(current_uid);
        databaseReference.keepSynced(true);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status = dataSnapshot.child("status").getValue().toString();
                String name=dataSnapshot.child("name").getValue().toString();
                String email=dataSnapshot.child("email").getValue().toString();
                final String image=dataSnapshot.child("image").getValue().toString();
                textemail.setText(email);
                textname.setText(name);
                textstatus.setText(status);
                if (!image.equals("default")){
                    Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.ic_person_outline_black_24dp).into(circleImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(image).placeholder(R.drawable.ic_person_outline_black_24dp).into(circleImageView);

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        statusbutton = (Button) findViewById(R.id.changeadminstatus);
        statusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status_value = textstatus.getText().toString();
                Intent i = new Intent(ProfileActivity.this, ChangeStatusActivity.class);
                i.putExtra("status_value", status_value);
                startActivity(i);
            }
        });
        namebutton=(Button)findViewById(R.id.changeadminname);
        namebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_value=textname.getText().toString();
                Intent i=new Intent(ProfileActivity.this,ChangeNameActivity.class);
                i.putExtra("name_value",name_value);
                startActivity(i);
            }
        });
        ibutton=(Button)findViewById(R.id.imagebutton);
        ibutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery_intent = new Intent();
                gallery_intent.setAction(Intent.ACTION_GET_CONTENT);
                gallery_intent.setType("image/*");
                startActivityForResult(Intent.createChooser(gallery_intent, "Select Image"), GALLERY_PICK);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data!=null) {
            Uri imageuri = data.getData();

            CropImage.activity(imageuri)
                    .setAspectRatio(1, 1)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mprogressdialog = new ProgressDialog(ProfileActivity.this);
                mprogressdialog.setTitle("Uploading Image");
                mprogressdialog.setMessage("Please Wait Until we Upload and Process the Image");
                mprogressdialog.setCanceledOnTouchOutside(false);
                mprogressdialog.show();



                Uri resultUri = result.getUri();
                File thumb_filepath=new File(resultUri.getPath());



                String current_user_uid = mcurrentuser.getUid();


                Bitmap thumb_bitmap = new Compressor(this)


                        .setMaxHeight(200)
                        .setQuality(75)
                        .compressToBitmap(thumb_filepath);


                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                final byte[]thumb_byte=baos.toByteArray();

                StorageReference filepath = mimagereference.child("profile_images_admin").child(current_user_uid + ".jpg");
                final StorageReference thumb_imagefilepath = mimagereference.child("profile_images_admin").child("thumbs").child(current_user_uid + ".jpg");




                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String download_uri = task.getResult().getDownloadUrl().toString();


                            UploadTask uploadTask=thumb_imagefilepath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                                    String thumb_downloaduri=thumb_task.getResult().getDownloadUrl().toString();

                                    if (thumb_task.isSuccessful()){

                                        Map updatehashmap=new HashMap();
                                        updatehashmap.put("image",download_uri);
                                        updatehashmap.put("thumb_image",thumb_downloaduri);

                                        databaseReference.updateChildren(updatehashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    mprogressdialog.dismiss();
                                                    Toast.makeText(ProfileActivity.this, "SuccessFully Uploaded ", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(ProfileActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                                    mprogressdialog.dismiss();
                                                }
                                            }
                                        });
                                    }


                                }
                            });



                        } else {
                            Toast.makeText(ProfileActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            mprogressdialog.dismiss();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }
    }


}
