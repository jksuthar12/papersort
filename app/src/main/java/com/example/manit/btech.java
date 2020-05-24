package com.example.manit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Random;

public class btech extends AppCompatActivity {
              SwipeRefreshLayout swipeRefreshLayout;
              CardView cs,el,ec,ms,cv,mec;
    AdView adView;
    AdRequest adRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btech);
        getSupportActionBar().setTitle("B.Tech");
        swipeRefreshLayout=findViewById(R.id.reff);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                changecolor();
                checkconnection();
             swipeRefreshLayout.setRefreshing(false);
            }
        });
        mec=(CardView)findViewById(R.id.mec);
        cs=(CardView)findViewById(R.id.cs);
        el=(CardView)findViewById(R.id.el);
        ec=(CardView)findViewById(R.id.ec);
        ms=(CardView)findViewById(R.id.ms);
        cv=(CardView)findViewById(R.id.cv);
        cs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),cse.class);
                startActivity(intent);
            }
        });
        el.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ele.class);
                startActivity(intent);
            }
        });
        mec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),mech.class);
                startActivity(intent);
            }
        });
        ms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),msme.class);
                startActivity(intent);
            }
        });
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),civil.class);
                startActivity(intent);
            }
        });
        ec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ece.class);
                startActivity(intent);
            }
        });
        adView = (AdView)findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


}
   public void changecolor(){
       final LinearLayout l1=(LinearLayout)findViewById(R.id.one);
       final LinearLayout l2=(LinearLayout)findViewById(R.id.two);
       final LinearLayout l3=(LinearLayout)findViewById(R.id.three);
       final LinearLayout l4=(LinearLayout)findViewById(R.id.four);
       final LinearLayout l5=(LinearLayout)findViewById(R.id.five);
       final LinearLayout l6=(LinearLayout)findViewById(R.id.six);
       Random rnd=new Random();
       int color1 = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
       int color2= Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
       int color3 = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
       int color4 = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
       int color5 = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
       int color6 = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
       l1.setBackgroundColor(color1);
       l2.setBackgroundColor(color2);
       l3.setBackgroundColor(color3);
       l4.setBackgroundColor(color4);
       l5.setBackgroundColor(color5);
       l6.setBackgroundColor(color6);
   }

    public void checkconnection(){
        ConnectivityManager manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=manager.getActiveNetworkInfo();
        if(networkInfo==null){
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}