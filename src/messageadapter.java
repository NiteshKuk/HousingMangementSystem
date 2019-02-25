package com.example.nitesh.housingmanagmentapp;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class messageadapter extends RecyclerView.Adapter<messageadapter.ViewHolder> {
    public List<Users> user_list;
    private Context mcontext;



    public messageadapter(List<Users> user_list,Context mcontext) {
        this.user_list = user_list;
        this.mcontext=mcontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final String uid=user_list.get(position).Userid;

        String desc = user_list.get(position).getName();
        String image = user_list.get(position).getImage();
        String eemail = user_list.get(position).getEmail();
        String estatus=user_list.get(position).getStatus();
        holder.setEmail(eemail);
        holder.setStatus(estatus);
        holder.setImage(image);
        holder.setName(desc);

        /*holder.mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                Toast.makeText(mcontext, "Current id :---  "+uid, Toast.LENGTH_SHORT).show();
            }
        });*/


        holder.eimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(mcontext,holder.eimage);
                popupMenu.inflate(R.menu.menu_message);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.action_add_message:
                                Intent i=new Intent(mcontext,ChatingActivity.class);
                                i.putExtra("userid",uid);
                                mcontext.startActivity(i);
                                Toast.makeText(mcontext, "Message", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.action_profile_next:
                                Intent profile=new Intent(mcontext,UserProfile.class);
                                profile.putExtra("userid",uid);
                                /*holder.eimage.setTransitionName("imagetransition");
                                Pair<View,String>pair=Pair.create((View)holder.eimage,holder.eimage.getTransitionName());
                                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation((Activity) mcontext,pair);*/
                                mcontext.startActivity(profile/*options.toBundle()*/);
                                Toasty.success(mcontext,"Profile",Toast.LENGTH_SHORT,true).show();
                                break;
                                default:
                                    break;
                        }
                        return false;
                    }
                });
            popupMenu.show();
            }

        });



    }



    @Override
    public int getItemCount() {
        return user_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mview;
        private TextView desc;
        private TextView eemail;
        private ImageView eimage;
        private TextView estatus;

        public ViewHolder(View itemView) {
            super(itemView);
            mview = itemView;
            eimage=(ImageView)mview.findViewById(R.id.overflow);

        }

        public void setName(String text) {
            desc = mview.findViewById(R.id.uname);
            desc.setText(text);
        }

        public void setImage(String image) {
            CircleImageView userimage = mview.findViewById(R.id.thumbnail);
            Picasso.get().load(image).placeholder(R.drawable.ic_person_outline_black_24dp).into(userimage);
        }

        public void setEmail(String email) {
            eemail = mview.findViewById(R.id.status);
            eemail.setText(email);
        }
        public void setStatus(String status){
            estatus=mview.findViewById(R.id.mstatus);
            estatus.setText(status);
        }

    }


}
