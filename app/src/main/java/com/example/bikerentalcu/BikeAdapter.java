package com.example.bikerentalcu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BikeAdapter extends RecyclerView.Adapter<BikeAdapter.ViewHolder> {

    private Context context;
    private List<bikeModel> bikeList;

    public BikeAdapter(Context context, List<bikeModel> bikeList) {
        this.context = context;
        this.bikeList = bikeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        bikeModel bike = bikeList.get(position);

        holder.bikeName.setText(bike.getName());
        holder.bikePrice.setText("₹" + bike.getPrice());

        Glide.with(context).load(bike.getImageUrl()).into(holder.bikeImage);

        // Click listener for bike item (Navigates to item view)
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, item_view.class);
            intent.putExtra("bike", bike);
            context.startActivity(intent);
        });

        // Click listener for "like" button (Adds bike to cart)
        holder.like.setOnClickListener(v -> {
            if (!CartManager.isBikeInCart(bike)) {
                CartManager.addToCart(bike);
                Toast.makeText(context, bike.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, bike.getName() + " is already in the cart!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return bikeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView bikeName, bikePrice;
        ImageView bikeImage, like;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bikeName = itemView.findViewById(R.id.bikename);
            bikePrice = itemView.findViewById(R.id.cost);
            bikeImage = itemView.findViewById(R.id.image123);
            like = itemView.findViewById(R.id.like);
        }
    }
}
