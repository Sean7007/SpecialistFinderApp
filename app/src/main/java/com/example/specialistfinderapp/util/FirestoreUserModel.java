package com.example.specialistfinderapp.util;

 public class FirestoreUserModel {

    private String username;

    private String email;

    private String password;


    public FirestoreUserModel(String username, String email, String password) {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
