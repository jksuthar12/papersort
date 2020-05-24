package com.example.manit;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import io.opencensus.internal.Utils;

public class myadapter extends ArrayAdapter<pdf> {

      Context c;
      ProgressDialog progressDialog;

      List<pdf>data=new ArrayList<>();
      public static int code=1000;


      myadapter(Context c, List<pdf> data) {
          super(c,R.layout.pdf,data);
        this.c = c;
        this.data = data;
    }
    private ProgressDialog dialogg = new ProgressDialog(getContext());
    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
            final View view = LayoutInflater.from(c).inflate(R.layout.pdf, parent, false);
            TextView textView = view.findViewById(R.id.title);
            textView.setText(data.get(position).getName());
            TextView year = view.findViewById(R.id.year);
            year.setText(data.get(position).getYear());
            Button button = view.findViewById(R.id.button);
            final Button dow = view.findViewById(R.id.dow);
            dow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConnectivityManager manager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                    if (networkInfo == null) {
                        Toast.makeText(getContext(), "Check Internet Connection", Toast.LENGTH_LONG).show();
                    } else {
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(c);
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(c);
                            builder.setPositiveButton("GRANT", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, code);
                                }
                            });
                            builder.setTitle("Need Storage Permission");
                            builder.setMessage("This App needs storage permission for storing files. Please Grant it.");
                            builder.show();


                        }
                    }
                }


            });
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkconnection();
                    Intent i = new Intent(getContext(), webview.class);
                    i.putExtra("url", data.get(position).getUrl());
                    i.putExtra("title", data.get(position).getName());
                    c.startActivity(i);
                }
            });


        return view;

    }
    public void downloading(int position){
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(data.get(position).getUrl()));
        request.setTitle(data.get(position).getName());
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
       request.setDestinationInExternalFilesDir(getContext(),"MANIT",data.get(position).getName());
     //   request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,data.get(position).getName());
        DownloadManager downloadManager=(DownloadManager) c.getSystemService(Context.DOWNLOAD_SERVICE);
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
