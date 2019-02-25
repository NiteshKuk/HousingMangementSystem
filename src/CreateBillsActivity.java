package com.example.nitesh.housingmanagmentapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.github.florent37.viewtooltip.ViewTooltip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SimpleTimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;


public class CreateBillsActivity extends AppCompatActivity {
    private CircleImageView circleImageView;
    private TextView txt;
    private TextInputLayout mroom, mbuilding, mcontact, memail, due, mlightbill, mclubhousebill, mmaintenancebill, mother, mtotal;
    private DatabaseReference databaseReference, mdatabasereference;
    private ImageButton imageButton, callendar;
    private Button btn;
    private Button register;

    int res;
    int day, month, year;
    Calendar mcurrentdate;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bills);


        String userid = getIntent().getStringExtra("userid");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("HsmApplication").child(userid);
        databaseReference.keepSynced(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.chattoolbar);
        toolbar.setBackgroundColor(Constant.color);
        setSupportActionBar(toolbar);


        txt = (TextView) findViewById(R.id.tooltext);
        circleImageView = (CircleImageView) findViewById(R.id.toolimage);
        mroom = (TextInputLayout) findViewById(R.id.textroom);
        mbuilding = (TextInputLayout) findViewById(R.id.textbuilding);
        mcontact = (TextInputLayout) findViewById(R.id.textphone);
        memail = (TextInputLayout) findViewById(R.id.textemail);
        due = (TextInputLayout) findViewById(R.id.textduedate);
        imageButton = (ImageButton) findViewById(R.id.backactivity);
        mlightbill = (TextInputLayout) findViewById(R.id.textlightbill);
        mclubhousebill = (TextInputLayout) findViewById(R.id.textclubhouse);
        mmaintenancebill = (TextInputLayout) findViewById(R.id.textmaintenance);
        mother = (TextInputLayout) findViewById(R.id.textother);
        mtotal = (TextInputLayout) findViewById(R.id.texttotal);
        callendar = (ImageButton) findViewById(R.id.calendarbutton);

        mcurrentdate=Calendar.getInstance();
        day=mcurrentdate.get(Calendar.DAY_OF_MONTH);
        month=mcurrentdate.get(Calendar.MONTH);
        year=mcurrentdate.get(Calendar.YEAR);
        month=month+1;

        btn = (Button) findViewById(R.id.buttonregisterconfirm);
        register = (Button) findViewById(R.id.buttonregister);

        setTitle(txt.getText().toString());


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateBillsActivity.this, BillsActivity.class));
            }
        });
        callendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(CreateBillsActivity.this, new
                        DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month=month+1;
                                due.getEditText().setText(dayOfMonth + "/" + month + "/" + year);
                            }
                        },year,month,day);
                datePickerDialog.show();
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlightbill.getEditText().getText().toString().isEmpty() || mclubhousebill.getEditText().getText().toString().isEmpty() || mmaintenancebill.getEditText().getText().toString().isEmpty() || mother.getEditText().getText().toString().isEmpty()) {
                    mlightbill.setError("Field Cant Be Empty");
                    mclubhousebill.setError("Field Cant Be Empty");
                    mmaintenancebill.setError("Field Cant Be Empty");
                    mother.setError("Field Cant Be Empty");
                    Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    mlightbill.setError("");
                    mclubhousebill.setError("");
                    mmaintenancebill.setError("");
                    mother.setError("");
                    int num1 = Integer.parseInt(mlightbill.getEditText().getText().toString());
                    int num2 = Integer.parseInt(mclubhousebill.getEditText().getText().toString());
                    int num3 = Integer.parseInt(mmaintenancebill.getEditText().getText().toString());
                    int num4 = Integer.parseInt(mother.getEditText().getText().toString());
                    try {
                        int res = num1 + num2 + num3 + num4;
                        mtotal.getEditText().setText(Integer.toString(res));
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Wrong = " + e, Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getApplicationContext(), "SUM = " + (num1 + num2 + num3 + num4), Toast.LENGTH_SHORT).show();
                }
            }

        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(validateroom()) | !validatecontact() | !validateemail() | !validatebuilding()
                        | !validatelightbill() | !validateclubbill() | !validatemainbill() | !validateotherbill()
                        | !validatetotal() | !validatedue()) {
                    return;
                } else {
                    String light = mlightbill.getEditText().getText().toString();
                    String club = mclubhousebill.getEditText().getText().toString();
                    String main = mmaintenancebill.getEditText().getText().toString();
                    String other = mother.getEditText().getText().toString();
                    String total = mtotal.getEditText().getText().toString();
                    String mdue = due.getEditText().getText().toString();

                    register_info(light, club, main, total, other, mdue);
                }

            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String displayname = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();


                txt.setText(displayname);
                memail.getEditText().setText(email);
                Picasso.get().load(image).placeholder(R.drawable.ic_person_outline_black_24dp).into(circleImageView);

                if (dataSnapshot.child("contact").exists()) {
                    String contact = dataSnapshot.child("contact").getValue().toString();
                    mcontact.getEditText().setText(contact);
                } else {
                    mcontact.setError("Phone No Does Not Exist");
                }


                if (dataSnapshot.child("room").exists()) {
                    String room = dataSnapshot.child("room").getValue().toString();
                    mroom.getEditText().setText(room);
                } else {
                    mroom.setError("Room No Does Not Exist");
                }


                if (dataSnapshot.child("building").exists()) {
                    String building = dataSnapshot.child("building").getValue().toString();
                    mbuilding.getEditText().setText(building);
                } else {
                    mbuilding.setError("Building No Does Not Exist");
                }


                if (dataSnapshot.child("clubhousebill").exists()) {
                    String club = dataSnapshot.child("clubhousebill").getValue().toString();
                    mclubhousebill.getEditText().setText(club);
                } else {
                }

                if (dataSnapshot.child("lightbill").exists()) {
                    String club = dataSnapshot.child("lightbill").getValue().toString();
                    mlightbill.getEditText().setText(club);
                } else {
                }

                if (dataSnapshot.child("maintenancebill").exists()) {
                    String club = dataSnapshot.child("maintenancebill").getValue().toString();
                    mmaintenancebill.getEditText().setText(club);
                } else {
                }

                if (dataSnapshot.child("othercharges").exists()) {
                    String club = dataSnapshot.child("othercharges").getValue().toString();
                    mother.getEditText().setText(club);
                } else {
                }

                if (dataSnapshot.child("totalbill").exists()) {
                    String club = dataSnapshot.child("totalbill").getValue().toString();
                    mtotal.getEditText().setText(club);
                } else {
                }
                if (dataSnapshot.child("lastduedate").exists()) {
                    String cdue = dataSnapshot.child("lastduedate").getValue().toString();
                    due.getEditText().setText(cdue);
                } else {
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void register_info(final String light, final String club, final String main, final String total, final String other, final String mdue) {

        if (mroom.getEditText().getText().toString().isEmpty() || mbuilding.getEditText().getText().toString().isEmpty()
                || mcontact.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(CreateBillsActivity.this,
                    "Cannot Be Applied Please Make Sure You Update Details \n In Your Update Member Filed Before You Process", Toast.LENGTH_SHORT).show();
        } else {
            boolean connected = false;
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                //we are connected to a network
                connected = true;
                final String userid = getIntent().getStringExtra("userid");
                final String currenttym = DateFormat.getDateTimeInstance().format(new Date());
                FirebaseDatabase.getInstance().getReference().child("HsmApplication").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> usermap = new HashMap<String, Object>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            usermap.put(snapshot.getKey(), snapshot.getValue());
                        }
                        usermap.put("lightbill", light);
                        usermap.put("clubhousebill", club);
                        usermap.put("maintenancebill", main);
                        usermap.put("totalbill", total);
                        usermap.put("othercharges", other);
                        usermap.put("lastduedate", mdue);
                        usermap.put("time", currenttym);

                        FirebaseDatabase.getInstance().getReference().child("HsmApplication").child(userid).updateChildren(usermap);
                        Toast.makeText(CreateBillsActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } else {
                Toasty.error(CreateBillsActivity.this, "Internet Not Connected ", Toast.LENGTH_LONG, true).show();
                connected = false;
            }

        }
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
        } else if (mbuilding.getCounterMaxLength() > 4) {
            mbuilding.setError("Maximum Limit exceed");
            return false;
        } else {
            mbuilding.setError(null);
            return true;
        }
    }

    private boolean validateemail() {
        String upadtevechile = memail.getEditText().getText().toString().trim();
        if ((upadtevechile.isEmpty())) {
            memail.setError("Field Cant Be Empty");
            return false;
        } else {
            memail.setError(null);
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

    private boolean validatelightbill() {
        String upadtecontact = mlightbill.getEditText().getText().toString().trim();
        if ((upadtecontact.isEmpty())) {
            mlightbill.setError("Field Cant Be Empty");
            return false;
        } else if (mlightbill.getCounterMaxLength() > 10) {
            mlightbill.setError("Maximum Limit exceed");
            return false;
        } else {
            mlightbill.setError(null);
            return true;
        }
    }

    private boolean validateclubbill() {
        String upadtecontact = mclubhousebill.getEditText().getText().toString().trim();
        if ((upadtecontact.isEmpty())) {
            mclubhousebill.setError("Field Cant Be Empty");
            return false;
        } else if (mclubhousebill.getCounterMaxLength() > 10) {
            mclubhousebill.setError("Maximum Limit exceed");
            return false;
        } else {
            mclubhousebill.setError(null);
            return true;
        }
    }

    private boolean validatemainbill() {
        String upadtecontact = mmaintenancebill.getEditText().getText().toString().trim();
        if ((upadtecontact.isEmpty())) {
            mmaintenancebill.setError("Field Cant Be Empty");
            return false;
        } else if (mmaintenancebill.getCounterMaxLength() > 10) {
            mmaintenancebill.setError("Maximum Limit exceed");
            return false;
        } else {
            mmaintenancebill.setError(null);
            return true;
        }
    }

    private boolean validateotherbill() {
        String upadtecontact = mother.getEditText().getText().toString().trim();
        if ((upadtecontact.isEmpty())) {
            mother.setError("Field Cant Be Empty");
            return false;
        } else if (mother.getCounterMaxLength() > 10) {
            mother.setError("Maximum Limit exceed");
            return false;
        } else {
            mother.setError(null);
            return true;
        }
    }

    private boolean validatetotal() {
        String upadtecontact = mtotal.getEditText().getText().toString().trim();
        if ((upadtecontact.isEmpty())) {
            mtotal.setError("Field Cant Be Empty");
            return false;
        } else if (mtotal.getCounterMaxLength() > 10) {
            mtotal.setError("Maximum Limit exceed");
            return false;
        } else {
            mtotal.setError(null);
            return true;
        }
    }

    private boolean validatedue() {
        String upadtecontact = due.getEditText().getText().toString().trim();
        if ((upadtecontact.isEmpty())) {
            due.setError("Field Cant Be Empty");
            return false;
        } else if (due.getCounterMaxLength() > 10) {
            due.setError("Maximum Limit exceed");
            return false;
        } else {
            due.setError(null);
            return true;
        }
    }

}