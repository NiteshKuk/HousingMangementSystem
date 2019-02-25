package com.example.nitesh.housingmanagmentapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static com.google.firebase.firestore.FieldValue.delete;

public class UpdateEmployeeDetail extends AppCompatActivity {
    private TextView txtname, txtid;
    private DatabaseReference musersdatabase;
    private CircleImageView circleImageView;
    private TextView mbuilding, mcontact;
   /* private Button update;*/
    private Button image;


    private static final int GALLERY_PICK = 1;
    private ProgressDialog mprogressdialog;


    private FirebaseUser mcurrentuser;
    private StorageReference mimagereference;
    private FirebaseStorage mstorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_employee_detail);


        final String user = getIntent().getStringExtra("user_id");
        mimagereference = FirebaseStorage.getInstance().getReference();

        txtid = (TextView) findViewById(R.id.updatetextid);
        txtname = (TextView) findViewById(R.id.updatetextname);
        mbuilding = (TextView) findViewById(R.id.updatebuildingid);
        mcontact = (TextView) findViewById(R.id.updatecontactid);
        //update = (Button) findViewById(R.id.update);
        circleImageView = (CircleImageView) findViewById(R.id.updateimage);
        image = (Button) findViewById(R.id.employeebutton);



        mstorage = FirebaseStorage.getInstance();
        musersdatabase = FirebaseDatabase.getInstance().getReference().child("HsmApplicationEmployee").child(user);


        musersdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String displayname = dataSnapshot.child("name").getValue().toString();
                String id = dataSnapshot.child("id").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String building = dataSnapshot.child("address").getValue().toString();
                String contact = dataSnapshot.child("contact").getValue().toString();
                mbuilding.setText(building);
                mcontact.setText(contact);
                txtid.setText(id);
                txtname.setText(displayname);
                if (!image.equals("default")) {
                    Picasso.get().load(image).placeholder(R.drawable.ic_person_outline_black_24dp).into(circleImageView);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery_intent = new Intent();
                gallery_intent.setAction(Intent.ACTION_GET_CONTENT);
                gallery_intent.setType("image/*");
                startActivityForResult(Intent.createChooser(gallery_intent, "Select Image"), GALLERY_PICK);

            }
        });
        /*update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                delete_info();
                }catch (Exception ex){
                    Toast.makeText(UpdateEmployeeDetail.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                }
            }
        });*/

    }

    /*private void delete_info() {
        try {
            final String usernew = getIntent().getStringExtra("user_id");
            DatabaseReference delete = FirebaseDatabase.getInstance().getReference().child("HsmApplicationEmployee").child(usernew);
            delete.removeValue();
            startActivity(new Intent(UpdateEmployeeDetail.this, UpdateEmployer.class));
            Toast.makeText(this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null) {
            Uri imageuri = data.getData();

            CropImage.activity(imageuri)
                    .setAspectRatio(1, 1)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mprogressdialog = new ProgressDialog(UpdateEmployeeDetail.this);
                mprogressdialog.setTitle("Uploading Image");
                mprogressdialog.setMessage("Please Wait Until we Upload and Process the Image");
                mprogressdialog.setCanceledOnTouchOutside(false);
                mprogressdialog.show();


                Uri resultUri = result.getUri();
                File thumb_filepath = new File(resultUri.getPath());


                String current_user_uid = getIntent().getStringExtra("user_id");

                Bitmap thumb_bitmap = new Compressor(this)


                        .setMaxHeight(200)
                        .setQuality(75)
                        .compressToBitmap(thumb_filepath);


                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();

                StorageReference filepath = mimagereference.child("profile_images_employee").child(current_user_uid + ".jpg");
                final StorageReference thumb_imagefilepath = mimagereference.child("profile_images_employee").child("thumbs").child(current_user_uid + ".jpg");


                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String download_uri = task.getResult().getDownloadUrl().toString();


                            UploadTask uploadTask = thumb_imagefilepath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                                    String thumb_downloaduri = thumb_task.getResult().getDownloadUrl().toString();

                                    if (thumb_task.isSuccessful()) {

                                        Map updatehashmap = new HashMap();
                                        updatehashmap.put("image", download_uri);
                                        updatehashmap.put("thumb_image", thumb_downloaduri);

                                        musersdatabase.updateChildren(updatehashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    mprogressdialog.dismiss();
                                                    Toast.makeText(UpdateEmployeeDetail.this, "SuccessFully Uploaded ", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(UpdateEmployeeDetail.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                                    mprogressdialog.dismiss();
                                                }
                                            }
                                        });
                                    }


                                }
                            });


                        } else {
                            Toast.makeText(UpdateEmployeeDetail.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
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
