package com.example.nitesh.housingmanagmentapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatingActivity extends AppCompatActivity {

    private TextView text;
    private TextView txt;
    private DatabaseReference databaseReference;
    private DatabaseReference mdatabaseReference;
    private TextView mtooltext;
    private CircleImageView circleImageView;
    private Dialog mydialog;
    private EditText editText;
    private ImageButton imageButton;

    private final List<Messages>messagesList=new ArrayList<>();
    private ChatAdapter messageadapter;
    private RecyclerView recyclerView;

    private FirebaseAuth mAuth;
    private String senderid;

    @Override
    protected void onStart() {
        super.onStart();
        String userid = getIntent().getStringExtra("userid");
        mdatabaseReference.child("Message").child(senderid).child(userid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Messages messages=dataSnapshot.getValue(Messages.class);
                messagesList.add(messages);
                messageadapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Constant.color);
        setContentView(R.layout.activity_chating);

        mAuth=FirebaseAuth.getInstance();
        senderid=mAuth.getCurrentUser().getUid();

        String userid = getIntent().getStringExtra("userid");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("HsmApplication").child(userid);
        databaseReference.keepSynced(true);
        mdatabaseReference=FirebaseDatabase.getInstance().getReference();
        mdatabaseReference.keepSynced(true);


        txt = (TextView) findViewById(R.id.toolstatus);
        editText = (EditText) findViewById(R.id.messagetext);
        imageButton = (ImageButton) findViewById(R.id.sendmessage);
        mtooltext = (TextView) findViewById(R.id.tooltext);
        circleImageView = (CircleImageView) findViewById(R.id.toolimage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.chattoolbar);
        toolbar.setBackgroundColor(Constant.color);
        setSupportActionBar(toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatingActivity.this, "Toolbar Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        setTitle(mtooltext.getText().toString());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        messageadapter=new ChatAdapter(messagesList);
        recyclerView=(RecyclerView)findViewById(R.id.chatrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageadapter);



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String displayname = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                txt.setText(status);
                mtooltext.setText(displayname);
                Picasso.get().load(image).placeholder(R.drawable.ic_person_outline_black_24dp).into(circleImageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(ChatingActivity.this, "Circle clicked", Toast.LENGTH_SHORT).show();
            }
        });

        final EditText imageclick = (EditText) findViewById(R.id.messagetext);
        imageclick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() <= imageclick.getTotalPaddingLeft()) {
                        // your action for drawable click event
                        Toast.makeText(ChatingActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                return false;
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmessage();
            }
        });
    }

    private void sendmessage() {
        String userid = getIntent().getStringExtra("userid");
        String messagetext = editText.getText().toString();



        if (TextUtils.isEmpty(messagetext)) {
            Toast.makeText(this, "Please Write The Message First To send", Toast.LENGTH_SHORT).show();
        } else {
            String messagesenderref="Message/" +senderid+"/"+ userid;
            String messagerecieverref="Message/" +userid+"/"+senderid;
            DatabaseReference usermessagekey=mdatabaseReference.child("Messages").child(senderid).child(userid).push();

            String messagepushid=usermessagekey.getKey();
            Map messagetextbody=new HashMap();
            messagetextbody.put("message",messagetext);
            messagetextbody.put("type","text");
            messagetextbody.put("from",senderid);

            Map messagebodydetails=new HashMap();
            messagebodydetails.put(messagesenderref+ "/" +messagepushid,messagetextbody);
            messagebodydetails.put(messagerecieverref+ "/" +messagepushid,messagetextbody);

            mdatabaseReference.updateChildren(messagebodydetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ChatingActivity.this, "Message Sent Successfully", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        String error=task.getException().getMessage();
                        Toast.makeText(ChatingActivity.this, "Error:  "  +  error, Toast.LENGTH_SHORT).show();
                    }
                    editText.setText("");
                }
            });


        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
