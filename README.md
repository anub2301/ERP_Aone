# ERP_Aone

A modern Android ERP (Enterprise Resource Planning) application for Electronics & Electrical Retail and Wholesale businesses. The application is designed to provide complete offline shop management with inventory tracking, GST billing, customer management, sales, purchases, accounting, and business analytics.

Built using **Kotlin**, **Jetpack Compose**, **MVVM Architecture**, **Room Database**, and **Material 3**.

---

## Features

### Authentication
- Secure Login & Registration
- Password Hashing
- Persistent Login using DataStore
- Multi-user Support
- User-specific Data Isolation
- Logout
- Reset User Data

---

## Dashboard
- Daily Sales
- Daily Purchases
- Profit Summary
- Expenses
- Inventory Status
- Low Stock Alerts
- Recent Activities
- Quick Actions
- Business Overview

---

## Inventory Management
- Add Products
- Edit Products
- Delete Products
- Purchase Entry
- Purchase History
- Stock Tracking
- Low Stock Warning
- Stock Value Calculation
- Barcode Ready
- IMEI & Serial Number Support

---

## Sales Management
- Sales Entry
- Sales History
- Live Stock Validation
- Automatic Stock Reduction
- Profit Calculation
- Customer-wise Sales

---

## Customer Management
- Customer Database
- GST Number
- Address Book
- Purchase History
- Outstanding Tracking

---

## Billing
- GST Billing
- Dynamic Invoice Items
- Auto Tax Calculation
- SGST / CGST
- Discounts
- Receipt Preview
- PDF Export
- Android Printing Support
- Invoice History

---

## Accounting
- Expenses
- Investments
- Profit Calculation
- Daily Business Reports
- Monthly Summary

---

## Reports
- Sales Reports
- Purchase Reports
- Inventory Reports
- Profit Reports
- Customer Reports
- Business Analytics

---

## Offline First
- Room Database
- Local Storage
- Fast Performance
- No Internet Required

---

## Modern UI
- Material 3
- Responsive Layout
- Light Theme
- Dark Theme
- Tablet Support
- Landscape Support

---

# Technology Stack

- Kotlin
- Jetpack Compose
- Material 3
- MVVM Architecture
- StateFlow
- Room Database
- Hilt Dependency Injection
- Navigation Compose
- DataStore
- Coroutines

---

# Project Structure

```
app/
├── data/
│   ├── dao/
│   ├── database/
│   ├── entity/
│   ├── repository/
│
├── di/
│
├── navigation/
│
├── ui/
│   ├── screens/
│   ├── components/
│   ├── theme/
│
├── util/
│
├── viewmodel/
│
├── MainActivity.kt
└── ERP_AoneApp.kt
```

---

# Architecture

The project follows **Clean MVVM Architecture**.

```
UI (Compose)
      │
ViewModel
      │
Repository
      │
Room Database
```

The UI observes StateFlow exposed by ViewModels.

Repositories handle data operations.

Room provides persistent local storage.

---

# Database

Main Entities

- User
- Item
- Sale
- Customer
- Expense
- Investment
- Bill
- BillLine

Each user's data is isolated using their unique User ID.

---

# Security

- Passwords are stored using secure hashing.
- No plaintext passwords.
- User session stored securely using DataStore.
- Data isolation between users.

---

# Future Enhancements

- Cloud Backup
- Firebase Sync
- Multi-device Synchronization
- AI Inventory Forecasting
- Voice Billing
- OCR Purchase Invoice Scanner
- WhatsApp Invoice Sharing
- QR Code Payments
- Barcode Scanner
- Repair & Warranty Module
- Business Analytics Dashboard
- Role-based Employee Login
- Multi-Store Support
- Customer Mobile App

---

# Installation

1. Clone the repository

```bash
git clone <repository-url>
```

2. Open in Android Studio.

3. Sync Gradle.

4. Run on Android 7.0+ (API 24 or higher).

---

# Requirements

- Android Studio Narwhal or newer
- Kotlin Latest Stable
- Android SDK 24+
- Gradle Latest Stable

---

# License

This project is intended for educational and commercial development purposes.

---

# Author

Developed by **Anubhav Rai**

B.Tech, Electrical and Electronics Engineering

Indian Institute of Technology (IIT) Patna

GitHub: https://github.com/anub2301

---

## Vision

To build a modern, offline-first ERP platform that enables Indian retailers and wholesalers to efficiently manage inventory, sales, GST billing, accounting, customers, and business insights through a fast, reliable, and user-friendly Android application.
