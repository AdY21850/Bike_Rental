🚴‍♂️ Bike Rental Android App – Frontend
An Android application that allows users to browse, rent, and upload bikes, complete with real-time updates, secure payments, email notifications, and profile management — all handled via a modern and intuitive UI using Android SDK and third-party integrations.

📱 Features (Frontend-Only)
1. 🔐 User Authentication (via Backend API)
Register, Login, and OTP verification

Managed via Retrofit + Dialog fragments

JWT token stored securely using SharedPreferences

2. 🏍️ Bike Listing & Details
Fetch bikes from backend using Volley

Display in grid format using RecyclerView

Each bike card includes image, price, specs, and owner details

Search & filter bikes by name, type, or owner

3. 📤 Upload Bike for Rent
Custom form to:

Enter bike name, type, registration number, rent price, etc.

Upload image from gallery (with permission checks)

Send data via Multipart Retrofit call

4. 🔍 Search & Category Filtering
Live search using SearchView

Filter bikes by categories (Sports, Electric, Street, etc.)

Dynamic update of UI via RecyclerView adapter

5. 🛒 Cart System
Add bikes to cart for batch booking

Quantity selector with plus/minus controls

Remove items from cart

View total calculated amount

6. 📅 Rental Booking
Select rental start & end date using DatePickerDialog

Calculates duration and total cost automatically

Autofills user location using Google FusedLocation API

7. 💳 Payment Integration
Integrated Razorpay Checkout SDK

Prefills name and contact info

On success:

Sends confirmation emails

Sends WhatsApp message via Twilio (API call from frontend)

8. 📧 Email Notifications
On booking confirmation:

Sends mail to both bike owner and renter

Email sending via JavaMail API with Gmail SMTP

9. 🖼️ Image Uploading & Display
Uses Glide to:

Load bike images from Cloudinary URLs

Cache and show fallback placeholders

User and bike images shown in profile and listings

10. 👤 User Profile Management
Fetch user info after login and display in:

Navigation Drawer

Profile section

Stores user name and profile image in SharedPreferences

🛠️ Technologies & Libraries
Purpose	Library / Tool
Networking	Volley, Retrofit
Image Loading	Glide
Email Sending	JavaMail API
UI Components	RecyclerView, ViewPager2
Location Access	FusedLocationProviderClient
Payments	Razorpay Android SDK
Data Storage	SharedPreferences
File Upload	Retrofit Multipart + Intent
Permission Handling	ActivityResultLauncher

