package com.example.nitesh.housingmanagmentapp;

import java.util.Date;

public class NoticeClass extends Userid {
    public String name;
    public String notice;
    public String room;
    public String time;



    public NoticeClass() {
    }

    public NoticeClass(String name, String notice, String room,String time) {

        this.name = name;
        this.notice = notice;
        this.room = room;
        this.time = time;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
