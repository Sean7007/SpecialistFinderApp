package com.example.specialistfinderapp.CustomerFragments;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

class SectionPagerAdapter extends FragmentPagerAdapter {
    private String[] tabTitles = new String[]{"Home", "Appointment", "Chat", "Payment"};

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentString = new ArrayList<>();

    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    void addFragment(Fragment fm, String title) {
        fragmentList.add(fm);
        fragmentString.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
        //return fragmentString.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CustomerHomeFragment1();
            case 1:
                return new CustomerAppointmentFragment();
            case 2:
                return new CustomerChatFragment();
            case 3:
                return new CustomerPayFragment();
            default:
                throw new RuntimeException("Invalid tab position");
        }

    }
}