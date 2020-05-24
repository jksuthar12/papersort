package com.example.manit;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class paper extends Fragment {


    public paper() {
        // Required empty public constructor
    }

    CardView btec, barch;
    AdView adView;
    AdRequest adRequest;
    InterstitialAd interstitialAd;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout l1,l2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_paper, container, false);
        btec = view.findViewById(R.id.tech);
        barch = view.findViewById(R.id.barch);
        l1=view.findViewById(R.id.one);
       l2=view.findViewById(R.id.two);
        swipeRefreshLayout=view.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkconnection();
                changecolor();
               swipeRefreshLayout.setRefreshing(false);
            }
        });
        adView = view.findViewById(R.id.adView);
        interstitialAd=new InterstitialAd(getContext());
        interstitialAd.setAdUnitId("ca-app-pub-9019470864409133/2447109258");
        AdRequest adRequest=new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
        btec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), btech.class);
                if(interstitialAd.isLoaded()){
                    interstitialAd.show();
                }
                startActivity(i);
            }
        });
        barch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), barch.class);
                if(interstitialAd.isLoaded()){
                    interstitialAd.show();
                }
                startActivity(i);
            }
        });
        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        return view;
    }
    public void changecolor(){

        Random rnd=new Random();
        int color1 = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        int color2= Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        l1.setBackgroundColor(color1);
        l2.setBackgroundColor(color2);

    }
    public void checkconnection(){
        ConnectivityManager manager=(ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=manager.getActiveNetworkInfo();
        if(networkInfo==null){
            Toast.makeText(getContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}



