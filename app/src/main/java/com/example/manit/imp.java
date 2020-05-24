package com.example.manit;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
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
public class imp extends Fragment {


    public imp() {
        // Required empty public constructor
    }
    ListView listView;
    DatabaseReference databaseReference;
    TextView viewStub,nodata;
    List<document>data;
    SwipeRefreshLayout swipeRefreshLayout;
    documentadapter1 documentadapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view=inflater.inflate(R.layout.fragment_imp, container, false);
        swipeRefreshLayout=view.findViewById(R.id.refresh);
        listView=view.findViewById(R.id.vie);
        viewStub=view.findViewById(R.id.stud);
        nodata=view.findViewById(R.id.nodata);
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
        data=new ArrayList<>();
        documentadapter=new documentadapter1(getContext(),data);
        listView.setAdapter(documentadapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(listView.getChildAt(0)!=null){
                    swipeRefreshLayout.setEnabled(listView.getFirstVisiblePosition()==0 && listView.getChildAt(0).getTop()==0);
                }
            }
        });
        AdView adView=view.findViewById(R.id.adView);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        return view;
    }
    public void checkconnection(){
        ConnectivityManager manager=(ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=manager.getActiveNetworkInfo();
        if(networkInfo==null){
            Toast.makeText(getContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search,menu);
        MenuItem menuItem=menu.findItem(R.id.app_bar_search);
        final SearchView searchView=(SearchView)menuItem.getActionView();
        searchView.setQueryHint("Type here to Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    public void search(String s){
        List<document>newdata=new ArrayList<>();
        for(document object:data){
            if(object.getName().toLowerCase().contains(s.toLowerCase())){
                newdata.add(object);
            }
        }
        documentadapter1 myadapter=new documentadapter1(getContext(),newdata);
        listView.setAdapter(myadapter);
        if(newdata.isEmpty() && s.trim().length()==0){
            nodata.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            viewStub.setVisibility(View.GONE);
        }
        else if(newdata.isEmpty()){
            listView.setVisibility(View.GONE);
            viewStub.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.GONE);
        }
        else{
            listView.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.GONE);
            viewStub.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        data.clear();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("document");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                document d=dataSnapshot.getValue(document.class);
                data.add(d);
                documentadapter.notifyDataSetChanged();
                if(!data.isEmpty()){
                    listView.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    nodata.setVisibility(View.GONE);
                }
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

}

class document {
    String name;
    String url;
    public document(){
        //
    }

    public document(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
class documentadapter extends ArrayAdapter<document>{

      List<document>data=new ArrayList<>();
      Context context;
    public documentadapter(@NonNull Context context, List<document>data) {
        super(context,R.layout.documents,data);
        this.context=context;
        this.data=data;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=LayoutInflater.from(context).inflate(R.layout.documents,parent,false);
        TextView textView=view.findViewById(R.id.titlee);
        textView.setText(data.get(position).getName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(position);
            }
        });

        return view;
    }
    public void click(final int position){
        ConnectivityManager manager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo == null) {
            Toast.makeText(getContext(), "Check Internet Connection", Toast.LENGTH_LONG).show();
        } else {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Downloading...", Toast.LENGTH_SHORT).show();
                        downloading(position);
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setMessage("Would you like to download this document?");
                builder.show();

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setPositiveButton("GRANT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                    }
                });
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This App needs storage permission for storing files. Please Grant it.");
                builder.show();


            }
        }
    }
    public void downloading(int position){
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(data.get(position).getUrl()));
        request.setTitle(data.get(position).getName());
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,data.get(position).getName()+".pdf");
        DownloadManager downloadManager=(DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        request.setMimeType("application/pdf");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        downloadManager.enqueue(request);
    }
}
