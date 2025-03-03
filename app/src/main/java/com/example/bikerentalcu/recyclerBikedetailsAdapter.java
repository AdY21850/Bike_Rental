package com.example.bikerentalcu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerBikedetailsAdapter extends RecyclerView.Adapter<recyclerBikedetailsAdapter.ViewHolder> {

    Context context;
    ArrayList<contactModel> arrdetails;
    public recyclerBikedetailsAdapter(Context context, ArrayList<contactModel> arrdetails) {
        this.context = context;
        this.arrdetails = arrdetails;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view=LayoutInflater.from(context).inflate(R.layout.recyclerview_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
 holder.image.setImageResource(arrdetails.get(position).img);
 holder.bikename.setText(arrdetails.get(position).name);
        holder.cost.setText(String.valueOf(arrdetails.get(position).cost));

    }

    @Override
    public int getItemCount() {
        return arrdetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
             TextView bikename;
             ImageView image;
             TextView cost;
             TextView perday;
             ImageView like;
             ImageView arrowright;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bikename=itemView.findViewById(R.id.bikename);
            image=itemView.findViewById(R.id.image123);
            cost=itemView.findViewById(R.id.cost);
            perday=itemView.findViewById(R.id.perday);
            like=itemView.findViewById(R.id.like);
            arrowright=itemView.findViewById(R.id.arrowright);
        }
    }
}
