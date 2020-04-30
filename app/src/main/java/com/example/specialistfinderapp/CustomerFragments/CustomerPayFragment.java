package com.example.specialistfinderapp.CustomerFragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.specialistfinderapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerPayFragment extends Fragment {
    //Variable declaration
    Button cardPay;
    private static final String ARG_PARAM1 = "param1" ;
    private static final String ARG_PARAM2 = "param2";
    private static CustomerPayFragment INSTANCE =null;
    View view;

    //Constructor
    public CustomerPayFragment() {
        // Required empty public constructor
    }
    public static CustomerPayFragment newInstance() {
        CustomerPayFragment fragment = new CustomerPayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static CustomerPayFragment getINSTANCE(){
        if(INSTANCE == null)
            INSTANCE = new CustomerPayFragment();
        return INSTANCE;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_pay, container, false);

        //Button
        cardPay = view.findViewById(R.id.card_button);
        cardPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CardPayment.class);
                getActivity().startActivity(intent);
                return;
            }
        });
        return view;
    }

}
