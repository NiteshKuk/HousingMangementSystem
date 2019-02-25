package com.example.nitesh.housingmanagmentapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {

    private List<Messages>usermessageslist;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    public ChatAdapter(List<Messages>usermessageslist){
        this.usermessageslist=usermessageslist;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        public TextView senderMessagetext,receivermessagetext;
        public CircleImageView circleImageView;

        public MessageViewHolder(View itemView) {
            super(itemView);
            senderMessagetext=(TextView)itemView.findViewById(R.id.message_sender);
            receivermessagetext=(TextView)itemView.findViewById(R.id.message_receiver);
            circleImageView=(CircleImageView) itemView.findViewById(R.id.messageimage);
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custome_message_layout,parent,false);
        mAuth=FirebaseAuth.getInstance();
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, int position) {

        String messagesenderid=mAuth.getCurrentUser().getUid();
        Messages messages=usermessageslist.get(position);

        String fromuserid=messages.getFrom();
        String frommessagetype=messages.getType();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Message").child(fromuserid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("image")){
                    String reciverimage=dataSnapshot.child("image").getValue().toString();
                    Picasso.get().load(reciverimage).placeholder(R.drawable.ic_person_outline_black_24dp).into(holder.circleImageView);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (frommessagetype.equals("text")){
            holder.receivermessagetext.setVisibility(View.INVISIBLE);
            holder.circleImageView.setVisibility(View.INVISIBLE);
            holder.senderMessagetext.setVisibility(View.INVISIBLE);
        }
        if (fromuserid.equals(messagesenderid)){
            holder.senderMessagetext.setVisibility(View.VISIBLE);
            holder.senderMessagetext.setBackgroundResource(R.drawable.sender_messages_layout);
            holder.senderMessagetext.setText(messages.getMessage());
        }
        else
        {

            holder.circleImageView.setVisibility(View.VISIBLE);
            holder.receivermessagetext.setVisibility(View.VISIBLE);

            holder.receivermessagetext.setBackgroundResource(R.drawable.receiver_message_layout);
            holder.receivermessagetext.setText(messages.getMessage());

        }

    }

    @Override
    public int getItemCount() {
        return usermessageslist.size();
    }


}
