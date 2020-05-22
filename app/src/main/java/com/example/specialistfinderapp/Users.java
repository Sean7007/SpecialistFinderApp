package com.example.specialistfinderapp;

public class Users {
    private String cFName;
    private String cLName;
    private String cEmail;
    private String cPhone;

    public Users() {
    }

    public Users(String cFName, String cLName, String cEmail, String cPhone) {
        this.cFName = cFName;
        this.cLName = cLName;
        this.cEmail = cEmail;
        this.cPhone = cPhone;
    }

    public String getcFName() {
        return cFName;
    }

    public void setcFName(String cFName) {
        this.cFName = cFName;
    }

    public String getcLName() {
        return cLName;
    }

    public void setcLName(String cLName) {
        this.cLName = cLName;
    }

    public String getcEmail() {
        return cEmail;
    }

    public void setcEmail(String cEmail) {
        this.cEmail = cEmail;
    }

    public String getcPhone() {
        return cPhone;
    }

    public void setcPhone(String cPhone) {
        this.cPhone = cPhone;
    }

}