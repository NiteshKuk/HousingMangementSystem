package com.example.nitesh.housingmanagmentapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tapadoo.alerter.Alerter;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;
    private FirebaseUser mcurrentuser;
    FirebaseAuth.AuthStateListener mAuthlistener;
    private DatabaseReference muserRef;
    private DatabaseReference databaseReference;


    TextView mtextview;
    TextView mtextview1;
    ImageView mimage;

    String names[] = {"camera", "gallery", "send", "share", "slideshow"};
    CircleMenu circleMenu;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currntuser = mAuth.getCurrentUser();
        if (currntuser == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else {

        }
        mAuth.addAuthStateListener(mAuthlistener);

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Constant.color);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Constant.color);
        setSupportActionBar(toolbar);


        mAuth = FirebaseAuth.getInstance();

        muserRef = FirebaseDatabase.getInstance().getReference().child("HsmApplicationAdmin").child(mAuth.getCurrentUser().getUid());

        circleMenu = (CircleMenu) findViewById(R.id.circlemenu);
        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.drawable.add, R.drawable.remove)
                .addSubMenu(Color.parseColor("#258CFF"), R.drawable.ic_menu_camera)
                .addSubMenu(Color.parseColor("#6d4c41"), R.drawable.ic_menu_gallery)
                .addSubMenu(Color.parseColor("#ff0000"), R.drawable.ic_menu_send)
                .addSubMenu(Color.parseColor("#00a9f4"), R.drawable.ic_menu_share)
                .addSubMenu(Color.parseColor("#1a237e"), R.drawable.ic_menu_slideshow)


                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int i) {
                        switch (i) {
                            case 0:
                                Toasty.success(MainActivity.this, "Camera Click", Toast.LENGTH_SHORT, true).show();
                                // Toast.makeText(MainActivity.this, "Camera Click", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toasty.success(MainActivity.this, "Gallery Click", Toast.LENGTH_SHORT, true).show();
                                //Toast.makeText(MainActivity.this, "Gallery click", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toasty.success(MainActivity.this, "Send Click", Toast.LENGTH_SHORT, true).show();
                                //Toast.makeText(MainActivity.this, "Send click", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                ApplicationInfo api = getApplicationContext().getApplicationInfo();
                                String apkpath = api.sourceDir;
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("application/vnd.android.package-archive");
                                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(apkpath)));
                                startActivity(Intent.createChooser(intent, "Share App Using"));
                                Toasty.success(MainActivity.this, "Share Click", Toast.LENGTH_SHORT, true).show();
                                //Toast.makeText(MainActivity.this, "Share click", Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                Toasty.success(MainActivity.this, "Slideshow Click", Toast.LENGTH_SHORT, true).show();
                                //Toast.makeText(MainActivity.this, "Slideshow click", Toast.LENGTH_SHORT).show();
                                break;
                        }

                        //Toast.makeText(MainActivity.this, "You Selected --" + names[i], Toast.LENGTH_SHORT).show();
                    }
                });

        mcurrentuser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mcurrentuser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("HsmApplicationAdmin").child(current_uid);
        databaseReference.keepSynced(true);


        mAuthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            }

        };


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Chat With People", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(new Intent(MainActivity.this, MessageActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View navigationview = navigationView.getHeaderView(0);

        /*navigationview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });*/
        mtextview = (TextView) navigationview.findViewById(R.id.nav_name);
        mtextview1 = (TextView) navigationview.findViewById(R.id.nav_email);
        mimage = (CircleImageView) navigationview.findViewById(R.id.nav_image);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                mtextview1.setText(email);
                mtextview.setText(name);
                Picasso.get().load(image).placeholder(R.drawable.ic_person_outline_black_24dp).into(mimage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.memberdetails) {
            startActivity(new Intent(MainActivity.this, MemberDetails.class));
        } else if (id == R.id.nav_employers) {
            startActivity(new Intent(MainActivity.this, Employers.class));

        } else if (id == R.id.nav_update_employers) {
            startActivity(new Intent(MainActivity.this, UpdateEmployer.class));

        } else if (id == R.id.nav_board) {
            startActivity(new Intent(MainActivity.this, NoticeBoardActivity.class));

        } else if (id == R.id.nav_share) {
            ApplicationInfo api = getApplicationContext().getApplicationInfo();
            String apkpath = api.sourceDir;
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("application/vnd.android.package-archive");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(apkpath)));
            startActivity(Intent.createChooser(intent, "Share App Using"));

        } else if (id == R.id.nav_profile) {
            Intent i = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(i);

        } else if (id == R.id.logout) {
            Intent i = new Intent(MainActivity.this, LogoutActivity.class);
            startActivity(i);
        } else if (id == R.id.registermember) {
            Intent i = new Intent(MainActivity.this, RegisterMember.class);
            startActivity(i);
        } else if (id == R.id.nav_upload) {
            startActivity(new Intent(MainActivity.this, UploadFilesActivity.class));

        } else if (id == R.id.nav_notice_get) {
            startActivity(new Intent(MainActivity.this, GetNoticeUsersActivity.class));
        } else if (id == R.id.nav_payment) {
            startActivity(new Intent(MainActivity.this, PaymentActivity.class));
        } else if (id == R.id.nav_Bills) {
            startActivity(new Intent(MainActivity.this, BillsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
