package com.example.specialistfinderapp.Genysis;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.specialistfinderapp.CustomerFragments.CustomerAppointmentFragment;
import com.example.specialistfinderapp.CustomerFragments.CustomerChatFragment;
import com.example.specialistfinderapp.CustomerFragments.CustomerHomeFragment1;
import com.example.specialistfinderapp.CustomerFragments.CustomerPayFragment;
import com.example.specialistfinderapp.CustomerFragments.CustomerUserFragment;
import com.example.specialistfinderapp.R;
import com.example.specialistfinderapp.Users;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Customer_Home extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    FirebaseUser firebaseUser;//Fire-base User

    DatabaseReference reference;
    TextView cFname, username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        //===========================================CHAT ==========================//
        Toolbar toolbar1 = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setTitle("");


        username = findViewById(R.id.username);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                    assert user != null;
                 //   username.setText(user.getcEmail());
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        //TabLayout
        TabLayout tabLayout =  findViewById(R.id.tabLayout_id);
        ViewPager viewPager =  findViewById(R.id.viewPager_id);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setupWithViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_appointment);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_chat);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_person_black_24dp);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_payment_black_24dp);

        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.add(R.id.fragment_customer_home_fragment1,home,"home");
        fragmentTransaction.commit();


        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(3).getIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(4).getIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void showUpdateDialog(String name) {

    }

    private void setupWithViewPager(ViewPager viewPager) {
        SectionPageAdapter sectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        sectionPageAdapter.addFragment(CustomerHomeFragment1.newInstance(),"Home");
        sectionPageAdapter.addFragment(CustomerAppointmentFragment.newInstance(), "Booking");
        sectionPageAdapter.addFragment(CustomerChatFragment.newInstance(), "Chat");
        sectionPageAdapter.addFragment(CustomerUserFragment.newInstance(), "Users");
        sectionPageAdapter.addFragment(CustomerPayFragment.newInstance(), "Payment");
        viewPager.setAdapter(sectionPageAdapter);
    }


    private class SectionPageAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentString = new ArrayList<>();
        public SectionPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        void addFragment(Fragment fm, String title){
            fragmentList.add(fm);
            fragmentString.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position){
            return fragmentString.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mini_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Customer_Home.this, CustomerLogin.class));
                finish();
                return true;
        }
        return false;
    }
}
