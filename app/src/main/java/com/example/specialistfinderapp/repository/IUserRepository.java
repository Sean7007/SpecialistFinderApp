package com.example.specialistfinderapp.repository;

import com.example.specialistfinderapp.util.FirestoreUserModel;

public interface IUserRepository {

    void doesUserEmailExist(String email);

    void addNewRegisteredUser(FirestoreUserModel firestoreUserModel);
}