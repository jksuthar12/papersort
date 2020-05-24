package com.example.manit;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ele5 extends Fragment {


    public ele5() {
        // Required empty public constructor
    }


    ListView vi;
    List<pdf> data;
    TextView viewStub,nodata;
    DatabaseReference databaseReference;
    SwipeRefreshLayout swipeRefreshLayout;

    myadapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view=inflater.inflate(R.layout.fragment_cse1, container, false);
        vi=view.findViewById(R.id.vie);
        viewStub=view.findViewById(R.id.stud);
        nodata=view.findViewById(R.id.nodata);
        swipeRefreshLayout=view.findViewById(R.id.refresh);
        data=new ArrayList<>();
        adapter =new myadapter(getContext(),data);
        vi.setAdapter(adapter);
        nodata.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(true);
        checkconnection();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                if(data.isEmpty())
                    nodata.setVisibility(View.VISIBLE);
            }
        },3000);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkconnection();
                onStart();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);

                    }
                },3000);
            }
        });
        vi.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(vi.getChildAt(0)!=null){
                    swipeRefreshLayout.setEnabled(vi.getFirstVisiblePosition()==0 && vi.getChildAt(0).getTop()==0);
                }
            }
        });
        AdView adView=view.findViewById(R.id.adView);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search,menu);
        MenuItem menuItem=menu.findItem(R.id.app_bar_search);
        SearchView searchView=(SearchView)menuItem.getActionView();
        searchView.setQueryHint("Type here to Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                nodata.setVisibility(View.GONE);
                search(newText);
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if(data.isEmpty()){
                    nodata.setVisibility(View.VISIBLE);
                    vi.setVisibility(View.VISIBLE);
                    viewStub.setVisibility(View.GONE);
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onStart() {
        super.onStart();
        data.clear();
        databaseReference= FirebaseDatabase.getInstance().getReference("ele6");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                pdf df=dataSnapshot.getValue(pdf.class);
                data.add(df);
                adapter.notifyDataSetChanged();
                if(!data.isEmpty()){
                    vi.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    nodata.setVisibility(View.GONE);}
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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

    public void search(String s){
        List<pdf>newdata=new ArrayList<>();
        for(pdf object:data){
            if(object.getName().toLowerCase().contains(s.toLowerCase())){
                newdata.add(object);
            }
        }
        myadapter myadapter=new myadapter(getContext(),newdata);
        vi.setAdapter(myadapter);
        if(newdata.isEmpty() && s.trim().length()==0){
            nodata.setVisibility(View.VISIBLE);
            vi.setVisibility(View.GONE);
            viewStub.setVisibility(View.GONE);
        }
        else if(newdata.isEmpty()){
            vi.setVisibility(View.GONE);
            viewStub.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.GONE);
        }
        else{
            vi.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.GONE);
            viewStub.setVisibility(View.GONE);
        }
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
