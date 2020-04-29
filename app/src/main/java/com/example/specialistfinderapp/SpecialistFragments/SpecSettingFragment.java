package com.example.specialistfinderapp.SpecialistFragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.specialistfinderapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SpecSettingFragment extends Fragment {


    public SpecSettingFragment() {
        // Required empty public constructor
    }

    public static SpecSettingFragment newInstance() {
        SpecSettingFragment fragment = new SpecSettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spec_setting, container, false);
    }

}
