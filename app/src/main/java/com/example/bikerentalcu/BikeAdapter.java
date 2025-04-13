package com.example.bikerentalcu;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

public class BikeAdapter extends RecyclerView.Adapter<BikeAdapter.ViewHolder> {

    private final Context context;
    private final List<bikeModel> bikeList;

    public BikeAdapter(Context context, List<bikeModel> bikeList) {
        this.context = context;
        this.bikeList = (bikeList != null) ? bikeList : new ArrayList<>();
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

        Glide.with(context)
                .load(bike.getImageUrl())
                .placeholder(R.drawable.exclamation_mark)
                .error(R.drawable.exclamation_mark)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.bikeImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, item_view.class);
            intent.putExtra("bike", bike);
            context.startActivity(intent);
        });

        holder.like.setOnClickListener(v -> {
            if (!CartManager.isBikeInCart(bike)) {
                CartItem cartItem = new CartItem(
                        bike.getName(),
                        bike.getPrice(),
                        bike.getTransmission(),
                        bike.getSpeed(),
                        bike.getMileage(),
                        bike.getOwnerName(),
                        bike.getOwnerEmail(),
                        bike.getOwnerContact(),
                        bike.getImageUrl(),
                        bike.getownerurl(),
                        bike.getownerupi()
                );
                CartManager.addToCart(cartItem);
                Toast.makeText(context, bike.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, bike.getName() + " is already in the cart!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (bikeList != null) ? bikeList.size() : 0;
    }

    public void updateList(List<bikeModel> newList) {
        bikeList.clear();
        bikeList.addAll(newList);
        notifyDataSetChanged();
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
