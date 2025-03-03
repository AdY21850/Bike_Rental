package com.example.bikerentalcu;

    public class HomeBannerviewpager {

        private String imageUrl;  // For loading images from URL (backend)
        private int imageResource;  // For loading static images from drawable

        // Constructor for dynamic images (from URL)
        public HomeBannerviewpager(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        // Constructor for static images (from drawable)
        public HomeBannerviewpager(int imageResource) {
            this.imageResource = imageResource;
        }

        // Getter for imageUrl
        public String getImageUrl() {
            return imageUrl;
        }

        // Setter for imageUrl
        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        // Getter for imageResource
        public int getImageResource() {
            return imageResource;
        }

        // Setter for imageResource
        public void setImageResource(int imageResource) {
            this.imageResource = imageResource;
        }
    }


