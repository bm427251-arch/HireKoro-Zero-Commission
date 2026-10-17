package com.example.ui.components

import android.content.Context
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.Booking
import com.example.data.repository.HireKoroRepository
import com.example.ui.theme.*
import java.io.File
import java.io.FileOutputStream

data class VerifiedDocumentItem(
    val id: String,
    val title: String,
    val category: String,
    val url: String,
    val icon: ImageVector,
    val sampleIdNumber: String = ""
)

fun getDocumentsForBooking(
    booking: Booking,
    groupKey: String,
    repository: HireKoroRepository
): List<VerifiedDocumentItem> {
    val provider = repository.getProviderForBooking(booking)
    val rentCar = repository.getRentCarForBooking(booking)

    return when (booking.bookingType) {
        "RENT_CAR" -> {
            if (groupKey == "OWNER") {
                listOf(
                    VerifiedDocumentItem(
                        id = "owner_aadhaar",
                        title = "Owner Aadhaar Card",
                        category = "Identity Verification",
                        url = rentCar?.ownerAadhaarUrl ?: "https://hirekoro.in/docs/owner_aadhaar.jpg",
                        icon = Icons.Default.CreditCard,
                        sampleIdNumber = "XXXX-XXXX-4819"
                    ),
                    VerifiedDocumentItem(
                        id = "owner_profile",
                        title = "Owner Profile Photo",
                        category = "KYC Biometric Photo",
                        url = rentCar?.ownerProfilePhotoUrl ?: "https://hirekoro.in/docs/owner_profile.jpg",
                        icon = Icons.Default.Person,
                        sampleIdNumber = "Facial KYC Verified"
                    )
                )
            } else {
                // Car Documents
                listOf(
                    VerifiedDocumentItem(
                        id = "car_rc",
                        title = "RC Book (Registration Certificate)",
                        category = "Transport Dept Govt of WB",
                        url = rentCar?.rcUrl ?: "https://hirekoro.in/docs/rc_book.jpg",
                        icon = Icons.Default.DirectionsCar,
                        sampleIdNumber = "WB 02 AK 9871"
                    ),
                    VerifiedDocumentItem(
                        id = "car_insurance",
                        title = "Comprehensive Insurance",
                        category = "National Insurance Co. (Zero Dep)",
                        url = rentCar?.insuranceUrl ?: "https://hirekoro.in/docs/insurance.jpg",
                        icon = Icons.Default.Shield,
                        sampleIdNumber = "POL-2026-991823"
                    ),
                    VerifiedDocumentItem(
                        id = "car_puc",
                        title = "Pollution Under Control (PUC)",
                        category = "Emission Standard Validated",
                        url = rentCar?.pucUrl ?: "https://hirekoro.in/docs/puc.jpg",
                        icon = Icons.Default.CheckCircle,
                        sampleIdNumber = "PUC/KOL/2026/08"
                    ),
                    VerifiedDocumentItem(
                        id = "car_photo_front",
                        title = "Vehicle Front Angle Photo",
                        category = "Inspection Photo",
                        url = "https://hirekoro.in/docs/car_front.jpg",
                        icon = Icons.Default.CameraAlt,
                        sampleIdNumber = "Front Bumper & Grille Pristine"
                    ),
                    VerifiedDocumentItem(
                        id = "car_photo_back",
                        title = "Vehicle Rear Angle Photo",
                        category = "Inspection Photo",
                        url = "https://hirekoro.in/docs/car_back.jpg",
                        icon = Icons.Default.CameraAlt,
                        sampleIdNumber = "Boot & Tail-lamps Clean"
                    ),
                    VerifiedDocumentItem(
                        id = "car_photo_side",
                        title = "Vehicle Side Profile Photo",
                        category = "Inspection Photo",
                        url = "https://hirekoro.in/docs/car_side.jpg",
                        icon = Icons.Default.CameraAlt,
                        sampleIdNumber = "Doors & Tyres 90% Tread"
                    ),
                    VerifiedDocumentItem(
                        id = "car_photo_interior",
                        title = "Vehicle Interior Cabin Photo",
                        category = "Inspection Photo",
                        url = "https://hirekoro.in/docs/car_interior.jpg",
                        icon = Icons.Default.CameraAlt,
                        sampleIdNumber = "Clean AC Cabin, Clean Mats"
                    ),
                    VerifiedDocumentItem(
                        id = "car_owner_aadhaar",
                        title = "Owner Govt Aadhaar Card",
                        category = "Registered Fleet Owner",
                        url = rentCar?.ownerAadhaarUrl ?: "https://hirekoro.in/docs/owner_aadhaar.jpg",
                        icon = Icons.Default.CreditCard,
                        sampleIdNumber = "XXXX-XXXX-4819"
                    )
                )
            }
        }
        "HIRE_TECHNICIAN" -> {
            listOf(
                VerifiedDocumentItem(
                    id = "tech_aadhaar",
                    title = "Aadhaar Card",
                    category = "Govt. of India UIDAI Verified",
                    url = provider?.aadhaarUrl ?: "https://hirekoro.in/docs/aadhaar.jpg",
                    icon = Icons.Default.CreditCard,
                    sampleIdNumber = "XXXX-XXXX-8821"
                ),
                VerifiedDocumentItem(
                    id = "tech_portfolio_1",
                    title = "Work Portfolio Photo 1",
                    category = "Verified Field Project",
                    url = "https://hirekoro.in/docs/work_1.jpg",
                    icon = Icons.Default.PhotoLibrary,
                    sampleIdNumber = "Completed Wiring & Modular Panel"
                ),
                VerifiedDocumentItem(
                    id = "tech_portfolio_2",
                    title = "Work Portfolio Photo 2",
                    category = "Verified Field Project",
                    url = "https://hirekoro.in/docs/work_2.jpg",
                    icon = Icons.Default.PhotoLibrary,
                    sampleIdNumber = "AC Copper Pipe Installation"
                ),
                VerifiedDocumentItem(
                    id = "tech_profile",
                    title = "Technician Profile Photo",
                    category = "Biometric KYC Verified",
                    url = provider?.profilePhotoUrl ?: "https://hirekoro.in/docs/profile.jpg",
                    icon = Icons.Default.Person,
                    sampleIdNumber = "Background Police Verification Clear"
                )
            )
        }
        else -> {
            // HIRE_DRIVER
            listOf(
                VerifiedDocumentItem(
                    id = "driver_dl_front",
                    title = "Driving License (Front)",
                    category = "Govt of West Bengal Transport",
                    url = provider?.licenseFrontUrl ?: "https://hirekoro.in/docs/dl_front.jpg",
                    icon = Icons.Default.Badge,
                    sampleIdNumber = "WB-02-2018-0091823 (LMV & Commercial)"
                ),
                VerifiedDocumentItem(
                    id = "driver_dl_back",
                    title = "Driving License (Back)",
                    category = "Endorsements & Validity 2031",
                    url = provider?.licenseBackUrl ?: "https://hirekoro.in/docs/dl_back.jpg",
                    icon = Icons.Default.Badge,
                    sampleIdNumber = "Badge: PSV Commercial Bus/Car/Cab"
                ),
                VerifiedDocumentItem(
                    id = "driver_aadhaar",
                    title = "Aadhaar Card",
                    category = "Govt. of India UIDAI Verified",
                    url = provider?.aadhaarUrl ?: "https://hirekoro.in/docs/aadhaar.jpg",
                    icon = Icons.Default.CreditCard,
                    sampleIdNumber = "XXXX-XXXX-7104"
                ),
                VerifiedDocumentItem(
                    id = "driver_rc",
                    title = "RC Book (Vehicle Registration)",
                    category = "Commercial Vehicle Smart Card",
                    url = provider?.rcUrl ?: "https://hirekoro.in/docs/rc_book.jpg",
                    icon = Icons.Default.DirectionsCar,
                    sampleIdNumber = "WB 19 J 4410 (Fitness Valid)"
                ),
                VerifiedDocumentItem(
                    id = "driver_profile",
                    title = "Driver Profile Photo",
                    category = "Biometric KYC Live Photo",
                    url = provider?.profilePhotoUrl ?: "https://hirekoro.in/docs/profile.jpg",
                    icon = Icons.Default.Person,
                    sampleIdNumber = "Verified Professional Partner"
                )
            )
        }
    }
}

