package com.example.specialistfinderapp;

public class Users {
    private String username, imageURL;

    //Blank Constructor
    public Users(){}


    //
    public Users(String username,String imageURL){
        this.username = username;
        this.imageURL = imageURL;
    }


    public String getusername() {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

}
