package com.example.specialistfinderapp.Interface;

import com.example.specialistfinderapp.Model.Hospital;

import java.util.List;

public interface IBranchLoadListener {
    void onBranchLoadSuccess(List<Hospital> hospitalListList);
    void onBranchLoadFailed(String message);
}
