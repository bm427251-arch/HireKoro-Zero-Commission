package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Language
import com.example.data.model.RentCar
import com.example.data.repository.HireKoroRepository
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentACarWall(
    repository: HireKoroRepository,
    language: Language,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onCarBooked: (com.example.data.model.Booking) -> Unit = {}
) {
    val context = LocalContext.current
    val rentCars by repository.rentCars.collectAsState()

    // Filters for Rent a Car
    var selectedTypeFilter by remember { mutableStateOf("All") }
    var selectedFuelFilter by remember { mutableStateOf("All") }
    var selectedTransmissionFilter by remember { mutableStateOf("All") }
    var selectedDriveModeFilter by remember { mutableStateOf("All") }
    var maxPricePerDay by remember { mutableDoubleStateOf(8000.0) }
    var showExtendedFilters by remember { mutableStateOf(false) }

    // Active booking modal
    var carToBook by remember { mutableStateOf<RentCar?>(null) }
    var bookingConfirmationMessage by remember { mutableStateOf<String?>(null) }

    val filteredCars = remember(
        searchQuery,
        selectedTypeFilter,
        selectedFuelFilter,
        selectedTransmissionFilter,
        selectedDriveModeFilter,
        maxPricePerDay,
        rentCars
    ) {
        repository.getFilteredRentCars(
            query = searchQuery,
            typeFilter = selectedTypeFilter,
            fuelFilter = selectedFuelFilter,
            transmissionFilter = selectedTransmissionFilter,
            driveModeFilter = selectedDriveModeFilter,
            maxPricePerDay = maxPricePerDay
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HireKoroBackground)
            .testTag("rent_a_car_wall")
    ) {
        // Car Search Bar (Dedicated to Car Name / Model)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = {
                    Text(
                        text = "Search car name or model, e.g. 'Innova', 'Swift', 'Thar'...",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.DirectionsCar, contentDescription = "Search Car", tint = HireKoroCharcoal)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("car_search_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = HireKoroYellow,
                    unfocusedBorderColor = Color.LightGray
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Primary Type Filters: All | Hatchback | Sedan | SUV | Luxury
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val carTypes = listOf("All", "Hatchback", "Sedan", "SUV", "Luxury")
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(carTypes) { type ->
                        val isSelected = selectedTypeFilter == type
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedTypeFilter = type },
                            label = {
                                Text(
                                    text = type,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = HireKoroYellow,
                                selectedLabelColor = HireKoroBlack,
                                containerColor = Color.White,
                                labelColor = HireKoroCharcoal
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) HireKoroYellow else Color(0xFFE0E0E0)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Toggle More Filters
                IconButton(
                    onClick = { showExtendedFilters = !showExtendedFilters },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (showExtendedFilters) HireKoroYellow else Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "More Filters",
                        tint = HireKoroCharcoal,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Extended Collapsible Filters (Fuel, Transmission, Drive Mode, Price Slider)
            AnimatedVisibility(visible = showExtendedFilters) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        // Fuel Row
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Fuel: ", fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(70.dp))
                            val fuels = listOf("All", "Petrol", "Diesel", "CNG")
                            fuels.forEach { f ->
                                val sel = selectedFuelFilter == f
                                Surface(
                                    onClick = { selectedFuelFilter = f },
                                    color = if (sel) HireKoroCharcoal else Color(0xFFF0F0F0),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.padding(horizontal = 3.dp)
                                ) {
                                    Text(
                                        text = f,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (sel) HireKoroYellow else Color.Black,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Transmission Row
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Gear: ", fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(70.dp))
                            val trans = listOf("All", "Manual", "Automatic")
                            trans.forEach { t ->
                                val sel = selectedTransmissionFilter == t
                                Surface(
                                    onClick = { selectedTransmissionFilter = t },
                                    color = if (sel) HireKoroCharcoal else Color(0xFFF0F0F0),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.padding(horizontal = 3.dp)
                                ) {
                                    Text(
                                        text = t,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (sel) HireKoroYellow else Color.Black,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Drive Mode Row
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Drive: ", fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(70.dp))
                            val modes = listOf("All", "Self Drive", "With Driver")
                            modes.forEach { m ->
                                val sel = selectedDriveModeFilter == m
                                Surface(
                                    onClick = { selectedDriveModeFilter = m },
                                    color = if (sel) HireKoroYellow else Color(0xFFF0F0F0),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.padding(horizontal = 3.dp)
                                ) {
                                    Text(
                                        text = m,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (sel) HireKoroBlack else Color.Black,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Price Slider
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Max Price / Day:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("₹${maxPricePerDay.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = HireKoroYellowDark)
                        }
                        Slider(
                            value = maxPricePerDay.toFloat(),
                            onValueChange = { maxPricePerDay = it.toDouble() },
                            valueRange = 800f..8000f,
                            steps = 10,
                            colors = SliderDefaults.colors(
                                thumbColor = HireKoroYellow,
                                activeTrackColor = HireKoroCharcoal
                            )
                        )
                    }
                }
            }

            if (bookingConfirmationMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = HireKoroGreen.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = HireKoroGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = bookingConfirmationMessage ?: "",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = HireKoroGreen
                        )
                    }
                }
            }
        }

        // CAR LISTINGS (16:9 large image cards)
        if (filteredCars.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "No cars found for '$searchQuery'",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = HireKoroCharcoal
                    )
                    Text("Try adjusting car type, fuel, or price filters", fontSize = 12.sp, color = Color.Gray)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(filteredCars) { car ->
                    RentCarCard(
                        car = car,
                        onCallOwner = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${car.ownerPhone.replace(" ", "")}"))
                            try { context.startActivity(intent) } catch (_: Exception) {}
                        },
                        onRentNow = { carToBook = car }
                    )
                }
            }
        }
    }

    // Rent Car Booking Modal
    carToBook?.let { car ->
        RentCarBookingDialog(
            car = car,
            onDismiss = { carToBook = null },
            onBookingConfirmed = { booking ->
                carToBook = null
                bookingConfirmationMessage = "Rental Confirmed for ${booking.vehicle}! Advance paid via Razorpay. Direct deal linked with owner."
                onCarBooked(booking)
            }
        )
    }
}

@Composable
fun RentCarCard(
    car: RentCar,
    onCallOwner: () -> Unit,
    onRentNow: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("car_card_${car.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            // 1. 16:9 Aspect Ratio Hero Banner for Car
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                    .background(
                        Brush.linearGradient(
                            colors = when (car.carType) {
                                "Luxury" -> listOf(Color(0xFF1E1E24), Color(0xFF2B2B36))
                                "SUV" -> listOf(Color(0xFF2C3E50), Color(0xFF34495E))
                                "Sedan" -> listOf(Color(0xFF2B2B2B), Color(0xFF3F3F46))
                                else -> listOf(Color(0xFF374151), Color(0xFF1F2937))
                            }
                        )
                    )
                    .padding(14.dp)
            ) {
                // Top Tag Badges inside 16:9 image
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Left Badges: Availability + Car Type
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        // Available Now with pulsating green indicator
                        Surface(
                            color = HireKoroGreen,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Available Now",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Car Type Badge
                        Surface(
                            color = HireKoroYellow,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = car.carType.uppercase(),
                                color = HireKoroBlack,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    // Right Badge: Rating
                    Surface(
                        color = Color.Black.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = HireKoroYellow, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "${car.rating} (${car.reviewCount})",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Center Graphic Visual of Car
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = when (car.carType) {
                                "Luxury" -> Icons.Default.DirectionsCar
                                "SUV" -> Icons.Default.AirportShuttle
                                else -> Icons.Default.DirectionsCar
                            },
                            contentDescription = car.carName,
                            tint = HireKoroYellow,
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = car.carModel,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Bottom Overlay inside 16:9: Drive Options (Self Drive & With Driver)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        if (car.selfDriveAvailable) {
                            Surface(
                                color = Color.White.copy(alpha = 0.25f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "✓ Self Drive",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        if (car.withDriverAvailable) {
                            Surface(
                                color = Color.White.copy(alpha = 0.25f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "✓ With Driver",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    // Owner Type
                    Text(
                        text = "${car.ownerType} Owner",
                        color = Color.LightGray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // 2. Card Content & Specifications
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = car.carName,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = HireKoroCharcoal
                        )
                        Text(
                            text = "Managed by ${car.ownerName} (${car.ownerType})",
                            fontSize = 12.sp,
                            color = Color(0xFF666666)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Specs Pills: Fuel | Transmission | Seats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SpecPill(icon = Icons.Default.LocalGasStation, label = car.fuel)
                    SpecPill(icon = Icons.Default.Settings, label = car.transmission)
                    SpecPill(icon = Icons.Default.AirlineSeatReclineNormal, label = car.seats)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Location row - purely textual distance
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = HireKoroYellowDark,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${car.distanceKm} km away - ${car.location}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF555555)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFFEEEEEE))
                Spacer(modifier = Modifier.height(10.dp))

                // 3. Price Box (Per Hour, Per Day, Per Km)
                Text(
                    text = "RENTAL RATES (Zero Commission):",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    CarRateBox(label = "Per Hour", amount = "₹${car.pricePerHour.toInt()}", modifier = Modifier.weight(1f))
                    CarRateBox(label = "Per Day", amount = "₹${car.pricePerDay.toInt()}", isHighlighted = true, modifier = Modifier.weight(1f))
                    CarRateBox(label = "Per Km", amount = "₹${car.pricePerKm.toInt()}", modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 4. Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onCallOwner,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = HireKoroCharcoal),
                        border = androidx.compose.foundation.BorderStroke(1.dp, HireKoroCharcoal),
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Call Owner", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onRentNow,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HireKoroYellow,
                            contentColor = HireKoroBlack
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Key, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Rent Now", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }
    }
}

@Composable
fun SpecPill(icon: ImageVector, label: String) {
    Surface(
        color = Color(0xFFF3F4F6),
        shape = RoundedCornerShape(6.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = HireKoroCharcoal, modifier = Modifier.size(13.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = HireKoroCharcoal)
        }
    }
}

@Composable
fun CarRateBox(label: String, amount: String, isHighlighted: Boolean = false, modifier: Modifier = Modifier) {
    Surface(
        color = if (isHighlighted) Color(0xFFFFF9DB) else Color(0xFFF9FAFB),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isHighlighted) HireKoroYellowDark else Color(0xFFE5E7EB)
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = amount,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (isHighlighted) HireKoroCharcoal else Color(0xFF374151)
            )
            Text(
                text = label,
                fontSize = 10.sp,
                color = Color.Gray
            )
        }
    }
}

// 5. Booking Flow Modal for Rent a Car
@Composable
fun RentCarBookingDialog(
    car: RentCar,
    onDismiss: () -> Unit,
    onBookingConfirmed: (com.example.data.model.Booking) -> Unit
) {
    val context = LocalContext.current
    var selectedDays by remember { mutableIntStateOf(1) }
    var isWithDriver by remember { mutableStateOf(false) }
    val baseRent = car.pricePerDay * selectedDays
    val driverAllowance = if (isWithDriver) 500.0 * selectedDays else 0.0
    val totalCost = baseRent + driverAllowance
    val advanceAmount = (totalCost * 0.20).coerceAtLeast(300.0) // 20% advance or min 300

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "Rent ${car.carName}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = HireKoroCharcoal
                )
                Text(
                    text = "${car.carModel} • ${car.carType}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Days selector
                Text("Select Rental Duration:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf(1, 2, 3, 5, 7).forEach { d ->
                        val isSel = selectedDays == d
                        Surface(
                            onClick = { selectedDays = d },
                            color = if (isSel) HireKoroYellow else Color(0xFFEEEEEE),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(width = 46.dp, height = 34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${d}d",
                                    fontSize = 12.sp,
                                    fontWeight = if (isSel) FontWeight.ExtraBold else FontWeight.Medium,
                                    color = HireKoroBlack
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // With Driver / Self Drive Selector
                Text("Select Drive Option:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        onClick = { isWithDriver = false },
                        color = if (!isWithDriver) HireKoroCharcoal else Color(0xFFF3F4F6),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).height(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "Self Drive",
                                color = if (!isWithDriver) HireKoroYellow else Color.Black,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Surface(
                        onClick = { isWithDriver = true },
                        color = if (isWithDriver) HireKoroCharcoal else Color(0xFFF3F4F6),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).height(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "With Driver (+₹500/d)",
                                color = if (isWithDriver) HireKoroYellow else Color.Black,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Total Cost Breakdown
                Surface(
                    color = Color(0xFFF8FAFC),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Base Vehicle Rent (${selectedDays}d):", fontSize = 12.sp, color = Color.Gray)
                            Text("₹${baseRent.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        if (isWithDriver) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Driver Allowance (${selectedDays}d):", fontSize = 12.sp, color = Color.Gray)
                                Text("₹${driverAllowance.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Rental Price:", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                            Text("₹${totalCost.toInt()}", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = HireKoroCharcoal)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Advance via Razorpay (20%):", fontSize = 11.sp, color = Color(0xFF16A34A), fontWeight = FontWeight.Bold)
                            Text("₹${advanceAmount.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF16A34A))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Zero Commission Direct Deal: Remaining balance payable directly to ${car.ownerName} upon vehicle handover.",
                    fontSize = 11.sp,
                    color = Color(0xFF4B5563)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Open Razorpay Link for Advance Payment
                    val paise = (advanceAmount * 100).toLong()
                    val razorpayUrl = "${HireKoroRepository.RAZORPAY_PAYMENT_URL}?amount=$paise"
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(razorpayUrl))
                        context.startActivity(intent)
                    } catch (_: Exception) {}

                    // Confirm Booking
                    val booking = com.example.data.model.Booking(
                        id = "RC-${(1000..9999).random()}",
                        providerId = car.id,
                        providerName = "${car.carName} (${car.ownerName})",
                        providerPhone = car.ownerPhone,
                        profession = "Car Rental (${if (isWithDriver) "With Driver" else "Self Drive"})",
                        vehicle = car.carModel,
                        date = "Today to +$selectedDays days ($selectedDays days)",
                        location = car.location,
                        totalAmount = totalCost,
                        status = com.example.data.model.BookingStatus.ACTIVE,
                        userName = "Customer (You)",
                        userPhone = "+91 98300 11223"
                    )
                    onBookingConfirmed(booking)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = HireKoroYellow,
                    contentColor = HireKoroBlack
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Pay Advance ₹${advanceAmount.toInt()} via Razorpay",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White
    )
}
