package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.LocalizationManager
import com.example.data.model.Booking
import com.example.data.model.BookingStatus
import com.example.data.model.Language
import com.example.data.repository.HireKoroRepository
import com.example.ui.components.RateProviderDialog
import com.example.ui.components.ReportIssueDialog
import com.example.ui.components.VerifiedDocumentsSection
import com.example.ui.theme.*

@Composable
fun MyBookingsScreen(
    repository: HireKoroRepository,
    language: Language
) {
    val bookings by repository.bookings.collectAsState()
    var selectedBookingForReport by remember { mutableStateOf<Booking?>(null) }
    var selectedBookingForRate by remember { mutableStateOf<Booking?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HireKoroBackground)
            .padding(16.dp)
            .testTag("my_bookings_screen")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = LocalizationManager.getString("menu_bookings", language),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = HireKoroCharcoal
                )
                Text(
                    text = "Track appointments, download verified safety docs & rate partners",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Surface(
                color = HireKoroYellow,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "${bookings.size} Total",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = HireKoroBlack,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (bookings.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No bookings yet. Hire a driver or technician from Home!", color = Color.Gray)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(bookings) { booking ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Booking #${booking.id}",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.sp,
                                        color = HireKoroCharcoal
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        color = HireKoroYellow.copy(alpha = 0.25f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = booking.bookingType.replace("_", " "),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = HireKoroCharcoal,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Surface(
                                    color = when (booking.status) {
                                        BookingStatus.ACTIVE -> HireKoroYellow.copy(alpha = 0.25f)
                                        BookingStatus.COMPLETED -> HireKoroGreen.copy(alpha = 0.15f)
                                        BookingStatus.CANCELLED -> HireKoroRed.copy(alpha = 0.15f)
                                    },
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = booking.status.name,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when (booking.status) {
                                            BookingStatus.ACTIVE -> HireKoroCharcoal
                                            BookingStatus.COMPLETED -> HireKoroGreen
                                            BookingStatus.CANCELLED -> HireKoroRed
                                        },
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = booking.providerName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = HireKoroCharcoal
                            )
                            Text(
                                text = "${booking.profession} ${if (booking.vehicle.isNotBlank()) "• ${booking.vehicle}" else ""}",
                                fontSize = 13.sp,
                                color = Color(0xFF555555)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Schedule, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(booking.date, fontSize = 12.sp, color = Color.Gray)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Place, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(booking.location, fontSize = 12.sp, color = Color.Gray)
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Total: ₹${booking.totalAmount.toInt()}",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp,
                                    color = HireKoroCharcoal
                                )
                                Text(
                                    text = "0% Platform Fee",
                                    fontSize = 11.sp,
                                    color = Color(0xFF2E7D32),
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Step C: Verified Documents Section
                            VerifiedDocumentsSection(
                                booking = booking,
                                repository = repository
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = Color(0xFFEEEEEE))
                            Spacer(modifier = Modifier.height(10.dp))

                            // Interactive Actions: Complete Job, Report Issue & Rate
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (booking.status == BookingStatus.ACTIVE) {
                                    // Complete Job button (immediately marks done and auto opens rating!)
                                    Button(
                                        onClick = {
                                            val completed = repository.completeBooking(booking.id)
                                            if (completed != null) {
                                                selectedBookingForRate = completed
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = HireKoroGreen,
                                            contentColor = Color.White
                                        ),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f).height(40.dp).testTag("complete_job_${booking.id}")
                                    ) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Complete Job", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                } else {
                                    // Rate Provider Button
                                    Button(
                                        onClick = { selectedBookingForRate = booking },
                                        enabled = true,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (booking.rated) Color(0xFFF3F4F6) else HireKoroYellow,
                                            contentColor = if (booking.rated) HireKoroCharcoal else HireKoroBlack
                                        ),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f).height(40.dp).testTag("rate_partner_${booking.id}")
                                    ) {
                                        Icon(Icons.Default.Star, contentDescription = null, tint = if (booking.rated) HireKoroYellowDark else HireKoroBlack, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (booking.rated) "${booking.rateGiven} ★ Rated (Tap to Edit)" else "Rate Experience",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                // Report Issue Button
                                OutlinedButton(
                                    onClick = { selectedBookingForReport = booking },
                                    enabled = !booking.reportedIssue,
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = if (booking.reportedIssue) Color.Gray else HireKoroRed
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f).height(40.dp).testTag("report_issue_${booking.id}")
                                ) {
                                    Icon(
                                        imageVector = if (booking.reportedIssue) Icons.Default.Check else Icons.Default.ReportProblem,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (booking.reportedIssue) "Issue Reported" else "Report Issue",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialogs
    selectedBookingForReport?.let { b ->
        ReportIssueDialog(
            booking = b,
            onIssueReported = { reason, desc, evidence ->
                repository.submitComplaint(b.id, reason, desc, evidence)
                selectedBookingForReport = null
            },
            onDismiss = { selectedBookingForReport = null }
        )
    }

    selectedBookingForRate?.let { b ->
        RateProviderDialog(
            booking = b,
            onRatingSubmitted = { stars, tags, comment ->
                repository.submitRating(b.id, stars, tags, comment)
                selectedBookingForRate = null
            },
            onDismiss = { selectedBookingForRate = null }
        )
    }
}
