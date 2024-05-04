# Rec-Auction Web Application
This is an e-commerce website that uses a reverse auction mechanism. Users can place bids on the products they want to buy and the sellers will bid to find out who is the winning bidder in the auction session. The website aims to provide a platform for buyers and sellers to interact and transact in a fair, transparent, and competitive environment. 
Website include: Website a user interface for customers and a CMS for admin and vendor
# Tech
- JDK 11 
- MySQL
- Spring Framework:
  - MVC
  - Data
  - Security
  - Validator
  - Scheduling
- Cloudinary for upload image
- Paypal Sandbox for payment service
- Fontend: Boostrap + JQuery and more js library ...

# Getting Started
- Clone the project
- Import the project into your IDE (Recommend IntelliJ IDEA)
- Put your configuration in application.yml
  - Database configuration
  - (**Imortant**) Email configuration (username and password) 
  - Paypal configuration (app and secret)
  - Cloudinary configuration (cloud name, api key, api secret)
- Create database schema name `recauction_db` in MySQL by using command `create database recauction_db;`
- Run the project
  - Run the project with the main class `RecAuctionWebApplication` by IDE
  - Or use maven build `.\mvnw clean package -DskipTest`
    - Then run the jar file: `java -jar target/recauction-web-0.0.1-SNAPSHOT.jar`
- After the project is started, the database schema will be created automatically. You need to run `recauction_db.sql` to insert some initial data (categories, product_tags)
- Access the website at `http://localhost:8080/`

# How to use (primary features)
- Create Admin account
  - Step 1: Register as User and verify your email
  - Step 2: Go to `user` table in database and update the `role_id` field to `1`
  - Step 3: Login new admin account and go to `/admin` page

- Create Supplier account
  - Step 1: Register as User and verify your email
  - Step 2: Go to Account Page and click `Become a Supplier` and fill in the form
  - Step 3: Login new supplier account and go to `/vendor` page
  - Step 4: Add product to your inventory (if you don't have any product, you can't attend any bid session)
- Pay in wallet via Paypal
  - Step 1: Login as User
  - Step 2: Go to `Wallet` page
  - Step 3: Click `Pay in via Paypal` and choose amount to pay
  - Step 4: You will be redirected to Paypal payment page 
  - Step 5: Login to your Paypal account and complete the payment
  - Step 6: You will be redirected back to the website and your wallet will be updated
- Create a bid session
  - **Note**: Before creating a bid session, you need to have enough account balance to pay for the bid (30% of the product price), and you must have at least address.
  - Step 1: Login as User
  - Step 2: Go to `Create Bid Session` page
  - Step 3: Fill in the form and submit
 
- Attend a bid session
  - Step 1: Login as Supplier
  - Step 2: Go to `Bid Session` in home page
  - Step 3: Find the bid session you want to attend and click `Attend`
  - Step 4: Place your bid
  - **Note**: You can only place a bid if you have product in your inventory and that product is matched with the bid session (base on category and product tag)
  
