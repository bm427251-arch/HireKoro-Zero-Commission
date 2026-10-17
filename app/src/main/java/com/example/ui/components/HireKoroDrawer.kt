package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
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
    onSecretAdminTriggered: () -> Unit,
    onCloseDrawer: () -> Unit
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val progress = remember { Animatable(0f) }
    var isHolding by remember { mutableStateOf(false) }

    ModalDrawerSheet(
        modifier = Modifier.width(320.dp).testTag("hirekoro_drawer"),
        drawerContainerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Drawer Top Banner with H Logo (also holds 7s for admin)
            Surface(
                color = HireKoroCharcoal,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
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
                                modifier = Modifier.size(68.dp),
                                color = HireKoroYellow,
                                strokeWidth = 4.dp
                            )
                        }

                        Image(
                            painter = painterResource(id = R.drawable.ic_hirekoro_logo),
                            contentDescription = "Drawer Logo",
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.1f))
                                .padding(4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "HireKoro",
                        color = HireKoroYellow,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = if (isHolding) "Hold 7s for Secret Admin: ${(progress.value * 7).toInt()}s"
                               else "Zero Commission Model - India",
                        color = if (isHolding) HireKoroYellow else Color.LightGray,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // User Mode / Provider Mode Switch
                    Surface(
                        color = Color.Black.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val isUser = currentAppMode == AppMode.USER
                            Button(
                                onClick = { onAppModeChanged(AppMode.USER) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isUser) HireKoroYellow else Color.Transparent,
                                    contentColor = if (isUser) HireKoroBlack else Color.White
                                ),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.weight(1f).height(36.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    text = LocalizationManager.getString("user_mode", currentLanguage),
                                    fontSize = 12.sp,
                                    fontWeight = if (isUser) FontWeight.Bold else FontWeight.Normal
                                )
                            }

                            Spacer(modifier = Modifier.width(4.dp))

                            Button(
                                onClick = { onAppModeChanged(AppMode.PROVIDER) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (!isUser) HireKoroYellow else Color.Transparent,
                                    contentColor = if (!isUser) HireKoroBlack else Color.White
                                ),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.weight(1f).height(36.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    text = LocalizationManager.getString("provider_mode", currentLanguage),
                                    fontSize = 12.sp,
                                    fontWeight = if (!isUser) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Navigation Items
            DrawerMenuItem(
                icon = Icons.Default.Home,
                title = LocalizationManager.getString("menu_home", currentLanguage),
                isSelected = currentScreen == Screen.HOME,
                onClick = { onNavigate(Screen.HOME); onCloseDrawer() }
            )
            DrawerMenuItem(
                icon = Icons.Default.Work,
                title = LocalizationManager.getString("menu_professions", currentLanguage),
                isSelected = currentScreen == Screen.AZ_PROFESSIONS,
                onClick = { onNavigate(Screen.AZ_PROFESSIONS); onCloseDrawer() }
            )
            DrawerMenuItem(
                icon = Icons.Default.DirectionsCar,
                title = LocalizationManager.getString("menu_vehicles", currentLanguage),
                isSelected = currentScreen == Screen.AZ_VEHICLES,
                onClick = { onNavigate(Screen.AZ_VEHICLES); onCloseDrawer() }
            )
            DrawerMenuItem(
                icon = Icons.Default.BookOnline,
                title = LocalizationManager.getString("menu_bookings", currentLanguage),
                isSelected = currentScreen == Screen.MY_BOOKINGS,
                onClick = { onNavigate(Screen.MY_BOOKINGS); onCloseDrawer() }
            )
            DrawerMenuItem(
                icon = Icons.Default.AccountBalanceWallet,
                title = LocalizationManager.getString("menu_wallet", currentLanguage),
                isSelected = currentScreen == Screen.WALLET,
                onClick = { onNavigate(Screen.WALLET); onCloseDrawer() }
            )
            DrawerMenuItem(
                icon = Icons.Default.Share,
                title = LocalizationManager.getString("menu_refer", currentLanguage),
                isSelected = currentScreen == Screen.REFER_AND_EARN,
                badge = "₹500 Bonus",
                onClick = { onNavigate(Screen.REFER_AND_EARN); onCloseDrawer() }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp, horizontal = 16.dp))

            // Provider Section
            DrawerMenuItem(
                icon = Icons.Default.PriceChange,
                title = LocalizationManager.getString("menu_rates", currentLanguage),
                isSelected = currentScreen == Screen.MY_RATES,
                onClick = { onNavigate(Screen.MY_RATES); onCloseDrawer() }
            )
            DrawerMenuItem(
                icon = Icons.Default.CardMembership,
                title = LocalizationManager.getString("menu_subscriptions", currentLanguage),
                isSelected = currentScreen == Screen.SUBSCRIPTION_PLANS,
                badge = "0% Comm",
                onClick = { onNavigate(Screen.SUBSCRIPTION_PLANS); onCloseDrawer() }
            )
            DrawerMenuItem(
                icon = Icons.Default.Dashboard,
                title = LocalizationManager.getString("menu_provider_dash", currentLanguage),
                isSelected = currentScreen == Screen.PROVIDER_DASHBOARD,
                onClick = { onNavigate(Screen.PROVIDER_DASHBOARD); onCloseDrawer() }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp, horizontal = 16.dp))

            // Feedback & Admin
            DrawerMenuItem(
                icon = Icons.Default.ReportProblem,
                title = LocalizationManager.getString("menu_complaints", currentLanguage),
                isSelected = currentScreen == Screen.COMPLAINTS,
                onClick = { onNavigate(Screen.COMPLAINTS); onCloseDrawer() }
            )
            DrawerMenuItem(
                icon = Icons.Default.Star,
                title = LocalizationManager.getString("menu_ratings", currentLanguage),
                isSelected = currentScreen == Screen.RATINGS,
                onClick = { onNavigate(Screen.RATINGS); onCloseDrawer() }
            )
            DrawerMenuItem(
                icon = Icons.Default.Settings,
                title = LocalizationManager.getString("menu_settings", currentLanguage),
                isSelected = currentScreen == Screen.SETTINGS,
                onClick = { onNavigate(Screen.SETTINGS); onCloseDrawer() }
            )
            DrawerMenuItem(
                icon = Icons.AutoMirrored.Filled.Help,
                title = LocalizationManager.getString("menu_help", currentLanguage),
                isSelected = currentScreen == Screen.HELP,
                onClick = { onNavigate(Screen.HELP); onCloseDrawer() }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp, horizontal = 16.dp))

            // Admin Shortcut (or Secret Trigger)
            DrawerMenuItem(
                icon = Icons.Default.AdminPanelSettings,
                title = "Secret Admin (bm427251@gmail.com)",
                isSelected = currentScreen == Screen.ADMIN_PANEL,
                badge = "Secret",
                onClick = { onSecretAdminTriggered(); onCloseDrawer() }
            )

            DrawerMenuItem(
                icon = Icons.AutoMirrored.Filled.Logout,
                title = LocalizationManager.getString("menu_logout", currentLanguage),
                isSelected = false,
                onClick = { onCloseDrawer() }
            )

            Spacer(modifier = Modifier.height(24.dp))
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
                .padding(horizontal = 12.dp, vertical = 10.dp),
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
                fontSize = 14.sp,
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
