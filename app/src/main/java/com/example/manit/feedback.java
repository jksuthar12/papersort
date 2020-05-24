package com.example.manit;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class feedback extends AppCompatActivity {
                Toolbar toolbar;
                ImageButton send;
                DatabaseReference databaseReference;
                EditText email,feedd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        toolbar=(Toolbar)findViewById(R.id.tolbar2);
        send=(ImageButton) findViewById(R.id.send);
        email=(EditText)findViewById(R.id.editText);
        send.setColorFilter(getColor(R.color.grey));
        feedd=(EditText)findViewById(R.id.editText1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        feedd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().length()>0){
                    send.setColorFilter(getColor(R.color.white));
                }
                else{
                    send.setColorFilter(getColor(R.color.grey));
                }
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendfeed();
            }
        });
    }
    public void sendfeed() {
         if(feedd.getText().toString().trim().length()==0) {
             final AlertDialog.Builder builder = new AlertDialog.Builder(this);
             builder.setMessage("Please write feedback.");
             builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     dialog.dismiss();
                 }
             });
             builder.show();
         }
         else if(email.getText().toString().trim().length()==0){
             final AlertDialog.Builder builder = new AlertDialog.Builder(this);
             builder.setMessage("Please write email.");
             builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     dialog.dismiss();
                 }
             });
             builder.show();
         }
         else if(!(email.getText().toString().toLowerCase().trim().contains("@")
                 && email.getText().toString().toLowerCase().trim().contains(".com"))){
             Toast.makeText(getApplicationContext(),"check email address",Toast.LENGTH_SHORT).show();
         }
         else{
             databaseReference =FirebaseDatabase.getInstance().getReference("feedback");
             String em=email.getText().toString();
             String fed= feedd.getText().toString();
             feed feed=new feed(em,fed);
             ConnectivityManager manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
             NetworkInfo networkInfo=manager.getActiveNetworkInfo();
             if(networkInfo!=null) {
                 Toast.makeText(getApplicationContext(), "Sending...", Toast.LENGTH_SHORT).show();
                 databaseReference.child(databaseReference.push().getKey()).setValue(feed).addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void aVoid) {
                         Toast.makeText(getApplicationContext(), "Feedback sent", Toast.LENGTH_LONG).show();
                         email.setText("");
                         feedd.setText("");
                     }
                 }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();
                     }
                 });
             }
             else{
                 Toast.makeText(this,"Check internet Connection",Toast.LENGTH_SHORT).show();
             }


         }
    }



    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(0,0);
        return true;
    }
}
class feed {
    String email;
    String feedback;

    public feed(String email, String feedback) {
        this.email = email;
        this.feedback = feedback;
    }

    public String getEmail() {
        return email;
    }

    public String getFeedback() {
        return feedback;
    }
}
