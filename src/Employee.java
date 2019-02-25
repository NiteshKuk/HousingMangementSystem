package com.example.nitesh.housingmanagmentapp;

public class Employee {
    public String name;
    public String id;
    public String mkey;
    public String image;
    public String email;



    public Employee(){

    }
    public Employee(String name, String id, String email, String image, String mkey ) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.image = image;
        this.mkey = mkey;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getMkey() {
        return mkey;

    }

    public void setMkey(String mkey) {
        this.mkey = mkey;
    }



}
