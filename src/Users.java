package com.example.nitesh.housingmanagmentapp;

public class Users extends Userid {
    public String name;
    public String email;
    public String id;
    public String image;
    public String thumb_image;
    public String status;


    public Users(){

    }

    public Users(String name, String email, String id, String image,String thumb_image,String status) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.image = image;
        this.thumb_image=thumb_image;
        this.status=status;

    }



    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getThumb_image(){
        return thumb_image;
    }
    public void setThumb_image(String thumb_image){
        this.thumb_image=thumb_image;
    }
    public void setStatus(String status){
        this.status=status;
    }
    public String getStatus(){
        return status;
    }
}
