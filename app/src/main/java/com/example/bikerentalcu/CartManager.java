package com.example.bikerentalcu;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static List<CartItem> cartList = new ArrayList<>();

    public static List<CartItem> getCart() {
        return cartList;
    }

    public static void addToCart(CartItem bike) {
        cartList.add(bike);
    }

    public static void removeFromCart(CartItem item) {
        if (cartList.contains(item)) {
            cartList.remove(item);
        }
    }

    public static void clearCart() {
        cartList.clear();
    }

    public static int getTotalPrice() {
        int total = 0;
        for (CartItem item : cartList) {
            total += item.getPrice();
        }
        return total;
    }

    public static boolean isCartEmpty() {
        return cartList.isEmpty();
    }

    public static boolean isBikeInCart(bikeModel bike) {
        for (CartItem item : cartList) {
            if (item.getName().equals(bike.getName())) {
                return true;
            }
        }
        return false;
    }
}
