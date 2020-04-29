package com.example.specialistfinderapp.CustomerFragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.specialistfinderapp.R;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerHomeFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerHomeFragment1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
     Button accessMap;
    private SectionPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public CustomerHomeFragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment CustomerHomeFragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerHomeFragment1 newInstance() {
        CustomerHomeFragment1 fragment = new CustomerHomeFragment1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootView = inflater.inflate(R.layout.fragment_customer_home_fragment1, container, false);

        mSectionsPagerAdapter = new SectionPagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) rootView.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
       // tabLayout.setupWithViewPager(mViewPager);

        accessMap = rootView.findViewById(R.id.accessMapp);
        accessMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CustomerHomeFragment.class);
                getActivity().startActivity(intent);
                return;
            }
        });
        return rootView;
    }

}
