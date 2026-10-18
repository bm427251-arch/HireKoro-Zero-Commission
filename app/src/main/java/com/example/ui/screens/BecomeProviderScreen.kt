package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CustomRateBox
import com.example.data.model.Language
import com.example.data.model.ProviderProfile
import com.example.data.model.Screen
import com.example.data.repository.HireKoroRepository
import com.example.ui.theme.*

@Composable
fun BecomeProviderScreen(
    initialCategory: String = "Driver",
    repository: HireKoroRepository,
    language: Language,
    onNavigateBack: () -> Unit,
    onRegistered: () -> Unit
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf(initialCategory) }
    var searchQuery by remember { mutableStateOf("") }

    // Synchronize if initialCategory changed
    LaunchedEffect(initialCategory) {
        selectedCategory = initialCategory
    }

    BackHandler {
        onNavigateBack()
    }

    // Common Inputs
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("Kolkata / Salt Lake") }
    var experienceYears by remember { mutableStateOf("5") }

    // Driver Specific
    var driverVehicleType by remember { mutableStateOf("Sedan & SUV Commercial") }
    var licenseNumber by remember { mutableStateOf("WB-02-2018-0098765") }
    var licenseFrontUrl by remember { mutableStateOf("https://images.unsplash.com/photo-1544620347-c4fd4a3d5957?w=600") }
    var licenseBackUrl by remember { mutableStateOf("https://images.unsplash.com/photo-1544620347-c4fd4a3d5957?w=600") }
    var driverDailyWage by remember { mutableStateOf("800") }
    var driverMonthlySalary by remember { mutableStateOf("22000") }

    // Technician Specific
    val allTrades = listOf(
        "AC Mistri", "Electrician", "Plumber", "Interior Decorator",
        "Carpenter", "Painter", "Tile & Marble Mistri", "Mason / Raj Mistri",
        "CCTV Technician", "Refrigerator Mistri", "Washing Machine Repair", "False Ceiling Specialist"
    )
    var selectedTrade by remember { mutableStateOf("AC Mistri") }
    var tradeVisitingCharge by remember { mutableStateOf("150") }
    var tradeHourlyRate by remember { mutableStateOf("120") }
    var youtubeLink by remember { mutableStateOf("https://youtube.com/@pro_services_india") }
    var facebookLink by remember { mutableStateOf("https://facebook.com/kolkata_mistri_services") }
    var instagramLink by remember { mutableStateOf("https://instagram.com/hirekoro_experts") }

    // Vehicle Owner Specific
    var carModelName by remember { mutableStateOf("Toyota Innova Crysta") }
    var carType by remember { mutableStateOf("SUV") }
    var rcNumber by remember { mutableStateOf("WB06J4432") }
    var insuranceExpiry by remember { mutableStateOf("2027-08-15") }
    var pucValidity by remember { mutableStateOf("2026-12-30") }
    var rentOptionMode by remember { mutableStateOf("Both Self Drive & With Driver") }
    var dailyCarRent by remember { mutableStateOf("2500") }
    var securityDeposit by remember { mutableStateOf("5000") }

    val searchHint = when {
        selectedCategory.contains("Driver", ignoreCase = true) -> "Search Become a Driver"
        selectedCategory.contains("Technician", ignoreCase = true) -> "Search Technician Profession"
        else -> "Search Become Rent a Car Owner"
    }

    val inputStyle = TextStyle(color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    val inputColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        cursorColor = Color.Black,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedBorderColor = HireKoroYellow,
        unfocusedBorderColor = Color(0xFFCCCCCC),
        focusedLabelColor = Color.Black,
        unfocusedLabelColor = Color(0xFF555555)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HireKoroBackground)
            .testTag("become_provider_screen")
    ) {
        // Screen Header with Back Button
        Surface(
            color = HireKoroCharcoal,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.testTag("become_provider_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = when {
                            selectedCategory.contains("Driver", ignoreCase = true) -> "Become a Driver Partner"
                            selectedCategory.contains("Technician", ignoreCase = true) -> "Become a Technician Partner"
                            else -> "Become a Rent a Car Owner"
                        },
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "100% Direct Customer Calls • 0% Commission",
                        fontSize = 11.sp,
                        color = HireKoroYellow
                    )
                }
            }
        }

        // Top Search Bar: Container White Border Grey Search TextField (MUST BLACK TEXT per user request)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFCCCCCC)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Black,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text(searchHint, color = Color(0xFF777777), fontSize = 14.sp) },
                        textStyle = TextStyle(color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        singleLine = true,
                        modifier = Modifier.weight(1f).testTag("become_provider_search_bar")
                    )
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Color.Black)
                        }
                    }
                }
            }

            // On typing, filter the form fields or show suggestions
            if (searchQuery.isNotBlank()) {
                val suggestions = when {
                    selectedCategory.contains("Driver", ignoreCase = true) -> {
                        listOf(
                            "Sedan Driver", "SUV Commercial Driver", "Personal Family Driver",
                            "Heavy Vehicle / LMV Driver", "Daily Duty Driver", "Outstation Driver",
                            "Automatic Luxury Driver"
                        ).filter { it.contains(searchQuery, ignoreCase = true) }
                    }
                    selectedCategory.contains("Technician", ignoreCase = true) -> {
                        allTrades.filter { it.contains(searchQuery, ignoreCase = true) }
                    }
                    else -> {
                        listOf(
                            "Toyota Innova Crysta", "Maruti Swift Dzire", "Mahindra Thar 4x4",
                            "Hyundai Creta SUV", "Maruti Ertiga 7 Seater", "Mahindra Scorpio-N",
                            "Tata Nexon EV", "Honda City Sedan", "Toyota Fortuner 4x4"
                        ).filter { it.contains(searchQuery, ignoreCase = true) }
                    }
                }

                if (suggestions.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, HireKoroYellow),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = "Suggestions (Tap to fill):",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                suggestions.take(3).forEach { item ->
                                    Surface(
                                        onClick = {
                                            if (selectedCategory.contains("Driver", ignoreCase = true)) {
                                                driverVehicleType = item
                                            } else if (selectedCategory.contains("Technician", ignoreCase = true)) {
                                                selectedTrade = item
                                            } else {
                                                carModelName = item
                                            }
                                            searchQuery = ""
                                        },
                                        color = HireKoroYellow.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(6.dp),
                                        border = BorderStroke(1.dp, HireKoroYellow),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = item,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black,
                                            maxLines = 1,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3-Option Quick Switch Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf(
                    Triple("Driver", "Driver", Icons.Default.DirectionsCar),
                    Triple("Technician", "Technician", Icons.Default.Engineering),
                    Triple("Vehicle Owner", "Car Owner", Icons.Default.Key)
                ).forEach { (catId, catLabel, catIcon) ->
                    val isSelected = selectedCategory.contains(catId, ignoreCase = true)
                    Surface(
                        onClick = { selectedCategory = catId },
                        color = if (isSelected) HireKoroYellow else Color.White,
                        border = BorderStroke(1.dp, if (isSelected) HireKoroYellow else Color(0xFFE2E8F0)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).height(38.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Icon(
                                imageVector = catIcon,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = catLabel,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }

        // Form Body Scroll
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Partner Personal Details",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        textStyle = inputStyle,
                        colors = inputColors,
                        label = { Text("Full Name", color = Color.Black) },
                        placeholder = { Text("e.g. Ramesh Kumar", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth().testTag("become_fullname_input"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        textStyle = inputStyle,
                        colors = inputColors,
                        label = { Text("Mobile Phone (+91)", color = Color.Black) },
                        placeholder = { Text("9876543210", color = Color.Gray) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth().testTag("become_phone_input"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = city,
                            onValueChange = { city = it },
                            textStyle = inputStyle,
                            colors = inputColors,
                            label = { Text("City / Town", color = Color.Black) },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = experienceYears,
                            onValueChange = { experienceYears = it },
                            textStyle = inputStyle,
                            colors = inputColors,
                            label = { Text("Exp (Years)", color = Color.Black) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(0.7f),
                            singleLine = true
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // CATEGORY SPECIFIC FORM SECTION
            if (selectedCategory.contains("Driver", ignoreCase = true)) {
                // DRIVER SPECIFIC FORM
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Driver License & Rates",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = driverVehicleType,
                            onValueChange = { driverVehicleType = it },
                            textStyle = inputStyle,
                            colors = inputColors,
                            label = { Text("Vehicle Expertise", color = Color.Black) },
                            placeholder = { Text("e.g. Sedan, SUV, Heavy LMV, Automatic", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = licenseNumber,
                            onValueChange = { licenseNumber = it },
                            textStyle = inputStyle,
                            colors = inputColors,
                            label = { Text("Driving License Number", color = Color.Black) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = driverDailyWage,
                                onValueChange = { driverDailyWage = it },
                                textStyle = inputStyle,
                                colors = inputColors,
                                label = { Text("Daily Rate (₹)", color = Color.Black) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = driverMonthlySalary,
                                onValueChange = { driverMonthlySalary = it },
                                textStyle = inputStyle,
                                colors = inputColors,
                                label = { Text("Monthly (₹)", color = Color.Black) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text("KYC Verification Documents", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = licenseFrontUrl,
                            onValueChange = { licenseFrontUrl = it },
                            textStyle = inputStyle,
                            colors = inputColors,
                            label = { Text("License Front Photo URL", color = Color.Black) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = licenseBackUrl,
                            onValueChange = { licenseBackUrl = it },
                            textStyle = inputStyle,
                            colors = inputColors,
                            label = { Text("License Back Photo URL", color = Color.Black) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            } else if (selectedCategory.contains("Technician", ignoreCase = true)) {
                // TECHNICIAN / MISTRI / INTERIOR DECORATOR SPECIFIC FORM
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Trade, Social Portfolio & Charges",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Select Primary Trade / Profession:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                        Spacer(modifier = Modifier.height(6.dp))

                        // Filterable Trade Chips
                        val filteredTrades = if (searchQuery.isNotBlank()) {
                            allTrades.filter { it.contains(searchQuery, ignoreCase = true) }
                        } else {
                            allTrades
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            filteredTrades.take(3).forEach { trade ->
                                val isSel = selectedTrade == trade
                                Surface(
                                    onClick = { selectedTrade = trade },
                                    color = if (isSel) HireKoroYellow else Color(0xFFF3F4F6),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 8.dp)) {
                                        Text(
                                            text = trade,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = selectedTrade,
                            onValueChange = { selectedTrade = it },
                            textStyle = inputStyle,
                            colors = inputColors,
                            label = { Text("Trade Name (Custom / Exact)", color = Color.Black) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = tradeVisitingCharge,
                                onValueChange = { tradeVisitingCharge = it },
                                textStyle = inputStyle,
                                colors = inputColors,
                                label = { Text("Visiting Fee (₹)", color = Color.Black) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = tradeHourlyRate,
                                onValueChange = { tradeHourlyRate = it },
                                textStyle = inputStyle,
                                colors = inputColors,
                                label = { Text("Hourly Fee (₹)", color = Color.Black) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Portfolio & Social Channels (Boost Leads):", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = youtubeLink,
                            onValueChange = { youtubeLink = it },
                            textStyle = inputStyle,
                            colors = inputColors,
                            label = { Text("YouTube Channel Link", color = Color.Black) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = facebookLink,
                            onValueChange = { facebookLink = it },
                            textStyle = inputStyle,
                            colors = inputColors,
                            label = { Text("Facebook Page Link", color = Color.Black) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = instagramLink,
                            onValueChange = { instagramLink = it },
                            textStyle = inputStyle,
                            colors = inputColors,
                            label = { Text("Instagram Profile Handle", color = Color.Black) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            } else {
                // VEHICLE OWNER / RENT A CAR SPECIFIC FORM
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Vehicle & Rental Documentation",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = carModelName,
                            onValueChange = { carModelName = it },
                            textStyle = inputStyle,
                            colors = inputColors,
                            label = { Text("Car Model & Brand", color = Color.Black) },
                            placeholder = { Text("e.g. Maruti Swift Dzire / Mahindra Thar", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = carType,
                                onValueChange = { carType = it },
                                textStyle = inputStyle,
                                colors = inputColors,
                                label = { Text("Vehicle Type (SUV/Sedan)", color = Color.Black) },
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = rcNumber,
                                onValueChange = { rcNumber = it },
                                textStyle = inputStyle,
                                colors = inputColors,
                                label = { Text("RC Registration No.", color = Color.Black) },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = dailyCarRent,
                                onValueChange = { dailyCarRent = it },
                                textStyle = inputStyle,
                                colors = inputColors,
                                label = { Text("Rent / Day (₹)", color = Color.Black) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = securityDeposit,
                                onValueChange = { securityDeposit = it },
                                textStyle = inputStyle,
                                colors = inputColors,
                                label = { Text("Deposit (₹)", color = Color.Black) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Rental Mode:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Self Drive", "With Driver", "Both").forEach { mode ->
                                val isSel = rentOptionMode.contains(mode)
                                Surface(
                                    onClick = { rentOptionMode = mode },
                                    color = if (isSel) HireKoroYellow else Color(0xFFF3F4F6),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 8.dp)) {
                                        Text(text = mode, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Documents (RC, Insurance & PUC):", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = insuranceExpiry,
                            onValueChange = { insuranceExpiry = it },
                            textStyle = inputStyle,
                            colors = inputColors,
                            label = { Text("Insurance Valid Until", color = Color.Black) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = pucValidity,
                            onValueChange = { pucValidity = it },
                            textStyle = inputStyle,
                            colors = inputColors,
                            label = { Text("PUC Valid Until", color = Color.Black) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // SUBMIT BUTTON
            Button(
                onClick = {
                    val name = if (fullName.isNotBlank()) fullName else "Partner " + (selectedCategory)
                    val pro = if (selectedCategory.contains("Driver", ignoreCase = true)) {
                        driverVehicleType
                    } else if (selectedCategory.contains("Technician", ignoreCase = true)) {
                        selectedTrade
                    } else {
                        "Car Owner: $carModelName"
                    }

                    val newProfile = ProviderProfile(
                        id = "pro_${System.currentTimeMillis() % 10000}",
                        name = name,
                        profession = pro,
                        phone = if (phone.isNotBlank()) phone else "+91 98301 22445",
                        location = city,
                        vehicle = if (selectedCategory.contains("Vehicle Owner", ignoreCase = true)) carModelName else "None",
                        rating = 5.0,
                        reviewCount = 1,
                        verified = true,
                        activeSlab = 2,
                        customRates = CustomRateBox(
                            visitingCharge = tradeVisitingCharge.toDoubleOrNull() ?: 150.0,
                            hourlyRate = tradeHourlyRate.toDoubleOrNull() ?: 120.0,
                            dailyRate = driverDailyWage.toDoubleOrNull() ?: 800.0,
                            perKmRate = 14.0
                        ),
                        licenseFrontUrl = licenseFrontUrl,
                        licenseBackUrl = licenseBackUrl
                    )

                    repository.registerNewProvider(newProfile)
                    Toast.makeText(context, "Partner Profile Activated! Zero Commission Activated.", Toast.LENGTH_LONG).show()
                    onRegistered()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = HireKoroYellow,
                    contentColor = HireKoroBlack
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("submit_become_provider_button")
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = HireKoroBlack)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Submit & Activate Partner Profile",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
