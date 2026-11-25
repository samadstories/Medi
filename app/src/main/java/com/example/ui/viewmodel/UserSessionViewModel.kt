package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.FavoriteMedicine
import com.example.data.model.Medicine
import com.example.data.model.MedicineReminder
import com.example.data.model.RecentlyViewed
import com.example.data.model.SearchHistoryEntry
import com.example.data.model.ThemeMode
import com.example.data.model.UserProfile
import com.example.data.repository.LocalUserPrefs
import com.example.data.repository.UserDataRepository
import com.example.data.repository.UserPreferencesRepository
import com.example.i18n.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class UserSessionViewModel(
    private val uid: String,
    private val userDataRepository: UserDataRepository,
    private val prefsRepository: UserPreferencesRepository
) : ViewModel() {

    val localPrefs: StateFlow<LocalUserPrefs> = prefsRepository.preferencesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LocalUserPrefs())

    val isAdmin: StateFlow<Boolean> = userDataRepository.observeIsAdmin(uid)
        .catch { e ->
            if (e is CancellationException) throw e
            emit(false)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val userProfile: StateFlow<UserProfile?> = userDataRepository.observeProfile(uid)
        .catch { e ->
            if (e is CancellationException) throw e
            emit(null)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val favorites: StateFlow<List<FavoriteMedicine>> = userDataRepository.observeFavorites(uid)
        .catch { e ->
            if (e is CancellationException) throw e
            emit(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentlyViewed: StateFlow<List<RecentlyViewed>> = userDataRepository.observeRecentlyViewed(uid)
        .catch { e ->
            if (e is CancellationException) throw e
            emit(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val searchHistory: StateFlow<List<SearchHistoryEntry>> = userDataRepository.observeSearchHistory(uid)
        .catch { e ->
            if (e is CancellationException) throw e
            emit(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reminders: StateFlow<List<MedicineReminder>> = userDataRepository.observeReminders(uid)
        .catch { e ->
            if (e is CancellationException) throw e
            emit(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    init {
        viewModelScope.launch {
            val prefs = localPrefs.value
            userDataRepository.ensureUserProfile(
                language = prefs.language,
                themeMode = prefs.themeMode,
                onboardingCompleted = prefs.onboardingCompleted
            )
        }
    }

    fun clearStatusMessage() {
        _statusMessage.value = null
    }

    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch {
            prefsRepository.setLanguage(language)
            val current = localPrefs.value
            userDataRepository.updateProfilePreferences(
                language = language,
                themeMode = current.themeMode,
                onboardingCompleted = current.onboardingCompleted
            )
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            prefsRepository.setThemeMode(mode)
            val current = localPrefs.value
            userDataRepository.updateProfilePreferences(
                language = current.language,
                themeMode = mode,
                onboardingCompleted = current.onboardingCompleted
            )
        }
    }

    fun setOnboardingCompleted() {
        viewModelScope.launch {
            prefsRepository.setOnboardingCompleted(true)
            val current = localPrefs.value
            userDataRepository.updateProfilePreferences(
                language = current.language,
                themeMode = current.themeMode,
                onboardingCompleted = true
            )
        }
    }

    fun toggleFavorite(medicine: Medicine, currentlyFavorite: Boolean) {
        viewModelScope.launch {
            userDataRepository.toggleFavorite(medicine, currentlyFavorite)
                .onFailure { _statusMessage.value = it.message }
        }
    }

    fun recordMedicineView(medicine: Medicine) {
        viewModelScope.launch {
            userDataRepository.recordMedicineView(medicine)
        }
    }

    fun recordSearch(query: String, searchType: String = "TEXT") {
        if (query.isBlank()) return
        viewModelScope.launch {
            userDataRepository.addSearchHistory(query, searchType)
        }
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            userDataRepository.clearSearchHistory(searchHistory.value)
        }
    }

    fun saveReminder(reminder: MedicineReminder, onSaved: () -> Unit = {}) {
        viewModelScope.launch {
            userDataRepository.saveReminder(reminder)
                .onSuccess { onSaved() }
                .onFailure { _statusMessage.value = it.message }
        }
    }

    fun deleteReminder(reminderId: String) {
        viewModelScope.launch {
            userDataRepository.deleteReminder(reminderId)
                .onFailure { _statusMessage.value = it.message }
        }
    }

    fun logCompare(medicineId: String) {
        viewModelScope.launch {
            userDataRepository.logAnalyticsEvent("COMPARE", medicineId)
        }
    }

    class Factory(
        private val uid: String,
        private val userDataRepository: UserDataRepository,
        private val prefsRepository: UserPreferencesRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UserSessionViewModel(uid, userDataRepository, prefsRepository) as T
        }
    }
}
