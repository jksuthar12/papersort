package com.example.manit;

import android.app.SearchManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;


public class cse extends AppCompatActivity {
                    ViewPager viewPager;
                    TabLayout tabLayout;
                Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cse);
        viewPager=(ViewPager)findViewById(R.id.view_pager);
        tabLayout=(TabLayout)findViewById(R.id.tabs);
       mypageradapter mypageradapter=new mypageradapter(getSupportFragmentManager());
       viewPager.setAdapter(mypageradapter);
       tabLayout.setupWithViewPager(viewPager);
   toolbar=findViewById(R.id.toolbar);
   setSupportActionBar(toolbar);
   getSupportActionBar().setDisplayHomeAsUpEnabled(true);
   getSupportActionBar().setTitle("Subjects");
     // AdView adView;
        // AdRequest adRequest;
        //adView =(AdView)findViewById(R.id.adView);
        //adRequest = new AdRequest.Builder().build();
        //adView.loadAd(adRequest);



    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


}