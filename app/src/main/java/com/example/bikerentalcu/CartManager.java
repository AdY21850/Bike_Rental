package com.example.bikerentalcu;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static List<bikeModel> cart = new ArrayList<>();

    public static void addToCart(bikeModel bike) {
        if (!isBikeInCart(bike)) {
            cart.add(bike);
        }
    }

    public static boolean isBikeInCart(bikeModel bike) {
        for (bikeModel b : cart) {
            if (b.getName().equals(bike.getName())) {
                return true;
            }
        }
        return false;
    }

    public static List<bikeModel> getCart() {
        return cart;
    }

    public static void clearCart() {
        cart.clear();
    }
}
