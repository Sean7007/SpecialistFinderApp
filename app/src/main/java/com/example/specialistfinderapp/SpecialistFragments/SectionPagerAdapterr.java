package com.example.specialistfinderapp.SpecialistFragments;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

class SectionPagerAdapterr extends FragmentPagerAdapter {
    private String[] tabTitles = new String[]{"Home", "Appointments", "Chat", "Settings","Share"};

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentString = new ArrayList<>();

    public SectionPagerAdapterr(FragmentManager fm) {
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
                return new SpecialistFragmentHome1();
            case 1:
                return new SpecApointmentFragment();
            case 2:
                return new SpecChatFragment();
            case 3:
                return new SpecSettingFragment();
            case 4:
                return new SpecShareFragment();
            default:
                throw new RuntimeException("Invalid tab position");
        }

    }
}
