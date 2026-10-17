package com.example.data.model

enum class Language(val code: String, val displayName: String) {
    EN("en", "English"),
    HI("hi", "हिन्दी (Hindi)"),
    BN("bn", "বাংলা (Bengali)"),
    TA("ta", "தமிழ் (Tamil)"),
    TE("te", "తెలుగు (Telugu)"),
    MR("mr", "मराठी (Marathi)"),
    GU("gu", "ગુજરાતી (Gujarati)")
}

enum class AppMode {
    USER,
    PROVIDER
}

enum class MainTab {
    HIRE_DRIVER,
    RENT_CAR,
    HIRE_TECHNICIAN
}

enum class Screen {
    HOME,
    AZ_PROFESSIONS,
    AZ_VEHICLES,
    MY_BOOKINGS,
    WALLET,
    REFER_AND_EARN,
    MY_RATES,
    SUBSCRIPTION_PLANS,
    PROVIDER_DASHBOARD,
    COMPLAINTS,
    RATINGS,
    SETTINGS,
    HELP,
    ADMIN_PANEL
}

data class CustomRateBox(
    val visitingCharge: Double = 150.0,
    val hourlyRate: Double = 120.0,
    val dailyRate: Double = 900.0,
    val perKmRate: Double = 14.0
)

data class Profession(
    val id: String,
    val name: String,
    val category: String, // "Driver", "Technician", "Labour", "Personal"
    val slabAssigned: Int = 1,
    val isEnabled: Boolean = true,
    val description: String = ""
)

data class Vehicle(
    val id: String,
    val name: String,
    val category: String, // "Two Wheeler", "Three Wheeler", "Commercial", "Heavy Machinery", "Four Wheeler"
    val slabAssigned: Int = 1,
    val isEnabled: Boolean = true,
    val capacity: String = "1-4 persons",
    val description: String = ""
)

data class ProviderProfile(
    val id: String,
    val name: String,
    val phone: String,
    val profession: String,
    val vehicle: String = "None",
    val location: String = "Dumdum, Kolkata",
    val distanceKm: Double = 0.8,
    val rating: Double = 4.8,
    val reviewCount: Int = 34,
    val isUnderReview: Boolean = false,
    val isBlocked: Boolean = false,
    val customRates: CustomRateBox = CustomRateBox(),
    val activeSlab: Int = 2,
    val subscriptionExpiry: String = "2026-12-31",
    val isSubscriptionActive: Boolean = true,
    val verified: Boolean = true,
    val licenseFrontUrl: String = "https://hirekoro.in/docs/dl_front.jpg",
    val licenseBackUrl: String = "https://hirekoro.in/docs/dl_back.jpg",
    val aadhaarUrl: String = "https://hirekoro.in/docs/aadhaar.jpg",
    val rcUrl: String = "https://hirekoro.in/docs/rc_book.jpg",
    val insuranceUrl: String = "https://hirekoro.in/docs/insurance.jpg",
    val pucUrl: String = "https://hirekoro.in/docs/puc.jpg",
    val profilePhotoUrl: String = "https://hirekoro.in/docs/profile.jpg",
    val portfolioImages: List<String> = listOf(
        "https://hirekoro.in/docs/work_interior_1.jpg",
        "https://hirekoro.in/docs/work_interior_2.jpg",
        "https://hirekoro.in/docs/work_interior_3.jpg"
    ),
    val carPhotos: List<String> = listOf(
        "https://hirekoro.in/docs/car_front.jpg",
        "https://hirekoro.in/docs/car_back.jpg",
        "https://hirekoro.in/docs/car_side.jpg",
        "https://hirekoro.in/docs/car_interior.jpg"
    ),
    val ownVehicle: Boolean = true
)

data class SubscriptionSlab(
    val slabNumber: Int,
    val title: String,
    val priceMonthly: Double,
    val trialPrice: Double? = null,
    val trialDays: Int = 7,
    val eligibleProfessions: String,
    val leadCount: String = "Unlimited Direct Leads",
    val isPopular: Boolean = false
)

