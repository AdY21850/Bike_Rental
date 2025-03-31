package com.example.bikerentalcu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class vpAdapter extends RecyclerView.Adapter<vpAdapter.ViewHolder> {

    private ArrayList<HomeBannerviewpager> viewpageritemArrayList;

    public vpAdapter(ArrayList<HomeBannerviewpager> viewpageritemArrayList) {
        this.viewpageritemArrayList = viewpageritemArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.veiwpager_home1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeBannerviewpager viewpagerItem = viewpageritemArrayList.get(position);

        // Load image using Glide
        Glide.with(holder.itemView.getContext())
                .load(viewpagerItem.getImageUrl())
                .placeholder(R.drawable.exclamation_mark) // Placeholder image
                .error(R.drawable.activa) // Error image if loading fails
                .into(holder.bannerImage);
    }

    @Override
    public int getItemCount() {
        return viewpageritemArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bannerImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerImage = itemView.findViewById(R.id.bannerimage);
        }
    }
}