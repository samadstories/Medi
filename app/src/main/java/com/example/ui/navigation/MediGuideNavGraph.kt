package com.example.ui.navigation

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.model.UiState
import com.example.i18n.LocalStrings
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeDashboardScreen
import com.example.ui.screens.LanguageSelectionScreen
import com.example.ui.screens.LegalDocType
import com.example.ui.screens.LegalInfoScreen
import com.example.ui.screens.MedicineDetailScreen
import com.example.ui.screens.MedicineScannerScreen
import com.example.ui.screens.MedicineSearchScreen
import com.example.ui.screens.PriceComparisonScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RemindersScreen
import com.example.ui.screens.SafetyAndInteractionCheckerScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SideEffectsScreen
import com.example.ui.viewmodel.AdminViewModel
import com.example.ui.viewmodel.CatalogViewModel
import com.example.ui.viewmodel.UserSessionViewModel
import com.google.firebase.auth.FirebaseUser

object MediGuideRoutes {
    const val ROUTE_DASHBOARD = "dashboard"
    const val ROUTE_SEARCH = "search"
    const val ROUTE_SCANNER = "scanner"
    const val ROUTE_DETAIL = "medicine_detail/{medicineId}"
    const val ROUTE_SIDE_EFFECTS = "side_effects/{medicineId}"
    const val ROUTE_COMPARE = "price_compare?medicineId={medicineId}"
    const val ROUTE_CATEGORIES = "categories"
    const val ROUTE_REMINDERS = "reminders?medicineId={medicineId}"
    const val ROUTE_FAVORITES = "favorites"
    const val ROUTE_PROFILE = "profile"
    const val ROUTE_SETTINGS = "settings"
    const val ROUTE_ABOUT = "about"
    const val ROUTE_PRIVACY = "privacy"
    const val ROUTE_DISCLAIMER = "disclaimer"
    const val ROUTE_SAFETY_CHECKER = "safety_checker?interaction={interaction}"
    const val ROUTE_LANGUAGE = "language_picker"
    const val ROUTE_ADMIN = "admin_dashboard"

    fun detail(medicineId: String) = "medicine_detail/$medicineId"
    fun sideEffects(medicineId: String) = "side_effects/$medicineId"
    fun compare(medicineId: String? = null) =
        if (medicineId != null) "price_compare?medicineId=$medicineId" else "price_compare"
    fun reminders(medicineId: String? = null) =
        if (medicineId != null) "reminders?medicineId=$medicineId" else "reminders"
    fun safetyChecker(interaction: Boolean) = "safety_checker?interaction=$interaction"
}

private data class TopLevelTab(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val testTag: String
)

