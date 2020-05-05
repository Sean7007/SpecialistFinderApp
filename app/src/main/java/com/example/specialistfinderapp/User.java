package com.example.specialistfinderapp;

public class User {
    public String cFname, cLname, cEmail, cPhone;

   //Blank Constructor
    public User(){}


    public String getcFname() {
        return cFname;
    }

    public void setcFname(String cFname) {
        this.cFname = cFname;
    }

    //
    public User(String fname, String lname, String email, String phone){
        this.cFname = fname;
        this.cLname =  lname;
        this.cEmail = email;
        this.cPhone = phone;
    }


}
