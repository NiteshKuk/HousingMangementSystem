package com.example.nitesh.housingmanagmentapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class UploadFilesActivity extends AppCompatActivity {
    private Button mselectbtn;
    private Button mcancelbtn;
    private Button mpausebtn;
    private TextView mfilenamelabel;
    private ProgressBar mProgress;
    private TextView mSizelabel;
    private TextView mProgresslabel;


    private final static int FILE_SELECT_CODE = 1;
    private StorageReference mStorageRef;
    private StorageTask mstoragetask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_files);

        mselectbtn = (Button) findViewById(R.id.fileuploadbtn);
        mcancelbtn = (Button) findViewById(R.id.cancelbtn);
        mpausebtn = (Button) findViewById(R.id.pausebtn);
        mfilenamelabel = (TextView) findViewById(R.id.filetype);
        mProgress = (ProgressBar) findViewById(R.id.upload_progress);
        mSizelabel = (TextView) findViewById(R.id.counter);
        mProgresslabel = (TextView) findViewById(R.id.counter1);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mselectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileselector();
            }
        });
        mpausebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btntext = mpausebtn.getText().toString();
                if (btntext.equals("Pause Upload")) {
                    try {
                        mstoragetask.pause();
                        mpausebtn.setText("Resume Upload");

                    } catch (Exception ex) {
                        Toast.makeText(UploadFilesActivity.this, "Please Select File First", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    mstoragetask.resume();
                    mpausebtn.setText("Pause Upload");
                }
            }
        });
        mcancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mstoragetask.cancel();
                } catch (Exception ex) {
                    Toast.makeText(UploadFilesActivity.this, "Please Select File First", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void openFileselector() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File To Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please Install a File Manger", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK) {
            Uri fileuri = data.getData();


            String uriString = fileuri.toString();
            File myFile = new File(uriString);

            // String path =myFile.getAbsolutePath();

            String displayname = null;
            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = UploadFilesActivity.this.getContentResolver().query(fileuri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayname = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayname = myFile.getName();
            }
            mfilenamelabel.setText(displayname);

            StorageReference riversRef = mStorageRef.child("files_upload/" + displayname);

            mstoragetask = riversRef.putFile(fileuri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Toast.makeText(UploadFilesActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            if (mcancelbtn.callOnClick()) {
                                Toast.makeText(UploadFilesActivity.this, "SuccessFully Cancelled", Toast.LENGTH_SHORT).show();
                                mProgress.setProgress(0);
                            } else {
                                Toast.makeText(UploadFilesActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }
                            // Handle unsuccessful uploads
                            // ...
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            mProgress.setProgress((int) progress);

                            String progressText = taskSnapshot.getBytesTransferred() / (1024 * 1024) + "/" + taskSnapshot.getTotalByteCount() / (1024 * 1024) + "mb";
                            mSizelabel.setText(progressText);


                        }
                    });


        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
