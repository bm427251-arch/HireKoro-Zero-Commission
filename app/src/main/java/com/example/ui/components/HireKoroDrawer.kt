package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.localization.LocalizationManager
import com.example.data.model.AppMode
import com.example.data.model.Language
import com.example.data.model.Screen
import com.example.ui.theme.HireKoroBlack
import com.example.ui.theme.HireKoroCharcoal
import com.example.ui.theme.HireKoroYellow
import kotlinx.coroutines.launch

@Composable
fun HireKoroDrawer(
    currentScreen: Screen,
    currentAppMode: AppMode,
    currentLanguage: Language,
    onAppModeChanged: (AppMode) -> Unit,
    onNavigate: (Screen) -> Unit,
    onNavigateToBecome: (String) -> Unit = {},
    onSecretAdminTriggered: () -> Unit,
    onCloseDrawer: () -> Unit
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val progress = remember { Animatable(0f) }
    var isHolding by remember { mutableStateOf(false) }

    val isProviderMode = currentAppMode == AppMode.PROVIDER

    ModalDrawerSheet(
        modifier = Modifier.width(320.dp).testTag("hirekoro_drawer"),
        drawerContainerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Drawer Top Banner with H Logo + Mode Toggle Switch
            Surface(
                color = HireKoroCharcoal,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // MODE TOGGLE SWITCH CONTAINER (AT VERY TOP OF DRAWER)
                    Surface(
                        color = Color.Black.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("drawer_mode_toggle_container")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "User Mode",
                                fontSize = 12.sp,
                                fontWeight = if (!isProviderMode) FontWeight.ExtraBold else FontWeight.Medium,
                                color = if (!isProviderMode) HireKoroYellow else Color.White
                            )

                            Switch(
                                checked = isProviderMode,
                                onCheckedChange = { checked ->
                                    val newMode = if (checked) AppMode.PROVIDER else AppMode.USER
                                    onAppModeChanged(newMode)
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.Black,
                                    checkedTrackColor = HireKoroYellow,
                                    checkedBorderColor = HireKoroYellow,
                                    uncheckedThumbColor = Color.LightGray,
                                    uncheckedTrackColor = Color(0xFF444444)
                                ),
                                modifier = Modifier.testTag("mode_toggle_switch")
                            )

                            Text(
                                text = "Provider Mode",
                                fontSize = 12.sp,
                                fontWeight = if (isProviderMode) FontWeight.ExtraBold else FontWeight.Medium,
                                color = if (isProviderMode) HireKoroYellow else Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onPress = {
                                            isHolding = true
                                            scope.launch {
                                                progress.animateTo(
                                                    1f,
                                                    animationSpec = tween(7000, easing = LinearEasing)
                                                )
                                                if (progress.value >= 1f) {
                                                    onSecretAdminTriggered()
                                                    progress.snapTo(0f)
                                                    onCloseDrawer()
                                                }
                                            }
                                            tryAwaitRelease()
                                            isHolding = false
                                            scope.launch { progress.snapTo(0f) }
                                        }
                                    )
                                }
                        ) {
                            if (progress.value > 0f) {
                                CircularProgressIndicator(
                                    progress = { progress.value },
                                    modifier = Modifier.size(56.dp),
                                    color = HireKoroYellow,
                                    strokeWidth = 3.dp
                                )
                            }

                            Image(
                                painter = painterResource(id = R.drawable.ic_hirekoro_logo),
                                contentDescription = "Drawer Logo",
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.1f))
                                    .padding(4.dp)
                                )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "HireKoro",
                                color = HireKoroYellow,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = if (isProviderMode) "Partner Dashboard & Tools"
                                       else "0% Commission Direct Model",
                                color = Color.LightGray,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // CONDITIONAL MENU ITEMS BY MODE
            if (!isProviderMode) {
                // ==================== USER MODE MENU ====================
                DrawerMenuItem(
                    icon = Icons.Default.Home,
                    title = "Home",
                    isSelected = currentScreen == Screen.HOME,
                    onClick = { onNavigate(Screen.HOME); onCloseDrawer() }
                )
                DrawerMenuItem(
                    icon = Icons.Default.Work,
                    title = "A-Z Professions (Hire Technician etc)",
                    isSelected = currentScreen == Screen.AZ_PROFESSIONS,
                    onClick = { onNavigate(Screen.AZ_PROFESSIONS); onCloseDrawer() }
                )
                DrawerMenuItem(
                    icon = Icons.Default.DirectionsCar,
                    title = "A-Z Vehicles (Rent a Car, Hire Driver)",
                    isSelected = currentScreen == Screen.AZ_VEHICLES,
                    onClick = { onNavigate(Screen.AZ_VEHICLES); onCloseDrawer() }
                )
                DrawerMenuItem(
                    icon = Icons.Default.BookOnline,
                    title = "My Bookings",
                    isSelected = currentScreen == Screen.MY_BOOKINGS,
                    onClick = { onNavigate(Screen.MY_BOOKINGS); onCloseDrawer() }
                )
                DrawerMenuItem(
                    icon = Icons.Default.AccountBalanceWallet,
                    title = "Wallet",
                    isSelected = currentScreen == Screen.WALLET,
                    onClick = { onNavigate(Screen.WALLET); onCloseDrawer() }
                )
                DrawerMenuItem(
                    icon = Icons.Default.Share,
                    title = "Refer & Earn",
                    isSelected = currentScreen == Screen.REFER_AND_EARN,
                    badge = "₹500 Bonus",
                    onClick = { onNavigate(Screen.REFER_AND_EARN); onCloseDrawer() }
                )
                DrawerMenuItem(
                    icon = Icons.Default.PriceChange,
                    title = "My Rates",
                    isSelected = currentScreen == Screen.MY_RATES,
                    onClick = { onNavigate(Screen.MY_RATES); onCloseDrawer() }
                )
                DrawerMenuItem(
                    icon = Icons.Default.ReportProblem,
                    title = "Complaints",
                    isSelected = currentScreen == Screen.COMPLAINTS,
                    onClick = { onNavigate(Screen.COMPLAINTS); onCloseDrawer() }
                )
                DrawerMenuItem(
                    icon = Icons.Default.Star,
                    title = "Rating & Reviews",
                    isSelected = currentScreen == Screen.RATINGS,
                    onClick = { onNavigate(Screen.RATINGS); onCloseDrawer() }
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp, horizontal = 16.dp))
                DrawerMenuItem(
                    icon = Icons.Default.Settings,
                    title = "Settings",
                    isSelected = currentScreen == Screen.SETTINGS,
                    onClick = { onNavigate(Screen.SETTINGS); onCloseDrawer() }
                )
                DrawerMenuItem(
                    icon = Icons.AutoMirrored.Filled.Help,
                    title = "Help & Support",
                    isSelected = currentScreen == Screen.HELP,
                    onClick = { onNavigate(Screen.HELP); onCloseDrawer() }
                )
                DrawerMenuItem(
                    icon = Icons.AutoMirrored.Filled.Logout,
                    title = "Logout",
                    isSelected = false,
                    onClick = { onCloseDrawer() }
                )
            } else {
                // ==================== PROVIDER MODE MENU ====================
                Text(
                    text = "Become a Provider - Earn Money",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = HireKoroCharcoal,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )

                // Option 1: Become a Driver
                BecomeOptionCard(
                    icon = Icons.Default.LocalShipping,
                    title = "Become a Driver",
                    subtitle = "Earn by driving - Daily / Monthly",
                    onClick = {
                        onNavigateToBecome("Driver")
                        onCloseDrawer()
                    }
                )

                // Option 2: Become a Technician
                BecomeOptionCard(
                    icon = Icons.Default.Engineering,
                    title = "Become a Technician",
                    subtitle = "Mistri, Electrician, Plumber, Interior Decorator",
                    onClick = {
                        onNavigateToBecome("Technician")
                        onCloseDrawer()
                    }
                )

                // Option 3: Become a Rent a Car Owner
                BecomeOptionCard(
                    icon = Icons.Default.DirectionsCar,
                    title = "Become a Rent a Car Owner",
                    subtitle = "Rent your car - Self Drive / With Driver",
                    onClick = {
                        onNavigateToBecome("Vehicle Owner")
                        onCloseDrawer()
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp))

                Text(
                    text = "Provider Management",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )

                DrawerMenuItem(
                    icon = Icons.Default.Dashboard,
                    title = "Provider Dashboard",
                    isSelected = currentScreen == Screen.PROVIDER_DASHBOARD,
                    onClick = { onNavigate(Screen.PROVIDER_DASHBOARD); onCloseDrawer() }
                )
                DrawerMenuItem(
                    icon = Icons.Default.CardMembership,
                    title = "My Subscription Plans",
                    isSelected = currentScreen == Screen.SUBSCRIPTION_PLANS,
                    badge = "0% Comm",
                    onClick = { onNavigate(Screen.SUBSCRIPTION_PLANS); onCloseDrawer() }
                )
                DrawerMenuItem(
                    icon = Icons.Default.AccountBalanceWallet,
                    title = "My Earnings / Wallet",
                    isSelected = currentScreen == Screen.WALLET,
                    onClick = { onNavigate(Screen.WALLET); onCloseDrawer() }
                )
                DrawerMenuItem(
                    icon = Icons.Default.BookOnline,
                    title = "My Bookings (Provider)",
                    isSelected = currentScreen == Screen.MY_BOOKINGS,
                    onClick = { onNavigate(Screen.MY_BOOKINGS); onCloseDrawer() }
                )
                DrawerMenuItem(
                    icon = Icons.Default.Description,
                    title = "My Documents (Uploaded Docs)",
                    isSelected = currentScreen == Screen.PROVIDER_DOCUMENTS,
                    onClick = { onNavigate(Screen.PROVIDER_DOCUMENTS); onCloseDrawer() }
                )
                DrawerMenuItem(
                    icon = Icons.Default.PriceChange,
                    title = "My Rates Manager",
                    isSelected = currentScreen == Screen.MY_RATES,
                    onClick = { onNavigate(Screen.MY_RATES); onCloseDrawer() }
                )
                DrawerMenuItem(
                    icon = Icons.Default.ReportProblem,
                    title = "Complaints",
                    isSelected = currentScreen == Screen.COMPLAINTS,
                    onClick = { onNavigate(Screen.COMPLAINTS); onCloseDrawer() }
                )
                DrawerMenuItem(
                    icon = Icons.Default.Star,
                    title = "Rating & Reviews",
                    isSelected = currentScreen == Screen.RATINGS,
                    onClick = { onNavigate(Screen.RATINGS); onCloseDrawer() }
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp, horizontal = 16.dp))
                DrawerMenuItem(
                    icon = Icons.Default.Settings,
                    title = "Settings",
                    isSelected = currentScreen == Screen.SETTINGS,
                    onClick = { onNavigate(Screen.SETTINGS); onCloseDrawer() }
                )
                DrawerMenuItem(
                    icon = Icons.AutoMirrored.Filled.Help,
                    title = "Help & Support",
                    isSelected = currentScreen == Screen.HELP,
                    onClick = { onNavigate(Screen.HELP); onCloseDrawer() }
                )
                DrawerMenuItem(
                    icon = Icons.AutoMirrored.Filled.Logout,
                    title = "Logout",
                    isSelected = false,
                    onClick = { onCloseDrawer() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFEEEEEE))
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "HireKoro v1.0",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun BecomeOptionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBE6)),
        border = BorderStroke(1.dp, HireKoroYellow),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 4.dp)
            .testTag("become_card_${title.lowercase().replace(" ", "_")}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = HireKoroYellow,
                shape = CircleShape,
                modifier = Modifier.size(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.Black
                )
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    color = Color(0xFF555555),
                    lineHeight = 13.sp
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun DrawerMenuItem(
    icon: ImageVector,
    title: String,
    isSelected: Boolean,
    badge: String? = null,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = if (isSelected) HireKoroYellow.copy(alpha = 0.2f) else Color.Transparent,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) HireKoroCharcoal else Color(0xFF555555),
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) HireKoroCharcoal else Color(0xFF333333),
                modifier = Modifier.weight(1f)
            )
            if (badge != null) {
                Surface(
                    color = HireKoroYellow,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = badge,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = HireKoroBlack,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
