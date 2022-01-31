package com.example.myapplication.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myapplication.fragment.ChatsFragment;
import com.example.myapplication.fragment.group;
import com.example.myapplication.fragment.status;

public class FragmentAdapter extends FragmentPagerAdapter {

    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new ChatsFragment();

            case 1: return new group();

            case 2: return new status();

            default:return new ChatsFragment();
        }

    }


    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String titel = null;
        if (position==0){
            titel="Chats";
        }if (position==1){
            titel="Groups";
        }if (position==2){
            titel="Status";
        }
        return titel;
    }
}
