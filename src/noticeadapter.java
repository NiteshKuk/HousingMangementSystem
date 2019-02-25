package com.example.nitesh.housingmanagmentapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Date;
import android.text.format.DateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class noticeadapter extends RecyclerView.Adapter<noticeadapter.ViewHolder> {
    public List<NoticeClass> user_list;
    private Context mcontext;

    public noticeadapter(List<NoticeClass> user_list,Context mcontext){
        this.user_list = user_list;
        this.mcontext=mcontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_layout, parent, false);

        return new noticeadapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final String uid=user_list.get(position).Userid;

        String desc = user_list.get(position).getNotice();
        String name = user_list.get(position).getName();
        String room = user_list.get(position).getRoom();
        holder.setName(name);
        holder.setRoom(room);
        holder.setNotice(desc);

        String millisecond=user_list.get(position).getTime().toString();
        String date = DateFormat.format("dd-MM-yyyy hh:mm:ss a",new Date(millisecond)).toString();
        holder.setTime(date);


        holder.eimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(mcontext,holder.eimage);
                popupMenu.inflate(R.menu.menu_notice_reply);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.action_add_message:
                                Intent i=new Intent(mcontext,Notice_Reply_Activity.class);
                                i.putExtra("userid",uid);
                                mcontext.startActivity(i);
                                Toasty.success(mcontext,"Reply",Toast.LENGTH_SHORT,true).show();
                                break;
                            case R.id.action_profile_next:
                              /*  Intent profile=new Intent(mcontext,UserProfile.class);
                                profile.putExtra("userid",uid);
                                mcontext.startActivity(profile);*/
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
        private TextView name;
        private TextView room;
        private ImageView eimage;
        private TextView desc;
        private TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            mview = itemView;
            eimage=(ImageView)mview.findViewById(R.id.overflow);
        }
        public void setNotice(String text) {
            desc = mview.findViewById(R.id.notice_message);
            desc.setText(text);
        }

        public void setName(String textname) {
            name = mview.findViewById(R.id.notice_name);
            name.setText(textname);
        }
        public void setRoom(String textroom){
            room=mview.findViewById(R.id.notice_room);
            room.setText(textroom);
        }
        public void setTime(String date){
            time=mview.findViewById(R.id.notice_time);
            time.setText(date);
        }
    }
}
