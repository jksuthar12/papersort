package com.example.manit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.MenuCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.manit.R.menu.drawer;


public class MainActivity extends AppCompatActivity {

           DrawerLayout drawerLayout;
            String link;
           NavigationView navigationView;
           FrameLayout frameLayout;
           DatabaseReference databaseReference;
           TextView button;
           FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, "ca-app-pub-9019470864409133~3266771875");
        Toolbar toolbar=findViewById(R.id.tool);
        auth=FirebaseAuth.getInstance();
        frameLayout=findViewById(R.id.view);
       setSupportActionBar(toolbar);
       getSupportActionBar().setTitle("Papers");
       navigationView=findViewById(R.id.navview);
        drawerLayout=findViewById(R.id.refff);
        navigationView.setItemIconTintList(null);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,0,0);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        data();
        if(savedInstanceState==null){
        getSupportFragmentManager().beginTransaction().replace(R.id.view,new paper()).commit();
        navigationView.setCheckedItem(R.id.paper);}
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.paper:
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.view,new paper()).commit();
                        getSupportActionBar().setTitle("Papers");

                        break;
                    }
                    case R.id.notes:{
                        getSupportFragmentManager().beginTransaction().replace(R.id.view,new notes()).commit();
                        getSupportActionBar().setTitle("Notes");
                        break;
                    }
                    case R.id.imp:{
                        getSupportFragmentManager().beginTransaction().replace(R.id.view,new imp()).commit();
                        getSupportActionBar().setTitle("Documents");
                        break;
                    }
                    case R.id.fedd:{
                        Intent i=new Intent(getApplicationContext(),feedback.class);
                        startActivity(i);
                        break;
                    }
                    case R.id.share:{
                        Intent i=new Intent();
                        i.setAction(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        String sub="PaperSort";
                        String body=link;
                        i.putExtra(Intent.EXTRA_SUBJECT,sub);
                        i.putExtra(Intent.EXTRA_TEXT,body);
                        startActivity(Intent.createChooser(i,"Share using"));
                        break;
                    }
                    case R.id.book:{
                        getSupportFragmentManager().beginTransaction().replace(R.id.view,new book()).commit();
                        getSupportActionBar().setTitle("Books");
                        break;
                    }
                }
                drawerLayout.closeDrawer(Gravity.START);
                return true;
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        data();
    }

    public void data(){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("share");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String a=dataSnapshot.getValue(String.class);
                link=a;
                Log.w("heelo",a);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String a=dataSnapshot.getValue(String.class);
                link=a;
                Log.w("heelo",a);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
