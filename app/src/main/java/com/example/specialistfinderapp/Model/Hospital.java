package com.example.specialistfinderapp.Model;

public class Hospital {
    public String name, address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Hospital(String name, String address){
        this.name = name;
        this.address = address;
    }
}
