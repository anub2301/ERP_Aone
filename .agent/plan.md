# Project Plan

Build a COMPLETE production-ready Android ERP & Billing Application for an Electronics & Electrical Retail + Wholesale Business named "ERP_Aone". 

The application must contain every interface, screen, model, widget, navigation flow, and reusable component required for a full ERP. 

Modules to include:
- Authentication (Splash, Onboarding, Login, OTP, Biometrics)
- Dashboard (Sales, Purchase, Profit, Expenses, Low Stock, Charts)
- POS Billing (Touch POS, Barcode, GST, Discounts, Print/PDF/WhatsApp)
- Product Management (IMEI/Serial Tracking, Variants, GST/HSN)
- Inventory (Multi-warehouse, Stock Ledger, Valuation)
- Purchase & Suppliers (Orders, Entry, Returns, Ledgers)
- Customers & CRM (Master, Ledger, Credit Limit, Loyalty)
- Accounting (Cash/Bank Books, P&L, Balance Sheet, GST Reports)
- Reports (Sales, Purchase, Inventory, Profit, Expenses)
- Employee & Service Center (Attendance, Salary, Job Cards, Repairs)
- Warranty (Registration, Claim, AMC)
- AI & OCR (Forecasting, Voice Billing, Invoice OCR)
- Settings (Business Profile, Invoice Templates, Printer Config)
- Customer Mobile App (Invoices, Warranty, Offers)

Tech Stack: Kotlin, Jetpack Compose (Material 3), Riverpod/Compose State, Room/SQLite, Retrofit, Adaptive Layouts.

## Project Brief

# ERP_Aone Project Brief

A robust, commercial-grade ERP and Billing solution tailored for the electronics and electrical retail and wholesale industry. This application transitions complex desktop ERP workflows into a modern, mobile-first experience using Material 3 and adaptive design principles.

## Features
*   **Inventory with IMEI/Serial Tracking**: Manage electronics stock with precision using unique identifier tracking (IMEI/Serial), GST/HSN support, and low-stock alerts.
*   **Adaptive POS Billing**: A versatile point-of-sale system supporting barcode/QR scanning, GST calculations, discounts, and instant PDF/WhatsApp invoice sharing.
*   **Dynamic Business Dashboard**: Real-time visualization of sales, purchases, profit, and expenses using Material 3 components and adaptive layouts for all screen sizes.
*   **Party Ledgers & Credit Management**: Comprehensive tracking of customer and supplier balances, credit limits, and transaction history.
*   **Offline-First Synchronization**: Core ERP operations are available offline via local persistence, with seamless cloud synchronization when connectivity is available.

## High-Level Technical Stack
*   **Language**: Kotlin
*   **UI Framework**: Jetpack Compose with Material 3
*   **Navigation**: Jetpack Navigation 3 (State-driven architecture)
*   **Adaptive Strategy**: Compose Material Adaptive library (List-Detail and Pane layouts)
*   **Concurrency**: Kotlin Coroutines & Flow
*   **Persistence**: Room Database (Offline-first capability)
*   **Networking**: Retrofit & OkHttp (Cloud Sync & OCR APIs)

## UI Design Image
![UI Design](file:///E:/ERP_Aone/input_images/image_6.jpeg)

## Implementation Steps

### Task_1_Foundation_Auth: Establish the project architecture with Room persistence, Retrofit networking, and Navigation 3. Implement the Authentication flow including Splash, Onboarding, and Login screens.
- **Status:** COMPLETED
- **Updates:** Project foundation established with Room, Retrofit, and Navigation 3. Implemented Splash, Login, and Company Selection screens using Material 3 and Kotlin Compose. App icon generated. UI matches branding from provided images.
- **Acceptance Criteria:**
  - Room database initialized with entities for Products, Sales, and Parties
  - Navigation 3 graph functional
  - The implemented UI must match the design provided in E:/ERP_Aone/input_images/image_4.jpeg and E:/ERP_Aone/input_images/image_9.jpeg
  - App builds and navigates successfully from Splash to Login screen

### Task_2_Inventory_POS_Billing: Develop the Product Management module with IMEI/Serial tracking and the POS Billing system. Integrate stock management with sales transactions and implement PDF invoice generation.
- **Status:** IN_PROGRESS
- **Acceptance Criteria:**
  - The implemented UI must match the design provided in E:/ERP_Aone/input_images/image_1.jpeg, E:/ERP_Aone/input_images/image_10.jpeg, and E:/ERP_Aone/input_images/image_2.jpeg
  - Stock levels update automatically upon sale completion
  - PDF invoices are generated and shareable
  - Barcode scanning integration placeholder functional
- **StartTime:** 2026-07-06 11:02:01 IST

### Task_3_Dashboard_CRM_Finance: Build the real-time Business Dashboard for analytics and the CRM module for managing Party Ledgers, Credit, and financial reporting.
- **Status:** PENDING
- **Acceptance Criteria:**
  - The implemented UI must match the design provided in E:/ERP_Aone/input_images/image_0.jpeg, E:/ERP_Aone/input_images/image_3.jpeg, and E:/ERP_Aone/input_images/image_5.jpeg
  - Accurate balance tracking and history for Customers and Suppliers
  - Dashboard charts correctly reflect sales and profit data

### Task_4_Advanced_Features_Final_Verify: Implement AI/OCR invoice scanning, Service/Warranty modules, and Settings. Perform final UI polish, adaptive icon branding, and comprehensive stability verification.
- **Status:** PENDING
- **Acceptance Criteria:**
  - The implemented UI must match the design provided in E:/ERP_Aone/input_images/image_8.jpeg, E:/ERP_Aone/input_images/image_7.jpeg, and E:/ERP_Aone/input_images/image_6.jpeg
  - Adaptive app icon and Material 3 vibrant theme applied
  - All existing tests pass, build passes, and app does not crash
  - Final stability verified by critic_agent and alignment with project requirements confirmed

