package com.example.ui.components

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.example.data.model.Booking
import com.example.data.repository.HireKoroRepository
import com.example.ui.theme.*

enum class BookingPromptStep {
    CONFIRM_AND_SHARE_LOCATION,
    VERIFIED_DOCUMENTS
}

@Composable
fun ShareLocationPromptDialog(
    booking: Booking,
    repository: HireKoroRepository,
    onDismiss: () -> Unit,
    onLocationShared: (link: String, via: String) -> Unit
) {
    val context = LocalContext.current
    var currentStep by remember { mutableStateOf(BookingPromptStep.CONFIRM_AND_SHARE_LOCATION) }

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var isLocationReady by remember { mutableStateOf(false) }
    var locationLink by remember { mutableStateOf("https://maps.google.com/?q=22.6520,88.4463") }
    var smsBodyText by remember { mutableStateOf("") }

    fun buildSmsText(lat: Double, lng: Double): String {
        val link = "https://maps.google.com/?q=$lat,$lng"
        locationLink = link
        val body = "HireKoro Booking Confirmed!\n" +
                "Booking ID: ${booking.id}\n" +
                "Name: ${booking.userName}\n" +
                "My Location: $link\n" +
                "Please come. Call: ${booking.userPhone}"
        smsBodyText = body
        return body
    }

    fun obtainLocationAndPrepare() {
        val locManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        var bestLocation: Location? = null
        try {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                val gpsLoc = locManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                val netLoc = locManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                bestLocation = gpsLoc ?: netLoc
            }
        } catch (_: Exception) {}

        val lat = bestLocation?.latitude ?: 22.6520
        val lng = bestLocation?.longitude ?: 88.4463
        buildSmsText(lat, lng)
        isLocationReady = true
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        if (granted) {
            obtainLocationAndPrepare()
        } else {
            // Gracefully use approximate / city coords
            buildSmsText(22.6520, 88.4463)
            isLocationReady = true
            Toast.makeText(context, "Using default location pin (Dumdum)", Toast.LENGTH_SHORT).show()
        }
    }

    val cleanPhone = booking.providerPhone.replace(" ", "").replace("-", "")

    if (currentStep == BookingPromptStep.CONFIRM_AND_SHARE_LOCATION) {
        AlertDialog(
            onDismissRequest = {
                // If user clicks outside or cancels, proceed to step C (Verified Documents)
                currentStep = BookingPromptStep.VERIFIED_DOCUMENTS
            },
            icon = {
                Surface(
                    color = HireKoroYellow.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.ShareLocation,
                            contentDescription = null,
                            tint = HireKoroCharcoal,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            },
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Share Location with Partner?",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 17.sp,
                        color = HireKoroCharcoal
                    )
                    Text(
                        text = "Booking #${booking.id} • ${booking.providerName}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (!isLocationReady) {
                        Text(
                            text = "Would you like to send your exact Google Maps location link to ${booking.providerName} via SMS or WhatsApp so they can arrive at your doorstep without delay?",
                            fontSize = 13.sp,
                            color = Color(0xFF4B5563),
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            color = Color(0xFFF9FAFB),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = HireKoroYellowDark, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Zero Commission Direct Deal: Free SMS link or instant WhatsApp dispatch.",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    } else {
                        // Location generated - choose SMS or WhatsApp
                        Text(
                            text = "Location link generated! Choose dispatch method:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = HireKoroCharcoal
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = Color(0xFFF0FDF4),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "Preview Message:",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF16A34A)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = smsBodyText,
                                    fontSize = 11.sp,
                                    color = Color(0xFF1E293B),
                                    lineHeight = 15.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Buttons: Open SMS App & Open WhatsApp
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    val encodedBody = Uri.encode(smsBodyText)
                                    val smsUri = Uri.parse("sms:$cleanPhone?body=$encodedBody")
                                    val smsIntent = Intent(Intent.ACTION_VIEW, smsUri)
                                    try {
                                        context.startActivity(smsIntent)
                                        Toast.makeText(context, "Location link ready to send via SMS", Toast.LENGTH_SHORT).show()
                                        onLocationShared(locationLink, "SMS")
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Could not open SMS app: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                    // Transition to Step C
                                    currentStep = BookingPromptStep.VERIFIED_DOCUMENTS
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = HireKoroCharcoal,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f).height(40.dp)
                            ) {
                                Icon(Icons.Default.Sms, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Open SMS", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = {
                                    val encodedText = Uri.encode(smsBodyText)
                                    val whatsappPhone = cleanPhone.replace("+", "")
                                    val waUri = Uri.parse("https://wa.me/$whatsappPhone?text=$encodedText")
                                    val waIntent = Intent(Intent.ACTION_VIEW, waUri)
                                    try {
                                        context.startActivity(waIntent)
                                        Toast.makeText(context, "Location link ready to send via WhatsApp", Toast.LENGTH_SHORT).show()
                                        onLocationShared(locationLink, "WhatsApp")
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Could not open WhatsApp: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                    // Transition to Step C
                                    currentStep = BookingPromptStep.VERIFIED_DOCUMENTS
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF25D366),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f).height(40.dp)
                            ) {
                                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("WhatsApp", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                if (!isLocationReady) {
                    Button(
                        onClick = {
                            if (!hasPermission) {
                                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            } else {
                                obtainLocationAndPrepare()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HireKoroYellow,
                            contentColor = HireKoroBlack
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("confirm_share_location_btn")
                    ) {
                        Text("YES, Share Location", fontWeight = FontWeight.ExtraBold)
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        // Skip location sharing, transition immediately to Step C
                        currentStep = BookingPromptStep.VERIFIED_DOCUMENTS
                    },
                    modifier = Modifier.testTag("dismiss_share_location_btn")
                ) {
                    Text(if (isLocationReady) "Next: Verified Docs" else "No, Skip", color = Color.Gray)
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White
        )
    } else {
        // Step C: Verified Documents Section Modal
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(20.dp)),
                color = HireKoroBackground,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = Color(0xFFDCFCE7),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color(0xFF15803D),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Booking Confirmed!",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 17.sp,
                                    color = HireKoroCharcoal
                                )
                                Text(
                                    text = "Step C: Safety & Verification Documents",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Step C Component
                    VerifiedDocumentsSection(
                        booking = booking,
                        repository = repository
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HireKoroCharcoal,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("done_verified_docs_btn")
                    ) {
                        Text("Done & Continue", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
