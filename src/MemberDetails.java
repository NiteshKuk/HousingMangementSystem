package com.example.nitesh.housingmanagmentapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberDetails extends AppCompatActivity {
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
        setContentView(R.layout.activity_member_details);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("HsmApplication");
        muserslist = (RecyclerView) findViewById(R.id.user_list);
        muserslist.setHasFixedSize(true);
        muserslist.setLayoutManager(new LinearLayoutManager(this));


    }


    //Original/////


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(

                Users.class,
                R.layout.users_single_layout,
                UsersViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder userviewHolder, Users users, int position) {

                userviewHolder.setName(users.getName());
                userviewHolder.setEmail(users.getEmail());
                userviewHolder.setId(users.getId());
                userviewHolder.setUserImage(users.getThumb_image());

                final String uid = getRef(position).getKey();


                userviewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CharSequence option[]=new CharSequence[]{"Open Profile","Update Member","Delete Entry"};
                        AlertDialog.Builder builder=new AlertDialog.Builder(MemberDetails.this);
                        builder.setTitle("Select Option");
                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (i==0){
                                    Toast.makeText(MemberDetails.this, "Open Profile Successfully", Toast.LENGTH_SHORT).show();
                                }
                                if (i==1){
                                    Intent profile = new Intent(MemberDetails.this, UpdateMember.class);
                                    profile.putExtra("user_id", uid);
                                    startActivity(profile);
                                }if (i==2){
                                    DatabaseReference delete = FirebaseDatabase.getInstance().getReference().child("HsmApplication").child(uid);
                                    delete.removeValue();
                                    startActivity(new Intent(MemberDetails.this, MemberDetails.class));
                                    Toast.makeText(MemberDetails.this, "Delete Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.show();
                        /*Intent profile = new Intent(MemberDetails.this, UpdateMember.class);
                        profile.putExtra("user_id", uid);

                        startActivity(profile);*/
                    }
                });

            }
        };

        muserslist.setAdapter(firebaseRecyclerAdapter);
    }


    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        View mview;

        public UsersViewHolder(View itemView) {
            super(itemView);
            mview = itemView;
        }

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
}
