package com.example.manit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class mypageradapter1 extends FragmentPagerAdapter {


    public mypageradapter1(@NonNull FragmentManager fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:{
                return new ele1();

            }
            case 1:{
                return new ele2();

            }
            case 2: return new ele3();
            case 3: return  new ele4();
            case 4: return  new ele5();
            case 5: return  new ele6();
            case 6: return  new ele7();
            default:{
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
