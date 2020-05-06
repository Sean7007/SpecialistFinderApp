package com.example.specialistfinderapp.Interface;

import java.util.List;

public interface IAllHospitalLoadListener {
    void onAllHospitalLoadSuccess(List<String> areaNameList);
    void onAllHospitalLoadFailed(String message);

}
