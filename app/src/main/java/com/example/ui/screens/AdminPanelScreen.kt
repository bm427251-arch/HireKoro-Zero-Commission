package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.data.model.*
import com.example.data.repository.HireKoroRepository
import com.example.ui.theme.*

enum class AdminTab {
    DASHBOARD,
    AZ_MANAGEMENT,
    SUBSCRIPTIONS,
    PROVIDERS,
    COMPLAINTS_RATINGS,
    WALLET_PAYOUTS
}

@Composable
fun AdminPanelScreen(
    repository: HireKoroRepository,
    language: Language,
    onExitAdmin: () -> Unit
) {
    var selectedAdminTab by remember { mutableStateOf(AdminTab.DASHBOARD) }

    val professions by repository.professions.collectAsState()
    val vehicles by repository.vehicles.collectAsState()
    val slabs by repository.subscriptionSlabs.collectAsState()
    val providers by repository.providers.collectAsState()
    val complaints by repository.complaints.collectAsState()
    val ratings by repository.ratings.collectAsState()
    val withdrawals by repository.withdrawals.collectAsState()
    val fast2SmsApiKey by repository.fast2SmsApiKey.collectAsState()

    var showAddProfessionDialog by remember { mutableStateOf(false) }
    var showAddVehicleDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5))
            .testTag("admin_panel_screen")
    ) {
        // Admin Top Bar
        Surface(
            color = HireKoroCharcoal,
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = null,
                        tint = HireKoroYellow,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "HireKoro Master Console",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = HireKoroYellow,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "SUPERADMIN",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = HireKoroBlack,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Authenticated: ${HireKoroRepository.ADMIN_EMAIL}",
                            color = Color.LightGray,
                            fontSize = 11.sp
                        )
                    }
                }

                Button(
                    onClick = onExitAdmin,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.15f),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("Exit Admin", fontSize = 12.sp)
                }
            }
        }

        // Admin Navigation Tabs (Web & Mobile Scrollable)
        ScrollableTabRow(
            selectedTabIndex = selectedAdminTab.ordinal,
            containerColor = Color.White,
            contentColor = HireKoroCharcoal,
            edgePadding = 8.dp
        ) {
            AdminTab.entries.forEach { tab ->
                Tab(
                    selected = selectedAdminTab == tab,
                    onClick = { selectedAdminTab = tab },
                    text = {
                        Text(
                            text = when (tab) {
                                AdminTab.DASHBOARD -> "Dashboard"
                                AdminTab.AZ_MANAGEMENT -> "A-Z Management"
                                AdminTab.SUBSCRIPTIONS -> "Subscriptions"
                                AdminTab.PROVIDERS -> "Providers"
                                AdminTab.COMPLAINTS_RATINGS -> "Complaints (${complaints.count { it.status == ComplaintStatus.OPEN }})"
                                AdminTab.WALLET_PAYOUTS -> "Payouts"
                            },
                            fontWeight = if (selectedAdminTab == tab) FontWeight.ExtraBold else FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    }
                )
            }
        }

        // Tab Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
        ) {
            when (selectedAdminTab) {
                AdminTab.DASHBOARD -> {
                    AdminDashboardTab(
                        providersCount = providers.size,
                        complaintsCount = complaints.size,
                        ratingsCount = ratings.size,
                        fast2SmsApiKey = fast2SmsApiKey,
                        onUpdateFast2SmsApiKey = { repository.updateFast2SmsApiKey(it) }
                    )
                }
                AdminTab.AZ_MANAGEMENT -> {
                    AdminAZManagementTab(
                        professions = professions,
                        vehicles = vehicles,
                        onAddProfession = { showAddProfessionDialog = true },
                        onToggleProfession = { repository.toggleProfession(it) },
                        onDeleteProfession = { repository.deleteProfession(it) },
                        onAddVehicle = { showAddVehicleDialog = true },
                        onToggleVehicle = { repository.toggleVehicle(it) },
                        onDeleteVehicle = { repository.deleteVehicle(it) }
                    )
                }
                AdminTab.SUBSCRIPTIONS -> {
                    AdminSubscriptionsTab(
                        slabs = slabs,
                        onUpdatePrice = { slabNum, price, trial ->
                            repository.updateSlabPrice(slabNum, price, trial)
                        }
                    )
                }
                AdminTab.PROVIDERS -> {
                    AdminProvidersTab(
                        providers = providers,
                        onToggleBlock = { repository.toggleProviderBlock(it) }
                    )
                }
                AdminTab.COMPLAINTS_RATINGS -> {
                    AdminComplaintsRatingsTab(
                        complaints = complaints,
                        ratings = ratings,
                        providers = providers,
                        onResolve = { id, action, deduct ->
                            repository.resolveComplaint(id, action, deduct)
                        },
                        onFlagFake = { repository.flagRatingFake(it) },
                        onWarnProvider = { repository.warnProviderForRating(it, "Official Admin Warning: Rating & quality complaints reported") },
                        onDeductPenalty = { proId, amt, reason -> repository.deductProviderPenalty(proId, amt, reason) },
                        onToggleBlock = { repository.toggleProviderBlock(it) }
                    )
                }
                AdminTab.WALLET_PAYOUTS -> {
                    AdminPayoutsTab(withdrawals = withdrawals)
                }
            }
        }
    }

    // Add Profession Dialog
    if (showAddProfessionDialog) {
        var name by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("Technician") }
        var slab by remember { mutableStateOf("2") }
        var desc by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddProfessionDialog = false },
            title = { Text("Add New Profession", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Profession Name") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category (Driver / Technician / Labour)") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = slab, onValueChange = { slab = it }, label = { Text("Assigned Slab (1-4)") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            repository.addProfession(name, category, slab.toIntOrNull() ?: 2, desc)
                            showAddProfessionDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HireKoroYellow, contentColor = HireKoroBlack)
                ) {
                    Text("Add Profession")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddProfessionDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Add Vehicle Dialog
    if (showAddVehicleDialog) {
        var name by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("Commercial") }
        var slab by remember { mutableStateOf("2") }
        var capacity by remember { mutableStateOf("2 Ton Payload") }

        AlertDialog(
            onDismissRequest = { showAddVehicleDialog = false },
            title = { Text("Add New Vehicle Fleet", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Vehicle Name (e.g. Crane / Tractor)") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = slab, onValueChange = { slab = it }, label = { Text("Assigned Slab (1-4)") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = capacity, onValueChange = { capacity = it }, label = { Text("Capacity / Specs") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            repository.addVehicle(name, category, slab.toIntOrNull() ?: 2, capacity)
                            showAddVehicleDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HireKoroYellow, contentColor = HireKoroBlack)
                ) {
                    Text("Add Vehicle")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddVehicleDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun AdminDashboardTab(
    providersCount: Int,
    complaintsCount: Int,
    ratingsCount: Int,
    fast2SmsApiKey: String,
    onUpdateFast2SmsApiKey: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    var editableApiKey by remember(fast2SmsApiKey) { mutableStateOf(fast2SmsApiKey) }
    var saveStatus by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Text("Overview Metrics", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = HireKoroCharcoal)
        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AdminMetricCard("Total Users", "2,480", "Active on platform", HireKoroCharcoal, Modifier.weight(1f))
            AdminMetricCard("Active Providers", "$providersCount", "Verified partners", HireKoroGreen, Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AdminMetricCard("Monthly GMV", "₹12.4 Lakh", "0% Commission Direct", HireKoroYellowDark, Modifier.weight(1f))
            AdminMetricCard("Subscription ARR", "₹2.9 Lakh", "Direct Razorpay revenue", HireKoroCharcoal, Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AdminMetricCard("Open Grievances", "$complaintsCount", "Target resolution: 2h", HireKoroRed, Modifier.weight(1f))
            AdminMetricCard("Customer Reviews", "$ratingsCount", "Avg rating 4.7 ★", HireKoroCharcoal, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Fast2SMS API Configuration Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Sms,
                        contentDescription = null,
                        tint = HireKoroYellowDark,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Fast2SMS & Automated Dispatch Config",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = HireKoroCharcoal
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Cloud Function sendAutoSMS uses Fast2SMS bulkV2 API (POST https://www.fast2sms.com/dev/bulkV2) to deliver instant SMS with Google Maps location link to drivers & technicians upon customer booking confirmation.",
                    fontSize = 11.sp,
                    color = Color(0xFF4B5563),
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = editableApiKey,
                    onValueChange = {
                        editableApiKey = it
                        saveStatus = null
                    },
                    label = { Text("Fast2SMS API Key (Live / Sandbox)") },
                    placeholder = { Text("Enter your Fast2SMS authorization key...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    trailingIcon = {
                        Button(
                            onClick = {
                                onUpdateFast2SmsApiKey(editableApiKey)
                                saveStatus = "Key Saved to Firestore Config!"
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = HireKoroYellow,
                                contentColor = HireKoroBlack
                            ),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Text("Save", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                )

                if (saveStatus != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "✓ $saveStatus",
                        color = Color(0xFF16A34A),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Backend Infrastructure & Webhook Status", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("• Razorpay Webhook: Active (https://cloudfunctions/verifyRazorpayPayment)", fontSize = 12.sp, color = Color.DarkGray)
                Text("• Fast2SMS Gateway: POST https://www.fast2sms.com/dev/bulkV2", fontSize = 12.sp, color = Color.DarkGray)
                Text("• Auto Expiry Function: onSubscriptionExpiry scheduled daily at 00:00 UTC", fontSize = 12.sp, color = Color.DarkGray)
                Text("• Quality Guard: onRatingCheck triggers Under Review for average < 3.5 ★", fontSize = 12.sp, color = Color.DarkGray)
                Text("• Zero Commission Policy: Active across all 4 slabs", fontSize = 12.sp, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun AdminAZManagementTab(
    professions: List<Profession>,
    vehicles: List<Vehicle>,
    onAddProfession: () -> Unit,
    onToggleProfession: (String) -> Unit,
    onDeleteProfession: (String) -> Unit,
    onAddVehicle: () -> Unit,
    onToggleVehicle: (String) -> Unit,
    onDeleteVehicle: (String) -> Unit
) {
    var subTab by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                Button(
                    onClick = { subTab = 0 },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (subTab == 0) HireKoroCharcoal else Color.White,
                        contentColor = if (subTab == 0) Color.White else HireKoroCharcoal
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("Professions (${professions.size})", fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { subTab = 1 },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (subTab == 1) HireKoroCharcoal else Color.White,
                        contentColor = if (subTab == 1) Color.White else HireKoroCharcoal
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("Vehicles (${vehicles.size})", fontSize = 12.sp)
                }
            }

            Button(
                onClick = if (subTab == 0) onAddProfession else onAddVehicle,
                colors = ButtonDefaults.buttonColors(containerColor = HireKoroYellow, contentColor = HireKoroBlack),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (subTab == 0) "+ Profession" else "+ Vehicle", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (subTab == 0) {
                items(professions) { p ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(p.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(color = HireKoroYellow.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp)) {
                                        Text("Slab ${p.slabAssigned}", fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                    }
                                }
                                Text("${p.category} • ${if (p.isEnabled) "Active" else "Disabled"}", fontSize = 11.sp, color = Color.Gray)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Switch(checked = p.isEnabled, onCheckedChange = { onToggleProfession(p.id) })
                                IconButton(onClick = { onDeleteProfession(p.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = HireKoroRed, modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }
                }
            } else {
                items(vehicles) { v ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(v.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(color = HireKoroYellow.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp)) {
                                        Text("Slab ${v.slabAssigned}", fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                    }
                                }
                                Text("${v.category} • ${v.capacity}", fontSize = 11.sp, color = Color.Gray)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Switch(checked = v.isEnabled, onCheckedChange = { onToggleVehicle(v.id) })
                                IconButton(onClick = { onDeleteVehicle(v.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = HireKoroRed, modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminSubscriptionsTab(
    slabs: List<SubscriptionSlab>,
    onUpdatePrice: (Int, Double, Int) -> Unit
) {
    var editingSlab by remember { mutableStateOf<SubscriptionSlab?>(null) }
    var newPriceInput by remember { mutableStateOf("") }
    var newTrialDaysInput by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        Text("Zero Commission Slabs Management", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text("Admin can adjust monthly price and trial periods for all slabs", fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(slabs) { slab ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(slab.title, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                            Text("₹${slab.priceMonthly.toInt()}/mo", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = HireKoroYellowDark)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Trial: ${slab.trialDays} Days • Covered: ${slab.eligibleProfessions}", fontSize = 11.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                editingSlab = slab
                                newPriceInput = slab.priceMonthly.toInt().toString()
                                newTrialDaysInput = slab.trialDays.toString()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = HireKoroCharcoal, contentColor = Color.White),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().height(36.dp)
                        ) {
                            Text("Edit Price & Trial Period", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }

    editingSlab?.let { s ->
        AlertDialog(
            onDismissRequest = { editingSlab = null },
            title = { Text("Edit ${s.title}", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = newPriceInput,
                        onValueChange = { newPriceInput = it },
                        label = { Text("Monthly Price (₹)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newTrialDaysInput,
                        onValueChange = { newTrialDaysInput = it },
                        label = { Text("Trial Days") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val p = newPriceInput.toDoubleOrNull() ?: s.priceMonthly
                        val t = newTrialDaysInput.toIntOrNull() ?: s.trialDays
                        onUpdatePrice(s.slabNumber, p, t)
                        editingSlab = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HireKoroYellow, contentColor = HireKoroBlack)
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingSlab = null }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun AdminProvidersTab(
    providers: List<ProviderProfile>,
    onToggleBlock: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Providers & Driver Verification", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text("Review quality score and toggle access", fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(providers) { pro ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(pro.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text("${pro.profession} • ${pro.phone}", fontSize = 12.sp, color = Color.Gray)
                            }
                            Surface(
                                color = if (pro.isBlocked) HireKoroRed.copy(alpha = 0.15f) else if (pro.isUnderReview) HireKoroYellow.copy(alpha = 0.2f) else HireKoroGreen.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = if (pro.isBlocked) "BLOCKED" else if (pro.isUnderReview) "UNDER REVIEW" else "ACTIVE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (pro.isBlocked) HireKoroRed else if (pro.isUnderReview) HireKoroCharcoal else HireKoroGreen,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Rating: ${pro.rating} ★ (${pro.reviewCount} reviews) • Location: ${pro.location}",
                            fontSize = 11.sp,
                            color = Color(0xFF555555)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = { onToggleBlock(pro.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (pro.isBlocked) HireKoroGreen else HireKoroRed,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().height(36.dp)
                        ) {
                            Text(if (pro.isBlocked) "Unblock Provider" else "Block Provider", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminComplaintsRatingsTab(
    complaints: List<Complaint>,
    ratings: List<RatingItem>,
    providers: List<ProviderProfile>,
    onResolve: (String, String, Boolean) -> Unit,
    onFlagFake: (String) -> Unit,
    onWarnProvider: (String) -> Unit,
    onDeductPenalty: (String, Double, String) -> Unit,
    onToggleBlock: (String) -> Unit
) {
    var subTab by remember { mutableStateOf(0) }
    var ratingFilterUnderReviewOnly by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { subTab = 0 },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (subTab == 0) HireKoroCharcoal else Color.White,
                    contentColor = if (subTab == 0) Color.White else HireKoroCharcoal
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f).height(38.dp)
            ) {
                Text("Grievances (${complaints.size})", fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { subTab = 1 },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (subTab == 1) HireKoroCharcoal else Color.White,
                    contentColor = if (subTab == 1) Color.White else HireKoroCharcoal
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f).height(38.dp)
            ) {
                Text("Reviews & Ratings (${ratings.size})", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (subTab == 0) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(complaints) { c ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Ticket #${c.id} • ${c.reason}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("User: ${c.userName} (${c.userPhone})", fontSize = 12.sp, color = Color.Gray)
                            Text("Provider: ${c.providerName} (${c.providerPhone})", fontSize = 12.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(c.description, fontSize = 12.sp, color = Color.DarkGray)
                            Spacer(modifier = Modifier.height(8.dp))

                            if (c.status != ComplaintStatus.RESOLVED) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            onResolve(c.id, "Warning issued to partner via push notification", false)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = HireKoroYellow, contentColor = HireKoroBlack),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.weight(1f).height(34.dp)
                                    ) {
                                        Text("Warn", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Button(
                                        onClick = {
                                            onResolve(c.id, "₹100 Penalty deducted from partner wallet and refunded to user", true)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = HireKoroRed, contentColor = Color.White),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.weight(1f).height(34.dp)
                                    ) {
                                        Text("Deduct Penalty", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            } else {
                                Text("Status: Resolved (${c.adminActionTaken})", fontSize = 11.sp, color = HireKoroGreen, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        } else {
            // Reviews & Rating Management
            val filteredRatings = if (ratingFilterUnderReviewOnly) {
                ratings.filter { it.rating < 3.5 }
            } else {
                ratings
            }

            Column(modifier = Modifier.fillMaxSize()) {
                // Filter row: All vs Below 3.5 (Under Review)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterChip(
                        selected = !ratingFilterUnderReviewOnly,
                        onClick = { ratingFilterUnderReviewOnly = false },
                        label = { Text("All Ratings (${ratings.size})", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = HireKoroCharcoal,
                            selectedLabelColor = Color.White
                        )
                    )
                    FilterChip(
                        selected = ratingFilterUnderReviewOnly,
                        onClick = { ratingFilterUnderReviewOnly = true },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Below 3.5 ★ (Under Review) (${ratings.count { it.rating < 3.5 }})", fontSize = 11.sp)
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = HireKoroRed,
                            selectedLabelColor = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (filteredRatings.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                        Text("No ratings matching the selected filter.", color = Color.Gray)
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(filteredRatings) { r ->
                            val provider = providers.find { it.id == r.providerId }
                            val isBlocked = provider?.isBlocked ?: false
                            val isUnderReview = (r.rating < 3.5) || (provider?.isUnderReview == true)

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = "Booking #${r.bookingId}",
                                                    fontWeight = FontWeight.ExtraBold,
                                                    fontSize = 13.sp,
                                                    color = HireKoroCharcoal
                                                )
                                                if (isUnderReview) {
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Surface(
                                                        color = HireKoroRed.copy(alpha = 0.15f),
                                                        shape = RoundedCornerShape(4.dp)
                                                    ) {
                                                        Text(
                                                            text = "UNDER REVIEW",
                                                            fontSize = 9.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = HireKoroRed,
                                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                        )
                                                    }
                                                }
                                            }
                                            Text(
                                                text = "${r.providerName} (ID: ${r.providerId})",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                                color = HireKoroCharcoal
                                            )
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            repeat(r.rating) {
                                                Icon(
                                                    imageVector = Icons.Default.Star,
                                                    contentDescription = null,
                                                    tint = if (r.rating < 3.5) HireKoroRed else HireKoroYellowDark,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "${r.rating}.0",
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 13.sp,
                                                color = if (r.rating < 3.5) HireKoroRed else HireKoroCharcoal
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    if (r.comment.isNotBlank()) {
                                        Text(
                                            text = "\"${r.comment}\"",
                                            fontSize = 12.sp,
                                            color = Color(0xFF374151)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                    }

                                    // Tags & details
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Tags: ${r.tags.joinToString(", ")}",
                                            fontSize = 11.sp,
                                            color = Color.Gray
                                        )
                                        Text(
                                            text = "${r.date} • ${r.userId}",
                                            fontSize = 10.sp,
                                            color = Color.Gray
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))
                                    HorizontalDivider(color = Color(0xFFF3F4F6))
                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Action Buttons: Warning, Deduct Penalty, Block/Unblock, Flag Fake
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Button(
                                            onClick = { onWarnProvider(r.providerId) },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = HireKoroYellow,
                                                contentColor = HireKoroBlack
                                            ),
                                            shape = RoundedCornerShape(6.dp),
                                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                            modifier = Modifier.weight(1f).height(34.dp)
                                        ) {
                                            Text("Warning", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Button(
                                            onClick = {
                                                onDeductPenalty(r.providerId, 100.0, "Low rating penalty on Booking #${r.bookingId}")
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = HireKoroRed.copy(alpha = 0.85f),
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(6.dp),
                                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                            modifier = Modifier.weight(1f).height(34.dp)
                                        ) {
                                            Text("Deduct ₹100", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Button(
                                            onClick = { onToggleBlock(r.providerId) },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (isBlocked) HireKoroGreen else HireKoroRed,
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(6.dp),
                                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                            modifier = Modifier.weight(1f).height(34.dp)
                                        ) {
                                            Text(if (isBlocked) "Unblock" else "Block", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }

                                        OutlinedButton(
                                            onClick = { onFlagFake(r.id) },
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                contentColor = if (r.isFlaggedFake) HireKoroGreen else Color.Gray
                                            ),
                                            shape = RoundedCornerShape(6.dp),
                                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                            modifier = Modifier.weight(1f).height(34.dp)
                                        ) {
                                            Text(if (r.isFlaggedFake) "Unflag" else "Flag Fake", fontSize = 10.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminPayoutsTab(withdrawals: List<WithdrawalRequest>) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("UPI Payout Requests", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text("Payouts are processed instantly with zero deduction", fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(12.dp))

        if (withdrawals.isEmpty()) {
            Text("No pending withdrawal requests.", color = Color.Gray)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(withdrawals) { wd ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("₹${wd.amount.toInt()} -> ${wd.upiId}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Requested on ${wd.date} • ID #${wd.id}", fontSize = 11.sp, color = Color.Gray)
                            }
                            Surface(
                                color = if (wd.status == "COMPLETED") HireKoroGreen.copy(alpha = 0.15f) else HireKoroYellow.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = wd.status,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (wd.status == "COMPLETED") HireKoroGreen else HireKoroCharcoal,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminMetricCard(title: String, value: String, sub: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Spacer(modifier = Modifier.height(2.dp))
            Text(sub, fontSize = 10.sp, color = Color(0xFF666666))
        }
    }
}