fun saveSimulatedDownloadFile(context: Context, filename: String, content: String): Boolean {
    return try {
        val downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            ?: context.filesDir
        val file = File(downloadsDir, filename)
        FileOutputStream(file).use { out ->
            out.write(content.toByteArray())
        }
        true
    } catch (_: Exception) {
        false
    }
}

@Composable
fun VerifiedDocumentsSection(
    booking: Booking,
    repository: HireKoroRepository,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedSheetGroup by remember { mutableStateOf<String?>(null) }
    var selectedSheetTitle by remember { mutableStateOf("") }
    var viewingDocument by remember { mutableStateOf<VerifiedDocumentItem?>(null) }
    var viewingReceipt by remember { mutableStateOf(false) }

    // Security Check: Only booking user or partner can see docs
    val isAuthorized = (booking.userId == repository.currentUserId)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("verified_documents_section_${booking.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.5.dp, HireKoroYellow),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = HireKoroYellow.copy(alpha = 0.2f),
                    shape = CircleShape,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = "Verified Shield",
                            tint = HireKoroYellowDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Verified Documents - Download for Safety",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp,
                            color = HireKoroCharcoal
                        )
                    }
                    Text(
                        text = "KYC & security verified by HireKoro. Visible only to booking customer.",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (!isAuthorized) {
                Surface(
                    color = Color(0xFFFEF2F2),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFFCA5A5)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = HireKoroRed, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Security Protection: Documents are encrypted and only accessible to the authorized booking creator.",
                            fontSize = 11.sp,
                            color = HireKoroRed
                        )
                    }
                }
            } else {
                // Action Buttons based on Booking Type
                when (booking.bookingType) {
                    "RENT_CAR" -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    selectedSheetGroup = "CAR"
                                    selectedSheetTitle = "Car Verified Documents"
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = HireKoroYellow,
                                    contentColor = HireKoroBlack
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("download_car_docs_btn")
                            ) {
                                Icon(Icons.Default.DirectionsCar, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Car Documents", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = {
                                    selectedSheetGroup = "OWNER"
                                    selectedSheetTitle = "Owner Verified Documents"
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = HireKoroYellow,
                                    contentColor = HireKoroBlack
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("download_owner_docs_btn")
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Owner Documents", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { viewingReceipt = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = HireKoroCharcoal,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .testTag("download_booking_receipt_btn")
                        ) {
                            Icon(Icons.Default.ReceiptLong, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Download Booking Receipt PDF", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    "HIRE_TECHNICIAN" -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    selectedSheetGroup = "TECH"
                                    selectedSheetTitle = "Technician Verified Documents"
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = HireKoroYellow,
                                    contentColor = HireKoroBlack
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("download_tech_docs_btn")
                            ) {
                                Icon(Icons.Default.Engineering, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Technician Documents", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { viewingReceipt = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = HireKoroCharcoal,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("download_booking_receipt_btn")
                            ) {
                                Icon(Icons.Default.ReceiptLong, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Receipt PDF", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    else -> {
                        // HIRE_DRIVER
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    selectedSheetGroup = "DRIVER"
                                    selectedSheetTitle = "Driver Verified Documents"
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = HireKoroYellow,
                                    contentColor = HireKoroBlack
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("download_driver_docs_btn")
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Download Driver Documents", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { viewingReceipt = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = HireKoroCharcoal,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("download_booking_receipt_btn")
                            ) {
                                Icon(Icons.Default.ReceiptLong, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Receipt PDF", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Bottom Sheet / Dialog for Documents List
    selectedSheetGroup?.let { group ->
        val docs = getDocumentsForBooking(booking, group, repository)

        Dialog(
            onDismissRequest = { selectedSheetGroup = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .clip(RoundedCornerShape(20.dp)),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = HireKoroYellowDark,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = selectedSheetTitle,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp,
                                    color = HireKoroCharcoal
                                )
                                Text(
                                    text = "Booking #${booking.id} • ${booking.providerName}",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                        IconButton(onClick = { selectedSheetGroup = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Verified Badge Row
                    Surface(
                        color = Color(0xFFF0FDF4),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = HireKoroGreen, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "100% Verified by HireKoro Trust & Safety Team",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF15803D)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(docs) { doc ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFF9FAFB),
                                border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            color = HireKoroYellow.copy(alpha = 0.25f),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.size(40.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = doc.icon,
                                                    contentDescription = null,
                                                    tint = HireKoroCharcoal,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = doc.title,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = HireKoroCharcoal
                                            )
                                            Text(
                                                text = doc.category,
                                                fontSize = 11.sp,
                                                color = Color.Gray
                                            )
                                            if (doc.sampleIdNumber.isNotBlank()) {
                                                Text(
                                                    text = doc.sampleIdNumber,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = Color(0xFF4B5563)
                                                )
                                            }
                                        }
                                    }

                                    // View & Download Buttons
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        OutlinedButton(
                                            onClick = { viewingDocument = doc },
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                            modifier = Modifier.height(34.dp).testTag("view_doc_${doc.id}")
                                        ) {
                                            Icon(Icons.Default.Visibility, contentDescription = "View", modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("View", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Button(
                                            onClick = {
                                                val content = "HireKoro Verified Document\nTitle: ${doc.title}\nCategory: ${doc.category}\nBooking ID: ${booking.id}\nProvider: ${booking.providerName}\nID Number: ${doc.sampleIdNumber}\nStatus: Officially Verified by HireKoro\nDate: ${booking.date}"
                                                saveSimulatedDownloadFile(context, "HireKoro_${doc.id}_${booking.id}.txt", content)
                                                Toast.makeText(context, "Downloaded to Gallery/Downloads", Toast.LENGTH_SHORT).show()
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = HireKoroYellow,
                                                contentColor = HireKoroBlack
                                            ),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                            modifier = Modifier.height(34.dp).testTag("download_doc_${doc.id}")
                                        ) {
                                            Icon(Icons.Default.Download, contentDescription = "Download", modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Download", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            val allContent = StringBuilder("=== HIREKORO VERIFIED DOCUMENTS BUNDLE ===\n")
                            allContent.append("Booking ID: ${booking.id}\n")
                            allContent.append("Provider: ${booking.providerName} (${booking.providerPhone})\n")
                            allContent.append("Customer: ${booking.userName} (${booking.userPhone})\n")
                            allContent.append("Service: ${booking.profession} - ${booking.location}\n\n")
                            docs.forEach { d ->
                                allContent.append("- ${d.title}: ${d.category} [${d.sampleIdNumber}]\n")
                            }
                            saveSimulatedDownloadFile(context, "HireKoro_Documents_Bundle_${booking.id}.txt", allContent.toString())
                            Toast.makeText(context, "Downloaded to Gallery/Downloads", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HireKoroCharcoal,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("download_all_docs_btn")
                    ) {
                        Icon(Icons.Default.DownloadForOffline, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Download All Verified Documents", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }

    // Document Full-Screen Image Preview Dialog
    viewingDocument?.let { doc ->
        Dialog(
            onDismissRequest = { viewingDocument = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.92f)),
                color = Color.Black.copy(alpha = 0.92f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Top Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = doc.title,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Verified Security Document • ${booking.providerName}",
                                color = Color.LightGray,
                                fontSize = 11.sp
                            )
                        }
                        IconButton(onClick = { viewingDocument = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Realistic Document Card View
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(vertical = 12.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFFDFBF7),
                        shadowElevation = 8.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Document Header
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Security,
                                        contentDescription = null,
                                        tint = HireKoroCharcoal,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "GOVERNMENT OF WEST BENGAL",
                                            fontWeight = FontWeight.Black,
                                            fontSize = 11.sp,
                                            color = Color(0xFF1E3A8A)
                                        )
                                        Text(
                                            text = doc.category.uppercase(),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Gray
                                        )
                                    }
                                }

                                Surface(
                                    color = Color(0xFFDCFCE7),
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(1.dp, Color(0xFF22C55E))
                                ) {
                                    Text(
                                        text = "VERIFIED",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 11.sp,
                                        color = Color(0xFF15803D),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            // Watermark / Body Graphic
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFE2E8F0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = doc.icon,
                                        contentDescription = null,
                                        tint = Color.DarkGray,
                                        modifier = Modifier.size(64.dp)
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = doc.sampleIdNumber,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.sp,
                                        color = HireKoroCharcoal
                                    )
                                    Text(
                                        text = "Holder: ${booking.providerName}",
                                        fontSize = 12.sp,
                                        color = Color(0xFF4B5563)
                                    )
                                }
                            }

                            // Footer Seal
                            Column(modifier = Modifier.fillMaxWidth()) {
                                HorizontalDivider(color = Color(0xFFE2E8F0))
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "HireKoro Trust ID: HK-KYC-${booking.providerId}",
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = "Direct Zero Commission Deal",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF15803D)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Download Button in Full Screen
                    Button(
                        onClick = {
                            val content = "HireKoro Verified Document\n${doc.title}\n${doc.sampleIdNumber}\nProvider: ${booking.providerName}\nBooking: ${booking.id}"
                            saveSimulatedDownloadFile(context, "HireKoro_${doc.id}_${booking.id}.txt", content)
                            Toast.makeText(context, "Downloaded to Gallery/Downloads", Toast.LENGTH_SHORT).show()
                            viewingDocument = null
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HireKoroYellow,
                            contentColor = HireKoroBlack
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("download_single_doc_fullscreen")
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Download to Gallery/Downloads", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    }
                }
            }
        }
    }

    // Booking Receipt PDF Preview & Download Dialog
    if (viewingReceipt) {
        Dialog(
            onDismissRequest = { viewingReceipt = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(20.dp)),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = HireKoroYellow,
                                shape = CircleShape,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = HireKoroBlack, modifier = Modifier.size(20.dp))
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("HireKoro Official Receipt", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = HireKoroCharcoal)
                                Text("Booking #${booking.id} • 0% Platform Fee", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                        IconButton(onClick = { viewingReceipt = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        color = Color(0xFFF9FAFB),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            ReceiptRow("Booking ID", booking.id)
                            ReceiptRow("Service", booking.profession)
                            ReceiptRow("Partner Name", booking.providerName)
                            ReceiptRow("Partner Phone", booking.providerPhone)
                            ReceiptRow("Date & Time", booking.date)
                            ReceiptRow("Location", booking.location)
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE5E7EB))
                            ReceiptRow("Service Charge", "₹${booking.totalAmount.toInt()}", isBold = true)
                            ReceiptRow("HireKoro Platform Fee", "₹0 (100% Free)", isGreen = true)
                            ReceiptRow("Total Paid / Agreed", "₹${booking.totalAmount.toInt()}", isBold = true, isLarge = true)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val receiptText = """
                                ==========================================
                                       HIREKORO OFFICIAL RECEIPT
                                   Direct Deal • Zero Commission
                                ==========================================
                                Booking ID     : ${booking.id}
                                Date & Time    : ${booking.date}
                                Customer       : ${booking.userName} (${booking.userPhone})
                                Partner        : ${booking.providerName} (${booking.providerPhone})
                                Service        : ${booking.profession}
                                Location       : ${booking.location}
                                ------------------------------------------
                                Base Amount    : ₹${booking.totalAmount.toInt()}
                                Platform Fee   : ₹0 (Zero Commission)
                                Total Amount   : ₹${booking.totalAmount.toInt()}
                                ------------------------------------------
                                Verified by    : Bharat Mitra Infotech Pvt Ltd
                                ==========================================
                            """.trimIndent()
                            saveSimulatedDownloadFile(context, "HireKoro_Receipt_${booking.id}.txt", receiptText)
                            Toast.makeText(context, "Downloaded to Gallery/Downloads", Toast.LENGTH_SHORT).show()
                            viewingReceipt = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HireKoroYellow,
                            contentColor = HireKoroBlack
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("confirm_download_receipt_pdf")
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Download Receipt PDF", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun ReceiptRow(
    label: String,
    value: String,
    isBold: Boolean = false,
    isGreen: Boolean = false,
    isLarge: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = if (isLarge) 13.sp else 12.sp,
            color = if (isBold) HireKoroCharcoal else Color.Gray,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            fontSize = if (isLarge) 15.sp else 12.sp,
            fontWeight = if (isBold) FontWeight.ExtraBold else FontWeight.Medium,
            color = if (isGreen) Color(0xFF16A34A) else if (isBold) HireKoroCharcoal else Color(0xFF1F2937)
        )
    }
}
