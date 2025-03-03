package com.example.bikerentalcu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class vpAdapter extends RecyclerView.Adapter<vpAdapter.ViewHolder> {




    ArrayList<HomeBannerviewpager>   viewpageritemArraylist;

    public vpAdapter(ArrayList<HomeBannerviewpager> viewpageritemArraylist) {
        this.viewpageritemArraylist = viewpageritemArraylist;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.veiwpager_home1,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeBannerviewpager viewpageritem = viewpageritemArraylist.get(position);
        holder.bannerimage.setImageResource(viewpageritem.getImageResource());
    }

    @Override
    public int getItemCount() {
        return viewpageritemArraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView bannerimage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerimage = itemView.findViewById(R.id.bannerimage);

        }
    }

}
