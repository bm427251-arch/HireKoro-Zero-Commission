package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HireKoroRepository {

    // Current App State
    private val _currentLanguage = MutableStateFlow(Language.EN)
    val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()

    private val _currentAppMode = MutableStateFlow(AppMode.USER)
    val currentAppMode: StateFlow<AppMode> = _currentAppMode.asStateFlow()

    private val _currentTab = MutableStateFlow(MainTab.HIRE_DRIVER)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    private val _currentScreen = MutableStateFlow(Screen.HOME)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _screenHistory = mutableListOf<Screen>()

    // Slabs
    private val _subscriptionSlabs = MutableStateFlow(
        listOf(
            SubscriptionSlab(
                slabNumber = 1,
                title = "Slab 1 - Micro & Commute",
                priceMonthly = 199.0,
                trialPrice = 99.0,
                trialDays = 30,
                eligibleProfessions = "Toto, Bike, Labour, Small Mistri",
                leadCount = "Unlimited Direct Calls",
                isPopular = false
            ),
            SubscriptionSlab(
                slabNumber = 2,
                title = "Slab 2 - Pro Drivers & Technicians",
                priceMonthly = 399.0,
                trialPrice = null,
                trialDays = 7,
                eligibleProfessions = "Car Driver, AC Mistri, Painter, Electrician, Plumber",
                leadCount = "Unlimited Direct Calls + Priority Ranking",
                isPopular = true
            ),
            SubscriptionSlab(
                slabNumber = 3,
                title = "Slab 3 - Heavy Fleet & Commercial",
                priceMonthly = 699.0,
                trialPrice = null,
                trialDays = 7,
                eligibleProfessions = "Truck, Bus, JCB, Crane, Interior Designer",
                leadCount = "Unlimited Direct Calls + Top Featured",
                isPopular = false
            ),
            SubscriptionSlab(
                slabNumber = 4,
                title = "Slab 4 - Agency & Fleet Masters",
                priceMonthly = 999.0,
                trialPrice = null,
                trialDays = 7,
                eligibleProfessions = "Multi-vehicle Agency, Tour Fleet, Contractor Hub",
                leadCount = "VIP Placement + Multi-Staff Accounts",
                isPopular = false
            )
        )
    )
    val subscriptionSlabs: StateFlow<List<SubscriptionSlab>> = _subscriptionSlabs.asStateFlow()

    // A-Z Professions
    private val _professions = MutableStateFlow(
        listOf(
            Profession("p1", "Car Driver", "Driver", 2, true, "Personal and commercial four wheeler driver"),
            Profession("p2", "JCB Driver", "Driver", 3, true, "Heavy machinery earthmover operator"),
            Profession("p3", "Truck Driver", "Driver", 3, true, "Heavy transport multi-axle vehicle driver"),
            Profession("p4", "Bus Driver", "Driver", 3, true, "Public transit and private bus operator"),
            Profession("p5", "Toto / Auto Driver", "Driver", 1, true, "Local city 3-wheeler commute driver"),
            Profession("p6", "AC Mistri", "Technician", 2, true, "AC installation, cooling gas refill, repair"),
            Profession("p7", "Electrician", "Technician", 2, true, "Domestic and industrial wiring, fault finding"),
            Profession("p8", "Plumber", "Technician", 2, true, "Pipes, water pumps, sanitary fittings"),
            Profession("p9", "Rajmistri (Mason)", "Labour", 1, true, "Brickwork, plaster, tiles and construction"),
            Profession("p10", "Painter", "Labour", 2, true, "Interior, exterior wall painting and polish"),
            Profession("p11", "Carpenter", "Technician", 2, true, "Furniture repair, modular woodwork"),
            Profession("p12", "Labour (Helper)", "Labour", 1, true, "Loading, unloading, house shifting helper"),
            Profession("p13", "Cook", "Personal", 1, true, "Daily domestic cook and catering chef"),
            Profession("p14", "Interior Designer", "Technician", 3, true, "Home layout, 3D design, modular setup"),
            Profession("p15", "Mechanic (Auto)", "Technician", 2, true, "On-spot puncture, battery jumpstart, tune-up")
        )
    )
    val professions: StateFlow<List<Profession>> = _professions.asStateFlow()

    // A-Z Vehicles
    private val _vehicles = MutableStateFlow(
        listOf(
            Vehicle("v1", "Bike / Scooter", "Two Wheeler", 1, true, "1 passenger or courier delivery"),
            Vehicle("v2", "Toto (E-Rickshaw)", "Three Wheeler", 1, true, "4 passengers eco-commute"),
            Vehicle("v3", "Auto Rickshaw", "Three Wheeler", 1, true, "3-4 passengers city transit"),
            Vehicle("v4", "Hatchback (Swift/WagonR)", "Four Wheeler", 2, true, "4 passengers compact car"),
            Vehicle("v5", "Sedan (Dzire/Etios)", "Four Wheeler", 2, true, "4 passengers premium ride"),
            Vehicle("v6", "SUV (Creta/Scorpio)", "Four Wheeler", 2, true, "6-7 passengers luxury & luggage"),
            Vehicle("v7", "Innova / Crysta", "Four Wheeler", 2, true, "7-8 passengers outstation king"),
            Vehicle("v8", "Tempo (Tata Ace / Chota Hathi)", "Commercial", 2, true, "Goods transport up to 1 Ton"),
            Vehicle("v9", "Pickup (Bolero Maxi)", "Commercial", 2, true, "Heavy cargo transport up to 2 Ton"),
            Vehicle("v10", "Mini Truck", "Commercial", 3, true, "Goods transport 3-5 Ton"),
            Vehicle("v11", "Truck (10-12 Wheeler)", "Commercial", 3, true, "Interstate logistics freight"),
            Vehicle("v12", "JCB (Earthmover)", "Heavy Machinery", 3, true, "Excavation, trenching, site leveling"),
            Vehicle("v13", "Crane", "Heavy Machinery", 3, true, "Heavy object lifting and erection"),
            Vehicle("v14", "Tractor", "Heavy Machinery", 3, true, "Farming, trolley haulage, land tilling"),
            Vehicle("v15", "Ambulance (Oxygen Fitted)", "Commercial", 2, true, "24x7 Emergency patient transport"),
            Vehicle("v16", "Bus (32-52 Seater)", "Commercial", 3, true, "Tour package and school transit"),
            Vehicle("v17", "Luxury Car (BMW/Audi/Mercedes)", "Four Wheeler", 4, true, "Weddings, VIP corporate events")
        )
    )
    val vehicles: StateFlow<List<Vehicle>> = _vehicles.asStateFlow()

    // Providers Database
    private val _providers = MutableStateFlow(
        listOf(
            ProviderProfile(
                id = "pro_1",
                name = "Ramesh Mondal",
                phone = "+91 98301 23456",
                profession = "Car Driver",
                vehicle = "Innova / Crysta",
                location = "Dumdum, Kolkata",
                distanceKm = 0.8,
                rating = 4.9,
                reviewCount = 62,
                customRates = CustomRateBox(visitingCharge = 100.0, hourlyRate = 120.0, dailyRate = 950.0, perKmRate = 14.0),
                activeSlab = 2
            ),
            ProviderProfile(
                id = "pro_2",
                name = "Bikramjit Ghosh",
                phone = "+91 98311 98765",
                profession = "JCB Driver",
                vehicle = "JCB (Earthmover)",
                location = "Barasat, Kolkata",
                distanceKm = 2.4,
                rating = 4.8,
                reviewCount = 41,
                customRates = CustomRateBox(visitingCharge = 500.0, hourlyRate = 850.0, dailyRate = 6500.0, perKmRate = 45.0),
                activeSlab = 3
            ),
            ProviderProfile(
                id = "pro_3",
                name = "Sheikh Nasiruddin",
                phone = "+91 97482 11223",
                profession = "AC Mistri",
                vehicle = "Bike / Scooter",
                location = "Salt Lake Sector 5, Kolkata",
                distanceKm = 1.2,
                rating = 4.9,
                reviewCount = 98,
                customRates = CustomRateBox(visitingCharge = 199.0, hourlyRate = 250.0, dailyRate = 1500.0, perKmRate = 10.0),
                activeSlab = 2
            ),
            ProviderProfile(
                id = "pro_4",
                name = "Amitava Roy",
                phone = "+91 91234 56789",
                profession = "Electrician",
                vehicle = "Bike / Scooter",
                location = "Nagerbazar, Dumdum",
                distanceKm = 0.5,
                rating = 4.7,
                reviewCount = 53,
                customRates = CustomRateBox(visitingCharge = 150.0, hourlyRate = 150.0, dailyRate = 1000.0, perKmRate = 10.0),
                activeSlab = 2
            ),
            ProviderProfile(
                id = "pro_5",
                name = "Subhasish Das",
                phone = "+91 90071 54321",
                profession = "Toto / Auto Driver",
                vehicle = "Toto (E-Rickshaw)",
                location = "Dumdum Cantonment",
                distanceKm = 0.3,
                rating = 4.6,
                reviewCount = 28,
                customRates = CustomRateBox(visitingCharge = 50.0, hourlyRate = 100.0, dailyRate = 600.0, perKmRate = 12.0),
                activeSlab = 1
            ),
            ProviderProfile(
                id = "pro_6",
                name = "Manoj Karmakar",
                phone = "+91 98322 77889",
                profession = "Truck Driver",
                vehicle = "Truck (10-12 Wheeler)",
                location = "Dankuni Toll Plaza",
                distanceKm = 5.6,
                rating = 4.5,
                reviewCount = 19,
                customRates = CustomRateBox(visitingCharge = 300.0, hourlyRate = 400.0, dailyRate = 3200.0, perKmRate = 28.0),
                activeSlab = 3
            ),
            ProviderProfile(
                id = "pro_7",
                name = "Kishore Halder",
                phone = "+91 98830 00112",
                profession = "Plumber",
                vehicle = "None",
                location = "Airport Gate 1, Kolkata",
                distanceKm = 1.9,
                rating = 3.2,
                reviewCount = 15,
                isUnderReview = true, // < 3.5 auto under review
                customRates = CustomRateBox(visitingCharge = 180.0, hourlyRate = 180.0, dailyRate = 1100.0, perKmRate = 10.0),
                activeSlab = 2
            )
        )
    )
    val providers: StateFlow<List<ProviderProfile>> = _providers.asStateFlow()

    // Provider Self Profile (Logged in provider for Provider Mode)
    private val _myProviderProfile = MutableStateFlow(
        ProviderProfile(
            id = "pro_self",
            name = "Bharat Mitra Partner",
            phone = "+91 98300 00000",
            profession = "Car Driver",
            vehicle = "Sedan (Dzire/Etios)",
            location = "Dumdum, Kolkata",
            distanceKm = 0.0,
            rating = 4.9,
            reviewCount = 12,
            customRates = CustomRateBox(visitingCharge = 150.0, hourlyRate = 130.0, dailyRate = 950.0, perKmRate = 14.0),
            activeSlab = 2,
            subscriptionExpiry = "2026-11-20",
            isSubscriptionActive = true
        )
    )
    val myProviderProfile: StateFlow<ProviderProfile> = _myProviderProfile.asStateFlow()

    // Bookings
    val currentUserId = "usr_me"
    private val _bookings = MutableStateFlow(
        listOf(
            Booking(
                id = "HK-8921",
                providerId = "pro_1",
                providerName = "Ramesh Mondal",
                providerPhone = "+91 98301 23456",
                profession = "Car Driver",
                vehicle = "Innova / Crysta",
                bookingType = "HIRE_DRIVER",
                date = "15 Oct 2026, 02:30 PM",
                location = "Dumdum to Howrah Station",
                totalAmount = 1450.0,
                status = BookingStatus.COMPLETED,
                reportedIssue = false,
                rated = true,
                rateGiven = 5,
                userId = currentUserId,
                locationShared = true,
                sharedLocationLink = "https://maps.google.com/?q=22.6520,88.4463",
                sharedVia = "SMS",
                sharedAt = "15 Oct 2026, 02:32 PM"
            ),
            Booking(
                id = "HK-8742",
                providerId = "pro_3",
                providerName = "Sheikh Nasiruddin",
                providerPhone = "+91 97482 11223",
                profession = "AC Mistri",
                vehicle = "Bike / Scooter",
                bookingType = "HIRE_TECHNICIAN",
                date = "12 Oct 2026, 11:00 AM",
                location = "Salt Lake, BD Block",
                totalAmount = 650.0,
                status = BookingStatus.COMPLETED,
                reportedIssue = false,
                rated = false,
                userId = currentUserId
            ),
            Booking(
                id = "RC-5521",
                providerId = "car_1",
                providerName = "Maruti Swift Dzire (Amit Roy)",
                providerPhone = "+91 98301 22334",
                profession = "Car Rental (Self Drive)",
                vehicle = "VXi 2022 Sedan",
                bookingType = "RENT_CAR",
                date = "16 Oct 2026 - 19 Oct 2026 (3 days)",
                location = "Salt Lake, Kolkata",
                totalAmount = 4200.0,
                status = BookingStatus.ACTIVE,
                reportedIssue = false,
                rated = false,
                userId = currentUserId
            ),
            Booking(
                id = "HK-9014",
                providerId = "pro_2",
                providerName = "Bikramjit Ghosh",
                providerPhone = "+91 98311 98765",
                profession = "JCB Driver",
                vehicle = "JCB (Earthmover)",
                bookingType = "HIRE_DRIVER",
                date = "16 Oct 2026, 09:00 AM",
                location = "Rajarhat Construction Plot 42",
                totalAmount = 6500.0,
                status = BookingStatus.ACTIVE,
                reportedIssue = false,
                rated = false,
                userId = currentUserId
            )
        )
    )
    val bookings: StateFlow<List<Booking>> = _bookings.asStateFlow()

    // Wallet
    private val _walletBalance = MutableStateFlow(1250.0)
    val walletBalance: StateFlow<Double> = _walletBalance.asStateFlow()

    private val _walletTransactions = MutableStateFlow(
        listOf(
            WalletTransaction("tx_1", "Referral Bonus (Friend Joined)", 50.0, true, "15 Oct 2026", "Referral credit for friend joining with HIRE50BM"),
            WalletTransaction("tx_2", "Milestone 5 Referrals Bonus", 500.0, true, "10 Oct 2026", "Milestone reward for achieving 5 valid referrals!"),
            WalletTransaction("tx_3", "Added via Razorpay", 700.0, true, "05 Oct 2026", "Razorpay Payment captured ref #pay_P9a81"),
            WalletTransaction("tx_4", "Withdrawal to UPI", 500.0, false, "01 Oct 2026", "Transferred to UPI: bm427251@okaxis")
        )
    )
    val walletTransactions: StateFlow<List<WalletTransaction>> = _walletTransactions.asStateFlow()

    private val _withdrawals = MutableStateFlow(
        listOf(
            WithdrawalRequest("wd_1", 500.0, "bm427251@okaxis", "COMPLETED", "01 Oct 2026")
        )
    )
    val withdrawals: StateFlow<List<WithdrawalRequest>> = _withdrawals.asStateFlow()

    // Refer & Earn
    val myReferralCode = "HIRE50BM"
    private val _referralCount = MutableStateFlow(5)
    val referralCount: StateFlow<Int> = _referralCount.asStateFlow()

    private val _referralMilestoneClaimed = MutableStateFlow(true)
    val referralMilestoneClaimed: StateFlow<Boolean> = _referralMilestoneClaimed.asStateFlow()

    private val _referralLeaderboard = MutableStateFlow(
        listOf(
            ReferralLeader(1, "Subhendu Pal", 48, 5300.0),
            ReferralLeader(2, "Rahul Karmakar", 36, 4100.0),
            ReferralLeader(3, "Bikash Sarkar", 29, 3400.0),
            ReferralLeader(4, "Bharat Mitra (You)", 5, 550.0),
            ReferralLeader(5, "Animesh Roy", 4, 400.0)
        )
    )
    val referralLeaderboard: StateFlow<List<ReferralLeader>> = _referralLeaderboard.asStateFlow()

    // Complaints
    private val _complaints = MutableStateFlow(
        listOf(
            Complaint(
                id = "CMP-101",
                bookingId = "HK-8109",
                userName = "Sneha Mukherjee",
                userPhone = "+91 98305 66778",
                providerName = "Kishore Halder",
                providerPhone = "+91 98830 00112",
                reason = "Overcharged",
                description = "Agreed visiting charge was ₹180 but demanded ₹500 on arrival and behaved rudely.",
                status = ComplaintStatus.OPEN,
                attachedEvidence = "Bill Receipt & Chat Screenshot",
                date = "14 Oct 2026",
                adminActionTaken = "Under investigation"
            ),
            Complaint(
                id = "CMP-102",
                bookingId = "HK-7994",
                userName = "Tanmoy Sen",
                userPhone = "+91 94331 22334",
                providerName = "Subhasish Das",
                providerPhone = "+91 90071 54321",
                reason = "Didn't come",
                description = "Driver confirmed booking for 8 AM but did not answer phone and never arrived.",
                status = ComplaintStatus.RESOLVED,
                attachedEvidence = "Call Logs",
                date = "11 Oct 2026",
                adminActionTaken = "Warning issued to provider, ₹100 compensated to user wallet"
            )
        )
    )
    val complaints: StateFlow<List<Complaint>> = _complaints.asStateFlow()

    // Ratings
    private val _ratings = MutableStateFlow(
        listOf(
            RatingItem("rat_1", "HK-8921", "pro_1", "Ramesh Mondal", 5, listOf("On Time", "Good Behaviour", "Fair Rate", "Skilled"), "Excellent driving, very polite and reached on time.", false, "15 Oct 2026"),
            RatingItem("rat_2", "HK-8742", "pro_3", "Sheikh Nasiruddin", 5, listOf("Skilled", "Fair Rate"), "Fixed AC gas leak in 30 minutes, very honest person.", false, "12 Oct 2026"),
            RatingItem("rat_3", "HK-8109", "pro_7", "Kishore Halder", 2, listOf("Overcharged", "Rude"), "Demanded extra money and left work incomplete.", false, "14 Oct 2026")
        )
    )
    val ratings: StateFlow<List<RatingItem>> = _ratings.asStateFlow()

    // Rent Cars Collection ("rent_cars")
    private val _rentCars = MutableStateFlow(
        listOf(
            RentCar(
                id = "car_1",
                carName = "Maruti Swift Dzire",
                carModel = "VXi 2022 Sedan",
                carType = "Sedan",
                fuel = "Petrol",
                seats = "5 Seater",
                transmission = "Manual",
                ownerType = "Individual",
                ownerName = "Amit Roy",
                ownerPhone = "+91 98301 22334",
                pricePerHour = 120.0,
                pricePerDay = 1400.0,
                pricePerKm = 12.0,
                withDriverAvailable = true,
                selfDriveAvailable = true,
                isAvailableNow = true,
                location = "Salt Lake, Kolkata",
                distanceKm = 1.2,
                rating = 4.8,
                reviewCount = 32,
                badge = "Most Popular"
            ),
            RentCar(
                id = "car_2",
                carName = "Toyota Innova Crysta",
                carModel = "ZX 2023 7-Seater Luxury",
                carType = "SUV",
                fuel = "Diesel",
                seats = "7 Seater",
                transmission = "Automatic",
                ownerType = "Agency",
                ownerName = "Kolkata City Fleet",
                ownerPhone = "+91 98311 44556",
                pricePerHour = 280.0,
                pricePerDay = 3200.0,
                pricePerKm = 18.0,
                withDriverAvailable = true,
                selfDriveAvailable = true,
                isAvailableNow = true,
                location = "Dumdum Airport, Kolkata",
                distanceKm = 0.9,
                rating = 4.9,
                reviewCount = 78,
                badge = "Top Rated"
            ),
            RentCar(
                id = "car_3",
                carName = "Hyundai Creta",
                carModel = "SX Turbo 2023 Panoramic",
                carType = "SUV",
                fuel = "Petrol",
                seats = "5 Seater",
                transmission = "Automatic",
                ownerType = "Individual",
                ownerName = "Rahul Sen",
                ownerPhone = "+91 97482 66778",
                pricePerHour = 220.0,
                pricePerDay = 2600.0,
                pricePerKm = 15.0,
                withDriverAvailable = true,
                selfDriveAvailable = true,
                isAvailableNow = true,
                location = "New Town, Action Area 1",
                distanceKm = 2.1,
                rating = 4.9,
                reviewCount = 45,
                badge = "Self-Drive Favorite"
            ),
            RentCar(
                id = "car_4",
                carName = "Maruti Suzuki WagonR",
                carModel = "LXi 1.2L 2023 Eco",
                carType = "Hatchback",
                fuel = "CNG",
                seats = "5 Seater",
                transmission = "Manual",
                ownerType = "Agency",
                ownerName = "Metro Rent-a-Car",
                ownerPhone = "+91 91234 88990",
                pricePerHour = 80.0,
                pricePerDay = 999.0,
                pricePerKm = 9.0,
                withDriverAvailable = true,
                selfDriveAvailable = true,
                isAvailableNow = true,
                location = "Nagerbazar, Dumdum",
                distanceKm = 0.6,
                rating = 4.7,
                reviewCount = 29,
                badge = "Budget Saver"
            ),
            RentCar(
                id = "car_5",
                carName = "BMW 3 Series Gran Limousine",
                carModel = "330Li Luxury Line 2022",
                carType = "Luxury",
                fuel = "Petrol",
                seats = "5 Seater",
                transmission = "Automatic",
                ownerType = "Agency",
                ownerName = "Royal Bengal Prestige Motors",
                ownerPhone = "+91 98830 55667",
                pricePerHour = 650.0,
                pricePerDay = 7500.0,
                pricePerKm = 40.0,
                withDriverAvailable = true,
                selfDriveAvailable = true,
                isAvailableNow = true,
                location = "Park Street, Kolkata",
                distanceKm = 3.5,
                rating = 5.0,
                reviewCount = 19,
                badge = "VIP Luxury"
            ),
            RentCar(
                id = "car_6",
                carName = "Mahindra Thar 4x4",
                carModel = "LX Hard Top Diesel 2023",
                carType = "SUV",
                fuel = "Diesel",
                seats = "4 Seater",
                transmission = "Manual",
                ownerType = "Individual",
                ownerName = "Bikash Adventure Rides",
                ownerPhone = "+91 90071 33445",
                pricePerHour = 260.0,
                pricePerDay = 3000.0,
                pricePerKm = 16.0,
                withDriverAvailable = true,
                selfDriveAvailable = true,
                isAvailableNow = true,
                location = "Rajarhat Expressway",
                distanceKm = 1.8,
                rating = 4.8,
                reviewCount = 56,
                badge = "4x4 Off-Road"
            ),
            RentCar(
                id = "car_7",
                carName = "Tata Altroz",
                carModel = "XZ Plus i-Turbo 2022",
                carType = "Hatchback",
                fuel = "Petrol",
                seats = "5 Seater",
                transmission = "Manual",
                ownerType = "Individual",
                ownerName = "Subhendu Pal",
                ownerPhone = "+91 98322 11009",
                pricePerHour = 100.0,
                pricePerDay = 1200.0,
                pricePerKm = 11.0,
                withDriverAvailable = true,
                selfDriveAvailable = true,
                isAvailableNow = true,
                location = "Ultadanga, Kolkata",
                distanceKm = 1.4,
                rating = 4.7,
                reviewCount = 18,
                badge = "5-Star Safety"
            )
        )
    )
    val rentCars: StateFlow<List<RentCar>> = _rentCars.asStateFlow()

    private val _rentCarBookings = MutableStateFlow<List<RentCarBooking>>(emptyList())
    val rentCarBookings: StateFlow<List<RentCarBooking>> = _rentCarBookings.asStateFlow()

    // Search query state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Filtered providers for Hire Driver & Hire Technician
    fun getFilteredProviders(query: String, tab: MainTab): List<ProviderProfile> {
        val q = query.trim().lowercase(Locale.ROOT)
        val all = _providers.value

        val tabFiltered = when (tab) {
            MainTab.HIRE_DRIVER -> all.filter { it.profession.contains("Driver", ignoreCase = true) }
            MainTab.RENT_CAR -> all // Not used for rent car anymore
            MainTab.HIRE_TECHNICIAN -> all.filter { !it.profession.contains("Driver", ignoreCase = true) }
        }

        if (q.isEmpty()) return tabFiltered

        return tabFiltered.filter { provider ->
            val pName = provider.name.lowercase(Locale.ROOT)
            val pProf = provider.profession.lowercase(Locale.ROOT)
            val pVeh = provider.vehicle.lowercase(Locale.ROOT)
            val pLoc = provider.location.lowercase(Locale.ROOT)

            val queryWords = q.split(" ").filter { it.isNotBlank() }
            queryWords.all { word ->
                pName.contains(word) || pProf.contains(word) || pVeh.contains(word) || pLoc.contains(word)
            }
        }
    }

    // Filtered cars for Rent a Car Wall
    fun getFilteredRentCars(
        query: String,
        typeFilter: String,
        fuelFilter: String,
        transmissionFilter: String,
        driveModeFilter: String,
        maxPricePerDay: Double
    ): List<RentCar> {
        val q = query.trim().lowercase(Locale.ROOT)
        return _rentCars.value.filter { car ->
            // Car Name / Model Search
            val matchesQuery = if (q.isEmpty()) true else {
                val words = q.split(" ").filter { it.isNotBlank() }
                words.all { w ->
                    car.carName.lowercase(Locale.ROOT).contains(w) ||
                    car.carModel.lowercase(Locale.ROOT).contains(w) ||
                    car.carType.lowercase(Locale.ROOT).contains(w) ||
                    car.location.lowercase(Locale.ROOT).contains(w)
                }
            }

            // Type Filter (Hatchback, Sedan, SUV, Luxury)
            val matchesType = typeFilter == "All" || car.carType.equals(typeFilter, ignoreCase = true)

            // Fuel Filter (Petrol, Diesel, CNG)
            val matchesFuel = fuelFilter == "All" || car.fuel.equals(fuelFilter, ignoreCase = true)

            // Transmission Filter (Manual, Automatic)
            val matchesTransmission = transmissionFilter == "All" || car.transmission.equals(transmissionFilter, ignoreCase = true)

            // Drive Mode Filter (With Driver, Self Drive)
            val matchesDriveMode = when (driveModeFilter) {
                "With Driver" -> car.withDriverAvailable
                "Self Drive" -> car.selfDriveAvailable
                else -> true
            }

            // Price filter
            val matchesPrice = car.pricePerDay <= maxPricePerDay

            matchesQuery && matchesType && matchesFuel && matchesTransmission && matchesDriveMode && matchesPrice
        }
    }

    fun bookRentCar(
        car: RentCar,
        fromDate: String,
        toDate: String,
        daysCount: Int,
        isWithDriver: Boolean,
        advanceAmount: Double
    ): RentCarBooking {
        val totalCost = (car.pricePerDay * daysCount) + (if (isWithDriver) 500.0 * daysCount else 0.0)
        val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
        val booking = RentCarBooking(
            id = "RC-${(1000..9999).random()}",
            carId = car.id,
            carName = car.carName,
            carModel = car.carModel,
            fromDate = fromDate,
            toDate = toDate,
            daysCount = daysCount,
            isWithDriver = isWithDriver,
            totalCost = totalCost,
            advancePaid = advanceAmount,
            date = dateStr
        )
        _rentCarBookings.value = listOf(booking) + _rentCarBookings.value

        // Also add to global bookings list so it appears under My Bookings
        val mainBooking = Booking(
            id = booking.id,
            providerId = car.id,
            providerName = "${car.carName} (${car.ownerName})",
            providerPhone = car.ownerPhone,
            profession = "Car Rental (${if (isWithDriver) "With Driver" else "Self Drive"})",
            vehicle = car.carModel,
            date = "$fromDate to $toDate ($daysCount days)",
            location = car.location,
            totalAmount = totalCost,
            status = BookingStatus.ACTIVE,
            userName = "Customer (You)",
            userPhone = "+91 98300 11223"
        )
        _bookings.value = listOf(mainBooking) + _bookings.value

        return booking
    }

    // Actions
    fun setLanguage(language: Language) {
        _currentLanguage.value = language
    }

    fun setAppMode(mode: AppMode) {
        _currentAppMode.value = mode
    }

    fun setTab(tab: MainTab) {
        _currentTab.value = tab
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    private val _becomeProviderInitialCategory = MutableStateFlow("Driver")
    val becomeProviderInitialCategory: StateFlow<String> = _becomeProviderInitialCategory.asStateFlow()

    fun setBecomeProviderInitialCategory(category: String) {
        _becomeProviderInitialCategory.value = category
    }

    fun navigateToBecomeProvider(category: String) {
        _becomeProviderInitialCategory.value = category
        val screen = when (category.lowercase()) {
            "driver" -> Screen.BECOME_DRIVER
            "technician" -> Screen.BECOME_TECHNICIAN
            "vehicle owner", "rentcar owner", "rent a car owner" -> Screen.BECOME_RENT_OWNER
            else -> Screen.BECOME_PROVIDER
        }
        navigateTo(screen)
    }

    fun registerNewProvider(profile: ProviderProfile) {
        val currentList = _providers.value.toMutableList()
        currentList.add(0, profile)
        _providers.value = currentList
        _myProviderProfile.value = profile
        setAppMode(AppMode.PROVIDER)
    }

    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            _screenHistory.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (_screenHistory.isNotEmpty()) {
            _currentScreen.value = _screenHistory.removeAt(_screenHistory.size - 1)
            return true
        }
        return false
    }

    fun updateMyRates(rates: CustomRateBox) {
        _myProviderProfile.value = _myProviderProfile.value.copy(customRates = rates)
    }

    fun addMoneyToWallet(amount: Double) {
        val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
        _walletBalance.value += amount
        val newTx = WalletTransaction(
            id = "tx_${System.currentTimeMillis()}",
            title = "Added via Razorpay",
            amount = amount,
            isCredit = true,
            timestamp = dateStr,
            description = "Razorpay payment captured successfully"
        )
        _walletTransactions.value = listOf(newTx) + _walletTransactions.value
    }

    fun requestWithdrawal(amount: Double, upiId: String): Boolean {
        if (_walletBalance.value < amount || amount <= 0) return false
        val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
        _walletBalance.value -= amount
        val newWd = WithdrawalRequest(
            id = "wd_${System.currentTimeMillis()}",
            amount = amount,
            upiId = upiId,
            status = "PENDING",
            date = dateStr
        )
        val newTx = WalletTransaction(
            id = "tx_${System.currentTimeMillis()}",
            title = "Withdrawal Request",
            amount = amount,
            isCredit = false,
            timestamp = dateStr,
            description = "Pending transfer to UPI $upiId"
        )
        _withdrawals.value = listOf(newWd) + _withdrawals.value
        _walletTransactions.value = listOf(newTx) + _walletTransactions.value
        return true
    }

    private val _fast2SmsApiKey = MutableStateFlow("f2s_live_hirekoro_demo_key_9831")
    val fast2SmsApiKey: StateFlow<String> = _fast2SmsApiKey.asStateFlow()

    fun updateFast2SmsApiKey(key: String) {
        _fast2SmsApiKey.value = key
    }

    fun updateBookingLocationShare(bookingId: String, locationLink: String, via: String) {
        val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
        _bookings.value = _bookings.value.map {
            if (it.id == bookingId) it.copy(
                locationShared = true,
                sharedLocationLink = locationLink,
                sharedVia = via,
                sharedAt = dateStr
            ) else it
        }
    }

    fun bookProvider(provider: ProviderProfile): Booking {
        val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
        val bookingType = if (provider.profession.contains("Driver", ignoreCase = true)) "HIRE_DRIVER" else "HIRE_TECHNICIAN"
        val newBooking = Booking(
            id = "HK-${(1000..9999).random()}",
            providerId = provider.id,
            providerName = provider.name,
            providerPhone = provider.phone,
            profession = provider.profession,
            vehicle = provider.vehicle,
            bookingType = bookingType,
            date = dateStr,
            location = provider.location,
            totalAmount = provider.customRates.visitingCharge,
            status = BookingStatus.ACTIVE,
            userId = currentUserId,
            userName = "Customer (You)",
            userPhone = "+91 98300 11223"
        )
        _bookings.value = listOf(newBooking) + _bookings.value
        return newBooking
    }

    fun completeBooking(bookingId: String): Booking? {
        var completedBooking: Booking? = null
        _bookings.value = _bookings.value.map {
            if (it.id == bookingId) {
                val updated = it.copy(status = BookingStatus.COMPLETED)
                completedBooking = updated
                updated
            } else it
        }
        return completedBooking
    }

    fun getProviderForBooking(booking: Booking): ProviderProfile? {
        return _providers.value.find { it.id == booking.providerId }
            ?: _providers.value.find { it.name.contains(booking.providerName, ignoreCase = true) }
    }

    fun getRentCarForBooking(booking: Booking): RentCar? {
        return _rentCars.value.find { it.id == booking.providerId }
            ?: _rentCars.value.find { booking.providerName.contains(it.ownerName, ignoreCase = true) || booking.vehicle.contains(it.carName, ignoreCase = true) || booking.vehicle.contains(it.carModel, ignoreCase = true) }
    }

    fun submitComplaint(bookingId: String, reason: String, description: String, evidence: String) {
        val booking = _bookings.value.find { it.id == bookingId }
        val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
        val newComplaint = Complaint(
            id = "CMP-${(100..999).random()}",
            bookingId = bookingId,
            userName = "Customer (You)",
            userPhone = "+91 98300 11223",
            providerName = booking?.providerName ?: "Partner",
            providerPhone = "+91 98301 23456",
            reason = reason,
            description = description,
            status = ComplaintStatus.OPEN,
            attachedEvidence = evidence,
            date = dateStr
        )
        _complaints.value = listOf(newComplaint) + _complaints.value

        _bookings.value = _bookings.value.map {
            if (it.id == bookingId) it.copy(reportedIssue = true) else it
        }
    }

    fun submitRating(bookingId: String, stars: Int, tags: List<String>, comment: String) {
        val booking = _bookings.value.find { it.id == bookingId }
        val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
        val newRating = RatingItem(
            id = "rat_${System.currentTimeMillis()}",
            bookingId = bookingId,
            providerId = booking?.providerId ?: "",
            providerName = booking?.providerName ?: "",
            userId = currentUserId,
            rating = stars,
            tags = tags,
            comment = comment,
            date = dateStr
        )
        _ratings.value = listOf(newRating) + _ratings.value

        // Update provider average rating and reviewCount
        val providerId = booking?.providerId
        if (providerId != null) {
            val providerRatings = _ratings.value.filter { it.providerId == providerId }
            val count = providerRatings.size
            val avg = if (count > 0) {
                val raw = providerRatings.map { it.rating }.average()
                Math.round(raw * 10.0) / 10.0
            } else stars.toDouble()

            _providers.value = _providers.value.map {
                if (it.id == providerId) {
                    it.copy(
                        rating = avg,
                        reviewCount = count,
                        isUnderReview = (avg < 3.5)
                    )
                } else it
            }
        }

        _bookings.value = _bookings.value.map {
            if (it.id == bookingId) it.copy(rated = true, rateGiven = stars) else it
        }
    }

    fun warnProviderForRating(providerId: String, warningNote: String) {
        _providers.value = _providers.value.map {
            if (it.id == providerId) it.copy(isUnderReview = true) else it
        }
    }

    fun deductProviderPenalty(providerId: String, penaltyAmount: Double, reason: String) {
        val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
        val tx = WalletTransaction(
            id = "tx_${System.currentTimeMillis()}",
            title = "Admin Penalty Deduction",
            amount = penaltyAmount,
            isCredit = false,
            timestamp = dateStr,
            description = "Deducted ₹${penaltyAmount.toInt()} penalty from provider ($reason)"
        )
        _walletTransactions.value = listOf(tx) + _walletTransactions.value
    }

    fun claimMilestoneBonus(): Boolean {
        if (_referralCount.value >= 5 && !_referralMilestoneClaimed.value) {
            _referralMilestoneClaimed.value = true
            _walletBalance.value += 500.0
            val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
            val tx = WalletTransaction(
                id = "tx_${System.currentTimeMillis()}",
                title = "Milestone 5 Referrals Bonus",
                amount = 500.0,
                isCredit = true,
                timestamp = dateStr,
                description = "Claimed ₹500 extra bonus for 5 friend invites!"
            )
            _walletTransactions.value = listOf(tx) + _walletTransactions.value
            return true
        }
        return false
    }

    fun activateSubscription(slabNumber: Int) {
        _myProviderProfile.value = _myProviderProfile.value.copy(
            activeSlab = slabNumber,
            isSubscriptionActive = true,
            subscriptionExpiry = "2026-12-31"
        )
    }

    // Admin Functions
    fun addProfession(name: String, category: String, slab: Int, desc: String) {
        val newP = Profession(
            id = "p_${System.currentTimeMillis()}",
            name = name,
            category = category,
            slabAssigned = slab,
            isEnabled = true,
            description = desc
        )
        _professions.value = _professions.value + newP
    }

    fun toggleProfession(id: String) {
        _professions.value = _professions.value.map {
            if (it.id == id) it.copy(isEnabled = !it.isEnabled) else it
        }
    }

    fun deleteProfession(id: String) {
        _professions.value = _professions.value.filter { it.id != id }
    }

    fun addVehicle(name: String, category: String, slab: Int, capacity: String) {
        val newV = Vehicle(
            id = "v_${System.currentTimeMillis()}",
            name = name,
            category = category,
            slabAssigned = slab,
            isEnabled = true,
            capacity = capacity
        )
        _vehicles.value = _vehicles.value + newV
    }

    fun toggleVehicle(id: String) {
        _vehicles.value = _vehicles.value.map {
            if (it.id == id) it.copy(isEnabled = !it.isEnabled) else it
        }
    }

    fun deleteVehicle(id: String) {
        _vehicles.value = _vehicles.value.filter { it.id != id }
    }

    fun updateSlabPrice(slabNumber: Int, newPrice: Double, newTrialDays: Int) {
        _subscriptionSlabs.value = _subscriptionSlabs.value.map {
            if (it.slabNumber == slabNumber) it.copy(priceMonthly = newPrice, trialDays = newTrialDays) else it
        }
    }

    fun resolveComplaint(complaintId: String, action: String, deductPenalty: Boolean) {
        _complaints.value = _complaints.value.map {
            if (it.id == complaintId) it.copy(status = ComplaintStatus.RESOLVED, adminActionTaken = action) else it
        }
    }

    fun toggleProviderBlock(providerId: String) {
        _providers.value = _providers.value.map {
            if (it.id == providerId) it.copy(isBlocked = !it.isBlocked) else it
        }
    }

    fun flagRatingFake(ratingId: String) {
        _ratings.value = _ratings.value.map {
            if (it.id == ratingId) it.copy(isFlaggedFake = !it.isFlaggedFake) else it
        }
    }

    companion object {
        const val RAZORPAY_PAYMENT_URL = "https://razorpay.me/@bharatmitrainfotech"
        const val ADMIN_EMAIL = "bm427251@gmail.com"
    }
}
