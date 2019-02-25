package com.example.nitesh.housingmanagmentapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateEmployer extends AppCompatActivity {


    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    private FirebaseDatabase database;


    private TextView memail;
    private EditText msearchfield;
    private ImageButton msearchbtn;
    private TextView mname;
    private RecyclerView muserslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_employer);


        mDatabase = FirebaseDatabase.getInstance().getReference().child("HsmApplicationEmployee");
        muserslist = (RecyclerView) findViewById(R.id.user_list);
        muserslist.setHasFixedSize(true);
        muserslist.setLayoutManager(new LinearLayoutManager(this));


    }


//Original/////


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Employee, EmployeeHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Employee, EmployeeHolder>(

                Employee.class,
                R.layout.users_update_employer,
                EmployeeHolder.class,
                mDatabase
        ) {


            @Override
            protected void populateViewHolder(final EmployeeHolder userviewHolder, Employee users, int position) {

                userviewHolder.setName(users.getName());
                userviewHolder.setEmail(users.getEmail());
                userviewHolder.setId(users.getId());
                userviewHolder.setUserImage(users.getImage());

                final String uid = getRef(position).getKey();


                userviewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence option[] = new CharSequence[]{"Open Profile", "Update Employee", "Delete Entry"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEmployer.this);
                        builder.setTitle("Select Option");
                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (i == 0) {
                                    Toast.makeText(UpdateEmployer.this, "Open Profile Successfully", Toast.LENGTH_SHORT).show();
                                }
                                if (i == 1) {
                                    Intent profile = new Intent(UpdateEmployer.this, UpdateEmployeeDetail.class);
                                    profile.putExtra("user_id", uid);
                                    startActivity(profile);
                                }
                                if (i == 2) {

                                    DatabaseReference delete = FirebaseDatabase.getInstance().getReference().child("HsmApplicationEmployee").child(uid);
                                    delete.removeValue();
                                    startActivity(new Intent(UpdateEmployer.this, UpdateEmployer.class));
                                    Toast.makeText(UpdateEmployer.this, "Delete Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.show();

                    }

                });

            }
        };

        muserslist.setAdapter(firebaseRecyclerAdapter);

    }



    public static class EmployeeHolder extends RecyclerView.ViewHolder {
        View mview;


        public EmployeeHolder(View itemView) {
            super(itemView);
            mview = itemView;


        }



        //ORIGINALY

        public void setName(String name) {
            TextView musername = (TextView) mview.findViewById(R.id.usertext);
            musername.setText(name);

        }

        public void setEmail(String email) {
            TextView memail = (TextView) mview.findViewById(R.id.useremail);
            memail.setText(email);
        }

        public void setId(String id) {
            TextView mid = (TextView) mview.findViewById(R.id.userid);
            mid.setText(id);
        }

        public void setUserImage(String thumb_image) {
            CircleImageView userimageview = (CircleImageView) mview.findViewById(R.id.userimage);
            Picasso.get().load(thumb_image).placeholder(R.drawable.ic_person_outline_black_24dp).into(userimageview);
        }

        public void setdetails(String username, String useremail, String userid, String userimage) {
            TextView musername = (TextView) mview.findViewById(R.id.usertext);
            TextView memail = (TextView) mview.findViewById(R.id.useremail);
            TextView mid = (TextView) mview.findViewById(R.id.userid);
            CircleImageView userimageview = (CircleImageView) mview.findViewById(R.id.userimage);


            musername.setText(username);
            memail.setText(useremail);
            mid.setText(userid);
            Picasso.get().load(userimage).into(userimageview);
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
            startActivity(new Intent(UpdateEmployer.this,SettingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}



