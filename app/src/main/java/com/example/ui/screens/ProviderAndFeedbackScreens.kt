package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import android.widget.Toast
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.LocalizationManager
import com.example.data.model.ComplaintStatus
import com.example.data.model.Language
import com.example.data.model.Screen
import com.example.data.repository.HireKoroRepository
import com.example.ui.theme.*

@Composable
fun ProviderDashboardScreen(
    repository: HireKoroRepository,
    language: Language,
    onNavigate: (Screen) -> Unit
) {
    val myProfile by repository.myProviderProfile.collectAsState()
    var isAvailable by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HireKoroBackground)
            .padding(16.dp)
            .testTag("provider_dashboard_screen")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Provider Partner Dashboard",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = HireKoroCharcoal
                )
                Text(
                    text = "${myProfile.name} • ${myProfile.profession}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            // Online/Offline switch
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (isAvailable) "ONLINE" else "OFFLINE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isAvailable) HireKoroGreen else Color.Gray
                )
                Spacer(modifier = Modifier.width(6.dp))
                Switch(
                    checked = isAvailable,
                    onCheckedChange = { isAvailable = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = HireKoroYellow,
                        checkedTrackColor = HireKoroCharcoal
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Metrics Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricCard(
                title = "Total Earnings",
                value = "₹18,450",
                sub = "100% Zero Commission",
                color = HireKoroCharcoal,
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "Direct Calls",
                value = "142",
                sub = "Leads this month",
                color = HireKoroCharcoal,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricCard(
                title = "Partner Rating",
                value = "${myProfile.rating} ★",
                sub = "${myProfile.reviewCount} customer reviews",
                color = HireKoroCharcoal,
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "Current Slab",
                value = "Slab #${myProfile.activeSlab}",
                sub = "Valid till ${myProfile.subscriptionExpiry}",
                color = HireKoroCharcoal,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Quick Provider Actions",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = HireKoroCharcoal
        )

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Button(
                    onClick = { onNavigate(Screen.MY_RATES) },
                    colors = ButtonDefaults.buttonColors(containerColor = HireKoroYellow, contentColor = HireKoroBlack),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.PriceChange, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Edit Custom Rates (Visiting, Hourly, Daily, Km)", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { onNavigate(Screen.SUBSCRIPTION_PLANS) },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.CardMembership, contentDescription = null, tint = HireKoroCharcoal)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Manage Zero Commission Subscription Slabs", color = HireKoroCharcoal)
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { onNavigate(Screen.WALLET) },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = HireKoroCharcoal)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Request UPI Payout / Check Wallet", color = HireKoroCharcoal)
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    sub: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = title, fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = sub, fontSize = 10.sp, color = Color(0xFF666666))
        }
    }
}

@Composable
fun ComplaintsScreen(
    repository: HireKoroRepository,
    language: Language
) {
    val complaints by repository.complaints.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HireKoroBackground)
            .padding(16.dp)
            .testTag("complaints_screen")
    ) {
        Text(
            text = "Complaints & Issue Resolution",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = HireKoroCharcoal
        )
        Text(
            text = "Real-time grievance tracking with direct admin resolution",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(complaints) { cmp ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Ticket #${cmp.id} • Booking #${cmp.bookingId}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Surface(
                                color = when (cmp.status) {
                                    ComplaintStatus.OPEN -> HireKoroRed.copy(alpha = 0.15f)
                                    ComplaintStatus.IN_PROGRESS -> HireKoroYellow.copy(alpha = 0.3f)
                                    ComplaintStatus.RESOLVED -> HireKoroGreen.copy(alpha = 0.15f)
                                },
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = cmp.status.name,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = when (cmp.status) {
                                        ComplaintStatus.OPEN -> HireKoroRed
                                        ComplaintStatus.IN_PROGRESS -> HireKoroCharcoal
                                        ComplaintStatus.RESOLVED -> HireKoroGreen
                                    },
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Reason: ${cmp.reason}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp,
                            color = HireKoroCharcoal
                        )
                        Text(
                            text = "Partner: ${cmp.providerName} (${cmp.providerPhone})",
                            fontSize = 13.sp,
                            color = Color(0xFF444444)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = cmp.description,
                            fontSize = 12.sp,
                            color = Color(0xFF555555)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Evidence: ${cmp.attachedEvidence} • Date: ${cmp.date}",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            color = Color(0xFFF0F4F8),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Admin Action: ${cmp.adminActionTaken}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = HireKoroCharcoal,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RatingsScreen(
    repository: HireKoroRepository,
    language: Language
) {
    val ratings by repository.ratings.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HireKoroBackground)
            .padding(16.dp)
            .testTag("ratings_screen")
    ) {
        Text(
            text = "Ratings & Reviews",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = HireKoroCharcoal
        )
        Text(
            text = "Providers below 3.5 automatically flagged for review",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(ratings) { r ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = r.providerName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = HireKoroCharcoal
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                repeat(r.rating) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = HireKoroYellowDark,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Tags
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            r.tags.forEach { tag ->
                                Surface(
                                    color = HireKoroYellow.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = tag,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = HireKoroCharcoal,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        if (r.comment.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "\"${r.comment}\"",
                                fontSize = 12.sp,
                                color = Color(0xFF444444)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Booking #${r.bookingId} • ${r.date}",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProviderDocumentsScreen(
    repository: HireKoroRepository,
    language: Language
) {
    val myProfile by repository.myProviderProfile.collectAsState()
    val context = LocalContext.current

    val docs = remember {
        listOf(
            Triple("Driving License (Front & Back)", "DL-WB-2018-09876", "Verified by HireKoro Trust Safety"),
            Triple("Government Aadhaar KYC", "Aadhaar ending with **** 8821", "Biometrically Verified"),
            Triple("Vehicle RC Book Registration", "Commercial Transport RC", "Verified & Active"),
            Triple("Comprehensive Commercial Insurance", "Policy #77882910-BAGIC", "Valid till 15 Aug 2027"),
            Triple("Pollution Certificate (PUC)", "PUC Certificate #WB06-9921", "Valid till 30 Dec 2026"),
            Triple("Work Portfolio & Trade Certification", "Technical Skill Verified", "5+ Years Experience Badge")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HireKoroBackground)
            .padding(16.dp)
            .testTag("provider_documents_screen")
    ) {
        Text(
            text = "My Uploaded Verification Documents",
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color = HireKoroCharcoal
        )
        Text(
            text = "Partner: ${myProfile.name} • ${myProfile.profession} (Zero Commission Verified)",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(docs) { (title, idNum, status) ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = HireKoroYellow,
                            shape = CircleShape,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.VerifiedUser,
                                    contentDescription = null,
                                    tint = HireKoroBlack,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.Black
                            )
                            Text(
                                text = idNum,
                                fontSize = 11.sp,
                                color = Color(0xFF555555)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "✓ $status",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF16A34A)
                            )
                        }

                        IconButton(
                            onClick = {
                                Toast.makeText(context, "$title is active and encrypted.", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = "View",
                                tint = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}

