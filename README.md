🚴‍♂️ Bike Rental Android App
A complete, production-ready Android application that allows users to browse, rent, and manage bikes in real-time. Integrated with a MongoDB-powered backend and modern third-party services for payment, email, OTP, and image handling.

✅ Project Summary
This application allows:

Users to rent bikes

Owners to upload bikes for rent

Real-time data sync with MongoDB

Integrated payment flow using Razorpay

Email & OTP verification for security

Profile management for both renters and owners

🧠 Key Features (with Implementation Highlights)
🗄️ 1. Authentication System
Built using MongoDB + Node.js + Express.js

Users can register, verify with OTP, log in, and update password

JWT tokens stored in SharedPreferences

Backend protects private routes using middleware auth

🏍️ 2. Bike Listing and Uploading
Bike data includes:

Name, type, speed, mileage, price, owner contact, UPI ID, images

Upload bikes using:

Volley for network communication

Cloudinary for storing and retrieving images

Uses Glide to load and cache images in the app

Real-time fetch using:

java
Copy
Edit
JsonObjectRequest -> Volley -> Backend `/api/v1/bike/allbikes`
🔍 3. Search and Filter
Users can:

Search bikes by name, category, or owner

Filter bikes using clickable category cards

Uses SearchView + RecyclerView filtering

🛒 4. Cart & Booking Confirmation
Add bikes to cart via CartManager

Confirm rentals with:

Custom start & end date pickers

Location autofill using Google’s FusedLocationProviderClient

Auto price calculation based on date range & quantity

💳 5. Payment Integration (Razorpay)
Razorpay SDK handles secure checkout

Payment success triggers:

Email to bike owner & user

WhatsApp message to the owner via Twilio API

📧 6. Email Confirmation (JavaMail API)
Sends email using:

Gmail SMTP via JavaMailAPI

Owner gets booking details

User gets confirmation copy

Credentials secured with App Passwords (2FA)

🔐 7. OTP Verification
OTP sent via email during registration

Validates code on backend

Frontend dialogs built using custom XML layouts

🖼️ 8. Image Upload & Display
Image picked from gallery using Intent

Stored on Cloudinary

Loaded into app using Glide

Used in:

Bike upload

Profile picture

Bike detail previews

👤 9. Profile Management
Fetches user profile using JWT-authenticated API call

Supports:

Display of name, image, email

Update of profile info

Data fetched and saved in SharedPreferences

🛠️ Tech Stack
Component	Technology/Library
Backend	Node.js, Express.js, MongoDB (Mongoose)
Auth	JWT, Bcrypt, OTP via email
Networking	Volley, Retrofit
Payments	Razorpay SDK
Email	JavaMail + Gmail SMTP
Image Handling	Cloudinary + Glide
Location	FusedLocationProviderClient
UI	Android SDK + XML + ViewPager2
Realtime Fetch	Volley + JSON API from backend

