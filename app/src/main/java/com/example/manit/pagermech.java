package com.example.manit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class pagermech extends FragmentPagerAdapter {

    public pagermech(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: {
                return new mech1();
            }
            case 1: return new mech2();
            case 2: return new mech3();
            case 3: return new mech4();
            case 4: return new mech5();
            case 5: return new mech6();
            case 6: return new mech7();
            default: {
                return null;
            }
            }

        }



    @Override
    public int getCount() {
        return 7;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0 :{
                return "1-2 semester";
            }
            case 1:return "3 semester";
            case 2:return  "4 semester";
            case 3:return "5 semester";
            case 4:return  "6 semester";
            case 5:return  "7 semester";
            case 6:return  "8 semester";

        }
        return null;
    }
}
