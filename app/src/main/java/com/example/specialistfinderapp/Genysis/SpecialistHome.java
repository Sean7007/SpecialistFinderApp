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

import com.bumptech.glide.Glide;
import com.example.specialistfinderapp.R;
import com.example.specialistfinderapp.SpecialistFragments.SpecApointmentFragment;
import com.example.specialistfinderapp.SpecialistFragments.SpecChatFragment;
import com.example.specialistfinderapp.SpecialistFragments.SpecSettingFragment;
import com.example.specialistfinderapp.SpecialistFragments.SpecShareFragment;
import com.example.specialistfinderapp.SpecialistFragments.SpecialistFragmentHome1;
import com.example.specialistfinderapp.User;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class SpecialistHome extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    FirebaseUser firebaseUser;//Fire-base User
    DatabaseReference reference;
    CircleImageView profile_image;
    TextView cFname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialist_home);
        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        //TabLayout
        TabLayout tabLayout =  findViewById(R.id.tabLayout_id);
        ViewPager viewPager =  findViewById(R.id.viewPager_id);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        profile_image = findViewById(R.id.profile_Image);
        cFname  = findViewById(R.id.cFname);

        /*Toolbar toolbar1 = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setTitle("");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child("Specialist").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                cFname.setText(user.getcFname());
                if(user.getImageURL().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Glide.with(SpecialistHome.this).load(user.getImageURL()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Checks if user is null
        if (firebaseUser != null) {
            Intent intent = new Intent(SpecialistHome.this, SpecialistHome.class);
            startActivity(intent);
            finish();
        }*/


        setupWithViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_appointment);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_chat);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_settings);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_share);

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

    private void setupWithViewPager(ViewPager viewPager) {
        SectionPageAdapterr sectionPageAdapter = new SectionPageAdapterr(getSupportFragmentManager());
        sectionPageAdapter.addFragment(SpecialistFragmentHome1.newInstance(),"Home");
        sectionPageAdapter.addFragment(SpecApointmentFragment.newInstance(), "Appointment");
        sectionPageAdapter.addFragment(SpecChatFragment.newInstance(), "Chat");
        sectionPageAdapter.addFragment(SpecSettingFragment.newInstance(), "Settings");
        sectionPageAdapter.addFragment(SpecShareFragment.newInstance(), "Share");
        viewPager.setAdapter(sectionPageAdapter);
    }


    private class SectionPageAdapterr extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentString = new ArrayList<>();
        public SectionPageAdapterr(FragmentManager fm) {
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
                startActivity(new Intent(SpecialistHome.this, SpecialistLogin.class));
                finish();
                return true;
        }
        return false;
    }
}

