package com.example.specialistfinderapp.Interface;

import com.example.specialistfinderapp.util.FirestoreUserModel;

public interface IUserRepository {

    void doesUserEmailExist(String email, FirestoreUserModel firestoreUserModel);

    void addNewRegisteredUser(FirestoreUserModel firestoreUserModel);

    void getLoginUserByEmail(String email);
}