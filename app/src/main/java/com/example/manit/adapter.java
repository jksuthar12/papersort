package com.example.manit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class adapter extends RecyclerView.Adapter<adapter.viewer>  {
        Context c;
        List<pdf>data;

    public adapter(Context c, List<pdf> data) {
        this.c = c;
        this.data = data;
    }

    public viewer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(c).inflate(R.layout.pdf,parent,false);

        return new viewer(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewer holder, int position) {

        holder.viw.setText(data.get(position).getName());
        holder.year.setText(data.get(position).getYear());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class viewer extends RecyclerView.ViewHolder{

                     TextView viw;
                     TextView year;
        public viewer(@NonNull View itemView) {
            super(itemView);
            viw=itemView.findViewById(R.id.title);
            year=itemView.findViewById(R.id.year);
        }
    }
}