@Composable
fun MediGuideAuthenticatedApp(
    user: FirebaseUser,
    catalogViewModel: CatalogViewModel,
    sessionViewModel: UserSessionViewModel,
    adminViewModel: AdminViewModel,
    onSignOut: () -> Unit
) {
    val navController = rememberNavController()
    val strings = LocalStrings.current
    val context = LocalContext.current

    val localPrefs by sessionViewModel.localPrefs.collectAsStateWithLifecycle()
    val isAdmin by sessionViewModel.isAdmin.collectAsStateWithLifecycle()
    val profile by sessionViewModel.userProfile.collectAsStateWithLifecycle()
    val favorites by sessionViewModel.favorites.collectAsStateWithLifecycle()
    val recentlyViewed by sessionViewModel.recentlyViewed.collectAsStateWithLifecycle()
    val searchHistory by sessionViewModel.searchHistory.collectAsStateWithLifecycle()
    val reminders by sessionViewModel.reminders.collectAsStateWithLifecycle()

    val medicinesState by catalogViewModel.medicinesState.collectAsStateWithLifecycle()
    val priceOffersState by catalogViewModel.priceOffersState.collectAsStateWithLifecycle()
    val categoriesState by catalogViewModel.categoriesState.collectAsStateWithLifecycle()
    val manufacturersState by catalogViewModel.manufacturersState.collectAsStateWithLifecycle()
    val searchQuery by catalogViewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategoryId by catalogViewModel.selectedCategoryId.collectAsStateWithLifecycle()
    val fdaLiveState by catalogViewModel.fdaLiveState.collectAsStateWithLifecycle()

    val apiConnections by adminViewModel.apiConnections.collectAsStateWithLifecycle()
    val analyticsEvents by adminViewModel.analyticsEvents.collectAsStateWithLifecycle()
    val adminBanner by adminViewModel.adminBanner.collectAsStateWithLifecycle()
    val adminBusy by adminViewModel.busy.collectAsStateWithLifecycle()

    LaunchedEffect(isAdmin) {
        catalogViewModel.setAdminMode(isAdmin)
    }

    val medicines = (medicinesState as? UiState.Success)?.data.orEmpty()
    val priceOffers = (priceOffersState as? UiState.Success)?.data.orEmpty()
    val categories = (categoriesState as? UiState.Success)?.data.orEmpty()
    val manufacturers = (manufacturersState as? UiState.Success)?.data.orEmpty()
    val favoriteIds = favorites.map { it.medicineId }.toSet()

    val voiceHomeLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spoken = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()?.trim()
            if (!spoken.isNullOrBlank()) {
                catalogViewModel.updateSearchQuery(spoken)
                sessionViewModel.recordSearch(spoken, "VOICE")
                navController.navigate(MediGuideRoutes.ROUTE_SEARCH)
            }
        }
    }

    fun launchHomeVoiceSearch() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PROMPT, strings.searchVoice)
        }
        runCatching { voiceHomeLauncher.launch(intent) }
            .onFailure {
                navController.navigate(MediGuideRoutes.ROUTE_SEARCH)
                Toast.makeText(context, "Opened search screen", Toast.LENGTH_SHORT).show()
            }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val topTabs = listOf(
        TopLevelTab(MediGuideRoutes.ROUTE_DASHBOARD, strings.navHome, Icons.Filled.Home, "nav_home"),
        TopLevelTab(MediGuideRoutes.ROUTE_SEARCH, strings.navSearch, Icons.Filled.Search, "nav_search"),
        TopLevelTab(MediGuideRoutes.ROUTE_COMPARE, strings.navCompare, Icons.AutoMirrored.Filled.CompareArrows, "nav_compare"),
        TopLevelTab(MediGuideRoutes.ROUTE_REMINDERS, strings.navReminders, Icons.Filled.NotificationsActive, "nav_reminders"),
        TopLevelTab(MediGuideRoutes.ROUTE_PROFILE, strings.navProfile, Icons.Filled.Person, "nav_profile")
    )

    val showTopNavChrome = currentRoute in setOf(
        MediGuideRoutes.ROUTE_DASHBOARD,
        MediGuideRoutes.ROUTE_SEARCH,
        MediGuideRoutes.ROUTE_COMPARE,
        MediGuideRoutes.ROUTE_REMINDERS,
        MediGuideRoutes.ROUTE_PROFILE
    )

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useRail = maxWidth >= 600.dp

        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                if (showTopNavChrome && !useRail) {
                    NavigationBar {
                        topTabs.forEach { tab ->
                            val selected = currentRoute == tab.route
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    navController.navigate(tab.route) {
                                        popUpTo(MediGuideRoutes.ROUTE_DASHBOARD) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = { Icon(tab.icon, contentDescription = tab.label) },
                                label = { Text(tab.label, maxLines = 1) },
                                modifier = Modifier.testTag(tab.testTag)
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (showTopNavChrome && useRail) {
                    NavigationRail {
                        topTabs.forEach { tab ->
                            val selected = currentRoute == tab.route
                            NavigationRailItem(
                                selected = selected,
                                onClick = {
                                    navController.navigate(tab.route) {
                                        popUpTo(MediGuideRoutes.ROUTE_DASHBOARD) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = { Icon(tab.icon, contentDescription = tab.label) },
                                label = { Text(tab.label) },
                                modifier = Modifier.testTag(tab.testTag)
                            )
                        }
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = MediGuideRoutes.ROUTE_DASHBOARD,
                    modifier = Modifier.weight(1f)
                ) {
                    composable(MediGuideRoutes.ROUTE_DASHBOARD) {
                        HomeDashboardScreen(
                            language = localPrefs.language,
                            isAdmin = isAdmin,
                            medicinesState = medicinesState,
                            priceOffersState = priceOffersState,
                            categoriesState = categoriesState,
                            favoriteIds = favoriteIds,
                            recentlyViewed = recentlyViewed,
                            onSelectLanguage = sessionViewModel::setLanguage,
                            onOpenSearch = { navController.navigate(MediGuideRoutes.ROUTE_SEARCH) },
                            onOpenScanner = { navController.navigate(MediGuideRoutes.ROUTE_SCANNER) },
                            onOpenVoiceSearch = ::launchHomeVoiceSearch,
                            onOpenCategories = { navController.navigate(MediGuideRoutes.ROUTE_CATEGORIES) },
                            onCategorySelected = { catId ->
                                catalogViewModel.selectCategory(catId)
                                navController.navigate(MediGuideRoutes.ROUTE_SEARCH)
                            },
                            onOpenMedicineDetail = { medId ->
                                catalogViewModel.clearFdaLookup()
                                navController.navigate(MediGuideRoutes.detail(medId))
                            },
                            onOpenPriceCompare = { medId ->
                                navController.navigate(MediGuideRoutes.compare(medId))
                            },
                            onOpenFavorites = { navController.navigate(MediGuideRoutes.ROUTE_FAVORITES) },
                            onOpenSafetyChecker = { navController.navigate(MediGuideRoutes.safetyChecker(false)) },
                            onOpenInteractionChecker = { navController.navigate(MediGuideRoutes.safetyChecker(true)) },
                            onOpenAdmin = { navController.navigate(MediGuideRoutes.ROUTE_ADMIN) },
                            onToggleFavorite = sessionViewModel::toggleFavorite
                        )
                    }

                    composable(MediGuideRoutes.ROUTE_SEARCH) {
                        val filtered = catalogViewModel.filterMedicines(
                            medicines = medicines,
                            categories = categories,
                            query = searchQuery,
                            categoryId = selectedCategoryId
                        )
                        MedicineSearchScreen(
                            language = localPrefs.language,
                            query = searchQuery,
                            selectedCategoryId = selectedCategoryId,
                            medicines = medicines,
                            filteredMedicines = filtered,
                            categories = categories,
                            priceOffers = priceOffers,
                            favoriteIds = favoriteIds,
                            searchHistory = searchHistory,
                            onQueryChange = catalogViewModel::updateSearchQuery,
                            onSubmitSearch = sessionViewModel::recordSearch,
                            onSelectCategory = catalogViewModel::selectCategory,
                            onClearHistory = sessionViewModel::clearSearchHistory,
                            onOpenScanner = { navController.navigate(MediGuideRoutes.ROUTE_SCANNER) },
                            onOpenMedicineDetail = { medId ->
                                catalogViewModel.clearFdaLookup()
                                navController.navigate(MediGuideRoutes.detail(medId))
                            },
                            onOpenPriceCompare = { medId ->
                                navController.navigate(MediGuideRoutes.compare(medId))
                            },
                            onToggleFavorite = sessionViewModel::toggleFavorite,
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(MediGuideRoutes.ROUTE_SCANNER) {
                        MedicineScannerScreen(
                            language = localPrefs.language,
                            medicines = medicines,
                            findBestMatch = catalogViewModel::findBestMatchingMedicineFromScan,
                            onScanMatchedQuery = { scanned ->
                                catalogViewModel.updateSearchQuery(scanned)
                                sessionViewModel.recordSearch(scanned, "SCAN")
                                navController.navigate(MediGuideRoutes.ROUTE_SEARCH)
                            },
                            onOpenMedicineDetail = { medId ->
                                catalogViewModel.clearFdaLookup()
                                sessionViewModel.recordSearch(medId, "SCAN")
                                navController.navigate(MediGuideRoutes.detail(medId))
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(
                        route = MediGuideRoutes.ROUTE_DETAIL,
                        arguments = listOf(navArgument("medicineId") { type = NavType.StringType })
                    ) { entry ->
                        val medId = entry.arguments?.getString("medicineId").orEmpty()
                        val med = medicines.firstOrNull { it.id == medId }
                        val offers = priceOffers.filter { it.medicineId == medId }
                        MedicineDetailScreen(
                            medicine = med,
                            allMedicines = medicines,
                            priceOffersForMedicine = offers,
                            language = localPrefs.language,
                            isFavorite = medId in favoriteIds,
                            fdaLiveState = fdaLiveState,
                            onRecordViewed = sessionViewModel::recordMedicineView,
                            onToggleFavorite = sessionViewModel::toggleFavorite,
                            onComparePrices = { id -> navController.navigate(MediGuideRoutes.compare(id)) },
                            onOpenSideEffects = { id -> navController.navigate(MediGuideRoutes.sideEffects(id)) },
                            onOpenAlternativeMedicine = { id ->
                                catalogViewModel.clearFdaLookup()
                                navController.navigate(MediGuideRoutes.detail(id))
                            },
                            onQuickAddReminder = { target ->
                                navController.navigate(MediGuideRoutes.reminders(target.id))
                            },
                            onFetchOpenFda = catalogViewModel::lookupFdaLabel,
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(
                        route = MediGuideRoutes.ROUTE_SIDE_EFFECTS,
                        arguments = listOf(navArgument("medicineId") { type = NavType.StringType })
                    ) { entry ->
                        val medId = entry.arguments?.getString("medicineId").orEmpty()
                        val med = medicines.firstOrNull { it.id == medId }
                        SideEffectsScreen(
                            medicine = med,
                            language = localPrefs.language,
                            onOpenDetails = { id -> navController.navigate(MediGuideRoutes.detail(id)) },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(
                        route = MediGuideRoutes.ROUTE_COMPARE,
                        arguments = listOf(navArgument("medicineId") {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        })
                    ) { entry ->
                        val initialMedId = entry.arguments?.getString("medicineId")
                        PriceComparisonScreen(
                            language = localPrefs.language,
                            initialMedicineId = initialMedId,
                            medicines = medicines,
                            priceOffers = priceOffers,
                            onLogCompare = sessionViewModel::logCompare,
                            onOpenMedicineDetail = { id -> navController.navigate(MediGuideRoutes.detail(id)) }
                        )
                    }

                    composable(MediGuideRoutes.ROUTE_CATEGORIES) {
                        CategoriesScreen(
                            language = localPrefs.language,
                            categories = categories,
                            medicines = medicines,
                            onSelectCategory = { catId ->
                                catalogViewModel.selectCategory(catId)
                                navController.navigate(MediGuideRoutes.ROUTE_SEARCH)
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(
                        route = MediGuideRoutes.ROUTE_REMINDERS,
                        arguments = listOf(navArgument("medicineId") {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        })
                    ) { entry ->
                        val prefillMedId = entry.arguments?.getString("medicineId")
                        RemindersScreen(
                            language = localPrefs.language,
                            medicines = medicines,
                            reminders = reminders,
                            prefillMedicineId = prefillMedId,
                            onSaveReminder = { sessionViewModel.saveReminder(it) },
                            onDeleteReminder = sessionViewModel::deleteReminder
                        )
                    }

                    composable(MediGuideRoutes.ROUTE_FAVORITES) {
                        FavoritesScreen(
                            language = localPrefs.language,
                            favorites = favorites,
                            medicines = medicines,
                            priceOffers = priceOffers,
                            onOpenMedicineDetail = { id -> navController.navigate(MediGuideRoutes.detail(id)) },
                            onOpenPriceCompare = { id -> navController.navigate(MediGuideRoutes.compare(id)) },
                            onToggleFavorite = sessionViewModel::toggleFavorite,
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(MediGuideRoutes.ROUTE_PROFILE) {
                        ProfileScreen(
                            profile = profile,
                            displayName = user.displayName ?: "MediGuide User",
                            email = user.email.orEmpty(),
                            isAdmin = isAdmin,
                            favoritesCount = favorites.size,
                            remindersCount = reminders.size,
                            onOpenFavorites = { navController.navigate(MediGuideRoutes.ROUTE_FAVORITES) },
                            onOpenSettings = { navController.navigate(MediGuideRoutes.ROUTE_SETTINGS) },
                            onOpenLanguageScreen = { navController.navigate(MediGuideRoutes.ROUTE_LANGUAGE) },
                            onOpenSafetyChecker = { navController.navigate(MediGuideRoutes.safetyChecker(false)) },
                            onOpenAdmin = { navController.navigate(MediGuideRoutes.ROUTE_ADMIN) },
                            onOpenAbout = { navController.navigate(MediGuideRoutes.ROUTE_ABOUT) },
                            onOpenPrivacy = { navController.navigate(MediGuideRoutes.ROUTE_PRIVACY) },
                            onOpenDisclaimer = { navController.navigate(MediGuideRoutes.ROUTE_DISCLAIMER) },
                            onSignOut = onSignOut
                        )
                    }

                    composable(MediGuideRoutes.ROUTE_SETTINGS) {
                        SettingsScreen(
                            language = localPrefs.language,
                            themeMode = localPrefs.themeMode,
                            onSelectLanguage = sessionViewModel::setLanguage,
                            onSelectThemeMode = sessionViewModel::setThemeMode,
                            onOpenAbout = { navController.navigate(MediGuideRoutes.ROUTE_ABOUT) },
                            onOpenPrivacy = { navController.navigate(MediGuideRoutes.ROUTE_PRIVACY) },
                            onOpenDisclaimer = { navController.navigate(MediGuideRoutes.ROUTE_DISCLAIMER) },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(MediGuideRoutes.ROUTE_LANGUAGE) {
                        LanguageSelectionScreen(
                            selectedLanguage = localPrefs.language,
                            onSelectLanguage = sessionViewModel::setLanguage,
                            onContinue = { navController.popBackStack() }
                        )
                    }

                    composable(MediGuideRoutes.ROUTE_ABOUT) {
                        LegalInfoScreen(LegalDocType.ABOUT) { navController.popBackStack() }
                    }
                    composable(MediGuideRoutes.ROUTE_PRIVACY) {
                        LegalInfoScreen(LegalDocType.PRIVACY) { navController.popBackStack() }
                    }
                    composable(MediGuideRoutes.ROUTE_DISCLAIMER) {
                        LegalInfoScreen(LegalDocType.DISCLAIMER) { navController.popBackStack() }
                    }

                    composable(
                        route = MediGuideRoutes.ROUTE_SAFETY_CHECKER,
                        arguments = listOf(navArgument("interaction") {
                            type = NavType.BoolType
                            defaultValue = false
                        })
                    ) { entry ->
                        val interactionMode = entry.arguments?.getBoolean("interaction") ?: false
                        SafetyAndInteractionCheckerScreen(
                            medicines = medicines,
                            language = localPrefs.language,
                            initialModeInteraction = interactionMode,
                            onOpenMedicineDetail = { id -> navController.navigate(MediGuideRoutes.detail(id)) },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(MediGuideRoutes.ROUTE_ADMIN) {
                        AdminDashboardScreen(
                            isAdmin = isAdmin,
                            busy = adminBusy,
                            adminBanner = adminBanner,
                            language = localPrefs.language,
                            medicines = medicines,
                            priceOffers = priceOffers,
                            categories = categories,
                            manufacturers = manufacturers,
                            translations = catalogViewModel.translationsState.collectAsStateWithLifecycle().value,
                            apiConnections = apiConnections,
                            analyticsEvents = analyticsEvents,
                            onDismissBanner = adminViewModel::dismissBanner,
                            onSeedCatalog = adminViewModel::seedVerifiedCatalog,
                            onSaveMedicine = adminViewModel::saveMedicine,
                            onDeleteMedicine = adminViewModel::deleteMedicine,
                            onSavePriceOffer = adminViewModel::savePriceOffer,
                            onDeletePriceOffer = adminViewModel::deletePriceOffer,
                            onSaveCategory = adminViewModel::saveCategory,
                            onSaveManufacturer = adminViewModel::saveManufacturer,
                            onSaveTranslation = adminViewModel::saveTranslation,
                            onSaveAndTestApi = adminViewModel::saveAndTestApiConnection,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
