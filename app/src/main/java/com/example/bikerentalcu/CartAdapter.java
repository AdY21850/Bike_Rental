package com.example.bikerentalcu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context context;
    private List<CartItem> cartList;

    public CartAdapter(Context context, List<CartItem> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_cart_view_recycler, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = cartList.get(position);
        holder.name.setText(item.getName());
        holder.price.setText("₹" + item.getPrice());
        Glide.with(context).load(item.getImageUrl()).into(holder.image);

        // Handle quantity change
        holder.quantity.setText("1"); // Default to 1

        holder.plus.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.quantity.getText().toString());
            currentQuantity++;
            holder.quantity.setText(String.valueOf(currentQuantity));
        });

        holder.minus.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.quantity.getText().toString());
            if (currentQuantity > 1) {
                currentQuantity--;
                holder.quantity.setText(String.valueOf(currentQuantity));
            }
        });

        // Handle Remove Item
        holder.removeButton.setOnClickListener(v -> {
            int removedPosition = holder.getAdapterPosition();
            if (removedPosition != RecyclerView.NO_POSITION) {
                // Remove from CartManager
                CartManager.removeFromCart(cartList.get(removedPosition));
                notifyItemRemoved(removedPosition);
            }
            Toast.makeText(context, item.getName() + " removed from cart", Toast.LENGTH_SHORT).show();
        });

        // Open item details when clicking on the cart item
        holder.itemView.setOnClickListener(v -> {
            int quantity = Integer.parseInt(holder.quantity.getText().toString()); // Get the quantity value
            Intent intent = new Intent(context, confirm_bike.class);
            intent.putExtra("cartItems", item);  // Ensure CartItem is Parcelable
            intent.putExtra("quantity", quantity); // Send quantity to item_view
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, quantity;
        ImageView image, plus, minus;
        Button removeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.bikename);
            price = itemView.findViewById(R.id.cost);
            image = itemView.findViewById(R.id.image123);
            plus = itemView.findViewById(R.id.plus1);
            minus = itemView.findViewById(R.id.minus1);
            quantity = itemView.findViewById(R.id.quantity);
            removeButton = itemView.findViewById(R.id.remove);
        }
    }
}
