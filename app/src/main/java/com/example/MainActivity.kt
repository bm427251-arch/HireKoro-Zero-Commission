package com.example

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import com.example.data.model.AppMode
import com.example.data.model.MainTab
import com.example.data.model.Screen
import com.example.data.repository.HireKoroRepository
import com.example.ui.components.ExitConfirmDialog
import com.example.ui.components.HireKoroDrawer
import com.example.ui.components.HireKoroHeader
import com.example.ui.components.SecretAdminLoginDialog
import com.example.ui.components.ShareLocationPromptDialog
import com.example.ui.screens.*
import com.example.ui.theme.HireKoroTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val repository = HireKoroRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HireKoroTheme {
                val context = LocalContext.current
                val sharedPrefs = remember {
                    context.getSharedPreferences("hirekoro_prefs", Context.MODE_PRIVATE)
                }

                // Restore saved mode on initial launch
                LaunchedEffect(Unit) {
                    val isProvider = sharedPrefs.getBoolean("is_provider_mode", false)
                    if (isProvider) {
                        repository.setAppMode(AppMode.PROVIDER)
                    }
                }

                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                val currentScreen by repository.currentScreen.collectAsState()
                val currentAppMode by repository.currentAppMode.collectAsState()
                val currentLanguage by repository.currentLanguage.collectAsState()
                val currentTab by repository.currentTab.collectAsState()

                var showExitDialog by remember { mutableStateOf(false) }
                var showAdminLoginDialog by remember { mutableStateOf(false) }
                var activeBookingForLocationShare by remember { mutableStateOf<com.example.data.model.Booking?>(null) }
                val snackbarHostState = remember { SnackbarHostState() }

                // WillPopScope / BackHandler Logic:
                // If drawer open -> close it
                // If inner page -> navigate back
                // If on Home page -> Show "Are you sure you want to exit HireKoro? Yes/No"
                BackHandler {
                    if (drawerState.isOpen) {
                        scope.launch { drawerState.close() }
                    } else if (currentScreen != Screen.HOME) {
                        repository.navigateBack()
                    } else {
                        showExitDialog = true
                    }
                }

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        HireKoroDrawer(
                            currentScreen = currentScreen,
                            currentAppMode = currentAppMode,
                            currentLanguage = currentLanguage,
                            onAppModeChanged = { mode ->
                                val isProvider = mode == AppMode.PROVIDER
                                sharedPrefs.edit().putBoolean("is_provider_mode", isProvider).apply()
                                repository.setAppMode(mode)
                                if (isProvider) {
                                    repository.navigateTo(Screen.PROVIDER_DASHBOARD)
                                } else {
                                    repository.navigateTo(Screen.HOME)
                                }
                            },
                            onNavigate = { screen -> repository.navigateTo(screen) },
                            onNavigateToBecome = { category ->
                                repository.navigateToBecomeProvider(category)
                            },
                            onSecretAdminTriggered = { showAdminLoginDialog = true },
                            onCloseDrawer = { scope.launch { drawerState.close() } }
                        )
                    }
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize().testTag("app_scaffold"),
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                        contentWindowInsets = WindowInsets.safeDrawing,
                        topBar = {
                            // Header is shown on all screens except Admin Panel and Become screens which have dedicated bars
                            if (currentScreen != Screen.ADMIN_PANEL &&
                                currentScreen != Screen.BECOME_DRIVER &&
                                currentScreen != Screen.BECOME_TECHNICIAN &&
                                currentScreen != Screen.BECOME_RENT_OWNER &&
                                currentScreen != Screen.BECOME_PROVIDER
                            ) {
                                HireKoroHeader(
                                    onMenuClick = { scope.launch { drawerState.open() } },
                                    currentLanguage = currentLanguage,
                                    onLanguageSelected = { repository.setLanguage(it) },
                                    onSecretAdminTriggered = { showAdminLoginDialog = true }
                                )
                            }
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            when (currentScreen) {
                                Screen.HOME -> {
                                    HomeScreen(
                                        repository = repository,
                                        currentLanguage = currentLanguage,
                                        currentTab = currentTab,
                                        onTabSelected = { repository.setTab(it) },
                                        onBookProvider = { provider ->
                                            val booking = repository.bookProvider(provider)
                                            activeBookingForLocationShare = booking
                                        },
                                        onCarBooked = { booking ->
                                            activeBookingForLocationShare = booking
                                        }
                                    )
                                }
                                Screen.AZ_PROFESSIONS -> {
                                    AZProfessionsScreen(
                                        repository = repository,
                                        language = currentLanguage,
                                        onSelectProfession = { professionName ->
                                            repository.setSearchQuery(professionName)
                                            repository.setTab(
                                                if (professionName.contains("Driver", ignoreCase = true)) MainTab.HIRE_DRIVER
                                                else MainTab.HIRE_TECHNICIAN
                                            )
                                            repository.navigateTo(Screen.HOME)
                                        }
                                    )
                                }
                                Screen.AZ_VEHICLES -> {
                                    AZVehiclesScreen(
                                        repository = repository,
                                        language = currentLanguage,
                                        onSelectVehicle = { vehicleName ->
                                            repository.setSearchQuery(vehicleName)
                                            repository.setTab(MainTab.RENT_CAR)
                                            repository.navigateTo(Screen.HOME)
                                        }
                                    )
                                }
                                Screen.MY_BOOKINGS -> {
                                    MyBookingsScreen(
                                        repository = repository,
                                        language = currentLanguage
                                    )
                                }
                                Screen.WALLET -> {
                                    WalletScreen(
                                        repository = repository,
                                        language = currentLanguage
                                    )
                                }
                                Screen.REFER_AND_EARN -> {
                                    ReferEarnScreen(
                                        repository = repository,
                                        language = currentLanguage
                                    )
                                }
                                Screen.MY_RATES -> {
                                    MyRatesScreen(
                                        repository = repository,
                                        language = currentLanguage
                                    )
                                }
                                Screen.SUBSCRIPTION_PLANS -> {
                                    SubscriptionPlansScreen(
                                        repository = repository,
                                        language = currentLanguage
                                    )
                                }
                                Screen.PROVIDER_DASHBOARD -> {
                                    ProviderDashboardScreen(
                                        repository = repository,
                                        language = currentLanguage,
                                        onNavigate = { repository.navigateTo(it) }
                                    )
                                }
                                Screen.COMPLAINTS -> {
                                    ComplaintsScreen(
                                        repository = repository,
                                        language = currentLanguage
                                    )
                                }
                                Screen.RATINGS -> {
                                    RatingsScreen(
                                        repository = repository,
                                        language = currentLanguage
                                    )
                                }
                                Screen.SETTINGS -> {
                                    SettingsScreen(
                                        repository = repository,
                                        language = currentLanguage
                                    )
                                }
                                Screen.HELP -> {
                                    HelpScreen(
                                        repository = repository,
                                        language = currentLanguage
                                    )
                                }
                                Screen.ADMIN_PANEL -> {
                                    AdminPanelScreen(
                                        repository = repository,
                                        language = currentLanguage,
                                        onExitAdmin = { repository.navigateTo(Screen.HOME) }
                                    )
                                }
                                Screen.BECOME_DRIVER -> {
                                    BecomeProviderScreen(
                                        initialCategory = "Driver",
                                        repository = repository,
                                        language = currentLanguage,
                                        onNavigateBack = { repository.navigateBack() },
                                        onRegistered = { repository.navigateTo(Screen.PROVIDER_DASHBOARD) }
                                    )
                                }
                                Screen.BECOME_TECHNICIAN -> {
                                    BecomeProviderScreen(
                                        initialCategory = "Technician",
                                        repository = repository,
                                        language = currentLanguage,
                                        onNavigateBack = { repository.navigateBack() },
                                        onRegistered = { repository.navigateTo(Screen.PROVIDER_DASHBOARD) }
                                    )
                                }
                                Screen.BECOME_RENT_OWNER -> {
                                    BecomeProviderScreen(
                                        initialCategory = "Vehicle Owner",
                                        repository = repository,
                                        language = currentLanguage,
                                        onNavigateBack = { repository.navigateBack() },
                                        onRegistered = { repository.navigateTo(Screen.PROVIDER_DASHBOARD) }
                                    )
                                }
                                Screen.BECOME_PROVIDER -> {
                                    val cat by repository.becomeProviderInitialCategory.collectAsState()
                                    BecomeProviderScreen(
                                        initialCategory = cat,
                                        repository = repository,
                                        language = currentLanguage,
                                        onNavigateBack = { repository.navigateBack() },
                                        onRegistered = { repository.navigateTo(Screen.PROVIDER_DASHBOARD) }
                                    )
                                }
                                Screen.PROVIDER_DOCUMENTS -> {
                                    ProviderDocumentsScreen(
                                        repository = repository,
                                        language = currentLanguage
                                    )
                                }
                            }
                        }
                    }
                }

                // Dialogs
                if (showExitDialog) {
                    ExitConfirmDialog(
                        language = currentLanguage,
                        onConfirmExit = { finish() },
                        onDismiss = { showExitDialog = false }
                    )
                }

                if (showAdminLoginDialog) {
                    SecretAdminLoginDialog(
                        onLoginSuccess = {
                            showAdminLoginDialog = false
                            repository.navigateTo(Screen.ADMIN_PANEL)
                        },
                        onDismiss = { showAdminLoginDialog = false }
                    )
                }

                activeBookingForLocationShare?.let { booking ->
                    ShareLocationPromptDialog(
                        booking = booking,
                        repository = repository,
                        onDismiss = { activeBookingForLocationShare = null },
                        onLocationShared = { link, via ->
                            repository.updateBookingLocationShare(booking.id, link, via)
                        }
                    )
                }
            }
        }
    }
}
