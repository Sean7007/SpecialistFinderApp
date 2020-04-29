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
public class SpecShareFragment extends Fragment {


    public SpecShareFragment() {
        // Required empty public constructor
    }

    public static SpecShareFragment newInstance() {
        SpecShareFragment fragment = new SpecShareFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spec_share, container, false);
    }

}
