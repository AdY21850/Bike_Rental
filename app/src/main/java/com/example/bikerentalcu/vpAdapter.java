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

    private final ArrayList<HomeBannerviewpager> viewpageritemArrayList;
    private final int MULTIPLIER = 1000; // Large but manageable

    public vpAdapter(ArrayList<HomeBannerviewpager> viewpageritemArrayList) {
        this.viewpageritemArrayList = viewpageritemArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.veiwpager_home1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Use modulo to loop within the list bounds
        int actualPosition = position % viewpageritemArrayList.size();
        HomeBannerviewpager viewpagerItem = viewpageritemArrayList.get(actualPosition);

        Glide.with(holder.itemView.getContext())
                .load(viewpagerItem.getImageUrl())
                .placeholder(R.drawable.exclamation_mark)
                .error(R.drawable.activa)
                .into(holder.bannerImage);
    }

    @Override
    public int getItemCount() {
        // Return large finite count instead of Integer.MAX_VALUE
        return viewpageritemArrayList == null || viewpageritemArrayList.isEmpty()
                ? 0
                : viewpageritemArrayList.size() * MULTIPLIER;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bannerImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerImage = itemView.findViewById(R.id.bannerimage);
        }
    }
}
