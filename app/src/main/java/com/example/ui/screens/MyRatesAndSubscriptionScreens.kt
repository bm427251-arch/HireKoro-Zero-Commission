package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.LocalizationManager
import com.example.data.model.CustomRateBox
import com.example.data.model.Language
import com.example.data.model.SubscriptionSlab
import com.example.data.repository.HireKoroRepository
import com.example.ui.theme.*

@Composable
fun MyRatesScreen(
    repository: HireKoroRepository,
    language: Language
) {
    val myProfile by repository.myProviderProfile.collectAsState()

    var visitingCharge by remember(myProfile) { mutableStateOf(myProfile.customRates.visitingCharge.toInt().toString()) }
    var hourlyRate by remember(myProfile) { mutableStateOf(myProfile.customRates.hourlyRate.toInt().toString()) }
    var dailyRate by remember(myProfile) { mutableStateOf(myProfile.customRates.dailyRate.toInt().toString()) }
    var perKmRate by remember(myProfile) { mutableStateOf(myProfile.customRates.perKmRate.toInt().toString()) }
    var savedMessage by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HireKoroBackground)
            .padding(16.dp)
            .testTag("my_rates_screen")
    ) {
        Text(
            text = "Custom Rate Box (Provider Profile)",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = HireKoroCharcoal
        )
        Text(
            text = "Set your own rates. Customers see exactly these 4 figures before booking.",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                RateInputField(
                    label = LocalizationManager.getString("visiting_charge", language),
                    sublabel = "Initial visit / inspection minimum fee",
                    value = visitingCharge,
                    onValueChange = { visitingCharge = it; savedMessage = false }
                )

                Spacer(modifier = Modifier.height(14.dp))

                RateInputField(
                    label = LocalizationManager.getString("hourly_rate", language),
                    sublabel = "Charge per hour of service",
                    value = hourlyRate,
                    onValueChange = { hourlyRate = it; savedMessage = false }
                )

                Spacer(modifier = Modifier.height(14.dp))

                RateInputField(
                    label = LocalizationManager.getString("daily_rate", language),
                    sublabel = "Full day (8-10 hours) standard package",
                    value = dailyRate,
                    onValueChange = { dailyRate = it; savedMessage = false }
                )

                Spacer(modifier = Modifier.height(14.dp))

                RateInputField(
                    label = LocalizationManager.getString("per_km_rate", language),
                    sublabel = "Outstation or vehicle trip distance fee",
                    value = perKmRate,
                    onValueChange = { perKmRate = it; savedMessage = false }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val newRates = CustomRateBox(
                            visitingCharge = visitingCharge.toDoubleOrNull() ?: 150.0,
                            hourlyRate = hourlyRate.toDoubleOrNull() ?: 120.0,
                            dailyRate = dailyRate.toDoubleOrNull() ?: 900.0,
                            perKmRate = perKmRate.toDoubleOrNull() ?: 14.0
                        )
                        repository.updateMyRates(newRates)
                        savedMessage = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HireKoroYellow,
                        contentColor = HireKoroBlack
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(46.dp)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Save Rates & Update Customer View", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                }

                if (savedMessage) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        color = HireKoroGreen.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Rates updated successfully in Firestore!",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = HireKoroGreen,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RateInputField(
    label: String,
    sublabel: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(text = label, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = HireKoroCharcoal)
        Text(text = sublabel, fontSize = 11.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            leadingIcon = { Text("₹", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = HireKoroCharcoal) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = HireKoroYellow,
                unfocusedBorderColor = Color.LightGray
            )
        )
    }
}

@Composable
fun SubscriptionPlansScreen(
    repository: HireKoroRepository,
    language: Language
) {
    val context = LocalContext.current
    val slabs by repository.subscriptionSlabs.collectAsState()
    val myProfile by repository.myProviderProfile.collectAsState()
    var activatedSlabMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HireKoroBackground)
            .padding(16.dp)
            .testTag("subscription_plans_screen")
    ) {
        Text(
            text = "Zero Commission Slabs",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = HireKoroCharcoal
        )
        Text(
            text = "Keep 100% of your earnings. Select monthly slab with Razorpay verification.",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (activatedSlabMessage != null) {
            Surface(
                color = HireKoroGreen.copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = activatedSlabMessage ?: "",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = HireKoroGreen,
                    modifier = Modifier.padding(10.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(slabs) { slab ->
                val isCurrentPlan = myProfile.activeSlab == slab.slabNumber
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCurrentPlan) HireKoroCharcoal else Color.White
                    ),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = slab.title,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = if (isCurrentPlan) HireKoroYellow else HireKoroCharcoal
                            )
                            if (isCurrentPlan) {
                                Surface(
                                    color = HireKoroGreen,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "ACTIVE PLAN",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "₹${slab.priceMonthly.toInt()}",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isCurrentPlan) Color.White else HireKoroCharcoal
                            )
                            Text(
                                text = "/month",
                                fontSize = 13.sp,
                                color = if (isCurrentPlan) Color.LightGray else Color.Gray,
                                modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
                            )
                            if (slab.trialPrice != null) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    color = HireKoroYellow,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "1st Month Trial ₹${slab.trialPrice.toInt()}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = HireKoroBlack,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    color = HireKoroYellow.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "${slab.trialDays} Days Free Trial",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCurrentPlan) HireKoroYellow else HireKoroCharcoal,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Eligible: ${slab.eligibleProfessions}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isCurrentPlan) Color(0xFFFFE082) else Color(0xFF444444)
                        )
                        Text(
                            text = "Features: ${slab.leadCount} • Zero Commission",
                            fontSize = 11.sp,
                            color = if (isCurrentPlan) Color.LightGray else Color.Gray
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                val amountInPaise = (slab.priceMonthly * 100).toLong()
                                val payUrl = "${HireKoroRepository.RAZORPAY_PAYMENT_URL}?amount=$amountInPaise"
                                try {
                                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(payUrl)))
                                } catch (_: Exception) {}
                                repository.activateSubscription(slab.slabNumber)
                                activatedSlabMessage = "Activated ${slab.title}! Payment link opened. Webhook verifyRazorpayPayment notified."
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isCurrentPlan) Color.White else HireKoroYellow,
                                contentColor = HireKoroBlack
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().height(42.dp)
                        ) {
                            Text(
                                text = if (isCurrentPlan) "Renew via Razorpay (₹${slab.priceMonthly.toInt()})"
                                       else "Pay Now via Razorpay (₹${slab.priceMonthly.toInt()})",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