enum class BookingStatus {
    ACTIVE,
    COMPLETED,
    CANCELLED
}

data class Booking(
    val id: String,
    val providerId: String,
    val providerName: String,
    val providerPhone: String = "+91 98301 23456",
    val profession: String,
    val vehicle: String = "",
    val bookingType: String = "HIRE_DRIVER", // "HIRE_DRIVER", "RENT_CAR", "HIRE_TECHNICIAN"
    val date: String,
    val location: String,
    val totalAmount: Double,
    val status: BookingStatus = BookingStatus.COMPLETED,
    val reportedIssue: Boolean = false,
    val rated: Boolean = false,
    val rateGiven: Int = 0,
    val userId: String = "usr_me",
    val userName: String = "Customer",
    val userPhone: String = "+91 98300 11223",
    val locationShared: Boolean = false,
    val sharedLocationLink: String = "",
    val sharedVia: String = "",
    val sharedAt: String = ""
)

data class WalletTransaction(
    val id: String,
    val title: String,
    val amount: Double,
    val isCredit: Boolean,
    val timestamp: String,
    val description: String
)

data class WithdrawalRequest(
    val id: String,
    val amount: Double,
    val upiId: String,
    val status: String,
    val date: String
)

data class ReferralLeader(
    val rank: Int,
    val name: String,
    val count: Int,
    val rewardEarned: Double
)

enum class ComplaintStatus {
    OPEN,
    IN_PROGRESS,
    RESOLVED
}

data class Complaint(
    val id: String,
    val bookingId: String,
    val userName: String,
    val userPhone: String,
    val providerName: String,
    val providerPhone: String,
    val reason: String,
    val description: String,
    val status: ComplaintStatus = ComplaintStatus.OPEN,
    val attachedEvidence: String = "None",
    val date: String,
    val adminActionTaken: String = "Pending Investigation"
)

data class RatingItem(
    val id: String,
    val bookingId: String,
    val providerId: String,
    val providerName: String,
    val rating: Int,
    val tags: List<String>,
    val comment: String,
    val isFlaggedFake: Boolean = false,
    val date: String,
    val userId: String = "usr_me"
)

data class RentCar(
    val id: String,
    val carName: String,
    val carModel: String,
    val carType: String, // "Hatchback", "Sedan", "SUV", "Luxury"
    val fuel: String, // "Petrol", "Diesel", "CNG"
    val seats: String, // "5 Seater", "7 Seater"
    val transmission: String, // "Manual", "Automatic"
    val ownerType: String, // "Individual", "Agency"
    val ownerName: String,
    val ownerPhone: String,
    val pricePerHour: Double,
    val pricePerDay: Double,
    val pricePerKm: Double,
    val withDriverAvailable: Boolean = true,
    val selfDriveAvailable: Boolean = true,
    val isAvailableNow: Boolean = true,
    val location: String,
    val distanceKm: Double,
    val rating: Double = 4.8,
    val reviewCount: Int = 24,
    val badge: String = "Top Rated",
    val rcUrl: String = "https://hirekoro.in/docs/rc_book.jpg",
    val insuranceUrl: String = "https://hirekoro.in/docs/insurance.jpg",
    val pucUrl: String = "https://hirekoro.in/docs/puc.jpg",
    val carPhotos: List<String> = listOf(
        "https://hirekoro.in/docs/car_front.jpg",
        "https://hirekoro.in/docs/car_back.jpg",
        "https://hirekoro.in/docs/car_side.jpg",
        "https://hirekoro.in/docs/car_interior.jpg"
    ),
    val ownerAadhaarUrl: String = "https://hirekoro.in/docs/owner_aadhaar.jpg",
    val ownerProfilePhotoUrl: String = "https://hirekoro.in/docs/owner_profile.jpg"
)

data class RentCarBooking(
    val id: String,
    val carId: String,
    val carName: String,
    val carModel: String,
    val fromDate: String,
    val toDate: String,
    val daysCount: Int,
    val isWithDriver: Boolean,
    val totalCost: Double,
    val advancePaid: Double,
    val status: String = "CONFIRMED",
    val date: String
)
