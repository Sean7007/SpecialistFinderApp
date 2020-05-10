package com.example.specialistfinderapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Hospital implements Parcelable {
    public String name, address,websites, phone, openHours, hospitalId;

    //Constructor
    public Hospital() {
    }

    protected Hospital(Parcel in) {
        name = in.readString();
        address = in.readString();
        websites = in.readString();
        phone = in.readString();
        openHours = in.readString();
        hospitalId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(websites);
        dest.writeString(phone);
        dest.writeString(openHours);
        dest.writeString(hospitalId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Hospital> CREATOR = new Creator<Hospital>() {
        @Override
        public Hospital createFromParcel(Parcel in) {
            return new Hospital(in);
        }

        @Override
        public Hospital[] newArray(int size) {
            return new Hospital[size];
        }
    };

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

    public String getWebsites() {
        return websites;
    }

    public void setWebsites(String websites) {
        this.websites = websites;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }
}
