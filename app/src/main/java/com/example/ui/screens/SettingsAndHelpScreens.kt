package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
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
import com.example.data.model.Language
import com.example.data.repository.HireKoroRepository
import com.example.ui.theme.HireKoroBackground
import com.example.ui.theme.HireKoroCharcoal
import com.example.ui.theme.HireKoroYellow

@Composable
fun SettingsScreen(
    repository: HireKoroRepository,
    language: Language
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var soundEnabled by remember { mutableStateOf(true) }
    var directCallAlerts by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HireKoroBackground)
            .padding(16.dp)
            .testTag("settings_screen")
    ) {
        Text("HireKoro Settings", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = HireKoroCharcoal)
        Text("Preferences & App Configuration", fontSize = 12.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Push Notifications", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Instant booking alerts and customer calls", fontSize = 11.sp, color = Color.Gray)
                    }
                    Switch(checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it })
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Sound Alerts", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Play notification tone on new job request", fontSize = 11.sp, color = Color.Gray)
                    }
                    Switch(checked = soundEnabled, onCheckedChange = { soundEnabled = it })
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Direct Call Priority", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Keep phone number visible to nearby users", fontSize = 11.sp, color = Color.Gray)
                    }
                    Switch(checked = directCallAlerts, onCheckedChange = { directCallAlerts = it })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("About HireKoro", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text("Version: 1.0.0 (Production Release)", fontSize = 12.sp, color = Color.Gray)
                Text("Platform: Android, iOS, Web Cross-Platform", fontSize = 12.sp, color = Color.Gray)
                Text("Architecture: Clean MVVM with Jetpack Compose & Flutter Multiplatform", fontSize = 12.sp, color = Color.Gray)
                Text("Payment Gateway: Razorpay Verified (@bharatmitrainfotech)", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun HelpScreen(
    repository: HireKoroRepository,
    language: Language
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HireKoroBackground)
            .padding(16.dp)
            .verticalScroll(scrollState)
            .testTag("help_screen")
    ) {
        Text("Help & Customer Support", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = HireKoroCharcoal)
        Text("24x7 Assistance for Customers & Partner Providers", fontSize = 12.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = HireKoroCharcoal)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Need Immediate Help?", color = HireKoroYellow, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Connect with HireKoro helpline or WhatsApp support:", color = Color.LightGray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+919830000000"))
                            try { context.startActivity(intent) } catch (_: Exception) {}
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = HireKoroYellow, contentColor = Color.Black),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).height(40.dp)
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Call Support", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/919830000000?text=Hello%20HireKoro%20Support"))
                            try { context.startActivity(intent) } catch (_: Exception) {}
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366), contentColor = Color.White),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).height(40.dp)
                    ) {
                        Text("WhatsApp", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Frequently Asked Questions", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = HireKoroCharcoal)
        Spacer(modifier = Modifier.height(10.dp))

        FaqItem(
            q = "What is the Zero Commission model?",
            a = "HireKoro charges 0% commission on any job or trip. Providers keep 100% of customer payments directly in cash or UPI. Providers only pay a simple monthly subscription slab."
        )
        Spacer(modifier = Modifier.height(8.dp))
        FaqItem(
            q = "How do the 4 Subscription Slabs work?",
            a = "Slab 1 (₹199) covers Toto, Bike, Labour with ₹99 first month trial. Slab 2 (₹399) covers Car Drivers, AC Mistri with 7-day free trial. Slab 3 (₹699) covers Trucks, JCB, Cranes. Slab 4 (₹999) covers Agencies and Fleet owners."
        )
        Spacer(modifier = Modifier.height(8.dp))
        FaqItem(
            q = "How does distance calculation work without opening Google Maps?",
            a = "HireKoro uses geolocator mathematical distance algorithms to compute the exact distance (e.g. 0.8 km away - Dumdum, Kolkata) in pure text, keeping your battery and data light without ever opening map views."
        )
        Spacer(modifier = Modifier.height(8.dp))
        FaqItem(
            q = "How does the Refer & Earn milestone bonus work?",
            a = "Share your code HIRE50BM. When friends join, users get ₹50 and providers get ₹100 in their wallet. Once you reach 5 invites, you unlock an extra ₹500 bonus!"
        )
    }
}

@Composable
fun FaqItem(q: String, a: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(q, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = HireKoroCharcoal)
            Spacer(modifier = Modifier.height(4.dp))
            Text(a, fontSize = 12.sp, color = Color(0xFF555555), lineHeight = 16.sp)
        }
    }
}
