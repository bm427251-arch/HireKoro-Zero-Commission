# HireKoro - Zero Commission On-Demand Platform

Production-ready cross-platform application (Android, iOS, Web) built with a zero-commission direct booking model.

## Core Features
1. **Zero Commission Model**:
   - 0% platform cuts on rides or services. Customers pay providers directly via Cash or UPI.
   - Fixed monthly subscription slabs for providers:
     - **Slab 1 (₹199/mo)**: Toto, Bike, Labour, Small Mistri (First Month Trial ₹99)
     - **Slab 2 (₹399/mo)**: Car Driver, AC Mistri, Painter, Electrician (7 Days Free Trial)
     - **Slab 3 (₹699/mo)**: Truck, Bus, JCB, Crane, Interior Designer (7 Days Free Trial)
     - **Slab 4 (₹999/mo)**: Agency & Multi-Vehicle Fleets (7 Days Free Trial)
2. **Compound A-Z Search**:
   - Search professions, vehicles, or compound terms (e.g., `JCB Driver` filters both JCB vehicles and JCB drivers).
   - Distance shown purely in mathematical text (e.g. `0.8 km away - Dumdum, Kolkata`), never opening map views.
3. **Custom Rate Box**:
   - Every provider sets 4 customizable fields: **Visiting Charge (₹)**, **Hourly Rate (₹/hr)**, **Daily Rate (₹/day)**, and **Per Km Rate (₹/km)**.
4. **Secret Admin Portal**:
   - Long press the header **H Logo for 7 seconds** with a circular progress indicator.
   - Restricted strictly to authorized administrator email: `bm427251@gmail.com`.
5. **Wallet & Refer & Earn**:
   - Code `HIRE50BM`: Friend signup earns User ₹50, Provider ₹100.
   - Milestone: 5 Referrals unlocks an extra ₹500 bonus.
   - Instant payouts to UPI.

---

## Getting Started & Build Instructions

### 1. Install Dependencies
```bash
flutter pub get
```

### 2. Configure Firebase
Connect your Firebase project using the FlutterFire CLI:
```bash
flutterfire configure
```
This updates `lib/firebase_options.dart` with your live Firebase configuration.

### 3. Deploy Cloud Functions & Setup Razorpay Webhook
Deploy Firebase Cloud Functions:
```bash
firebase deploy --only functions
```
Copy your function endpoint:
```
https://<region>-<project-id>.cloudfunctions.net/verifyRazorpayPayment
```
In your **Razorpay Dashboard**:
1. Navigate to **Settings > Webhooks > Add New Webhook**.
2. Paste the Webhook URL: `https://<your-cloud-function-url>/verifyRazorpayPayment`.
3. Select active event: `payment.captured`.
4. Payment link destination: `https://razorpay.me/@bharatmitrainfotech`.

### 4. Run on Web (Chrome)
```bash
flutter run -d chrome
```

### 5. Build Android Release APK Locally
```bash
flutter build apk --release
```
The output APK will be generated at `build/app/outputs/flutter-apk/app-release.apk`.

### 6. Automated APK Builds via GitHub Actions
Every push to the `main` branch triggers `.github/workflows/build-apk.yml`.
To download without a local environment:
1. Push your changes to your GitHub repository.
2. Go to the **Actions** tab in GitHub.
3. Click on the latest workflow run: **Build HireKoro APK**.
4. Scroll to **Artifacts** and download `app-release.apk`.
