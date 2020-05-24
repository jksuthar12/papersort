package com.example.manit;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class book extends Fragment {


    public book() {
        // Required empty public constructor
    }
    ListView vi;
    List<databook> data;
    TextView viewStub,nodata;
    DatabaseReference databaseReference;
    SwipeRefreshLayout swipeRefreshLayout;
    bookadapter adapter;
    FloatingActionButton dialog;
    BottomSheetDialog bottomSheetDialog;
    Button closw;
    ChipGroup chipGroup,chipGroup1;
    final int[] year = new int[1];
    final int[] branch = new int[1];
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);

        View view=inflater.inflate(R.layout.fragment_book, container, false);
        vi=view.findViewById(R.id.view);
        viewStub=view.findViewById(R.id.stud);
        nodata=view.findViewById(R.id.nodata);
        dialog=view.findViewById(R.id.dialog);
        swipeRefreshLayout=view.findViewById(R.id.refresh);
        data=new ArrayList<>();
        adapter =new bookadapter(getContext(),data);
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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);

                    }
                },2000);
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

        branch[0]=R.id.cs;
        bottomSheetDialog=new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.dialogelayout);
        TextView cancel=bottomSheetDialog.findViewById(R.id.cancel);
        TextView apply=bottomSheetDialog.findViewById(R.id.Done);
         chipGroup=bottomSheetDialog.findViewById(R.id.chipyear);
        chipGroup1=bottomSheetDialog.findViewById(R.id.chipbranch);
        chipGroup1.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
               Chip chip=chipGroup.findViewById(i);
               if(chip==null)
                  chipGroup.check(branch[0]);
            }
        });
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip=chipGroup.findViewById(i);
                if(chip==null)
                    chipGroup.check(year[0]);
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    year[0] =chipGroup.getCheckedChipId();
                    branch[0] =chipGroup1.getCheckedChipId();
                    bottomSheetDialog.dismiss();
                    swipeRefreshLayout.setRefreshing(true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                            if(data.isEmpty())
                                nodata.setVisibility(View.VISIBLE);
                        }
                    },3000);
                getdata(year[0],branch[0]);

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chipGroup.check(year[0]);
                chipGroup1.check(branch[0]);
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                chipGroup.check(year[0]);
                chipGroup1.check(branch[0]);
            }
        });
        Button reset=bottomSheetDialog.findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chipGroup.check(R.id.f);
                chipGroup1.check(R.id.cs);
            }
        });
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();
            }
        });

        AdView adView=view.findViewById(R.id.adView);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        getdata(R.id.f,R.id.cs);

        return view;
    }


    public void getdata(int year, int brach){
        data.clear();
        String ref = null,child=null;
        if(year==R.id.f){
            if(brach==R.id.cs){ref="first";child="cse";}
            else if(brach==R.id.ece){ref="first";child="ece";}
            else if(brach==R.id.mech){ref="first";child="mech";}
            else if(brach==R.id.ee){ref="first";child="ee";}
            else if(brach==R.id.civil){ref="first";child="civil";}
            else if(brach==R.id.msme){ref="first";child="msme";}}
            if(year==R.id.s){
            if(brach==R.id.cs){ref="second";child="cse";}
            else if(brach==R.id.ece){ref="second";child="ece";}
            else if(brach==R.id.mech){ref="second";child="mech";}
            else if(brach==R.id.ee){ref="second";child="ee";}
            else if(brach==R.id.civil){ref="second";child="civil";}
            else if(brach==R.id.msme){ref="second";child="msme";}}
            if(year==R.id.t){
            if(brach==R.id.cs){ref="third";child="cse";}
            else if(brach==R.id.ece){ref="third";child="ece";}
            else if(brach==R.id.mech){ref="third";child="mech";}
            else if(brach==R.id.ee){ref="third";child="ee";}
            else if(brach==R.id.civil){ref="third";child="civil";}
            else if(brach==R.id.msme){ref="third";child="msme";}}
            if(year==R.id.fo){
            if(brach==R.id.cs){ref="fourth";child="cse";}
            else if(brach==R.id.ece){ref="fourth";child="ece";}
            else if(brach==R.id.mech){ref="fourth";child="mech";}
            else if(brach==R.id.ee){ref="fourth";child="ee";}
            else if(brach==R.id.civil){ref="fourth";child="civil";}
            else if(brach==R.id.msme){ref="fourth";child="msme";} }
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("book").child(ref+child);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                databook df=dataSnapshot.getValue(databook.class);
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
         adapter.notifyDataSetChanged();

     }


    @Override
    public void onStart() {
        super.onStart();
        branch[0]=R.id.cs;
        year[0]=R.id.f;

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



    public void search(String s){
        List<databook>newdata=new ArrayList<>();
        for(databook object:data){
            if(object.getTitle().toLowerCase().contains(s.toLowerCase())){
                newdata.add(object);
            }
        }
        bookadapter myadapter=new bookadapter(getContext(),newdata);
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
class databook {
    String title,auther,image,url;
    public databook(){
        //requre
    }

    public databook(String title, String auther, String image,String url) {
        this.title = title;
        this.auther = auther;
        this.url=url;
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getAuther() {
        return auther;
    }

    public String getImage() {
        return image;
    }
}
class bookadapter extends ArrayAdapter<databook>{
     List<databook>data=new ArrayList<>();
     Context context;
    public bookadapter(@NonNull Context context,List<databook>j) {
        super(context, R.layout.forbook,j);
        this.data=j;
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       View view=LayoutInflater.from(context).inflate(R.layout.forbook,parent,false);
        TextView title=view.findViewById(R.id.titleb);
        TextView auther=view.findViewById(R.id.auther);
        ImageView cover=view.findViewById(R.id.imageb);
        title.setText(data.get(position).getTitle());
        auther.setText(data.get(position).getAuther());
        Picasso.get().load(data.get(position).getImage()).placeholder(R.drawable.loading).into(cover);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager manager=(ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo=manager.getActiveNetworkInfo();
                if(networkInfo==null){
                    Toast.makeText(getContext(),"Check Internet Connection",Toast.LENGTH_LONG).show();
                }
                else{
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
                                ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 11);
                            }
                        });
                        builder.setTitle("Need Storage Permission");
                        builder.setMessage("This App needs storage permission for storing files. Please Grant it.");
                        builder.show();


                    }

                }

            }
        });
       return view;
    }
    public void downloading(int position){
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(data.get(position).getUrl()));
        request.setTitle(data.get(position).getTitle());
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
      request.setDestinationInExternalFilesDir(getContext(),"MANIT",data.get(position).getTitle());
       //   request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,data.get(position).getTitle()+".pdf");
        DownloadManager downloadManager=(DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        request.setMimeType("application/pdf");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        downloadManager.enqueue(request);
    }
    public void checkconnection(){
        ConnectivityManager manager=(ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=manager.getActiveNetworkInfo();
        if(networkInfo==null){
            Toast.makeText(getContext(),"Check Internet Connection",Toast.LENGTH_LONG).show();
        }
    }
}