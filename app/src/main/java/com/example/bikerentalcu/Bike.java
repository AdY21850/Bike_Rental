package com.example.bikerentalcu;

public class Bike {
    public class Car {

        // Fields (Attributes)
        private String name;
        private String brand;
        private double price;

        // Constructor
        public Car(String name, String brand, double price) {
            this.name = name;
            this.brand = brand;
            this.price = price;
        }

        // Getter and Setter for 'name'
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        // Getter and Setter for 'brand'
        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        // Getter and Setter for 'price'
        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        // Optional: Override toString() for readable object representation
        @Override
        public String toString() {
            return "Car [Name: " + name + ", Brand: " + brand + ", Price: " + price + "]";
        }
    }

}
