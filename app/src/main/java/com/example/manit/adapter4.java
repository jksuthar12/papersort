package com.example.manit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class adapter4 extends FragmentPagerAdapter {


    public adapter4(@NonNull FragmentManager fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:{
                return new msme1();

            }
            case 1:{
                return new msme2();

            }
            case 2: return new msme3();
            case 3: return  new msme4();
            case 4: return  new msme5();
            case 5: return  new msme6();
            case 6: return  new msme7();
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
