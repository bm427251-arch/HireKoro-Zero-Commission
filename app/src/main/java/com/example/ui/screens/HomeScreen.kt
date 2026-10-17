package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.LocalizationManager
import com.example.data.model.Language
import com.example.data.model.MainTab
import com.example.data.model.ProviderProfile
import com.example.data.repository.HireKoroRepository
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    repository: HireKoroRepository,
    currentLanguage: Language,
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    onBookProvider: (ProviderProfile) -> Unit,
    onCarBooked: (com.example.data.model.Booking) -> Unit = {}
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    val providers by repository.providers.collectAsState()

    val filteredList = remember(searchQuery, currentTab, providers) {
        repository.getFilteredProviders(searchQuery, currentTab)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HireKoroBackground)
            .testTag("home_screen")
    ) {
        // Top 3 Fixed Tabs
        Surface(
            color = HireKoroCharcoal,
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MainTab.entries.forEach { tab ->
                    val isSelected = currentTab == tab
                    val title = when (tab) {
                        MainTab.HIRE_DRIVER -> LocalizationManager.getString("hire_driver", currentLanguage)
                        MainTab.RENT_CAR -> LocalizationManager.getString("rent_car", currentLanguage)
                        MainTab.HIRE_TECHNICIAN -> LocalizationManager.getString("hire_technician", currentLanguage)
                    }
                    val icon = when (tab) {
                        MainTab.HIRE_DRIVER -> Icons.Default.DirectionsCar
                        MainTab.RENT_CAR -> Icons.Default.Key
                        MainTab.HIRE_TECHNICIAN -> Icons.Default.Build
                    }

                    Button(
                        onClick = { onTabSelected(tab) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) HireKoroYellow else Color.White,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 3.dp)
                            .height(42.dp)
                            .testTag("tab_${tab.name.lowercase()}")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = icon, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = title,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }

        // TAB CONTENT ROUTING
        if (currentTab == MainTab.RENT_CAR) {
            // RENT A CAR WALL (CAR listings with 16:9 cards, dedicated filters, and booking modal)
            var carSearchQuery by remember { mutableStateOf("") }
            RentACarWall(
                repository = repository,
                language = currentLanguage,
                searchQuery = carSearchQuery,
                onSearchChange = { carSearchQuery = it },
                onCarBooked = onCarBooked
            )
        } else {
            // HIRE DRIVER / HIRE TECHNICIAN WALL (DRIVER / TECHNICIAN profiles with avatar circles)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    placeholder = {
                        Text(
                            text = if (currentTab == MainTab.HIRE_DRIVER)
                                "Search driver by name, vehicle e.g. 'Sedan Driver'..."
                            else
                                "Search technician e.g. 'AC Mistri', 'Electrician'...",
                            fontSize = 13.sp,
                            color = Color(0xFF666666)
                        )
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = Color.Black)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = Color.Black)
                            }
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("home_search_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        cursorColor = Color.Black,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = HireKoroYellow,
                        unfocusedBorderColor = Color(0xFFCCCCCC),
                        focusedLeadingIconColor = Color.Black,
                        unfocusedLeadingIconColor = Color.Black,
                        focusedTrailingIconColor = Color.Black,
                        unfocusedTrailingIconColor = Color.Black
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Zero Commission Direct Model Badge
                Surface(
                    color = HireKoroYellow.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, HireKoroYellow.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = HireKoroCharcoal,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (currentTab == MainTab.HIRE_DRIVER)
                                "Zero Commission Driver Hire: Call & book verified drivers directly with 0% platform cuts."
                            else
                                "Zero Commission Technician Hire: Connect directly with skilled local mistris & electricians.",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = HireKoroCharcoal
                        )
                    }
                }
            }

            // Providers List (Hire Driver / Hire Technician)
            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No providers matching \"$searchQuery\"",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )
                        Text(
                            text = "Try searching for Driver, JCB, Mistri, or Plumber",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(filteredList) { provider ->
                        ProviderCard(
                            provider = provider,
                            language = currentLanguage,
                            onCall = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${provider.phone.replace(" ", "")}"))
                                try {
                                    context.startActivity(intent)
                                } catch (_: Exception) {}
                            },
                            onBook = { onBookProvider(provider) }
                        )
                    }
                }
            }
        } // end of tab routing else
    }
}

@Composable
fun ProviderCard(
    provider: ProviderProfile,
    language: Language,
    onCall: () -> Unit,
    onBook: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("provider_card_${provider.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            // Header Row: Avatar, Name, Rating, Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(HireKoroCharcoal)
                ) {
                    Text(
                        text = provider.name.take(1),
                        color = HireKoroYellow,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = provider.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = HireKoroCharcoal
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        if (provider.verified) {
                            Surface(
                                color = HireKoroGreen.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "VERIFIED",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = HireKoroGreen,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "${provider.profession} ${if (provider.vehicle != "None") "• ${provider.vehicle}" else ""}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF555555)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    // Distance text - purely textual calculation, no map view opened per spec!
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = HireKoroYellowDark,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${provider.distanceKm} km away - ${provider.location}",
                            fontSize = 12.sp,
                            color = Color(0xFF666666),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Rating & Review badge
                Column(horizontalAlignment = Alignment.End) {
                    Surface(
                        color = if (provider.isUnderReview) HireKoroRed.copy(alpha = 0.15f) else HireKoroYellow.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (provider.isUnderReview) HireKoroRed else HireKoroYellowDark,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "${provider.rating}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (provider.isUnderReview) HireKoroRed else HireKoroCharcoal
                            )
                        }
                    }
                    Text(
                        text = "(${provider.reviewCount} reviews)",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                    if (provider.isUnderReview) {
                        Text(
                            text = "Under Review",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = HireKoroRed
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFEEEEEE))
            Spacer(modifier = Modifier.height(10.dp))

            // Custom Rate Box: 4 editable fields (Visiting, Hourly, Daily, Per Km)
            Text(
                text = "PROVIDER RATES (Direct & Fixed):",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                RateChip(
                    label = LocalizationManager.getString("visiting_charge", language),
                    value = "₹${provider.customRates.visitingCharge.toInt()}",
                    modifier = Modifier.weight(1f)
                )
                RateChip(
                    label = LocalizationManager.getString("hourly_rate", language),
                    value = "₹${provider.customRates.hourlyRate.toInt()}/hr",
                    modifier = Modifier.weight(1f)
                )
                RateChip(
                    label = LocalizationManager.getString("daily_rate", language),
                    value = "₹${provider.customRates.dailyRate.toInt()}/day",
                    modifier = Modifier.weight(1f)
                )
                RateChip(
                    label = LocalizationManager.getString("per_km_rate", language),
                    value = "₹${provider.customRates.perKmRate.toInt()}/km",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onCall,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = HireKoroCharcoal),
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                ) {
                    Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = LocalizationManager.getString("direct_call", language),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = onBook,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HireKoroYellow,
                        contentColor = HireKoroBlack
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                ) {
                    Icon(imageVector = Icons.Default.ElectricBolt, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = LocalizationManager.getString("book_now", language),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Composable
fun RateChip(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFFF9F9F9),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E2E2)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = HireKoroCharcoal
            )
            Text(
                text = label,
                fontSize = 9.sp,
                color = Color(0xFF777777),
                maxLines = 1
            )
        }
    }
}
