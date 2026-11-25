package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.auth.GoogleAuthHelper
import com.example.data.remote.FirestoreProvider
import com.example.data.repository.CatalogRepository
import com.example.data.repository.LocalUserPrefs
import com.example.data.repository.UserDataRepository
import com.example.data.repository.UserPreferencesRepository
import com.example.i18n.LocalStrings
import com.example.i18n.resolveStrings
import com.example.reminders.ReminderReceiver
import com.example.ui.navigation.MediGuideAuthenticatedApp
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.LanguageSelectionScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.MediGuideTheme
import com.example.ui.viewmodel.AdminViewModel
import com.example.ui.viewmodel.CatalogViewModel
import com.example.ui.viewmodel.UserSessionViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

private enum class StartupStage {
    SPLASH, LANGUAGE, ONBOARDING, READY
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ReminderReceiver.ensureChannel(applicationContext)

        val firestore = FirestoreProvider.get(applicationContext)
        val firebaseAuth = Firebase.auth
        val prefsRepository = UserPreferencesRepository(applicationContext)
        val catalogRepository = CatalogRepository(firestore, firebaseAuth)
        val userDataRepository = UserDataRepository(firestore, firebaseAuth)

        setContent {
            val scope = rememberCoroutineScope()
            val prefs by prefsRepository.preferencesFlow.collectAsStateWithLifecycle(initialValue = LocalUserPrefs())
            val authHelper = remember(this) { GoogleAuthHelper(this@MainActivity) }

            var currentUser by remember { mutableStateOf<FirebaseUser?>(firebaseAuth.currentUser) }
            var startupStage by remember { mutableStateOf(StartupStage.SPLASH) }
            var isSigningIn by remember { mutableStateOf(false) }
            var authError by remember { mutableStateOf<String?>(null) }

            DisposableEffect(firebaseAuth) {
                val listener = FirebaseAuth.AuthStateListener { auth ->
                    currentUser = auth.currentUser
                }
                firebaseAuth.addAuthStateListener(listener)
                onDispose {
                    firebaseAuth.removeAuthStateListener(listener)
                }
            }

            LaunchedEffect(startupStage, currentUser) {
                if (startupStage == StartupStage.READY && currentUser == null) {
                    authHelper.attemptAutoSignIn()
                }
            }

            val baseStrings = remember(prefs.language) {
                resolveStrings(prefs.language)
            }

            MediGuideTheme(themeMode = prefs.themeMode) {
                CompositionLocalProvider(LocalStrings provides baseStrings) {
                    when (startupStage) {
                        StartupStage.SPLASH -> {
                            SplashScreen(
                                onFinished = {
                                    startupStage = if (prefs.onboardingCompleted) {
                                        StartupStage.READY
                                    } else {
                                        StartupStage.LANGUAGE
                                    }
                                }
                            )
                        }

                        StartupStage.LANGUAGE -> {
                            LanguageSelectionScreen(
                                selectedLanguage = prefs.language,
                                onSelectLanguage = { lang ->
                                    scope.launch { prefsRepository.setLanguage(lang) }
                                },
                                onContinue = { startupStage = StartupStage.ONBOARDING }
                            )
                        }

                        StartupStage.ONBOARDING -> {
                            OnboardingScreen(
                                onFinish = {
                                    scope.launch { prefsRepository.setOnboardingCompleted(true) }
                                    startupStage = StartupStage.READY
                                }
                            )
                        }

                        StartupStage.READY -> {
                            val user = currentUser
                            if (user == null) {
                                AuthScreen(
                                    isSigningIn = isSigningIn,
                                    errorMessage = authError,
                                    selectedLanguage = prefs.language,
                                    onSelectLanguage = { lang ->
                                        scope.launch { prefsRepository.setLanguage(lang) }
                                    },
                                    onGoogleSignInClick = {
                                        scope.launch {
                                            isSigningIn = true
                                            authError = null
                                            authHelper.signInWithGoogleButton()
                                                .onFailure { err ->
                                                    authError = err.message ?: "Google Sign-In failed."
                                                }
                                            isSigningIn = false
                                        }
                                    }
                                )
                            } else {
                                key(user.uid) {
                                    val catalogViewModel: CatalogViewModel = viewModel(
                                        key = "catalog_${user.uid}",
                                        factory = CatalogViewModel.Factory(catalogRepository)
                                    )
                                    val sessionViewModel: UserSessionViewModel = viewModel(
                                        key = "session_${user.uid}",
                                        factory = UserSessionViewModel.Factory(
                                            uid = user.uid,
                                            userDataRepository = userDataRepository,
                                            prefsRepository = prefsRepository
                                        )
                                    )
                                    val adminViewModel: AdminViewModel = viewModel(
                                        key = "admin_${user.uid}",
                                        factory = AdminViewModel.Factory(catalogRepository)
                                    )

                                    val translationOverrides by catalogViewModel.translationsState.collectAsStateWithLifecycle()
                                    val resolvedStrings = remember(prefs.language, translationOverrides) {
                                        resolveStrings(prefs.language, translationOverrides)
                                    }

                                    CompositionLocalProvider(LocalStrings provides resolvedStrings) {
                                        MediGuideAuthenticatedApp(
                                            user = user,
                                            catalogViewModel = catalogViewModel,
                                            sessionViewModel = sessionViewModel,
                                            adminViewModel = adminViewModel,
                                            onSignOut = {
                                                scope.launch {
                                                    authHelper.signOut()
                                                }
                                            }
                                        )
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
